package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;

public class CosmicProcessorCircuitRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Silicon Neutron to UHV circuits
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Wetware_Extreme.get(1),
                GTCMItemList.SiliconBasedNeuron.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUEVBase, 2))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 1))
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 5)
            .addTo(RecipeMaps.formingPressRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Wetware_Extreme.get(1),
                GTCMItemList.SiliconBasedNeuron.get(1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUIVBase, 1))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 2))
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(20 * 5)
            .addTo(RecipeMaps.formingPressRecipes);

    }
}
