package com.github.sync667.CraftlandiaRails.CollisionManagement;

import com.github.sync667.CraftlandiaRails.Signs.Triggable;
import org.bukkit.Location;

/**
 * A builder for simple collision avoider, i.e for a T cross-roads
 */
public class SimpleCollisionAvoiderBuilder extends AbstractCollisionAvoiderBuilder implements CollisionAvoiderBuilder{

    public SimpleCollisionAvoiderBuilder(Triggable ic, Location loc) {
        super(ic, loc);
    }


    /* (non-Javadoc)
     * @see CollisionAvoiderBuilder#getCollisionAvoider()
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends CollisionAvoider> T getCollisionAvoider() {

        return (T) new SimpleCollisionAvoider(this.ic, this.loc);
    }


}
