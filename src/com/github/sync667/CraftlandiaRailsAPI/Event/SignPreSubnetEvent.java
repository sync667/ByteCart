package com.github.sync667.CraftlandiaRailsAPI.Event;

import com.github.sync667.CraftlandiaRailsAPI.CollisionManagement.IntersectionSide.Side;
import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import com.github.sync667.CraftlandiaRailsAPI.Signs.Subnet;
import org.bukkit.event.HandlerList;

/**
 * Event triggered when a vehicle is using a subnet sign, before the collision avoidance layer operations.
 * <p>
 * The direction may be modified by collision avoidance layer.
 */
public class SignPreSubnetEvent extends SignPostSubnetEvent{

    /**
     * Default constructor
     * <p>
     * The side parameter may be: - LEFT: the vehicle wish not to enter the subnet - RIGHT: the vehicle wish to enter
     * the subnet OR wish to leave the subnet if it was inside
     *
     * @param subnet The BC9XXX sign involved
     * @param side   The direction wished of the vehicle
     */
    public SignPreSubnetEvent(Subnet subnet, Side side) {
        super(subnet, side);
    }


    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /* (non-Javadoc)
     * @see SignPostSubnetEvent#getSign()
     */
    @Override
    protected BCSign getSign() {
        return subnet;
    }

    /**
     * Change the direction taken by the vehicle on the fly This will modify internal state of the sign before actual
     * operations. This will not change the destination address recorded in the vehicle.
     * <p>
     * The final direction is undefined until routing layer operations occur.
     *
     * @param side A value from IntersectionSide.Side enum
     */
    public void setSide(Side side) {
        this.side = side;
    }
}
