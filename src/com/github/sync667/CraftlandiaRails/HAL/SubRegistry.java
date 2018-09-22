package com.github.sync667.CraftlandiaRails.HAL;

import com.github.catageek.ByteCartAPI.HAL.Registry;
import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.HAL.RegistryOutput;

/**
 * A restricted view of a registry
 *
 * @param <T> A type implementing Registry
 */
public class SubRegistry <T extends Registry> implements RegistryBoth{

    private final Registry Registry;
    private final int Length;
    private final int First;

    /**
     * @param reg    the original registry
     * @param length the length of the restricted view
     * @param first  the index of the first bit of the restricted view
     */
    public SubRegistry(T reg, int length, int first) {
        this.Registry = reg;
        this.Length = length;
        this.First = first;
		
/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails : creating SubRegistry " + reg.length() + " -> " + length + " bits beginning at index "+ first);
*/
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#length()
     */
    @Override
    public int length() {
        return this.Length;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#getAmount()
     */
    @Override
    public int getAmount() {
        return (this.Registry.getAmount() >> (this.Registry.length() - (this.First + this.length())) &
                (1 << this.length()) - 1);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryOutput#setBit(int, boolean)
     */
    @Override
    public void setBit(int index, boolean value) {
        ((RegistryOutput) this.Registry).setBit(index + this.First, value);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryInput#getBit(int)
     */
    @Override
    public boolean getBit(int index) {
        return ((RegistryInput) this.Registry).getBit(index + this.First);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryOutput#setAmount(int)
     */
    @Override
    public void setAmount(int amount) {
        ((RegistryOutput) this.Registry).setAmount(this.Registry.getAmount() - this.getAmount() +
                ((amount % (1 << this.length())) << (this.Registry.length() - (this.First + this.length()))));
    }

}
