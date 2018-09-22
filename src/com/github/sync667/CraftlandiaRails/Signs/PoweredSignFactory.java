package com.github.sync667.CraftlandiaRails.Signs;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.github.sync667.CraftlandiaRails.CraftlandiaRails;
import com.github.sync667.CraftlandiaRails.HAL.AbstractIC;


/**
 * This class contains the method to instantiate any IC
 */
public class PoweredSignFactory{

    /**
     * Get an IC at the powered sign
     *
     * @param block the sign clicked
     *
     * @return a Powerable IC, or null
     */
    public Powerable getIC(Block block) {


        if (AbstractIC.checkEligibility(block)) {

            // if there is really a BC sign post
            // we extract its #

            return PoweredSignFactory.getPoweredIC(block, ((Sign) block.getState()).getLine(1));


        }
        // no BC sign post

        return null;

    }

    /**
     * Get an IC with the specific code
     *
     * @param block      the block where to reference the IC
     * @param signString the name of the sign as "BCXXXX"
     *
     * @return a Powerable IC, or null
     */
    static final public Powerable getPoweredIC(Block block, String signString) {

        if (signString.length() < 7) {
            return null;
        }

        int ICnumber = Integer.parseInt(signString.substring(3, 7));
/*		
		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails: Powered #IC " + ICnumber + " detected");
*/

        try {

            // then we instantiate accordingly
            switch (ICnumber) {

                case 7000:
                case 7001:
                    return (new BC7001(block, null));
                case 7003:
                    return (new BC7003(block));
                case 7004:
                    return (new BC7004(block, ((Sign) block.getState()).getLine(3),
                            ((Sign) block.getState()).getLine(2)));
                case 9001:
                    return (new BC9001(block, null));


            }
        } catch (Exception e) {
            if (CraftlandiaRails.debug) {
                CraftlandiaRails.log.info("CraftlandiaRails : " + e.toString());
            }

            // there was no inventory in the cart
            return null;
        }
/*		
		if(CraftlandiaRails.debug)
			CraftlandiaRails.log.info("CraftlandiaRails: #IC " + ICnumber + " not activated");
*/
        return null;

    }

}
