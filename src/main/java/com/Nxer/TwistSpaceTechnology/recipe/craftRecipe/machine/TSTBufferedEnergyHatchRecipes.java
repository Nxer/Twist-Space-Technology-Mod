package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchLV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchLuV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchUXV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BufferedEnergyHatchZPM;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_HV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.dreammaster.gthandler.GT_Loader_Machines.bitsd;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.CustomItemList;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class TSTBufferedEnergyHatchRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {

        final ItemStack[] circuits = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Good, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1),
            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 1),
            // TODO Piko and Quantum circuit
            CustomItemList.PikoCircuit.get(1), CustomItemList.QuantumCircuit.get(1) };

        final IRecipeMap assembler = RecipeMaps.assemblerRecipes;
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_LV.get(1),
                ItemList.Hatch_Energy_LV.get(1),
                circuits[0],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Tin, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 9))
            .itemOutputs(BufferedEnergyHatchLV.get(1))
            .noOptimize()
            .eut(RECIPE_LV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_MV.get(1),
                ItemList.Hatch_Energy_MV.get(1),
                circuits[1],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Copper, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 9))
            .itemOutputs(BufferedEnergyHatchMV.get(1))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_HV.get(1),
                ItemList.Hatch_Energy_HV.get(1),
                circuits[2],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 9))
            .itemOutputs(BufferedEnergyHatchHV.get(1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_EV.get(1),
                ItemList.Hatch_Energy_EV.get(1),
                circuits[3],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Aluminium, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 9))
            .itemOutputs(BufferedEnergyHatchEV.get(1))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_IV.get(1),
                ItemList.Hatch_Energy_IV.get(1),
                circuits[4],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorIV, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 9))
            .itemOutputs(BufferedEnergyHatchIV.get(1))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_LuV.get(1),
                ItemList.Hatch_Energy_LuV.get(1),
                circuits[5],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorLuV, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchLuV.get(1))
            .noOptimize()
            .eut(RECIPE_LuV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_ZPM.get(1),
                ItemList.Hatch_Energy_ZPM.get(1),
                circuits[6],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorZPM, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchZPM.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UV.get(1),
                ItemList.Hatch_Energy_UV.get(1),
                circuits[7],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUV, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUV.get(1))
            .noOptimize()
            .eut(RECIPE_UV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UXV.get(1),
                ItemList.Hatch_Energy_UXV.get(1),
                circuits[8],
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUHV.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UEV.get(1),
                ItemList.Hatch_Energy_UEV.get(1),
                circuits[9],
                ItemList.Field_Generator_UEV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUEV.get(1))
            .noOptimize()
            .eut(RECIPE_UEV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UIV.get(1),
                ItemList.Hatch_Energy_UIV.get(1),
                circuits[10],
                ItemList.Field_Generator_UIV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUIV.get(1))
            .noOptimize()
            .eut(RECIPE_UIV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UMV.get(1),
                ItemList.Hatch_Energy_UMV.get(1),
                circuits[11],
                ItemList.Field_Generator_UMV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUMV.get(1))
            .noOptimize()
            .eut(RECIPE_UMV)
            .duration(120)
            .addTo(assembler);
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Battery_Buffer_4by4_UXV.get(1),
                ItemList.Hatch_Energy_UXV.get(1),
                circuits[12],
                ItemList.Field_Generator_UXV.get(1))
            .fluidInputs(Materials.Plastic.getMolten(144 * 90))
            .itemOutputs(BufferedEnergyHatchUXV.get(1))
            .noOptimize()
            .eut(RECIPE_UXV)
            .duration(120)
            .addTo(assembler);
        GTModHandler.addCraftingRecipe(
            GTCMItemList.superCleanRoom.get(1),
            bitsd,
            new Object[] { "COL", "XMP", "COL", 'M', ItemList.Machine_Multi_Cleanroom, 'C',
                OrePrefixes.cableGt01.get(Materials.Tin), 'X', OrePrefixes.circuit.get(Materials.Basic), 'O',
                ItemList.LV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        // GTValues.RA.stdBuilder().itemInputs(CustomItemList.Hatch_Energy_MAX.get(1)).fluidInputs().itemOutputs(BufferedEnergyHatchMAX.get(1)).noOptimize().eut(RECIPE_MAX).duration(120).addTo(assembler);
    }

}
