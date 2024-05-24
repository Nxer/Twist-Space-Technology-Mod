package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import static com.Nxer.TwistSpaceTechnology.util.Utils.filterValidMTE;
import static com.Nxer.TwistSpaceTechnology.util.Utils.mergeArray;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.misc.CheckRecipeResults.CheckRecipeResults;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.AdvExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.ExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.IExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.util.NBTUtils;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;

public abstract class MultiExecutionCoreMachineBase<T extends MultiExecutionCoreMachineBase<T>>
    extends ModularizedMachineBase<T> implements IModularizedMachine.ISupportExecutionCore, IExecutionCore {

    public MultiExecutionCoreMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MultiExecutionCoreMachineBase(String aName) {
        super(aName);
    }

    // region Modular and preparing
    protected final Collection<AdvExecutionCore> advExecutionCores = new ArrayList<>();
    protected final Collection<ExecutionCore> executionCores = new ArrayList<>();
    protected final Collection<IRecipeProcessingAwareHatch> MEInputHatches = new ArrayList<>();

    @Override
    public void resetModularHatchCollections() {
        super.resetModularHatchCollections();
        advExecutionCores.clear();
        executionCores.clear();
        MEInputHatches.clear();
    }

    @Override
    protected void startRecipeProcessing() {
        startedRecipeProcessing = true;
        if (MEInputHatches.isEmpty()) return;
        for (IRecipeProcessingAwareHatch hatch : filterValidMTE(MEInputHatches)) {
            hatch.startRecipeProcessing();
        }
    }

    @Override
    protected void endRecipeProcessing() {
        startedRecipeProcessing = false;
        if (MEInputHatches.isEmpty()) return;
        for (IRecipeProcessingAwareHatch hatch : filterValidMTE(MEInputHatches)) {
            setResultIfFailure(hatch.endRecipeProcessing(this));
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;

        maxEutCanUse = (long) (0.975d * getMaxInputEu());

        // collect ME input hatches to easier flush
        for (GT_MetaTileEntity_Hatch_InputBus hatch : filterValidMTEs(mInputBusses)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                MEInputHatches.add(aware);
            }
        }
        for (GT_MetaTileEntity_Hatch_Input hatch : filterValidMTEs(mInputHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                MEInputHatches.add(aware);
            }
        }

        // collect execution core module hatches and setup
        Collection<IModularHatch> allExecutionCores = modularHatches.get(ModularHatchTypes.EXECUTION_CORE);
        if (allExecutionCores != null && !allExecutionCores.isEmpty()) {
            for (IModularHatch hatch : allExecutionCores) {
                if (hatch == null) continue;
                if (hatch instanceof AdvExecutionCore advExecutionCore) {
                    advExecutionCores.add(advExecutionCore);
                    advExecutionCore.setup(this);
                } else if (hatch instanceof ExecutionCore executionCore) {
                    executionCores.add(executionCore);
                    executionCore.setup(this);
                }
            }
        }

        return true;
    }

    @Override
    public Collection<IExecutionCore> getIdleNormalExecutionCores() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        for (ExecutionCore executionCore : executionCores) {
            if (executionCore.isIdle()) {
                cores.add(executionCore);
            }
        }
        return cores;
    }

    @Override
    public int getParallelOfEveryNormalExecutionCore() {
        if (executionCores.isEmpty()) return getMaxParallelRecipes();
        int coreAmount = executionCores.size() + 1;
        int totalParallel = getMaxParallelRecipes();
        if (coreAmount >= totalParallel) return 1;
        return 1 + totalParallel / coreAmount;
    }

    @Override
    public IModularizedMachine.ISupportExecutionCore getMainMachine() {
        return this;
    }

    // endregion

    // region Of an Execution core

    protected ItemStack[] eOutputItems;
    protected FluidStack[] eOutputFluids;
    protected int eMaxProgressingTime;
    protected int eProgressedTime;
    protected long eEut;

    protected void saveNBTDataItemStacks(NBTTagCompound aNBT) {
        if (eOutputItems != null && eOutputItems.length > 0) {
            aNBT.setInteger("eOutputItemsLength", eOutputItems.length);
            for (int i = 0; i < eOutputItems.length; i++) {
                NBTUtils.saveItem(aNBT, "eOutputItems" + i, eOutputItems[i]);
            }
        }
    }

    protected void saveNBTDataFluidStacks(NBTTagCompound aNBT) {
        if (eOutputFluids != null && eOutputFluids.length > 0) {
            aNBT.setInteger("eOutputFluidsLength", eOutputFluids.length);
            for (int i = 0; i < eOutputFluids.length; i++) {
                NBTUtils.saveFluid(aNBT, "eOutputFluids" + i, eOutputFluids[i]);
            }
        }
    }

    protected void loadNBTDataItemStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("eOutputItemsLength");
        if (length > 0) {
            eOutputItems = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                eOutputItems[i] = NBTUtils.loadItem(aNBT, "eOutputItems" + i);
            }
        }
    }

    protected void loadNBTDataFluidStacks(NBTTagCompound aNBT) {
        int length = aNBT.getInteger("eOutputFluidsLength");
        if (length > 0) {
            eOutputFluids = new FluidStack[length];
            for (int i = 0; i < length; i++) {
                eOutputFluids[i] = NBTUtils.loadFluid(aNBT, "eOutputFluids" + i);
            }
        }
    }

    @Override
    public boolean setup(ISupportExecutionCore mainMachine) {
        return true;
    }

    @Override
    public void reset() {
        eOutputItems = null;
        eOutputFluids = null;
        eMaxProgressingTime = 0;
        eProgressedTime = 0;
        eEut = 0;
    }

    @Override
    public void shutDown() {
        eOutputItems = null;
        eOutputFluids = null;
        eMaxProgressingTime = 0;
        eProgressedTime = 0;
        eEut = 0;
    }

    @Override
    public boolean isIdle() {
        return this.eMaxProgressingTime < 1;
    }

    @Override
    public IExecutionCore setOutputItems(ItemStack[] outputItems) {
        this.eOutputItems = outputItems;
        return this;
    }

    @Override
    public IExecutionCore setOutputFluids(FluidStack[] outputFluids) {
        this.eOutputFluids = outputFluids;
        return this;
    }

    @Override
    public IExecutionCore setMaxProgressingTime(int maxProgressingTime) {
        this.eMaxProgressingTime = maxProgressingTime;
        return this;
    }

    @Override
    public IExecutionCore setEut(long eut) {
        this.eEut = eut;
        return this;
    }

    // endregion

    // region Logic
    protected boolean startedRecipeProcessing = false;
    protected long maxEutCanUse = 0;
    protected CheckRecipeResult lastCheck = CheckRecipeResultRegistry.NO_RECIPE;

    public void mergeOutputItems(ItemStack... outputs) {
        mOutputItems = mergeArray(mOutputItems, outputs);
    }

    public void mergeOutputFluids(FluidStack... outputs) {
        mOutputFluids = mergeArray(mOutputFluids, outputs);
    }

    public boolean tryUseEut(long eut) {
        long canUse = getEutCanUse();
        if (canUse >= eut) {
            lEUt -= eut;
            return true;
        }
        return false;
    }

    public boolean tryDecreaseUsedEut(long eut) {
        if ((-lEUt) >= eut) {
            lEUt += eut;
            return true;
        }
        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (this.lEUt < 0) {
            if (!drainEnergyInput(getActualEnergyUsage())) {
                shutDownAllExecutionCore();
                stopMachine(ShutDownReasonRegistry.POWER_LOSS);
                return false;
            }
        }
        return true;
    }

    protected void shutDownAllExecutionCore() {
        this.shutDown();
        for (IExecutionCore executionCore : executionCores) {
            executionCore.shutDown();
        }
        for (IExecutionCore executionCore : advExecutionCores) {
            executionCore.shutDown();
        }
    }

    protected void resetAllExecutionCore() {
        this.reset();
        for (IExecutionCore executionCore : executionCores) {
            executionCore.reset();
        }
        for (IExecutionCore executionCore : advExecutionCores) {
            executionCore.reset();
        }
        resetModularHatchCollections();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        resetAllExecutionCore();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("startedRecipeProcessing", startedRecipeProcessing);
        aNBT.setLong("maxEutCanUse", maxEutCanUse);
        aNBT.setInteger("eMaxProgressingTime", eMaxProgressingTime);
        aNBT.setInteger("eProgressedTime", eProgressedTime);
        aNBT.setLong("eEut", eEut);
        saveNBTDataItemStacks(aNBT);
        saveNBTDataFluidStacks(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        startedRecipeProcessing = aNBT.getBoolean("startedRecipeProcessing");
        maxEutCanUse = aNBT.getLong("maxEutCanUse");
        eMaxProgressingTime = aNBT.getInteger("eMaxProgressingTime");
        eProgressedTime = aNBT.getInteger("eProgressedTime");
        eEut = aNBT.getLong("eEut");
        loadNBTDataItemStacks(aNBT);
        loadNBTDataFluidStacks(aNBT);
    }

    public long getEutCanUse() {
        return maxEutCanUse + lEUt;
    }

    protected ProcessingLogic createProcessingLogic() {
        return new MultiExecutionProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {

                setEuModifier(getEuModifier());
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                return super.process();
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe);
            }

        };
    }

    @Override
    protected void setupProcessingLogic(ProcessingLogic logic) {
        logic.clear();
        logic.setMachine(this);
        logic.setRecipeMapSupplier(this::getRecipeMap);
        logic.setVoidProtection(false, false);
        logic.setBatchSize(isBatchModeEnabled() ? getMaxBatchSize() : 1);
        logic.setRecipeLocking(this, false);
        logic.setAvailableVoltage(getEutCanUse());
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
        logic.setMaxParallel(getParallelOfEveryNormalExecutionCore());
    }

    @Nonnull
    protected CheckRecipeResult doCheckRecipe() {
        processingLogic.setAvailableVoltage(getEutCanUse());
        return super.doCheckRecipe();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.wiremillRecipes;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        lastCheck = checkProcessingForAllNormalExecutionCore();

        // check every 20tick
        mMaxProgresstime = 20;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public @NotNull CheckRecipeResult checkProcessingForAllNormalExecutionCore() {
        Collection<IExecutionCore> idleExecutionCores = getIdleNormalExecutionCores();
        if (this.isIdle()) idleExecutionCores.add(this);
        if (idleExecutionCores.isEmpty()) {
            return CheckRecipeResults.NoIdleExecutionCore;
        }

        setupProcessingLogic(processingLogic);
        boolean flag = false;
        for (IExecutionCore executionCore : idleExecutionCores) {
            CheckRecipeResult result = checkExecutionCoreProcessing(executionCore);
            if (!result.wasSuccessful()) {
                break;
            } else {
                flag = true;
            }
        }

        return flag ? CheckRecipeResultRegistry.SUCCESSFUL : CheckRecipeResultRegistry.NO_RECIPE;
    }

    protected CheckRecipeResult checkExecutionCoreProcessing(IExecutionCore executionCore) {
        if (!startedRecipeProcessing) startRecipeProcessing();
        CheckRecipeResult result = doCheckRecipe();
        if (result.wasSuccessful()) {
            // set basic parameters to execution core
            executionCore.setOutputItems(processingLogic.getOutputItems())
                .setOutputFluids(processingLogic.getOutputFluids())
                .setMaxProgressingTime(processingLogic.getDuration())
                .setEut(processingLogic.getCalculatedEut());

            // set power parameters of this controller
            lEUt -= processingLogic.getCalculatedEut();
        }
        endRecipeProcessing();
        return result;
    }

    @Override
    public boolean supportsVoidProtection() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    // endregion

}
