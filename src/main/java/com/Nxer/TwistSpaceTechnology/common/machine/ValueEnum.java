package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.config.Config;

/**
 * ValueEnum are stored here.
 */
public final class ValueEnum {

    // region General and misc
    /**
     * The max parallel number.(now Deprecated)
     * <li>Default 8388608
     */
    @Deprecated
    public static final int MAX_PARALLEL_LIMIT = Config.MAX_PARALLEL_LIMIT; // default 8388608

    /**
     * Casing index of space elevator bas casing.
     */
    public static final int SPACE_ELEVATOR_BASE_CASING_INDEX = 4096;

    // endregion

    // region Intensify Chemical Distorter
    /**
     * The default mode when deploy a machine.
     * <li>Default 0
     */
    public static final int Mode_Default_IntensifyChemicalDistorter = Config.Mode_Default_IntensifyChemicalDistorter < 0
        || Config.Mode_Default_IntensifyChemicalDistorter > 1 ? 0 : Config.Mode_Default_IntensifyChemicalDistorter;
    public static final int Parallel_LCRMode_IntensifyChemicalDistorter = Config.Parallel_LCRMode_IntensifyChemicalDistorter;
    public static final int Parallel_ICDMode_IntensifyChemicalDistorter = Config.Parallel_ICDMode_IntensifyChemicalDistorter;
    public static final int SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter = Config.SpeedUpMultiplier_LCRMode_IntensifyChemicalDistorter;
    public static final int SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter = Config.SpeedUpMultiplier_ICDMode_IntensifyChemicalDistorter;

    // endregion

    // region Precise High Energy Photonic Quantum Master
    public static final boolean Mode_Default_PreciseHighEnergyPhotonicQuantumMaster = Config.Mode_Default_PreciseHighEnergyPhotonicQuantumMaster;
    public static final int SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = Config.SpeedUpMultiplier_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster;
    public static final int SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = Config.SpeedUpMultiplier_PhCMode_PreciseHighEnergyPhotonicQuantumMaster;
    public static final int Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster = Config.Parallel_LaserEngraverMode_PreciseHighEnergyPhotonicQuantumMaster;
    public static final int Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster = Config.Parallel_PhCMode_PreciseHighEnergyPhotonicQuantumMaster;
    // endregion

    // region Miracle Top
    public static final byte Mode_Default_MiracleTop = Config.Mode_Default_MiracleTop;
    public static final int SpeedUpMultiplier_PerRing_MiracleTop = Config.SpeedUpMultiplier_PerRing_MiracleTop;
    public static final int Parallel_PerRing_MiracleTop = Config.Parallel_PerRing_MiracleTop;
    public static final int RingsAmount_EnablePerfectOverclock_MiracleTop = Config.RingsAmount_EnablePerfectOverclock_MiracleTop;
    // endregion

    // region Magnetic Drive Pressure Former
    public static final byte Mode_Default_MagneticDrivePressureFormer = Config.Mode_Default_MagneticDrivePressureFormer;
    public static final int SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer = Config.SpeedUpMultiplier_ExtruderMode_MagneticDrivePressureFormer;
    public static final int SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer = Config.SpeedUpMultiplier_OtherMode_MagneticDrivePressureFormer;
    public static final int SpeedUpMultiplier_Coil_MagneticDrivePressureFormer = Config.SpeedUpMultiplier_Coil_MagneticDrivePressureFormer;
    public static final int Parallel_MagneticDrivePressureFormer = Config.Parallel_MagneticDrivePressureFormer;
    public static final float EU_Multiplier_MagneticDrivePressureFormer = Config.EU_Multiplier_MagneticDrivePressureFormer;
    public static final int GlassTier_LimitLaserHatch_MagneticDrivePressureFormer = Config.GlassTier_LimitLaserHatch_MagneticDrivePressureFormer
        > 0 && Config.GlassTier_LimitLaserHatch_MagneticDrivePressureFormer <= 12
            ? Config.GlassTier_LimitLaserHatch_MagneticDrivePressureFormer
            : 11;
    public static final int CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer = Config.CoilTier_EnablePerfectOverclockExtruderMode_MagneticDrivePressureFormer;

    // endregion

    // region Physical Form Switcher
    public static final boolean Mode_Default_PhysicalFormSwitcher = Config.Mode_Default_PhysicalFormSwitcher;
    public static final float SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher = Config.SpeedBonus_MultiplyPerTier_PhysicalFormSwitcher;
    // endregion

    // region Magnetic Mixer
    public static final float SpeedBonus_MultiplyPerTier_MagneticMixer = Config.SpeedBonus_MultiplyPerTier_MagneticMixer;
    // endregion

    // region Magnetic Domain Constructor
    public static final byte Mode_Default_MagneticDomainConstructor = Config.Mode_Default_MagneticDomainConstructor;
    public static final float SpeedBonus_MultiplyPerTier_MagneticDomainConstructor = Config.SpeedBonus_MultiplyPerTier_MagneticDomainConstructor;
    public static final int Parallel_PerRing_MagneticDomainConstructor = Config.Parallel_PerRing_MagneticDomainConstructor;
    // endregion

    // region Silksong
    public static final float SpeedBonus_MultiplyPerCoilTier_Silksong = Config.SpeedBonus_MultiplyPerCoilTier_Silksong;
    public static final int Parallel_PerPiece_Silksong = Config.Parallel_PerPiece_Silksong;
    // endregion

    // region Holy Separator
    /**
     * The default mode when deploy a machine.
     * <li>Default 0
     */
    public static final byte Mode_Default_HolySeparator = Config.Mode_Default_HolySeparator <= 2
        && Config.Mode_Default_HolySeparator >= 0 ? Config.Mode_Default_HolySeparator : 0;

    /**
     * How many piece to enable perfect overclock.
     * <li>Default 16
     */
    public static final int Piece_EnablePerfectOverclock_HolySeparator = Config.Piece_EnablePerfectOverclock_HolySeparator;

    /**
     * How many parallel per tier add.
     * <li>Default 8
     */
    public static final int ParallelPerPiece_HolySeparator = Config.ParallelPerPiece_HolySeparator;

    /**
     * The speed bonus = this ^ tier .
     * <li>Default 0.9
     */
    public static final float SpeedBonus_MultiplyPerTier_HolySeparator = Config.SpeedBonus_MultiplyPerTier_HolySeparator;

    // endregion

    // region Space Scaler
    public static final byte Mode_Default_SpaceScaler = Config.Mode_Default_SpaceScaler;
    public static final int Multiplier_ExtraOutputsPerFieldTier_SpaceScaler = Config.Multiplier_ExtraOutputsPerFieldTier_SpaceScaler;
    public static final int SpeedMultiplier_Tier1Block_SpaceScaler = Config.SpeedMultiplier_Tier1Block_SpaceScaler;
    public static final int SpeedMultiplier_BeyondTier2Block_SpaceScaler = Config.SpeedMultiplier_BeyondTier2Block_SpaceScaler;
    // endregion

    // region Molecule Deconstructor
    public static final byte Mode_Default_MoleculeDeconstructor = Config.Mode_Default_MoleculeDeconstructor;
    public static final int PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor = Config.PieceAmount_EnablePerfectOverclock_MoleculeDeconstructor;
    public static final int Parallel_PerPiece_MoleculeDeconstructor = Config.Parallel_PerPiece_MoleculeDeconstructor;
    public static final float SpeedBonus_MultiplyPerTier_MoleculeDeconstructor = Config.SpeedBonus_MultiplyPerTier_MoleculeDeconstructor;
    // endregion

    // region Crystalline Infinitier
    public static final byte Mode_Default_CrystallineInfinitier = Config.Mode_Default_CrystallineInfinitier;
    public static final int SpeedMultiplier_AutoclaveMode_CrystallineInfinitier = Config.SpeedMultiplier_AutoclaveMode_CrystallineInfinitier;
    public static final int SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier = Config.SpeedMultiplier_CrystallineInfinitierMode_CrystallineInfinitier;
    public static final int ParallelMultiplier_CrystallineInfinitier = Config.ParallelMultiplier_CrystallineInfinitier;
    public static final byte FieldTier_EnablePerfectOverclock_CrystallineInfinitier = Config.FieldTier_EnablePerfectOverclock_CrystallineInfinitier;
    // endregion

    // region Miracle Door
    /**
     * Default ticks of Miracle Door per processing cost.
     * <li>Default 20 * 25.6
     */
    public static final int ticksOfMiracleDoorProcessingTime = (int) (20 * Config.secondsOfMiracleDoorProcessingTime);
    public static final int amountOfPhotonsEveryMiracleDoorProcessingCost = Config.amountOfPhotonsEveryMiracleDoorProcessingCost;
    public static final int multiplierOfMiracleDoorEUCost = Config.multiplierOfMiracleDoorEUCost;

    // endregion
}
