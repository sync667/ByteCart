package com.github.sync667.CraftlandiaRails.IO;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;

/**
 * A sign
 */
public final class ComponentSign extends AbstractComponent{

    /**
     * @param block the block containing the component
     */
    public ComponentSign(Block block) {
        super(block);
    }

    /**
     * Set a line of the sign
     *
     * @param line index of the line
     * @param s    the text to write
     */
    public void setLine(int line, String s) {
        BlockState blockstate = this.getBlock().getState();

        if (blockstate instanceof org.bukkit.block.Sign) {
            ((org.bukkit.block.Sign) blockstate).setLine(line, s);
            blockstate.update();
        }
    }

    /**
     * Get a line of a sign
     *
     * @param line index of the line
     *
     * @return the text
     */
    public String getLine(int line) {
        BlockState blockstate = this.getBlock().getState();
        if (blockstate instanceof org.bukkit.block.Sign) {
            return ((org.bukkit.block.Sign) blockstate).getLine(line);
        } else {
            CraftlandiaRails.log.info("CraftlandiaRails: AddressSign cannot be built");
            throw new IllegalArgumentException();
        }

    }
}
