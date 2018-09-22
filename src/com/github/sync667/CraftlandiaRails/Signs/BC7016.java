package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.AddressLayer.ReturnAddressFactory;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPinFactory;
import com.github.sync667.CraftlandiaRailsAPI.AddressLayer.Address;
import com.github.sync667.CraftlandiaRailsAPI.Util.MathUtil;

/**
 * A return address checker
 */
final class BC7016 extends AbstractTriggeredSign implements Triggable{

    BC7016(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7016";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Is returnable ?";
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() {
        addIO();
        Address returnAddress = ReturnAddressFactory.getAddress(this.getInventory());

        if (returnAddress != null && returnAddress.isReturnable()) {
            this.getOutput(0).setAmount(3);
        } else {
            this.getOutput(0).setAmount(0);
        }
    }

    /**
     * Register the levers output
     */
    private void addIO() {
        // Output[0] = 2 bits registry representing levers on the left and on the right of the sign
        OutputPin[] lever2 = new OutputPin[2];

        // Left
        lever2[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.anticlockwise(this.getCardinal())));
        // Right
        lever2[1] = OutputPinFactory.getOutput(this.getBlock().getRelative(MathUtil.clockwise(this.getCardinal())));

        PinRegistry<OutputPin> command1 = new PinRegistry<OutputPin>(lever2);

        this.addOutputRegistry(command1);
    }

}
