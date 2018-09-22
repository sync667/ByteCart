package com.github.sync667.CraftlandiaRails.CollisionManagement;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.Util.DirectionRegistry;

/**
 * A router where the cart goes straight
 */
class StraightRouter extends AbstractRouter implements Router{

    StraightRouter(BlockFace from, org.bukkit.Location loc, boolean isOldVersion) {
        super(from, loc, isOldVersion);

        FromTo.put(Side.BACK, Side.STRAIGHT);
        FromTo.put(Side.LEFT, Side.LEFT);
        FromTo.put(Side.STRAIGHT, Side.RIGHT);
        FromTo.put(Side.RIGHT, Side.BACK);

        setSecondpos(Integer.parseInt("00100101", 2));


    }

    /* (non-Javadoc)
     * @see AbstractRouter#route(org.bukkit.block.BlockFace)
     */
    @Override
    public void route(BlockFace from) {
        // activate main levers
        this.getOutput(0).setAmount((new DirectionRegistry(from.getOppositeFace())).getAmount());

    }

    /* (non-Javadoc)
     * @see AbstractRouter#getTo()
     */
    @Override
    public final BlockFace getTo() {
        return this.getFrom().getOppositeFace();
    }
}
