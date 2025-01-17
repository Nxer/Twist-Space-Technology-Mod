package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machineRecipeList;

import static bartworks.common.loaders.ItemRegistry.megaMachines;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CircuitConverter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CrystallineInfinitier;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.InfiniteAirHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.InfiniteWirelessDynamoHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.LargeIndustrialCokingFactory;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDomainConstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDrivePressureFormer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticMixer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MegaBrickedBlastFurnace;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleDoor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.OpticalSOC;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.OreProcessingFactory;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhysicalFormSwitcher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Silksong;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.enums.ItemList.CompressorUHV;
import static gregtech.api.enums.ItemList.ElectromagneticSeparatorUHV;
import static gregtech.api.enums.ItemList.ExtractorUHV;
import static gregtech.api.enums.ItemList.FluidExtractorUV;
import static gregtech.api.enums.ItemList.FluidSolidifierUV;
import static gregtech.api.enums.ItemList.Machine_Multi_Autoclave;
import static gregtech.api.enums.ItemList.MixerUV;
import static gregtech.api.enums.ItemList.PolarizerUHV;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_IndustrialForgeHammer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake_Extreme;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Centrifuge;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_CuttingFactoryController;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Electrolyzer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Extruder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Mixer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_PlatePress;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_WireFactory;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Mega_AlloyBlastSmelter;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class SingleMachineRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Intensify Chemical Distorter
        GTValues.RA.stdBuilder()
            .itemInputs(
                copyAmount(1, megaMachines[3]),
                Materials.Carbon.getNanite(16),
                ItemList.Emitter_UV.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .itemOutputs(IntensifyChemicalDistorter.get(1))
            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        // Miracle Top
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 12735),
            51_200_000,
            4096,
            (int) RECIPE_UIV,
            64,
            new Object[] { GTModHandler.getModItem("gregtech", "gt.blockmachines", 64, 12735),
                Component_Assembly_Line.get(64), SpaceWarper.get(64), MaterialsUEVplus.TranscendentMetal.getNanite(48),

                CustomItemList.eM_Coil.get(64), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),

                OpticalSOC.get(64), GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14), OpticalSOC.get(64),

                CustomItemList.eM_Spacetime.get(64), ItemList.Field_Generator_UIV.get(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, MaterialsUEVplus.TranscendentMetal, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1024 * 144),
                MaterialsUEVplus.SpaceTime.getMolten(16 * 144), Materials.SuperconductorUIVBase.getMolten(256 * 144),
                MaterialsUEVplus.DimensionallyShiftedSuperfluid.getMolten(64 * 1000) },
            GTCMItemList.MiracleTop.get(1),
            20 * 3600,
            (int) RECIPE_UMV);

        // Magnetic Drive Pressure Former
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 1),
            512_000,
            256,
            (int) RECIPE_UV,
            64,
            new Object[] { ItemList.Casing_MiningOsmiridium.get(64), Industrial_Extruder.get(64),
                Industrial_PlatePress.get(64), Controller_IndustrialForgeHammer.get(64),

                ItemList.UV_Coil.get(64), GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64), ItemList.UV_Coil.get(64),

                ItemList.Field_Generator_UV.get(8), ItemList.Robot_Arm_UV.get(16), ItemList.Conveyor_Module_UV.get(32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 64),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Iridium, 64) },
            new FluidStack[] { new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 256),
                Materials.Terbium.getMolten(144 * 256),
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 1000 * 256),
                Materials.Lubricant.getFluid(1000 * 256) },
            MagneticDrivePressureFormer.get(1),
            20 * 256,
            (int) RECIPE_UHV);

        // Physical Form Switcher
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTModHandler.getModItem(Mods.GregTech.ID, "gt.metaitem.99", 1, 397),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            16,
            new Object[] { ItemList.Machine_Multi_Solidifier.get(64), ItemList.LargeFluidExtractor.get(64),
                FluidSolidifierUV.get(64), FluidExtractorUV.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                ItemList.Superconducting_Magnet_Solenoid_UHV.get(28),
                GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, Materials.NetherStar, 16),
                ItemList.Electric_Pump_UV.get(8),

                ItemList.Field_Generator_UV.get(16), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                MaterialsAlloy.QUANTUM.getRotor(32), MaterialsAlloy.OCTIRON.getRotor(32),

                GTModHandler.getModItem(Mods.GregTech.ID, "gt.metaitem.01", 1, 32413), Materials.Silver.getNanite(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                Materials.UUMatter.getFluid(1000 * 512),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 4096),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(144 * 8192) },
            PhysicalFormSwitcher.get(1),
            20 * 180,
            (int) RECIPE_UHV);

        // Magnetic Mixer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                Industrial_Mixer.get(64),
                MixerUV.get(64),

                ItemList.Field_Generator_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.Iridium.getMolten(144 * 64))
            .itemOutputs(MagneticMixer.get(1))

            .noOptimize()
            .eut(RECIPE_UV)
            .duration(20 * 384)
            .addTo(assemblerRecipes);

        // Infinity Air Intake Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Hatch_Air_Intake_Extreme.get(4),
                ItemList.Electric_Pump_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 16))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(144 * 16))
            .itemOutputs(InfiniteAirHatch.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        // Magnetic Domain Constructor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_IndustrialElectromagneticSeparator.get(1),
            4_096_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] { ItemList.Machine_Multi_IndustrialElectromagneticSeparator.get(64),
                ElectromagneticSeparatorUHV.get(16), PolarizerUHV.get(16), ItemList.Electromagnet_Tengam.get(8),

                ItemList.Robot_Arm_UEV.get(16), ItemList.Conveyor_Module_UEV.get(8),
                ItemList.Field_Generator_UEV.get(4), GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.BlackPlutonium, 8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                GGMaterial.tairitsu.getMolten(144 * 256), Materials.TengamAttuned.getMolten(144 * 1024),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(144 * 4096) },
            MagneticDomainConstructor.get(1),
            20 * 640,
            (int) RECIPE_UEV);

        // Silksong
        GTValues.RA.stdBuilder()
            .itemInputs(
                Industrial_WireFactory.get(64),
                ItemList.WiremillUV.get(16),
                HighEnergyFlowCircuit.get(8),

                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Conveyor_Module_ZPM.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.Iridium.getMolten(144 * 32))
            .itemOutputs(Silksong.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 600)
            .addTo(assemblerRecipes);

        // Holy Separator
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Industrial_CuttingFactoryController.get(1),
            1_024_000,
            512,
            (int) RECIPE_UHV,
            16,
            new Object[] { Industrial_CuttingFactoryController.get(64), ItemList.CuttingMachineUHV.get(32),
                ItemList.SlicingMachineUHV.get(32), CustomItemList.eM_Power.get(16),

                ItemList.Field_Generator_UHV.get(16), ItemList.Emitter_UHV.get(64), ItemList.Emitter_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(8),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16), ItemList.Circuit_Chip_PPIC.get(64),
                ItemList.Circuit_Chip_PPIC.get(64),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CallistoIce, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Ledox, 64),
                copyAmount(64, Ic2Items.iridiumPlate) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.Naquadria.getMolten(144 * 256), Materials.SuperCoolant.getFluid(1000 * 512),
                Materials.Lubricant.getFluid(1000 * 1024) },
            HolySeparator.get(1),
            20 * 1200,
            (int) RECIPE_UHV);

        // Space Scaler
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            SpaceWarper.get(1),
            4_096_000,
            2048,
            (int) RECIPE_UIV,
            16,
            new Object[] { ItemList.Machine_Multi_IndustrialExtractor.get(64), CompressorUHV.get(64),
                ExtractorUHV.get(64), GTOreDictUnificator.get(OrePrefixes.block, Materials.CosmicNeutronium, 64),

                ItemList.Field_Generator_UEV.get(32), ItemList.Field_Generator_UHV.get(64),
                ItemList.Field_Generator_UHV.get(64),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64), HighEnergyFlowCircuit.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                Materials.UUMatter.getFluid(1000 * 256), Materials.SuperCoolant.getFluid(1000 * 256),
                Materials.NaquadahAlloy.getMolten(144 * 256) },
            SpaceScaler.get(1),
            20 * 1800,
            (int) RECIPE_UIV);

        // region Molecule Deconstructor
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Materials.Neutronium.getNanite(1),
            512_000,
            256,
            (int) RECIPE_UV,
            16,
            new Object[] { Industrial_Electrolyzer.get(64), Industrial_Centrifuge.get(64),
                ItemList.ElectrolyzerUV.get(64), ItemList.CentrifugeUV.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                Materials.Silver.getNanite(16), ItemList.Robot_Arm_UV.get(64), ItemList.Electric_Pump_UV.get(32),

                ItemList.Emitter_UV.get(16), ItemList.Field_Generator_UV.get(8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 64),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 256), Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 128) },
            MoleculeDeconstructor.get(1),
            20 * 600,
            (int) RECIPE_UHV);

        // endregion

        // region CrystallineInfinitier
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Machine_Multi_Autoclave.get(1),
            2_048_000,
            512,
            (int) RECIPE_UEV,
            16,
            new Object[] { CustomItemList.eM_Containment_Field.get(4), Machine_Multi_Autoclave.get(64),
                ItemList.AutoclaveUEV.get(64), ItemList.ChemicalBathUEV.get(64),

                ItemList.Field_Generator_UEV.get(32), ItemList.Electric_Pump_UEV.get(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 64),

                ItemList.EnergisedTesseract.get(64), ItemList.NuclearStar.get(64), ItemList.Circuit_Wafer_PPIC.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 64),

                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 256),
                GGMaterial.tairitsu.getMolten(144 * 512), Materials.StableBaryonicMatter.getFluid(1000 * 64),
                Materials.UUMatter.getFluid(1000 * 128) },
            CrystallineInfinitier.get(1),
            20 * 1800,
            (int) RECIPE_UEV);

        // Miracle Door
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_Teleportation.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),

                CustomItemList.eM_Teleportation.get(64),
                StellarConstructionFrameMaterial.get(64),
                ItemList.Emitter_UMV.get(64),
                ItemList.Field_Generator_UMV.get(64),

                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64),
                HighEnergyFlowCircuit.get(64),

                SpaceWarper.get(64),
                GravitationalLens.get(64),
                ParticleTrapTimeSpaceShield.get(64),
                ItemList.EnergisedTesseract.get(64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 65536),
                MaterialsAlloy.PIKYONIUM.getFluidStack(144 * 65536),
                Materials.UUMatter.getFluid(1000 * 65536),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16384))
            .itemOutputs(MiracleDoor.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 3600)
            .addTo(AssemblyLine);

        // Infinite Dynamo Hatch
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CustomItemList.eM_dynamoTunnel5_UEV.get(1))
            .metadata(RESEARCH_TIME, 24 * HOURS)
            .itemInputs(
                CustomItemList.eM_dynamoTunnel5_UEV.get(1),
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                CustomItemList.Machine_Multi_Transformer.get(1),

                ItemList.Casing_Dim_Injector.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 4),

                ItemList.Field_Generator_UEV.get(2),
                ItemList.EnergisedTesseract.get(4),

                GravitationalLens.get(16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 36),
                Materials.UUMatter.getFluid(1000 * 8),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 64))
            .itemOutputs(InfiniteWirelessDynamoHatch.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 20)
            .addTo(AssemblyLine);

        // Ore Processing Factory
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Ore_Processor.get(1))
            .metadata(RESEARCH_TIME, 16 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Ore_Processor.get(64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 64),

                ItemList.Electric_Motor_UEV.get(64),
                ItemList.Electric_Pump_UEV.get(16),
                ItemList.Conveyor_Module_UEV.get(16),
                ItemList.Robot_Arm_UEV.get(16),

                Materials.Neutronium.getNanite(16),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Iridium, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.StainlessSteel, 64),
                CustomItemList.eM_Power.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 512),
                Materials.TungstenSteel.getMolten(144 * 1024),
                Materials.Neutronium.getMolten(144 * 1024),
                Materials.Osmiridium.getMolten(144 * 1024))
            .itemOutputs(OreProcessingFactory.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 1800)
            .addTo(AssemblyLine);

        // Circuit Converter
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                ItemList.Casing_Processor.get(3),
                ItemList.Machine_IV_Boxinator.get(1),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),

                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(CircuitConverter.get(1))

            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 30)
            .addTo(assemblerRecipes);

        // Large Industrial Coking Factory
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_CokeOven.get(64),
                ItemList.PyrolyseOven.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),
                HighEnergyFlowCircuit.get(16),

                ItemList.Electric_Pump_UV.get(6),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.BlackSteel, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 96))
            .itemOutputs(LargeIndustrialCokingFactory.get(1))

            .noOptimize()
            .eut(RECIPE_UHV)
            .duration(20 * 128)
            .addTo(assemblerRecipes);

        // Mega Bricked Blast Furnace
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Firebricks.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Casing_Firebricks.get(64),

                ItemList.Machine_Bricked_BlastFurnace.get(64),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 16),
                ItemList.Machine_Bricked_BlastFurnace.get(64))

            .itemOutputs(MegaBrickedBlastFurnace.get(1))

            .noOptimize()
            .eut(RECIPE_LV)
            .duration(20 * 114)
            .addTo(assemblerRecipes);
    }
}
