package com.Nxer.TwistSpaceTechnology.config;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UHV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UMV;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

// spotless:off
public class Config {
    // region Regions enum
    public static final String GENERAL = "General";
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
<<<<<<< HEAD
    public static final String HyperSpacetimeTransformer = "HyperSpacetimeTransformer";
=======
    public static final String Scavenger = "Scavenger";
>>>>>>> main
    public static final String SingleBlocks = "SingleBlocks";

    public static final String spaceStation="spaceStation";

    public static final String CombatStats="CombatStats";
    // endregion

    // region General
    public static int MAX_PARALLEL_LIMIT = Integer.MAX_VALUE;

    // endregion

    // region Dyson Sphere Program
    public static long EUPerCriticalPhoton = Integer.MAX_VALUE;
    public static long solarSailPowerPoint = 524288;
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
    public static int[] PhotonControllerUpgradeCasingSpeedIncrement = new int[] { /* LV */100, /* MV */200, /* HV */300, /* EV */400, /* IV */500,
        /* LuV */1000, /* ZPM */2000, /* UV */4000, /* UHV */7000, /* UEV */10000, /* UIV */14000, /* UMV */19000,
        /* UXV */25000, /* MAX */32000 };
    // endregion

    // region Magnetic Domain Constructor
    public static byte Mode_Default_MagneticDomainConstructor = 0;
    public static float SpeedBonus_MultiplyPerTier_MagneticDomainConstructor = 0.9F;
    public static int Parallel_PerRing_MagneticDomainConstructor = 8;
    // endregion

    // region Silksong
    public static float SpeedBonus_MultiplyPerCoilTier_Silksong = 0.9F;
    public static int Parallel_PerPiece_Silksong = 8;
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

    // region Molecule Deconstructor
    public static byte Mode_Default_MoleculeDeconstructor = 0;
    public static int PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor = 16;
    public static int Parallel_PerPiece_MoleculeDeconstructor = 24;
    public static float SpeedBonus_MultiplyPerTier_MoleculeDeconstructor = 0.9F;
    // endregion

    // region Crystalline Infinitier
    public static byte Mode_Default_CrystallineInfinitier = 0;
    public static int SpeedMultiplier_AutoclaveMode_CrystallineInfinitier = 4;
    public static int SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier = 1;
    public static int ParallelMultiplier_CrystallineInfinitier = 1;
    public static byte FieldTier_EnablePerfectOverclock_CrystallineInfinitier = 3;
    // endregion

    // region Scavenger
    public static boolean EnablePerfectOverclock_Scavenger = false;
    public static float EuModifier_Scavenger = 0.6F;
    public static double SpeedBonus_MultiplyPerTier_Scavenger = 0.8D;
    // endregion

    // region Infinite Air Hatch
    public static double secondsOfInfiniteAirHatchFillFull = 1;
    // endregion

    public static boolean activateMegaSpaceStation=false;
    public static boolean activateCombatStats=false;
    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        // region General
        MAX_PARALLEL_LIMIT = configuration.getInt("MAX_PARALLEL_LIMIT", GENERAL, MAX_PARALLEL_LIMIT, 1, Integer.MAX_VALUE, "Max parallel limit of normal machines.");

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
        Mode_Default_PreciseHighEnergyPhotonicQuantumMaster = configuration.getBoolean("Mode_Default_PreciseHighEnergyPhotonicQuantumMaster",PreciseHighEnergyPhotonicQuantumMaster,Mode_Default_PreciseHighEnergyPhotonicQuantumMaster,"The default mode when deploy a machine. true=Photon Controller, false=Laser Engraver. Type: boolean");
        SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster",PreciseHighEnergyPhotonicQuantumMaster,SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster,1,256,"Speed Multiplier of Precise High Energy Photonic Quantum Master in Laser Engraver mode. Type: int");
        SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster",PreciseHighEnergyPhotonicQuantumMaster,SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster,1,256,"Speed Multiplier of Precise High Energy Photonic Quantum Master in Photon Controller mode. Type: int");
        Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster",PreciseHighEnergyPhotonicQuantumMaster,Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster,1,65536,"Parallel of Precise High Energy Photonic Quantum Master in Laser Engraver mode. Type: int");
        Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = configuration.getInt("Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster",PreciseHighEnergyPhotonicQuantumMaster,Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster,1,65536,"Parallel of Precise High Energy Photonic Quantum Master in Photon Controller mode. Type: int");
        PhotonControllerUpgradeCasingSpeedIncrement = configuration.get("PhotonControllerUpgradeCasingSpeedIncrement", PreciseHighEnergyPhotonicQuantumMaster, PhotonControllerUpgradeCasingSpeedIncrement, "Photon Controller Upgrade Casing Speed Increment data.")
                                                                   .getIntList();
        // endregion

        // region Miracle Top
        Mode_Default_MiracleTop = Byte.parseByte(configuration.getString("Mode_Default_MiracleTop",MiracleTop,String.valueOf(Mode_Default_MiracleTop),"The default mode when deploy a machine. 0=Circuit Assembler, 1=Gravitation Inversion. Type: byte"));
        SpeedUpMultiplier_PerRing_MiracleTop = configuration.getInt("SpeedUpMultiplier_PerRing_MiracleTop",MiracleTop,SpeedUpMultiplier_PerRing_MiracleTop,1,64,"Speed Up amount of per Ring. Type: int");
        Parallel_PerRing_MiracleTop = configuration.getInt("Parallel_PerRing_MiracleTop",MiracleTop,Parallel_PerRing_MiracleTop,1,65536,"Parallel per Ring add. Type: int");
        RingsAmount_EnablePerfectOverclock_MiracleTop = configuration.getInt("RingsAmount_EnablePerfectOverclock_MiracleTop",MiracleTop,RingsAmount_EnablePerfectOverclock_MiracleTop,1,16,"How many Rings can enable Perfect overclock. Type: int");
        // endregion

        // region Magnetic Drive Pressure Former
        Mode_Default_MagneticDrivePressureFormer = Byte.parseByte(configuration.getString("Mode_Default_MagneticDrivePressureFormer",MagneticDrivePressureFormer,String.valueOf(Mode_Default_MagneticDrivePressureFormer),"The default mode when deploy a machine. 0=Extruder, 1=Bender, 2=Press, 3=Hammer. Type: byte"));
        SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer",MagneticDrivePressureFormer,SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer,1,256,"Speed Multiplier of Magnetic Drive Pressure Former in Extruder mode. Type: int");
        SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer",MagneticDrivePressureFormer,SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer,1,256,"Speed Multiplier of Magnetic Drive Pressure Former in Other mode. Type: int");
        SpeedUpMultiplier_Coil_MagneticDrivePressureFormer = configuration.getInt("SpeedUpMultiplier_Coil_MagneticDrivePressureFormer",MagneticDrivePressureFormer,SpeedUpMultiplier_Coil_MagneticDrivePressureFormer,1,256,"Speed Up amount of per Coil tier. Type: int");
        Parallel_MagneticDrivePressureFormer = configuration.getInt("Parallel_MagneticDrivePressureFormer",MagneticDrivePressureFormer,Parallel_MagneticDrivePressureFormer,1,65536,"Parallel of Magnetic Drive Pressure Former. Type: int");
        EU_Multiplier_MagneticDrivePressureFormer = Float.parseFloat(configuration.getString("EU_Multiplier_MagneticDrivePressureFormer",MagneticDrivePressureFormer,String.valueOf(EU_Multiplier_MagneticDrivePressureFormer),"EU Multiplier of Magnetic Drive Pressure Former. Type: float"));
        GlassTier_LimitLaserHatch_MagneticDrivePressureFormer = configuration.getInt("GlassTier_LimitLaserHatch_MagneticDrivePressureFormer",MagneticDrivePressureFormer,GlassTier_LimitLaserHatch_MagneticDrivePressureFormer,1,12,"Glass Tier of Laser Hatch Limit in Magnetic Drive Pressure Former. Type: int");
        CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer = configuration.getInt("CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer",MagneticDomainConstructor,CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer,0,13,"The Coil Tier can enable perfect overclock in Extruder mode. 0 is Cupronickel Coil, 13 is Eternal Coil, default 11 is Infinity Coil. Type: int");
        // endregion

        // region Physical Form Switcher
        Mode_Default_PhysicalFormSwitcher = configuration.getBoolean("Mode_Default_PhysicalFormSwitcher",PhysicalFormSwitcher,Mode_Default_PhysicalFormSwitcher,"The default mode when deploy a machine. true=Fluid Extraction, false=Fluid Solidfication. Type: boolean");
        SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher",PhysicalFormSwitcher,String.valueOf(SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher),"The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Magnetic Mixer
        SpeedBonus_MultiplyPerTier_MagneticMixer = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MagneticMixer",MagneticMixer,String.valueOf(SpeedBonus_MultiplyPerTier_MagneticMixer),"The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Magnetic Domain Constructor
        Mode_Default_MagneticDomainConstructor = (byte) configuration.getInt("Mode_Default_MagneticDomainConstructor",MagneticDomainConstructor,Mode_Default_MagneticDomainConstructor,0,1,"The default mode when deploy a machine. 0=Electro Magnetic Separator, 1=Polarizer. Type: byte");
        SpeedBonus_MultiplyPerTier_MagneticDomainConstructor = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MagneticDomainConstructor",MagneticDomainConstructor,String.valueOf(SpeedBonus_MultiplyPerTier_MagneticDomainConstructor),"The speed bonus = this ^ tier . Type: float"));
        Parallel_PerRing_MagneticDomainConstructor = configuration.getInt("Parallel_PerRing_MagneticDomainConstructor",MagneticDomainConstructor,Parallel_PerRing_MagneticDomainConstructor,1,65536,"Parallel per Ring add. Type: int");
        // endregion

        // region Silksong
        SpeedBonus_MultiplyPerCoilTier_Silksong = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerCoilTier_Silksong",Silksong,String.valueOf(SpeedBonus_MultiplyPerCoilTier_Silksong),"The speed bonus = this ^ CoilTier . Type: float"));
        Parallel_PerPiece_Silksong = configuration.getInt("Parallel_PerPiece_Silksong",Silksong,Parallel_PerPiece_Silksong,1,65536,"Parallel per Piece add. Type: int");
        // endregion

        // region Holy Separator
        Mode_Default_HolySeparator = Byte.parseByte(configuration.getString("Mode_Default_HolySeparator", HolySeparator, String.valueOf(Mode_Default_HolySeparator), "The default mode when deploy a machine. 0=Cutter, 1=Slicer, 2=Lathe. Type: byte"));
        Piece_EnablePerfectOverclock_HolySeparator = configuration.getInt("Piece_EnablePerfectOverclock_HolySeparator", HolySeparator, Piece_EnablePerfectOverclock_HolySeparator, 1, 64, "How many piece to enable perfect overclock. Type: int");
        ParallelPerPiece_HolySeparator = configuration.getInt("ParallelPerPiece_HolySeparator", HolySeparator, ParallelPerPiece_HolySeparator, 1, 255, "How many parallel per tier add. Type: int");
        SpeedBonus_MultiplyPerTier_HolySeparator = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_HolySeparator", HolySeparator, String.valueOf(SpeedBonus_MultiplyPerTier_HolySeparator), "The speed bonus = this ^ tier . Type: float"));
        // endregion

        // region Space Scaler
        Mode_Default_SpaceScaler = (byte) configuration.getInt("Mode_Default_SpaceScaler",SpaceScaler,Mode_Default_SpaceScaler,0,1,"The default mode when deploy a machine. 0=Compressor, 1=Extractor. Can not set the default 2 - Cyclotron, machine will crash directly. Type: byte");
        Multiplier_ExtraOutputsPerFieldTier_SpaceScaler = configuration.getInt("Multiplier_ExtraOutputsPerFieldTier_SpaceScaler",SpaceScaler,Multiplier_ExtraOutputsPerFieldTier_SpaceScaler,1,64,"Extra outputs multiplier of Cyclotron mode, every higher tier of field block bring this value multiplied extra output items and fluids. Type: int");
        SpeedMultiplier_Tier1Block_SpaceScaler = configuration.getInt("SpeedMultiplier_Tier1Block_SpaceScaler",SpaceScaler,SpeedMultiplier_Tier1Block_SpaceScaler,1,64,"Speed Multiplier at Tier 1 field generator block. Type: int");
        SpeedMultiplier_BeyondTier2Block_SpaceScaler = configuration.getInt("SpeedMultiplier_BeyondTier2Block_SpaceScaler",SpaceScaler,SpeedMultiplier_BeyondTier2Block_SpaceScaler,1,64,"Speed Multiplier at beyond Tier 2 field generator block. Type: int");
        // endregion

        // region Crystalline Infinitier
        Mode_Default_CrystallineInfinitier = (byte) configuration.getInt("Mode_Default_CrystallineInfinitier",CrystallineInfinitier,Mode_Default_CrystallineInfinitier,0,1,"The default mode when deploy a machine. 0=Autoclave, 1=Crystalline Infinitier. Type: byte");
        SpeedMultiplier_AutoclaveMode_CrystallineInfinitier = configuration.getInt("SpeedMultiplier_AutoclaveMode_CrystallineInfinitier",CrystallineInfinitier,SpeedMultiplier_AutoclaveMode_CrystallineInfinitier,1,64,"Speed Multiplier of Crystalline Infinitier in Autoclave mode. Type: int");
        SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier = configuration.getInt("SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier",CrystallineInfinitier,SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier,1,64,"Speed Multiplier of Crystalline Infinitier in Crystalline Infinitier mode. Type: int");
        ParallelMultiplier_CrystallineInfinitier = configuration.getInt("ParallelMultiplier_CrystallineInfinitier",CrystallineInfinitier,ParallelMultiplier_CrystallineInfinitier,1,256,"Parallel Multiplier of Crystalline Infinitier. The final parallel will be multiplied this value. Type: int");
        FieldTier_EnablePerfectOverclock_CrystallineInfinitier = (byte) configuration.getInt("FieldTier_EnablePerfectOverclock_CrystallineInfinitier",CrystallineInfinitier,FieldTier_EnablePerfectOverclock_CrystallineInfinitier,1,11,"When field generator block tier is beyond this value, machine will enable perfect overclock. 3 is the lowest EOH block. Type: byte");
        // endregion

        // region Molecule Deconstructor
        Mode_Default_MoleculeDeconstructor = (byte) configuration.getInt("Mode_Default_MoleculeDeconstructor",MoleculeDeconstructor,Mode_Default_MoleculeDeconstructor,0,1,"The default mode when deploy a machine. 0=Electrolyzer, 1=Centrifuge. Type: byte");
        PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor = configuration.getInt("PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor",MoleculeDeconstructor,PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor,1,64,"How many piece can enable perfect overclock. Type: int");
        Parallel_PerPiece_MoleculeDeconstructor = configuration.getInt("Parallel_PerPiece_MoleculeDeconstructor",MoleculeDeconstructor,Parallel_PerPiece_MoleculeDeconstructor,1,65536,"Parallel per piece add. Type: int");
        SpeedBonus_MultiplyPerTier_MoleculeDeconstructor = Float.parseFloat(configuration.getString("SpeedBonus_MultiplyPerTier_MoleculeDeconstructor",MoleculeDeconstructor,String.valueOf(SpeedBonus_MultiplyPerTier_MoleculeDeconstructor),"The speed bonus = this ^ tier . Type: float"));
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
        EUPerCriticalPhoton = Long.parseLong(configuration.getString("EUPerCriticalPhoton", DSP, String.valueOf(EUPerCriticalPhoton), "EU per Critical Photon Cost. Type: long"));
        solarSailPowerPoint = Long.parseLong(configuration.getString("solarSailPowerPoint", DSP, String.valueOf(solarSailPowerPoint), "DSP Power Point per Solar Sail can produce. Type: long"));
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
        // endregion

        // region Space Station
        activateMegaSpaceStation = configuration.getBoolean("activateMegaSpaceStation",spaceStation,activateMegaSpaceStation,"decide whether can use mega space station.");
        // endregion

        // region CombatRework
        activateCombatStats = configuration.getBoolean("activateCombatStats",CombatStats,activateCombatStats,"decide whether to enable the combatstats system(WIP).");
        // endregion

        // region Hyper Spacetime Transformer
        Mode_Default_HyperSpacetimeTransformer=                                      (byte) configuration.getInt( "Mode_Default_HyperSpacetimeTransformer", HyperSpacetimeTransformer,Mode_Default_HyperSpacetimeTransformer, 0,1,"");;
        ParallelMultiplier_HyperSpacetimeTransformer=                                configuration.getInt(        "ParallelMultiplier_HyperSpacetimeTransformer",             HyperSpacetimeTransformer,ParallelMultiplier_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE,"");;
        SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer=          configuration.getInt(        "SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer",             HyperSpacetimeTransformer,SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE,"");;
        SpeedMultiplier_QuantumForceTransformerMode_HyperSpacetimeTransformer=       configuration.getInt(        "SpeedMultiplier_QuantumForceTransformerMode_HyperSpacetimeTransformer",             HyperSpacetimeTransformer,SpeedMultiplier_QuantumForceTransformerMode_HyperSpacetimeTransformer, 1, Integer.MAX_VALUE,"");;
        EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer=   configuration.getBoolean(    "EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer",    HyperSpacetimeTransformer,EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer,"");;
        // endregion

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
// spotless:on
