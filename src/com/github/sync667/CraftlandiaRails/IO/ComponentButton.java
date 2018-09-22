package com.github.sync667.CraftlandiaRails.IO;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRailsAPI.Util.MathUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;
import org.bukkit.material.MaterialData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A button
 */
class ComponentButton extends AbstractComponent implements OutputPin, InputPin{

    final static private Map<Location, Integer> ActivatedButtonMap = new ConcurrentHashMap<Location, Integer>();

    /**
     * @param block the block containing the component
     */
    protected ComponentButton(Block block) {
        super(block);
/*		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails : adding Button at " + block.getLocation().toString());
*/
    }

    /* (non-Javadoc)
     * @see OutputPin#write(boolean)
     */
    @Override
    public void write(boolean bit) {
        final Block block = this.getBlock();
        final BlockState blockstate = block.getState();
        if (blockstate.getData() instanceof Button) {
            final ComponentButton component = this;
            int id;

            final Button button = (Button) blockstate.getData();

            if (bit) {
                if (ActivatedButtonMap.containsKey(block)) {

                    // if button is already on, we cancel the scheduled thread
                    CraftlandiaRails.myPlugin.getServer().getScheduler().cancelTask(ActivatedButtonMap.get(block));

                    // and we reschedule one
                    id = CraftlandiaRails.myPlugin.getServer().getScheduler()
                            .scheduleSyncDelayedTask(CraftlandiaRails.myPlugin,
                                    new SetButtonOff(component, ActivatedButtonMap), 40);

                    // We update the HashMap
                    ActivatedButtonMap.put(block.getLocation(), id);

                } else {
                    // if button is off, we power the button
                    button.setPowered(true);
                    blockstate.setData(button);
                    blockstate.update(false, true);
                    MathUtil.forceUpdate(this.getBlock().getRelative(button.getAttachedFace()));
			
			
/*			if(CraftlandiaRails.debug)
				CraftlandiaRails.log.info("Button at (" + this.getLocation().toString() + ") : " + bit);
*/


                    // delayed action to unpower the button after 2 s.

                    id = CraftlandiaRails.myPlugin.getServer().getScheduler()
                            .scheduleSyncDelayedTask(CraftlandiaRails.myPlugin,
                                    new SetButtonOff(component, ActivatedButtonMap), 40);

                    // We update the HashMap
                    ActivatedButtonMap.put(block.getLocation(), id);
                }
            }

        }
    }

    /* (non-Javadoc)
     * @see InputPin#read()
     */
    @Override
    public boolean read() {
        MaterialData md = this.getBlock().getState().getData();
        if (md instanceof Button) {
            return ((Button) md).isPowered();
        }
        return false;
    }


}
