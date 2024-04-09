package com.Nxer.TwistSpaceTechnology.config;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import java.io.File;
import java.math.BigInteger;

import net.minecraftforge.common.config.Configuration;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_CleanRoom;

// spotless:off
public class Config {
    // region Regions enum
    public static final String GENERAL = "General";
    public static final String RECIPE = "Recipe";
    public static final String DSP = "DSP";
    public static final String IntensifyChemicalDistorter = "IntensifyChemicalDistorter";
    public static final String PreciseHighEnergyPhotonicQuantumMaster = "PreciseHighEnergyPhotonicQuantumMaster";
    public static final String MiracleTop = "MiracleTop";
    public static final String MagneticDrivePressureFormer = "MagneticDrivePressureFormer";
    public static final String PhysicalFormSwitcher = "PhysicalFormSwitcher";
    public static final String MagneticMixer = "MagneticMixer";
    public static final String HolySeparator = "HolySeparator";
    public static final String MiracleDoor = "MiracleDoor";
    public static final String MagneticDomainConstructor = "MagneticDomainConstructor";
    public static final String Silksong = "Silksong";
    public static final String SpaceScaler = "SpaceScaler";
    public static final String MoleculeDeconstructor = "MoleculeDeconstructor";
    public static final String CrystallineInfinitier = "CrystallineInfinitier";
    public static final String HyperSpacetimeTransformer = "HyperSpacetimeTransformer";
    public static final String Scavenger = "Scavenger";
    public static final String AdvancedMegaOilCracker = "AdvancedMegaOilCracker";
    public static final String IndistinctTentacle = "IndistinctTentacle";
    public static final String ThermalEnergyDevourer = "ThermalEnergyDevourer";
    public static final String VacuumFilterExtractor = "VacuumFilterExtractor";
    public static final String MEG = "TowerOFAbstraction";
    public static final String BeeEngineer = "BeeEngineer";
    public static final String MegaMacerator = "HouseholdCellFragmentizer";
    public static final String HephaestusAtelier = "HephaestusAtelier";
    public static final String DeployedNanoCore = "DeployedNanoCore";
    public static final String CoreDeviceOfHumanPowerGenerationFacility = "CoreDeviceOfHumanPowerGenerationFacility";
    public static final String BallLightning = "Ball Lightning";
    public static final String StarcoreMiner = "StarcoreMiner";
    public static final String Disassembler = "Disassembler";
    public static final String EOW = "Eye of Wood";
    public static final String SingleBlocks = "SingleBlocks";
    public static final String spaceStation="spaceStation";
    public static final String SpaceApiary = "SpaceApiary";
    public static final String IndustrialMagnetarSeparator = "IndustrialMagnetarSeparator";
    public static final String CombatStats = "CombatStats";
    // endregion

    // region General
    public static int MAX_PARALLEL_LIMIT = Integer.MAX_VALUE;

    // endregion

    // region Dyson Sphere Program
    public static boolean EnableDysonSphereProgramSystem = true;
    public static long EUPerCriticalPhoton = Integer.MAX_VALUE;
    public static long solarSailPowerPoint = 524288;
    public static BigInteger solarSailPowerPoint_BigInteger = BigInteger.valueOf(524288);
    public static long solarSailCanHoldPerNode = 256;
    public static long solarSailCanHoldDefault = 2048;
    public static long maxPowerPointPerReceiver = 1024L * Integer.MAX_VALUE;
    public static long EUEveryAntimatterFuelRod = 1024L * Integer.MAX_VALUE;// default 1024L * Integer.MAX_VALUE;
    public static long EUEveryAntimatter = 4L * Integer.MAX_VALUE;// default 4L * Integer.MAX_VALUE;
    public static double secondsOfArtificialStarProgressCycleTime = 6.4;
    public static int secondsOfEverySpaceWarperProvideToOverloadTime = 60 * 15;
    public static int overloadSpeedUpMultiplier = 30;
    public static double gravitationalLensSpeedMultiplier = 4;
    public static int secondsOfEveryGravitationalLensProvideToIntensifyTime = 60 * 10;
    public static double secondsOfLaunchingSolarSail = 120;
    public static double secondsOfLaunchingNode = 900;
    public static int EUTOfLaunchingSolarSail = (int) RECIPE_UHV;
    public static int EUTOfLaunchingNode = (int) RECIPE_UMV;
    public static boolean EnableRenderDefaultArtificialStar = true;

    // endregion

    // region Miracle Door
    public static double secondsOfMiracleDoorProcessingTimeABSMode = 25.6;
    public static double secondsOfMiracleDoorProcessingTimeEBFMode = 128;
    public static int amountOfPhotonsEveryMiracleDoorProcessingCost = 1;
    public static int multiplierOfMiracleDoorEUCostABSMode = 16;
    public static int multiplierOfMiracleDoorEUCostEBFMode = 64;
    public static boolean OutputMoltenFluidInsteadIngotInStellarForgeRecipe = true;

    // endregion

    // region Intensify Chemical Distorter
    public static int Mode_Default_IntensifyChemicalDistorter = 0;
    public static int Parallel_LCRMode_IntensifyChemicalDistorter = 1024;
    public static int Parallel_ICDMode_IntensifyChemicalDistorter = 16;
    public static int SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter = 10;
    public static int SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter = 1;

    // endregion

    // region Magnetic Drive Pressure Former
    public static byte Mode_Default_MagneticDrivePressureFormer = 0;
    public static int SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer = 8;
    public static int SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer = 16;
    public static int SpeedUpMultiplier_Coil_MagneticDrivePressureFormer = 1;
    public static int Parallel_MagneticDrivePressureFormer = 1024;
    public static float EU_Multiplier_MagneticDrivePressureFormer = 0.75F;
    public static int GlassTier_LimitLaserHatch_MagneticDrivePressureFormer = 11;
    public static int CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer = 11;
    // endregion

    // region Miracle Top
    public static byte Mode_Default_MiracleTop = 0;
    public static int SpeedUpMultiplier_PerRing_MiracleTop = 4;
    public static int Parallel_PerRing_MiracleTop = 128;
    public static int RingsAmount_EnablePerfectOverclock_MiracleTop = 8;
    // endregion

    // region Physical Form Switcher
    public static boolean Mode_Default_PhysicalFormSwitcher = false;
    public static float SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher = 0.9F;
    // endregion

    // region Magnetic Mixer
    public static float SpeedBonus_MultiplyPerTier_MagneticMixer = 0.8F;
    // endregion

    // region Precise High Energy Photonic Quantum Master
    public static boolean Mode_Default_PreciseHighEnergyPhotonicQuantumMaster = false;
    public static int SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = 2;
    public static int SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = 1;
    public static int Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = 256;
    public static int Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = 16;
    public static int[] PhotonControllerUpgradeCasingSpeedIncrement = new int[]{ /* LV */100, /* MV */200, /* HV */300, /* EV */400, /* IV */500,
        /* LuV */1000, /* ZPM */2000, /* UV */4000, /* UHV */7000, /* UEV */10000, /* UIV */14000, /* UMV */19000,
        /* UXV */25000, /* MAX */32000};
    // endregion

    // region Magnetic Domain Constructor
    public static byte Mode_Default_MagneticDomainConstructor = 0;
    public static float SpeedBonus_MultiplyPerTier_MagneticDomainConstructor = 0.8F;
    public static int Parallel_PerRing_MagneticDomainConstructor = 32;
    // endregion

    // region Silksong
    public static float SpeedBonus_MultiplyPerCoilTier_Silksong = 0.8F;
    public static int Parallel_PerPiece_Silksong = 24;
    // endregion

    // region Holy Separator
    public static byte Mode_Default_HolySeparator = 0;
    public static int Piece_EnablePerfectOverclock_HolySeparator = 16;
    public static int ParallelPerPiece_HolySeparator = 8;
    public static float SpeedBonus_MultiplyPerTier_HolySeparator = 0.9F;
    // endregion

    // region Space Scaler
    public static byte Mode_Default_SpaceScaler = 0;
    public static int Multiplier_ExtraOutputsPerFieldTier_SpaceScaler = 4;
    public static int SpeedMultiplier_Tier1Block_SpaceScaler = 1;
    public static int SpeedMultiplier_BeyondTier2Block_SpaceScaler = 10;
    // endregion

    // region Hyper Spacetime Transformer
    public static byte Mode_Default_HyperSpacetimeTransformer = 0;
    public static int ParallelMultiplier_HyperSpacetimeTransformer = 1;
    public static int SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer = 5;
    public static int SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer = 1;
    public static boolean EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer = true;
    // endregion

    // region Molecule Deconstructor
    public static byte Mode_Default_MoleculeDeconstructor = 0;
    public static int PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor = 16;
    public static int Parallel_PerPiece_MoleculeDeconstructor = 24;
    public static float SpeedBonus_MultiplyPerTier_MoleculeDeconstructor = 0.9F;
    // endregion

    // region Crystalline Infinitier
    public static byte Mode_Default_CrystallineInfinitier = 0;
    public static int SpeedMultiplier_AutoclaveMode_CrystallineInfinitier = 4;
    public static int SpeedMultiplier_ChemicalBath_CrystallineInfinitier = 16;
    public static int SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier = 1;
    public static int ParallelMultiplier_CrystallineInfinitier = 1;
    public static byte FieldTier_EnablePerfectOverclock_CrystallineInfinitier = 3;
    // endregion

    // region Scavenger
    public static boolean EnablePerfectOverclock_Scavenger = false;
    public static float EuModifier_Scavenger = 0.6F;
    public static double SpeedBonus_MultiplyPerTier_Scavenger = 0.8D;
    // endregion

    // region AdvancedMegaOilCracker
    public static boolean EnablePerfectOverclock_AdvancedMegaOilCracker = false;
    public static float SpeedBonus_AdvancedMegaOilCracker = 0.5F;
    public static int Parallel_AdvancedMegaOilCracker = 256;
    // endregion

    // region IndistinctTentacle
    public static boolean EnableRecipeRegistry_IndistinctTentacle = true;
    public static byte Mode_Default_IndistinctTentacle = 0;
    public static int SpeedMultiplier_AssemblyLine_IndistinctTentacle = 1;
    public static int SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle = 2;
    public static int SpeedMultiplier_Assembler_IndistinctTentacle = 4;
    public static int SpeedMultiplier_PreciseAssembler_IndistinctTentacle = 4;
    public static int Parallel_Default_IndistinctTentacle = 256;
    public static int TickEveryProcess_WirelessMode_IndistinctTentacle = 512;
    public static int AstralArrayOverclockedTickEveryProcess_WirelessMode_IndistinctTentacle = 20;
    public static int ExtraEuCostMultiplierAstralArrayOverclocked_WirelessMode_IndistinctTentacle = 64;
    public static byte ComponentCasingTierLimit_WirelessMode_IndistinctTentacle = 12;
    public static byte GlassTierLimit_WirelessMode_IndistinctTentacle = 12;
    public static byte GlassTierLimit_LaserHatch_IndistinctTentacle = 8;
    // endregion

    // region Mega Egg Generator
    public static int MEG_Laser_Pieces = 16;
    public static int MEG_Dynamo_Limit = 1;
    public static long MEG_CrepperEgg_Gen = 512L;
    public static long MEG_DragonEgg_Gen = 2048L;
    public static long MEG_InfinityEgg_Gen = 16384L;
    public static int MEG_Efficiency_PiecesBuff = 200;
    public static int MEG_Efficiency_InfinityEggBuff = 100;
    public static int MEG_Efficiency_Lost = 500;
    public static double MEG_Overall_Multiply = 1.0D;
    public static boolean MEG_Rotation = false;
    // endregion

    // region ThermalEnergyDevourer
    public static byte Mode_Default_ThermalEnergyDevourer = 0;
    public static int Parallel_HighSpeedMode_ThermalEnergyDevourer = 1024;
    public static int Parallel_HighParallelMode_ThermalEnergyDevourer = Integer.MAX_VALUE;
    public static int TickPerProgressing_WirelessMode_ThermalEnergyDevourer = 128;
    // endregion

    // region VacuumFilterExtractor
    public static byte Mode_Default_VacuumFilterExtractor = 0;
    public static float EuModifier_VacuumFilterExtractor = 0.5F;
    // endregion

    // region BeeEngineer
    public static double BE_pChance = 0.4;
    public static double BE_pChanceEnhanced = 0.8;
    public static int BE_pHoneyCost = 128000;
    public static int BE_pUUMCost = 32000;
    public static int BE_pEachProcessTime = 20 * 10;
    // endregion

    // region Mega Macerator
    public static int BlockTier1Parallel_MegaMacerator = 128;
    public static int BlockTier2Parallel_MegaMacerator = 32768;
    public static float SpeedBonus_MegaMacerator = 0.125F;
    public static boolean EnablePerfectOverclock_MegaMacerator = false;
    // endregion

    // region HephaestusAtelier
    public static int ConsumeEutPerParallel_HephaestusAtelier = 7;
    public static int ConsumeDuration_HephaestusAtelier = 512;
    public static int ConsumeEuPerSmelting_HephaestusAtelier = 2048;
    public static int DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier = 256;
    public static int DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier = 20;

    // endregion

    // region DeployedNanoCore
    public static boolean Enable_DeployedNanoCore = true;
    public static int TickPerProgressing_WirelessMode_DeployedNanoCore = 128;
    // endregion

    // region CoreDeviceOfHumanPowerGenerationFacility
    public static boolean Enable_CoreDeviceOfHumanPowerGenerationFacility = true;
    // endregion

    // region Ball Lightning
    public static boolean Enable_BallLightning = true;
    public static int WirelessModeExtraEuCost_BallLightning = 64;
    public static int WirelessModeTickEveryProcess_BallLightning = 20;
    // end region

    // region StarcoreMiner
    public static boolean Enable_StarcoreMiner = true;
    public static byte HeightValueLimit_StarcoreMiner = 24;
    public static int StackSizeOfEveryOreItemStackWhenMining_StarcoreMiner = 131072;
    public static int AmountOfOreStackPerMining_StarcoreMiner = 24;
    public static int Eut_StarcoreMiner = (int) RECIPE_MAX;
    public static int DurationPerMining_StarcoreMiner = 128;
    public static boolean DebugMode_StarcoreMiner = false;
    // endregion

    // region Disassembler
    public static boolean Enable_Disassembler = true;
    public static int CostTicksPerItemDisassembling_Disassembler = 100;
    // endregion

    // region Eye of Wood
    public static int StandardWaterNeed_EyeOfWood = 256;
    public static int StandardLavaNeed_EyeOfWood = 256;
    public static int SecondsPerProcessing_EyeOfWood = 60;

    // endregion

    // region Industrial Magnetar Separator
    public static boolean Enable_IndustrialMagnetarSeparator = true;
    public static float SpeedBouns_IndustrialMagnetarSeparator = 0.25F;
    public static float EuModifier_IndustrialMagnetarSeparator = 0.8F;
    public static int ParallelMultiply_IndustrialMagnetarSeparator = 8;

    // endregion

    public static boolean Enable_MegaTreeFarm = true;

    // region Infinite Air Hatch

    public static double secondsOfInfiniteAirHatchFillFull = 1;
    // endregion

    // region Recipe
    public static boolean Registry_DragonBlood_ExtraRecipe = true;

    // endregion

    // region Space Apiary
    public static boolean EnableSpaceApiaryModule = true;
    public static boolean enableDNAConsuming = true;
    public static int SpaceApiaryCycleTime = 100;
    public static int SpaceApiaryDNACost_T1 = 100;
    public static int SpaceApiaryDNACost_T2 = 25;
    public static int SpaceApiaryDNACost_T3 = 5;
    public static int SpaceApiaryDNACost_T4 = 1;
    public static int SpaceApiaryMaxParallels_T1 = 256;
    public static int SpaceApiaryMaxParallels_T2 = 4096;
    public static int SpaceApiaryMaxParallels_T3 = 32768;
    public static int SpaceApiaryMaxParallels_T4 = 2147483647;
    public static boolean SpaceApiaryEnableDisplayInfo = true;


    // endregion

    public static boolean activateMegaSpaceStation = false;
    public static boolean activateCombatStats = false;

    public static boolean Enable_LargeCanner = true;

    public static boolean Enable_IndustrialMagicMatrix = true;


    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        // region General
        MAX_PARALLEL_LIMIT = configuration.getInt("MAX_PARALLEL_LIMIT", GENERAL, MAX_PARALLEL_LIMIT, 1, Integer.MAX_VALUE, "Max parallel limit of normal machines.");
        // endregion

        // region Recipe
        Registry_DragonBlood_ExtraRecipe = configuration.getBoolean("Registry_DragonBlood_ExtraRecipe", RECIPE, Registry_DragonBlood_ExtraRecipe, "Registry Dragon Blood Extra Recipes.");
        // endregion

        // region Eye of Wood
        StandardWaterNeed_EyeOfWood = configuration.getInt("StandardWaterNeed_EyeOfWood", EOW, StandardWaterNeed_EyeOfWood, 1, 1024, "Standard amount (in L) of Water per processing need of Eye of Wood. Type: int");
        StandardLavaNeed_EyeOfWood = configuration.getInt("StandardLavaNeed_EyeOfWood", EOW, StandardLavaNeed_EyeOfWood, 1, 1024, "Standard amount (in L) of Lava per processing need of Eye of Wood. Type: int");
        SecondsPerProcessing_EyeOfWood = configuration.getInt("SecondsPerProcessing_EyeOfWood", EOW, SecondsPerProcessing_EyeOfWood, 1, 3600, "How many seconds per processing cost of Eye of Wood. Type: int");

        // endregion

        // region Disassembler
        Enable_Disassembler = configuration.getBoolean("Enable_Disassembler", Disassembler, Enable_Disassembler, "Enable TST Disassembler.");
        CostTicksPerItemDisassembling_Disassembler = configuration.getInt("CostTicksPerItemDisassembling_Disassembler", Disassembler, CostTicksPerItemDisassembling_Disassembler, 1, 72000, "Cost Ticks per item disassembling.");
        // endregion

        // region StarcoreMiner
        Enable_StarcoreMiner = configuration.getBoolean("Enable_StarcoreMiner", StarcoreMiner, Enable_StarcoreMiner, "Enable Starcore Miner.");
        HeightValueLimit_StarcoreMiner = (byte) configuration.getInt("HeightValueLimit_StarcoreMiner", StarcoreMiner, HeightValueLimit_StarcoreMiner, 1, 255, "The height value of Mining Pipe structure of a Starcore Miner need to reach. Type: byte");
        StackSizeOfEveryOreItemStackWhenMining_StarcoreMiner = configuration.getInt("StackSizeOfEveryOreItemStackWhenMining_StarcoreMiner", StarcoreMiner, StackSizeOfEveryOreItemStackWhenMining_StarcoreMiner, 1, Integer.MAX_VALUE, "How many ores in one item stack when mining. Type: int");
        AmountOfOreStackPerMining_StarcoreMiner = configuration.getInt("AmountOfOreStackPerMining_StarcoreMiner", StarcoreMiner, AmountOfOreStackPerMining_StarcoreMiner, 1, Integer.MAX_VALUE, "How many ore item stacks every mining output. Type: int");
        Eut_StarcoreMiner = configuration.getInt("Eut_StarcoreMiner", StarcoreMiner, Eut_StarcoreMiner, 1, Integer.MAX_VALUE, "EU/t when Starcore Miner working. Type: int");
        DurationPerMining_StarcoreMiner = configuration.getInt("DurationPerMining_StarcoreMiner", StarcoreMiner, DurationPerMining_StarcoreMiner, 1, Integer.MAX_VALUE, "How many ticks per mining cost. 20 tick = 1 second . Type: int");
        DebugMode_StarcoreMiner = configuration.getBoolean("DebugMode_StarcoreMiner", StarcoreMiner, DebugMode_StarcoreMiner, "Debug mode.");

        // endregion

        // region CoreDeviceOfHumanPowerGenerationFacility
        Enable_CoreDeviceOfHumanPowerGenerationFacility = configuration.getBoolean("Enable_CoreDeviceOfHumanPowerGenerationFacility", CoreDeviceOfHumanPowerGenerationFacility, Enable_CoreDeviceOfHumanPowerGenerationFacility, "Enable Core Device of Human Power Generation Facility.");
        // endregion

        // region DeployedNanoCore
        Enable_DeployedNanoCore = configuration.getBoolean("Enable_DeployedNanoCore", DeployedNanoCore, Enable_DeployedNanoCore, "Enable Deployed Nano Core.");
        TickPerProgressing_WirelessMode_DeployedNanoCore = configuration.getInt("TickPerProgressing_WirelessMode_DeployedNanoCore", DeployedNanoCore, TickPerProgressing_WirelessMode_DeployedNanoCore, 1, 65536, "How many ticks per progressing cost in Wireless mode of Deployed Nano Core. Type: int");
        // endregion

        // region HephaestusAtelier
        ConsumeEutPerParallel_HephaestusAtelier = configuration.getInt("ConsumeEutPerParallel_HephaestusAtelier", HephaestusAtelier, ConsumeEutPerParallel_HephaestusAtelier, 1, 30, "In normal mode, how much EU/t per parallel cost of Hephaestus' Atelier. Type: int");
        ConsumeDuration_HephaestusAtelier = configuration.getInt("ConsumeDuration_HephaestusAtelier", HephaestusAtelier, ConsumeDuration_HephaestusAtelier, 1, 32767, "In normal mode, how many ticks every processing cost. Type: int");
        ConsumeEuPerSmelting_HephaestusAtelier = configuration.getInt("ConsumeEuPerSmelting_HephaestusAtelier", HephaestusAtelier, ConsumeEuPerSmelting_HephaestusAtelier, 1, 32767, "In wireless mode, how much EU per item smelting cost. Type: int");
        DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier = configuration.getInt("DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier", HephaestusAtelier, DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier, 1, 32767, "In wireless mode with T2 coil, how many ticks every processing cost. Type: int");
        DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier = configuration.getInt("DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier", HephaestusAtelier, DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier, 1, 32767, "In wireless mode with T3 coil, how many ticks every processing cost. Type: int");
        // endregion

        // region VacuumFilterExtractor
        Mode_Default_VacuumFilterExtractor = (byte) configuration.getInt("Mode_Default_VacuumFilterExtractor", VacuumFilterExtractor, Mode_Default_VacuumFilterExtractor, 0, 1, "Default mode when placing a Vacuum Filter Extractor controller block. 0=Distillation Tower; 1=Distillery. Type: byte");
        EuModifier_VacuumFilterExtractor = Float.parseFloat(configuration.getString("EuModifier_VacuumFilterExtractor", VacuumFilterExtractor, String.valueOf(EuModifier_VacuumFilterExtractor), "Eu Modifier of Vacuum Filter Extractor. Type: float"));
        // endregion

        // region ThermalEnergyDevourer
        Mode_Default_ThermalEnergyDevourer = (byte) configuration.getInt("Mode_Default_ThermalEnergyDevourer", ThermalEnergyDevourer, Mode_Default_ThermalEnergyDevourer, 0, 1, "The default mode when deploy Thermal Energy Devourer. 0=HighSpeedMode, 1=HighParallelMode. Type: byte");
        if (Mode_Default_ThermalEnergyDevourer < 0 || Mode_Default_ThermalEnergyDevourer > 1)
            Mode_Default_ThermalEnergyDevourer = 0;
        Parallel_HighSpeedMode_ThermalEnergyDevourer = configuration.getInt("Parallel_HighSpeedMode_ThermalEnergyDevourer", ThermalEnergyDevourer, Parallel_HighSpeedMode_ThermalEnergyDevourer, 1, Integer.MAX_VALUE, "Max Parallel of Thermal Energy Devourer high speed mode. Type: int");
        Parallel_HighParallelMode_ThermalEnergyDevourer = configuration.getInt("Parallel_HighParallelMode_ThermalEnergyDevourer", ThermalEnergyDevourer, Parallel_HighParallelMode_ThermalEnergyDevourer, 1, Integer.MAX_VALUE, "Max Parallel of Thermal Energy Devourer high parallel mode. Type: int");
        TickPerProgressing_WirelessMode_ThermalEnergyDevourer = configuration.getInt("TickPerProgressing_WirelessMode_ThermalEnergyDevourer", ThermalEnergyDevourer, TickPerProgressing_WirelessMode_ThermalEnergyDevourer, 1, 16384, "How many ticks per progressing cost in Wireless mode of Thermal Energy Devourer. Type: int");
        // endregion

        // region IndistinctTentacle
        EnableRecipeRegistry_IndistinctTentacle = configuration.getBoolean("EnableRecipeRegistry_IndistinctTentacle", IndistinctTentacle, EnableRecipeRegistry_IndistinctTentacle, "Enable Indistinct Tentacle Recipe Registry.");
        Mode_Default_IndistinctTentacle = (byte) configuration.getInt("Mode_Default_IndistinctTentacle", IndistinctTentacle, Mode_Default_IndistinctTentacle, 0, 3, "Default mode when placing a Indistinct Tentacle controller block. 0=AL; 1=CAL; 2=Assembler; 3=PreciseAssembler; Type: byte");
        SpeedMultiplier_AssemblyLine_IndistinctTentacle = configuration.getInt("SpeedMultiplier_AssemblyLine_IndistinctTentacle", IndistinctTentacle, SpeedMultiplier_AssemblyLine_IndistinctTentacle, 1, 256, "Speed Multiplier of Indistinct Tentacle Assembly Line mode. Type: int");
        SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle = configuration.getInt("SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle", IndistinctTentacle, SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle, 1, 256, "Speed Multiplier of IndistinctTentacle Component Assembly Line mode. Type: int");
        SpeedMultiplier_Assembler_IndistinctTentacle = configuration.getInt("SpeedMultiplier_Assembler_IndistinctTentacle", IndistinctTentacle, SpeedMultiplier_Assembler_IndistinctTentacle, 1, 256, "Speed Multiplier of Indistinct Tentacle Assembler mode. Type: int");
        SpeedMultiplier_PreciseAssembler_IndistinctTentacle = configuration.getInt("SpeedMultiplier_PreciseAssembler_IndistinctTentacle", IndistinctTentacle, SpeedMultiplier_PreciseAssembler_IndistinctTentacle, 1, 256, "Speed Multiplier of Indistinct Tentacle Precise Assembler mode. Type: int");
        Parallel_Default_IndistinctTentacle = configuration.getInt("Parallel_Default_IndistinctTentacle", IndistinctTentacle, Parallel_Default_IndistinctTentacle, 1, 65536, "Parallel of Indistinct Tentacle default power mode. Type: int");
        TickEveryProcess_WirelessMode_IndistinctTentacle = configuration.getInt("TickEveryProcess_WirelessMode_IndistinctTentacle", IndistinctTentacle, TickEveryProcess_WirelessMode_IndistinctTentacle, 1, 65536, "Indistinct Tentacle in Wireless Mode every process cost the ticks. Type: int");
        ComponentCasingTierLimit_WirelessMode_IndistinctTentacle = (byte) configuration.getInt("ComponentCasingTierLimit_WirelessMode_IndistinctTentacle", IndistinctTentacle, ComponentCasingTierLimit_WirelessMode_IndistinctTentacle, 0, 14, "Component Casing Tier Limit of Indistinct Tentacle Wireless Mode. LV=1; MAX=14; UMV=12. Type: byte");
        GlassTierLimit_WirelessMode_IndistinctTentacle = (byte) configuration.getInt("GlassTierLimit_WirelessMode_IndistinctTentacle", IndistinctTentacle, GlassTierLimit_WirelessMode_IndistinctTentacle, 0, 12, "Glass Tier Limit of Indistinct Tentacle Wireless Mode. Type: byte");
        GlassTierLimit_LaserHatch_IndistinctTentacle = (byte) configuration.getInt("GlassTierLimit_LaserHatch_IndistinctTentacle", IndistinctTentacle, GlassTierLimit_LaserHatch_IndistinctTentacle, 0, 12, "Glass Tier Limit of Indistinct Tentacle Laser Hatch permission. Type: byte");
        // endregion

        // region AdvancedMegaOilCracker
        EnablePerfectOverclock_AdvancedMegaOilCracker = configuration.getBoolean("EnablePerfectOverclock_AdvancedMegaOilCracker", AdvancedMegaOilCracker, EnablePerfectOverclock_AdvancedMegaOilCracker, "Enable Advanced Mega Oil Cracker Perfect Overclock. Type: boolean");
        SpeedBonus_AdvancedMegaOilCracker = Float.parseFloat(configuration.getString("SpeedBonus_AdvancedMegaOilCracker", AdvancedMegaOilCracker, String.valueOf(SpeedBonus_AdvancedMegaOilCracker), "Speed Bonus of Advanced Mega Oil Cracker. Type: float"));
        Parallel_AdvancedMegaOilCracker = configuration.getInt("Parallel_AdvancedMegaOilCracker", AdvancedMegaOilCracker, Parallel_AdvancedMegaOilCracker, 1, 65536, "Parallel of Advanced Mega Oil Cracker. Type: int");
        // endregion

        // region Scavenger
        EnablePerfectOverclock_Scavenger = configuration.getBoolean("EnablePerfectOverclock_Scavenger", Scavenger, EnablePerfectOverclock_Scavenger, "Enable perfect overclock of Scavenger.");
        EuModifier_Scavenger = Float.parseFloat(configuration.getString("EuModifier_Scavenger", Scavenger, String.valueOf(EuModifier_Scavenger), "EU Modifier of Scavenger. Type: float"));
        SpeedBonus_MultiplyPerTier_Scavenger = Double.parseDouble(configuration.getString("SpeedBonus_MultiplyPerTier_Scavenger", Scavenger, String.valueOf(SpeedBonus_MultiplyPerTier_Scavenger), "The speed bonus = this ^ tier . Type: double"));
        // endregion

        // region IntensifyChemicalDistorter
        Mode_Default_IntensifyChemicalDistorter = configuration.getInt("Mode_Default_IntensifyChemicalDistorter", IntensifyChemicalDistorter, Mode_Default_IntensifyChemicalDistorter, 0, 1, "The default mode when deploy a machine. 0=ICD, 1=LCR. Type: int");
        Parallel_LCRMode_IntensifyChemicalDistorter = configuration.getInt("Parallel_LCRMode_IntensifyChemicalDistorter", IntensifyChemicalDistorter, Parallel_LCRMode_IntensifyChemicalDistorter, 1, 32767, "Parallel of Intensify Chemical Distorter in Large Chemical Reactor mode. Type: int");
        Parallel_ICDMode_IntensifyChemicalDistorter = configuration.getInt("Parallel_ICDMode_IntensifyChemicalDistorter", IntensifyChemicalDistorter, Parallel_ICDMode_IntensifyChemicalDistorter, 1, 32767, "Parallel of Intensify Chemical Distorter in Intensify Chemical Distorter mode. Type: int");
        SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter = configuration.getInt("SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter", IntensifyChemicalDistorter, SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter, 1, 16, "Speed Multiplier of Intensify Chemical Distorter in Large Chemical Reactor mode. Type: int");
        SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter = configuration.getInt("SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter", IntensifyChemicalDistorter, SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter, 1, 16, "Speed Multiplier of Intensify Chemical Distorter in Intensify Chemical Distorter. Type: int");
        // endregion

        // region Precise High Energy Photonic Quantum Master
        Mode_Default_PreciseHighEnergyPhotonicQuantumMaster = configuration.getBoolean("Mode_Default_PreciseHighEnergyPhotonicQuantumMaster", PreciseHighEnergyPhotonicQuantumMaster, Mode_Default_PreciseHighEnergyPhotonicQuantumMaster, "The default mode when deploy a machine. true=Photon Controller, false=Laser Engraver. Type: boolean");
        SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster", PreciseHighEnergyPhotonicQuantumMaster, SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster, 1, 256, "Speed Multiplier of Precise High Energy Photonic Quantum Master in Laser Engraver mode. Type: int");
        SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster", PreciseHighEnergyPhotonicQuantumMaster, SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster, 1, 256, "Speed Multiplier of Precise High Energy Photonic Quantum Master in Photon Controller mode. Type: int");
        Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster", PreciseHighEnergyPhotonicQuantumMaster, Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster, 1, 65536, "Parallel of Precise High Energy Photonic Quantum Master in Laser Engraver mode. Type: int");
        Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster", PreciseHighEnergyPhotonicQuantumMaster, Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster, 1, 65536, "Parallel of Precise High Energy Photonic Quantum Master in Photon Controller mode. Type: int");
        PhotonControllerUpgradeCasingSpeedIncrement = configuration.get("PhotonControllerUpgradeCasingSpeedIncrement", PreciseHighEnergyPhotonicQuantumMaster, PhotonControllerUpgradeCasingSpeedIncrement, "Photon Controller Upgrade Casing Speed Increment data.")
            .getIntList();
        // endregion

        // region Miracle Top
        Mode_Default_MiracleTop = Byte.parseByte(configuration.getString("Mode_Default_MiracleTop", MiracleTop, String.valueOf(Mode_Default_MiracleTop), "The default mode when deploy a machine. 0=Circuit Assembler, 1=Gravitation Inversion. Type: byte"));
        SpeedUpMultiplier_PerRing_MiracleTop = configuration.getInt("SpeedUpMultiplier_PerRing_MiracleTop", MiracleTop, SpeedUpMultiplier_PerRing_MiracleTop, 1, 64, "Speed Up amount of per Ring. Type: int");
        Parallel_PerRing_MiracleTop = configuration.getInt("Parallel_PerRing_MiracleTop", MiracleTop, Parallel_PerRing_MiracleTop, 1, 65536, "Parallel per Ring add. Type: int");
        RingsAmount_EnablePerfectOverclock_MiracleTop = configuration.getInt("RingsAmount_EnablePerfectOverclock_MiracleTop", MiracleTop, RingsAmount_EnablePerfectOverclock_MiracleTop, 1, 16, "How many Rings can enable Perfect overclock. Type: int");
        // endregion

        // region Magnetic Drive Pressure Former
        Mode_Default_MagneticDrivePressureFormer = Byte.parseByte(configuration.getString("Mode_Default_MagneticDrivePressureFormer", MagneticDrivePressureFormer, String.valueOf(Mode_Default_MagneticDrivePressureFormer), "The default mode when deploy a machine. 0=Extruder, 1=Bender, 2=Press, 3=Hammer. Type: byte"));
        SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer", MagneticDrivePressureFormer, SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer, 1, 256, "Speed Multiplier of Magnetic Drive Pressure Former in Extruder mode. Type: int");
        SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer", MagneticDrivePressureFormer, SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer, 1, 256, "Speed Multiplier of Magnetic Drive Pressure Former in Other mode. Type: int");
        SpeedUpMultiplier_Coil_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_Coil_MagneticDrivePressureFormer", MagneticDrivePressureFormer, SpeedUpMultiplier_Coil_MagneticDrivePressureFormer, 1, 256, "Speed Up amount of per Coil tier. Type: int");
        Parallel_MagneticDrivePressureFormer = configuration.getInt("Parallel_MagneticDrivePressureFormer", MagneticDrivePressureFormer, Parallel_MagneticDrivePressureFormer, 1, 65536, "Parallel of Magnetic Drive Pressure Former. Type: int");
        EU_Multiplier_MagneticDrivePressureFormer = Float.parseFloat(configuration.getString("EU_Multiplier_MagneticDrivePressureFormer", MagneticDrivePressureFormer, String.valueOf(EU_Multiplier_MagneticDrivePressureFormer), "EU Multiplier of Magnetic Drive Pressure Former. Type: float"));
        GlassTier_LimitLaserHatch_MagneticDrivePressureFormer = configuration.getInt("GlassTier_LimitLaserHatch_MagneticDrivePressureFormer", MagneticDrivePressureFormer, GlassTier_LimitLaserHatch_MagneticDrivePressureFormer, 1, 12, "Glass Tier of Laser Hatch Limit in Magnetic Drive Pressure Former. Type: int");
        CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer = configuration.getInt("CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer", MagneticDomainConstructor, CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer, 0, 13, "The Coil Tier can enable perfect overclock in Extruder mode. 0 is Cupronickel Coil, 13 is Eternal Coil, default 11 is Infinity Coil. Type: int");
        // endregion

        // region Physical Form Switcher
        Mode_Default_PhysicalFormSwitcher = configuration.getBoolean("Mode_Default_PhysicalFormSwitcher", PhysicalFormSwitcher, Mode_Default_PhysicalFormSwitcher, "The default mode when deploy a machine. true=Fluid Extraction, false=Fluid Solidfication. Type: boolean");
        SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher", PhysicalFormSwitcher, String.valueOf(SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher), "The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Magnetic Mixer
        SpeedBonus_MultiplyPerTier_MagneticMixer = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MagneticMixer", MagneticMixer, String.valueOf(SpeedBonus_MultiplyPerTier_MagneticMixer), "The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Magnetic Domain Constructor
        Mode_Default_MagneticDomainConstructor = (byte) configuration.getInt("Mode_Default_MagneticDomainConstructor", MagneticDomainConstructor, Mode_Default_MagneticDomainConstructor, 0, 1, "The default mode when deploy a machine. 0=Electro Magnetic Separator, 1=Polarizer. Type: byte");
        SpeedBonus_MultiplyPerTier_MagneticDomainConstructor = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MagneticDomainConstructor", MagneticDomainConstructor, String.valueOf(SpeedBonus_MultiplyPerTier_MagneticDomainConstructor), "The speed bonus = this ^ tier . Type: float"));
        Parallel_PerRing_MagneticDomainConstructor = configuration.getInt("Parallel_PerRing_MagneticDomainConstructor", MagneticDomainConstructor, Parallel_PerRing_MagneticDomainConstructor, 1, 65536, "Parallel per Ring add. Type: int");
        // endregion

        // region Silksong
        SpeedBonus_MultiplyPerCoilTier_Silksong = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerCoilTier_Silksong", Silksong, String.valueOf(SpeedBonus_MultiplyPerCoilTier_Silksong), "The speed bonus = this ^ CoilTier . Type: float"));
        Parallel_PerPiece_Silksong = configuration.getInt("Parallel_PerPiece_Silksong", Silksong, Parallel_PerPiece_Silksong, 1, 65536, "Parallel per Piece add. Type: int");
        // endregion

        // region Holy Separator
        Mode_Default_HolySeparator = Byte.parseByte(configuration.getString("Mode_Default_HolySeparator", HolySeparator, String.valueOf(Mode_Default_HolySeparator), "The default mode when deploy a machine. 0=Cutter, 1=Slicer, 2=Lathe. Type: byte"));
        Piece_EnablePerfectOverclock_HolySeparator = configuration.getInt("Piece_EnablePerfectOverclock_HolySeparator", HolySeparator, Piece_EnablePerfectOverclock_HolySeparator, 1, 64, "How many piece to enable perfect overclock. Type: int");
        ParallelPerPiece_HolySeparator = configuration.getInt("ParallelPerPiece_HolySeparator", HolySeparator, ParallelPerPiece_HolySeparator, 1, 255, "How many parallel per tier add. Type: int");
        SpeedBonus_MultiplyPerTier_HolySeparator = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_HolySeparator", HolySeparator, String.valueOf(SpeedBonus_MultiplyPerTier_HolySeparator), "The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Space Scaler
        Mode_Default_SpaceScaler = (byte) configuration.getInt("Mode_Default_SpaceScaler", SpaceScaler, Mode_Default_SpaceScaler, 0, 1, "The default mode when deploy a machine. 0=Compressor, 1=Extractor. Can not set the default 2 - Cyclotron, machine will crash directly. Type: byte");
        Multiplier_ExtraOutputsPerFieldTier_SpaceScaler = configuration.getInt("Multiplier_ExtraOutputsPerFieldTier_SpaceScaler", SpaceScaler, Multiplier_ExtraOutputsPerFieldTier_SpaceScaler, 1, 64, "Extra outputs multiplier of Cyclotron mode, every higher tier of field block bring this value multiplied extra output items and fluids. Type: int");
        SpeedMultiplier_Tier1Block_SpaceScaler = configuration.getInt("SpeedMultiplier_Tier1Block_SpaceScaler", SpaceScaler, SpeedMultiplier_Tier1Block_SpaceScaler, 1, 64, "Speed Multiplier at Tier 1 field generator block. Type: int");
        SpeedMultiplier_BeyondTier2Block_SpaceScaler = configuration.getInt("SpeedMultiplier_BeyondTier2Block_SpaceScaler", SpaceScaler, SpeedMultiplier_BeyondTier2Block_SpaceScaler, 1, 64, "Speed Multiplier at beyond Tier 2 field generator block. Type: int");
        // endregion

        // region Crystalline Infinitier
        Mode_Default_CrystallineInfinitier = (byte) configuration.getInt("Mode_Default_CrystallineInfinitier", CrystallineInfinitier, Mode_Default_CrystallineInfinitier, 0, 1, "The default mode when deploy a machine. 0=Autoclave, 1=Crystalline Infinitier. Type: byte");
        SpeedMultiplier_AutoclaveMode_CrystallineInfinitier = configuration.getInt("SpeedMultiplier_AutoclaveMode_CrystallineInfinitier", CrystallineInfinitier, SpeedMultiplier_AutoclaveMode_CrystallineInfinitier, 1, 64, "Speed Multiplier of Crystalline Infinitier in Autoclave mode. Type: int");
        SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier = configuration.getInt("SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier", CrystallineInfinitier, SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier, 1, 64, "Speed Multiplier of Crystalline Infinitier in Crystalline Infinitier mode. Type: int");
        ParallelMultiplier_CrystallineInfinitier = configuration.getInt("ParallelMultiplier_CrystallineInfinitier", CrystallineInfinitier, ParallelMultiplier_CrystallineInfinitier, 1, 256, "Parallel Multiplier of Crystalline Infinitier. The final parallel will be multiplied this value. Type: int");
        FieldTier_EnablePerfectOverclock_CrystallineInfinitier = (byte) configuration.getInt("FieldTier_EnablePerfectOverclock_CrystallineInfinitier", CrystallineInfinitier, FieldTier_EnablePerfectOverclock_CrystallineInfinitier, 1, 11, "When field generator block tier is beyond this value, machine will enable perfect overclock. 3 is the lowest EOH block. Type: byte");
        // endregion

        // region Molecule Deconstructor
        Mode_Default_MoleculeDeconstructor = (byte) configuration.getInt("Mode_Default_MoleculeDeconstructor", MoleculeDeconstructor, Mode_Default_MoleculeDeconstructor, 0, 1, "The default mode when deploy a machine. 0=Electrolyzer, 1=Centrifuge. Type: byte");
        PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor = configuration.getInt("PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor", MoleculeDeconstructor, PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor, 1, 64, "How many piece can enable perfect overclock. Type: int");
        Parallel_PerPiece_MoleculeDeconstructor = configuration.getInt("Parallel_PerPiece_MoleculeDeconstructor", MoleculeDeconstructor, Parallel_PerPiece_MoleculeDeconstructor, 1, 65536, "Parallel per piece add. Type: int");
        SpeedBonus_MultiplyPerTier_MoleculeDeconstructor = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MoleculeDeconstructor", MoleculeDeconstructor, String.valueOf(SpeedBonus_MultiplyPerTier_MoleculeDeconstructor), "The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Miracle Door
        secondsOfMiracleDoorProcessingTimeABSMode = Double.parseDouble(configuration.getString("secondsOfMiracleDoorProcessingTimeABSMode", MiracleDoor, String.valueOf(secondsOfMiracleDoorProcessingTimeABSMode), "Seconds of Miracle Door Default Progress Time in Alloy Blast Smelter mode. Type: double"));
        secondsOfMiracleDoorProcessingTimeEBFMode = Double.parseDouble(configuration.getString("secondsOfMiracleDoorProcessingTimeEBFMode", MiracleDoor, String.valueOf(secondsOfMiracleDoorProcessingTimeEBFMode), "Seconds of Miracle Door Default Progress Time in Electric Blast Furnace mode. Type: double"));
        amountOfPhotonsEveryMiracleDoorProcessingCost = configuration.getInt("amountOfPhotonsEveryMiracleDoorProcessingCost", MiracleDoor, amountOfPhotonsEveryMiracleDoorProcessingCost, 0, 64, "Needed Photons amount of Miracle Door each run cost. Type: int");
        multiplierOfMiracleDoorEUCostABSMode = configuration.getInt("multiplierOfMiracleDoorEUCostABSMode", MiracleDoor, multiplierOfMiracleDoorEUCostABSMode, 1, Integer.MAX_VALUE, "Miracle Door EU Cost multiplier in Alloy Blast Smelter mode. Type: int");
        multiplierOfMiracleDoorEUCostEBFMode = configuration.getInt("multiplierOfMiracleDoorEUCostEBFMode", MiracleDoor, multiplierOfMiracleDoorEUCostEBFMode, 1, Integer.MAX_VALUE, "Miracle Door EU Cost multiplier in Electric Blast Furnace mode. Type: int");
        OutputMoltenFluidInsteadIngotInStellarForgeRecipe = configuration.getBoolean("OutputMoltenFluidInsteadIngotInStellarForgeRecipe", MiracleDoor, OutputMoltenFluidInsteadIngotInStellarForgeRecipe, "Set the Stellar Forge recipe output material type. true = output molten fluid ; false = output ingot . Type: boolean");

        // endregion

        // region Single Blocks
        secondsOfInfiniteAirHatchFillFull = Double.parseDouble(configuration.getString("secondsOfInfiniteAirHatchFillFull", SingleBlocks, String.valueOf(secondsOfInfiniteAirHatchFillFull), "How many seconds Infinite Air Hatch fill itself to max capacity. Type:double"));
        // endregion

        // region DSP
        EnableDysonSphereProgramSystem = configuration.getBoolean("EnableDysonSphereProgramSystem", DSP, EnableDysonSphereProgramSystem, "Enable Dyson Sphere Program System. Type: boolean");
        EUPerCriticalPhoton = Long.parseLong(configuration.getString("EUPerCriticalPhoton", DSP, String.valueOf(EUPerCriticalPhoton), "EU per Critical Photon Cost. Type: long"));
        solarSailPowerPoint = Long.parseLong(configuration.getString("solarSailPowerPoint", DSP, String.valueOf(solarSailPowerPoint), "DSP Power Point per Solar Sail can produce. Type: long"));
        solarSailPowerPoint_BigInteger = BigInteger.valueOf(solarSailPowerPoint);
        solarSailCanHoldPerNode = Long.parseLong(configuration.getString("solarSailCanHoldPerNode", DSP, String.valueOf(solarSailCanHoldPerNode), "Solar Sail amount per DSP Node can hold. Type: long"));
        solarSailCanHoldDefault = Long.parseLong(configuration.getString("solarSailCanHoldDefault", DSP, String.valueOf(solarSailCanHoldDefault), "Default Solar Sail amount can hold. Type: long"));
        maxPowerPointPerReceiver = Long.parseLong(configuration.getString("maxPowerPointPerReceiver", DSP, String.valueOf(maxPowerPointPerReceiver), "Max DSP Power Point per DSP Receiver can request. Type: long"));
        EUEveryAntimatterFuelRod = Long.parseLong(configuration.getString("EUEveryAntimatterFuelRod", DSP, String.valueOf(EUEveryAntimatterFuelRod), "EU of every Antimatter Fuel Rod can generate. Type: long"));
        EUEveryAntimatter = Long.parseLong(configuration.getString("EUEveryAntimatter", DSP, String.valueOf(EUEveryAntimatter), "EU of every Antimatter can generate. Type: long"));
        secondsOfArtificialStarProgressCycleTime = Double.parseDouble(configuration.getString("secondsOfArtificialStarProgressCycleTime", DSP, String.valueOf(secondsOfArtificialStarProgressCycleTime), "Seconds of Artificial Star one progress time. Type: double, turn to tick time."));
        secondsOfEverySpaceWarperProvideToOverloadTime = configuration.getInt("secondsOfEverySpaceWarperProvideToOverloadTime", DSP, secondsOfEverySpaceWarperProvideToOverloadTime, 1, Integer.MAX_VALUE, "Overload Time (second) of every Space Warper will provide. Type: int");
        overloadSpeedUpMultiplier = configuration.getInt("overloadSpeedUpMultiplier", DSP, overloadSpeedUpMultiplier, 1, 256, "How much speed up when overload mode. Type: int");
        gravitationalLensSpeedMultiplier = Double.parseDouble(configuration.getString("gravitationalLensSpeedMultiplier", DSP, String.valueOf(gravitationalLensSpeedMultiplier), "How much of Speed Multiplier when in Gravitational Lens intensify mode. Type: double"));
        secondsOfEveryGravitationalLensProvideToIntensifyTime = configuration.getInt("secondsOfEveryGravitationalLensProvideToIntensifyTime", DSP, secondsOfEveryGravitationalLensProvideToIntensifyTime, 0, Integer.MAX_VALUE, "Intensify Mode Time (second) of every Gravitational Lens will provide. Type: int");
        secondsOfLaunchingSolarSail = Double.parseDouble(configuration.getString("secondsOfLaunchingSolarSail", DSP, String.valueOf(secondsOfLaunchingSolarSail), "Seconds of launching a Solar Sail."));
        secondsOfLaunchingNode = Double.parseDouble(configuration.getString("secondsOfLaunchingNode", DSP, String.valueOf(secondsOfLaunchingNode), "Seconds of launching a Dyson Sphere Node."));
        EUTOfLaunchingSolarSail = configuration.getInt("EUTOfLaunchingSolarSail", DSP, EUTOfLaunchingSolarSail, 1, Integer.MAX_VALUE, "EUt of Launching Solar Sail.");
        EUTOfLaunchingNode = configuration.getInt("EUTOfLaunchingNode", DSP, EUTOfLaunchingNode, 1, Integer.MAX_VALUE, "EUt of Launching Node.");
        EnableRenderDefaultArtificialStar = configuration.getBoolean("EnableRenderDefaultArtificialStar", DSP, EnableRenderDefaultArtificialStar, "Enable Render of Artificial Star when placing a new one.");
        // endregion

        // region Space Station
        activateMegaSpaceStation = configuration.getBoolean("activateMegaSpaceStation", spaceStation, activateMegaSpaceStation, "decide whether can use mega space station.");
        // endregion

        // region CombatRework
        activateCombatStats = configuration.getBoolean("activateCombatStats", CombatStats, activateCombatStats, "decide whether to enable the combatstats system(WIP).DO NOT USE IT FOR NOW!");
        // endregion

        // region Hyper Spacetime Transformer
        Mode_Default_HyperSpacetimeTransformer = (byte) configuration.getInt("Mode_Default_HyperSpacetimeTransformer", HyperSpacetimeTransformer, Mode_Default_HyperSpacetimeTransformer, 0, 1, "");
        ;
        ParallelMultiplier_HyperSpacetimeTransformer = configuration.getInt("ParallelMultiplier_HyperSpacetimeTransformer", HyperSpacetimeTransformer, ParallelMultiplier_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE, "");
        ;
        SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer = configuration.getInt("SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer", HyperSpacetimeTransformer, SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE, "");
        ;
        SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer = configuration.getInt("SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer", HyperSpacetimeTransformer, SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE, "");
        ;
        EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer = configuration.getBoolean("EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer", HyperSpacetimeTransformer, EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer, "");
        ;
        // endregion

        // region Mega Egg Generator
        MEG_CrepperEgg_Gen = Long.parseLong(configuration.getString("MEG_CrepperEgg_Gen", MEG, String.valueOf(MEG_CrepperEgg_Gen), "EUt of Crepper eggs, type: long"));
        MEG_DragonEgg_Gen = Long.parseLong(configuration.getString("MEG_DragonEgg_Gen", MEG, String.valueOf(MEG_DragonEgg_Gen), "EUt of Dragon eggs, type: long"));
        MEG_InfinityEgg_Gen = Long.parseLong(configuration.getString("MEG_InfinityEgg_Gen", MEG, String.valueOf(MEG_InfinityEgg_Gen), "EUt of Crepper eggs, type: long"));
        MEG_Laser_Pieces = configuration.getInt("MEG_Laser_Pieces", MEG, 16, 1, Integer.MAX_VALUE, "Piece num when unlock laser");
        MEG_Dynamo_Limit = configuration.getInt("MEG_Dynamo_Limit", MEG, 1, 1, Integer.MAX_VALUE, "How many dynamo allowed in total");
        MEG_Efficiency_PiecesBuff = configuration.getInt("MEG_Efficiency_PiecesBuff", MEG, 200, 0, Integer.MAX_VALUE, "Every 2^n pieces bring n*this max efficiency buff");
        MEG_Efficiency_InfinityEggBuff = configuration.getInt("MEG_Efficiency_InfinityEggBuff", MEG, 100, 0, Integer.MAX_VALUE, "Every n infinity eggs bring n*this max efficiency buff");
        MEG_Efficiency_Lost = configuration.getInt("MEG_Efficiency_Lost", MEG, 500, 0, Integer.MAX_VALUE, "Every n empty position bring n*this max efficiency loss");
        MEG_Overall_Multiply = Double.parseDouble(configuration.getString("MEG_Overall_Multiply", MEG, String.valueOf(MEG_Overall_Multiply), "Overall multiply of EUt, type: double"));
        MEG_Rotation = configuration.getBoolean("MEG_Rotation", MEG, false, "If rotation allowed");
        // endregion

        // region Bee Engineer
        BE_pChance = Double.parseDouble(configuration.getString("BE_pChance", BeeEngineer, String.valueOf(BE_pChance), "Chance to successfully transform, type: double, 0.0 - 1.0"));
        BE_pChanceEnhanced = Double.parseDouble(configuration.getString("BE_pChanceEnhanced", BeeEngineer, String.valueOf(BE_pChanceEnhanced), "Chance to successfully transform with UUM, type: double, 0.0 - 1.0"));
        BE_pHoneyCost = configuration.getInt("BE_pHoneyCost", BeeEngineer, BE_pHoneyCost, 0, Integer.MAX_VALUE, "Honey needed for each try to transform drone.");
        BE_pUUMCost = configuration.getInt("BE_pUUMCost", BeeEngineer, BE_pUUMCost, 0, Integer.MAX_VALUE, "UUM needed for each try to enhance.");
        BE_pEachProcessTime = configuration.getInt("BE_pEachProcessTime", BeeEngineer, BE_pEachProcessTime, 1, 65536 * 20, "Time needed for each try to transform drone, in ticks.");
        // endregion

        // region Mega Macerator
        BlockTier1Parallel_MegaMacerator = configuration.getInt("BlockTier1Parallel_MegaMacerator", MegaMacerator, BlockTier1Parallel_MegaMacerator, 1, 2147483646, "Parallel of Tier 1. Type: int");
        BlockTier2Parallel_MegaMacerator = configuration.getInt("BlockTier2Parallel_MegaMacerator", MegaMacerator, BlockTier2Parallel_MegaMacerator, 1, 2147483646, "Parallel of Tier 2. Type: int");
        SpeedBonus_MegaMacerator = Float.parseFloat(configuration.getString("SpeedBonus_MegaMacerator", MegaMacerator, String.valueOf(SpeedBonus_MegaMacerator), "Speed Bonus of Mega Macerator. Type: float"));
        EnablePerfectOverclock_MegaMacerator = configuration.getBoolean("EnablePerfectOverclock_MegaMacerator", MegaMacerator, EnablePerfectOverclock_MegaMacerator, "Enable perfect overclock of Mega Macerator. Type: boolean");
        // end region

        // region Ball Lightning
        Enable_BallLightning = configuration.getBoolean("Enable_BallLightning", BallLightning, Enable_BallLightning, "Enable Ball Lightning.");
        WirelessModeExtraEuCost_BallLightning = configuration.getInt("WirelessModeExtraEuCost_BallLightning", BallLightning, WirelessModeExtraEuCost_BallLightning, 1, 2147483646, "Wireless Mode Extra Eu Cost. Type: int");
        WirelessModeTickEveryProcess_BallLightning = configuration.getInt("WirelessModeTickEveryProcess_BallLightning", BallLightning, WirelessModeTickEveryProcess_BallLightning, 1, 2147483646, "Wireless Mode Work Ticks. Type: int");
        // end region

        // region Space Apiary
        EnableSpaceApiaryModule = configuration.getBoolean("EnableSpaceApiaryModule", SpaceApiary, EnableSpaceApiaryModule, "Enable Space Apiary Module.");
        enableDNAConsuming = configuration.getBoolean("enableDNAConsuming", SpaceApiary, enableDNAConsuming, "Enable DNA consuming for Space Apiary Modules. Type: boolean");
        SpaceApiaryCycleTime = configuration.getInt("SpaceApiaryCycleTime", SpaceApiary, SpaceApiaryCycleTime, 1, 2147483646, "Ticks required for each run. Type: int");
        SpaceApiaryDNACost_T1 = configuration.getInt("SpaceApiaryDNACost_T1", SpaceApiary, SpaceApiaryDNACost_T1,1, 2147483646, "DNA needed per parallel for Space Apiary Module MK-I. Type: int");
        SpaceApiaryDNACost_T2 = configuration.getInt("SpaceApiaryDNACost_T2", SpaceApiary, SpaceApiaryDNACost_T2, 1, 2147483646,"DNA needed per parallel for Space Apiary Module MK-II. Type: int");
        SpaceApiaryDNACost_T3 = configuration.getInt("SpaceApiaryDNACost_T3", SpaceApiary, SpaceApiaryDNACost_T3,1, 2147483646, "DNA needed per parallel for Space Apiary Module MK-III. Type: int");
        SpaceApiaryDNACost_T4 = configuration.getInt("SpaceApiaryDNACost_T4", SpaceApiary, SpaceApiaryDNACost_T4,1, 2147483646, "DNA needed per parallel for Space Apiary Module MK-IV. Type: int");
        SpaceApiaryMaxParallels_T1 = configuration.getInt("SpaceApiaryMaxParallels_T1", SpaceApiary, SpaceApiaryMaxParallels_T1, 1, 2147483646, "Max parallels for Space Apiary Module MK-I. Type: int");
        SpaceApiaryMaxParallels_T2 = configuration.getInt("SpaceApiaryMaxParallels_T2", SpaceApiary, SpaceApiaryMaxParallels_T2, 1, 2147483646, "Max parallels for Space Apiary Module MK-II. Type: int");
        SpaceApiaryMaxParallels_T3 = configuration.getInt("SpaceApiaryMaxParallels_T3", SpaceApiary, SpaceApiaryMaxParallels_T3, 1, 2147483646, "Max parallels for Space Apiary Module MK-III. Type: int");
        SpaceApiaryMaxParallels_T4 = configuration.getInt("SpaceApiaryMaxParallels_T4", SpaceApiary, SpaceApiaryMaxParallels_T4, 1, 2147483646, "Max parallels for Space Apiary Module MK-IV. Type: int");
        SpaceApiaryEnableDisplayInfo = configuration.getBoolean("SpaceApiaryEnableDisplayInfo", SpaceApiary, SpaceApiaryEnableDisplayInfo, "Enable output display in controller. Type: boolean");
        // endregion

        // region Industrial Magnetar Separator
        Enable_IndustrialMagnetarSeparator = configuration.getBoolean("EnableIndustrialMagnetarSeparator",IndustrialMagnetarSeparator, Enable_IndustrialMagnetarSeparator, "Enable Industrial Magnetar Separator.");;
        SpeedBouns_IndustrialMagnetarSeparator = Float.parseFloat(configuration.getString("SpeedBonus_IndustrialMagnetarSeparator", IndustrialMagnetarSeparator, String.valueOf(SpeedBouns_IndustrialMagnetarSeparator), "Speed Bonus of Industrial Magnetar Separator. Type: float"));
        EuModifier_IndustrialMagnetarSeparator = Float.parseFloat(configuration.getString("EuModifier_IndustrialMagnetarSeparator", IndustrialMagnetarSeparator, String.valueOf(EuModifier_IndustrialMagnetarSeparator), "Eu Modifier of Industrial Magnetar Separator. Type: float"));
        ParallelMultiply_IndustrialMagnetarSeparator = configuration.getInt("ParallelMultiply_IndustrialMagnetarSeparator", IndustrialMagnetarSeparator, ParallelMultiply_IndustrialMagnetarSeparator, 1, 2147483646, "Parallel Multiply of Industrial Magnetar Separator. Type: int");;
        // endregion

        // region Mega Tree Farm

        // endregion

        TST_CleanRoom.loadConfig(configuration);
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
// spotless:on
