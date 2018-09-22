package com.github.sync667.CraftlandiaRails.Signs;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.github.sync667.CraftlandiaRails.AddressLayer.TicketFactory;

/**
 * A ticket remover
 */
class BC7018 extends AbstractTriggeredSign implements Triggable, Clickable{

    BC7018(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        // TODO Auto-generated constructor stub
    }

    BC7018(org.bukkit.block.Block block, Player player) {
        super(block, null);
        this.setInventory(player.getInventory());
    }

    /* (non-Javadoc)
     * @see Clickable#click()
     */
    @Override
    public void click() {
        try {
            this.trigger();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() throws ClassNotFoundException, IOException {
        TicketFactory.removeTickets(this.getInventory());
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7018";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Remove Ticket";
    }
}
