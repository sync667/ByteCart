package com.github.sync667.CraftlandiaRails.HAL;

import com.github.sync667.CraftlandiaRails.IO.InputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRailsAPI.HAL.Registry;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryInput;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryOutput;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * A registry implementation
 *
 * @param <T> InputPin or OutputPin type
 */
public class PinRegistry <T> implements RegistryInput, RegistryOutput, Registry{

    final private List<T> PinArray;

    /**
     * @param pins an array of pins
     */
    public PinRegistry(T[] pins) {
        this.PinArray = Arrays.asList(pins);
/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails : creating PinRegistry with" + this.length() + "pin(s)");
*/
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#length()
     */
    @Override
    public int length() {
        return PinArray.size();
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#getAmount()
     */
    @Override
    public int getAmount() {

        int amount = 0;
        int i = 1;

        for (ListIterator<T> it = this.PinArray.listIterator(this.length()); it.hasPrevious(); i = i << 1) {
            if (it.previous() != null) {

                it.next();

                if (((InputPin) it.previous()).read()) {
                    amount += i;

                }

            }
        }
        return amount;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryOutput#setBit(int, boolean)
     */
    @Override
    public void setBit(int index, boolean value) {
        ((OutputPin) this.PinArray.get(index)).write(value);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryOutput#setAmount(int)
     */
    @Override
    public void setAmount(int amount) {
        int i = amount;


        for (ListIterator<T> it = this.PinArray.listIterator(this.length()); it.hasPrevious(); i = i >> 1) {
            if (it.previous() != null) {

                it.next();

                if ((i & 1) != 0) {
                    ((OutputPin) it.previous()).write(true);

                } else {
                    ((OutputPin) it.previous()).write(false);

                }
            }
        }


    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryInput#getBit(int)
     */
    @Override
    public boolean getBit(int index) {
        return ((InputPin) this.PinArray.get(index)).read();
    }


}
