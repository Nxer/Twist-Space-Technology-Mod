package com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class LanthanidesRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        IRecipeMap digester = LanthanidesRecipeMaps.digesterRecipes;

        // GT Rare Earth to Lanthanides process
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RareEarth.getDust(8))
            .fluidInputs(Materials.Chlorine.getGas(6000))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .noOptimize()
            .eut(TierEU.RECIPE_ZPM)
            .duration(40)
            .addTo(digester);

        // add dust of Monazite to Lanthanides process
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Monazite.getDust(2))
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(400))
            .specialValue(800)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(20 * 20)
            .addTo(digester);

        // add dust of Bastnasite to Lanthanides process
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Bastnasite.getDust(2))
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(400))
            .specialValue(800)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(20 * 20)
            .addTo(digester);

    }
}
