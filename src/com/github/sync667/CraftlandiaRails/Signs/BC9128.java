package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRailsAPI.Signs.HasNetmask;
import com.github.sync667.CraftlandiaRailsAPI.Signs.Subnet;

/**
 * A 128-station subnet bound
 */
final class BC9128 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable{

    BC9128(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 1;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public final String getName() {
        return "BC9128";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "128-station subnet";
    }
}