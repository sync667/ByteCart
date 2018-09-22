package com.github.sync667.CraftlandiaRailsAPI.Wanderer;

import com.github.sync667.CraftlandiaRailsAPI.CollisionManagement.IntersectionSide;
import com.github.sync667.CraftlandiaRailsAPI.CraftlandiaRailsAPI;
import com.github.sync667.CraftlandiaRailsAPI.Signs.BCRouter;
import com.github.sync667.CraftlandiaRailsAPI.Signs.BCSign;
import com.github.sync667.CraftlandiaRailsAPI.Signs.HasNetmask;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer.Level;
import org.bukkit.block.BlockFace;

import java.io.IOException;
import java.util.Random;
import java.util.Stack;

public class DefaultLocalWanderer <T extends InventoryContent> extends AbstractWanderer{

    private final int SignNetmask;
    private final Stack<Integer> Start;
    private final Stack<Integer> End;
    private final T content;
    private final Counter Counter;
    protected com.github.sync667.CraftlandiaRailsAPI.Wanderer.RoutingTable RoutingTable = null;
    private final String name;
    private final Level level;

    protected enum counterSlot{
        REGION(CraftlandiaRailsAPI.MAXSTATION), RING(CraftlandiaRailsAPI.MAXSTATION + 1);

        public final int slot;

        private counterSlot(int i) {
            this.slot = i;
        }
    }

    protected DefaultLocalWanderer(BCSign bc, T rte, String name, Level level) {
        super(bc, rte.getRegion());
        content = rte;
        this.name = name;
        this.level = level;
        Counter = content.getCounter();

        Start = content.getStart();
        End = content.getEnd();

		if (bc instanceof HasNetmask) {
			SignNetmask = ((HasNetmask) bc).getNetmask();
		} else {
			SignNetmask = 0;
		}

        if (bc instanceof BCRouter) {
            BCRouter ic = (BCRouter) bc;
            RoutingTable = ic.getRoutingTable();
        }

        // set cookie A to wait a router
        if (this.getContent().getCurrent() == - 2) {
            this.getStart().push(1);
            this.getContent().setCurrent(0);
            save();
        }


    }

    @Override
    public IntersectionSide.Side giveSimpleDirection() {


        // turn if it's not a station, and the ring is initialized or the address is invalid
        // and the subnet is contained in the current borders
        // and we are in the region
		if (this.getNetmask() < CraftlandiaRailsAPI.MAXSTATIONLOG && (! (this.getStart().empty() ^ this.getEnd().empty())) &&
				! this.isExactSubnet(this.getFirstStationNumber(), this.getNetmask()) &&
				this.getWandererRegion() == this.getCounter().getCount(counterSlot.REGION.slot)) {
			return IntersectionSide.Side.LEVER_ON;
		}

        return IntersectionSide.Side.LEVER_OFF;
    }

    @Override
    public BlockFace giveRouterDirection() {
        // check if we are in the good region
        if (this.getSignAddress().isValid() && this.getSignAddress().getRegion().getAmount() != getWandererRegion()) {
            // case this is not the right region
            BlockFace dir = RoutingTable.getDirection(getWandererRegion());
			if (dir != null) {
				return dir;
			}
            return this.getFrom().getBlockFace();
        }

        // there is a cookie (so it is cookie A) or it's a reset cart
        if (this.getStart().empty() ^ this.getEnd().empty()) {
            int signring = this.getSignAddress().getTrack().getAmount();
            int preferredroute = this.getStart().peek();

            BlockFace dir = null;
            // if we are not arrived yet or in ring 0, we continue
            if (signring == 0 || signring != preferredroute) {
                dir = RoutingTable.getDirection(preferredroute);
                if (dir != null) {
                    return dir;
                }
                // no route to ring
                return getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
            }
        } else {
            // no cookie
            // check counter
            if (this.getCounter().isAllFull(0, CraftlandiaRailsAPI.MAXSTATION - 1)) {
                // we configured all stations
                // incrementing ring counter in the RoutingTableExchange map
                int ring = this.getCounter().getCount(counterSlot.RING.slot);
                incrementRingCounter(ring);

                // reset counters
                this.getCounter().resetAll();
                //clear stacks and set cookie B
                this.getStart().clear();
                this.getEnd().clear();
                this.getStart().push(0);

                save();

                // the route where we went the lesser
                int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, getFrom());
                BlockFace dir;
				if ((dir = RoutingTable.getDirection(preferredroute)) != null) {
					return dir;
				}
                return getRandomBlockFace(RoutingTable, getFrom().getBlockFace());
            }
        }
        return this.getFrom().getBlockFace();
    }

    @Override
    public void doAction(BlockFace to) {

        int signring = this.getSignAddress().getTrack().getAmount();
        // the route where we went the lesser
        int preferredroute = this.getContent().getMinDistanceRing(RoutingTable, this.getFrom());


        // if we are not in the good region or on ring 0, skip update
		if (this.getSignAddress().getRegion().getAmount() != getContent().getRegion() || signring == 0) {
			return;
		}

        // if this is cookie A, do nothing if this is not the route where we want to go
        if (this.getEnd().empty() ^ this.getStart().empty() && this.getStart().peek() != 0) {
            preferredroute = this.getStart().peek();
			if (preferredroute != signring) {
				return;
			}
        }

        // mark all stations of subnets in stack as taken
		while (! this.getEnd().empty()) {
			this.leaveSubnet();
		}

        //if cookie B is present, leave cookie A and return
		if (this.getEnd().empty() ^ this.getStart().empty()) {
			if (this.getStart().pop() == 0) {
				// Pushing the route where we want to go
				this.getStart().push(preferredroute);
				save();
				return;
			}
		}

        // leave subnet, resetting start and end stacks (and removing cookie A)
        this.getStart().clear();
        this.getEnd().clear();

        // updating region and counter data from sign
        this.getCounter().setCount(counterSlot.REGION.slot, this.getSignAddress().getRegion().getAmount());
        this.getCounter().setCount(counterSlot.RING.slot, this.getSignAddress().getTrack().getAmount());

        // mark station 0 as taken
        this.getCounter().incrementCount(0, 32);

        save();

    }

    @Override
    public void doAction(IntersectionSide.Side to) {
        // cookie still there
		if (this.getStart().empty() ^ this.getEnd().empty()) {
			return;
		}

        // we did not enter the subnet
        if (to.Value() != IntersectionSide.Side.LEVER_ON.Value() &&
                this.getNetmask() < CraftlandiaRailsAPI.MAXSTATIONLOG) {
            // if we have the same sign as when entering the subnet, close the subnet
            if (this.isExactSubnet(this.getFirstStationNumber(), this.getNetmask())) {
                this.leaveSubnet();
                this.save();
            }
            return;
        }

        int length = ((CraftlandiaRailsAPI.MAXSTATION) >> this.getNetmask());

        int stationfield = - 1;
		if (getSignAddress().isValid()) {
			stationfield = this.getSignAddress().getStation().getAmount();
		}

        if (length != 1) {
            if (stationfield != - 1) {
                // register new subnet start and mask
                Stack<Integer> startstack = this.getStart();
                Stack<Integer> endstack = this.getEnd();
                startstack.push(stationfield);
                endstack.push(stationfield + length);
            }
        } else
            // case of stations
			if (stationfield != - 1) {
				this.getCounter().incrementCount(stationfield, 64);
			}

        save();
    }

    protected final int getNetmask() {
        return SignNetmask;
    }

    protected final Stack<Integer> getStart() {
        return Start;
    }


    protected final Stack<Integer> getEnd() {
        return End;
    }

    protected final Counter getCounter() {
        return Counter;
    }

    protected final T getContent() {
        return content;
    }

    /**
     * Get the current element of the first station number stack
     *
     * @return the first station number
     */
    protected int getFirstStationNumber() {
        return (this.getStart().empty() ? 0 : this.getStart().peek());
    }

    protected final boolean isExactSubnet(int address, int netmask) {
        return (address == this.getFirstStationNumber() &&
                (address | ((CraftlandiaRailsAPI.MAXSTATION - 1) >> netmask)) == (this.getLastStationNumber() - 1));
    }

    /**
     * Get the current element of the last station number stack
     *
     * @return the last station number
     */
    protected int getLastStationNumber() {
        return (this.getEnd().empty()) ? CraftlandiaRailsAPI.MAXSTATION : this.getEnd().peek();
    }

    protected void incrementRingCounter(int ring) {
		if (this.getContent().hasRouteTo(ring)) {
			this.getContent().setRoute(ring,
					this.getContent().getMetric(ring) + (new Random()).nextInt(RoutingTable.size()) + 1);
		} else {
			this.getContent().setRoute(ring, (new Random()).nextInt(RoutingTable.size()) + 1);
		}
    }

    public void save() {
        T rte = this.getContent();
        try {
            CraftlandiaRailsAPI.getPlugin().getWandererManager().saveContent(rte, this.name, this.level);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void leaveSubnet() {
        if (! this.getStart().empty() && ! this.getEnd().empty()) {
            this.fillSubnet();
        }
    }

    private final void fillSubnet() {
        int start = this.getFirstStationNumber();
        int end = this.getLastStationNumber();
		for (int i = start; i < end; i++) {
			this.getCounter().incrementCount(i, 64);
		}
    }

}
