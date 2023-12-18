package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_UIV;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;

public class CircuitAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        final IRecipeMap circuitAssembler = RecipeMaps.circuitAssemblerRecipes;

        // Optical Soc Circuit Assembly Line
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(16),
                GTCMItemList.OpticalSOC.get(1),
                com.github.technus.tectech.thing.CustomItemList.DATApipe.get(32),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.EnrichedHolmium, 64))
            .fluidInputs(new FluidStack(solderPlasma, 144 * 2))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(16))
            .eut(RECIPE_UIV)
            .duration(20 * 90)
            .addTo(circuitAssembler);

    }
}
