package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import fox.spiteful.avaritia.items.LudicrousItems;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import tectech.thing.casing.TTCasingsContainer;

public class AstralArrayRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Machine_Multi_Computer.get(1),
            8_192_000,
            4096,
            (int) RECIPE_UEV,
            8,
            new Object[] {
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
                new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 2)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(1000 * 144),
                MaterialsTST.Axonium.getMolten(144 * 4),
                Materials.UUMatter.getFluid(1000 * 4000),},
            GTCMItemList.AstralComputingArray.get(1),
            20 * 1000,
            (int) RECIPE_UEV * 3);

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
                Materials.Fluix.getDust(1),
                Materials.CertusQuartz.getGems(1),
                Materials.EnderPearl.getPlates(1),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Titanium, 2),
                GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Aluminium.getMolten(144))
            .itemOutputs(copyAmount(1, wirelessCard))
            .eut(RECIPE_LV)
            .duration(100)
            .addTo(assemblerRecipes);

        // Infinity Booster Card
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                copyAmount(32, wirelessCard),
                new ItemStack(LudicrousItems.resource, 4, 5),
                GTUtility.getIntegratedCircuit(2))
            .fluidInputs(Materials.Infinity.getMolten(144 * 8))
            .itemOutputs(copyAmount(1, quantumCard))
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(assemblerRecipes);

        // Wireless Data Input Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
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
                GTUtility.getIntegratedCircuit(18),
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
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.WirelessDataInputHatch.get(1),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            8,
            new Object[] {
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),
                copyAmount(64, quantumCard),

                WirelessDataOutputHatch.get(8),
                WirelessDataInputHatch.get(8),
                ItemList.Field_Generator_UIV.get(64),
                ItemList.Wireless_Dynamo_Energy_UIV.get(8),

                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64),
                CustomItemList.Machine_Multi_Switch.get(64)},
            new FluidStack[] {
                MaterialsUEVplus.SpaceTime.getMolten(144 * 200),
                Materials.SuperconductorUIVBase.getMolten(144 * 32768),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(10)},
            WirelessUpdateItem.get(1),
            20 * 600,
            (int) RECIPE_UMV);

    }
    // spotless:on
}
