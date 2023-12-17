package com.Nxer.TwistSpaceTechnology.recipe.processingLineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.elisis.gtnhlanth.loader.RecipeAdder;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;

public class LanthanidesRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        GT_Recipe.GT_Recipe_Map digester = RecipeAdder.instance.DigesterRecipes;

        // GT Rare Earth to Lanthanides process
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.RareEarth.getDust(8))
            .fluidInputs(Materials.Chlorine.getGas(6000))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(40)
            .addTo(digester);

    }
}
