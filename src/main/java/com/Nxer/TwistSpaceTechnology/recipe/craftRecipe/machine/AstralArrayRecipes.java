package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AstralComputingArray;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.RealRackHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessDataInputHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessDataOutputHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessUpdateItem;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static goodgenerator.util.ItemRefer.HiC_T5;
import static gregtech.api.enums.Mods.AE2WCT;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import fox.spiteful.avaritia.items.LudicrousItems;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import tectech.thing.CustomItemList;
import tectech.thing.casing.TTCasingsContainer;

public class AstralArrayRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.Machine_Multi_Computer.get(1))
            .metadata(RESEARCH_TIME, 114514 * 20)
            .itemInputs(
                CustomItemList.Machine_Multi_Computer.get(64),
                CustomItemList.Machine_Multi_Computer.get(64),
                CustomItemList.Machine_Multi_Computer.get(64),
                CustomItemList.Machine_Multi_Computer.get(64),
                HiC_T5.get(64),
                HiC_T5.get(64),
                ItemList.Circuit_OpticalMainframe.get(64),
                ItemList.Circuit_OpticalMainframe.get(64),
                ItemList.Sensor_UEV.get(64),
                ItemList.Field_Generator_UEV.get(64),
                Materials.Silver.getNanite(64),
                ItemList.Gravistar.get(64),
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 1),
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 1),
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 2),
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 2))
            .fluidInputs(
                Materials.Tin.getPlasma(14400),
                Materials.SuperCoolant.getFluid(4000000),
                Materials.Infinity.getMolten(114514))
            .itemOutputs(AstralComputingArray.get(1))
            .eut(RECIPE_UEV * 3)
            .duration(20 * 1000)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.rack_Hatch.get(64),
                ItemList.Field_Generator_UEV.get(64),
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 1),
                HiC_T5.get(64))
            .itemOutputs(RealRackHatch.get(1))
            .eut(RECIPE_UEV * 2)
            .duration(20 * 500)
            .addTo(assemblerRecipes);

        final ItemStack wirelessCard = getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 42);
        final ItemStack quantumCard = getModItem(AE2WCT.ID, "infinityBoosterCard", 1);

        // Wireless Booster Card
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Materials.Fluix.getDust(1),
                Materials.CertusQuartz.getGems(1),
                Materials.EnderPearl.getPlates(1),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Titanium, 2))
            .fluidInputs(Materials.Aluminium.getMolten(144))
            .itemOutputs(copyAmount(1, wirelessCard))
            .eut(RECIPE_LV)
            .duration(100)
            .addTo(assemblerRecipes);

        // Infinity Booster Card
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                copyAmount(32, wirelessCard),
                new ItemStack(LudicrousItems.resource, 4, 5))
            .fluidInputs(Materials.Infinity.getMolten(144 * 8))
            .itemOutputs(copyAmount(1, quantumCard))
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(assemblerRecipes);

        // Wireless Data Input Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CustomItemList.dataIn_Hatch.get(1),
                CustomItemList.Machine_Multi_Switch.get(1),
                copyAmount(16, quantumCard),
                ItemList.Sensor_UIV.get(16),
                ItemList.Tesseract.get(64))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 8))
            .itemOutputs(WirelessDataInputHatch.get(1))
            .eut(RECIPE_UMV)
            .duration(800)
            .addTo(assemblerRecipes);

        // Wireless Data Output Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                CustomItemList.dataOut_Hatch.get(1),
                CustomItemList.Machine_Multi_Switch.get(1),
                copyAmount(16, quantumCard),
                ItemList.Emitter_UIV.get(16),
                ItemList.EnergisedTesseract.get(64))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 8))
            .itemOutputs(WirelessDataOutputHatch.get(1))
            .eut(RECIPE_UMV)
            .duration(800)
            .addTo(assemblerRecipes);

        // Wireless computation update card
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, WirelessDataInputHatch.get(1))
            .metadata(RESEARCH_TIME, 720000)
            .itemInputs(
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),

                WirelessDataOutputHatch.get(8),
                WirelessDataInputHatch.get(8),
                ItemList.Field_Generator_UMV.get(64),
                ItemList.Wireless_Dynamo_Energy_UMV.get(8),

                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64))
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144 * 200),
                Materials.UUMatter.getFluid(20480000),
                Materials.SuperconductorUIVBase.getMolten(5000000))
            .itemOutputs(WirelessUpdateItem.get(1))
            .eut(RECIPE_UMV)
            .duration(800)
            .addTo(AssemblyLine);

    }
}
