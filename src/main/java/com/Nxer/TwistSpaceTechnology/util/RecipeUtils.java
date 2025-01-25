package com.Nxer.TwistSpaceTechnology.util;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class RecipeUtils {

    public static Object[] getCircuits(Materials circuitTier, int amount) {
        return new Object[] { OrePrefixes.circuit.get(circuitTier), amount };
    }
}
