package com.Nxer.TwistSpaceTechnology.util;

import gregtech.api.enums.TierEU;
import gregtech.api.util.recipe.Scanning;

public class ScanningWorkaround {

    /**
     * Just a workaround for removed RESEARCH_TIME.
     * The power level need to be adjusted with new Scanner recipe system.
     * <p>
     * Need to be removed when all the Scanner recipes are re-adjusted.
     */
    public static Scanning fromLegacy(int time) {
        return new Scanning(time, TierEU.RECIPE_LV);
    }

}
