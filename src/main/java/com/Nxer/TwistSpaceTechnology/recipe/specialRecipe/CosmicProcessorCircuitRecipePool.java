package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceTimeSuperconductingInlaidMotherboard;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class CosmicProcessorCircuitRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // SpaceTimeSuperconductingInlaidMotherboard
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 1),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 22),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUIVBase, 22))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(334))
            .itemOutputs(SpaceTimeSuperconductingInlaidMotherboard.get(16))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 22)
            .addTo(RecipeMaps.pcbFactoryRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                GT_Utility.getNaniteAsCatalyst(Materials.Gold),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 1),
                MyMaterial.shirabon.get(OrePrefixes.foil, 22),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SuperconductorUMVBase, 22))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(667))
            .itemOutputs(SpaceTimeSuperconductingInlaidMotherboard.get(64))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(20 * 22)
            .addTo(RecipeMaps.pcbFactoryRecipes);

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
