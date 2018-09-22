/**
 *
 */
package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressFactory;
import com.github.sync667.CraftlandiaRails.AddressLayer.AddressString;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryBoth;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryInput;

import java.util.Random;

/**
 * Gives random address to a cart
 */
final class BC7019 extends BC7010 implements Triggable{

    BC7019(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
        this.StorageCartAllowed = true;
        this.addIO();
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7019";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Random address";
    }

    /* (non-Javadoc)
     * @see BC7010#getAddressToWrite()
     */
    @Override
    protected Address getAddressToWrite() {
        int startRegion = getInput(3).getAmount();
        int endRegion = getInput(0).getAmount();

        int newRegion = startRegion + (new Random()).nextInt(endRegion - startRegion + 1);

        int startTrack = getInput(4).getAmount();
        int endTrack = getInput(1).getAmount();

        int newTrack = startTrack + (new Random()).nextInt(endTrack - startTrack + 1);

        int startStation = getInput(5).getAmount();
        int endStation = getInput(2).getAmount();

        int newStation = startStation + (new Random()).nextInt(endStation - startStation + 1);

        StringBuilder sb = new StringBuilder();
        String dot = ".";

        sb.append(newRegion).append(dot).append(newTrack).append(dot).append(newStation);

        return new AddressString(sb.toString(), false);
    }

    private void addIO() {
        // add input [0], [1] and [2] from 4th line
        this.addAddressAsInputs(AddressFactory.getAddress(getBlock(), 3));

        // add input [3], [4] and [5] from 3th line
        this.addAddressAsInputs(AddressFactory.getAddress(getBlock(), 2));
    }

    private void addAddressAsInputs(Address addr) {
        if (addr.isValid()) {
            RegistryInput region = addr.getRegion();
            this.addInputRegistry(region);

            RegistryInput track = addr.getTrack();
            this.addInputRegistry(track);

            RegistryBoth station = addr.getStation();
            this.addInputRegistry(station);
        }
    }
}
