package com.github.sync667.CraftlandiaRails.IO;

import org.bukkit.block.Block;

/**
 * Represents a component, i.e a lever, a button, etc.
 */
interface Component{
    /**
     * Get the block containing the component
     *
     * @return the block
     */
    public Block getBlock();

}
