package com.Nxer.TwistSpaceTechnology.common.machine.treefarm;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_EcoSphereSimulator;

import gregtech.api.recipe.RecipeMap;

public interface IEcoSphereMode {

    EcoSphereModeResult process(TST_EcoSphereSimulator machine, int euTier);

    RecipeMap<?> getRecipeMap();

    String getDisplayName();

    default boolean displaysFluidArea() {
        return false;
    }

}
