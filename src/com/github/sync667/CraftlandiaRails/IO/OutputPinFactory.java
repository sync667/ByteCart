package com.github.sync667.CraftlandiaRails.IO;

import org.bukkit.Material;
import org.bukkit.block.Block;


/**
 * Factory to get an instance of an output component
 */
final public class OutputPinFactory{
    /**
     * Get an instance of the output component
     *
     * @param block block containing the component
     *
     * @return the instance
     */
    static public OutputPin getOutput(Block block) {

        if (block.getType().equals(Material.LEVER)) {
            return new ComponentLever(block);
        }

        if (block.getType().equals(Material.STONE_BUTTON) || block.getType().equals(Material.BIRCH_BUTTON) ||
                block.getType().equals(Material.ACACIA_BUTTON) || block.getType().equals(Material.DARK_OAK_BUTTON) ||
                block.getType().equals(Material.JUNGLE_BUTTON) || block.getType().equals(Material.OAK_BUTTON) ||
                block.getType().equals(Material.SPRUCE_BUTTON)) {
            return new ComponentButton(block);
        }

        return null;

    }

}
