package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.LightWeightPlate;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
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
import com.Nxer.TwistSpaceTechnology.util.enums.TierEU;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import galaxyspace.core.register.GSBlocks;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class DSPRecipes implements IRecipePool {

    // spotless:off
    @Override
    public void loadRecipes() {

        // DSP Launch Site machine
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(GCBlocks.landingPad, 1, 0),
            16_384_000,
            8192,
            (int) TierEU.RECIPE_UEV,
            64,
            new Object[] {
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 9),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                new ItemStack(IGBlocks.SpaceElevatorCasing, 64),
                new ItemStack(GCBlocks.landingPad, 64),

                ItemList.Electric_Motor_UEV.get(64),
                ItemList.Electric_Piston_UEV.get(64),
                ItemList.Robot_Arm_UEV.get(64),
                ItemList.Sensor_UEV.get(64),

                ItemList.Field_Generator_UEV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SixPhasedCopper, 64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SixPhasedCopper, 64),

                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                MaterialsUEVplus.TranscendentMetal.getNanite(64),
                GTCMItemList.StellarConstructionFrameMaterial.get(64),
                copyAmount(64, LightWeightPlate.getLeft())},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 2048),
                MaterialsAlloy.BOTMIUM.getFluidStack(144 * 8192),
                Materials.Bedrockium.getMolten(144 * 8192),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(100)},
            GTCMItemList.DSPLauncher.get(1),
            20 * 1800,
            (int) TierEU.RECIPE_UIV);

        // DSP Ray Receiving Station
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.SolarSail.get(1),
            131_072_000,
            65536,
            (int) TierEU.RECIPE_UIV,
            64,
            new Object[] {
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 9),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 5),
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 1),

                ItemList.Sensor_UIV.get(64),
                ItemList.Sensor_UIV.get(64),
                ItemList.Emitter_UIV.get(64),
                ItemList.Emitter_UIV.get(64),

                ItemList.Field_Generator_UIV.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                ItemList.ZPM4.get(1),

                GTCMItemList.SpaceWarper.get(64),
                GTCMItemList.StellarConstructionFrameMaterial.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsTST.NeutroniumAlloy, 64)},
            new FluidStack[] {
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 8192),
                Materials.Iridium.getPlasma(1000 * 1152),
                MaterialsAlloy.CINOBITE.getFluidStack(144 * 8192),
                MaterialPool.ConcentratedUUMatter.getFluidOrGas(1000)},
            GTCMItemList.DSPReceiver.get(1),
            20 * 2400,
            (int) TierEU.RECIPE_UIV);

        // ArtificialStar
        // TODO -- Temporarily, be revised in the next version
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.Antimatter.get(1),
            2_000_000_000,
            1677216,
            (int) TierEU.RECIPE_UXV,
            1024,
            new Object[] {
                CustomItemList.eM_Spacetime.get(64),
                CustomItemList.eM_Ultimate_Containment_Field.get(64),
                ItemList.Casing_Dim_Bridge.get(64),
                CustomItemList.eM_Ultimate_Containment_Advanced.get(64),

                ItemList.Field_Generator_UXV.get(64),
                ItemList.Field_Generator_UXV.get(64),
                ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTCMItemList.StellarConstructionFrameMaterial.get(64),
                GTCMItemList.AnnihilationConstrainer.get(64),

                ItemList.ZPM6.get(64), MaterialsUEVplus.MagMatter.getNanite(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.MagMatter, 64),
                GTOreDictUnificator.get(
                    OrePrefixes.plateSuperdense,
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                    64) },
            new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(144 * 262144),
                MaterialsUEVplus.Antimatter.getFluid(144 * 262144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 65536),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 65536) },
            GTCMItemList.ArtificialStar.get(1),
            20 * 4800,
            (int) TierEU.RECIPE_UXV);

        // Strange Matter Aggregator Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.AntimatterFuelRod.get(1),
            1_048_576_000,
            524288,
            (int) TierEU.RECIPE_UXV,
            64,
            new Object[] {
                GTOreDictUnificator.get(OrePrefixes.frameGt,MaterialsUEVplus.SpaceTime,64),
                GTCMItemList.StabilisationFieldGeneratorUMV.get(4),
                GTCMItemList.MatterRecombinator.get(4),
                ItemList.ZPM.get(1),

                GTCMItemList.SpaceWarper.get(64),
                GTCMItemList.SeedsSpaceTime.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(64),
                ItemList.NaquadriaSupersolid.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UXV,64),
                GTCMItemList.UltimateEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16,Materials.SuperconductorUMV,64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense,MaterialsTST.AxonisAlloy,64),

                GTCMItemList.AntimatterFuelRod.get(64),
                GTCMItemList.AntimatterFuelRod.get(64),
                GTCMItemList.AntimatterFuelRod.get(64),
                GTCMItemList.AntimatterFuelRod.get(64)},
            new FluidStack[] {
                Materials.Void.getMolten(144 * 131072),
                MaterialsUEVplus.Space.getMolten(144 * 16384),
                MaterialsUEVplus.Time.getMolten(144 * 16384),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 8192)},
            GTCMItemList.StrangeMatterAggregator.get(1),
            20 * 21600,
            (int) TierEU.RECIPE_UXV);

        // Oscillator T1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.AntimatterFuelRod.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Ultimate_Containment_Field.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),

                GTCMItemList.AntimatterFuelRod.get(64),
                ItemList.Emitter_UMV.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 16384),
                GGMaterial.shirabon.getMolten(144 * 128),
                MaterialsUEVplus.Time.getMolten(144 * 256))
            .itemOutputs(GTCMItemList.SpaceTimeOscillatorT1.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Oscillator T2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeOscillatorT1.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Ultimate_Containment_Field.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                CustomItemList.EOH_Infinite_Energy_Casing.get(4),

                GTCMItemList.StrangeAnnihilationFuelRod.get(64),
                ItemList.Emitter_UXV.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 131072),
                GGMaterial.shirabon.getMolten(144 * 1024),
                MaterialsUEVplus.Time.getMolten(144 * 2048))
            .itemOutputs(GTCMItemList.SpaceTimeOscillatorT2.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Oscillator T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeOscillatorT2.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Ultimate_Containment_Field.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                CustomItemList.EOH_Infinite_Energy_Casing.get(16),

                GTCMItemList.CoreElement.get(64),
                ItemList.Emitter_MAX.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 2097152),
                GGMaterial.shirabon.getMolten(144 * 16384),
                MaterialsUEVplus.Time.getMolten(144 * 32768))
            .itemOutputs(GTCMItemList.SpaceTimeOscillatorT3.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 3600 * 24)
            .addTo(AssemblyLine);

        // Constraintor T1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.ParticleTrapTimeSpaceShield.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Teleportation.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),

                GTCMItemList.AntimatterFuelRod.get(64),
                ItemList.Field_Generator_UMV.get(16),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 16384),
                GGMaterial.shirabon.getMolten(144 * 128),
                MaterialsUEVplus.Space.getMolten(144 * 256))
            .itemOutputs(GTCMItemList.SpaceTimeConstraintorT1.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Constraintor T2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeConstraintorT1.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Teleportation.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                CustomItemList.EOH_Infinite_Energy_Casing.get(4),

                GTCMItemList.StrangeAnnihilationFuelRod.get(64),
                ItemList.Field_Generator_UXV.get(16),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 131072),
                GGMaterial.shirabon.getMolten(144 * 1024),
                MaterialsUEVplus.Space.getMolten(144 * 2048))
            .itemOutputs(GTCMItemList.SpaceTimeConstraintorT2.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Constraintor T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeConstraintorT2.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Teleportation.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                CustomItemList.EOH_Infinite_Energy_Casing.get(16),

                GTCMItemList.CoreElement.get(64),
                ItemList.Field_Generator_MAX.get(16),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 2097152),
                GGMaterial.shirabon.getMolten(144 * 16384),
                MaterialsUEVplus.Space.getMolten(144 * 32768))
            .itemOutputs(GTCMItemList.SpaceTimeConstraintorT3.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 3600 * 24)
            .addTo(AssemblyLine);

        // Merger T1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Spacetime.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),

                GTCMItemList.AntimatterFuelRod.get(64),
                ItemList.Sensor_UMV.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 16384),
                GGMaterial.shirabon.getMolten(144 * 128),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 64))
            .itemOutputs(GTCMItemList.SpaceTimeMergerT1.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Merger T2
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeMergerT1.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Spacetime.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                CustomItemList.EOH_Infinite_Energy_Casing.get(4),

                GTCMItemList.StrangeAnnihilationFuelRod.get(64),
                ItemList.Sensor_UXV.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 131072),
                GGMaterial.shirabon.getMolten(144 * 1024),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 512))
            .itemOutputs(GTCMItemList.SpaceTimeMergerT2.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 2400)
            .addTo(AssemblyLine);

        // Merger T3
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.SpaceTimeMergerT2.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_Spacetime.get(64),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                CustomItemList.EOH_Infinite_Energy_Casing.get(16),

                GTCMItemList.CoreElement.get(64),
                ItemList.Sensor_MAX.get(32),
                GTCMItemList.SpaceWarper.get(32),
                GTCMItemList.StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 2097152),
                GGMaterial.shirabon.getMolten(144 * 16384),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 8192))
            .itemOutputs(GTCMItemList.SpaceTimeMergerT3.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 3600 * 24)
            .addTo(AssemblyLine);

    }
    // spotless:on
}
