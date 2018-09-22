package com.github.sync667.CraftlandiaRails.AddressLayer;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Resolver;
import com.github.sync667.CraftlandiaRailsAPI.HAL.VirtualRegistry;


/**
 * This class represents a canonical address like xx.xx.xx
 */
public class AddressString extends AbstractAddress implements Address{

    /**
     * String used as internal storage
     */
    private String String; // address as displayed
    private final static Resolver resolver;

    static {
        resolver = CraftlandiaRails.myPlugin.getResolver();
    }

    /**
     * Creates the address
     *
     * @param s the string containing the address or a name to resolve to an address
     */
    public AddressString(String s, boolean resolve) {

        if (isAddress(s)) {
            this.String = s;
            return;
        }

        if (isResolvableName(s)) {
            if (resolve) {
                this.String = resolver.resolve(s);
            } else {
                this.String = s;
            }
            return;
        }

        this.String = null;
        this.isValid = false;
    }

    /**
     * Static method to check the format of an address
     * <p>
     * A resolvable name will return true
     * <p>
     * This method does not check if the address fields are in a valid range
     *
     * @param s the string containing the address to check
     *
     * @return true if the address is in the valid format or a resolvable name
     */
    static public boolean isResolvableAddressOrName(String s) {
        if ((s.matches("([0-9]{1,4}\\.){2,2}[0-9]{1,3}")) || isResolvableName(s)) {
            return true;
        }

        return false;

    }

    /**
     * Static method to check the format of an address
     * <p>
     * This method does not check if the address fields are in a valid range
     *
     * @param s the string containing the address to check
     *
     * @return true if the address is in the valid format
     */
    static private boolean isAddress(String s) {
        if (! (s.matches("([0-9]{1,4}\\.){2,2}[0-9]{1,3}"))) {
            return false;
        }

        return true;

    }

    /**
     * Static method to check if a name resolution gives a valid address
     *
     * @param s the string containing the name to check
     *
     * @return true if the resolution gives an address
     */
    static private boolean isResolvableName(String name) {
        if (resolver != null) {
            String a = resolver.resolve(name);
            if (a != null && AddressString.isAddress(a)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#getRegion()
     */
    @Override
    public VirtualRegistry getRegion() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.REGION.getLength())).setAmount(this.getField(0));
        return ret;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#getTrack()
     */
    @Override
    public VirtualRegistry getTrack() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.TRACK.getLength())).setAmount(this.getField(1));
        return ret;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#getStation()
     */
    @Override
    public VirtualRegistry getStation() {
        VirtualRegistry ret;
        (ret = new VirtualRegistry(Offsets.STATION.getLength())).setAmount(this.getField(2));
        return ret;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#isTrain()
     */
    @Override
    public boolean isTrain() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a field by index
     *
     * @param index a number between 0 and 2
     *
     * @return the number contained in the field
     */
    private int getField(int index) {
        if (this.String == null) {
            throw new IllegalStateException("Address is not valid.");
        }
        String[] st = this.String.split("\\.");
        return Integer.parseInt(st[index].trim());
    }

    /* (non-Javadoc)
     * @see AbstractAddress#setRegion(int)
     */
    @Override
    public void setRegion(int region) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see AbstractAddress#setTrack(int)
     */
    @Override
    public void setTrack(int track) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see AbstractAddress#setStation(int)
     */
    @Override
    public void setStation(int station) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see AbstractAddress#setIsTrain(boolean)
     */
    @Override
    public void setIsTrain(boolean isTrain) {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see AbstractAddress#toString()
     */
    @Override
    public java.lang.String toString() {
        if (this.String != null) {
            return this.String;
        }
        return "";
    }

    /* (non-Javadoc)
     * @see AbstractAddress#setAddress(java.lang.String)
     */
    @Override
    public boolean setAddress(java.lang.String s) {
        if (isResolvableAddressOrName(s)) {
            this.String = s;
            this.isValid = true;
        } else {
            this.String = null;
            this.isValid = false;
        }
        return this.isValid;
    }

    /* (non-Javadoc)
     * @see AbstractAddress#UpdateAddress()
     */
    @Override
    protected boolean UpdateAddress() {
        finalizeAddress();
        return true;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#remove()
     */
    @Override
    public void remove() {
        this.String = null;
        this.isValid = false;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.AddressLayer.Address#isReturnable()
     */
    @Override
    public boolean isReturnable() {
        return false;
    }
}
