package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRails.AddressLayer.TicketFactory;
import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.HAL.AbstractIC;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

/**
 * Base class for all signs that are triggered by vehicles that pass over it.
 * <p>
 * Reads the address configuration of the vehicle from its inventory and caches it.
 */
abstract class AbstractTriggeredSign extends AbstractIC implements Triggable{

    private final org.bukkit.entity.Vehicle Vehicle;
    private org.bukkit.inventory.Inventory Inventory;

    AbstractTriggeredSign(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block);
        this.Vehicle = vehicle;

        this.Inventory = this.extractInventory();
    }

    /**
     * @return The vehicle which triggered the sign.
     */
    final public org.bukkit.entity.Vehicle getVehicle() {
        return Vehicle;
    }

    /**
     * Extract the address configuration of the current vehicle. If the vehicle has no address configuration, the
     * default address (if configured) is applied.
     *
     * @return Inventory with address configuration from the current vehicle.
     */
    final private org.bukkit.inventory.Inventory extractInventory() {

        org.bukkit.inventory.Inventory newInv = Bukkit.createInventory(null, 27);


        // we load inventory of cart or player
        if (this.Vehicle != null) {

            if (this.getVehicle() instanceof InventoryHolder) {
                return ((InventoryHolder) this.getVehicle()).getInventory();
            } else if (this.getVehicle() instanceof Minecart) {
                if (! this.getVehicle().isEmpty()) {
                    if (((Minecart) this.getVehicle()).getPassenger() instanceof Player) {

                        if (CraftlandiaRails.debug) {
                            CraftlandiaRails.log.info("CraftlandiaRails: loading player inventory :" +
                                    ((Player) this.getVehicle().getPassenger()).getDisplayName());
                        }

                        return ((Player) this.getVehicle().getPassenger()).getInventory();
                    }
                }
            }

            /* There is no inventory, so we create one */

            // we have a default route ? so we write it in inventory
            if (CraftlandiaRails.myPlugin.getConfig().contains("EmptyCartsDefaultRoute")) {
                String DefaultRoute = CraftlandiaRails.myPlugin.getConfig().getString("EmptyCartsDefaultRoute");
                TicketFactory.getOrCreateTicket(newInv);
                //construct address object
                AddressRouted myAddress = AddressFactory.getAddress(newInv);
                //write address
                myAddress.setAddress(DefaultRoute);
                myAddress.initializeTTL();
                myAddress.finalizeAddress();
            }

        }
        return newInv;
    }

    /**
     * @return The inventory of the vehicle which triggered this sign.
     */
    public org.bukkit.inventory.Inventory getInventory() {
        return Inventory;
    }

    /**
     * Set the inventory variable
     *
     * @param inv
     */
    protected void setInventory(org.bukkit.inventory.Inventory inv) {
        this.Inventory = inv;
    }

    /* (non-Javadoc)
     * @see Triggable#isTrain()
     */
    @Override
    public final boolean isTrain() {
        return AbstractTriggeredSign.isTrain(AddressFactory.getAddress(this.getInventory()));
    }

    public static final boolean isTrain(Address address) {
        if (address != null) {
            return address.isTrain();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see Triggable#wasTrain(org.bukkit.Location)
     */
    @Override
    public final boolean wasTrain(Location loc) {
        boolean ret;
        if (CraftlandiaRails.myPlugin.getIsTrainManager().getMap().contains(loc)) {
            ret = CraftlandiaRails.myPlugin.getIsTrainManager().getMap().get(loc);
			/*			if(CraftlandiaRails.debug  && ret)
				CraftlandiaRails.log.info("CraftlandiaRails: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is wagon !");
			 */
            return ret;
        }
		/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails: "+ this.getName() + " at " + this.getLocation() + " : " + this.getVehicle() + " is not wagon !");
		 */
        return false;
    }

    /**
     * Remember the train bit
     *
     * @param loc the location where to store the bit
     * @param b   the bit
     */
    protected final void setWasTrain(Location loc, boolean b) {
        if (b) {
            CraftlandiaRails.myPlugin.getIsTrainManager().getMap().put(loc, true);
        }

    }

    /**
     * Default is lever not reversed
     *
     * @return false
     */
    @Override
    public boolean isLeverReversed() {
        return false;
    }


}
