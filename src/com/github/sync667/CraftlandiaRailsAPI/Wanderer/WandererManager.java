package com.github.sync667.CraftlandiaRailsAPI.Wanderer;

import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer.Level;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer.Scope;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public interface WandererManager{
    /**
     * Register a wanderer factory
     *
     * @param wanderer the wanderer class implementing the wanderer
     * @param name     the name that will reference this type of wanderer
     */
    public boolean register(WandererFactory wanderer, String name);

    /**
     * Unregister a wanderer factory. All wanderers in the network that were created with this factory will be treated
     * as normal carts.
     *
     * @param name the name of the type of wanderer
     */
    public void unregister(String name);

    /**
     * Get a wanderer factory
     *
     * @param bc  the sign that request the wanderer
     * @param inv the inventory where to extract the wanderercontent from
     *
     * @return the wanderer factory
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public WandererFactory getFactory(Inventory inv) throws ClassNotFoundException, IOException;

    /**
     * Create a wanderer
     *
     * @param ivc  the content of the wanderer
     * @param name the name of the type of wanderer previously registered
     * @param type a suffix to add to book title
     */
    public void saveContent(InventoryContent rte, String type, Level level) throws ClassNotFoundException, IOException;

    public InventoryContent getWandererContent(Inventory inv) throws IOException, ClassNotFoundException;

    /**
     * Tells if this type is registered as a wanderer type
     *
     * @param type the type to test
     *
     * @return true if the type is registered
     */
    boolean isRegistered(String type);

    boolean isWanderer(Inventory inv, Scope scope);

    boolean isWanderer(Inventory inv, Level level, String type);

    boolean isWanderer(Inventory inv, String type);
}
