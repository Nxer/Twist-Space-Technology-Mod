package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.config.Config;

/**
 * ValueEnum are stored here.
 */
public final class ValueEnum {

    // region General and misc
    /**
     * The max parallel number.(now Deprecated)
     * <li>Default Integer.MAX_VALUE
     */
    @Deprecated
    public static final int MAX_PARALLEL_LIMIT = Config.MAX_PARALLEL_LIMIT; // default 8388608 // now is
                                                                            // Integer.MAX_VALUE

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

    // region Hyper Spacetime Transformer
    public static final byte Mode_Default_HyperSpacetimeTransformer = Config.Mode_Default_HyperSpacetimeTransformer;
    public static final int SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer = Config.SpeedMultiplier_MolecularTransformerMode_HyperSpacetimeTransformer;
    public static final int SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer = Config.SpeedMultiplier_SpaceTimeTransformerMode_HyperSpacetimeTransformer;
    public static final int ParallelMultiplier_HyperSpacetimeTransformer = Config.ParallelMultiplier_HyperSpacetimeTransformer;
    public static final boolean EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer = Config.EnablePerfectOverclock_MolecularTransformerMode_HyperSpacetimeTransformer;
    // endregion

    // region Miracle Door
    /**
     * Default ticks of Miracle Door per processing cost in mode ABS.
     * <li>Default 20 * 25.6
     */
    public static final int ticksOfMiracleDoorProcessingTimeABSMode = (int) (20
        * Config.secondsOfMiracleDoorProcessingTimeABSMode);
    public static final int ticksOfMiracleDoorProcessingTimeEBFMode = (int) (20
        * Config.secondsOfMiracleDoorProcessingTimeEBFMode);
    public static final int amountOfPhotonsEveryMiracleDoorProcessingCost = Config.amountOfPhotonsEveryMiracleDoorProcessingCost;
    public static final int multiplierOfMiracleDoorEUCostABSMode = Config.multiplierOfMiracleDoorEUCostABSMode;
    public static final int multiplierOfMiracleDoorEUCostEBFMode = Config.multiplierOfMiracleDoorEUCostEBFMode;

    // endregion

    // region Scavenger
    public static final boolean EnablePerfectOverclock_Scavenger = Config.EnablePerfectOverclock_Scavenger;
    public static final float EuModifier_Scavenger = Config.EuModifier_Scavenger;
    public static final double SpeedBonus_MultiplyPerTier_Scavenger = Config.SpeedBonus_MultiplyPerTier_Scavenger;
    // endregion

    // region Advanced Mega Oil Cracker
    public static final boolean EnablePerfectOverclock_AdvancedMegaOilCracker = Config.EnablePerfectOverclock_AdvancedMegaOilCracker;
    public static final float SpeedBonus_AdvancedMegaOilCracker = Config.SpeedBonus_AdvancedMegaOilCracker;
    public static final int Parallel_AdvancedMegaOilCracker = Config.Parallel_AdvancedMegaOilCracker;

    // endregion

    // region Indistinct Tentacle
    public static final byte Mode_Default_IndistinctTentacle = Config.Mode_Default_IndistinctTentacle;
    public static final int SpeedMultiplier_AssemblyLine_IndistinctTentacle = Config.SpeedMultiplier_AssemblyLine_IndistinctTentacle;
    public static final int SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle = Config.SpeedMultiplier_ComponentAssemblyLine_IndistinctTentacle;
    public static final int SpeedMultiplier_Assembler_IndistinctTentacle = Config.SpeedMultiplier_Assembler_IndistinctTentacle;
    public static final int SpeedMultiplier_PreciseAssembler_IndistinctTentacle = Config.SpeedMultiplier_PreciseAssembler_IndistinctTentacle;
    public static final int Parallel_Default_IndistinctTentacle = Config.Parallel_Default_IndistinctTentacle;
    public static final int TickEveryProcess_WirelessMode_IndistinctTentacle = Config.TickEveryProcess_WirelessMode_IndistinctTentacle;
    public static final int AstralArrayOverclockedTickEveryProcess_WirelessMode_IndistinctTentacle = Config.AstralArrayOverclockedTickEveryProcess_WirelessMode_IndistinctTentacle;
    public static final int ExtraEuCostMultiplierAstralArrayOverclocked_WirelessMode_IndistinctTentacle = Config.ExtraEuCostMultiplierAstralArrayOverclocked_WirelessMode_IndistinctTentacle;
    public static final byte ComponentCasingTierLimit_WirelessMode_IndistinctTentacle = Config.ComponentCasingTierLimit_WirelessMode_IndistinctTentacle;
    public static final byte GlassTierLimit_WirelessMode_IndistinctTentacle = Config.GlassTierLimit_WirelessMode_IndistinctTentacle;
    public static final byte GlassTierLimit_LaserHatch_IndistinctTentacle = Config.GlassTierLimit_LaserHatch_IndistinctTentacle;

    // endregion

    // region Mega Egg Generator

    public static final int MEG_Laser_Pieces = Config.MEG_Laser_Pieces;
    public static final int MEG_Dynamo_Limit = Config.MEG_Dynamo_Limit;
    public static final long MEG_CrepperEgg_Gen = Config.MEG_CrepperEgg_Gen;
    public static final long MEG_DragonEgg_Gen = Config.MEG_DragonEgg_Gen;
    public static final long MEG_InfinityEgg_Gen = Config.MEG_InfinityEgg_Gen;
    public static final int MEG_Efficiency_PiecesBuff = Config.MEG_Efficiency_PiecesBuff;
    public static final int MEG_Efficiency_InfinityEggBuff = Config.MEG_Efficiency_InfinityEggBuff;
    public static final int MEG_Efficiency_Lost = Config.MEG_Efficiency_Lost;
    public static final double MEG_Overall_Multiply = Config.MEG_Overall_Multiply;
    public static final boolean MEG_AllowRotation = Config.MEG_Rotation;
    // endregion

    // region Thermal Energy Devourer
    public static final byte Mode_Default_ThermalEnergyDevourer = Config.Mode_Default_ThermalEnergyDevourer;
    public static final int Parallel_HighSpeedMode_ThermalEnergyDevourer = Config.Parallel_HighSpeedMode_ThermalEnergyDevourer;
    public static final int Parallel_HighParallelMode_ThermalEnergyDevourer = Config.Parallel_HighParallelMode_ThermalEnergyDevourer;
    public static final int TickPerProgressing_WirelessMode_ThermalEnergyDevourer = Config.TickPerProgressing_WirelessMode_ThermalEnergyDevourer;

    // endregion

    // region Vacuum Filter Extractor
    public static final byte Mode_Default_VacuumFilterExtractor = Config.Mode_Default_VacuumFilterExtractor;
    public static final float EuModifier_VacuumFilterExtractor = Config.EuModifier_VacuumFilterExtractor;
    // endregion

    // region Bee Engineer
    public static final double BE_pChance = Config.BE_pChance;
    public static final double BE_pChanceEnhanced = Config.BE_pChanceEnhanced;
    public static final int BE_pHoneyCost = Config.BE_pHoneyCost;
    public static final int BE_pUUMCost = Config.BE_pUUMCost;
    public static final int BE_pEachProcessTime = Config.BE_pEachProcessTime;
    // endregion

    // region Mega Macerator

    public static final int BlockTier1Parallel_MegaMacerator = Config.BlockTier1Parallel_MegaMacerator;
    public static final int BlockTier2Parallel_MegaMacerator = Config.BlockTier2Parallel_MegaMacerator;
    public static final float SpeedBonus_MegaMacerator = Config.SpeedBonus_MegaMacerator;
    public static final boolean EnablePerfectOverclock_MegaMacerator = Config.EnablePerfectOverclock_MegaMacerator;
    // endregion

    // region Infinite Air Hatch
    public static final int ticksOfInfiniteAirHatchFillFull = (int) (20 * Config.secondsOfInfiniteAirHatchFillFull);
    // endregion

    // region Hephaestus' Atelier
    public static final int ConsumeEutPerParallel_HephaestusAtelier = Config.ConsumeEutPerParallel_HephaestusAtelier;
    public static final int ConsumeDuration_HephaestusAtelier = Config.ConsumeDuration_HephaestusAtelier;
    public static final int ConsumeEuPerSmelting_HephaestusAtelier = Config.ConsumeEuPerSmelting_HephaestusAtelier;
    public static final int DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier = Config.DurationPerProcessing_T2Coil_Wireless_HephaestusAtelier;
    public static final int DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier = Config.DurationPerProcessing_T3Coil_Wireless_HephaestusAtelier;
    // endregion

}
