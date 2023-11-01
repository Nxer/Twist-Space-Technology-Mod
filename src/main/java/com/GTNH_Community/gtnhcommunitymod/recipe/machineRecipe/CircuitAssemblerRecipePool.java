package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static com.GTNH_Community.gtnhcommunitymod.common.GTCMItemList.OpticalSOC;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.GTNH_Community.gtnhcommunitymod.recipe.RecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;

public class CircuitAssemblerRecipePool implements RecipePool {

    @Override
    public void loadRecipes() {

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        // Optical Soc Circuit Assembly Line
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(16),
                OpticalSOC.get(1),
                com.github.technus.tectech.thing.CustomItemList.DATApipe.get(32),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.EnrichedHolmium, 64))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 2))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .noFluidOutputs()
            .eut(9830400)
            .duration(20 * 20)
            .addTo(GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes);

    }
}
