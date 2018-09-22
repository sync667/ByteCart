package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;

import com.github.sync667.CraftlandiaRails.Routing.RoutingTableFactory;
import com.github.sync667.CraftlandiaRails.Routing.RoutingTableWritable;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.github.catageek.ByteCartAPI.Signs.BCRouter;

final class BC8011 extends BC8010 implements BCRouter, Triggable, HasRoutingTable{

    BC8011(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) throws ClassNotFoundException, IOException {
        super(block, vehicle, false);
    }

    /* (non-Javadoc)
     * @see BC8010#loadChest()
     */
    protected RoutingTableWritable loadChest() throws ClassNotFoundException, IOException {
        BlockState blockstate;
        if ((blockstate = getCenter().getState()) instanceof InventoryHolder) {
            // Loading inventory of chest at same level of the sign
            Inventory ChestInventory = ((InventoryHolder) blockstate).getInventory();

            // Converting inventory in routing table
            return RoutingTableFactory.getRoutingTable(ChestInventory, 0);
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see BC8010#getName()
     */
    @Override
    public String getName() {
        return "BC8011";
    }

}
