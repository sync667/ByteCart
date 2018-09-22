package com.github.sync667.CraftlandiaRails.AddressLayer;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressBook.Parameter;
import com.github.sync667.CraftlandiaRails.FileStorage.BookFile;
import com.github.sync667.CraftlandiaRails.FileStorage.BookProperties;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import org.bukkit.inventory.Inventory;

/**
 * Factory class to create a return address from various supports
 */
public final class ReturnAddressFactory{

    /**
     * Creates a return address from a ticket
     *
     * @param inv the inventory containing the ticket
     *
     * @return the return address
     */
    @SuppressWarnings("unchecked")
    public final static <T extends Address> T getAddress(Inventory inv) {
        int slot;
        if ((slot = Ticket.getTicketslot(inv)) != - 1) {
            return (T) new ReturnAddressBook(
                    new Ticket(BookFile.getFrom(inv, slot, false, "ticket"), BookProperties.Conf.NETWORK),
                    Parameter.RETURN);
        }
        return null;
    }
}
