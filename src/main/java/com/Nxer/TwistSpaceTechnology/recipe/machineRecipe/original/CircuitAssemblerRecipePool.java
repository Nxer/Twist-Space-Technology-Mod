package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import tectech.thing.CustomItemList;

public class CircuitAssemblerRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap CA = RecipeMaps.circuitAssemblerRecipes;

        // CA recipes will be multiplied during the initialization of bartworks,
        // so it is better not to input more than 64/6 items too much.

        // Optical Soc Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Optical.get(1),
                GTCMItemList.OpticalSOC.get(1),
                CustomItemList.DATApipe.get(1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Infinity, 3))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 4))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(1))
            .eut(RECIPE_UIV)
            .duration(20 * 60)
            .addTo(CA);

        // High Energy Flow Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                ItemList.Energy_LapotronicOrb2.get(1),
                ItemList.Circuit_Chip_QPIC.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 2))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 2))
            .itemOutputs(HighEnergyFlowCircuit.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 60)
            .addTo(CA);

    }
}
