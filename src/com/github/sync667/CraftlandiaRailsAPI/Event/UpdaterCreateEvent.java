package com.github.sync667.CraftlandiaRailsAPI.Event;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when an updater is created
 */
public final class UpdaterCreateEvent extends Event{

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final int vehicleId;
    private final Location location;


    /**
     * Default constructor
     *
     * @param VehicleId the vehicle id
     * @param location  the location
     */
    public UpdaterCreateEvent(int VehicleId, Location location) {
        this.vehicleId = VehicleId;
        this.location = location;
    }

    /**
     * @return the vehicleId
     */
    public int getVehicleId() {
        return vehicleId;
    }

    /**
     * @return the location
     */
    public Location getLocation() {
        return location;
    }
}
