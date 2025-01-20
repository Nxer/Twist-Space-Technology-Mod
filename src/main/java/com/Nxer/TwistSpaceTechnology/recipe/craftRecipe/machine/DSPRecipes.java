package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AntimatterFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ArtificialStar;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CoreElement;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DSPLauncher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DSPReceiver;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SolarSail;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StrangeAnnihilationFuelRod;
import static com.Nxer.TwistSpaceTechnology.common.api.ModItemsHandler.LightWeightPlate;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.QuantumInversionRecipes;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static gregtech.api.enums.ItemList.ZPM6;
import static gregtech.api.enums.TierEU.RECIPE_MAX;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UXV;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing;
import static tectech.thing.CustomItemList.eM_Spacetime;
import static tectech.thing.CustomItemList.eM_Teleportation;
import static tectech.thing.CustomItemList.eM_Ultimate_Containment_Advanced;
import static tectech.thing.CustomItemList.eM_Ultimate_Containment_Field;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.enums.TierEU;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;

import galaxyspace.core.register.GSBlocks;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import tectech.recipe.TTRecipeAdder;

public class DSPRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {

        // DSP Launch Site machine
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(GCBlocks.landingPad, 1, 0),
            20_480_000,
            4096,
            (int) TierEU.RECIPE_UIV,
            16,
            new Object[] { new ItemStack(GCBlocks.landingPad, 64),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                new ItemStack(IGBlocks.SpaceElevatorCasing, 64), new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 9),

                ItemList.Electric_Motor_UEV.get(64), ItemList.Electric_Piston_UEV.get(64),
                ItemList.Robot_Arm_UEV.get(64), ItemList.Sensor_UEV.get(64),

                ItemList.Field_Generator_UEV.get(64), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SixPhasedCopper, 64),
                GTOreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SixPhasedCopper, 64),

                MaterialsUEVplus.TranscendentMetal.getNanite(64), StellarConstructionFrameMaterial.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                setStackSize(LightWeightPlate.getLeft(), 64), },
            new FluidStack[] { new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 8192),
                Materials.UUMatter.getFluid(1000 * 1024), Materials.SuperCoolant.getFluid(1000 * 1024),
                Materials.Bedrockium.getMolten(144 * 8192) },
            DSPLauncher.get(1),
            20 * 2400,
            (int) TierEU.RECIPE_UIV);

        // DSP Ray Receiving Station
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            SolarSail.get(1),
            81_920_0000,
            4096,
            (int) TierEU.RECIPE_UIV,
            64,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                new ItemStack(GSBlocks.DysonSwarmBlocks, 64, 1), new ItemStack(IGBlocks.SpaceElevatorCasing, 64),
                StellarConstructionFrameMaterial.get(64),

                ItemList.Sensor_UEV.get(64), ItemList.Sensor_UEV.get(64), ItemList.Emitter_UEV.get(64),
                ItemList.Emitter_UEV.get(64),

                ItemList.Field_Generator_UEV.get(64), Laser_Lens_Special.get(64), SpaceWarper.get(64),
                ItemList.ZPM4.get(1),

                CustomItemList.HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64) },
            new FluidStack[] { new FluidStack(MaterialMisc.MUTATED_LIVING_SOLDER.generateFluid(), 144 * 8192),
                Materials.UUMatter.getFluid(1000 * 1024), Materials.Grade8PurifiedWater.getFluid(1000 * 1024),
                Materials.CosmicNeutronium.getMolten(144 * 8192) },
            DSPReceiver.get(1),
            20 * 2400,
            (int) TierEU.RECIPE_UIV);

        // ArtificialStar
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            AntimatterFuelRod.get(1),
            1_024_000_000,
            16384,
            (int) TierEU.RECIPE_UXV,
            64,
            new Object[] { eM_Spacetime.get(64), eM_Ultimate_Containment_Field.get(64),
                ItemList.Casing_Dim_Bridge.get(64), eM_Ultimate_Containment_Advanced.get(64),

                ItemList.Field_Generator_UXV.get(64), ItemList.Field_Generator_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                StellarConstructionFrameMaterial.get(64), AnnihilationConstrainer.get(64),

                ZPM6.get(64), MaterialsUEVplus.MagMatter.getNanite(64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.MagMatter, 64),
                GTOreDictUnificator.get(
                    OrePrefixes.plateSuperdense,
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
                    64) },
            new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(144 * 262144),
                MaterialsUEVplus.Antimatter.getFluid(144 * 262144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getFluid(1000 * 65536),
                MaterialsUEVplus.PhononMedium.getFluid(1000 * 65536) },
            ArtificialStar.get(1),
            20 * 4800,
            (int) TierEU.RECIPE_UXV);

        // Strange Matter Aggregator Controller
        TST_RecipeBuilder.builder()
            .itemInputs(
                setStackSize(GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1), 8192),
                AntimatterFuelRod.get(512),
                AnnihilationConstrainer.get(512),
                DysonSphereFrameComponent.get(512),

                SpaceWarper.get(512),
                eM_Spacetime.get(64),
                eM_Ultimate_Containment_Field.get(64),
                eM_Teleportation.get(64),

                setStackSize(ItemList.Field_Generator_UMV.get(1), 512),
                setStackSize(MaterialsUEVplus.TranscendentMetal.getNanite(1), 512),
                setStackSize(ItemList.Tesseract.get(1), 512),
                setStackSize(ItemList.EnergisedTesseract.get(1), 512),

                // TODO quantum circuit
                setStackSize(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1), 512),
                ItemList.ZPM.get(1))
            .fluidInputs(
                Materials.Void.getMolten(144 * 131072),
                MaterialsUEVplus.Space.getMolten(144 * 16384),
                MaterialsUEVplus.Time.getMolten(144 * 16384),
                GGMaterial.shirabon.getMolten(144 * 16384))
            .itemOutputs(GTCMItemList.StrangeMatterAggregator.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 86400)
            .addTo(MiracleTopRecipes);

        // Oscillator T1
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, AntimatterFuelRod.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                eM_Ultimate_Containment_Field.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                EOH_Infinite_Energy_Casing.get(1),

                AntimatterFuelRod.get(64),
                ItemList.Emitter_UMV.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Ultimate_Containment_Field.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                EOH_Infinite_Energy_Casing.get(4),

                StrangeAnnihilationFuelRod.get(64),
                ItemList.Emitter_UXV.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Ultimate_Containment_Field.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                EOH_Infinite_Energy_Casing.get(16),

                CoreElement.get(64),
                ItemList.Emitter_MAX.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
            .metadata(RESEARCH_ITEM, ParticleTrapTimeSpaceShield.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                eM_Teleportation.get(1),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                EOH_Infinite_Energy_Casing.get(1),

                AntimatterFuelRod.get(64),
                ItemList.Field_Generator_UMV.get(16),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Teleportation.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                EOH_Infinite_Energy_Casing.get(4),

                StrangeAnnihilationFuelRod.get(64),
                ItemList.Field_Generator_UXV.get(16),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Teleportation.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                EOH_Infinite_Energy_Casing.get(16),

                CoreElement.get(64),
                ItemList.Field_Generator_MAX.get(16),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Spacetime.get(4),
                GregtechItemList.SpaceTimeContinuumRipper.get(1),
                GregtechItemList.SpaceTimeBendingCore.get(1),
                EOH_Infinite_Energy_Casing.get(1),

                AntimatterFuelRod.get(64),
                ItemList.Sensor_UMV.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Spacetime.get(16),
                GregtechItemList.SpaceTimeContinuumRipper.get(4),
                GregtechItemList.SpaceTimeBendingCore.get(4),
                EOH_Infinite_Energy_Casing.get(4),

                StrangeAnnihilationFuelRod.get(64),
                ItemList.Sensor_UXV.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
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
                eM_Spacetime.get(64),
                GregtechItemList.SpaceTimeContinuumRipper.get(16),
                GregtechItemList.SpaceTimeBendingCore.get(16),
                EOH_Infinite_Energy_Casing.get(16),

                CoreElement.get(64),
                ItemList.Sensor_MAX.get(32),
                SpaceWarper.get(32),
                StellarConstructionFrameMaterial.get(64))
            .fluidInputs(
                Materials.Void.getMolten(144 * 2097152),
                GGMaterial.shirabon.getMolten(144 * 16384),
                MaterialsUEVplus.PrimordialMatter.getFluid(1000 * 8192))
            .itemOutputs(GTCMItemList.SpaceTimeMergerT3.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 3600 * 24)
            .addTo(AssemblyLine);

        // Core Element
        TST_RecipeBuilder.builder()
            .itemInputs(GTCMItemList.MatterRecombinator.get(0), ItemList.ZPM.get(1))
            .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(1))
            .itemOutputs(GTCMItemList.EnergyShard.get(1), GTCMItemList.CoreElement.get(1))
            .outputChances(9990, 10)
            .eut(RECIPE_MAX)
            .duration(20 * 60)
            .addTo(QuantumInversionRecipes);

        // Matter Recombinator
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                ItemList.Field_Generator_UMV.get(4),
                ItemList.Emitter_UMV.get(8),

                GravitationalLens.get(32),
                Laser_Lens_Special.get(16),
                Materials.Void.getPlates(64),

                MaterialsUEVplus.WhiteDwarfMatter.getNanite(1),
                MaterialsUEVplus.BlackDwarfMatter.getNanite(1))
            .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
            .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
            .outputChances(114)
            .eut(RECIPE_UMV)
            .duration(20 * 200)
            .addTo(RecipeMaps.assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                ItemList.Field_Generator_UMV.get(4),
                ItemList.Emitter_UMV.get(8),

                GravitationalLens.get(32),
                Laser_Lens_Special.get(16),
                Materials.Void.getPlates(64),

                MaterialsUEVplus.Eternity.getNanite(1),
                MaterialsUEVplus.Universium.getNanite(1))
            .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
            .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
            .outputChances(1140)
            .eut(RECIPE_UXV)
            .duration(20 * 300)
            .addTo(RecipeMaps.assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                ItemList.Field_Generator_UMV.get(4),
                ItemList.Emitter_UMV.get(8),

                GravitationalLens.get(32),
                Laser_Lens_Special.get(16),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getPlates(16),

                MaterialsUEVplus.Eternity.getNanite(1),
                MaterialsUEVplus.Universium.getNanite(1))
            .fluidInputs(MaterialsUEVplus.ExcitedDTSC.getFluid(100))
            .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
            .outputChances(5140)
            .eut(RECIPE_MAX)
            .duration(20 * 300)
            .addTo(RecipeMaps.assemblerRecipes);

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.MatterRecombinator.get(0),
                ItemList.Field_Generator_UMV.get(1),
                ItemList.Emitter_UMV.get(2),
                GTCMItemList.EnergyShard.get(16),

                GravitationalLens.get(6),
                Laser_Lens_Special.get(6),
                Materials.Void.getPlates(64))
            .fluidInputs(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 8))
            .itemOutputs(GTCMItemList.MatterRecombinator.get(1))
            .eut(RECIPE_MAX)
            .duration(20 * 300)
            .addTo(MiracleTopRecipes);

        // endregion

    }
}
