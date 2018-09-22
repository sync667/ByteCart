package com.github.sync667.CraftlandiaRails.Signs;

import com.github.catageek.ByteCartAPI.Signs.HasNetmask;
import com.github.catageek.ByteCartAPI.Signs.Subnet;


/**
 * A 2-station subnet bound
 */
final class BC9002 extends AbstractBC9000 implements Subnet, HasNetmask, Triggable{

    BC9002(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 7;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public final String getName() {
        return "BC9002";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "2-station subnet";
    }

}
