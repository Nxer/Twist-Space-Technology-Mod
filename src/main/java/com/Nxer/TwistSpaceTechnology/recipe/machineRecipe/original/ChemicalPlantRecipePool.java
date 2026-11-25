package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;

import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;

public class ChemicalPlantRecipePool {

    public static void loadRecipes() {
        // Recover residual prismarine with the same precipitant used by the prismatic acid line.
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(9))
            .fluidInputs(Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(32_000))
            .itemOutputs(ItemList.Prismarine_Precipitate.get(4))
            .fluidOutputs(new FluidStack(GTPPFluids.HydrogenPeroxide, 32_000))
            .eut(RECIPE_LuV)
            .duration(20 * 10)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(RecipeMaps.chemicalPlantRecipes);
    }
}
