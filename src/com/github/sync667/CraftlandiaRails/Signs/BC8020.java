package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;

import com.github.sync667.CraftlandiaRails.Routing.RoutingTableWritable;
import com.github.sync667.CraftlandiaRails.Wanderer.BCWandererManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.Signs.BCRouter;
import com.github.catageek.ByteCartAPI.Wanderer.AbstractWanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer.Scope;


/**
 * An IC at the entry of a L2 router
 */
class BC8020 extends BC8010 implements BCRouter, Triggable, HasRoutingTable{


    BC8020(Block block, org.bukkit.entity.Vehicle vehicle) throws ClassNotFoundException, IOException {
        super(block, vehicle);
        this.IsTrackNumberProvider = false;
    }

    BC8020(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle, boolean isOldVersion)
            throws ClassNotFoundException, IOException {
        super(block, vehicle, isOldVersion);
        this.IsTrackNumberProvider = false;
    }

    /* (non-Javadoc)
     * @see BC8010#selectUpdater()
     */
    @Override
    protected boolean selectWanderer() {
        final BCWandererManager wm = CraftlandiaRails.myPlugin.getWandererManager();
        return (! wm.isWanderer(this.getInventory())) || wm.isWanderer(this.getInventory(), Scope.LOCAL);
    }

    /* (non-Javadoc)
     * @see BC8010#SelectRoute(AddressRouted, com.github.catageek.CraftlandiaRails.AddressLayer.Address, RoutingTableWritable)
     */
    @Override
    protected BlockFace SelectRoute(AddressRouted IPaddress, Address sign, RoutingTableWritable RoutingTable) {

        try {
            if (IPaddress.getTTL() != 0) {
                // lookup destination region
                return RoutingTable.getDirection(IPaddress.getRegion().getAmount());
            }
        } catch (NullPointerException e) {
        }

        // if TTL reached end of life and is not returnable, then we lookup region 0
        try {
            return RoutingTable.getDirection(0);
        } catch (NullPointerException e) {
        }

        // If everything has failed, then we randomize output direction
        return AbstractWanderer.getRandomBlockFace(RoutingTable, getCardinal().getOppositeFace());

    }

    /* (non-Javadoc)
     * @see BC8010#getLevel()
     */
    @Override
    public Wanderer.Level getLevel() {
        return Wanderer.Level.BACKBONE;
    }

    /* (non-Javadoc)
     * @see BC8010#getName()
     */
    @Override
    public String getName() {
        return "BC8020";
    }

    /* (non-Javadoc)
     * @see BC8010#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "L2 Router";
    }
}
