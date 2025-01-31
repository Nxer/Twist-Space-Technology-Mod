package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_UIV;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;

public class CircuitAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        final IRecipeMap circuitAssembler = RecipeMaps.circuitAssemblerRecipes;

        // Optical Soc Circuit Assembly Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(16),
                GTCMItemList.OpticalSOC.get(1),
                tectech.thing.CustomItemList.DATApipe.get(32),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.EnrichedHolmium, 64))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 2))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .eut(RECIPE_UIV)
            .duration(20 * 90)
            .addTo(circuitAssembler);

    }
}
