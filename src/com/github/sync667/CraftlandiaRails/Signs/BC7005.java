/**
 *
 */
package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;

/**
 * An eject sign
 */
final class BC7005 extends AbstractTriggeredSign implements Triggable{

    /**
     * @param block
     * @param vehicle
     */
    public BC7005(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() throws ClassNotFoundException, IOException {
        if (this.getVehicle() != null) {
            this.getVehicle().eject();
        }
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7005";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Eject";
    }

}
