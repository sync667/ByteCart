package com.github.sync667.CraftlandiaRails.Signs;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPinFactory;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * Power the lever on the train including wagons
 */
class BC7020 extends AbstractTriggeredSign implements Triggable{

    BC7020(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() {
        addIO();

        // if this is a cart in a train
        if (this.wasTrain(this.getLocation())) {
            CraftlandiaRails.myPlugin.getIsTrainManager().getMap().reset(getLocation());
            actionWagon();
            return;
        }

        if (this.isTrain()) {
            this.setWasTrain(this.getLocation(), true);
            this.getOutput(0).setAmount(3);    // activate levers
        } else {
            this.getOutput(0).setAmount(0);    // deactivate levers
        }

    }

    /**
     * A method called on each wagon of the train
     */
    protected void actionWagon() {
    }

    /**
     * Register the output levers
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

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public String getName() {
        return "BC7020";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "Is a Train ?";
    }

}
