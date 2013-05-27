package com.github.catageek.ByteCart.Signs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.catageek.ByteCart.ByteCart;
import com.github.catageek.ByteCart.AddressLayer.Address;
import com.github.catageek.ByteCart.AddressLayer.AddressFactory;
import com.github.catageek.ByteCart.AddressLayer.AddressRouted;
import com.github.catageek.ByteCart.AddressLayer.ReturnAddressFactory;

final class BC7015 extends BC7011 implements Triggable {

	BC7015(org.bukkit.block.Block block,
			org.bukkit.entity.Vehicle vehicle) {
		super(block, vehicle);
	}

	protected AddressRouted getTargetAddress() {
		return ReturnAddressFactory.getAddress(this.getInventory());
	}
	
	@Override
	protected final boolean getIsTrain() {
		Address address;
		if((address = AddressFactory.getAddress(this.getInventory())) != null)
			return address.isTrain();
		return false;
	}

	@Override
	public String getName() {
		return "BC7015";
	}

	@Override
	public String getFriendlyName() {
		return "Set Return";
	}

	@Override
	protected boolean forceTicketReuse() {
		return true;
	}
	
	protected void infoPlayer(Address address) {
		((Player) this.getInventory().getHolder()).sendMessage(ChatColor.DARK_GREEN+"[Bytecart] " + ChatColor.YELLOW + ByteCart.myPlugin.getConfig().getString("Info.SetReturnAddress") + " (" + ChatColor.RED + address + ")");
	}
}
