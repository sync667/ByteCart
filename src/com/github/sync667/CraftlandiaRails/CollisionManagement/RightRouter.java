package com.github.sync667.CraftlandiaRails.CollisionManagement;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A router where the cart turns right
 */
final class RightRouter extends AbstractRouter implements Router{

    RightRouter(BlockFace from, org.bukkit.Location loc, boolean b) {
        super(from, loc, b);

        FromTo.put(Side.BACK, Side.RIGHT);
        FromTo.put(Side.LEFT, Side.LEFT);
        FromTo.put(Side.STRAIGHT, Side.STRAIGHT);
        FromTo.put(Side.RIGHT, Side.BACK);

        setSecondpos(Integer.parseInt("00101001", 2));


    }

    /* (non-Javadoc)
     * @see AbstractRouter#route(org.bukkit.block.BlockFace)
     */
    @Override
    public void route(BlockFace from) {
        // activate main levers
        this.getOutput(0).setAmount((new DirectionRegistry(MathUtil.anticlockwise(from))).getAmount());

    }

    /* (non-Javadoc)
     * @see AbstractRouter#getTo()
     */
    @Override
    public BlockFace getTo() {
        return MathUtil.anticlockwise(this.getFrom());
    }

}
