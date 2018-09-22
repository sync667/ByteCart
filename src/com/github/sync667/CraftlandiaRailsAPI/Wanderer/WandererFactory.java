package com.github.sync667.CraftlandiaRailsAPI.Wanderer;

import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer.Level;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public interface WandererFactory{

    /**
     * @return a new wanderer instance
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    Wanderer getWanderer(BCSign bc, Inventory inv) throws ClassNotFoundException, IOException;

    void removeAllWanderers();

    void createWanderer(int id, Inventory inv, int region, Level level, Player player, boolean isfullreset,
                        boolean isnew);

    boolean areAllRemoved();

    void updateTimestamp(InventoryContent routes);

    void destroyWanderer(Inventory inv);

}
