package com.github.sync667.CraftlandiaRails.CollisionManagement;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPinFactory;
import com.github.sync667.CraftlandiaRails.Signs.Triggable;
import com.github.sync667.CraftlandiaRails.Storage.ExpirableMap;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryOutput;
import com.github.sync667.CraftlandiaRailsAPI.Util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

abstract class AbstractRouter extends AbstractCollisionAvoider implements Router{

    private static final ExpirableMap<Location, Boolean> recentlyUsedMap =
            new ExpirableMap<Location, Boolean>(40, false, "recentlyUsedRouter");
    private static final ExpirableMap<Location, Boolean> hasTrainMap =
            new ExpirableMap<Location, Boolean>(14, false, "hasTrainRouter");

    private BlockFace From;

    protected Map<Side, Side> FromTo = new ConcurrentHashMap<Side, Side>();
    protected Map<Side, Set<Side>> Possibility = new ConcurrentHashMap<Side, Set<Side>>();
    private int secondpos = 0;
    private int posmask = 255;

    private boolean IsOldVersion;

    AbstractRouter(BlockFace from, org.bukkit.Location loc, boolean isOldVersion) {
        super(loc);
        this.setFrom(from);
        this.IsOldVersion = isOldVersion;
        this.addIO(from, loc.getBlock());
    }

    /* (non-Javadoc)
     * @see CollisionAvoider#Add(Triggable)
     */
    @Override
    public void Add(Triggable t) {
        return;
    }

    /**
     * @return the isOldVersion
     */
    private boolean isIsOldVersion() {
        return IsOldVersion;
    }

    /* (non-Javadoc)
     * @see Router#WishToGo(org.bukkit.block.BlockFace, org.bukkit.block.BlockFace, boolean)
     */
    @Override
    public final BlockFace WishToGo(BlockFace from, BlockFace to, boolean isTrain) {
        //		IntersectionSide sfrom = getSide(from);
        //		IntersectionSide sto = getSide(to);


        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : Router : coming from " + from + " going to " + to);
        }
		/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails : Router : going to " + sto);
		 */
        Router ca = this;
		/*
		if(CraftlandiaRails.debug) {
			CraftlandiaRails.log.info("CraftlandiaRails : position found  " + ca.getClass().toString());
			CraftlandiaRails.log.info("CraftlandiaRails : Recently used ? " + recentlyUsed);
			CraftlandiaRails.log.info("CraftlandiaRails : hasTrain ? " + hasTrain );
			CraftlandiaRails.log.info("CraftlandiaRails : isTrain ? " + isTrain );
		}
		 */
        Side s = getSide(from, to);

        boolean cond = ! this.getRecentlyUsed() && ! this.getHasTrain();

        if (this.getPosmask() != 255 || cond) {

            switch (s) {
                case STRAIGHT:
                    ca = new StraightRouter(from, getLocation(), this.isIsOldVersion());
                    if ((cond) || this.ValidatePosition(ca)) {
                        break;
                    }
                case RIGHT:
                    ca = new RightRouter(from, getLocation(), this.isIsOldVersion());
                    if ((cond) || this.ValidatePosition(ca)) {
                        break;
                    }
                case LEFT:
                    ca = new LeftRouter(from, getLocation(), this.isIsOldVersion());
                    if ((cond) || this.ValidatePosition(ca)) {
                        break;
                    }
                case BACK:
                    ca = new BackRouter(from, getLocation(), this.isIsOldVersion());
                    if ((cond) || this.ValidatePosition(ca)) {
                        break;
                    }
                default:
                    ca = new LeftRouter(from, getLocation(), this.isIsOldVersion());
                    if ((cond) || this.ValidatePosition(ca)) {
                        break;
                    }
                    ca = this;
            }

			/*
			if(CraftlandiaRails.debug)
				CraftlandiaRails.log.info("CraftlandiaRails : Router : position changed to " + ca.getClass().toString());
			if(CraftlandiaRails.debug)
				CraftlandiaRails.log.info("CraftlandiaRails : Router : really going to " + ca.getTo());
			 */            // save router in collision avoider map
            CraftlandiaRails.myPlugin.getCollisionAvoiderManager().setCollisionAvoider(this.getLocation(), ca);

            // activate secondary levers
            ca.getOutput(1).setAmount(ca.getSecondpos());
            ca.getOutput(2).setAmount(ca.getSecondpos());

            //activate primary levers
            ca.route(from);
        }
        ca.Book(isTrain);

        return ca.getTo();
    }


    /* (non-Javadoc)
     * @see Router#route(org.bukkit.block.BlockFace)
     */
    @Override
    public void route(BlockFace from) {
        return;
    }

    /* (non-Javadoc)
     * @see Router#getTo()
     */
    @Override
    public abstract BlockFace getTo();

    /* (non-Javadoc)
     * @see Router#getFrom()
     */
    @Override
    public final BlockFace getFrom() {
        return From;
    }

    /**
     * Tell if a transition is necessary for the router to satisfy direction request
     *
     * @param ca the collision avoider against which to check the state
     *
     * @return false if a transition is needed
     */
    private final boolean ValidatePosition(Router ca) {
        Side side = getSide(this.getFrom(), ca.getFrom());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log
                    .info("CraftlandiaRails : pos value befor rotation : " + Integer.toBinaryString(getSecondpos()));
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : rotation of bits         : " + side.Value());
        }
        int value = AbstractRouter.leftRotate8(getSecondpos(), side.Value());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : pos value after rotation : " + Integer.toBinaryString(value));
        }
        int mask = AbstractRouter.leftRotate8(getPosmask(), side.Value());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : mask after rotation      : " + Integer.toBinaryString(mask));
        }
        ca.setSecondpos(value | ca.getSecondpos());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log
                    .info("CraftlandiaRails : value after OR           : " + Integer.toBinaryString(ca.getSecondpos()));
        }
        ca.setPosmask(mask | ca.getPosmask());
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log
                    .info("CraftlandiaRails : mask after OR            : " + Integer.toBinaryString(ca.getPosmask()));
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : compatible ?             : " +
                    (((value ^ ca.getSecondpos()) & mask) == 0));
        }
        return ((value ^ ca.getSecondpos()) & mask) == 0;
    }

    /**
     * @param from the from to set
     */
    private final void setFrom(BlockFace from) {
        From = from;
    }

    /**
     * Get the relative direction of an absolute direction
     *
     * @param to the absolute direction
     *
     * @return the relative direction
     */
    @SuppressWarnings("unused")
    private final Side getSide(BlockFace to) {
        return getSide(getFrom(), to);
    }

    /**
     * Get the relative direction of an absolute direction with a specific origin
     *
     * @param from the origin axis
     * @param to   the absolute direction
     *
     * @return the relative direction
     */
    private final static Side getSide(BlockFace from, BlockFace to) {
        BlockFace t = to;
        if (from == t) {
            return Side.BACK;
        }
        t = turn(t);
        if (from == t) {
            return Side.LEFT;
        }
        t = turn(t);
        if (from == t) {
            return Side.STRAIGHT;
        }
        return Side.RIGHT;
    }

    /**
     * Get the next absolute direction on the left
     *
     * @param b the initial direction
     *
     * @return the next direction
     */
    private final static BlockFace turn(BlockFace b) {
        return MathUtil.anticlockwise(b);
    }

    /**
     * Registers levers as output
     *
     * @param from   the origin axis
     * @param center the center of the router
     */
    private final void addIO(BlockFace from, org.bukkit.block.Block center) {

        BlockFace f = from;
        BlockFace g = MathUtil.clockwise(from);
        // Main output
        OutputPin[] sortie = new OutputPin[4];

        if (this.isIsOldVersion()) {
            // East
            sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST, 3).getRelative(BlockFace.SOUTH));
            // North
            sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST, 3).getRelative(BlockFace.NORTH));
            // South
            sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH, 3).getRelative(BlockFace.EAST));
            // West
            sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH, 3).getRelative(BlockFace.WEST));
        } else {
            // East
            sortie[0] = OutputPinFactory.getOutput(center.getRelative(BlockFace.SOUTH, 3).getRelative(BlockFace.EAST));
            // North
            sortie[1] = OutputPinFactory.getOutput(center.getRelative(BlockFace.NORTH, 3).getRelative(BlockFace.WEST));
            // South
            sortie[3] = OutputPinFactory.getOutput(center.getRelative(BlockFace.WEST, 3).getRelative(BlockFace.SOUTH));
            // West
            sortie[2] = OutputPinFactory.getOutput(center.getRelative(BlockFace.EAST, 3).getRelative(BlockFace.NORTH));
        }

        checkIOPresence(sortie);

        RegistryOutput main = new PinRegistry<OutputPin>(sortie);

        // output[0] is main levers
        this.addOutputRegistry(main);


        // Secondary output to make U-turn
        OutputPin[] secondary = new OutputPin[8];
        // Alternate secondary output to make U-turn (for new BC8011 router)
        OutputPin[] altsecond = new OutputPin[8];

        for (int i = 0; i < 7; i++) {
            // the first is Back
            altsecond[i] = OutputPinFactory.getOutput(center.getRelative(f, 3).getRelative(g, 1));
            secondary[i++] = OutputPinFactory.getOutput(center.getRelative(f, 4).getRelative(g, 2));
            altsecond[i] = OutputPinFactory.getOutput(center.getRelative(f, 5).getRelative(g, 1));
            secondary[i] = OutputPinFactory.getOutput(center.getRelative(f, 6));
            f = g;
            g = MathUtil.clockwise(g);
        }

        checkIOPresence(secondary, altsecond);

        RegistryOutput second = new PinRegistry<OutputPin>(secondary);
        RegistryOutput altsec = new PinRegistry<OutputPin>(altsecond);

        // output[1] is second and third levers
        this.addOutputRegistry(second);
        // output[2] is alternate second and third levers
        this.addOutputRegistry(altsec);

    }

    /**
     * Check if there are levers as expected
     *
     * @param secondary an array of levers
     * @param altsecond alternative array of levers
     */
    private void checkIOPresence(OutputPin[] secondary, OutputPin[] altsecond) {
        for (int i = 0; i < secondary.length; i++) {
            if (secondary[i] == null && altsecond[i] == null) {
                CraftlandiaRails.log.log(java.util.logging.Level.SEVERE,
                        "CraftlandiaRails : Lever missing or wrongly positioned in router " + this.getLocation());
                throw new NullPointerException();
            }
        }
    }

    /**
     * Check if there are levers as expected
     *
     * @param sortie an array of levers
     */
    private void checkIOPresence(OutputPin[] sortie) {
        for (int i = 0; i < sortie.length; i++) {
            if (sortie[i] == null) {
                CraftlandiaRails.log.log(java.util.logging.Level.SEVERE,
                        "CraftlandiaRails : Lever missing or wrongly positioned in router " + this.getLocation());
                throw new NullPointerException();
            }
        }
    }

    /* (non-Javadoc)
     * @see Router#getSecondpos()
     */
    @Override
    public final int getSecondpos() {
        return secondpos;
    }

    /* (non-Javadoc)
     * @see Router#getPosmask()
     */
    @Override
    public final int getPosmask() {
        return posmask;
    }

    /* (non-Javadoc)
     * @see Router#setPosmask(int)
     */
    @Override
    public final void setPosmask(int posmask) {
        this.posmask = posmask;
    }

    /* (non-Javadoc)
     * @see Router#setSecondpos(int)
     */
    @Override
    public final void setSecondpos(int secondpos) {
        this.secondpos = secondpos;
    }


    /**
     * Bit-rotate a value on 8 bits
     *
     * @param value the value to rotate
     * @param d     the numbers of bits to shift left
     *
     * @return the result
     */
    private final static int leftRotate8(int value, int d) {
        int b = 8 - d;
        return (value >> (b)) | ((value & ((1 << b) - 1)) << d);
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
