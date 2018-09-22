package com.github.sync667.CraftlandiaRailsAPI.Signs;

/**
 * An IC that has a net mask should implement this
 */
public interface HasNetmask{
    /**
     * Get the net mask
     *
     * @return the net mask
     */
    public int getNetmask();
}
