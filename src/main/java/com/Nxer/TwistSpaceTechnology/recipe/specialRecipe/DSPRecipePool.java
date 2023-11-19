package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SmallLaunchVehicle;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static gregtech.api.enums.TierEU.RECIPE_UHV;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;

public class DSPRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map DSPLauncher = GTCMRecipe.instance.DSP_LauncherRecipes;

        // launcher
        GT_Values.RA.stdBuilder()
            .itemInputs(SolarSail.get(1))
            .noFluidInputs()
            .itemOutputs(SolarSail.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(DSPLauncher);

        GT_Values.RA.stdBuilder()
            .itemInputs(SmallLaunchVehicle.get(1))
            .noFluidInputs()
            .itemOutputs(SmallLaunchVehicle.get(1))
            .noFluidOutputs()
            .eut(RECIPE_UHV)
            .duration(20 * 900)
            .addTo(DSPLauncher);

        // receiver fake recipe
        GT_Values.RA.stdBuilder()
            .noItemInputs()
            .itemOutputs(CriticalPhoton.get(1))
            .noFluidInputs()
            .noFluidOutputs()
            .specialValue(Integer.MAX_VALUE)
            .eut(0)
            .duration(0)
            .addTo(GTCMRecipe.instance.DSP_ReceiverRecipes);

        // inversion
        GT_Values.RA.stdBuilder()
            .itemInputs(CriticalPhoton.get(2))
            .noFluidInputs()
            .itemOutputs(Antimatter.get(1))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1000))
            .eut(16000)
            .duration(20 * 64 * 8)
            .addTo(GTCMRecipe.instance.QuantumInversionRecipes);

    }
}
