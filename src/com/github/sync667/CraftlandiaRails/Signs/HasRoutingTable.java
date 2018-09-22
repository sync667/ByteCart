package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.Routing.RoutingTableWritable;

/**
 * An IC that have a routing table should implement this
 */
interface HasRoutingTable{
    /**
     * Get the routing table
     *
     * @return the routing table
     */
    public RoutingTableWritable getRoutingTable();
}
