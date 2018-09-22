package com.github.sync667.CraftlandiaRails.Routing;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.Updaters.UpdaterContent;
import com.github.sync667.CraftlandiaRailsAPI.Wanderer.RoutingTable;
import org.bukkit.block.BlockFace;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


/**
 * A routing table
 */
public interface RoutingTableWritable extends RoutingTable{

    /**
     * Store a line in the routing table
     *
     * @param entry     the track number
     * @param direction the direction to associate
     * @param metric    the metric to associate
     */
    public void setEntry(int entry, BlockFace direction, int metric);

    /**
     * Remove a line from the routing table
     *
     * @param entry the track number
     * @param from  the direction to remove
     */
    public void removeEntry(int entry, BlockFace from);

    /**
     * Performs the IGP protocol to update the routing table
     *
     * @param neighbour the IGP packet received
     * @param from      the direction from where we received it
     */
    public default void Update(UpdaterContent neighbour, BlockFace from) {

        // Djikstra algorithm
        // search for better routes in the received ones
        int interfacedelay = neighbour.getInterfaceDelay();

        for (Map.Entry<Integer, Metric> entry : neighbour.getEntrySet()) {

            int ring = entry.getKey();
            Metric metric = entry.getValue();
            int computedmetric = metric.value();
            if (interfacedelay > 0) {
                computedmetric += interfacedelay;
            }
            int routermetric = this.getMetric(ring, from);
            boolean directlyconnected = (this.getMinMetric(ring) == 0);

            if (! directlyconnected && (routermetric > computedmetric || routermetric < 0)) {
                this.setEntry(ring, from, computedmetric);
                if (CraftlandiaRails.debug) {
                    CraftlandiaRails.log
                            .info("CraftlandiaRails : Update : ring = " + ring + ", metric = " + computedmetric +
                                    ", direction " + from);
                }
                CraftlandiaRails.myPlugin.getWandererManager().getFactory("Updater").updateTimestamp(neighbour);
            }
        }
        // search for routes that are no more announced and not directly connected
        // to remove them
        Iterator<Integer> it = this.getNotDirectlyConnectedList(from).iterator();
        while (it.hasNext()) {
            Integer route;
            if (! neighbour.hasRouteTo(route = it.next())) {
                this.removeEntry(route, from);
                if (CraftlandiaRails.debug) {
                    CraftlandiaRails.log.info("CraftlandiaRails : Remove : ring = " + route + " from " + from);
                }
                CraftlandiaRails.myPlugin.getWandererManager().getFactory("Updater").updateTimestamp(neighbour);
            }
        }
    }

    /**
     * Clear the routing table
     *
     * @param fullreset if set to false, route to entry 0 is kept.
     */
    public void clear(boolean fullreset);


    /**
     * Serialize the routing table
     *
     * @param allowconversion if set to false, non conversion to another format
     *
     * @throws IOException
     */
    void serialize(boolean allowconversion) throws IOException;

}
