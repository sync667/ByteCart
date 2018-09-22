package com.github.sync667.CraftlandiaRails.Signs;

import org.bukkit.block.BlockFace;

import com.github.sync667.CraftlandiaRails.AddressLayer.AddressRouted;
import com.github.sync667.CraftlandiaRails.HAL.PinRegistry;
import com.github.sync667.CraftlandiaRails.HAL.SuperRegistry;
import com.github.sync667.CraftlandiaRails.IO.InputFactory;
import com.github.sync667.CraftlandiaRails.IO.InputPin;
import com.github.catageek.ByteCartAPI.HAL.RegistryInput;
import com.github.catageek.ByteCartAPI.Util.MathUtil;

/**
 * A ring field setter using redstone
 */
class BC7013 extends BC7014 implements Triggable{

    BC7013(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see BC7014#format(com.github.catageek.CraftlandiaRails.HAL.RegistryInput, AddressRouted)
     */
    @Override
    protected String format(RegistryInput wire, AddressRouted InvAddress) {
        return "" + InvAddress.getRegion().getAmount() + "." + wire.getAmount() + "." +
                InvAddress.getStation().getAmount();
    }

    /* (non-Javadoc)
     * @see BC7014#addIO()
     */
    @Override
    protected void addIO() {
        // Input[0] : wire on left
        org.bukkit.block.Block block =
                this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.anticlockwise(getCardinal()));
        RegistryInput wire = InputFactory.getInput(block);

        InputPin[] levers = new InputPin[2];
        block = this.getBlock().getRelative(BlockFace.UP).getRelative(MathUtil.clockwise(getCardinal()));
        levers[0] = InputFactory.getInput(block);

        block = this.getBlock().getRelative(getCardinal().getOppositeFace());
        levers[1] = InputFactory.getInput(block);

        RegistryInput ret = new SuperRegistry<RegistryInput>(new PinRegistry<InputPin>(levers), wire);

        this.addInputRegistry(ret);

    }

    /* (non-Javadoc)
     * @see BC7014#getName()
     */
    @Override
    public String getName() {
        return "BC7013";
    }

    /* (non-Javadoc)
     * @see BC7014#getFriendlyName()
     */
    @Override
    public String getFriendlyName() {
        return "setTrack";
    }

    /* (non-Javadoc)
     * @see BC7014#forceTicketReuse()
     */
    @Override
    protected boolean forceTicketReuse() {
        return true;
    }
}
