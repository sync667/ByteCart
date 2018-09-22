package com.github.sync667.CraftlandiaRailsAPI.Signs;

import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.HAL.IC;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;

/**
 * A network sign should implement this
 */
public interface BCSign extends IC{
    /**
     * Get the hierarchical level of the IC
     *
     * @return the level
     */
    public Wanderer.Level getLevel();

    /**
     * Get the vehicle that uses this IC
     *
     * @return the vehicle
     */
    public Vehicle getVehicle();

    /**
     * Get the address stored in the IC
     *
     * @return the address
     */
    public Address getSignAddress();

    /**
     * Get the address stored in the ticket
     *
     * @return the address
     */
    public String getDestinationIP();

    /**
     * Get the center of the IC.
     *
     * @return the center
     */
    public Block getCenter();
}
