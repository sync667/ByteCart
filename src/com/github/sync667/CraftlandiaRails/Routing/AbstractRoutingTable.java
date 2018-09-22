package com.github.sync667.CraftlandiaRails.Routing;

import com.github.sync667.CraftlandiaRailsAPI.Wanderer.RoutingTable;
import org.bukkit.block.BlockFace;

import java.util.Iterator;
import java.util.Set;

/**
 * An abstract class for routing tables
 */
abstract class AbstractRoutingTable implements RoutingTable{

    /* (non-Javadoc)
     * @see RoutingTable#isDirectlyConnected(int, org.bukkit.block.BlockFace)
     */
    @Override
    public final boolean isDirectlyConnected(int ring, BlockFace direction) {
        if (this.getDirection(ring) != null) {
            return this.getMetric(ring, direction) == 0;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see RoutingTable#getDirectlyConnected(org.bukkit.block.BlockFace)
     */
    @Override
    public final int getDirectlyConnected(BlockFace direction) {
        Set<Integer> rings = getDirectlyConnectedList(direction);
        return rings.size() == 1 ? rings.iterator().next() : - 1;
    }

    /* (non-Javadoc)
     * @see RoutingTable#getFirstUnknown()
     */
    @Override
    public final BlockFace getFirstUnknown() {
        for (BlockFace face : BlockFace.values()) {
            switch (face) {
                case NORTH:
                case EAST:
                case SOUTH:
                case WEST:

                    if (this.getDirectlyConnectedList(face).isEmpty()) {
                        return face;
                    }
                default:
                    break;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see RoutingTable#getDirectlyConnectedList(org.bukkit.block.BlockFace)
     */
    @Override
    abstract public Set<Integer> getDirectlyConnectedList(BlockFace direction);

    /* (non-Javadoc)
     * @see RoutingTable#getMetric(int, org.bukkit.block.BlockFace)
     */
    @Override
    abstract public int getMetric(int entry, BlockFace direction);

    /* (non-Javadoc)
     * @see RoutingTable#getMinMetric(int)
     */
    @Override
    abstract public int getMinMetric(int entry);

    /**
     * Store a line in the routing table
     *
     * @param entry     the track number
     * @param direction the direction to associate
     * @param metric    the metric to associate
     */
    abstract public void setEntry(int entry, BlockFace direction, int metric);

    /* (non-Javadoc)
     * @see RoutingTable#isEmpty(int)
     */
    @Override
    abstract public boolean isEmpty(int entry);

    /* (non-Javadoc)
     * @see RoutingTable#getOrderedRouteNumbers()
     */
    @Override
    public abstract Iterator<Integer> getOrderedRouteNumbers();

    /* (non-Javadoc)
     * @see RoutingTable#getNotDirectlyConnectedList(org.bukkit.block.BlockFace)
     */
    @Override
    public abstract Set<Integer> getNotDirectlyConnectedList(BlockFace direction);

    /**
     * Remove a line from the routing table
     *
     * @param entry the track number
     * @param from  the direction to remove
     */
    abstract public void removeEntry(int entry, BlockFace from);

    /* (non-Javadoc)
     * @see RoutingTable#getDirection(int)
     */
    @Override
    abstract public BlockFace getDirection(int entry);
}
