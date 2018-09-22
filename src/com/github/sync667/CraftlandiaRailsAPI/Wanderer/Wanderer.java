package com.github.sync667.CraftlandiaRailsAPI.Wanderer;

import com.github.sync667.CraftlandiaRailsAPI.CollisionManagement.IntersectionSide;
import com.github.sync667.CraftlandiaRailsAPI.HAL.IC;
import com.github.sync667.CraftlandiaRailsAPI.Util.DirectionRegistry;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Vehicle;

/**
 * Represents an updater
 */
public interface Wanderer{

    /**
     * The scope of the updater
     */
    public enum Scope{
        BACKBONE("backbone"), REGION("region"), LOCAL("local");

        public final String name;

        private Scope(String name) {
            this.name = name;
        }

    }

    /**
     * All types of updaters
     */
    public enum Level{
        BACKBONE("backbone", 0, Scope.BACKBONE, "rip"), REGION("region", 1, Scope.REGION, "rip"),
        LOCAL("local", 2, Scope.LOCAL, "conf"), OTHER_BACKBONE("other_backbone", 3, Scope.BACKBONE, "other"),
        OTHER_REGION("other_region", 4, Scope.REGION, "other"), OTHER_LOCAL("other_local", 5, Scope.LOCAL, "other"),
        RESET_BACKBONE("reset_backbone", 8, Scope.BACKBONE, "reset"),
        RESET_REGION("reset_region", 9, Scope.REGION, "reset"), RESET_LOCAL("reset_local", 10, Scope.LOCAL, "reset");

        public final int number;
        public final String name;
        public final Scope scope;
        public final String type;

        private Level(String name, int level, Scope scope, String type) {
            this.number = level;
            this.name = name;
            this.scope = scope;
            this.type = type;
        }

        static public boolean isMember(String aName) {
            Level[] aLevels = Level.values();
            for (Level aLevel : aLevels) {
                if (aLevel.name.equals(aName)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Method called when an updater meets a BC9XXX sign
     *
     * @param to the position of the lever
     */
    void doAction(IntersectionSide.Side to);

    /**
     * Method called when an updater meets a BC8XXX sign
     *
     * @param to the direction where the cart goes
     */
    void doAction(BlockFace to);

    /**
     * Method that must return the direction to take on a BC8XXX sign
     *
     * @return the direction that the cart should take
     */
    BlockFace giveRouterDirection();

    /**
     * Method that must return the position of the lever
     *
     * @return the position of the lever
     */
    IntersectionSide.Side giveSimpleDirection();

    /**
     * Get the current track where the updater is
     *
     * @return the track
     */
    public int getTrackNumber();

    /**
     * Get the direction from where the cart is coming
     *
     * @return the direction, or null if not relevant
     */
    public DirectionRegistry getFrom();

    /**
     * Get the type of updater
     *
     * @return the type
     */
    public Level getLevel();

    /**
     * Get the Vehicle that this updater is
     *
     * @return the Vehicle
     */
    public Vehicle getVehicle();

    /**
     * Get the region where this updater is attached to
     *
     * @return the region
     */
    public int getWandererRegion();

    /**
     * Get the center of the IC that triggered this updater class
     *
     * @return the center block
     */
    public Block getCenter();

    /**
     * Get the name of the sign
     *
     * @return the name
     */
    public String getFriendlyName();

    /**
     * Get the IC that triggered this updater
     *
     * @return the IC
     */
    public IC getBcSign();
}
