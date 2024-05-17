package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IDynamicModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IStaticModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ModularHatchBase;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;

public abstract class ModularizedMachineBase<T extends ModularizedMachineBase<T>> extends GTCM_MultiMachineBase<T>
    implements IModularizedMachine {

    // region Class Constructor
    public ModularizedMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ModularizedMachineBase(String aName) {
        super(aName);
    }

    // endregion

    // region Modular Logic
    // TODO 内存泄露
    protected Map<ModularHatchTypes, Collection<IModularHatch>> modularHatches = new HashMap<>();
    protected Map<ModularHatchTypes, Collection<IStaticModularHatch>> staticModularHatches = new HashMap<>();
    protected Map<ModularHatchTypes, Collection<IDynamicModularHatch>> dynamicModularHatches = new HashMap<>();
    protected Collection<IModularHatch> allModularHatches = new ArrayList<>();

    public Map<ModularHatchTypes, Collection<IModularHatch>> getModularHatchMap() {
        return modularHatches;
    }

    public Map<ModularHatchTypes, Collection<IStaticModularHatch>> getStaticModularHatchMap() {
        return staticModularHatches;
    }

    public Map<ModularHatchTypes, Collection<IDynamicModularHatch>> getDynamicModularHatchMap() {
        return dynamicModularHatches;
    }

    @Override
    public Collection<IModularHatch> getAllModularHatches() {
        return allModularHatches;
    }

    public void resetModularHatchCollections() {
        modularHatches.forEach((k, v) -> v.clear());
        staticModularHatches.forEach((k, v) -> v.clear());
        dynamicModularHatches.forEach((k, v) -> v.clear());
        allModularHatches.clear();
    }

    @Override
    public void applyModularStaticSettings() {
        for (Collection<IStaticModularHatch> c : getStaticModularHatchMap().values()) {
            for (IStaticModularHatch d : c) {
                d.onCheckMachine(this);
            }
        }
    }

    @Override
    public void applyModularDynamicParameters() {
        for (Collection<IDynamicModularHatch> c : getDynamicModularHatchMap().values()) {
            for (IDynamicModularHatch d : c) {
                d.onCheckProcessing(this);
            }
        }
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || addModularHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addModularHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addAnyModularHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addAnyModularHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof ModularHatchBase modularHatchBase) {
            ModularHatchTypes hatchTypes = modularHatchBase.getType();
            final Collection<ModularHatchTypes> supportedModularHatchTypes = getSupportedModularHatchTypes();
            if (!supportedModularHatchTypes.contains(ModularHatchTypes.ALL)
                && !supportedModularHatchTypes.contains(hatchTypes)) return false;

            modularHatchBase.updateTexture(aBaseCasingIndex);
            modularHatchBase.updateCraftingIcon(this.getMachineCraftingIcon());

            if (!modularHatches.containsKey(hatchTypes) || modularHatches.get(hatchTypes) == null) {
                modularHatches.put(hatchTypes, new ArrayList<>());
            }
            modularHatches.get(hatchTypes)
                .add(modularHatchBase);
            allModularHatches.add(modularHatchBase);

            if (modularHatchBase instanceof IDynamicModularHatch dynamicModularHatch) {
                if (!dynamicModularHatches.containsKey(hatchTypes) || dynamicModularHatches.get(hatchTypes) == null) {
                    dynamicModularHatches.put(hatchTypes, new ArrayList<>());
                }

                return dynamicModularHatches.get(hatchTypes)
                    .add(dynamicModularHatch);

            } else if (modularHatchBase instanceof IStaticModularHatch staticModularHatch) {
                if (!staticModularHatches.containsKey(hatchTypes) || staticModularHatches.get(hatchTypes) == null) {
                    staticModularHatches.put(hatchTypes, new ArrayList<>());
                }

                return staticModularHatches.get(hatchTypes)
                    .add(staticModularHatch);
            }
        }
        return false;
    }

    protected boolean addSpecialModularHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex,
        ModularHatchTypes hatchType) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof ModularHatchBase modularHatchBase) {
            if (modularHatchBase.getType() != hatchType) return false;
            if (!getSupportedModularHatchTypes().contains(hatchType)) return false;

            modularHatchBase.updateTexture(aBaseCasingIndex);
            modularHatchBase.updateCraftingIcon(this.getMachineCraftingIcon());

            if (!modularHatches.containsKey(hatchType) || modularHatches.get(hatchType) == null) {
                modularHatches.put(hatchType, new ArrayList<>());
            }
            modularHatches.get(hatchType)
                .add(modularHatchBase);
            allModularHatches.add(modularHatchBase);

            if (modularHatchBase instanceof IDynamicModularHatch dynamicModularHatch) {
                if (!dynamicModularHatches.containsKey(hatchType) || dynamicModularHatches.get(hatchType) == null) {
                    dynamicModularHatches.put(hatchType, new ArrayList<>());
                }

                return dynamicModularHatches.get(hatchType)
                    .add(dynamicModularHatch);

            } else if (modularHatchBase instanceof IStaticModularHatch staticModularHatch) {
                if (!staticModularHatches.containsKey(hatchType) || staticModularHatches.get(hatchType) == null) {
                    staticModularHatches.put(hatchType, new ArrayList<>());
                }

                return staticModularHatches.get(hatchType)
                    .add(staticModularHatch);
            }
        }
        return false;
    }

    public boolean addParallelControllerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addSpecialModularHatchToMachineList(
            aTileEntity,
            aBaseCasingIndex,
            ModularHatchTypes.PARALLEL_CONTROLLER);
    }

    public boolean addSpeedControllerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addSpecialModularHatchToMachineList(aTileEntity, aBaseCasingIndex, ModularHatchTypes.SPEED_CONTROLLER);
    }

    public boolean addPowerConsumptionControllerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return addSpecialModularHatchToMachineList(
            aTileEntity,
            aBaseCasingIndex,
            ModularHatchTypes.POWER_CONSUMPTION_CONTROLLER);
    }

    // endregion

    // region Processing Logic

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        checkModularDynamicParameters();
        return checkProcessingMM();
    }

    public CheckRecipeResult checkProcessingMM() {
        // If no logic is found, try legacy checkRecipe
        if (processingLogic == null) {
            return checkRecipe(mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        }

        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = processingLogic.getOutputItems();
        mOutputFluids = processingLogic.getOutputFluids();

        return result;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        resetModularHatchCollections();
        if (!checkMachineMM(aBaseMetaTileEntity, aStack)) return false;
        checkModularStaticSettings();
        return true;
    }

    public abstract boolean checkMachineMM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack);

    // endregion
}
