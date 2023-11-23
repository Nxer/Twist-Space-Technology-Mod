package com.Nxer.TwistSpaceTechnology.common;

import static gregtech.api.enums.GT_Values.W;

import com.Nxer.TwistSpaceTechnology.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

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

    // single block Machines
    InfiniteAirHatch,

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
    PhotonControllerUpgradeMAX

    ;

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
