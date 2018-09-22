package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.CollisionManagement.SimpleCollisionAvoider;
import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Updaters.UpdaterLocal;
import com.github.sync667.CraftlandiaRailsAPI.Signs.Subnet;

import java.io.IOException;


/**
 * A simple intersection block with anticollision
 */
final class BC9000 extends AbstractSimpleCrossroad implements Subnet, Triggable{

    private final int netmask;

    BC9000(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = 0;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#manageUpdater(SimpleCollisionAvoider)
     */
    @Override
    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // it's an updater, so let it choosing direction
        super.manageWanderer(intersection);

        if (CraftlandiaRails.myPlugin.getConfig().getBoolean("oldBC9000behaviour", true)) {
            UpdaterLocal updater;
            try {
                updater = (UpdaterLocal) CraftlandiaRails.myPlugin.getWandererManager().getFactory(this.getInventory())
                        .getWanderer(this, this.getInventory());

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
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public String getName() {
        return "BC9000";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Collision avoider";
    }

    /**
     * @return the netmask
     */
    @Override
    public final int getNetmask() {
        return netmask;
    }
}
