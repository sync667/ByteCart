package com.github.sync667.CraftlandiaRailsAPI.Signs;

import com.github.sync667.CraftlandiaRailsAPI.Wanderer.RoutingTable;
import org.bukkit.block.BlockFace;


/**
 * A router
 */
public interface BCRouter extends BCSign{
    /**
     * Get the track from where the cart is coming.
     * <p>
     * For a region router, the returned value is the ring number.
     * <p>
     * For a backbone router, the returned value is the region number.
     *
     * @return the track number
     */
    public int getOriginTrack();

    /**
     * Return the direction from where the cart is coming
     *
     * @return the direction
     */
    public BlockFace getFrom();

    public RoutingTable getRoutingTable();
}
