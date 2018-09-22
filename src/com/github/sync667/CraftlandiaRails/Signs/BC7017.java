package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRails.AddressLayer.ReturnAddressFactory;
import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * A block that makes the cart return to its origin using return address
 */
public final class BC7017 extends AbstractTriggeredSign implements Triggable{

    BC7017(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    public BC7017(org.bukkit.block.Block block, Player player) {
        super(block, null);
        this.setInventory(player.getInventory());
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7017";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Return back";
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() {
        Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());

		if (returnAddress == null || ! returnAddress.isReturnable()) {
			return;
		}

        String returnAddressString = returnAddress.toString();
        AddressRouted targetAddress = AddressFactory.getAddress(getInventory());
		if (CraftlandiaRails.debug) {
			CraftlandiaRails.log.info("CraftlandiaRails: 7017 : Writing address " + returnAddressString);
		}
        returnAddress.remove();
        returnAddress.finalizeAddress();
        boolean isTrain = targetAddress.isTrain();
        targetAddress.setAddress(returnAddressString);
        targetAddress.setTrain(isTrain);
		if (this.getInventory().getHolder() instanceof Player) {
			((Player) this.getInventory().getHolder()).sendMessage(
					ChatColor.DARK_GREEN + "[CraftlandiaRails] " + ChatColor.YELLOW +
							CraftlandiaRails.myPlugin.getConfig().getString("Info.SetAddress") + " (" + ChatColor.RED +
							returnAddressString + ")");
		}
        targetAddress.initializeTTL();
        targetAddress.finalizeAddress();

    }
}
