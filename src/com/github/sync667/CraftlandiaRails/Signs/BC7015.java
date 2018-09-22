package com.github.sync667.CraftlandiaRails.Signs;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRails.AddressLayer.ReturnAddressFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;

/**
 * A return address setter
 */
final class BC7015 extends BC7011 implements Triggable{

    BC7015(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see BC7010#getTargetAddress()
     */
    @Override
    protected AddressRouted getTargetAddress() {
        return ReturnAddressFactory.getAddress(this.getInventory());
    }

    /* (non-Javadoc)
     * @see BC7010#getIsTrain()
     */
    @Override
    protected final boolean getIsTrain() {
        Address address;
		if ((address = AddressFactory.getAddress(this.getInventory())) != null) {
			return address.isTrain();
		}
        return false;
    }

    /* (non-Javadoc)
     * @see BC7011#getName()
     */
    @Override
    public String getName() {
        return "BC7015";
    }

    /* (non-Javadoc)
     * @see BC7011#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Set Return";
    }

    /* (non-Javadoc)
     * @see BC7010#forceTicketReuse()
     */
    @Override
    protected boolean forceTicketReuse() {
        return true;
    }

    /* (non-Javadoc)
     * @see BC7010#infoPlayer(com.github.catageek.CraftlandiaRails.AddressLayer.Address)
     */
    @Override
    protected void infoPlayer(String address) {
        ((Player) this.getInventory().getHolder()).sendMessage(
                ChatColor.DARK_GREEN + "[CraftlandiaRails] " + ChatColor.YELLOW +
                        CraftlandiaRails.myPlugin.getConfig().getString("Info.SetReturnAddress") + " " + ChatColor.RED +
                        address);
    }
}
