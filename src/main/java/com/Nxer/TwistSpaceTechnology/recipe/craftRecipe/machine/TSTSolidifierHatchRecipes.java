package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsAlloy;

public class TSTSolidifierHatchRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hatch_Input_IV.get(1),
                ItemList.Hatch_Input_Bus_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1),
                Materials.TungstenSteel.getPlates(6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 2))
            .itemOutputs(GTCMItemList.SolidifyHatch_IV.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hatch_Input_LuV.get(2),
                ItemList.Hatch_Input_Bus_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 4))
            .itemOutputs(GTCMItemList.SolidifyHatch_LuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hatch_Input_ZPM.get(2),
                ItemList.Hatch_Input_Bus_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                Materials.Iridium.getPlates(6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 6))
            .itemOutputs(GTCMItemList.SolidifyHatch_ZPM.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hatch_Input_UV.get(4),
                ItemList.Hatch_Input_Bus_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1),
                Materials.Osmium.getPlates(6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 8))
            .itemOutputs(GTCMItemList.SolidifyHatch_UV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hatch_Input_UHV.get(6),
                ItemList.Hatch_Input_Bus_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1),
                Materials.Neutronium.getPlates(6))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 16))
            .itemOutputs(GTCMItemList.SolidifyHatch_UHV.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);
    }
}
