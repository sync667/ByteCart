package com.github.sync667.CraftlandiaRails.IO;

/**
 * Represents a writable component, storing a single bit
 */
public interface OutputPin{
    /**
     * write the bit
     *
     * @param bit true to write 1, false to write 0
     */
    public void write(boolean bit);

}
