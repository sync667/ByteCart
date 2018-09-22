package com.github.sync667.CraftlandiaRails.Signs;

/**
 * Match IP ranges.
 * <p>
 * 1. Empty 2. [BC9037] 3. AA.BB.CC 4. XX.YY.ZZ
 * <p>
 * onState <=> AA.BB.CC <= IP <= XX.YY.ZZ
 */
final class BC9037 extends AbstractBC9037{

    BC9037(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see AbstractBC9037#negated()
     */
    @Override
    protected boolean negated() {
        return false;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public final String getName() {
        return "BC9037";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "Range matcher";
    }

}
