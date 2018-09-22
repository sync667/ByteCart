package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryInput;

/**
 * A region field setter using redstone
 */
final class BC7012 extends BC7013 implements Triggable{

    BC7012(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see BC7013#format(com.github.catageek.CraftlandiaRails.HAL.RegistryInput, AddressRouted)
     */
    @Override
    protected String format(RegistryInput wire, AddressRouted InvAddress) {
        return "" + wire.getAmount() + "." + InvAddress.getTrack().getAmount() + "." +
                InvAddress.getStation().getAmount();
    }

    /* (non-Javadoc)
     * @see BC7013#getName()
     */
    @Override
    public final String getName() {
        return "BC7012";
    }

    /* (non-Javadoc)
     * @see BC7013#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "setRegion";
    }

    /* (non-Javadoc)
     * @see BC7013#forceTicketReuse()
     */
    @Override
    protected boolean forceTicketReuse() {
        return true;
    }

}
