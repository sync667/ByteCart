package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRails.CollisionManagement.CollisionAvoiderBuilder;
import com.github.sync667.CraftlandiaRails.CollisionManagement.SimpleCollisionAvoider;
import com.github.sync667.CraftlandiaRails.CollisionManagement.SimpleCollisionAvoiderBuilder;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPinFactory;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.HAL.RegistryBoth;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.Signs.BCSign;
import com.github.catageek.ByteCartAPI.Util.MathUtil;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;

/**
 * An abstract class for T-intersection signs
 */
abstract class AbstractSimpleCrossroad extends AbstractTriggeredSign implements BCSign{

    protected CollisionAvoiderBuilder builder;
    private AddressRouted destination;


    AbstractSimpleCrossroad(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        builder = new SimpleCollisionAvoiderBuilder(this, block.getRelative(this.getCardinal(), 3).getLocation());
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    abstract public String getName();

    /**
     * Register the inputs and outputs
     */
    protected void addIO() {
        // Output[0] = 2 bits registry representing levers on the left and on the right of the sign
        OutputPin[] lever2 = new OutputPin[3];

        // Left
        lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
        // Right
        lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));
        // Back
        lever2[2] = OutputPinFactory.getOutput(this.getBlock().getRelative(this.getCardinal()));

        PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

        this.addOutputRegistry(command1);
    }

    protected final void addIOInv() {
        // Input[0] = destination region taken from Inventory, slot #0


        Address IPaddress = getDestinationAddress();

        if (IPaddress == null) {
            return;
        }

        RegistryInput slot2 = IPaddress.getRegion();


        this.addInputRegistry(slot2);

        // Input[1] = destination track taken from cart, slot #1

        RegistryInput slot1 = IPaddress.getTrack();


        this.addInputRegistry(slot1);

        // Input[2] = destination station taken from cart, slot #2

        RegistryBoth slot0 = IPaddress.getStation();

        this.addInputRegistry(slot0);
    }


    protected void manageWanderer(SimpleCollisionAvoider intersection) {
        // routing
        intersection.WishToGo(route(), false);
    }

    protected Side route() {
        return Side.LEVER_OFF;
    }

    @Override
    public void trigger() {
        try {

            this.addIO();

            SimpleCollisionAvoider intersection =
                    CraftlandiaRails.myPlugin.getCollisionAvoiderManager().<SimpleCollisionAvoider>getCollisionAvoider(
                            builder);

            if (! CraftlandiaRails.myPlugin.getWandererManager().isWanderer(getInventory())) {

                boolean isTrain = isTrain(getDestinationAddress());

                // if this is a cart in a train
                if (this.wasTrain(this.getLocation())) {
                    CraftlandiaRails.myPlugin.getIsTrainManager().getMap().reset(getBlock().getLocation());
                    intersection.Book(isTrain);
                    return;
                }

                // if this is the first car of a train
                // we keep it during 2 s
                if (isTrain) {
                    this.setWasTrain(this.getLocation(), true);
                }

                intersection.WishToGo(this.route(), isTrain);
                return;
            }

            manageWanderer(intersection);

        } catch (ClassCastException e) {
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : " + e.toString());
            }

            // Not the good blocks to build the signs
            return;
        } catch (NullPointerException e) {
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : " + e.toString());
            }
            e.printStackTrace();

            // there was no inventory in the cart
            return;
        }

    }

    protected final AddressRouted getDestinationAddress() {
        if (destination != null) {
            return destination;
        }
        return destination = AddressFactory.getAddress(this.getInventory());
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Signs.BCSign#getLevel()
     */
    @Override
    public Wanderer.Level getLevel() {
        return Wanderer.Level.LOCAL;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Signs.BCSign#getSignAddress()
     */
    @Override
    public final Address getSignAddress() {
        return AddressFactory.getAddress(getBlock(), 3);
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Signs.BCSign#getCenter()
     */
    @Override
    public final org.bukkit.block.Block getCenter() {
        return this.getBlock();
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.Signs.BCSign#getDestinationIP()
     */
    @Override
    public final String getDestinationIP() {
        Address ip;
        if ((ip = getDestinationAddress()) != null) {
            return ip.toString();
        }
        return "";
    }
}
