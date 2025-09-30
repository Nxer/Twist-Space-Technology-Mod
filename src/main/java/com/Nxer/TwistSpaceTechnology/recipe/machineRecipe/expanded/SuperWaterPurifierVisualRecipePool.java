package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.SuperWaterPurifierVisualRecipeMap;
import static gregtech.api.enums.TierEU.RECIPE_UMV;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;

public class SuperWaterPurifierVisualRecipePool {

    public static void loadRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.UUMatter.getFluid(1_000))
            .fluidOutputs(
                Materials.Grade1PurifiedWater.getFluid(1_000),
                Materials.Grade2PurifiedWater.getFluid(1_000),
                Materials.Grade3PurifiedWater.getFluid(1_000),
                Materials.Grade4PurifiedWater.getFluid(1_000),
                Materials.Grade5PurifiedWater.getFluid(1_000),
                Materials.Grade6PurifiedWater.getFluid(1_000),
                Materials.Grade7PurifiedWater.getFluid(1_000),
                Materials.Grade8PurifiedWater.getFluid(1_000),
                Materials.StableBaryonicMatter.getFluid(1_000))
            .eut(RECIPE_UMV)
            .duration(20 * 60)
            .addTo(SuperWaterPurifierVisualRecipeMap);

        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialPool.ConcentratedUUMatter.getFluidOrGas(100))
            .fluidOutputs(
                Materials.Grade1PurifiedWater.getFluid(1_000),
                Materials.Grade2PurifiedWater.getFluid(1_000),
                Materials.Grade3PurifiedWater.getFluid(1_000),
                Materials.Grade4PurifiedWater.getFluid(1_000),
                Materials.Grade5PurifiedWater.getFluid(1_000),
                Materials.Grade6PurifiedWater.getFluid(1_000),
                Materials.Grade7PurifiedWater.getFluid(1_000),
                Materials.Grade8PurifiedWater.getFluid(1_000),
                Materials.StableBaryonicMatter.getFluid(1_000))
            .eut(RECIPE_UMV)
            .duration(20 * 60)
            .addTo(SuperWaterPurifierVisualRecipeMap);
    }
}
