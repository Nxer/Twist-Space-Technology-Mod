package com.Nxer.TwistSpaceTechnology.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;

public enum GTCMItemList {

    // Items
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

    spaceStationConstructingMaterialMax,

    MultiStructuresLinkTool,

    // Machines

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

    // MAX
    HighDimensionalExtend,
    HighDimensionalCircuitDoard,
    HighDimensionalCapacitor,
    HighDimensionalInterface,
    HighDimensionalDiode,
    HighDimensionalResistor,
    HighDimensionalTransistor,

    // DSP
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
    DSPLauncher,
    DSPReceiver,
    ArtificialStar,
    StellarMaterialSiphon,

    // single block Machines
    InfiniteAirHatch,
    InfiniteWirelessDynamoHatch,

    // Blocks

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
    megaUniversalSpaceStation;

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
            GT_Log.out.println("Object in the ItemList is null at:");
            new NullPointerException().printStackTrace(GT_Log.out);
            return Utils.copyAmount(aAmount, TestItem0.get(1));
        }
        return Utils.copyAmount(aAmount, GT_OreDictUnificator.get(mStack));
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
            new Exception(this + " is now deprecated").printStackTrace(GT_Log.err);
            // warn only once
            mWarned = true;
        }
    }
}
