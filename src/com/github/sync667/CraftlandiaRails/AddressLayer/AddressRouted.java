package com.github.sync667.CraftlandiaRails.AddressLayer;

import com.github.catageek.ByteCartAPI.AddressLayer.Address;


/**
 * Represents an address currently routed
 */
public interface AddressRouted extends Address{

    /**
     * Get the TTL (time-to-live) associated with the address
     *
     * @return the TTL
     */
    public int getTTL();

    /**
     * Set the TTL
     * <p>
     * {@link Address#finalizeAddress()} should be called later to actually set the TTL
     *
     * @param i the value to set
     */
    public void updateTTL(int i);

    /**
     * Initialize TTL to its default value
     * <p>
     * {@link Address#finalizeAddress()} should be called later to actually initialize the TTL
     */
    public void initializeTTL();
}
