package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.machine;

import static bartworks.common.loaders.ItemRegistry.megaMachines;
import static bartworks.common.loaders.ItemRegistry.voidminer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AdvancedHighPowerCoilBlock;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AdvancedMegaOilCracker;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AnnihilationConstrainer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Antimatter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.AstralComputingArray;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BallLightning;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BallLightningUpgradeChip;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.BiosphereIII;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CircuitConverter;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CompactCyclotronCoil;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CriticalPhoton;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.CrystallineInfinitier;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DebugUncertaintyHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DenseCyclotronOuterCasing;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_IV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_LuV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_UV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DualInputBuffer_ZPM;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.DysonSphereFrameComponent;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GiantVacuumDryingFurnace;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.GravitationalLens;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HighPowerRadiationProofCasing;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.HolySeparator;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IncompactCyclotron;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.IndustrialMagnetarSeparator;
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
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeLV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeLuV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeMAX;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUEV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUHV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUIV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUMV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeUXV;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhotonControllerUpgradeZPM;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.PhysicalFormSwitcher;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.RealRackHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Scavenger;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.Silksong;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceScaler;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.SpaceWarper;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.StellarConstructionFrameMaterial;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.TestItem0;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.ThermalEnergyDevourer;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.VacuumFilterExtractor;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WhiteDwarfMold_Ingot;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessDataInputHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessDataOutputHatch;
import static com.Nxer.TwistSpaceTechnology.common.GTCMItemList.WirelessUpdateItem;
import static com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe.MiracleTopRecipes;
import static com.Nxer.TwistSpaceTechnology.util.RecipeUtils.getCircuits;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_EV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_HV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_IV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_LuV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UEV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UIV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UXV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_ZPM;
import static com.dreammaster.gthandler.CustomItemList.HighEnergyFlowCircuit;
import static com.dreammaster.gthandler.CustomItemList.PicoWafer;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static goodgenerator.util.ItemRefer.HiC_T5;
import static gregtech.api.enums.ItemList.ArcFurnaceUEV;
import static gregtech.api.enums.ItemList.AssemblingMachineUHV;
import static gregtech.api.enums.ItemList.CompressorUHV;
import static gregtech.api.enums.ItemList.ElectromagneticSeparatorUHV;
import static gregtech.api.enums.ItemList.ExtractorUHV;
import static gregtech.api.enums.ItemList.FluidExtractorUV;
import static gregtech.api.enums.ItemList.FluidSolidifierUV;
import static gregtech.api.enums.ItemList.MixerUV;
import static gregtech.api.enums.ItemList.PlasmaArcFurnaceUEV;
import static gregtech.api.enums.ItemList.PolarizerUHV;
import static gregtech.api.enums.ItemList.SiftingMachineZPM;
import static gregtech.api.enums.ItemList.Transformer_UMV_UIV;
import static gregtech.api.enums.ItemList.Transformer_UXV_UMV;
import static gregtech.api.enums.ItemList.ZPM3;
import static gregtech.api.enums.ItemList.ZPM6;
import static gregtech.api.enums.Materials.RadoxPolymer;
import static gregtech.api.enums.Mods.AE2WCT;
import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GoodGenerator;
import static gregtech.api.util.GTModHandler.addCraftingRecipe;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTUtility.copyAmount;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.COMET_Cyclotron;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_AdvancedVacuum;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Cyclotron_Coil;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Cyclotron_External;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Casing_Industrial_Arc_Furnace;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_IndustrialRockBreaker;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_Vacuum_Furnace;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.GTPP_Casing_UHV;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.GT_Dehydrator_ZPM;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Hatch_Air_Intake_Extreme;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Arc_Furnace;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_Extruder;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_MacerationStack;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_MassFab;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Industrial_PlatePress;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Laser_Lens_Special;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Mega_AlloyBlastSmelter;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TransmissionComponent_UV;
import static tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing;
import static tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing;
import static tectech.thing.CustomItemList.LASERpipe;
import static tectech.thing.CustomItemList.Machine_Multi_Computer;
import static tectech.thing.CustomItemList.Machine_Multi_Switch;
import static tectech.thing.CustomItemList.Machine_Multi_Transformer;
import static tectech.thing.CustomItemList.SpacetimeCompressionFieldGeneratorTier8;
import static tectech.thing.CustomItemList.StabilisationFieldGeneratorTier8;
import static tectech.thing.CustomItemList.TimeAccelerationFieldGeneratorTier8;
import static tectech.thing.CustomItemList.UncertaintyX_Hatch;
import static tectech.thing.CustomItemList.dataIn_Hatch;
import static tectech.thing.CustomItemList.dataOut_Hatch;
import static tectech.thing.CustomItemList.eM_Coil;
import static tectech.thing.CustomItemList.eM_Containment_Field;
import static tectech.thing.CustomItemList.eM_Hollow;
import static tectech.thing.CustomItemList.eM_Spacetime;
import static tectech.thing.CustomItemList.eM_Teleportation;
import static tectech.thing.CustomItemList.eM_Ultimate_Containment;
import static tectech.thing.CustomItemList.eM_Ultimate_Containment_Advanced;
import static tectech.thing.CustomItemList.eM_Ultimate_Containment_Field;
import static tectech.thing.CustomItemList.hatch_CreativeMaintenance;
import static tectech.thing.CustomItemList.rack_Hatch;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler;
import com.Nxer.TwistSpaceTechnology.common.block.meta.multiuse.BlockMultiUseCore;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.BloodyHellTierKey;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.util.BloodMagicHelper;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import appeng.api.AEApi;
import appeng.items.materials.MaterialType;
import bartworks.common.loaders.BioItemList;
import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.CircuitGeneration.BWMetaItems;
import bartworks.system.material.WerkstoffLoader;
import fox.spiteful.avaritia.items.LudicrousItems;
import ggfab.GGItemList;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.ItemRefer;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtnhintergalactic.recipe.IGRecipeMaps;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.core.Ic2Items;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.casing.TTCasingsContainer;

@Deprecated
@SuppressWarnings("SpellCheckingInspection")
public class GTCMMachineRecipes {

    public static void loadRecipes() {
        TwistSpaceTechnology.LOG.info("GTCMMachineRecipePool loading recipes.");

        Fluid solderIndAlloy = FluidRegistry.getFluid("molten.indalloy140");

        Fluid solderPlasma = FluidRegistry.getFluid("molten.mutatedlivingsolder");

        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        IItemContainer eM_Power = tectech.thing.CustomItemList.eM_Power;

        final IRecipeMap assemblyLine = GTRecipeConstants.AssemblyLine;
        final IRecipeMap assembler = RecipeMaps.assemblerRecipes;
        ItemStack FarmGear;
        ItemStack FarmOutput;
        ItemStack FarmPump;
        ItemStack FarmController;
        if (Forestry.isModLoaded()) {
            FarmGear = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 2);
            FarmOutput = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 3);
            FarmPump = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 4);
            FarmController = GTModHandler.getModItem(Forestry.ID, "ffarm", 1, 5);
        } else {
            FarmGear = new ItemStack(Blocks.stonebrick, 1);
            FarmOutput = new ItemStack(Blocks.stonebrick, 1);
            FarmPump = new ItemStack(Blocks.stonebrick, 1);
            FarmController = new ItemStack(Blocks.stonebrick, 1);
        }

        ItemStack ExtraUtilitiesNodeUpgrade2 = Mods.ExtraUtilities.isModLoaded()
            ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "nodeUpgrade", 64, 2)
            : new ItemStack(Items.iron_pickaxe);
        ItemStack CompressCobblestone8 = Mods.ExtraUtilities.isModLoaded()
            ? GTModHandler.getModItem(Mods.ExtraUtilities.ID, "cobblestone_compressed", 64, 7)
            : new ItemStack(Blocks.cobblestone);

        // spotless:off

        // test machine recipe
        /*
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000),
                Materials.Helium.getGas(1000),
                WerkstoffLoader.Neon.getFluidOrGas(1000),
                Materials.Argon.getGas(1000),
                WerkstoffLoader.Krypton.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(1000),
                Materials.Radon.getGas(1000))
            .fluidOutputs(MaterialPool.TestingMaterial.getMolten(144))
            // GTNH Version 2.4.1+ don't need call this method , BUT!
            .specialValue(11451) // set special value, like HeatCapacity is the special value of EBF recipes
            .noOptimize() // disable the auto optimize of GT Machine recipes
            .eut(1919810)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);

         */
        /*
         * 2.4.0 and earlier need call these methods:
         * noItemInputs(); noItemOutputs(); noFluidInputs(); noFluidOutputs();
         * So had better call.
         */

        // Intensify Chemical Distorter
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTUtility.copyAmountUnsafe(1, megaMachines[3]),
                Materials.Carbon.getNanite(16),
                ItemList.Emitter_UV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16})
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))

            .itemOutputs(IntensifyChemicalDistorter.get(1))
                        .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assembler);

        // region PreciseHighEnergyPhotonicQuantumMaster

        // PreciseHighEnergyPhotonicQuantumMaster Controller
        GTValues.RA.stdBuilder()
            .itemInputs(
                eM_Power.get(4),
                getModItem("gregtech", "gt.blockmachines", 8, 10932),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                getModItem(GTPlusPlus.ID, "MU-metaitem.01", 1L, 32105),
                ItemList.Emitter_UV.get(5),
                ItemList.Field_Generator_UV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4},
                GTUtility.copyAmountUnsafe(64, Ic2Items.iridiumPlate),
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
                        .eut(RECIPE_UHV)
            .duration(20 * 120)
            .addTo(assembler);

        // Upgrade LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11100),
                ItemList.Transformer_MV_LV.get(1),
                ItemList.Emitter_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 3),
                ItemList.Field_Generator_LV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Basic), 4},
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 2))
            .itemOutputs(PhotonControllerUpgradeLV.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 10)
            .addTo(assembler);

        // Upgrade MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11101),
                ItemList.Transformer_HV_MV.get(1),
                ItemList.Emitter_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 6),
                ItemList.Field_Generator_MV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Good), 4},
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 8))
            .itemOutputs(PhotonControllerUpgradeMV.get(1))
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(assembler);

        // Upgrade HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11102),
                ItemList.Transformer_EV_HV.get(1),
                ItemList.Emitter_HV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 12),
                ItemList.Field_Generator_HV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Advanced), 4},
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(PhotonControllerUpgradeHV.get(1))
            .eut(RECIPE_HV)
            .duration(20 * 40)
            .addTo(assembler);

        // Upgrade EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11103),
                ItemList.Transformer_IV_EV.get(1),
                ItemList.Emitter_EV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 24),
                ItemList.Field_Generator_EV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Data), 4},
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 128))
            .itemOutputs(PhotonControllerUpgradeEV.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 80)
            .addTo(assembler);

        // Upgrade IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11104),
                ItemList.Transformer_LuV_IV.get(1),
                ItemList.Emitter_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 48),
                ItemList.Field_Generator_IV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 4},
                GTUtility.getIntegratedCircuit(10))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 512))
            .itemOutputs(PhotonControllerUpgradeIV.get(1))

            .eut(RECIPE_IV)
            .duration(20 * 160)
            .addTo(assembler);

        // Upgrade LuV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105))
            .metadata(SCANNING, new Scanning(1 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11105),
                ItemList.Transformer_ZPM_LuV.get(1),
                eM_Power.get(4),
                ItemList.Emitter_LuV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 4),
                ItemList.Field_Generator_LuV.get(2),
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 4))
            .itemOutputs(PhotonControllerUpgradeLuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 20)
            .addTo(AssemblyLine);

        // Upgrade ZPM
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11106),
                ItemList.Transformer_UV_ZPM.get(1),
                eM_Power.get(4),
                ItemList.Emitter_ZPM.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 8),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 8),
                ItemList.Field_Generator_ZPM.get(2),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 8))
            .itemOutputs(PhotonControllerUpgradeZPM.get(1))

            .eut(RECIPE_ZPM)
            .duration(20 * 40)
            .addTo(AssemblyLine);

        // Upgrade UV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 11107),
                ItemList.Transformer_MAX_UV.get(1),
                eM_Power.get(4),
                ItemList.Emitter_UV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 16),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 16),
                ItemList.Field_Generator_UV.get(2),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 4))
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 16))
            .itemOutputs(PhotonControllerUpgradeUV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 80)
            .addTo(AssemblyLine);

        // Upgrade UHV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GTCMItemList.PreciseHighEnergyPhotonicQuantumMaster.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 8, 11107),
                ItemList.Transformer_UEV_UHV.get(1),
                eM_Power.get(4),

                ItemList.Emitter_UHV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 32),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 32),
                ItemList.Field_Generator_UHV.get(2),

                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 16 * 144)
            )
            .itemOutputs(
                PhotonControllerUpgradeUHV.get(1)
            )
            .eut(RECIPE_UHV)
            .duration(20 * 160)
            .addTo(AssemblyLine);

        // Upgrade UEV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUHV.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                GTModHandler.getModItem("gregtech", "gt.blockmachines", 64, 11107),
                ItemList.Transformer_UIV_UEV.get(1),
                eM_Power.get(4),

                ItemList.Emitter_UEV.get(4),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.lens, 64),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UEV.get(2),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUEV, 4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 64 * 144),
                Materials.SuperconductorUEVBase.getMolten(64 * 144)
            )
            .itemOutputs(
                PhotonControllerUpgradeUEV.get(1)
            )

            .eut(RECIPE_UEV)
            .duration(20 * 320)
            .addTo(AssemblyLine);

        // Upgrade UIV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, PhotonControllerUpgradeUEV.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                SpaceWarper.get(1),
                Transformer_UMV_UIV.get(1),
                ItemList.Casing_Dim_Injector.get(1),

                ItemList.Emitter_UIV.get(4),
                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 4L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UIV.get(2),

                new Object[]{OrePrefixes.circuit.get(Materials.Optical), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUIV, 8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144)
            )
            .itemOutputs(
                PhotonControllerUpgradeUIV.get(1)
            )

            .eut(RECIPE_UIV)
            .duration(20 * 640)
            .addTo(AssemblyLine);

        // Upgrade UMV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, SpaceWarper.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_Advanced_Iridium.get(1),
                SpaceWarper.get(4),
                Transformer_UXV_UMV.get(1),
                ItemList.Casing_Dim_Injector.get(4),

                ItemList.Emitter_UMV.get(8),
                GTModHandler.getModItem(GTPlusPlus.ID, "MU-metaitem.01", 16L, 32105),
                WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 64),
                ItemList.Field_Generator_UMV.get(4),

                new Object[]{OrePrefixes.circuit.get(Materials.UMV), 8},
                new Object[]{OrePrefixes.circuit.get(Materials.UIV), 32},
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUMV, 16),
                MaterialsUEVplus.TranscendentMetal.getNanite(16),

                EOH_Reinforced_Temporal_Casing.get(4)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 256 * 144),
                Materials.SuperconductorUMVBase.getMolten(64 * 144),
                MaterialsUEVplus.Space.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8)
            )
            .itemOutputs(
                PhotonControllerUpgradeUMV.get(1)
            )

            .eut(RECIPE_UMV)
            .duration(20 * 1280)
            .addTo(AssemblyLine);

        // Upgrade UXV
        GTValues.RA.stdBuilder()
            .itemInputs(
                PhotonControllerUpgradeUMV.get(1),
                getCircuits(Materials.UXV, 64),
                EOH_Infinite_Energy_Casing.get(16),
                ItemList.Transformer_MAX_UXV.get(1),
                SpaceWarper.get(16),
                ItemList.Tesseract.get(64),
                ItemList.Emitter_UXV.get(16),
                ItemList.Field_Generator_UXV.get(8),
                SpacetimeCompressionFieldGeneratorTier8.get(1),
                TimeAccelerationFieldGeneratorTier8.get(1),
                StabilisationFieldGeneratorTier8.get(1),
                MaterialsUEVplus.Universium.getNanite(16),
                EOH_Reinforced_Temporal_Casing.get(16))
            .itemOutputs(PhotonControllerUpgradeUXV.get(1))
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(144 * 64),
                MaterialsUEVplus.Space.getMolten(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64),
                Materials.SuperconductorUMVBase.getMolten(128 * 144),
                Materials.SuperconductorUIVBase.getMolten(256 * 144),
                Materials.SuperconductorUEVBase.getMolten(512 * 144),
                GGMaterial.shirabon.getMolten(9 * 144 * 144))

            .eut(RECIPE_UXV)
            .duration(2560 * 20)
            .addTo(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // Upgrade MAX
        GTValues.RA.stdBuilder()
            .itemInputs(
                PhotonControllerUpgradeUXV.get(1),
                SpaceWarper.get(64),
                getCircuits(Materials.UXV, 64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 15),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 16),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 20),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 21),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 17),
                MaterialsUEVplus.Eternity.getNanite(16),
                MaterialsUEVplus.Universium.getNanite(16),
                ItemList.Timepiece.get(64),
                ItemList.EnergisedTesseract.get(64),
                GTModHandler.getModItem("eternalsingularity", "combined_singularity", 3, 15))
            .itemOutputs(PhotonControllerUpgradeMAX.get(1))
            .fluidInputs(
                MaterialsUEVplus.Universium.getMolten(144 * 64),
                MaterialsUEVplus.Eternity.getMolten(144 * 64),
                MaterialsUEVplus.PrimordialMatter.getFluid(144 * 64),
                MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Time.getMolten(144 * 64 * 8),
                MaterialsUEVplus.Space.getMolten(144 * 64 * 8))

            .eut(RECIPE_MAX)
            .duration(114514 * 20)
            .addTo(GTCMRecipe.PreciseHighEnergyPhotonicQuantumMasterRecipes);

        // endregion

        // region Miracle Top

        // MiracleTop
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                Config.Enable_AdvCircuitAssemblyLine ? GTCMItemList.AdvCircuitAssemblyLine.get(1)
                    : GTModHandler.getModItem("gregtech", "gt.blockmachines", 1, 12735))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                Config.Enable_AdvCircuitAssemblyLine ? GTCMItemList.AdvCircuitAssemblyLine.get(64)
                    : GTModHandler.getModItem("gregtech", "gt.blockmachines", 64, 12735),
                Component_Assembly_Line.get(64),
                SpaceWarper.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(48),

                eM_Coil.get(64),
                getCircuits(Materials.UMV, 32),
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 64 },
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 64 },

                OpticalSOC.get(64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 64, 14),
                OpticalSOC.get(64),

                eM_Spacetime.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 64),
                ItemList.Field_Generator_UIV.get(32),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 64))
            .fluidInputs(
                new FluidStack(solderPlasma, 1024 * 144),
                MaterialsUEVplus.SpaceTime.getMolten(16 * 144),
                Materials.SuperconductorUIVBase.getMolten(64 * 144),
                Materials.SuperconductorUEVBase.getMolten(512 * 144))
            .itemOutputs(GTCMItemList.MiracleTop.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 3600)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Coil.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                eM_Hollow.get(4),
                SpaceWarper.get(8),
                eM_Coil.get(8),
                HighEnergyFlowCircuit.get(4),

                ItemList.Field_Generator_UEV.get(24),
                ItemList.Casing_Assembler.get(24),
                ItemList.Casing_Gearbox_TungstenSteel.get(24),
                new Object[]{OrePrefixes.circuit.get(Materials.Optical), 24},

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 2, 14),
                GTModHandler.getModItem(GTPlusPlus.ID, "particleBase", 2, 14),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 1024 * 144),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.Osmiridium.getMolten(144 * 256),
                Materials.SuperconductorUEVBase.getMolten(32 * 144)
            )
            .itemOutputs(
                eM_Spacetime.get(1)
            )

            .eut(RECIPE_UHV)
            .duration(20 * 512)
            .addTo(AssemblyLine);


        // endregion

        // region MagneticDrivePressureFormer
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Industrial_Extruder.get(1))
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_MiningOsmiridium.get(64),
                Industrial_Extruder.get(64),
                Industrial_PlatePress.get(64),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 64},

                ItemList.UV_Coil.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                ItemList.UV_Coil.get(64),

                ItemList.Field_Generator_UV.get(2),
                ItemList.Robot_Arm_UV.get(16),
                ItemList.Conveyor_Module_UV.get(16),
                HighEnergyFlowCircuit.get(32),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 32),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUV, 64)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 256),
                Materials.Naquadria.getMolten(144 * 256),
                Materials.Lubricant.getFluid(1000 * 256),
                Materials.Samarium.getMolten(144 * 256)
            )
            .itemOutputs(MagneticDrivePressureFormer.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 256)
            .addTo(AssemblyLine);

        // endregion

        // region PhysicalFormSwitcher
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Naquadria, 16),
                FluidExtractorUV.get(64),

                FluidSolidifierUV.get(64),
                ItemList.Field_Generator_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),

                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16},
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8)
            )
            .fluidInputs(Materials.Iridium.getMolten(144 * 64))
            .itemOutputs(PhysicalFormSwitcher.get(1))

            .eut(RECIPE_UV)
            .duration(20 * 180)
            .addTo(assembler);

        // Containment Field casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                ItemList.Field_Generator_LuV.get(4),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 8},
                GTOreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8))
            .fluidInputs(Materials.NaquadahAlloy.getMolten(144 * 4))
            .itemOutputs(GTModHandler.getModItem("gregtech", "gt.blockcasings2", 1, 8))

                        .eut(RECIPE_UV)
            .duration(20 * 30)
            .addTo(assembler);

        // endregion

        // region MagneticMixer
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                MixerUV.get(64),
                ItemList.Field_Generator_UV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16},
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8)
            )
            .fluidInputs(Materials.Iridium.getMolten(144 * 64))
            .itemOutputs(MagneticMixer.get(1))

                        .eut(RECIPE_UV)
            .duration(20 * 384)
            .addTo(assembler);

        // endregion

        // region Infinity Air Intake Hatch
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10),
                Hatch_Air_Intake_Extreme.get(4),
                ItemList.Electric_Pump_UHV.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 8},
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.DraconiumAwakened, 16))
            .fluidInputs(Materials.CosmicNeutronium.getMolten(144 * 16))
            .itemOutputs(InfiniteAirHatch.get(1))

            .eut(RECIPE_UHV)
            .duration(20 * 30)
            .addTo(assembler);
        // endregion

        // region MagneticDomainConstructor
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Config.Enable_IndustrialMagnetarSeparator ?
                    IndustrialMagnetarSeparator.get(64):
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16),
                ElectromagneticSeparatorUHV.get(16),

                PolarizerUHV.get(16),
                ItemList.Field_Generator_UV.get(3),
                ItemList.Robot_Arm_UHV.get(8),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 16},
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(new FluidStack(solderPlasma, 144 * 64))
            .itemOutputs(MagneticDomainConstructor.get(1))
                        .eut(RECIPE_UHV)
            .duration(20 * 320)
            .addTo(assembler);

        // endregion

        // region Silksong
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 8),
                ItemList.WiremillUV.get(16),

                HighEnergyFlowCircuit.get(8),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Conveyor_Module_ZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 8},
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16)
            )
            .fluidInputs(Materials.Iridium.getMolten(144 * 32))
            .itemOutputs(Silksong.get(1))
                        .eut(RECIPE_ZPM)
            .duration(20 * 600)
            .addTo(assembler);

        // endregion

        // region GiantVacuumDryingFurnace
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Controller_Vacuum_Furnace.get(16),
                GT_Dehydrator_ZPM.get(16),
                ItemList.Robot_Arm_ZPM.get(16),
                ItemList.Conveyor_Module_ZPM.get(16),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 32},
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16)
            )
            .fluidInputs(Materials.Iridium.getMolten(144 * 32))
            .itemOutputs(GiantVacuumDryingFurnace.get(1))
                        .eut(RECIPE_ZPM)
            .duration(20 * 600)
            .addTo(assembler);
        // endregion

        // region HolySeparator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.CuttingMachineUHV.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                ItemList.CuttingMachineUHV.get(32),
                ItemList.SlicingMachineUHV.get(32),
                eM_Power.get(16),

                ItemList.Field_Generator_UHV.get(16),
                ItemList.Emitter_UHV.get(64),
                ItemList.Emitter_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(8),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 16},
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},
                GTUtility.copyAmountUnsafe(64, Ic2Items.iridiumPlate),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 128),
                Materials.Naquadria.getMolten(144 * 64),
                Materials.SuperCoolant.getFluid(1000 * 128)
            )
            .itemOutputs(HolySeparator.get(1))

                        .eut(RECIPE_UHV)
            .duration(20 * 1200)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),

                eM_Hollow.get(4),
                ItemList.Field_Generator_UHV.get(8),
                ItemList.Field_Generator_UV.get(16),

                ItemList.Field_Generator_ZPM.get(64),
                ItemRefer.Advanced_Radiation_Protection_Plate.get(16),
                eM_Power.get(4),

                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 6},
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2)
            )
            .fluidInputs(new FluidStack(solderPlasma, 144 * 32))
            .itemOutputs(eM_Containment_Field.get(4))

                        .eut(RECIPE_UHV)
            .duration(20 * 60)
            .addTo(assembler);

        // endregion

        // region SpaceScaler
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, CompressorUHV.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                CompressorUHV.get(64),
                ExtractorUHV.get(64),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.CosmicNeutronium, 64),

                ItemList.Field_Generator_UEV.get(32),
                ItemList.Field_Generator_UHV.get(64),
                ItemList.Field_Generator_UHV.get(64),
                GTModHandler.getModItem("eternalsingularity", "eternal_singularity", 16),

                new Object[]{OrePrefixes.circuit.get(Materials.Optical), 32},
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                HighEnergyFlowCircuit.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CosmicNeutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 256),
                Materials.UUMatter.getFluid(1000 * 256),
                Materials.SuperCoolant.getFluid(1000 * 256),
                Materials.NaquadahAlloy.getMolten(144 * 256)
            )
            .itemOutputs(SpaceScaler.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 1800)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Containment_Field.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                eM_Containment_Field.get(4),
                ItemList.Field_Generator_UIV.get(16),
                ItemList.Field_Generator_UEV.get(64),
                SpaceWarper.get(4),

                ItemList.Tesseract.get(32),
                ItemList.EnergisedTesseract.get(32),
                GTOreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 32),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 256),
                new FluidStack(celestialTungsten, 144 * 256),
                Materials.Infinity.getMolten(144 * 32)
            )
            .itemOutputs(eM_Ultimate_Containment_Field.get(1))

            .eut(RECIPE_UIV)
            .duration(20 * 180)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 64))

            .itemOutputs(GTModHandler.getModItem("Avaritia", "Resource", 1, 5))

                        .eut(RECIPE_UHV)
            .duration(10)
            .addTo(RecipeMaps.compressorRecipes);

        // endregion

        // region Molecule Deconstructor
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.ElectrolyzerUV.get(1))
            .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Casing_MAX.get(16),
                ItemList.ElectrolyzerUV.get(64),
                ItemList.CentrifugeUV.get(64),
                Materials.Carbon.getNanite(16),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Emitter_UV.get(16),
                ItemList.Field_Generator_UV.get(8),
                ItemList.Electric_Pump_UV.get(32),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 64},
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 256),
                Materials.Osmiridium.getMolten(144 * 256),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 128)
            )
            .itemOutputs(MoleculeDeconstructor.get(1))

                        .eut(RECIPE_UHV)
            .duration(20 * 600)
            .addTo(AssemblyLine);

        // endregion

        // region CrystallineInfinitier
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.AutoclaveUHV.get(1))
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                eM_Containment_Field.get(4),
                ItemList.AutoclaveUHV.get(64),
                ItemList.Electric_Pump_UEV.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.NaquadahAlloy, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 256),
                Materials.NaquadahAlloy.getMolten(144 * 256),
                Materials.UUMatter.getFluid(1000 * 128)
            )
            .itemOutputs(CrystallineInfinitier.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 1800)
            .addTo(AssemblyLine);

        // endregion

        // region Miracle Door
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, eM_Teleportation.get(1))
            .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),
                Mega_AlloyBlastSmelter.get(64),

                StellarConstructionFrameMaterial.get(64),
                ItemList.Emitter_UMV.get(64),
                ItemList.Field_Generator_UMV.get(64),
                eM_Teleportation.get(64),

                HighEnergyFlowCircuit.get(64),
                getCircuits(Materials.UXV, 64),
                getCircuits(Materials.UXV, 64),
                HighEnergyFlowCircuit.get(64),

                SpaceWarper.get(64),
                GravitationalLens.get(64),
                ParticleTrapTimeSpaceShield.get(64),
                ItemList.EnergisedTesseract.get(64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 65536),
                MaterialsAlloy.PIKYONIUM.getFluidStack(144 * 65536),
                Materials.UUMatter.getFluid(1000 * 65536),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16384)
            )
            .itemOutputs(MiracleDoor.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 3600)
            .addTo(AssemblyLine);

        // White Dwarf Mold(Ingot)
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Neutronium.getIngots(1)
            )
            .fluidInputs(
                MaterialsUEVplus.WhiteDwarfMatter.getMolten(576)
            )
            .itemOutputs(WhiteDwarfMold_Ingot.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 10)
            .addTo(RecipeMaps.fluidSolidifierRecipes);

        // eM_Teleportation blockCasingsTT 10
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_PlasmaForge.get(1))
            .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16),
                eM_Ultimate_Containment.get(4),
                ItemList.Casing_Dim_Bridge.get(4),
                ItemList.Casing_Dim_Injector.get(4),

                StellarConstructionFrameMaterial.get(4),
                ItemList.Field_Generator_UMV.get(3),
                ItemList.Emitter_UMV.get(6),
                new Object[]{OrePrefixes.circuit.get(Materials.UMV), 1},

                SpaceWarper.get(24),
                ParticleTrapTimeSpaceShield.get(32),
                GTOreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 16),
                GTOreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 16),

                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 128),
                MaterialsAlloy.PIKYONIUM.getFluidStack(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16)
            )
            .itemOutputs(eM_Teleportation.get(1))
            .eut(RECIPE_UMV)
            .duration(20 * 120)
            .addTo(AssemblyLine);

        // endregion

        // region Infinite Dynamo Hatch
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, tectech.thing.CustomItemList.eM_dynamoMulti64_UMV.get(1))
            .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                AnnihilationConstrainer.get(1),
                ItemRefer.Compact_Fusion_Coil_T0.get(1),
                ItemRefer.Compact_Fusion_Coil_T4.get(1),
                Machine_Multi_Transformer.get(1),

                eM_Power.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 16),
                ItemList.Field_Generator_UMV.get(2),
                getCircuits(Materials.UXV, 4),

                getCircuits(Materials.UMV, 4),
                ItemList.EnergisedTesseract.get(4),
                ItemList.Tesseract.get(4),
                GravitationalLens.get(8)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 36),
                Materials.UUMatter.getFluid(1000 * 8),
                MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 64)
            )
            .itemOutputs(InfiniteWirelessDynamoHatch.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 20)
            .addTo(AssemblyLine);
        // endregion

        // region Ore Processing Factory
        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Ore_Processor.get(1))
            .metadata(SCANNING, new Scanning(16 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 64),
                ItemList.Ore_Processor.get(64),
                new Object[]{OrePrefixes.circuit.get(Materials.Optical), 64},
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
                eM_Power.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 512),
                Materials.TungstenSteel.getMolten(144 * 1024),
                Materials.Neutronium.getMolten(144 * 1024),
                Materials.Osmiridium.getMolten(144 * 1024)
            )
            .itemOutputs(OreProcessingFactory.get(1))

            .eut(RECIPE_UEV)
            .duration(20 * 1800)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(15),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sphalerite, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.germanium"), 36)
            )
                        .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenum, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(625))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 36)
            )
                        .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Scheelite, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 18)
            )
                        .eut(RECIPE_IV)
            .duration((int) (20 * 37.5))
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(20),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Molybdenite, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 8)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1875))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.rhenium"), 36)
            )
                        .eut(RECIPE_IV)
            .duration(20 * 75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(21),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrite, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 16)
            )
            .fluidInputs(Materials.SulfuricAcid.getFluid(1250))

            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("molten.thallium"), 288)
            )
                        .eut(RECIPE_IV)
            .duration(20 * 75)
            .addTo(GTPPRecipeMaps.alloyBlastSmelterRecipes);

        // endregion

        // region Circuit Converter
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                ItemList.Casing_Processor.get(3),
                ItemList.Machine_IV_Boxinator.get(1),

                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.IV), 1),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.EV), 2),
                GTOreDictUnificator.get(OrePrefixes.circuit.get(Materials.HV), 4),

                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 8))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(CircuitConverter.get(1))

                        .eut(RECIPE_IV)
            .duration(20 * 30)
            .addTo(assembler);

        // endregion

        // region Large Industrial Coking Factory
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_CokeOven.get(64),
                ItemList.PyrolyseOven.get(64),

                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackSteel, 16),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16},
                HighEnergyFlowCircuit.get(16),

                ItemList.Electric_Pump_UV.get(6),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.BlackSteel, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 8)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 96))
            .itemOutputs(LargeIndustrialCokingFactory.get(1))

                        .eut(RECIPE_UHV)
            .duration(20 * 128)
            .addTo(assembler);
        // endregion

        // region Mega Bricked Blast Furnace
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Casing_Firebricks.get(64),
                ItemList.Machine_Bricked_BlastFurnace.get(64),
                ItemList.Casing_Firebricks.get(64),

                ItemList.Machine_Bricked_BlastFurnace.get(64),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Steel, 16),
                ItemList.Machine_Bricked_BlastFurnace.get(64)
            )

            .itemOutputs(MegaBrickedBlastFurnace.get(1))

                        .eut(RECIPE_LV)
            .duration(20 * 114)
            .addTo(assembler);
        // endregion

        // region Dual Input Buffer
        // IV
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                ItemList.Hatch_Input_Multi_2x2_IV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Master), 1},
                Materials.TungstenSteel.getPlates(4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 4))
            .itemOutputs(DualInputBuffer_IV.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 15)
            .addTo(assembler);

        // LuV
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Hatch_Input_Multi_2x2_LuV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 1},
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plate, 4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 8))
            .itemOutputs(DualInputBuffer_LuV.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 15)
            .addTo(assembler);

        // ZPM
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Hatch_Input_Multi_2x2_ZPM.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 1},
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)

            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 16))
            .itemOutputs(DualInputBuffer_ZPM.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 15)
            .addTo(assembler);

        // UV
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_UV.get(1),
                ItemList.Hatch_Input_Multi_2x2_UV.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 1},
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 32))
            .itemOutputs(DualInputBuffer_UV.get(1))
            .eut(RECIPE_UV)
            .duration(20 * 15)
            .addTo(assembler);

        // endregion

        // region Scavenger
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GregtechItemList.Industrial_Sifter.get(64),
                SiftingMachineZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 16},
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 9),
                GTUtility.copyAmountUnsafe(36, Ic2Items.iridiumPlate),

                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 32))
            .itemOutputs(Scavenger.get(1))
                        .eut(RECIPE_ZPM)
            .duration(20 * 60)
            .addTo(assembler);
        // endregion

        // region BiosphereIII
        ItemStack bioVat = GTModHandler.getModItem("gregtech", "gt.blockmahines", 1, 12600 + GTValues.VN.length * 7, ItemList.BreweryUHV.get(1)); // todo ConfigHandler.IDOffset has been removed

        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, bioVat.copy())
            .metadata(SCANNING, new Scanning(16 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmiridium, 64),
                GTUtility.copyAmountUnsafe(64, bioVat),
                GTUtility.copyAmountUnsafe(64, BioItemList.getPetriDish(null)),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 3),

                ItemList.Electric_Pump_UHV.get(16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 64),

                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 8},
                ItemList.Circuit_Silicon_Wafer7.get(64),
                ItemList.Circuit_Parts_Chip_Bioware.get(64),
                HighEnergyFlowCircuit.get(16),

                GTUtility.copyAmountUnsafe(64, Ic2Items.iridiumPlate),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 64),
                Materials.Naquadria.getMolten(144 * 64),
                Materials.CosmicNeutronium.getMolten(144 * 64),
                GGMaterial.metastableOganesson.getMolten(144 * 32)
            )
            .itemOutputs(BiosphereIII.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);
        // endregion

        // region Advanced Oil Cracker
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                megaMachines[4],
                new Object[]{OrePrefixes.circuit.get(Materials.Elite), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.Data), 16},
                ItemList.Electric_Pump_IV.get(4)
            )
            .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 16))
            .itemOutputs(AdvancedMegaOilCracker.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 3600)
            .addTo(assembler);
        // endregion

        // region Indistinct Tentacle
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GGItemList.AdvAssLine.get(64),
                Component_Assembly_Line.get(64),
                ItemList.AssemblingMachineUMV.get(64),
                ItemRefer.Precise_Assembler.get(64),

                GTUtility.copyAmountUnsafe(128, StellarConstructionFrameMaterial.get(64)),
                GTUtility.copyAmountUnsafe(128, AnnihilationConstrainer.get(64)),
                GTUtility.copyAmountUnsafe(128, DysonSphereFrameComponent.get(64)),
                GTUtility.copyAmountUnsafe(128, SpaceWarper.get(64)),

                GTUtility.copyAmountUnsafe(128, GravitationalLens.get(64)),
                GTUtility.copyAmountUnsafe(128, GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1)),
                GTUtility.copyAmountUnsafe(128, eM_Ultimate_Containment_Advanced.get(64))
            )
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
                new FluidStack(solderPlasma, 144 * 2097152)
            )
            .itemOutputs(GTCMItemList.IndistinctTentacle.get(1))
            .eut(TierEU.RECIPE_MAX)
            .duration(20 * 14400)
            .addTo(MiracleTopRecipes);

        // endregion

        // region MEG
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
                GTCMItemList.VoidPollen.get(4)
            )
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(16 * 1000))
            .itemOutputs(GTCMItemList.MegaEggGenerator.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(120 * 20)
            .addTo(assembler);
        // endregion

        // region ThermalEnergyDevourer
        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, megaMachines[1])
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 64),
                GTUtility.copyAmountUnsafe(64, megaMachines[1]),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),

                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Neutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 16),
                MaterialType.Singularity.stack(16),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},

                ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderIndAlloy, 144 * 64),
                Materials.NaquadahAlloy.getMolten(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64))
            .itemOutputs(ThermalEnergyDevourer.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);
        // endregion

        // region Debug uncertainty hatch
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                UncertaintyX_Hatch.get(1),
                hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1)
            )
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(DebugUncertaintyHatch.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 120)
            .addTo(assembler);
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                UncertaintyX_Hatch.get(1),
                hatch_CreativeMaintenance.get(1),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Timepiece.get(1)
            )
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128))
            .itemOutputs(DebugUncertaintyHatch.get(16))
            .eut(RECIPE_UXV)
            .duration(20 * 120)
            .addTo(assembler);
        // endregion

        // region LaserSmartNode
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                LASERpipe.get(64),
                Laser_Lens_Special.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Bio), 1},
                ItemList.Hatch_Energy_UHV.get(1)
            )
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144))
            .itemOutputs(LaserSmartNode.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 5)
            .addTo(assembler);
        // endregion

        // region VacuumFilterExtractor
        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.DistilleryUV.get(1))
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackPlutonium, 64),
                GTUtility.copyAmountUnsafe(64, megaMachines[2]),
                Materials.Carbon.getNanite(64),
                ItemList.Field_Generator_UHV.get(8),

                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Electric_Pump_UHV.get(64),

                Laser_Lens_Special.get(1),
                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 16},
                HighEnergyFlowCircuit.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.BlackPlutonium, 64),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 128),
                Materials.Neutronium.getMolten(144 * 64),
                Materials.BlackPlutonium.getMolten(144 * 64))
            .itemOutputs(VacuumFilterExtractor.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 900)
            .addTo(AssemblyLine);
        // endregion

        // region Eye of Wood
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(17),
                new ItemStack(Items.golden_apple, 1, 1),
                ItemList.Emitter_LV.get(64),
                ItemList.Field_Generator_LV.get(64),
                new Object[]{OrePrefixes.circuit.get(Materials.Basic), 64}
            )
            .itemOutputs(GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 114)
            .addTo(assembler);

        addCraftingRecipe(
            GTCMItemList.EyeOfWood.get(1),
            new Object[]{"ABA", "BCB", "ABA",
                'A', new ItemStack(Blocks.brick_block),
                'B', "plankWood",
                'C', GTCMItemList.PrimitiveMansSpaceTimeDistortionDevice.get(1)
            });

        // endregion

        // region Mega Macerator
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                Industrial_MacerationStack.get(64),
                ItemList.MaceratorZPM.get(16),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 16},
                GregtechItemList.Maceration_Upgrade_Chip.get(64),
                WerkstoffLoader.AdemicSteel.get(OrePrefixes.gearGt, 16),

                ItemList.Electric_Motor_UV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plateDense, 16)
            )
            .fluidInputs(new FluidStack(solderIndAlloy, 144 * 64))
            .itemOutputs(MegaMacerator.get(1))
                        .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assembler);
        // endregion

        // region Hephaestus' Atelier
        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_Multi_Furnace.get(1))
            .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                ItemList.Machine_Multi_Furnace.get(64),
                ItemList.Casing_HeatProof.get(64),
                ItemList.Casing_FrostProof.get(64),
                new ItemStack[]{
                    ItemList.Casing_Coil_AwakenedDraconium.get(64),
                    ItemList.Casing_Coil_Infinity.get(16),
                    ItemList.Casing_Coil_Hypogen.get(4),
                    ItemList.Casing_Coil_Eternal.get(1),
                },

                eM_Hollow.get(64),
                ItemList.Field_Generator_UHV.get(16),
                ItemList.Electric_Pump_UHV.get(64),
                ItemList.Conveyor_Module_UHV.get(64),

                new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 32},
                HighEnergyFlowCircuit.get(64),
                HighEnergyFlowCircuit.get(64),
                HighEnergyFlowCircuit.get(64),

                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Invar, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 16)
            )
            .fluidInputs(
                new FluidStack(solderPlasma, 144 * 128),
                Materials.CosmicNeutronium.getMolten(144 * 64),
                Materials.UUMatter.getFluid(1000 * 64),
                Materials.SuperCoolant.getFluid(1000 * 64)
            )
            .itemOutputs(GTCMItemList.HephaestusAtelier.get(1))
            .eut(RECIPE_UEV)
            .duration(20 * 600)
            .addTo(assemblyLine);

        // endregion

        // region Legend Laser Hatch
        IItemContainer LegendTarget = tectech.thing.CustomItemList.eM_energyTunnel9001;
        IItemContainer LegendSource = tectech.thing.CustomItemList.eM_dynamoTunnel9001;
        IItemContainer UXVTarget104 = tectech.thing.CustomItemList.eM_energyTunnel7_UXV;
        IItemContainer UXVSource104 = tectech.thing.CustomItemList.eM_dynamoTunnel7_UXV;
        IItemContainer HomoStructureTime = tectech.thing.CustomItemList.EOH_Reinforced_Temporal_Casing;
        IItemContainer HomoStructureSpace = tectech.thing.CustomItemList.EOH_Reinforced_Spatial_Casing;
        IItemContainer HomoStructureMain = tectech.thing.CustomItemList.EOH_Infinite_Energy_Casing;
        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, UXVTarget104.get(1))
            .metadata(SCANNING, new Scanning(640 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                UXVTarget104.get(64),
                ItemList.Field_Generator_UXV.get(64),
                UXVTarget104.get(64),
                HomoStructureTime.get(64),

                UXVTarget104.get(64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 64),
                UXVTarget104.get(64),
                ZPM6.get(1),

                UXVTarget104.get(64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 64),
                UXVTarget104.get(64),
                GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", 16),

                UXVTarget104.get(64),
                ItemList.Field_Generator_UXV.get(64),
                UXVTarget104.get(64),
                HomoStructureTime.get(64)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(2000000000),
                MaterialsUEVplus.Eternity.getMolten(2000000000),
                MaterialsUEVplus.SpaceTime.getMolten(2000000000),
                Materials.Infinity.getMolten(2000000000)
            )
            .itemOutputs(LegendTarget.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 512)
            .addTo(AssemblyLine);

        GTValues.RA
            .stdBuilder()
            .metadata(RESEARCH_ITEM, UXVSource104.get(1))
            .metadata(SCANNING, new Scanning(640 * HOURS, TierEU.RECIPE_LV))
            .itemInputs(
                UXVSource104.get(64),
                ItemList.Field_Generator_UXV.get(64),
                UXVSource104.get(64),
                HomoStructureTime.get(64),

                UXVSource104.get(64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 64),
                UXVSource104.get(64),
                ZPM6.get(1),

                UXVSource104.get(64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 64),
                UXVSource104.get(64),
                GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", 16),

                UXVSource104.get(64),
                ItemList.Field_Generator_UXV.get(64),
                UXVSource104.get(64),
                HomoStructureTime.get(64)
            )
            .fluidInputs(
                MaterialsUEVplus.Time.getMolten(2000000000),
                MaterialsUEVplus.Eternity.getMolten(2000000000),
                MaterialsUEVplus.SpaceTime.getMolten(2000000000),
                Materials.Infinity.getMolten(2000000000)
            )
            .itemOutputs(LegendSource.get(1))
            .eut(RECIPE_UXV)
            .duration(20 * 512)
            .addTo(AssemblyLine);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            LegendTarget.get(1),
            256_000_000,
            2048,
            512_000_000,
            1_048_576,
            new Object[]{
                LegendTarget.get(1),
                getModItem(GoodGenerator.ID, "compactFusionCoil", 1, 4),
                new ItemStack(GregTechAPI.sBlockCasingsDyson, 1, 4),
                tectech.thing.CustomItemList.Machine_Multi_Transformer.get(1),

                tectech.thing.CustomItemList.eM_Power.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsUEVplus.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16),

                getCircuits(Materials.UXV, 16),
                ItemList.EnergisedTesseract.get(1)
            },
            new FluidStack[]{
                new FluidStack(solderPlasma, 1_296 * 64 * 4),
                MaterialsUEVplus.ExcitedDTSC.getFluid(500L * 64)
            },
            GTCMItemList.LegendaryWirelessEnergyHatch.get(1),
            20 * 60,
            (int) RECIPE_UMV
        );

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.LegendaryWirelessEnergyHatch.get(1),
            2048_000_000,
            16384,
            512_000_000,
            512_000_000,
            new Object[]{
                GTCMItemList.LegendaryWirelessEnergyHatch.get(16),
                AdvancedHighPowerCoilBlock.get(64),
                ZPM6.get(64),
                GTCMItemList.MassFabricatorGenesis.get(1),

                HomoStructureMain.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialsUEVplus.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16),

                getCircuits(Materials.Transcendent, 16),
                ItemList.EnergisedTesseract.get(64)
            },
            new FluidStack[] {
                new FluidStack(solderPlasma, 1_296 * 256 * 4),
                MaterialsUEVplus.ExcitedDTSC.getFluid(500L * 256)
            },
            GTCMItemList.HarmoniousWirelessEnergyHatch.get(1),
            20*60,
            (int) RECIPE_UXV
        );
        // endregion

        // region Deployed Nano Core
        if (Config.Enable_DeployedNanoCore) {
            TST_RecipeBuilder
                .builder()
                .itemInputs(
                    GTUtility.copyAmountUnsafe(512, ItemList.NanoForge.get(1)),
                    MaterialsUEVplus.Universium.getNanite(64),
                    GTUtility.copyAmountUnsafe(128, MaterialsUEVplus.Eternity.getNanite(1)),
                    GTUtility.copyAmountUnsafe(1024, MaterialsUEVplus.TranscendentMetal.getNanite(1)),

                    SpaceScaler.get(64),
                    GravitationalLens.get(1024),
                    AnnihilationConstrainer.get(1024),
                    DysonSphereFrameComponent.get(1024),

                    PerfectLapotronCrystal.get(2048),
                    GTUtility.copyAmountUnsafe(1024, ItemList.Field_Generator_UMV.get(1)),
                    GTUtility.copyAmountUnsafe(2048, ItemList.Emitter_UMV.get(1)),
                    GTUtility.copyAmountUnsafe(4096, GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1)),

                    StellarConstructionFrameMaterial.get(2048),
                    GTUtility.copyAmountUnsafe(4096, GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 1))
                )
                .fluidInputs(
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(144 * 256),
                    Materials.UUMatter.getFluid(2000000),
                    MaterialsUEVplus.ExcitedDTSC.getFluid(1000000),
                    GGMaterial.shirabon.getMolten(144 * 8192),

                    Materials.Neutronium.getMolten(144 * 524288),
                    Materials.CosmicNeutronium.getMolten(144 * 524288),
                    Materials.NaquadahAlloy.getMolten(144 * 524288)
                )
                .itemOutputs(GTCMItemList.DeployedNanoCore.get(1))
                .eut(2000000000)
                .duration(20 * 775500)
                .addTo(MiracleTopRecipes);
        }
        // endregion

        // region Astral Array
        {
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, Machine_Multi_Computer.get(1))
                .metadata(SCANNING, new Scanning(114514 * 20, TierEU.RECIPE_LV))
                .itemInputs(
                    Machine_Multi_Computer.get(64),
                    Machine_Multi_Computer.get(64),
                    Machine_Multi_Computer.get(64),
                    Machine_Multi_Computer.get(64),
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
                    new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 2)
                )
                .fluidInputs(Materials.Tin.getPlasma(14400), Materials.SuperCoolant.getFluid(4000000), Materials.Infinity.getMolten(114514))
                .itemOutputs(AstralComputingArray.get(1))
                .eut(RECIPE_UEV * 3)
                .duration(20 * 1000)
                .addTo(AssemblyLine);

            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    rack_Hatch.get(64),
                    ItemList.Field_Generator_UEV.get(64),
                    new ItemStack(TTCasingsContainer.sBlockCasingsTT, 64, 1),
                    HiC_T5.get(64))
                .itemOutputs(RealRackHatch.get(1))
                .eut(RECIPE_UEV * 2)
                .duration(20 * 500)
                .addTo(assembler);

            final ItemStack wirelessCard = getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1, 42);
            final ItemStack quantumCard = getModItem(AE2WCT.ID, "infinityBoosterCard", 1);

            // Wireless Booster Card
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    Materials.Fluix.getDust(1),
                    Materials.CertusQuartz.getGems(1),
                    Materials.EnderPearl.getPlates(1),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Titanium, 2)
                )
                .fluidInputs(Materials.Aluminium.getMolten(144))
                .itemOutputs(GTUtility.copyAmountUnsafe(1, wirelessCard))
                .eut(RECIPE_LV)
                .duration(100)
                .addTo(assembler);

            // Infinity Booster Card
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 4),
                    GTUtility.copyAmountUnsafe(32, wirelessCard),
                    new ItemStack(LudicrousItems.resource, 4, 5)
                )
                .fluidInputs(Materials.Infinity.getMolten(144 * 8))
                .itemOutputs(GTUtility.copyAmountUnsafe(1, quantumCard))
                .eut(RECIPE_UHV)
                .duration(20)
                .addTo(assembler);

            // Wireless Data Input Hatch
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    dataIn_Hatch.get(1),
                    Machine_Multi_Switch.get(1),
                    GTUtility.copyAmountUnsafe(16, quantumCard),
                    ItemList.Sensor_UIV.get(16),
                    ItemList.Tesseract.get(64)
                )
                .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 8))
                .itemOutputs(WirelessDataInputHatch.get(1))
                .eut(RECIPE_UMV)
                .duration(800)
                .addTo(assembler);

            // Wireless Data Output Hatch
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    dataOut_Hatch.get(1),
                    Machine_Multi_Switch.get(1),
                    GTUtility.copyAmountUnsafe(16, quantumCard),
                    ItemList.Emitter_UIV.get(16),
                    ItemList.EnergisedTesseract.get(64)
                )
                .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 8))
                .itemOutputs(WirelessDataOutputHatch.get(1))
                .eut(RECIPE_UMV)
                .duration(800)
                .addTo(assembler);

            // Wireless computation update card
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, WirelessDataInputHatch.get(1))
                .metadata(SCANNING, new Scanning(720000, TierEU.RECIPE_LV))
                .itemInputs(
                    GTUtility.copyAmountUnsafe(64, quantumCard),
                    GTUtility.copyAmountUnsafe(64, quantumCard),
                    GTUtility.copyAmountUnsafe(64, quantumCard),
                    GTUtility.copyAmountUnsafe(64, quantumCard),

                    WirelessDataOutputHatch.get(8),
                    WirelessDataInputHatch.get(8),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Wireless_Dynamo_Energy_UMV.get(8),

                    Machine_Multi_Switch.get(64),
                    Machine_Multi_Switch.get(64),
                    Machine_Multi_Switch.get(64),
                    Machine_Multi_Switch.get(64)
                )
                .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 200), Materials.UUMatter.getFluid(20480000), Materials.SuperconductorUIVBase.getMolten(5000000))
                .itemOutputs(WirelessUpdateItem.get(1))
                .eut(RECIPE_UMV)
                .duration(800)
                .addTo(AssemblyLine);

        }

        // endregion

        // region CoreDeviceOfHumanPowerGenerationFacility
        if (Config.Enable_CoreDeviceOfHumanPowerGenerationFacility) {
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    ItemList.FluidHeaterUV.get(64),
                    ItemList.Electric_Pump_UV.get(64),

                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 64),
                    new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 32},
                    HighEnergyFlowCircuit.get(32),

                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 64)
                )
                .fluidInputs(new FluidStack(solderPlasma, 144 * 64))
                .itemOutputs(GTCMItemList.CoreDeviceOfHumanPowerGenerationFacility.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 900)
                .addTo(assembler);
        }
        // endregion

        // region StarcoreMiner
        if (Config.Enable_StarcoreMiner) {
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, GTOreDictUnificator.get(OrePrefixes.toolHeadDrill, Materials.Infinity, 1))
                .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    new ItemStack(GregTechAPI.sBlockCasingsSE, 64),
                    GTUtility.copyAmountUnsafe(64, voidminer[2]),
                    ItemList.MiningDroneUEV.get(18),
                    SpaceWarper.get(18),

                    ItemList.EnergisedTesseract.get(64),
                    ItemList.Electric_Motor_UEV.get(64),
                    ItemList.Field_Generator_UEV.get(48),
                    ItemList.Sensor_UEV.get(64),

                    new Object[]{OrePrefixes.circuit.get(Materials.Optical), 64},
                    HighEnergyFlowCircuit.get(64),
                    eM_Power.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 64)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 144 * 1024),
                    Materials.Quantium.getMolten(144 * 1024),
                    Materials.UUMatter.getFluid(1000 * 2048),
                    GGMaterial.metastableOganesson.getMolten(144 * 512)
                )
                .itemOutputs(GTCMItemList.StarcoreMiner.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 7200)
                .addTo(assemblyLine);

        }
        // endregion

        // region TST Disassembler
        if (Config.Enable_Disassembler) {
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.Machine_LV_Assembler.get(1))
                .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    AssemblingMachineUHV.get(64),
                    ItemList.Field_Generator_UHV.get(16),
                    ItemList.Electric_Pump_UHV.get(64),
                    ItemList.Conveyor_Module_UHV.get(64),

                    ItemList.Robot_Arm_UHV.get(64),
                    ItemList.Robot_Arm_UHV.get(64),
                    ItemList.Robot_Arm_UHV.get(64),
                    ItemList.Robot_Arm_UHV.get(64),

                    eM_Power.get(64),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 16)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 144 * 64 * 16),
                    Materials.UUMatter.getFluid(1000 * 128),
                    Materials.SuperCoolant.getFluid(1000 * 128 * 6)
                )
                .itemOutputs(GTCMItemList.Disassembler.get(1))
                .eut(RECIPE_UEV)
                .duration(20 * 3600)
                .addTo(assemblyLine);
        }
        // endregion

        // region Ball Lightning
        if (Config.Enable_BallLightning) {
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, Industrial_Arc_Furnace.get(1))
                .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    HighPowerRadiationProofCasing.get(64),
                    Industrial_Arc_Furnace.get(64),
                    ArcFurnaceUEV.get(16),
                    PlasmaArcFurnaceUEV.get(16),

                    GTPP_Casing_UHV.get(64),
                    new ItemStack(ItemRegistry.bw_realglas, 48, 14),
                    ItemRefer.Field_Restriction_Coil_T2.get(32),
                    ItemList.Field_Generator_UEV.get(16),

                    ItemList.Robot_Arm_UEV.get(32),
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 16),

                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64),
                    HighEnergyFlowCircuit.get(64)
                )
                .fluidInputs(
                    WerkstoffLoader.Oganesson.getFluidOrGas(2000 * 1000),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(144 * 64 * 64),
                    RadoxPolymer.getMolten(144 * 64 * 16),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(1000 * 16)
                )
                .itemOutputs(BallLightning.get(1))
                .eut(RECIPE_UEV)
                .duration(20 * 1800)
                .addTo(assemblyLine);

            //Casing
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, Casing_Industrial_Arc_Furnace.get(1))
                .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    Casing_Industrial_Arc_Furnace.get(8),
                    eM_Power.get(8),
                    ItemRefer.Advanced_Radiation_Protection_Plate.get(64),
                    ItemList.DysonSwarmModule.getWithDamage(64, 3),

                    TransmissionComponent_UV.get(16),
                    ItemList.Electric_Piston_UHV.get(8),
                    HighEnergyFlowCircuit.get(8),
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4},

                    PicoWafer.get(8),
                    GGMaterial.incoloy903.get(OrePrefixes.pipeHuge, 64),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFineWire(64)
                )
                .fluidInputs(
                    new FluidStack(solderPlasma, 144 * 40),
                    Materials.UUMatter.getFluid(1000 * 8),
                    MaterialsAlloy.ABYSSAL.getFluidStack(144 * 28)
                )
                .itemOutputs(HighPowerRadiationProofCasing.get(4))
                .eut(RECIPE_UHV)
                .duration(20 * 120)
                .addTo(assemblyLine);

            //Chip
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),
                    HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                    CriticalPhoton.get(64),
                    Laser_Lens_Special.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                    Antimatter.get(64),
                    SpaceWarper.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                    ItemList.ZPM.get(1),
                    ItemList.ZPM5.get(1),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64)
                )
                .fluidInputs(
                    new FluidStack(GTPPFluids.LiquidHydrogen, 1_800_000),
                    new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1_800_000),
                    Materials.Nitrogen.getPlasma(1_800_000),
                    new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1_800_000),
                    new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_800_000),
                    Materials.Bismuth.getPlasma(1_800_000),
                    Materials.Boron.getPlasma(1_800_000),
                    FluidRegistry.getFluidStack("cryotheum", 1_800_000)
                )
                .itemOutputs(BallLightningUpgradeChip.get(1))
                .eut(RECIPE_UIV)
                .duration(630_720_000)
                .addTo(MiracleTopRecipes);

            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64),
                    HighEnergyFlowCircuit.get(64),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                    CriticalPhoton.get(64),
                    Laser_Lens_Special.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),
                    Antimatter.get(64),
                    SpaceWarper.get(64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 64),

                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64),
                    ItemUtils.getSimpleStack(ModItems.itemChargePack_High_4, 1),
                    ItemList.ZPM5.get(1),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, MaterialsUEVplus.TranscendentMetal, 64)
                )
                .fluidInputs(
                    new FluidStack(GTPPFluids.LiquidHydrogen, 1_800_000),
                    new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 1_800_000),
                    Materials.Nitrogen.getPlasma(1_800_000),
                    new FluidStack(MaterialsElements.getInstance().KRYPTON.getPlasma(), 1_800_000),
                    new FluidStack(MaterialsElements.STANDALONE.RUNITE.getPlasma(), 1_800_000),
                    Materials.Bismuth.getPlasma(1_800_000),
                    Materials.Boron.getPlasma(1_800_000),
                    FluidRegistry.getFluidStack("cryotheum", 1_800_000)
                )
                .itemOutputs(BallLightningUpgradeChip.get(1))
                .eut(RECIPE_UIV)
                .duration(630_720_000)
                .addTo(MiracleTopRecipes);

            //Coil
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1))
                .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Longasssuperconductornameforuhvwire, 8),
                    GregtechItemList.Casing_Coil_QuantumForceTransformer.get(4),
                    ItemRefer.Compact_Fusion_Coil_T4.get(16),
                    LaserSmartNode.get(16),

                    ItemList.Emitter_UIV.get(64),
                    ItemList.Sensor_UIV.get(64),
                    new Object[]{OrePrefixes.circuit.get(Materials.Optical), 32},
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64},

                    ItemRefer.HiC_T5.get(64),
                    GravitationalLens.get(64),
                    PerfectLapotronCrystal.get(64),
                    ModItemHandler.EternalSingularity.NitronicSingularity.get(1),

                    AnnihilationConstrainer.get(1),
                    ItemList.ZPM2.get(1),
                    GTModHandler.getModItem(GTPlusPlus.ID, "item.itemBufferCore10", 1),
                    Laser_Lens_Special.get(4)
                )
                .fluidInputs(
                    Materials.Hydrogen.getPlasma(1000 * 4096),
                    MaterialsElements.getInstance().CALIFORNIUM.getFluidStack(144 * 256),
                    MaterialsAlloy.QUANTUM.getFluidStack(144 * 256),
                    MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(144 * 256)
                )
                .itemOutputs(AdvancedHighPowerCoilBlock.get(1))
                .eut(RECIPE_UIV)
                .duration(20 * 240)
                .addTo(assemblyLine);
        }
        // endregion

        // region Bee Engineer
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 64),
                GTModHandler.getModItem(Forestry.ID, "alveary", 64, 0),

                ItemList.Field_Generator_LuV.get(4),
                ItemList.Electric_Pump_LuV.get(16),
                ItemList.Conveyor_Module_LuV.get(16),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Plutonium241, 64),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16}
            )
            .fluidInputs(Materials.Honey.getFluid(1000 * 256))
            .itemOutputs(GTCMItemList.BeeEngineer.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assembler);
        // endregion

        // region Space Apiary Module
        if (Config.EnableSpaceApiaryModule) {
            final IRecipeMap SpaceAssembler = IGRecipeMaps.spaceAssemblerRecipes;

            // t1
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemList.Conveyor_Module_UHV.get(16),
                    ItemList.Robot_Arm_UHV.get(16),
                    ItemList.Electric_Pump_UHV.get(16),

                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64}
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 128),
                    Materials.Honey.getFluid(1000 * 256)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT1.get(1))
                .specialValue(1)
                .eut(RECIPE_UHV)
                .duration(20 * 300)
                .addTo(SpaceAssembler);

            // t2
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UEV.get(16),
                    ItemList.Conveyor_Module_UEV.get(16),
                    ItemList.Robot_Arm_UEV.get(16),
                    ItemList.Electric_Pump_UEV.get(16),

                    new Object[]{OrePrefixes.circuit.get(Materials.Optical), 64}
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 256),
                    Materials.Honey.getFluid(1000 * 512)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT2.get(1))
                .specialValue(1)
                .eut(RECIPE_UEV)
                .duration(20 * 600)
                .addTo(SpaceAssembler);

            // t3
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UIV.get(16),
                    ItemList.Conveyor_Module_UIV.get(16),
                    ItemList.Robot_Arm_UIV.get(16),
                    ItemList.Electric_Pump_UIV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64)
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 512),
                    Materials.Honey.getFluid(1000 * 1024)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT3.get(1))
                .specialValue(2)
                .eut(RECIPE_UIV)
                .duration(20 * 1200)
                .addTo(SpaceAssembler);

            // t4
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UMV.get(16),
                    ItemList.Conveyor_Module_UMV.get(16),
                    ItemList.Robot_Arm_UMV.get(16),
                    ItemList.Electric_Pump_UMV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64)
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 1024),
                    Materials.Honey.getFluid(1000 * 2048)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT4.get(1))
                .specialValue(3)
                .eut(RECIPE_UMV)
                .duration(20 * 2400)
                .addTo(SpaceAssembler);

        }

        // endregion

        // region Bee Engineer
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 64),
                GTModHandler.getModItem(Forestry.ID, "alveary", 64, 0),

                ItemList.Field_Generator_LuV.get(4),
                ItemList.Electric_Pump_LuV.get(16),
                ItemList.Conveyor_Module_LuV.get(16),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Plutonium241, 64),
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16}
            )
            .fluidInputs(Materials.Honey.getFluid(1000 * 256))
            .itemOutputs(GTCMItemList.BeeEngineer.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 300)
            .addTo(assembler);
        // endregion

        // region Space Apiary Module
        if (Config.EnableSpaceApiaryModule) {
            final IRecipeMap SpaceAssembler = IGRecipeMaps.spaceAssemblerRecipes;

            // t1
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemList.Conveyor_Module_UHV.get(16),
                    ItemList.Robot_Arm_UHV.get(16),
                    ItemList.Electric_Pump_UHV.get(16),

                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 64}
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 128),
                    Materials.Honey.getFluid(1000 * 256)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT1.get(1))
                .specialValue(1)
                .eut(RECIPE_UHV)
                .duration(20 * 300)
                .addTo(SpaceAssembler);

            // t2
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UEV.get(16),
                    ItemList.Conveyor_Module_UEV.get(16),
                    ItemList.Robot_Arm_UEV.get(16),
                    ItemList.Electric_Pump_UEV.get(16),

                    new Object[]{OrePrefixes.circuit.get(Materials.Optical), 64}
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 256),
                    Materials.Honey.getFluid(1000 * 512)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT2.get(1))
                .specialValue(1)
                .eut(RECIPE_UEV)
                .duration(20 * 600)
                .addTo(SpaceAssembler);

            // t3
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UIV.get(16),
                    ItemList.Conveyor_Module_UIV.get(16),
                    ItemList.Robot_Arm_UIV.get(16),
                    ItemList.Electric_Pump_UIV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 64)
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 512),
                    Materials.Honey.getFluid(1000 * 1024)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT3.get(1))
                .specialValue(2)
                .eut(RECIPE_UIV)
                .duration(20 * 1200)
                .addTo(SpaceAssembler);

            // t4
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),
                    ItemList.Machine_IndustrialApiary.get(64),

                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),
                    ItemList.IndustrialApiary_Upgrade_Acceleration_8_Upgraded.get(64),

                    ItemList.Field_Generator_UMV.get(16),
                    ItemList.Conveyor_Module_UMV.get(16),
                    ItemList.Robot_Arm_UMV.get(16),
                    ItemList.Electric_Pump_UMV.get(16),

                    GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 64)
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 1024),
                    Materials.Honey.getFluid(1000 * 2048)
                )
                .itemOutputs(GTCMItemList.SpaceApiaryT4.get(1))
                .specialValue(3)
                .eut(RECIPE_UMV)
                .duration(20 * 2400)
                .addTo(SpaceAssembler);

        }

        // endregion

        // region Large Canner
        if (Config.Enable_LargeCanner) {
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 16),

                    ItemList.FluidCannerZPM.get(16),
                    ItemList.CanningMachineZPM.get(16),
                    new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 16},

                    ItemList.Electric_Pump_ZPM.get(16),
                    ItemList.Conveyor_Module_ZPM.get(8),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 16)
                )
                .fluidInputs(Materials.SolderingAlloy.getMolten(144 * 64))
                .itemOutputs(GTCMItemList.LargeCanner.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 300)
                .addTo(assembler);
        }
        // endregion


        //region Lightning Spire
        if (Config.Enable_LightningSpire){
            GTValues.RA.stdBuilder()
                .itemInputs(
                    CI.getTieredGTPPMachineCasing(5,1),
                    CI.getEnergyCore(4,4),
                    CI.getTransmissionComponent(5,2),
                    ItemList.Field_Generator_HV.get(4),
                    MaterialsAlloy.NITINOL_60.getGear(4),
                    MaterialsElements.getInstance().GERMANIUM.getBolt(16),
                    MaterialsAlloy.NICHROME.getFineWire(16),
                    MaterialsAlloy.NICHROME.getCable16(1)
                )
                .fluidInputs(
                    Materials.Silicone.getMolten(2304)
                )
                .itemOutputs(GTCMItemList.LightningSpire.get(1))
                .eut(RECIPE_IV)
                .duration(20 * 120)
                .addTo(assembler);
        }
        // endregion

        //region Incompact Cyclotron
        if(Config.Enable_IncompactCyclotron){
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, COMET_Cyclotron.get(1))
                .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    ItemList.Hull_UEV.get(64),
                    COMET_Cyclotron.get(64),
                    ItemList.Casing_Coil_Infinity.get(8),
                    Laser_Lens_Special.get(4),

                    ItemList.Field_Generator_UHV.get(16),
                    ItemRefer.HiC_T5.get(32),
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 16},
                    GGMaterial.enrichedNaquadahAlloy.get(OrePrefixes.plateDense,16),

                    GTOreDictUnificator.get(OrePrefixes.gearGt,Materials.NaquadahAlloy,16),
                    GTOreDictUnificator.get(OrePrefixes.screw,Materials.NaquadahAlloy,64)

                )
                .fluidInputs(
                    Materials.NaquadahAlloy.getMolten(144 * 256),
                    FluidRegistry.getFluidStack("cryotheum", 1_000_000),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 2)
                )
                .itemOutputs(IncompactCyclotron.get(1))
                .eut(RECIPE_UEV)
                .duration(20 * 900)
                .addTo(assemblyLine);

                GTValues.RA
                    .stdBuilder()
                    .metadata(RESEARCH_ITEM, Casing_Cyclotron_External.get(1))
                    .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
                    .itemInputs(
                        Casing_Cyclotron_External.get(4),
                        Casing_AdvancedVacuum.get(4),
                        ItemUtils.simpleMetaStack("miscutils:itemDehydratorCoilWire", 3, 16),
                        ItemRefer.Advanced_Radiation_Protection_Plate.get(6),

                        MaterialsAlloy.ABYSSAL.getLongRod(12),
                        MaterialsAlloy.TITANSTEEL.getScrew(24),
                        ItemList.Electric_Piston_UV.get(6)

                    )
                    .fluidInputs(
                        MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144*10),
                        GGMaterial.enrichedNaquadahAlloy.getMolten(144*4)
                    )
                    .itemOutputs(DenseCyclotronOuterCasing.get(1))
                    .eut(RECIPE_UHV)
                    .duration(20 * 30)
                    .addTo(assemblyLine);

            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, Casing_Cyclotron_Coil.get(1))
                .metadata(SCANNING, new Scanning(4 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    Casing_Cyclotron_Coil.get(16),
                    ItemList.Casing_Coil_Superconductor.get(4),
                    new ItemStack[]{ GregtechItemList.Battery_Gem_2.get(1), ItemList.Energy_Module.get(2) },
                    ItemList.UHV_Coil.get(64),

                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 2},
                    ItemUtils.simpleMetaStack(ModItems.itemStandarParticleBase, 19, 16),
                    ItemList.Field_Generator_UHV.get(1)
                )
                .fluidInputs(
                    Materials.UUMatter.getFluid(1000 * 64),
                    new FluidStack(celestialTungsten, 1000 * 16),
                    Materials.Longasssuperconductornameforuhvwire.getMolten(144 * 8),
                    GGMaterial.enrichedNaquadahAlloy.getMolten(144 * 2)
                )
                .itemOutputs(CompactCyclotronCoil.get(1))
                .eut(RECIPE_UHV)
                .duration(20 * 60)
                .addTo(assemblyLine);
        }
        // endregion

        // Casing Stone Brick
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Palladium, 1),
                com.dreammaster.item.NHItemList.StonePlate.getIS(6)
            )
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("concrete"), 36000)
            )
            .itemOutputs(GTCMItemList.ReinforcedStoneBrickCasing.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 12)
            .addTo(assembler);

            // Casing Stone Brick Advanced
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTCMItemList.ReinforcedStoneBrickCasing.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bedrockium, 6)
            )
            .fluidInputs(
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(1728)
            )
            .itemOutputs(GTCMItemList.ReinforcedBedrockCasing.get(1))
            .eut(RECIPE_ZPM)
            .duration(20 * 30)
            .addTo(assembler);

            // Casing Farm
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTCMItemList.ReinforcedStoneBrickCasing.get(1),
            1_000_000,
            512,
            2_000_000,
            4,
            new Object[]{
                GTCMItemList.ReinforcedStoneBrickCasing.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Polybenzimidazole, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeRestrictiveHuge, Materials.BlackPlutonium, 4),
                ItemList.Casing_Vent.get(1),

                FarmGear,
                FarmOutput,
                FarmPump,
                FarmController,

                GGMaterial.marCeM200.get(OrePrefixes.gearGt, 4),
                ItemList.Electric_Piston_UV.get(2),
                ItemList.Electric_Pump_UV.get(2),
                ItemRefer.HiC_T3.get(4),

                new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 4},
                new Object[]{OrePrefixes.circuit.get(Materials.SuperconductorUHV), 2},
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 18)
            },
            new FluidStack[]{
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 16000),
                MaterialsAlloy.ARCANITE.getFluidStack(864),
                MaterialsAlloy.TITANSTEEL.getFluidStack(144)
            },
            GTCMItemList.CompositeFarmCasing.get(1),
            20*60,
            (int) RECIPE_UV
        );

            // Casing Clean
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1),
            2_000_000,
            512,
            2_000_000,
            16,
            new Object[]{
                GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1),
                ItemList.Casing_Coil_Superconductor.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SterlingSilver, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 4),

                ItemList.Circuit_Parts_Chip_Bioware.get(8),
                GGMaterial.adamantiumAlloy.get(OrePrefixes.plateDouble, 6),
                ItemList.neutroniumHeatCapacitor.get(1)
            },
            new FluidStack[]{
                Materials.Grade8PurifiedWater.getFluid(8000),
                new FluidStack(FluidRegistry.getFluid("liquid helium"), 64000)
            },
            GTCMItemList.AsepticGreenhouseCasing.get(1),
            20*240,
            (int) RECIPE_UHV
        );

        if(Config.Enable_MegaStoneBreaker){
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, Controller_IndustrialRockBreaker.get(1))
                .metadata(SCANNING, new Scanning(8 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    ItemList.Hull_UEV.get(4),
                    ItemList.RockBreakerZPM.get(16),
                    Controller_IndustrialRockBreaker.get(64),
                    GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 64),

                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bedrockium, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahAlloy, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tetranaquadahdiindiumhexaplatiumosminid, 1),

                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,
                    ExtraUtilitiesNodeUpgrade2,

                    CompressCobblestone8,
                    CompressCobblestone8,
                    CompressCobblestone8,
                    CompressCobblestone8
                )
                .fluidInputs(
                    GGMaterial.adamantiumAlloy.getMolten(9216),
                    Materials.Lava.getFluid(2_000_000_000),
                    Materials.Water.getFluid(2_000_000_000)
                )
                .itemOutputs(GTCMItemList.MegaStoneBreaker.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 120)
                .addTo(assemblyLine);
        }

        // region advanced circuit assembly line
        if (Config.Enable_AdvCircuitAssemblyLine){
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, ItemRegistry.cal)
                .metadata(SCANNING, new Scanning(HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    copyAmount(16,ItemRegistry.cal),
                    new Object[] {OrePrefixes.circuit.get(Materials.ZPM), 16},
                    new Object[] {OrePrefixes.circuit.get(Materials.LuV), 32},
                    new Object[] {OrePrefixes.circuit.get(Materials.IV), 64},

                    ItemList.Automation_ChestBuffer_ZPM.get(1)
                )
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 18),
                    Materials.Lubricant.getFluid(1000 * 8),
                    GGMaterial.adamantiumAlloy.getMolten(144 * 64)
                )
                .itemOutputs(GTCMItemList.AdvCircuitAssemblyLine.get(1))
                .eut(RECIPE_ZPM)
                .duration(20 * 900)
                .addTo(assemblyLine);
        }
        // endregion


        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hull_IV.get(1),
                ItemList.Electric_Motor_LuV.get(1),

                ItemList.Sensor_LuV.get(2),
                ItemList.Conveyor_Module_LuV.get(4),
                BWMetaItems.getCircuitParts().getStack(3,8),

                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UV,8)
            )
            .fluidInputs(
                MaterialsAlloy.ARCANITE.getFluidStack(144 * 16)
            )
            .itemOutputs(GTCMItemList.CircuitImprintHatchT1.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 60)
            .addTo(assembler);

        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                ItemList.Hull_MAX.get(1),
                ItemList.Electric_Motor_UHV.get(2),

                ItemList.Sensor_UHV.get(4),
                ItemList.Conveyor_Module_UHV.get(16),
                BWMetaItems.getCircuitParts().getStack(3,32),

                GTOreDictUnificator.get(OrePrefixes.circuit,Materials.UEV,32)
            )
            .fluidInputs(
                MaterialsAlloy.OCTIRON.getFluidStack(144 * 16)
            )
            .itemOutputs(GTCMItemList.CircuitImprintHatchT2.get(1))
            .eut(RECIPE_UHV)
            .duration(20 * 60)
            .addTo(assembler);

        if (Config.EnableModularizedMachineSystem) {

            if (Config.EnableDimensionallyTranscendentMatterPlasmaForgePrototypeMK2) {
                GTValues.RA
                    .stdBuilder()
                    .metadata(RESEARCH_ITEM, MiracleDoor.get(1))
                    .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
                    .itemInputs(
                        eM_Teleportation.get(64),
                        ItemList.Machine_Multi_PlasmaForge.get(64),
                        ItemRefer.Compact_Fusion_Coil_T4.get(64),
                        GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),

                        AnnihilationConstrainer.get(64),
                        SpaceWarper.get(64),
                        SpaceWarper.get(64),
                        AnnihilationConstrainer.get(64),

                        ItemList.Emitter_UMV.get(64),
                        ItemList.Field_Generator_UMV.get(64),
                        ItemList.Field_Generator_UMV.get(64),
                        ItemList.Emitter_UMV.get(64),

                        AdvancedHighPowerCoilBlock.get(64),
                        ZPM6.get(64),
                        GravitationalLens.get(64),
                        AdvancedHighPowerCoilBlock.get(64)
                    )
                    .fluidInputs(
                        MaterialsUEVplus.DimensionallyTranscendentResidue.getFluid(2_000_000_000),
                        MaterialsUEVplus.Eternity.getMolten(144 * 524288),
                        GGMaterial.shirabon.getMolten(144 * 524288),
                        MaterialsUEVplus.SpaceTime.getMolten(144 * 2097152)
                    )
                    .itemOutputs(GTCMItemList.DimensionallyTranscendentMatterPlasmaForgePrototypeMK2.get(1))
                    .eut(RECIPE_UXV)
                    .duration(20 * 3600 * 24)
                    .addTo(assemblyLine);
            }

            if (Config.EnableLargeNeutronOscillator) {
                GTValues.RA
                    .stdBuilder()
                    .itemInputs(
                        GTUtility.copyAmountUnsafe(64, Loaders.NA),
                        HiC_T5.get(64),
                        ItemRefer.Compact_Fusion_Coil_T3.get(8),
                        new Object[]{OrePrefixes.circuit.get(Materials.Optical), 8}
                    )
                    .fluidInputs(
                        MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 514),
                        GGMaterial.metastableOganesson.getMolten(144 * 514),
                        GGMaterial.dalisenite.getMolten(144 * 514)
                    )
                    .itemOutputs(GTCMItemList.LargeNeutronOscillator.get(1))
                    .specialValue(3)
                    .eut(RECIPE_UEV)
                    .duration(20 * 600)
                    .addTo(GoodGeneratorRecipeMaps.preciseAssemblerRecipes);
            }

            if (Config.EnableRecipeRegistry_IndistinctTentacle) {
                TST_RecipeBuilder
                    .builder()
                    .itemInputs(
                        GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                        GTCMItemList.PerfectExecutionCore.get(1),
                        GTCMItemList.IndistinctTentacle.get(16),
                        AdvancedHighPowerCoilBlock.get(64),

                        GTUtility.copyAmountUnsafe(16384, ItemList.EnergisedTesseract.get(1)),
                        StellarConstructionFrameMaterial.get(512),
                        DysonSphereFrameComponent.get(512),
                        AnnihilationConstrainer.get(512),
                        GTUtility.copyAmountUnsafe(1024, GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1)),
                        GTUtility.copyAmountUnsafe(1024, eM_Ultimate_Containment_Advanced.get(1))
                    )
                    .fluidInputs(
                        Materials.UUMatter.getFluid(2_000_000_000),
                        MaterialsUEVplus.Eternity.getMolten(144 * 524288),
                        MaterialsUEVplus.Universium.getMolten(144 * 524288),
                        MaterialsUEVplus.SpaceTime.getMolten(144 * 524288)
                    )
                    .itemOutputs(GTCMItemList.IndistinctTentaclePrototypeMK2.get(1))
                    .eut(RECIPE_MAX)
                    .duration(20 * 3600 * 24)
                    .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
            }

            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, ItemList.MassFabricatorUMV.get(1))
                .metadata(SCANNING, new Scanning(24 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 64),
                    Industrial_MassFab.get(64),
                    ZPM3.get(64),
                    getCircuits(Materials.UXV, 64),

                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Field_Generator_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),
                    ItemList.Emitter_UMV.get(64),

                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),
                    Laser_Lens_Special.get(64),

                    HighEnergyFlowCircuit.get(64),
                    Machine_Multi_Transformer.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 64)
                )
                .fluidInputs(
                    MaterialsUEVplus.Space.getMolten(144 * 256),
                    MaterialsUEVplus.Time.getMolten(144 * 256),
                    MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(144 * 1024),
                    Materials.CosmicNeutronium.getMolten(144 * 2048)
                )
                .itemOutputs(GTCMItemList.MassFabricatorGenesis.get(1))
                .eut(RECIPE_UMV)
                .duration(20 * 3600)
                .addTo(assemblyLine);

        }

        if(Config.Enable_BloodHell) {

            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt,Materials.BloodInfusedIron,1),
                    new ItemStack(WayofTime.alchemicalWizardry.ModItems.blankSlate,6),
                    GTUtility.getIntegratedCircuit(6))
                .fluidInputs(BloodMagicHelper.getLifeEssence(1000))
                .itemOutputs(GTCMItemList.BloodyCasing1.get(1))
                .eut(0)
                .duration(20 * 18)
                .metadata(BloodyHellTierKey.INSTANCE, 2)
                .addTo(GTCMRecipe.BloodyHellRecipes);

            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    Mods.DraconicEvolution.isModLoaded()?
                    GTModHandler.getModItem(Mods.DraconicEvolution.ID,"draconicBlock",1):TestItem0.get(1),
                    GTUtility.getIntegratedCircuit(6))
                .fluidInputs(BloodMagicHelper.getLifeEssence(100000))
                .itemOutputs(GTCMItemList.BloodyCasing2.get(1))
                .eut(0)
                .duration(20 * 300)
                .metadata(BloodyHellTierKey.INSTANCE, 5)
                .addTo(GTCMRecipe.BloodyHellRecipes);
        }

        if(Config.Enable_SwelegfyrBlastFurnace){
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, GregtechItemList.Machine_Adv_BlastFurnace.get(1))
                .metadata(SCANNING, new Scanning(30 * MINUTES, TierEU.RECIPE_LV))
                .itemInputs(
                    GregtechItemList.Machine_Adv_BlastFurnace.get(64),
                    GregtechItemList.Machine_Adv_BlastFurnace.get(64),
                    GregtechItemList.Machine_Adv_BlastFurnace.get(64),
                    GregtechItemList.Machine_Adv_BlastFurnace.get(64),

                    ItemList.Electric_Pump_UV.get(8),
                    ItemList.Conveyor_Module_UV.get(16),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 32},
                    new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 64},

                    HighEnergyFlowCircuit.get(64),
                    ItemList.Circuit_Chip_QPIC.get(32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt08,Materials.SuperconductorUV,16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense,Materials.Neutronium,4)
                )
                .fluidInputs(
                    MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 16),
                    MaterialsAlloy.BLACK_TITANIUM.getFluidStack(144 * 64),
                    WerkstoffLoader.Oganesson.getFluidOrGas(1000 * 8)
                )
                .itemOutputs(GTCMItemList.SwelegfyrBlastFurnace.get(1))
                .eut(RECIPE_UHV)
                .duration(20 * 120)
                .addTo(assemblyLine);

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTCMItemList.SwelegfyrBlastFurnace.get(1),
                64_000,
                32,
                600_000,
                4,
                new Object[]{
                    ItemList.Circuit_Board_Multifiberglass_Elite.get(1),
                    ItemList.ZPM2.get(1),
                    Materials.Silver.getNanite(2),
                    ItemRefer.Fluid_Storage_Core_T5.get(2),

                    ItemList.UHV_Coil.get(64),
                    PicoWafer.get(64),
                    ItemList.Circuit_Chip_QPIC.get(64),
                    ItemList.Circuit_Chip_QPIC.get(64),

                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 16},
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 32},
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getBolt(48),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFoil(64),
                },
                new FluidStack[]{
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144 * 16),
                    MaterialsAlloy.ABYSSAL.getFluidStack(144 * 64),
                    new FluidStack(TFFluids.fluidPyrotheum, 1000 * 4096)
                },
                GTCMItemList.SwelegfyrUpgradeChip.get(1),
                20 * 320,
                (int) RECIPE_UEV
            );
        }

        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                MaterialsAlloy.PIKYONIUM.getFrameBox(1),
                GregtechItemList.Casing_Adv_BlastFurnace.get(2),

                GregtechItemList.Hatch_Input_Pyrotheum.get(1),
                GregtechItemList.TransmissionComponent_LuV.get(4),
                ItemList.DysonSwarmModule.getWithDamage(6, 3),

                MaterialsAlloy.PIKYONIUM.getPlateDense(3),
                MaterialsAlloy.ZERON_100.getPlateDense(3)
            )
            .fluidInputs(
                MaterialsAlloy.ARCANITE.getFluidStack(144 * 4)
            )
            .itemOutputs(GTCMItemList.SwelegfyrCasing.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 15)
            .addTo(assembler);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.block,Materials.Iridium,1),
            64_000,
            32,
            800_000,
            4,
            new Object[]{
                GTOreDictUnificator.get(OrePrefixes.frameGt,Materials.Neutronium,2),
                ItemList.neutroniumHeatCapacitor.get(1),
                ItemList.Neutron_Reflector.get(64),
                ItemList.DysonSwarmModule.getWithDamage(24, 3),

                ItemRefer.Advanced_Radiation_Protection_Plate.get(36),
                ItemList.Electric_Piston_UV.get(4),
                ItemList.Field_Generator_UV.get(2),
                GTOreDictUnificator.get(OrePrefixes.ring,Materials.CosmicNeutronium,64),

                GTOreDictUnificator.get(OrePrefixes.screw,Materials.Bedrockium,32)
            },
            new FluidStack[]{
                MaterialsAlloy.INDALLOY_140.getFluidStack(144 * 8),
                GGMaterial.preciousMetalAlloy.getMolten(144 * 64),
                Materials.SuperCoolant.getFluid(1000 * 8),
            },
            GTCMItemList.ReinforcedIridiumAlloyCasing.get(1),
            20 * 15,
            (int) RECIPE_UV
        );

        // Manufacturing Center
        GTModHandler.addCraftingRecipe(GTCMItemList.ManufacturingCenter.get(1), new Object[]{
            "AhA",
            "BHB",
            "CdC",
            'A', MaterialsAlloy.STABALLOY.getPlate(1),
            'B', Materials.StainlessSteel.getPlates(1),
            'C', MaterialsAlloy.ZIRCONIUM_CARBIDE.getPlate(1),
            'H', ItemList.Hull_IV,
        });

        // MultiUse Core

        BlockMultiUseCore.setupMultiUseCoreRecipe(GTCMItemList.MultiUseCore_IV.get(1), new IItemContainer[] {
            ItemList.Machine_IV_Compressor,
            ItemList.Machine_IV_Lathe,
            ItemList.Machine_IV_Polarizer,
            ItemList.Machine_IV_Fermenter,
            ItemList.Machine_IV_FluidExtractor,
            ItemList.Machine_IV_Extractor,
            ItemList.Machine_IV_LaserEngraver,
            ItemList.Machine_IV_Autoclave,
            ItemList.Machine_IV_FluidSolidifier,
        });

        BlockMultiUseCore.setupMultiUseCoreRecipe(GTCMItemList.MultiUseCore_LuV.get(1), new IItemContainer[] {
            ItemList.CompressorLuV,
            ItemList.LatheLuV,
            ItemList.PolarizerLuV,
            ItemList.FermenterLuV,
            ItemList.FluidExtractorLuV,
            ItemList.ExtractorLuV,
            ItemList.PrecisionLaserEngraverLuV,
            ItemList.AutoclaveLuV,
            ItemList.FluidSolidifierLuV,
        });

        BlockMultiUseCore.setupMultiUseCoreRecipe(GTCMItemList.MultiUseCore_ZPM.get(1), new IItemContainer[] {
            ItemList.CompressorZPM,
            ItemList.LatheZPM,
            ItemList.PolarizerZPM,
            ItemList.FermenterZPM,
            ItemList.FluidExtractorZPM,
            ItemList.ExtractorZPM,
            ItemList.PrecisionLaserEngraverZPM,
            ItemList.AutoclaveZPM,
            ItemList.FluidSolidifierZPM,
        });

        BlockMultiUseCore.setupMultiUseCoreRecipe(GTCMItemList.MultiUseCore_UV.get(1), new IItemContainer[] {
            ItemList.CompressorUV,
            ItemList.LatheUV,
            ItemList.PolarizerUV,
            ItemList.FermenterUV,
            ItemList.FluidExtractorUV,
            ItemList.ExtractorUV,
            ItemList.PrecisionLaserEngraverUV,
            ItemList.AutoclaveUV,
            ItemList.FluidSolidifierUV,
        });

        // region Pattern Access Hatch
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                ItemList.Hatch_CraftingInput_Bus_ME_ItemOnly.get(1),
                AEApi.instance().definitions().parts().storageBus().maybeStack(1).get(),
                AEApi.instance().definitions().parts().terminal().maybeStack(1).get(),
                AEApi.instance().definitions().materials().engProcessor().maybeStack(16).get(),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 8)
            )
            .itemOutputs(GTCMItemList.PatternAccessHatch.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 15)
            .addTo(assembler);
        // endregion

        // region Laser Meteor Miner
        if (Config.Enable_LaserMeteorMiner) {

            // Laser Meteor Miner
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(10),
                    getModItem(Mods.BloodMagic.ID, "masterStone", 1, new ItemStack(Blocks.stone)),
                    ItemList.OreDrill3.get(4),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 12, 28, new ItemStack(Items.diamond)),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 12, 29, new ItemStack(Items.gold_ingot)),
                    ItemList.Sensor_LuV.get(3),
                    ItemRefer.HiC_T2.get(8),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 18)
                )
                .itemOutputs(GTCMItemList.MeteorMiner.get(1))
                .eut(RECIPE_LuV)
                .duration(20 * 180)
                .addTo(assembler);

            // laser beacon
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(3),
                    ItemList.Casing_Coil_Superconductor.get(4),
                    GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 64),
                    ItemList.Emitter_LuV.get(3),
                    ItemRefer.HiC_T2.get(8),
                    HighEnergyFlowCircuit.get(1),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 18)
                )
                .itemOutputs(GTCMItemList.Laser_Beacon.get(1))
                .eut(RECIPE_LuV)
                .duration(20 * 180)
                .addTo(assembler);

            // T1 schematic
            GTValues.RA
                .stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(3),
                    getModItem(Mods.GTNHIntergalactic.ID, "item.MiningDrone", 1, 2, new ItemStack(Items.iron_pickaxe)),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 1, 28, new ItemStack(Items.diamond)),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 1, 29, new ItemStack(Items.gold_ingot)),
                    ItemList.Emitter_LuV.get(1),
                    ItemRefer.HiC_T2.get(8),
                    GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.TungstenSteel, 18)
                )
                .itemOutputs(GTCMItemList.MeteorMinerSchematic1.get(1))
                .eut(RECIPE_LuV)
                .duration(20 * 180)
                .addTo(assembler);

            // T2 schematic
            GTValues.RA
                .stdBuilder()
                .metadata(RESEARCH_ITEM, GTCMItemList.MeteorMinerSchematic1.get(1))
                .metadata(SCANNING, new Scanning(2 * HOURS, TierEU.RECIPE_LV))
                .itemInputs(
                    getModItem(Mods.GTNHIntergalactic.ID, "item.MiningDrone", 1, 6, new ItemStack(Items.iron_pickaxe)),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 16, 28, new ItemStack(Items.diamond)),
                    getModItem(Mods.BloodMagic.ID, "bloodMagicBaseItems", 16, 29, new ItemStack(Items.gold_ingot)),
                    ItemList.Emitter_ZPM.get(1),
                    ItemRefer.HiC_T3.get(8),
                    HighEnergyFlowCircuit.get(2)
                )
                .fluidInputs(
                    new FluidStack(solderIndAlloy, 144 * 16)
                )
                .itemOutputs(GTCMItemList.MeteorMinerSchematic2.get(1))
                .eut(RECIPE_UV)
                .duration(20 * 300)
                .addTo(assemblyLine);

        }

        // endregion



    }

    // spotless:on
}
