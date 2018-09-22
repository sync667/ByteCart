package com.github.sync667.CraftlandiaRails.CollisionManagement;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A router where a cart turns left
 */
final class LeftRouter extends AbstractRouter implements Router{

    LeftRouter(BlockFace from, org.bukkit.Location loc, boolean b) {
        super(from, loc, b);
        FromTo.put(Side.BACK, Side.LEFT);

        Set<Side> left = EnumSet.of(Side.LEFT, Side.STRAIGHT, Side.RIGHT);
        Possibility.put(Side.LEFT, left);

        Set<Side> straight = EnumSet.of(Side.STRAIGHT, Side.LEFT, Side.BACK);
        Possibility.put(Side.STRAIGHT, straight);

        Set<Side> right = EnumSet.of(Side.LEFT, Side.BACK);
        Possibility.put(Side.RIGHT, right);

        setSecondpos(Integer.parseInt("01000000", 2));
        setPosmask(Integer.parseInt("11100000", 2));

    }

    /* (non-Javadoc)
     * @see AbstractRouter#getTo()
     */
    @Override
    public BlockFace getTo() {
        return MathUtil.clockwise(this.getFrom());
    }

}
