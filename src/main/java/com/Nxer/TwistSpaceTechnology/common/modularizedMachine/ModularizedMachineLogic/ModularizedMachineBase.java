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
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.DynamicParallelControllerBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.ParallelControllerBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers.StaticParallelControllerBase;

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
        return addParallelControllerToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addParallelControllerToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof ParallelControllerBase parallelController) {
            parallelController.updateTexture(aBaseCasingIndex);
            parallelController.updateCraftingIcon(this.getMachineCraftingIcon());

            if (!modularHatches.containsKey(ModularHatchTypes.PARALLEL_CONTROLLER)
                || modularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER) == null) {
                modularHatches.put(ModularHatchTypes.PARALLEL_CONTROLLER, new ArrayList<>());
            }
            modularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER)
                .add(parallelController);
            allModularHatches.add(parallelController);

            if (parallelController instanceof DynamicParallelControllerBase dynamicParallelController) {
                if (!dynamicModularHatches.containsKey(ModularHatchTypes.PARALLEL_CONTROLLER)
                    || dynamicModularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER) == null) {
                    dynamicModularHatches.put(ModularHatchTypes.PARALLEL_CONTROLLER, new ArrayList<>());
                }

                return dynamicModularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER)
                    .add(dynamicParallelController);

            } else if (parallelController instanceof StaticParallelControllerBase staticParallelControllerBase) {
                if (!staticModularHatches.containsKey(ModularHatchTypes.PARALLEL_CONTROLLER)
                    || staticModularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER) == null) {
                    staticModularHatches.put(ModularHatchTypes.PARALLEL_CONTROLLER, new ArrayList<>());
                }

                return staticModularHatches.get(ModularHatchTypes.PARALLEL_CONTROLLER)
                    .add(staticParallelControllerBase);
            }
        }
        return false;
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
