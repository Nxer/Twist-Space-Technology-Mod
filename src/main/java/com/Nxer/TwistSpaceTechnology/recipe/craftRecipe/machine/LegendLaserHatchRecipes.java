package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static gregtech.api.enums.ItemList.ZPM6;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import galaxyspace.core.register.GSBlocks;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class LegendLaserHatchRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_energyTunnel9_UXV.get(1))
            .metadata(RESEARCH_TIME, 640 * HOURS)
            .itemInputs(
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),

                CustomItemList.eM_energyTunnel9_UXV.get(32),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 64),
                ZPM6.get(1),

                ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),

                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64))
            .fluidInputs(
                MaterialsUEVplus.Eternity.getMolten(2_000_000_000),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(2_000_000_000),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(2_000_000_000),
                MaterialsUEVplus.Space.getMolten(2_000_000_000))
            .itemOutputs(CustomItemList.eM_energyTunnel9001.get(1))
            .eut(RECIPE_UXV)
            .duration(1448154)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_dynamoTunnel9_UXV.get(1))
            .metadata(RESEARCH_TIME, 640 * HOURS)
            .itemInputs(
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),
                GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                GTCMItemList.AdvancedHighPowerCoilBlock.get(64),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),

                CustomItemList.eM_dynamoTunnel9_UXV.get(32),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 64),
                ZPM6.get(1),

                ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64),

                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64))
            .fluidInputs(
                MaterialsUEVplus.Eternity.getMolten(2_000_000_000),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(2_000_000_000),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(2_000_000_000),
                MaterialsUEVplus.Time.getMolten(2_000_000_000))
            .itemOutputs(CustomItemList.eM_dynamoTunnel9001.get(1))
            .eut(RECIPE_UXV)
            .duration(1448154)
            .addTo(AssemblyLine);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel9001.get(1),
            2147483647,
            1048576,
            536870912,
            16777216,
            new Object[] {
                CustomItemList.eM_energyTunnel9001.get(1),
                ItemRefer.Compact_Fusion_Coil_T4.get(1),
                new ItemStack(GSBlocks.DysonSwarmBlocks,1,4),
                CustomItemList.Machine_Multi_Transformer.get(1),

                CustomItemList.eM_Power.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsUEVplus.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16),

                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.UXV), 16),
                ItemList.EnergisedTesseract.get(1) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1_296 * 64 * 4),
                MaterialsUEVplus.ExcitedDTSC.getFluid(500L * 64) },
            GTCMItemList.LegendaryWirelessEnergyHatch.get(1),
            20 * 60,
            (int) RECIPE_UMV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.LegendaryWirelessEnergyHatch.get(1),
            2147483647,
            16777216,
            536870912,
            536870912,
            new Object[] {
                GTCMItemList.LegendaryWirelessEnergyHatch.get(16),
                CustomItemList.eM_Dimensional.get(16),
                GTCMItemList.StabilisationFieldGeneratorUXV.get(1),
                GTCMItemList.MassFabricatorGenesis.get(1),

                CustomItemList.EOH_Infinite_Energy_Casing.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsTST.Axonium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.Eternity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                // -- Temporarily, be revised in the next version
                ItemList.EnergisedTesseract.get(64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1_296 * 256 * 4),
                MaterialsUEVplus.ExcitedDTSC.getFluid(500L * 256)},
            GTCMItemList.HarmoniousWirelessEnergyHatch.get(1),
            20 * 60,
            (int) RECIPE_UXV);

    }
    // spotless:on
}
