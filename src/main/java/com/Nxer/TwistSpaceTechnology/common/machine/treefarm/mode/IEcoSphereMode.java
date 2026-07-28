package com.Nxer.TwistSpaceTechnology.common.machine.treefarm.mode;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_MegaTreeFarm;

import gregtech.api.recipe.RecipeMap;

public interface IEcoSphereMode {

    EcoSphereModeResult process(TST_MegaTreeFarm machine, int euTier, double tierMultiplier);

    RecipeMap<?> getRecipeMap();

    String getDisplayName();
}
