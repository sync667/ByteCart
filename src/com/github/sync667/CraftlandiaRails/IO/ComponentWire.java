package com.github.sync667.CraftlandiaRails.IO;

import org.bukkit.block.Block;
import org.bukkit.material.RedstoneWire;

import com.github.catageek.ByteCartAPI.HAL.RegistryInput;


/**
 * A Redstone wire
 */
class ComponentWire extends AbstractComponent implements InputPin, RegistryInput{

    /**
     * @param block the block containing the wire
     */
    ComponentWire(Block block) {
        super(block);
/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails : adding Redstone wire at " + block.getLocation().toString());
*/
    }

    /* (non-Javadoc)
     * @see InputPin#read()
     */
    @Override
    public boolean read() {
        if (((RedstoneWire) this.getBlock().getState().getData()).isPowered()) {
/*				if(CraftlandiaRails.debug)
					CraftlandiaRails.log.info("Redstone wire on at (" + this.getLocation().toString() + ")");
*/
            return true;
        }
/*			if(CraftlandiaRails.debug)
				CraftlandiaRails.log.info("Redstone wire off at (" + this.getLocation().toString() + ")");
*/
        return false;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.RegistryInput#getBit(int)
     */
    @Override
    public boolean getBit(int index) {
        RedstoneWire wire = ((RedstoneWire) this.getBlock().getState().getData());
        return (wire.getData() & 1 << (length() - index)) != 0;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#getAmount()
     */
    @Override
    public int getAmount() {
        return ((RedstoneWire) this.getBlock().getState().getData()).getData();
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.Registry#length()
     */
    @Override
    public int length() {
        return 4;
    }


}
