package com.github.sync667.CraftlandiaRails.FileStorage;

import org.bukkit.inventory.meta.BookMeta;

import com.github.sync667.CraftlandiaRails.Util.Base64;


/**
 * Base64 encoder/decoder for BookOutPutStream
 */
final class Base64BookOutputStream extends BookOutputStream{

    public Base64BookOutputStream(BookMeta book) {
        super(book);
    }

    /* (non-Javadoc)
     * @see BookOutputStream#getEncodedString()
     */
    @Override
    protected String getEncodedString() {
        return Base64.encodeToString(buf, false);
    }

    /* (non-Javadoc)
     * @see BookOutputStream#getBuffer()
     */
    @Override
    protected byte[] getBuffer() {
        return Base64.encodeToByte(buf, false);
    }
}
