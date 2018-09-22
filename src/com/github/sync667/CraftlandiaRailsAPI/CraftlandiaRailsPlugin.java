package com.github.sync667.CraftlandiaRailsAPI;

import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Resolver;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.WandererManager;

import java.util.logging.Logger;


public interface CraftlandiaRailsPlugin{
    /**
     * @return the resolver registered
     */
    public Resolver getResolver();

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    public void setResolver(Resolver resolver);

    /**
     * Get the logger
     *
     * @return the logger
     */
    public Logger getLog();

    /**
     * @return the wanderer factory
     */
    public WandererManager getWandererManager();
}
