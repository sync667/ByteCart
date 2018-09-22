package com.github.sync667.CraftlandiaRailsAPI.Event;

import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a sign is invalidated by an updater
 */
public final class UpdaterSignInvalidateEvent extends UpdaterEvent{

    public UpdaterSignInvalidateEvent(Wanderer updater) {
        super(updater);
    }

    private static final HandlerList handlers = new HandlerList();

    /* (non-Javadoc)
     * @see org.bukkit.event.Event#getHandlers()
     */
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Needed for Bukkit Event API usage
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}