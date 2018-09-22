package com.github.sync667.CraftlandiaRails.Signs;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.HAL.AbstractIC;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.IO.OutputPin;
import com.github.sync667.CraftlandiaRails.IO.OutputPinFactory;


/**
 * A cart detector
 */
final class BC7002 extends AbstractTriggeredSign implements Triggable{

    BC7002(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see Triggable#trigger()
     */
    @Override
    public void trigger() {
        OutputPin[] lever = new OutputPin[1];

        // Right
        lever[0] = OutputPinFactory.getOutput(this.getBlock().getRelative(getCardinal()));

        // OutputRegistry[1] = red light signal
        this.addOutputRegistry(new PinRegistry<OutputPin>(lever));

        this.getOutput(0).setAmount(1);
        //		if(CraftlandiaRails.debug)
        //			CraftlandiaRails.log.info("CraftlandiaRails : BC7002 count 1");

        //		CraftlandiaRails.myPlugin.getDelayedThreadManager().renew(getLocation(), 4, new Release(this));
        (new Release(this)).runTaskLater(CraftlandiaRails.myPlugin, 4);

    }

    private final class Release extends BukkitRunnable{

        private final AbstractIC bc;

        public Release(AbstractIC bc) {
            this.bc = bc;
        }

        @Override
        public void run() {
            this.bc.getOutput(0).setAmount(0);
        }
    }

    /* (non-Javadoc)
     * @see AbstractIC#getName()
     */
    @Override
    public final String getName() {
        return "BC7002";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "Cart detector";
    }
}