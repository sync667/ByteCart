package com.github.sync667.CraftlandiaRails.CollisionManagement;

import com.github.sync667.CraftlandiaRails.Signs.Triggable;

/**
 * A state machine depending of 2 elements
 */
interface CollisionAvoider{
    /**
     * Get the value stored as second pos
     *
     * @return the value of the second position
     */
    public int getSecondpos();

    /**
     * Add the second triggered IC to current CollisonAvoider
     *
     * @param t
     */
    public void Add(Triggable t);

}
