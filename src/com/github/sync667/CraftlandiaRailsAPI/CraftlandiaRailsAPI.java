package com.github.sync667.CraftlandiaRailsAPI;

import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Resolver;

import java.util.logging.Logger;


public final class CraftlandiaRailsAPI{

    private static CraftlandiaRailsPlugin plugin;
    public static final int MAXSTATION = 256;
    public static final int MAXSTATIONLOG = 8;
    public static final int MAXRING = 2048;
    public static final int MAXRINGLOG = 11;

    /**
     * @return the plugin
     */
    public static CraftlandiaRailsPlugin getPlugin() {
        return plugin;
    }

    /**
     * @param plugin the plugin to set
     */
    public static void setPlugin(CraftlandiaRailsPlugin plugin) {
        if (CraftlandiaRailsAPI.plugin != null && plugin != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Plugin");
        }

        CraftlandiaRailsAPI.plugin = plugin;
    }

    /**
     * @return the resolver registered
     */
    public static Resolver getResolver() {
        return plugin.getResolver();
    }

    /**
     * Set the resolver that will be used
     *
     * @param resolver the resolver provided
     */
    public static void setResolver(Resolver resolver) {
        plugin.setResolver(resolver);
    }

    /**
     * Get the logger
     *
     * @return the logger
     */
    public static Logger getLogger() {
        return plugin.getLog();
    }

}
