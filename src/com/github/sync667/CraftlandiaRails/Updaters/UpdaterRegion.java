package com.github.sync667.CraftlandiaRails.Updaters;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Util.LogUtil;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.Event.UpdaterSetRingEvent;
import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

import java.util.Random;

class UpdaterRegion extends AbstractRegionUpdater implements Wanderer{

    UpdaterRegion(BCSign bc, UpdaterContent rte) {
        super(bc, rte);
    }

    private final String getAddress(int ring) {
        return "" + getWandererRegion() + "." + ring + ".0";
    }

    private int setSign(int current) {
        current = findFreeTrackNumber(current);
        // update sign with new number we found or current
        this.getSignAddress().setAddress(this.getAddress(current));
        this.getSignAddress().finalizeAddress();
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : change address on sign to " + this.getAddress(current));
        }
        return current;
    }

    private int findFreeTrackNumber(int current) {
        if (current == - 2) {
            return 0;
        }
        while (current < 0) {
            // find a free number for the track if needed
            current = this.getCounter().firstEmpty();
            // if there is already a route, find another number
            // except if the route connects to here
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : trying ring " + current);
            }
            if (! this.getRoutingTable().isEmpty(current) &&
                    ! this.getRoutingTable().isDirectlyConnected(current, getFrom().getBlockFace())) {
                this.getCounter().incrementCount(current);
                current = - 1;
            }
        }
        return current;
    }


    private int getOrSetCurrent(int current) {
        // check if the sign has not priority
        if (current > 0 && current < getTrackNumber()) {
            // current < sign => reset counter, clear route and write sign
            this.getCounter().reset(getTrackNumber());
            this.getRoutingTable().removeEntry(getTrackNumber(), getFrom().getBlockFace());
            this.getSignAddress().setAddress(this.getAddress(current));
            this.getSignAddress().finalizeAddress();
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : change address on sign to " + this.getAddress(current));
            }
            return current;
        } else {
            // sign seems to have priority
            // if the router knows that it is directly connected
            // we keep it, otherwise we find a new number (if possible)
            //			if (! this.getRoutingTable().isDirectlyConnected(getTrackNumber(), getFrom()) && this.isTrackNumberProvider())
            //				return setSign(current);
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : getOrSetCurrent() : current as track on sign " +
                        this.getTrackNumber());
            }

            return getTrackNumber();
        }
    }

    private boolean isSignNeedUpdate(int current) {
        int track = getTrackNumber();

        if (! this.getRoutes().isNew() && track == - 1 && current == - 2) {
            String error = "First BC8010 sign met has no address. If it is an initial configuration" +
                    ", add option 'new' at the end of bcupdater command to confirm." +
                    " If this is not a new network (i.e. you have already used bcupdater)" +
                    ", you should start from anywhere but here";
            LogUtil.sendError(this.getRoutes().getPlayer(), error);
            CraftlandiaRails.myPlugin.getWandererManager().getFactory("Updater")
                    .destroyWanderer(this.getRoutes().getInventory());
            return true;
        }
        return (track == - 1 && current != - 2) || (track > 0 && current != - 2 && getCounter().getCount(track) == 0) ||
                (current >= 0 && current != track) ||
                (track > 0 && ! this.getRoutingTable().isDirectlyConnected(track, getFrom().getBlockFace()));
    }

    @Override
    protected BlockFace selectDirection() {
        BlockFace face;
        if ((face = manageBorder()) != null) {
            return face;
        }

        if (this.getRoutes() != null) {
            // current: track number we are on
            int current = this.getRoutes().getCurrent();

            if (this.isSignNeedUpdate(current)) {
                if (CraftlandiaRails.debug) {
                    CraftlandiaRails.log
                            .info("CraftlandiaRails : selectDirection() : sign need update as current = " + current);
                }
                return this.getFrom().getBlockFace();
            }

            // if there is a side we don't have visited yet, let go there
            if (isTrackNumberProvider()) {
                try {
                    if ((face = this.getRoutingTable().getFirstUnknown()) != null && ! this.isSameTrack(face)) {
                        if (CraftlandiaRails.debug) {
                            CraftlandiaRails.log
                                    .info("CraftlandiaRails : selectDirection() : first unknown " + face.toString());
                        }
                        return face;
                    }
                } catch (NullPointerException e) {
                    LogUtil.sendError(this.getRoutes().getPlayer(), "CraftlandiaRails : Chest expected at position " +
                            this.getCenter().getRelative(BlockFace.UP, 5).getLocation());
                    LogUtil.sendError(this.getRoutes().getPlayer(), "CraftlandiaRails : or at position " +
                            this.getCenter().getRelative(BlockFace.DOWN, 2).getLocation());
                    throw e;
                }

                int min;
                if ((min = this.getCounter().getMinimum(this.getRoutingTable(), this.getFrom())) != - 1) {
                    if (CraftlandiaRails.debug) {
                        CraftlandiaRails.log.info("CraftlandiaRails : selectDirection() : minimum counter " + min);
                    }
                    return this.getRoutingTable().getDirection(min);
                }
            }
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : selectDirection() : default ");
        }
        return getRandomBlockFace(this.getRoutingTable(), this.getFrom().getBlockFace());
    }

    @Override
    public void Update(BlockFace To) {

        // current: track number we are on
        int current = getCurrent();
        boolean isNew = (current < 0);

        UpdaterSetRingEvent event = null;

        if (isTrackNumberProvider()) {
            if (! getSignAddress().isValid()) {
                // if there is no address on the sign
                // we provide one
                current = setSign(current);
                event = new UpdaterSetRingEvent(this, - 1, current);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }

            if (getSignAddress().isValid()) {
                // there is an address on the sign
                int old = getTrackNumber();
                current = getOrSetCurrent(current);
                event = new UpdaterSetRingEvent(this, old, current);
                if (old != current) {
                    Bukkit.getServer().getPluginManager().callEvent(event);
                }
            }
        } else {
            current = 0;
        }

        setCurrent(current);
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : Update() : current is " + current);
        }

        // update track counter if we have entered a new one
        if (current > 0 && isNew) {
            this.getCounter().incrementCount(current, new Random().nextInt(this.getRoutingTable().size() + 1) + 1);
        }

        routeUpdates(To);
    }


    @Override
    public final int getTrackNumber() {
        Address address;
        if ((address = getSignAddress()).isValid()) {
            return address.getTrack().getAmount();
        }
        return - 1;
    }
}
