package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_IV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_LuV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_UV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_ZPM;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsAlloy;

public class DualInputBufferRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Hatch_Input_Multi_2x2_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 1),
                Materials.TungstenSteel.getPlates(4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 4))
            .itemOutputs(DualInputBuffer_IV.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Hatch_Input_Multi_2x2_LuV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 1),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 8))
            .itemOutputs(DualInputBuffer_LuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Hatch_Input_Multi_2x2_ZPM.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)

            )
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 16))
            .itemOutputs(DualInputBuffer_ZPM.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 15)
            .addTo(assemblerRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_UV.get(1),
                ItemList.Hatch_Input_Multi_2x2_UV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 32))
            .itemOutputs(DualInputBuffer_UV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 15)
            .addTo(assemblerRecipes);
    }
}
