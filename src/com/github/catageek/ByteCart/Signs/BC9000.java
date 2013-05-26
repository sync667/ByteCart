package com.github.catageek.ByteCart.Signs;

import java.io.IOException;

import com.github.catageek.ByteCart.CollisionManagement.SimpleCollisionAvoider;
import com.github.catageek.ByteCart.HAL.PinRegistry;
import com.github.catageek.ByteCart.IO.OutputPin;
import com.github.catageek.ByteCart.IO.OutputPinFactory;
import com.github.catageek.ByteCart.Routing.UpdaterFactory;
import com.github.catageek.ByteCart.Routing.UpdaterLocal;
import com.github.catageek.ByteCart.Util.MathUtil;


final class BC9000 extends AbstractBC9000 implements Subnet, Triggable {

	BC9000(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
		this.netmask = 0;
	}

	@Override
	protected SimpleCollisionAvoider.Side route() {
		return SimpleCollisionAvoider.Side.RIGHT;
	}
	
	@Override
	protected void manageUpdater(SimpleCollisionAvoider intersection) {
		// it's an updater, so let it choosing direction
		UpdaterLocal updater;
		try {
			updater = (UpdaterLocal) UpdaterFactory.getUpdater(this, this.getInventory());
			// routing
			intersection.WishToGo(route(), false);

			// here we perform routes update
			updater.leaveSubnet();
			updater.save();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void addIO() {
		// Output[0] = 2 bits registry representing levers on the left and on the right of the sign
		OutputPin[] lever2 = new OutputPin[2];

		// Left
		lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
		// Right
		lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

		PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

		this.addOutputRegistry(command1);
	}

	@Override
	public String getName() {
		return "BC9000";
	}

	@Override
	public String getFriendlyName() {
		return "Collision avoider";
	}
}
