package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static bartworks.common.loaders.ItemRegistry.megaMachines;
import static bartworks.common.loaders.ItemRegistry.voidminer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AdvancedMegaOilCracker;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BiosphereIII;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CircuitConverter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CrystallineInfinitier;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DebugUncertaintyHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.InfiniteAirHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.InfiniteWirelessDynamoHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IntensifyChemicalDistorter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.LargeIndustrialCokingFactory;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.LaserSmartNode;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDomainConstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticDrivePressureFormer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MagneticMixer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MegaBrickedBlastFurnace;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MegaMacerator;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MiracleDoor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.MoleculeDeconstructor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.OpticalSOC;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.OreProcessingFactory;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ParticleTrapTimeSpaceShield;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PerfectLapotronCrystal;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhysicalFormSwitcher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Scavenger;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Silksong;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ThermalEnergyDevourer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.VacuumFilterExtractor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessUpdateItem;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
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
import static com.dreammaster.item.ItemList.CircuitUXV;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.enums.ItemList.AssemblingMachineUHV;
import static gregtech.api.enums.ItemList.CompressorUHV;
import static gregtech.api.enums.ItemList.ElectromagneticSeparatorUHV;
import static gregtech.api.enums.ItemList.ExtractorUHV;
import static gregtech.api.enums.ItemList.FluidExtractorUV;
import static gregtech.api.enums.ItemList.FluidSolidifierUV;
import static gregtech.api.enums.ItemList.Machine_Multi_Autoclave;
import static gregtech.api.enums.ItemList.MixerUV;
import static gregtech.api.enums.ItemList.PolarizerUHV;
import static gregtech.api.enums.ItemList.SiftingMachineZPM;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_TIME;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_IndustrialForgeHammer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_IndustrialRockBreaker;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake_Extreme;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Centrifuge;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_CuttingFactoryController;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Electrolyzer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Extruder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_MacerationStack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Mixer;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_PlatePress;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_WireFactory;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Mega_AlloyBlastSmelter;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.item.ItemMiningDrones;

import appeng.api.AEApi;
import appeng.items.materials.MaterialType;
import bartworks.common.loaders.BioItemList;
import bartworks.system.material.WerkstoffLoader;
import ggfab.GGItemList;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import wanion.avaritiaddons.block.extremeautocrafter.BlockExtremeAutoCrafter;

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

        // Molecule Deconstructor
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

        // Crystalline Infinitier
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

        // Scavenger
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_Sifter.get(64),
                SiftingMachineZPM.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 9),
                copyAmount(36, Ic2Items.iridiumPlate),

                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(Scavenger.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 60)
            .addTo(assemblerRecipes);

        // Biosphere III
        ItemStack bioVat = GTModHandler
            .getModItem("gregtech", "gt.blockmahines", 1, 12600 + GTValues.VN.length * 7, ItemList.BreweryUHV.get(1));
        // todo ConfigHandler.IDOffset has been removed

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, bioVat.copy())
            .metadata(RESEARCH_TIME, 16 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 64),
                copyAmount(64, bioVat),
                copyAmount(64, BioItemList.getPetriDish(null)),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 3),

                ItemList.Electric_Pump_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 8),
                ItemList.Circuit_Silicon_Wafer7.get(64),
                ItemList.Circuit_Parts_Chip_Bioware.get(64),
                HighEnergyFlowCircuit.get(16),

                copyAmount(64, Ic2Items.iridiumPlate),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.UV, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64),
                Materials.Naquadria.getMolten(144 * 64),
                Materials.CosmicNeutronium.getMolten(144 * 64),
                GGMaterial.metastableOganesson.getMolten(144 * 32))
            .itemOutputs(BiosphereIII.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);

        // Advanced Oil Cracker
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                megaMachines[4],
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 16),
                ItemList.Electric_Pump_IV.get(4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .itemOutputs(AdvancedMegaOilCracker.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 3600)
            .addTo(assemblerRecipes);

        // Indistinct Tentacle
        TST_RecipeBuilder.builder()
            .itemInputs(
                GGItemList.AdvAssLine.get(64),
                ItemRefer.Component_Assembly_Line.get(64),
                ItemList.AssemblingMachineUMV.get(64),
                ItemRefer.Precise_Assembler.get(64),

                setStackSize(GTCMItemList.StellarConstructionFrameMaterial.get(64), 128),
                setStackSize(GTCMItemList.AnnihilationConstrainer.get(64), 128),
                setStackSize(GTCMItemList.DysonSphereFrameComponent.get(64), 128),
                setStackSize(GTCMItemList.SpaceWarper.get(64), 128),

                setStackSize(GTCMItemList.GravitationalLens.get(64), 128),
                setStackSize(CircuitUXV.getIS(1), 128),
                setStackSize(CustomItemList.eM_Ultimate_Containment_Advanced.get(64), 128))
            .fluidInputs(
                MaterialsUEVplus.SpaceTime.getMolten(144 * 524288),
                MaterialsUEVplus.Space.getMolten(144 * 524288),
                MaterialsUEVplus.Time.getMolten(144 * 524288),
                MaterialsUEVplus.Eternity.getMolten(144 * 524288),

                MaterialsUEVplus.WhiteDwarfMatter.getMolten(144 * 524288),
                MaterialsUEVplus.BlackDwarfMatter.getMolten(144 * 524288),
                MaterialsUEVplus.Universium.getMolten(144 * 524288),
                MaterialsUEVplus.RawStarMatter.getFluid(1000 * 524288),

                GGMaterial.metastableOganesson.getMolten(144 * 524288),
                GGMaterial.shirabon.getMolten(144 * 524288),
                Materials.UUMatter.getFluid(1000 * 2097152),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 2097152))
            .itemOutputs(GTCMItemList.IndistinctTentacle.get(1))
            .eut(TierEU.RECIPE_MAX)
            .duration(20 * 14400)
            .addTo(MiracleTopRecipes);

        // region Mega Egg Generator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.MagicEnergyAbsorber_LV.get(4),
                ItemList.MagicEnergyAbsorber_MV.get(4),
                ItemList.MagicEnergyAbsorber_HV.get(4),
                ItemList.MagicEnergyAbsorber_EV.get(4),
                ItemList.MagicEnergyConverter_LV.get(4),
                ItemList.MagicEnergyConverter_MV.get(4),
                ItemList.MagicEnergyConverter_HV.get(4),
                Materials.Thaumium.getPlates(16),
                GTCMItemList.VoidPollen.get(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(16 * 1000))
            .itemOutputs(GTCMItemList.MegaEggGenerator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(120 * 20)
            .addTo(assemblerRecipes);

        // Thermal Energy Devourer
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, megaMachines[1])
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                copyAmount(64, megaMachines[1]),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),

                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                MaterialType.Singularity.stack(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),

                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16))
            .fluidInputs(
                new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 64),
                Materials.NaquadahAlloy.getMolten(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64))
            .itemOutputs(ThermalEnergyDevourer.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);

        // Debug uncertainty hatch
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                CustomItemList.UncertaintyX_Hatch.get(1),
                CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(DebugUncertaintyHatch.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                CustomItemList.UncertaintyX_Hatch.get(1),
                CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Timepiece.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(DebugUncertaintyHatch.get(16))
            .eut(RECIPE_UXV)
            .duration(20 * 120)
            .addTo(assemblerRecipes);

        // Laser Smart Node
        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.LASERpipe.get(64),
                Laser_Lens_Special.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UEV, 1),
                ItemList.Hatch_Energy_UHV.get(1))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144))
            .itemOutputs(LaserSmartNode.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 5)
            .addTo(assemblerRecipes);

        // VacuumFilterExtractor
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.DistilleryUV.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 64),
                copyAmount(64, megaMachines[2]),
                Materials.Carbon.getNanite(64),
                ItemList.Field_Generator_UHV.get(8),

                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),

                Laser_Lens_Special.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 16),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackPlutonium, 64),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.Neutronium.getMolten(144 * 64),
                Materials.BlackPlutonium.getMolten(144 * 64))
            .itemOutputs(VacuumFilterExtractor.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);

        // Mega Macerator
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Industrial_MacerationStack.get(64),
                ItemList.MaceratorZPM.get(16),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 16),
                GregtechItemList.Maceration_Upgrade_Chip.get(64),
                WerkstoffLoader.AdemicSteel.get(OrePrefixes.gearGt, 16),

                ItemList.Electric_Motor_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plateDense, 16))
            .fluidInputs(new FluidStack(MaterialsAlloy.INDALLOY_140.getFluid(), 144 * 64))
            .itemOutputs(MegaMacerator.get(1))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assemblerRecipes);

        // Hephaestus' Atelier
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Furnace.get(1))
            .metadata(RESEARCH_TIME, 8 * HOURS)
            .itemInputs(
                ItemList.Machine_Multi_Furnace.get(64),
                ItemList.Casing_HeatProof.get(64),
                ItemList.Casing_FrostProof.get(64),
                new ItemStack[] { ItemList.Casing_Coil_AwakenedDraconium.get(64), ItemList.Casing_Coil_Infinity.get(16),
                    ItemList.Casing_Coil_Hypogen.get(4), ItemList.Casing_Coil_Eternal.get(1), },

                CustomItemList.eM_Hollow.get(64),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Conveyor_Module_UHV.get(64),

                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 32),
                HighEnergyFlowCircuit.get(64),
                HighEnergyFlowCircuit.get(64),
                HighEnergyFlowCircuit.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Invar, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.UV, 16))
            .fluidInputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.CosmicNeutronium.getMolten(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 64))
            .itemOutputs(GTCMItemList.HephaestusAtelier.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 600)
            .addTo(AssemblyLine);

        // Deployed Nano Core
        if (Config.Enable_DeployedNanoCore) {
            TST_RecipeBuilder.builder()
                .itemInputs(
                    setStackSize(ItemList.NanoForge.get(1), 512),
                    MaterialsUEVplus.Universium.getNanite(64),
                    setStackSize(MaterialsUEVplus.Eternity.getNanite(1), 128),
                    setStackSize(MaterialsUEVplus.TranscendentMetal.getNanite(1), 1024),

                    SpaceScaler.get(64),
                    GravitationalLens.get(1024),
                    AnnihilationConstrainer.get(1024),
                    DysonSphereFrameComponent.get(1024),

                    PerfectLapotronCrystal.get(2048),
                    setStackSize(ItemList.Field_Generator_UMV.get(1), 1024),
                    setStackSize(ItemList.Emitter_UMV.get(1), 2048),
                    setStackSize(CircuitUXV.getIS(1), 4096),

                    StellarConstructionFrameMaterial.get(2048),
                    setStackSize(GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 1), 4096))
                .fluidInputs(
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 256),
                    Materials.UUMatter.getFluid(2000000),
                    MaterialsUEVplus.ExcitedDTSC.getFluid(1000000),
                    GGMaterial.shirabon.getMolten(144 * 8192),

                    Materials.Neutronium.getMolten(144 * 524288),
                    Materials.CosmicNeutronium.getMolten(144 * 524288),
                    Materials.NaquadahAlloy.getMolten(144 * 524288))
                .itemOutputs(GTCMItemList.DeployedNanoCore.get(1))
                .eut(2000000000)
                .duration(20 * 775500)
                .addTo(MiracleTopRecipes);

            // Core Device of Human Power Generation Facility
            if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(10),
                        ItemList.FluidHeaterUV.get(64),
                        ItemList.Electric_Pump_UV.get(64),

                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 64),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 32),
                        HighEnergyFlowCircuit.get(32),

                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64))
                    .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64))
                    .itemOutputs(GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.get(1))
                    .eut(RECIPE_UV)
                    .duration(20 * 900)
                    .addTo(assemblerRecipes);
            }

            // Starcore Miner
            if (Config.Enable_StarcoreMiner) {
                GTValues.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 1))
                    .metadata(RESEARCH_TIME, 8 * HOURS)
                    .itemInputs(
                        new ItemStack(IGBlocks.SpaceElevatorCasing, 64),
                        copyAmount(64, voidminer[2]),
                        new ItemStack(IGItems.MiningDrones, 18, ItemMiningDrones.DroneTiers.UEV.ordinal()),
                        SpaceWarper.get(18),

                        ItemList.EnergisedTesseract.get(64),
                        ItemList.Electric_Motor_UEV.get(64),
                        ItemList.Field_Generator_UEV.get(48),
                        ItemList.Sensor_UEV.get(64),

                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UIV, 64),
                        HighEnergyFlowCircuit.get(64),
                        CustomItemList.eM_Power.get(64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64))
                    .fluidInputs(
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 1024),
                        Materials.Quantium.getMolten(144 * 1024),
                        Materials.UUMatter.getFluid(1000 * 2048),
                        GGMaterial.metastableOganesson.getMolten(144 * 512))
                    .itemOutputs(GTCMItemList.StarcoreMiner.get(1))
                    .eut(RECIPE_UIV)
                    .duration(20 * 7200)
                    .addTo(AssemblyLine);

            }

            // TST Disassembler
            if (Config.Enable_Disassembler) {
                GTValues.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, ItemList.Machine_LV_Assembler.get(1))
                    .metadata(RESEARCH_TIME, 8 * HOURS)
                    .itemInputs(
                        AssemblingMachineUHV.get(64),
                        ItemList.Field_Generator_UHV.get(16),
                        ItemList.Electric_Pump_UHV.get(64),
                        ItemList.Conveyor_Module_UHV.get(64),

                        ItemList.Robot_Arm_UHV.get(64),
                        ItemList.Robot_Arm_UHV.get(64),
                        ItemList.Robot_Arm_UHV.get(64),
                        ItemList.Robot_Arm_UHV.get(64),

                        CustomItemList.eM_Power.get(64),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16),
                        GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 16))
                    .fluidInputs(
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 64 * 16),
                        Materials.UUMatter.getFluid(1000 * 128),
                        Materials.SuperCoolant.getFluid(1000 * 128 * 6))
                    .itemOutputs(GTCMItemList.Disassembler.get(1))
                    .eut(RECIPE_UEV)
                    .duration(20 * 3600)
                    .addTo(AssemblyLine);

                // Bee Engineer
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTUtility.getIntegratedCircuit(10),
                        GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 64),
                        GTModHandler.getModItem(Forestry.ID, "alveary", 64, 0),

                        ItemList.Field_Generator_LuV.get(4),
                        ItemList.Electric_Pump_LuV.get(16),
                        ItemList.Conveyor_Module_LuV.get(16),

                        GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Plutonium241, 64),
                        GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16))
                    .fluidInputs(Materials.Honey.getFluid(1000 * 256))
                    .itemOutputs(GTCMItemList.BeeEngineer.get(1))
                    .eut(RECIPE_ZPM)
                    .duration(20 * 300)
                    .addTo(assemblerRecipes);

                // Large Canner
                if (Config.Enable_LargeCanner) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            GTUtility.getIntegratedCircuit(10),
                            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                            GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 16),

                            ItemList.FluidCannerZPM.get(16),
                            ItemList.CanningMachineZPM.get(16),
                            GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 16),

                            ItemList.Electric_Pump_ZPM.get(16),
                            ItemList.Conveyor_Module_ZPM.get(8),
                            GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 16))
                        .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 64))
                        .itemOutputs(GTCMItemList.LargeCanner.get(1))
                        .eut(RECIPE_UV)
                        .duration(20 * 300)
                        .addTo(assemblerRecipes);
                }

                // Extreme Craft Center
                ItemStack extremeCraftTable = new ItemStack(BlockExtremeAutoCrafter.instance);
                extremeCraftTable.stackSize = 64;
                // endregion
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        AEApi.instance()
                            .definitions()
                            .blocks()
                            .molecularAssembler()
                            .maybeStack(64)
                            .orNull(),
                        AEApi.instance()
                            .definitions()
                            .blocks()
                            .molecularAssembler()
                            .maybeStack(64)
                            .orNull(),
                        AEApi.instance()
                            .definitions()
                            .blocks()
                            .molecularAssembler()
                            .maybeStack(64)
                            .orNull(),
                        AEApi.instance()
                            .definitions()
                            .blocks()
                            .molecularAssembler()
                            .maybeStack(64)
                            .orNull(),

                        extremeCraftTable.copy(),
                        extremeCraftTable.copy(),
                        extremeCraftTable.copy(),
                        extremeCraftTable.copy(),

                        GregtechItemList.Controller_MolecularTransformer.get(4),
                        ItemList.Field_Generator_UMV.get(4),
                        WirelessUpdateItem.get(4))
                    .fluidInputs(
                        Materials.Infinity.getMolten(144 * 64),
                        MaterialsUEVplus.SpaceTime.getMolten(144 * 64),
                        Materials.UUMatter.getFluid(4000000),
                        Materials.Hydrogen.getPlasma(4000000))
                    .itemOutputs(GTCMItemList.ExtremeCraftCenter.get(1))
                    .eut(RECIPE_UMV)
                    .duration(20 * 500)
                    .addTo(MiracleTopRecipes);
            }

            // Lightning Spire
            if (Config.Enable_LightningSpire) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        CI.getTieredGTPPMachineCasing(5, 1),
                        CI.getEnergyCore(4, 4),
                        CI.getTransmissionComponent(5, 2),
                        ItemList.Field_Generator_HV.get(4),
                        MaterialsAlloy.NITINOL_60.getGear(4),
                        MaterialsElements.getInstance().GERMANIUM.getBolt(16),
                        MaterialsAlloy.NICHROME.getFineWire(16),
                        MaterialsAlloy.NICHROME.getCable16(1))
                    .fluidInputs(Materials.Silicone.getMolten(2304))
                    .itemOutputs(GTCMItemList.LightningSpire.get(1))
                    .eut(RECIPE_IV)
                    .duration(20 * 120)
                    .addTo(assemblerRecipes);
            }

            // Mega Stone Breaker
            ItemStack ExtraUtilitiesNodeUpgrade2 = Mods.ExtraUtilities.isModLoaded()
                ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "nodeUpgrade", 64, 2)
                : new ItemStack(Items.iron_pickaxe);
            ItemStack CompressCobblestone8 = Mods.ExtraUtilities.isModLoaded()
                ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 64, 7)
                : new ItemStack(Blocks.cobblestone);

            if (Config.Enable_MegaStoneBreaker) {
                GTValues.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, Controller_IndustrialRockBreaker.get(1))
                    .metadata(RESEARCH_TIME, 8 * HOURS)
                    .itemInputs(
                        ItemList.Hull_UEV.get(4),
                        ItemList.RockBreakerZPM.get(16),
                        Controller_IndustrialRockBreaker.get(64),
                        GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 64),

                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 1),
                        GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 1),
                        GTOreDictUnificator
                            .get(OrePrefixes.plateSuperdense, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1),

                        ExtraUtilitiesNodeUpgrade2,
                        ExtraUtilitiesNodeUpgrade2,
                        ExtraUtilitiesNodeUpgrade2,
                        ExtraUtilitiesNodeUpgrade2,

                        CompressCobblestone8,
                        CompressCobblestone8,
                        CompressCobblestone8,
                        CompressCobblestone8)
                    .fluidInputs(
                        GGMaterial.adamantiumAlloy.getMolten(9216),
                        Materials.Lava.getFluid(2_000_000_000),
                        Materials.Water.getFluid(2_000_000_000))
                    .itemOutputs(GTCMItemList.MegaStoneBreaker.get(1))
                    .eut(RECIPE_UV)
                    .duration(20 * 120)
                    .addTo(AssemblyLine);
            }
        }
    }
}
