package com.github.sync667.CraftlandiaRails.Updaters;

import org.bukkit.block.BlockFace;

import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Wanderer.AbstractWanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

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
