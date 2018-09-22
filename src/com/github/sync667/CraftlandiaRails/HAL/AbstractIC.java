package com.github.sync667.CraftlandiaRails.HAL;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.IO.ComponentSign;
import com.github.sync667.CraftlandiaRailsAPI.HAL.IC;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryInput;
import com.github.sync667.CraftlandiaRailsAPI.HAL.RegistryOutput;
import com.github.sync667.CraftlandiaRailsAPI.Util.MathUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.util.Map;
import java.util.WeakHashMap;


// All ICs must inherit from this class

/**
 * An abstract class implementing common methods for all ICs
 */
abstract public class AbstractIC implements IC{

    final private Block Block;
    final private org.bukkit.Location Location;
    private static final Map<String, Boolean> icCache = new WeakHashMap<String, Boolean>();
    private static org.bukkit.Location emptyLocation = new org.bukkit.Location(null, 0, 0, 0);

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getName()
     */
    @Override
    abstract public String getName();

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return ((Sign) this.getBlock().getState()).getLine(2);
    }

    private RegistryInput[] input = new RegistryInput[9];
    private int input_args = 0;

    private RegistryOutput[] output = new RegistryOutput[6];
    private int output_args = 0;

    public AbstractIC(Block block) {
        this.Block = block;
        if (block != null) {
            this.Location = block.getLocation();
        } else {
            this.Location = new org.bukkit.Location(null, 0, 0, 0);
        }
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#addInputRegistry(com.github.catageek.CraftlandiaRails.HAL.RegistryInput)
     */
    @Override
    public final void addInputRegistry(RegistryInput reg) {
        this.input[this.input_args++] = reg;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#addOutputRegistry(com.github.catageek.CraftlandiaRails.HAL.RegistryOutput)
     */
    @Override
    public final void addOutputRegistry(RegistryOutput reg) {
        this.output[this.output_args++] = reg;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getInput(int)
     */
    @Override
    public final RegistryInput getInput(int index) {
        return input[index];
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getOutput(int)
     */
    @Override
    public final RegistryOutput getOutput(int index) {
        return output[index];
    }

    static public final void removeFromCache(Block block) {
        icCache.remove(block.getLocation(emptyLocation).toString());
    }

    // This function checks if we have a CraftlandiaRails sign at this location
    static public final boolean checkEligibility(Block b) {

        if (b.getType() != Material.SIGN_POST && b.getType() != Material.WALL_SIGN) {
            return false;
        }

        Boolean ret;
        String s;
        if ((ret = icCache.get(s = b.getLocation(emptyLocation).toString())) != null) {
            return ret;
        }

        String line_content = ((Sign) b.getState()).getLine(1);

        if (CraftlandiaRails.myPlugin.getConfig().getBoolean("FixBroken18", false)) {
            if (ret = AbstractIC.checkLooseEligibility(line_content)) {
                (new ComponentSign(b)).setLine(1, "[" + line_content + "]");
            } else {
                ret = AbstractIC.checkEligibility(line_content);
            }
        } else {
            ret = AbstractIC.checkEligibility(line_content);
        }
        icCache.put(s, ret);
        return ret;
    }

    static public final boolean checkEligibility(String s) {

        if (! (s.matches("^\\[BC[0-9]{4,4}\\]$"))) {
            return false;
        }

        return true;

    }


    static private final boolean checkLooseEligibility(String s) {

        if (! (s.matches("^BC[0-9]{4,4}$"))) {
            return false;
        }

        return true;

    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getCardinal()
     */
    @Override
    public final BlockFace getCardinal() {
        try {
            BlockFace f =
                    ((org.bukkit.material.Sign) this.getBlock().getState().getData()).getFacing().getOppositeFace();
            f = MathUtil.straightUp(f);
            if (f == BlockFace.UP) {
                CraftlandiaRails.log.severe("CraftlandiaRails: Tilted sign found at " + this.getLocation() +
                        ". Please straight it up in the axis of the track");
            }
            return f;
        } catch (ClassCastException e) {
            // this is not a sign
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getBlock()
     */
    @Override
    public final Block getBlock() {
        return Block;
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getBuildPermission()
     */
    @Override
    public final String getBuildPermission() {
        return "bytecart." + getName();
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getTriggertax()
     */
    @Override
    public final int getTriggertax() {
        return CraftlandiaRails.myPlugin.getConfig().getInt("usetax." + this.getName());
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getBuildtax()
     */
    @Override
    public final int getBuildtax() {
        return CraftlandiaRails.myPlugin.getConfig().getInt("buildtax." + this.getName());
    }

    /* (non-Javadoc)
     * @see com.github.catageek.CraftlandiaRails.HAL.IC#getLocation()
     */
    @Override
    public org.bukkit.Location getLocation() {
        return Location;
    }
}
