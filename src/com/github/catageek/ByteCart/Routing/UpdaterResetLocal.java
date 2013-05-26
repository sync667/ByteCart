package com.github.catageek.ByteCart.Routing;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.Event.UpdaterClearStationEvent;
import com.github.catageek.ByteCart.Event.UpdaterClearSubnetEvent;
import com.github.catageek.ByteCart.Event.UpdaterSignInvalidateEvent;
import com.github.catageek.ByteCart.Signs.BCSign;
import com.github.catageek.ByteCart.Util.DirectionRegistry;

final class UpdaterResetLocal extends UpdaterLocal implements Updater {

	UpdaterResetLocal(BCSign bc, UpdaterContent rte) {
		super(bc, rte);
	}

	@Override
	public void doAction(BlockFace to) {
		int ring;
		if ((ring = this.getTrackNumber()) != -1) {
			incrementRingCounter(ring);
			save();
		}
	}

	@Override
	public void doAction(Side to) {
		Address address = this.getSignAddress();

		// if we are not in the good region or on ring 0, skip update
		if (address.isValid() && (address.getRegion().getAmount() != getContent().getRegion()
				|| address.getTrack().getAmount() == 0))
			return;

		if (address.isValid() && this.getContent().isFullreset()) {
			if (this.getNetmask() == 8) {
				UpdaterClearStationEvent event = new UpdaterClearStationEvent(this, address);
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
			else {
				UpdaterClearSubnetEvent event = new UpdaterClearSubnetEvent(this, address, 256 >> this.getNetmask());
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}
		else {
			UpdaterSignInvalidateEvent event = new UpdaterSignInvalidateEvent(this);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (! this.getContent().isFullreset())
				return;
		}
		address.remove();
		if (ByteCart.debug)
			ByteCart.log.info("ByteCart: removing address");

		save();
	}

	@Override
	public Side giveSimpleDirection() {
		if (this.getNetmask() < 8)
			return Side.RIGHT;
		return Side.LEFT;
	}

	@Override
	public BlockFace giveRouterDirection() {
		// check if we are in the good region
		if (this.getSignAddress().isValid()
				&& this.getSignAddress().getRegion().getAmount() != getWandererRegion()) {
			// case this is not the right region
			DirectionRegistry dir = RoutingTable.getDirection(getWandererRegion());
			if (dir != null)
				return dir.getBlockFace();
			return this.getFrom().getBlockFace();
		}
		// the route where we went the lesser
		int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, getFrom());
		DirectionRegistry dir;
		if ((dir = RoutingTable.getDirection(preferredroute)) != null)
			return dir.getBlockFace();
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
	}
}
