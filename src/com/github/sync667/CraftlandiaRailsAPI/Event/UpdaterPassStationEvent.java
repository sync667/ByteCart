package com.github.sync667.CraftlandiaRailsAPI.Event;

import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when an local updater pass a station sign.
 */

public class UpdaterPassStationEvent extends UpdaterEvent{
    private final Address address;
    private final String name;

    /**
     * Default constructor
     *
     * @param updater The updater involved
     * @param address The address of the station
     * @param name    The name of the station
     */
    public UpdaterPassStationEvent(Wanderer updater, Address address, String name) {
        super(updater);
        this.address = address;
        this.name = name;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return The address of the station
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Get the name of the station
     *
     * @return The name
     */
    public String getName() {
        return name;
    }
}
