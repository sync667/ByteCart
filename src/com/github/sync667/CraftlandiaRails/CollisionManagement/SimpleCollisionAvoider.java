package com.github.sync667.CraftlandiaRails.CollisionManagement;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Signs.Triggable;
import com.github.sync667.CraftlandiaRails.Storage.ExpirableMap;
import com.github.sync667.CraftlandiaRailsAPI.CollisionManagement.IntersectionSide;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryOutput;
import org.bukkit.Location;

/**
 * A collision avoider for T cross-roads
 */
public class SimpleCollisionAvoider extends AbstractCollisionAvoider implements CollisionAvoider{

    private static final ExpirableMap<Location, Boolean> recentlyUsedMap =
            new ExpirableMap<Location, Boolean>(20, false, "recentlyUsed9000");
    private static final ExpirableMap<Location, Boolean> hasTrainMap =
            new ExpirableMap<Location, Boolean>(14, false, "hastrain");

    private RegistryOutput Lever1 = null, Lever2 = null, Active = null;
    private final Location loc1;

    private IntersectionSide.Side state;

    private boolean reversed;


    SimpleCollisionAvoider(Triggable ic, org.bukkit.Location loc) {
        super(loc);
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails: new IntersectionSide() at " + loc);
        }

        Lever1 = ic.getOutput(0);
        Active = Lever1;
        reversed = ic.isLeverReversed();
        loc1 = ic.getLocation();
        state = (Lever1.getAmount() == 0 ? IntersectionSide.Side.LEVER_OFF : IntersectionSide.Side.LEVER_ON);
    }

    /**
     * Ask for a direction, requesting a possible transition
     *
     * @param s       the direction where the cart goes to
     * @param isTrain true if it is a train
     *
     * @return the direction actually taken
     */
    public IntersectionSide.Side WishToGo(IntersectionSide.Side s, boolean isTrain) {

        IntersectionSide.Side trueside = getActiveTrueSide(s);

        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : WishToGo to side " + trueside + " and isTrain is " + isTrain);
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : state is " + state);
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log
                    .info("CraftlandiaRails : recentlyUsed is " + this.getRecentlyUsed() + " and hasTrain is " +
                            this.getHasTrain());
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : Lever1 is " + Lever1.getAmount());
        }

        if (trueside != state && (Lever2 == null || (! this.getRecentlyUsed()) && ! this.getHasTrain())) {
            Set(trueside);
        }
        this.setRecentlyUsed(true);
        return state;

    }

    /**
     * Get the fixed side of the active lever. the second IC lever can be reversed
     *
     * @param s the original side
     *
     * @return the fixed side
     */
    private final IntersectionSide.Side getActiveTrueSide(IntersectionSide.Side s) {
        if (Active != Lever2) {
            return s;
        }
        return getSecondLeverSide(s);
    }

    /**
     * Get the fixed side of the second lever
     *
     * @param s the original side
     *
     * @return the fixed side
     */
    private final IntersectionSide.Side getSecondLeverSide(IntersectionSide.Side s) {
        return reversed ? s : s.opposite();
    }


    /* (non-Javadoc)
     * @see CollisionAvoider#Add(Triggable)
     */
    @Override
    public void Add(Triggable t) {
        if (t.getLocation().equals(loc1)) {
            Active = Lever1;
            return;
        }
        if (Lever2 != null) {
            Active = Lever2;
            return;
        }
        Lever2 = t.getOutput(0);
        Active = Lever2;
        reversed ^= t.isLeverReversed();
        Lever2.setAmount(getSecondLeverSide(state).Value());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails: Add and setting lever2 to " + Lever2.getAmount());
        }
    }

    /**
     * Activate levers. The 2 levers are in opposition
     *
     * @param s the side of the lever of the IC that created this collision avoider
     */
    private void Set(IntersectionSide.Side s) {
        this.Lever1.setAmount(s.Value());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails: Setting lever1 to " + Lever1.getAmount());
        }
        if (this.Lever2 != null) {
            this.Lever2.setAmount(getSecondLeverSide(state).Value());
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails: Setting lever2 to " + Lever2.getAmount());
            }
        }
        state = s;
    }

    /* (non-Javadoc)
     * @see CollisionAvoider#getSecondpos()
     */
    @Override
    public int getSecondpos() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see AbstractCollisionAvoider#getRecentlyUsedMap()
     */
    @Override
    protected ExpirableMap<Location, Boolean> getRecentlyUsedMap() {
        return recentlyUsedMap;
    }

    /* (non-Javadoc)
     * @see AbstractCollisionAvoider#getHasTrainMap()
     */
    @Override
    protected ExpirableMap<Location, Boolean> getHasTrainMap() {
        return hasTrainMap;
    }


}
