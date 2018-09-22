package com.github.sync667.CraftlandiaRails.Updaters;


import com.github.sync667.CraftlandiaRailsAPI.CollisionManagement.IntersectionSide.Side;
import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import org.bukkit.block.BlockFace;

/**
 * This class implements a wanderer that will run through all routers randomly, without going to branches.
 * <p>
 * Wanderers implementors may extends this class and overrides its methods
 */
class DefaultRouterWanderer extends AbstractUpdater{

    DefaultRouterWanderer(BCSign bc, int region) {
        super(bc, region);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Routing.AbstractWanderer#doAction(SimpleCollisionAvoider.Side)
     */
    @Override
    public void doAction(Side To) {
        return;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Routing.AbstractWanderer#doAction(org.bukkit.block.BlockFace)
     */
    @Override
    public void doAction(BlockFace To) {
    }


    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Routing.AbstractWanderer#giveSimpleDirection()
     */
    @Override
    public Side giveSimpleDirection() {
        return Side.LEVER_OFF;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Routing.AbstractWanderer#giveRouterDirection()
     */
    @Override
    public BlockFace giveRouterDirection() {
        return getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
    }

}
