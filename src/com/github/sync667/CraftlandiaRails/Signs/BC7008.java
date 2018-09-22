/**
 *
 */
package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;
import java.util.ListIterator;

import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;

/**
 * A cart remover
 */
final class BC7008 extends AbstractTriggeredSign implements Triggable{

    /**
     * @param block
     */
    public BC7008(org.bukkit.block.Block block, Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7008";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Cart remover";
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() throws ClassNotFoundException, IOException {
        org.bukkit.entity.Vehicle vehicle = this.getVehicle();

        // we eject the passenger
        if (vehicle.getPassenger() != null) {
            vehicle.eject();
        }

        // we drop items
        if (CraftlandiaRails.myPlugin.keepItems()) {
            org.bukkit.inventory.Inventory inventory;
            if (vehicle instanceof InventoryHolder) {
                inventory = ((InventoryHolder) vehicle).getInventory();
                World world = this.getBlock().getWorld();
                org.bukkit.Location loc = this.getBlock().getRelative(BlockFace.UP, 2).getLocation();
                ListIterator<ItemStack> it = inventory.iterator();
                while (it.hasNext()) {
                    ItemStack stack = it.next();
                    if (stack != null) {
                        world.dropItem(loc, stack);
                    }
                }
            }
        }

        vehicle.remove();
    }
}
