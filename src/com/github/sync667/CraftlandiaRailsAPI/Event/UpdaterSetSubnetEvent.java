package com.github.sync667.CraftlandiaRailsAPI.Event;

import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a local updater modifies the address of a BC9XXX sign, except BC9001.
 */
public class UpdaterSetSubnetEvent extends UpdaterClearSubnetEvent{
    private final Address newAddress;

    /**
     * Default constructor
     *
     * @param updater    The updater involved
     * @param oldAddress The old address of the subnet
     * @param newAddress The new address of the subnet
     * @param length     number of stations this subnet can contain
     */
    public UpdaterSetSubnetEvent(Wanderer updater, Address oldAddress, Address newAddress, int length) {
        super(updater, oldAddress, length);
        this.newAddress = newAddress;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the newAddress
     */
    public String getNewAddress() {
        return newAddress.toString();
    }
}
