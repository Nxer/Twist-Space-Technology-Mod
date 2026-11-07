package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import static gregtech.api.util.GTRecipeConstants.NANITE_TIERS;

import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.material.BECCondensates;
import com.dreammaster.item.NHItemList;

import gregtech.api.enums.CondensateType;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.NaniteTier;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import tectech.recipe.TTRecipeAdder;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;

/** Energy buffering, regulation and high-current transmission. */
public final class EnergySustainmentMatrixRecipes {

    // spotless:off
    public static void loadRecipes() {
        // Framework: an accessible ZPM foundation, four blocks per batch.
        if (Mods.GraviSuite.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 4),
                    ModItemHandler.GraviSuite.CoolingCore.get(16),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                    ItemList.Circuit_Parts_Crystal_Chip_Master.get(16),
                    ItemList.Circuit_Chip_UHPIC.get(16),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 8),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 6))
                .circuit(10)
                .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(2304))
                .itemOutputs(GTCMItemList.EnergySustainmentMatrixFramework.get(4))
                .duration(20 * 30)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(RecipeMaps.assemblerRecipes);
        }

        if (!Mods.NewHorizonsCoreMod.isModLoaded()) return;

        // UEV: bidirectional laser power, battery storage and high-power switching.
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.EnergySustainmentMatrixFramework.get(1),
            32_768_000, 2048, (int) TierEU.RECIPE_UEV, 8,
            new Object[] {
                GTCMItemList.EnergySustainmentMatrixFramework.get(1),
                CustomItemList.eM_Power.get(16),
                CustomItemList.eM_energyTunnel6_UEV.get(1),
                CustomItemList.eM_dynamoTunnel6_UEV.get(1),

                ItemList.ZPM3.get(4),
                ItemList.Field_Generator_UEV.get(8),
                ItemList.Sensor_UEV.get(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 16 },

                NHItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Circuit_Wafer_PPIC.get(64),
                Materials.Silver.getNanite(32),
                GTCMItemList.PerfectLapotronCrystal.get(64),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahEnriched, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.Naquadria.getMolten(144 * 512),
                Materials.SuperCoolant.getFluid(256_000),
                Materials.Grade8PurifiedWater.getFluid(64_000) },
            GTCMItemList.EnergySustainmentMatrixUEV.get(1),
            20 * 60 * 4, (int) TierEU.RECIPE_UEV);

        // UIV: spatially isolated power buffers and optical control.
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.EnergySustainmentMatrixUEV.get(1),
            131_072_000, 4096, (int) TierEU.RECIPE_UIV, 16,
            new Object[] {
                GTCMItemList.EnergySustainmentMatrixUEV.get(1),
                CustomItemList.eM_Containment_Advanced.get(16),
                CustomItemList.eM_energyTunnel7_UIV.get(1),
                CustomItemList.eM_dynamoTunnel7_UIV.get(1),

                ItemList.ZPM4.get(4),
                ItemList.Field_Generator_UIV.get(16),
                ItemList.Sensor_UIV.get(16),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 16 },

                NHItemList.HighEnergyFlowCircuit.get(64),
                ItemList.Optically_Compatible_Memory.get(64),
                Materials.Gold.getNanite(32),
                ItemList.StableBosonContainmentUnit.get(16),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Naquadria, 64),
                MaterialsAlloy.BLACK_TITANIUM.getPlateDouble(64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Kevlar, 64) },
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 192),
                Materials.DimensionallyShiftedSuperfluid.getFluid(256_000),
                Materials.StableBaryonicMatter.getFluid(256_000),
                Materials.SuperCoolant.getFluid(1_024_000) },
            GTCMItemList.EnergySustainmentMatrixUIV.get(1),
            20 * 60 * 16, (int) TierEU.RECIPE_UIV);

        // UMV: resonant power transmission and fluctuation suppression.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EnergySustainmentMatrixUIV.get(1),
                CustomItemList.eM_Ultimate_Containment.get(16),
                CustomItemList.eM_energyTunnel8_UMV.get(1),
                CustomItemList.eM_dynamoTunnel8_UMV.get(1),

                ItemList.ZPM5.get(4),
                ItemList.Field_Generator_UMV.get(32),
                ItemList.Sensor_UMV.get(32),
                NHItemList.CircuitUXV.get(16),

                ItemList.MetaMaterial_Waveguide2.get(32),
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(32),
                ItemList.Transdimensional_Alignment_Matrix.get(4),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SixPhasedCopper, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 64))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] {
                    NaniteTier.WhiteDwarf, NaniteTier.SixPhasedCopper, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf,

                    NaniteTier.BlackDwarf, NaniteTier.WhiteDwarf, NaniteTier.WhiteDwarf, NaniteTier.BlackDwarf,

                    NaniteTier.BlackDwarf, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf, NaniteTier.BlackDwarf,

                    NaniteTier.WhiteDwarf, NaniteTier.SixPhasedCopper, NaniteTier.BlackDwarf, NaniteTier.Transcendent })
            .fluidInputs(
                CondensateType.BoundlessCosmicSolder.getEntangled(1_024_000),
                CondensateType.ChromaticGlass.getEntangled(144 * 16384),
                CondensateType.DimensionallyShiftedSuperfluid.getEntangled(4_096_000),
                BECCondensates.ConcentratedUUMatter.getEntangled(100_000))
            .itemOutputs(GTCMItemList.EnergySustainmentMatrixUMV.get(1))
            .duration(20 * 60 * 64)
            .eut(TierEU.RECIPE_UMV)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);

        // UXV: dense stellar-matter buffers and high-current power regulation.
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EnergySustainmentMatrixUMV.get(1),
                CustomItemList.eM_Ultimate_Containment_Advanced.get(16),
                CustomItemList.eM_energyTunnel9_UXV.get(1),
                CustomItemList.eM_dynamoTunnel9_UXV.get(1),

                ItemList.ZPM6.get(4),
                ItemList.Field_Generator_UXV.get(64),
                ItemList.Sensor_UXV.get(64),
                NHItemList.CircuitMAX.get(16),

                ItemList.MetaMaterial_Waveguide3.get(64),
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                ItemList.MetaMaterial_FieldManipulator4.get(64),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.WhiteDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.BlackDwarfMatter, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 64))
            .metadata(
                NANITE_TIERS,
                new NaniteTier[] {
                    NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.Universium, NaniteTier.Universium,

                    NaniteTier.Eternity, NaniteTier.Universium, NaniteTier.Universium, NaniteTier.MagMatter,

                    NaniteTier.Eternity, NaniteTier.Universium, NaniteTier.Universium, NaniteTier.Eternity,

                    NaniteTier.Universium, NaniteTier.Eternity, NaniteTier.MagMatter, NaniteTier.Transcendent })
            .fluidInputs(
                CondensateType.BoundlessCosmicSolder.getEntangled(2_048_000),
                CondensateType.QuarkGluonPlasma.getEntangled(512_000),
                CondensateType.PhononMedium.getEntangled(8_192_000),
                BECCondensates.ConcentratedUUMatter.getEntangled(200_000_000))
            .itemOutputs(GTCMItemList.EnergySustainmentMatrixUXV.get(1))
            .duration(20 * 60 * 256)
            .eut(TierEU.RECIPE_UXV)
            .addTo(TecTechRecipeMaps.condensateAssemblingRecipes);
    }
    // spotless:on
}
