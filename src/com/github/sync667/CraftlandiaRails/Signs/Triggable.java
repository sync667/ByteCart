package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRailsAPI.HAL.IC;
import org.bukkit.Location;

import java.io.IOException;

/**
 * An IC that can be triggered by a cart should implement this
 */
public interface Triggable extends IC{

    /**
     * Method called when a cart is passing on the IC
     *
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void trigger() throws ClassNotFoundException, IOException;

    /**
     * Tell if the cart that triggers this IC has the train bit set, i.e is the head of a train
     *
     * @return true if the train bit is set
     */
    public boolean isTrain();

    /**
     * Tell if the IC was previously triggered by a cart with the train bit set
     * <p>
     * This method retrieves the persistent value stored in a map.
     * <p>
     * The value retrieved has a timeout, i.e the method will return false after a while.
     *
     * @param loc the location of the IC
     *
     * @return true if a train is currently using the IC
     */
    public boolean wasTrain(Location loc);

    /**
     * Tell if the lever is negated
     *
     * @return true if it is negated
     */
    public boolean isLeverReversed();
}
