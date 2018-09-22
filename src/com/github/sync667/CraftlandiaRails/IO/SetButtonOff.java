package com.github.sync667.CraftlandiaRails.IO;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.material.Button;

import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * this call represents a thread that powers off a button
 */
class SetButtonOff implements Runnable{

    final private Component component;
    final private Map<Location, Integer> ActivatedButtonMap;

    /**
     * @param component          the component to power off
     * @param ActivatedButtonMap a map containing the task id of current task
     */
    SetButtonOff(Component component, Map<Location, Integer> ActivatedButtonMap) {
        this.component = component;
        this.ActivatedButtonMap = ActivatedButtonMap;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        BlockState block = component.getBlock().getState();

        if (block.getData() instanceof Button) {
            Button button = (Button) block.getData();

            button.setPowered(false);
            block.setData(button);

            block.update(false, true);
            MathUtil.forceUpdate(component.getBlock().getRelative(button.getAttachedFace()));
        }

        ActivatedButtonMap.remove(block.getLocation());
    }
}
