package com.github.sync667.CraftlandiaRailsAPI.Event;

import com.github.sync667.CraftlandiaRailsAPI.HAL.IC;
import org.bukkit.event.Event;

/**
 * A container class for all events
 */
public abstract class BCEvent extends Event{
    public BCEvent(IC ic) {
        super();
        this.ic = ic;
    }

    /**
     * @return the component
     */
    public IC getIc() {
        return ic;
    }

    private final IC ic;


}
