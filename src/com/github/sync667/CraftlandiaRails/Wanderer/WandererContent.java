package com.github.sync667.CraftlandiaRails.Wanderer;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Routing.BCCounter;
import com.github.sync667.CraftlandiaRails.Routing.Metric;
import com.github.sync667.CraftlandiaRailsAPI.Util.DirectionRegistry;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.InventoryContent;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.RoutingTable;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.Wanderer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class WandererContent implements InventoryContent{

    /**
     *
     */
    private static final long serialVersionUID = - 9068486630910859194L;

    private transient Inventory inventory;
    private String player;

    private BCCounter counter;

    private long creationtime = Calendar.getInstance().getTimeInMillis();
    private int lastrouterid;
    private Stack<Integer> Start;
    private Stack<Integer> End;

    //internal variable used by updaters
    private int Current = - 2;

    private long expirationtime;

    protected Map<Integer, Metric> tablemap = new HashMap<>();


    public WandererContent(Inventory inv, Wanderer.Level level, int region, Player player) {
        this.Region = region;
        this.Level = level;
        this.inventory = inv;
        this.player = player.getName();
        counter = new BCCounter();
        setStart(new Stack<>());
        setEnd(new Stack<>());
    }

    /**
     * Set the counter instance
     *
     * @param counter the counter instance to set
     */
    final void setCounter(BCCounter counter) {
        this.counter = counter;
    }

    private Wanderer.Level Level;

    private int Region;

    /**
     * Set the level of the updater
     *
     * @param level the level to store
     */
    final void setLevel(Wanderer.Level level) {
        Level = level;
    }

    /**
     * Set the region of the updater
     *
     * @param region the region to set
     */
    final void setRegion(int region) {
        Region = region;
    }

    /**
     * Get the level of the updater
     *
     * @return the level
     */
    @Override
    public Wanderer.Level getLevel() {
        return Level;
    }

    /**
     * Get the region of the updater
     *
     * @return the region
     */
    @Override
    public int getRegion() {
        return Region;
    }

    /**
     * Get the ring id where the updater thinks it is in
     *
     * @return the ring id
     */
    @Override
    public int getCurrent() {
        return Current;
    }

    /**
     * Set the ring id where the updater thinks it is in
     *
     * @param current the ring id
     */
    @Override
    public void setCurrent(int current) {
        Current = current;
    }

    /**
     * @return the counter
     */
    @Override
    public BCCounter getCounter() {
        return counter;
    }

    /**
     * @return the inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * @param inventory the inventory to set
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public long getCreationtime() {
        return creationtime;
    }

    /**
     * @param creationtime the creationtime to set
     */
    @SuppressWarnings("unused")
    private void setCreationtime(long creationtime) {
        this.creationtime = creationtime;
    }

    /**
     * @return the player
     */
    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }


    /**
     * Get the id previously stored
     *
     * @return the id
     */
    public final int getLastrouterid() {
        return lastrouterid;
    }

    /**
     * Store an id in the updater book
     *
     * @param lastrouterid the id to store
     */
    public final void setLastrouterid(int lastrouterid) {
        this.lastrouterid = lastrouterid;
    }

    /**
     * @return the start
     */
    @Override
    public Stack<Integer> getStart() {
        return Start;
    }

    /**
     * @return the end
     */
    @Override
    public Stack<Integer> getEnd() {
        return End;
    }

    /**
     * @param start the start to set
     */
    private void setStart(Stack<Integer> start) {
        Start = start;
    }


    /**
     * @param end the end to set
     */
    private void setEnd(Stack<Integer> end) {
        End = end;
    }

    public long getExpirationTime() {
        return expirationtime;
    }

    /**
     * Set the expiration time
     *
     * @param lastupdate the lastupdate to set
     */
    @Override
    public void setExpirationTime(long lastupdate) {
        this.expirationtime = lastupdate;
    }

    /**
     * Insert an entry in the IGP packet
     *
     * @param number the ring id
     * @param metric the metric value
     */
    @Override
    public void setRoute(int number, int metric) {
        tablemap.put(number, new Metric(metric));
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : setting metric of ring " + number + " to " + metric);
        }
    }

    /**
     * Get the metric value of a ring of the IGP exchange packet
     *
     * @param entry the ring id
     *
     * @return the metric
     */
    @Override
    public int getMetric(int entry) {
        return tablemap.get(entry).value();
    }

    /**
     * Get the ring that has the minimum metric in the IGP packet
     *
     * @param routingTable the routing table
     * @param from         the direction to exclude from the search
     *
     * @return the ring id, or -1
     */
    @Override
    public int getMinDistanceRing(RoutingTable routingTable, DirectionRegistry from) {
        Iterator<Integer> it = routingTable.getOrderedRouteNumbers();

        if (! it.hasNext()) {
            return - 1;
        }

        //skip ring 0
        it.next();

        int route;
        int min = 10000, ret = - 1; // big value

        while (it.hasNext()) {
            route = it.next();
            if (routingTable.getDirection(route) != from.getBlockFace()) {
                if (! this.hasRouteTo(route)) {
                    if (CraftlandiaRails.debug) {
                        CraftlandiaRails.log.info("CraftlandiaRails : found ring " + route + " was never visited");
                    }
                    return route;
                } else {
                    if (getMetric(route) < min) {
                        min = getMetric(route);
                        ret = route;
                    }
                }
            }
        }
        if (CraftlandiaRails.debug) {
            CraftlandiaRails.log.info("CraftlandiaRails : minimum found ring " + ret + " with " + min);
        }
        return ret;
    }

    /**
     * Tells if the IGP packet has data on a ring
     *
     * @param ring the ring id
     *
     * @return true if there is data on this ring
     */
    @Override
    public boolean hasRouteTo(int ring) {
        return tablemap.containsKey(ring);
    }
}
