package com.github.sync667.CraftlandiaRailsAPI.HAL;

/**
 * A read-only registry
 */
public interface Registry{
    /**
     * @return The value stored in this registry.
     */
    public int getAmount();

    /**
     * @return The length of this registry in bits.
     */
    public int length();
}
