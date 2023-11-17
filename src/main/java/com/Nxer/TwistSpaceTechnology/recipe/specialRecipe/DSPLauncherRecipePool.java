package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static gregtech.api.enums.TierEU.RECIPE_UHV;

import com.Nxer.TwistSpaceTechnology.common.machine.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;

public class DSPLauncherRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map DSPLauncher = GTCMRecipe.instance.DSP_LauncherRecipes;

        GT_Values.RA.stdBuilder()
            .itemInputs(SolarSail.get(1))
            .noFluidInputs()
            .itemOutputs(SolarSail.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            // .duration(20*120)
            .duration(20 * 1)
            .addTo(DSPLauncher);

        GT_Values.RA.stdBuilder()
            .itemInputs(SmallLaunchVehicle.get(1))
            .noFluidInputs()
            .itemOutputs(SmallLaunchVehicle.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 900)
            .addTo(DSPLauncher);

    }
}
