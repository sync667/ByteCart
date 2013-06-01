package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import org.bukkit.Bukkit;


import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.CollisionManagement.CollisionAvoiderBuilder;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider.Side;
import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoiderBuilder;
import com.github.catageek.ByteCart.Event.SignPostSubnetEvent;
import com.github.catageek.ByteCart.Event.SignPreSubnetEvent;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.HAL.RegistryBoth;
import com.github.catageek.ByteCart.HAL.RegistryInput;
import com.github.catageek.ByteCart.HAL.SubRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.UpdaterContentFactory;
import com.github.catageek.ByteCart.Routing.Updater;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Util.MathUtil;


abstract class AbstractBC9000 extends AbstractTriggeredSign implements Subnet,HasNetmask {

	protected int netmask;

	protected CollisionAvoiderBuilder builder;

	private Address destination;

	AbstractBC9000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		builder = new SimpleCollisionAvoiderBuilder((Triggable) this, block.getRelative(this.getCardinal(), 3).getLocation());
		/*		if(ByteCart.debug)
			ByteCart.log.info("ByteCart : SimpleCollisionAvoiderBuilder(" + block.getRelative(this.getCardinal(), 3).getLocation()+")");
		 */	}

	public void trigger() {
		try {

			this.addIO();

			SimpleCollisionAvoider intersection = ByteCart.myPlugin.getCollisionAvoiderManager().<SimpleCollisionAvoider>getCollisionAvoider(builder);

			if (! UpdaterContentFactory.isRoutingTableExchange(getInventory())) {

				boolean isTrain = AbstractTriggeredSign.isTrain(getDestinationAddress());

				// if this is a cart in a train
				if (this.wasTrain(this.getLocation())) {
					ByteCart.myPlugin.getIsTrainManager().getMap().reset(getBlock().getLocation());
					intersection.Book(isTrain);
					return;
				}

				// if this is the first car of a train
				// we keep it during 2 s
				if (isTrain) {
					this.setWasTrain(this.getLocation(), true);
				}

				Side result = intersection.WishToGo(this.route(), isTrain);
				SignPostSubnetEvent event = new SignPostSubnetEvent(this, result);
				Bukkit.getServer().getPluginManager().callEvent(event);
				return;
			}

			manageUpdater(intersection);

		}
		catch (ClassCastException e) {
			if(ByteCart.debug)
				ByteCart.log.info("ByteCart : " + e.toString());

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

	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// it's an updater, so let it choosing direction
		Updater updater;
		try {
			updater = UpdaterFactory.getUpdater(this, this.getInventory());

			// routing
			Side to = intersection.WishToGo(updater.giveSimpleDirection(), false);

			// here we perform routes update
			updater.doAction(to);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected SimpleCollisionAvoider.Side route() {
		SignPreSubnetEvent event;
		if (this.isAddressMatching())
			event = new SignPreSubnetEvent(this, Side.RIGHT);
		else
			event = new SignPreSubnetEvent(this, Side.LEFT);

		Bukkit.getServer().getPluginManager().callEvent(event);
		return event.getSide();
	}

	protected final RegistryBoth applyNetmask(RegistryBoth station) {
		if (this.netmask < station.length())
			return new SubRegistry<RegistryBoth>(station, this.netmask, 0);
		return station;
	}

	protected boolean isAddressMatching() {
		try {
			return this.getInput(2).getAmount() == this.getInput(5).getAmount()
					&& this.getInput(1).getAmount() == this.getInput(4).getAmount()
					&& this.getInput(0).getAmount() == this.getInput(3).getAmount();
		} catch (NullPointerException e) {
			// there is no address on sign
		}
		return false;
	}


	public Updater.Level getLevel() {
		return Updater.Level.LOCAL;
	}

	/**
	 * Configures all IO ports of this sign.
	 *
	 * The following input pins are configured:
	 * 0: vehicle region
	 * 1: vehicle track
	 * 2: vehicle station (w/ applied net mask)
	 * 3: sign region
	 * 4: sign track
	 * 5: sign station (w/ applied net mask)
	 *
	 * The following output pins are configured:
	 * 0: left lever
	 * 1: right lever
	 */
	protected void addIO() {
		Address sign = this.getSignAddress();


		// Output[0] = 2 bits registry representing levers on the left and on the right of the sign
		OutputPin[] lever2 = new OutputPin[2];

		// Left
		lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

		this.addOutputRegistry(command1);

		// Input[0] = destination region taken from Inventory, slot #0


		Address IPaddress = getDestinationAddress();

		if (IPaddress == null)
			return;

		RegistryInput slot2 = IPaddress.getRegion();


		this.addInputRegistry(slot2);

		// Input[1] = destination track taken from cart, slot #1

		RegistryInput slot1 = IPaddress.getTrack();


		this.addInputRegistry(slot1);

		// Input[2] = destination station taken from cart, slot #2, 6 bits

		RegistryBoth slot0 = IPaddress.getStation();


		// We keep only the X most significant bits (netmask)

		slot0 = applyNetmask(slot0);

		this.addInputRegistry(slot0);


		// Address is on a sign, line #3
		// Input[3] = region from sign, line #3, 6 bits registry
		// Input[4] = track from sign, line #3, 6 bits registry
		// Input[5] = station number from sign, line #0, 6 bits registry
		this.addAddressAsInputs(sign);
	}

	protected void addAddressAsInputs(Address addr) {
		if(addr.isValid()) {
			RegistryInput region = addr.getRegion();
			this.addInputRegistry(region);

			RegistryInput track = addr.getTrack();
			this.addInputRegistry(track);

			RegistryBoth station = addr.getStation();
			station = applyNetmask(station);
			this.addInputRegistry(station);
		}
	}

	public final Address getSignAddress() {
		return AddressFactory.getAddress(getBlock(), 3);
	}

	public final int getNetmask() {
		return netmask;
	}

	protected final Address getDestinationAddress() {
		if (destination != null)
			return destination;
		return destination = AddressFactory.getAddress(this.getInventory());
	}

	@Override
	public final String getDestinationIP() {
		Address ip;
		if ((ip = getDestinationAddress()) != null)
			return ip.toString();
		return "";
	}

	public final org.bukkit.block.Block getCenter() {
		return this.getBlock();
	}
}
