package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import tectech.thing.CustomItemList;

public class CircuitAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // CA recipes will be doubled during the initialization of bartworks,
        // so it is better not to input more than 64/6 items too much.

        // Optical Soc Circuit Assembly Line
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(1),
                GTCMItemList.OpticalSOC.get(1),
                CustomItemList.DATApipe.get(4),
                GTOreDictUnificator.get(OrePrefixes.bolt, MaterialsTST.AxonisAlloy, 8))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 3))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(1))
            .eut(RECIPE_UIV)
            .duration(20 * 90)
            .addTo(circuitAssemblerRecipes);

    }
}
