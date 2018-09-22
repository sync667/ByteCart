package com.github.sync667.CraftlandiaRails.Updaters;

import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.AbstractWanderer;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.block.BlockFace;

class UpdaterResetBackbone extends UpdaterBackBone implements Wanderer{

    UpdaterResetBackbone(BCSign bc, UpdaterContent rte) {
        super(bc, rte);
    }


    @Override
    public void doAction(BlockFace to) {
        if (! this.isAtBorder()) {
            reset();
        }
    }

    @Override
    protected BlockFace selectDirection() {
        BlockFace face;
        if ((face = manageBorder()) != null) {
            return face;
        }
        return AbstractWanderer.getRandomBlockFace(getRoutingTable(), getFrom().getBlockFace());
    }


}
