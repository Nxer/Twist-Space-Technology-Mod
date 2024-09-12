package com.Nxer.TwistSpaceTechnology.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.util.GTLog;

public enum GTCMItemList {

    // region Items
    TestItem0,
    SpaceWarper,
    OpticalSOC,
    ProofOfHeroes,
    MoldSingularity,
    ParticleTrapTimeSpaceShield,
    LapotronShard,
    PerfectLapotronCrystal,
    EnergyCrystalShard,
    PerfectEnergyCrystal,
    CosmicCircuitBoard,
    IntelligentImitationNeutronStarCore,
    EventHorizonNanoSwarm,
    MicroDimensionOutput,
    EntropyReductionMaterialNanoswarm,
    TranscendentCircuitBoard,
    NarrativeLayerOverwritingDevice,
    HyperspaceNarrativeLayerAdaptiveSpecialSRA,
    RealSingularityNanoSwarm,
    ParadoxEngine,
    QuasarSoc,
    MiniatureGalaxy,
    Self_adaptiveAI1,
    Self_adaptiveAI2,
    Self_adaptiveAI3,
    Self_adaptiveAI4,
    Self_adaptiveAI5,
    CoreOfT800,
    ExoticCircuitBoard,
    Yamato,

    LightQuantumMatrix,
    StarCore,
    CasimirQuantumFiber,
    SuperstringStructure,
    DynamicParadoxBody,
    VoidPrism,
    PulsarCore,
    StarCrystalI,
    SuperDimensionalRing,
    HyperdimensionalExpansion,
    OpticalLayer,
    MagneticSpinI,
    Staraxis,
    BoltzmannBrain,
    RemnantsOfTheBigBang,
    StrangeFilm,
    PulseManganese,
    SuperdimensionalWeb,
    PinoanStructure,
    QuantumChain,
    StarBelt,
    Nanoflow,
    Space_TimeLayer,
    SuperconductingRing,
    QuantizedSuperstringStructure,
    ThezeroPointOfVacuumCanManifestObjects,
    QuasarRemnant,
    InfiniteDivineMachine,
    OriginalSoup,
    GravityBelt,
    anti_GravityEngine,
    CondensedDarkMatterPolymer,
    LowDensityDarkMatterPolymer,
    InfiniteRecursion,
    SuperdimensionalSpiral,
    InfiniteDivineMachineI,
    NuclearaxisFluctuation,
    StrangeFluctuations,
    PulseCopper,
    DarkMatterCrystallization,
    QuantumCore,
    PhotonFlow,
    NuclearBelt,
    LifeGuide,
    QuantizedSuperstringStructureI,
    EmptyHeart,
    StarCoreBelt,
    Space_TimeSpiral,
    MagneticSpinIV,
    NuclearFluctuation,
    CelestialResonanceCrystal,
    LowDensityDarkMatterPolymerI,

    SuperdimensionalLife,
    GravityFluctuation,
    LightSpiral,
    NuclearaxisBelt,
    SuperconductingNetwork,
    Nanolife,
    CoreOfAncientCreation,
    QuantumHeart,
    FluctuatingLife,
    PioneerRemains,
    LifeIsEmpty,
    SuperstringStructureV,
    SuperdimensionalFluctuations,
    CreationsFromTheOuterUniverse,
    MagneticVortex,
    Space_TimeCore,
    SubspaceHeart,
    CosmicExpansionEffectFluctuations,
    StarCrystalIV,
    InfiniteRecursiveNet,
    SuperconductingLifeWaves,
    GravitationalHeart,
    CelestialResonanceCrystalSpiral,
    NuclearaxisCore,
    VoidFluctuation,
    AncientCreationFluctuation,
    InfiniteRecursiveHeart,
    SpiralSpiral,
    MagneticSpinLife,
    LightWaves,
    NuclearSpiral,
    CosmicExpansionEffectCore,
    GravityLife,
    CelestialResonanceCrystalNetwork,
    LifeInTimeandSpace,
    CoreAxisLifeHeart,
    AlienStarCore,
    NanoLifeHeart,
    AncientCreationLife,
    InfiniteRecursiveKernel,
    SuperconductingLifeZone,
    GravityLifeFluctuation,
    PioneerRemainsLifeCore,
    SubspaceFluctuation,
    Space_TimeLifeCore,

    spaceStationConstructingMaterialMax,

    MultiStructuresLinkTool,

    PurpleMagnoliaPetal,
    PurpleMagnoliaSapling,
    VoidPollen,

    Rune_of_Vigilance,
    Rune_of_Erelong,
    Rune_of_Ether,
    Rune_of_Perdition,
    PowerChair,
    PrimitiveMansSpaceTimeDistortionDevice,
    BallLightningUpgradeChip,
    HatchUpdateTool,
    WirelessUpdateItem,
    FountOfEcology,
    OffSpring,

    // endregion

    // region Blocks
    TestCasing,

    TestMetaBlock01_0,
    PhotonControllerUpgradeLV,
    PhotonControllerUpgradeMV,
    PhotonControllerUpgradeHV,
    PhotonControllerUpgradeEV,
    PhotonControllerUpgradeIV,
    PhotonControllerUpgradeLuV,
    PhotonControllerUpgradeZPM,
    PhotonControllerUpgradeUV,
    PhotonControllerUpgradeUHV,
    PhotonControllerUpgradeUEV,
    PhotonControllerUpgradeUIV,
    PhotonControllerUpgradeUMV,
    PhotonControllerUpgradeUXV,
    PhotonControllerUpgradeMAX,
    HighPowerRadiationProofCasing,
    AdvancedHighPowerCoilBlock,
    ParallelismCasing0,
    ParallelismCasing1,
    ParallelismCasing2,
    ParallelismCasing3,
    ParallelismCasing4,
    AntiMagneticCasing,
    ReinforcedStoneBrickCasing,
    CompositeFarmCasing,
    DenseCyclotronOuterCasing,
    CompactCyclotronCoil,
    AsepticGreenhouseCasing,
    // endregion

    // region Machines

    IntensifyChemicalDistorter,
    PreciseHighEnergyPhotonicQuantumMaster,
    MiracleTop,
    MagneticDrivePressureFormer,
    PhysicalFormSwitcher,
    MagneticMixer,
    MagneticDomainConstructor,
    Silksong,
    HolySeparator,
    SpaceScaler,
    MoleculeDeconstructor,
    CrystallineInfinitier,
    MiracleDoor,
    OreProcessingFactory,
    CircuitConverter,
    MegaBrickedBlastFurnace,
    LargeIndustrialCokingFactory,
    Scavenger,
    MegaEggGenerator,
    AstralComputingArray,
    IndustrialMagicMatrix,
    StellarMaterialSiphon,
    ElvenWorkshop,
    HyperSpacetimeTransformer,
    superCleanRoom,
    BiosphereIII,
    AdvancedMegaOilCracker,
    IndistinctTentacle,
    ThermalEnergyDevourer,
    VacuumFilterExtractor,
    LargeSteamForgeHammer,
    LargeSteamAlloySmelter,
    EyeOfWood,
    BeeEngineer,
    MegaMacerator,
    HephaestusAtelier,
    DeployedNanoCore,
    CoreDeviceOfHumanPowerGenerationFacility,
    BallLightning,
    StarcoreMiner,
    Disassembler,
    SpaceApiaryT1,
    SpaceApiaryT2,
    SpaceApiaryT3,
    SpaceApiaryT4,
    LargeCanner,
    BigBroArray,
    IndustrialMagnetarSeparator,
    MegaTreeFarm,
    LightningSpire,
    ExtremeCraftCenter,
    MassFabricatorGenesis,
    IncompactCyclotron,

    // endregion

    // region Modularized Machines
    DimensionallyTranscendentMatterPlasmaForgePrototypeMK2,
    LargeNeutronOscillator,
    IndistinctTentaclePrototypeMK2,

    // endregion

    // region Modularized Machine System Stuffs

    // dynamic parallel controllers
    DynamicParallelControllerT1,
    DynamicParallelControllerT2,
    DynamicParallelControllerT3,
    DynamicParallelControllerT4,
    DynamicParallelControllerT5,
    DynamicParallelControllerT6,
    DynamicParallelControllerT7,
    DynamicParallelControllerT8,

    // static dynamic parallel controllers
    StaticParallelControllerT1,
    StaticParallelControllerT2,
    StaticParallelControllerT3,
    StaticParallelControllerT4,
    StaticParallelControllerT5,
    StaticParallelControllerT6,
    StaticParallelControllerT7,
    StaticParallelControllerT8,

    // dynamic speed controllers
    DynamicSpeedControllerT1,
    DynamicSpeedControllerT2,
    DynamicSpeedControllerT3,
    DynamicSpeedControllerT4,
    DynamicSpeedControllerT5,
    DynamicSpeedControllerT6,
    DynamicSpeedControllerT7,
    DynamicSpeedControllerT8,

    // static speed controllers
    StaticSpeedControllerT1,
    StaticSpeedControllerT2,
    StaticSpeedControllerT3,
    StaticSpeedControllerT4,
    StaticSpeedControllerT5,
    StaticSpeedControllerT6,
    StaticSpeedControllerT7,
    StaticSpeedControllerT8,

    // static power consumption controllers
    StaticPowerConsumptionControllerT1,
    StaticPowerConsumptionControllerT2,
    StaticPowerConsumptionControllerT3,
    StaticPowerConsumptionControllerT4,
    StaticPowerConsumptionControllerT5,
    StaticPowerConsumptionControllerT6,
    StaticPowerConsumptionControllerT7,
    StaticPowerConsumptionControllerT8,

    // overclock controller
    LowSpeedPerfectOverclockController,
    PerfectOverclockController,
    SingularityPerfectOverclockController,

    // execution core
    ExecutionCore,
    AdvancedExecutionCore,
    PerfectExecutionCore,

    // endregion

    // region MAX
    HighDimensionalExtend,
    // TODO: typo
    HighDimensionalCircuitDoard,
    HighDimensionalCapacitor,
    HighDimensionalInterface,
    HighDimensionalDiode,
    HighDimensionalResistor,
    HighDimensionalTransistor,
    // endregion

    // region Cosmic Processor Productions
    SpaceTimeSuperconductingInlaidMotherboard,
    PacketInformationTranslationArray,
    InformationHorizonInterventionShell,
    EnergyFluctuationSelfHarmonizer,
    EncapsulatedMicroSpaceTimeUnit,
    SeedsSpaceTime,
    MicroSpaceTimeFabricatorio,

    // endregion

    // region DSP
    SolarSail,
    DysonSphereFrameComponent,
    SmallLaunchVehicle,
    EmptySmallLaunchVehicle,
    CriticalPhoton,
    Antimatter,
    AnnihilationConstrainer,
    AntimatterFuelRod,
    StellarConstructionFrameMaterial,
    GravitationalLens,
    EnergyShard,
    SiliconBasedNeuron,
    MatterRecombinator,
    CoreElement,
    StrangeAnnihilationFuelRod,
    DSPLauncher,
    DSPReceiver,
    ArtificialStar,
    StrangeMatterAggregator,
    SpaceTimeOscillatorT1,
    SpaceTimeOscillatorT2,
    SpaceTimeOscillatorT3,
    SpaceTimeConstraintorT1,
    SpaceTimeConstraintorT2,
    SpaceTimeConstraintorT3,
    SpaceTimeMergerT1,
    SpaceTimeMergerT2,
    SpaceTimeMergerT3,

    // endregion

    // region single block Machines
    InfiniteAirHatch,
    ManaHatch,
    InfiniteWirelessDynamoHatch,
    DualInputBuffer_IV,
    DualInputBuffer_LuV,
    DualInputBuffer_ZPM,
    DualInputBuffer_UV,
    BufferedEnergyHatchLV,
    BufferedEnergyHatchMV,
    BufferedEnergyHatchHV,
    BufferedEnergyHatchEV,
    BufferedEnergyHatchIV,
    BufferedEnergyHatchLuV,
    BufferedEnergyHatchZPM,
    BufferedEnergyHatchUV,
    BufferedEnergyHatchUHV,
    BufferedEnergyHatchUEV,
    BufferedEnergyHatchUIV,
    BufferedEnergyHatchUMV,
    BufferedEnergyHatchUXV,
    BufferedEnergyHatchMAX,
    DebugUncertaintyHatch,
    LaserSmartNode,
    FackRackHatch,
    RealRackHatch,

    WirelessDataInputHatch,
    WirelessDataOutputHatch,

    LegendaryWirelessEnergyHatch,
    HarmoniousWirelessEnergyHatch,

    // endregion

    // region super space station miscs

    spaceStationStructureBlockLV,
    spaceStationStructureBlockMV,
    spaceStationStructureBlockHV,
    spaceStationStructureBlockEV,
    spaceStationStructureBlockIV,
    spaceStationStructureBlockLuV,
    spaceStationStructureBlockZPM,
    spaceStationStructureBlockUV,
    spaceStationStructureBlockUHV,
    spaceStationStructureBlockUEV,
    spaceStationStructureBlockUIV,
    spaceStationStructureBlockUMV,
    spaceStationStructureBlockUXV,
    spaceStationStructureBlockMAX,
    SpaceStationAntiGravityBlockLV,
    SpaceStationAntiGravityBlockMV,
    SpaceStationAntiGravityBlockHV,
    SpaceStationAntiGravityBlockEV,
    SpaceStationAntiGravityBlockIV,
    SpaceStationAntiGravityBlockLuV,
    SpaceStationAntiGravityBlockZPM,
    SpaceStationAntiGravityBlockUV,
    SpaceStationAntiGravityBlockUHV,
    SpaceStationAntiGravityBlockUEV,
    SpaceStationAntiGravityBlockUIV,
    SpaceStationAntiGravityBlockUMV,
    SpaceStationAntiGravityBlockUXV,
    SpaceStationAntiGravityBlockMAX,

    // MultiStructure Machine:
    TestMultiStructureMainMachine,
    TestMultiStructureSubMachine,
    megaUniversalSpaceStation,

    // endregion

    // region nuclear reactor
    NuclearReactor,
    NuclearReactorStructure0,
    NuclearReactorStructure1,
    NuclearReactorStructure2,
    NuclearReactorStructure3;

    // endregion

    // region Member Variables

    private boolean mHasNotBeenSet;
    private boolean mDeprecated;
    private boolean mWarned;

    private ItemStack mStack;

    // endregion

    GTCMItemList() {
        mHasNotBeenSet = true;
    }

    GTCMItemList(boolean aDeprecated) {
        if (aDeprecated) {
            mDeprecated = true;
            mHasNotBeenSet = true;
        }
    }

    public Item getItem() {
        sanityCheck();
        if (Utils.isStackInvalid(mStack)) return null;// TODO replace a default issue item
        return mStack.getItem();
    }

    public Block getBlock() {
        sanityCheck();
        return Block.getBlockFromItem(getItem());
    }

    public ItemStack get(int aAmount, Object... aReplacements) {
        sanityCheck();
        // if invalid, return a replacements
        if (Utils.isStackInvalid(mStack)) {
            GTLog.out.println("Object in the ItemList is null at:");
            new NullPointerException().printStackTrace(GTLog.out);
            return Utils.copyAmount(aAmount, TestItem0.get(1));
        }
        return Utils.copyAmount(aAmount, mStack);
    }

    public int getMeta() {
        return mStack.getItemDamage();
    }

    public GTCMItemList set(Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = Utils.copyAmount(1, aStack);
        return this;
    }

    public GTCMItemList set(ItemStack aStack) {
        if (aStack != null) {
            mHasNotBeenSet = false;
            mStack = Utils.copyAmount(1, aStack);
        }
        return this;
    }

    public boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    /**
     * Returns the internal stack. This method is unsafe. It's here only for quick operations. DON'T CHANGE THE RETURNED
     * VALUE!
     */
    public ItemStack getInternalStack_unsafe() {
        return mStack;
    }

    private void sanityCheck() {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        if (mDeprecated && !mWarned) {
            new Exception(this + " is now deprecated").printStackTrace(GTLog.err);
            // warn only once
            mWarned = true;
        }
    }
}
