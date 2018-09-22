package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.InputFactory;
import com.github.sync667.CraftlandiaRails.IO.InputPin;
import com.github.catageek.ByteCartAPI.ByteCartAPI;
import com.github.catageek.ByteCartAPI.AddressLayer.Address;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide;
import com.github.catageek.ByteCartAPI.CollisionManagement.IntersectionSide.Side;
import com.github.catageek.ByteCartAPI.Event.SignPostStationEvent;
import com.github.catageek.ByteCartAPI.Event.SignPreStationEvent;
import com.github.catageek.ByteCartAPI.Signs.Station;
import com.github.catageek.ByteCartAPI.Util.MathUtil;
import com.github.catageek.ByteCartAPI.Wanderer.Wanderer;


/**
 * A station sign
 */
public final class BC9001 extends AbstractBC9000 implements Station, Powerable, Triggable{


    BC9001(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.netmask = ByteCartAPI.MAXSTATIONLOG;
    }

    /* (non-Javadoc)
     * @see AbstractBC9000#trigger()
     */
    @Override
    public void trigger() {
        try {

            Address sign = AddressFactory.getAddress(this.getBlock(), 3);

            this.addIO();

            // input[6] = redstone for "full station" signal

            InputPin[] wire = new InputPin[2];

            // Right
            wire[0] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2)
                    .getRelative(MathUtil.clockwise(getCardinal())));
            // left
            wire[1] = InputFactory.getInput(this.getBlock().getRelative(BlockFace.UP).getRelative(getCardinal(), 2)
                    .getRelative(MathUtil.anticlockwise(getCardinal())));

            // InputRegistry[0] = start/stop command
            this.addInputRegistry(new PinRegistry<InputPin>(wire));

            triggerBC7003();

            if (! CraftlandiaRails.myPlugin.getWandererManager().isWanderer(getInventory())) {

                // if this is a cart in a train
                if (this.wasTrain(this.getLocation())) {
                    CraftlandiaRails.myPlugin.getIsTrainManager().getMap().reset(getLocation());
                    //				this.getOutput(0).setAmount(3);	// push buttons
                    return;
                }

                // if this is the first car of a train
                // we keep the state during 2 s
                if (isTrain(getDestinationAddress())) {
                    this.setWasTrain(this.getLocation(), true);
                }

                this.route();

                if (this.isAddressMatching() && this.getName().equals("BC9001") &&
                        this.getInventory().getHolder() instanceof Player) {
                    ((Player) this.getInventory().getHolder()).sendMessage(
                            ChatColor.DARK_GREEN + "[CraftlandiaRails] " + ChatColor.GREEN +
                                    CraftlandiaRails.myPlugin.getConfig().getString("Info.Destination") + " " +
                                    this.getFriendlyName() + " (" + sign + ")");

                }
                return;
            }

            // it's an wanderer
            Wanderer wanderer;
            try {
                wanderer = CraftlandiaRails.myPlugin.getWandererManager().getFactory(this.getInventory())
                        .getWanderer(this, this.getInventory());
                // here we perform wanderer action
                wanderer.doAction(IntersectionSide.Side.LEVER_OFF);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // routing
            this.getOutput(0).setAmount(0); // unpower levers


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

    /* (non-Javadoc)
     * @see Powerable#power()
     */
    @Override
    public void power() {
        this.powerBC7003();
    }


    /**
     * Manage the red light signal when triggered
     */
    private void triggerBC7003() {
        (new BC7003(this.getBlock())).trigger();
    }

    /**
     * Manage the red light signal when powered
     */
    private void powerBC7003() {
        (new BC7003(this.getBlock())).power();
    }


    /* (non-Javadoc)
     * @see AbstractBC9000#route()
     */
    @Override
    protected Side route() {
        SignPreStationEvent event;
        SignPostStationEvent event1;
        // test if every destination field matches sign field
		if (this.isAddressMatching() && this.getInput(6).getAmount() == 0) {
			event = new SignPreStationEvent(this, Side.LEVER_ON); // power levers if matching
		} else {
			event = new SignPreStationEvent(this, Side.LEVER_OFF); // unpower levers if not matching
		}
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (event.getSide().equals(Side.LEVER_ON) && this.getInput(6).getAmount() == 0) {
            this.getOutput(0).setAmount(Side.LEVER_ON.Value()); // power levers if matching
            event1 = new SignPostStationEvent(this, Side.LEVER_ON);
        } else {
            this.getOutput(0).setAmount(0); // unpower levers if not matching
            event1 = new SignPostStationEvent(this, Side.LEVER_ON);
        }
        Bukkit.getServer().getPluginManager().callEvent(event1);
        return null;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public final String getName() {
        return "BC9001";
    }

    @Override
    public final String getStationName() {
        return ((Sign) this.getBlock().getState()).getLine(2);
    }
}
