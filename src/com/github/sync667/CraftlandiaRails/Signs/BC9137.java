package com.github.sync667.CraftlandiaRails.Signs;

/**
 * Match IP ranges and negate the result.
 * <p>
 * 1. Empty 2. [BC9137] 3. AA.BB.CC 4. XX.YY.ZZ
 * <p>
 * onState <=> !(AA.BB.CC <= IP <= XX.YY.ZZ)
 */
final class BC9137 extends AbstractBC9037{

    BC9137(org.bukkit.block.Block block, org.bukkit.entity.Vehicle vehicle) {
        super(block, vehicle);
    }

    /* (non-Javadoc)
     * @see AbstractBC9037#negated()
     */
    @Override
    protected boolean negated() {
        return true;
    }

    /* (non-Javadoc)
     * @see AbstractSimpleCrossroad#getName()
     */
    @Override
    public final String getName() {
        return "BC9137";
    }

    /* (non-Javadoc)
     * @see AbstractIC#getFriendlyName()
     */
    @Override
    public final String getFriendlyName() {
        return "Negated range matcher";
    }

    @Override
    public boolean isLeverReversed() {
        return true;
    }
}
