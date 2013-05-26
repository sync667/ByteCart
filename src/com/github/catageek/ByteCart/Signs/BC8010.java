package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.ReturnAddressFactory;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.Router;
import com.github.catageek.ByteCart.CollisionManagement.RouterCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.Event.SignPostRouteEvent;
import com.github.catageek.ByteCart.Event.UpdaterPassRouterEvent;
import com.github.catageek.ByteCart.Event.SignPreRouteEvent;
import com.github.catageek.ByteCart.Routing.DefaultRouterWanderer;
import com.github.catageek.ByteCart.Routing.RoutingTable;
import com.github.catageek.ByteCart.Routing.UpdaterContentFactory;
import com.github.catageek.ByteCart.Routing.RoutingTableFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Util.DirectionRegistry;
import com.github.catageek.ByteCart.Util.MathUtil;



public class BC8010 extends AbstractTriggeredSign implements BCRouter, Triggable, HasRoutingTable {

	private final BlockFace From;
	private final Address Sign;
	private final RoutingTable RoutingTable;
	private AddressRouted destination;
	private final Block center;
	protected boolean IsTrackNumberProvider;

	BC8010(Block block, org.bukkit.entity.Vehicle vehicle) throws ClassNotFoundException, IOException {
		super(block, vehicle);
		this.IsTrackNumberProvider = true;
		From = this.getCardinal().getOppositeFace();
		// reading destination address of the cart
		destination = AddressFactory.getAddress(this.getInventory());
		// reading address written on BC8010 sign
		Sign = AddressFactory.getAddress(this.getBlock(),3);
		// Center of the router, at sign level
		center = this.getBlock().getRelative(this.getCardinal(), 6).getRelative(MathUtil.clockwise(this.getCardinal()));

		BlockState blockstate;

		if ((blockstate = center.getRelative(BlockFace.UP, 5).getState()) instanceof InventoryHolder) {
			// Loading inventory of chest above router
			Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

			// Converting inventory in routing table
			RoutingTable = RoutingTableFactory.getRoutingTable(ChestInventory);
		}
		else {
			RoutingTable = null;
		}
	}

	@Override
	public void trigger() throws ClassNotFoundException, IOException {

		CollisionAvoiderBuilder builder = new RouterCollisionAvoiderBuilder(this, center.getLocation());

		try {

			BlockFace direction, to;
			Router router = ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder);
			boolean isTrain = AbstractTriggeredSign.isTrain(destination);

			// Here begins the triggered action

			// is this an updater who needs special routing ? no then routing normally
			if(selectUpdater()) {
				//				UpdaterManager um = ByteCart.myPlugin.getUm();

				// non updater carts case
				//				if (! um.isUpdater(vehicle.getEntityId())) {
				//				if (! RoutingTableExchangeFactory.isRoutingTableExchange(getInventory(), Sign.getRegion().getAmount(), this.getLevel())) {

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {

					// leave a message to next cart that it is a train
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getLocation());
					// tell to router not to change position
					ByteCart.myPlugin.getCollisionAvoiderManager().<Router>getCollisionAvoider(builder).Book(isTrain);
					return;
				}

				if (destination != null) {
					// Time-to-live management

					//loading TTl of cart
					int ttl = destination.getTTL();

					// if ttl did not reach end of life ( = 0)
					if (ttl != 0) {

						destination.updateTTL(ttl-1);
					}

					// if ttl was 1 (now 0), we try to return the cart to source station

					if (ttl == 1 && tryReturnCart())
						destination = AddressFactory.getAddress(this.getInventory());

					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : TTL is " + destination.getTTL());


					// if this is the first car of a train
					// we keep it during 2 s
					if (isTrain) {
						this.setWasTrain(this.getLocation(), true);
					}

					destination.finalizeAddress();
				}

				direction = this.SelectRoute(destination, Sign, RoutingTable);
				/*			}
							else {
					// is an updater (this code is executed only by BC8020)
					int region = RoutingTableExchangeFactory.getRoutingTableExchange(getInventory(), Sign.getRegion().getAmount(), this.getLevel()).;
					if(ByteCart.debug)
						ByteCart.log.info("ByteCart : region " + region);
					try {
						direction = RoutingTable.getDirection(region).getBlockFace();
					} catch (NullPointerException e) {
						// this region does not exist
						direction = From;
						// remove the cart as updater
						//um.getMapRoutes().remove(vehicle.getEntityId());
						RoutingTableExchangeFactory.deleteRoutingTableExchange(getInventory());
					}
				}
				 */
				// trigger event
				BlockFace bdest = router.WishToGo(From, direction, isTrain);
				int ring = this.getRoutingTable().getDirectlyConnected(new DirectionRegistry(bdest));
				SignPostRouteEvent event = new SignPostRouteEvent(this, ring);
				Bukkit.getServer().getPluginManager().callEvent(event);

				return;
			}

			// it's an updater, so let it choosing direction
			Updater updater = getUpdater();

			// routing normally
			to = router.WishToGo(From, updater.giveRouterDirection(), isTrain);

			int nextring = this.getRoutingTable().getDirectlyConnected(new DirectionRegistry(to));
			UpdaterPassRouterEvent event = new UpdaterPassRouterEvent(updater, to, nextring);
			Bukkit.getServer().getPluginManager().callEvent(event);

			// here we perform routes update
			updater.doAction(to);

		}
		catch (ClassCastException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : " + e.toString());
			e.printStackTrace();

			// Not the good blocks to build the signs
			return;
		}
		catch (NullPointerException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : "+ e.toString());

			e.printStackTrace();

			// there was no inventory in the cart
			return;
		}




	}

	/**
	 * Tells if this cart needs normal routing
	 * @param id: id of the cart
	 * @return: true if the cart needs normal routing
	 */
	protected boolean selectUpdater() {
		// everything that is not an updater must be routed
		//return ! ByteCart.myPlugin.getUm().isUpdater(id);
		return ! UpdaterContentFactory.isRoutingTableExchange(getInventory());
	}

	protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTable RoutingTable) {

		DirectionRegistry face;
		// same region : lookup destination track
		if (IPaddress != null && IPaddress.getRegion().getAmount() == sign.getRegion().getAmount() && IPaddress.getTTL() != 0) {
			int destination = this.destination.getTrack().getAmount();
			DirectionRegistry out = RoutingTable.getDirection(destination);
			if (out != null) {
				// trigger event
				SignPreRouteEvent event = new SignPreRouteEvent(this, this.getRoutingTable().getDirectlyConnected(out));
				Bukkit.getServer().getPluginManager().callEvent(event);
				return out.getBlockFace();
			}
		}

		// If not in same region, or if TTL is 0, or the ring does not exist then we lookup track 0
		if ((face = RoutingTable.getDirection(0)) != null)
			return face.getBlockFace();

		// If everything has failed, then we randomize output direction
		return DefaultRouterWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());
	}

	private boolean tryReturnCart() {
		Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());
		if (returnAddress != null && returnAddress.isReturnable()) {
			(new BC7017(this.getBlock(), this.getVehicle())).trigger();
			return true;
		}
		return false;
	}

	protected final Updater getUpdater() throws ClassNotFoundException, IOException {
		return UpdaterFactory.getUpdater(this, this.getInventory());
	}


	public Updater.Level getLevel() {
		return Updater.Level.REGION;
	}

	public final BlockFace getFrom() {
		return From;
	}

	public final Address getSignAddress() {
		return Sign;
	}

	public final RoutingTable getRoutingTable() {
		return RoutingTable;
	}

	public final boolean isTrackNumberProvider() {
		return IsTrackNumberProvider;
	}

	public final String getDestinationIP() {
		return destination.toString();
	}

	public final int getOriginTrack() {
		return Sign.getTrack().getAmount();
	}

	public final Block getCenter() {
		return center;
	}

	@Override
	public String getName() {
		return "BC8010";
	}

	@Override
	public String getFriendlyName() {
		return "L1 Router";
	}
}
