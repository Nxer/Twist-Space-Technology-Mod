package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.FluidStackLong;
import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase.ItemStackLong;
import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;

public class GTCM_ProcessingLogic extends ProcessingLogic {

    private GTCM_ParallelHelper longOutputHelper;
    private List<ItemStackLong> longItemOutputs;
    private List<FluidStackLong> longFluidOutputs;

    /**
     * Override to tweak parallel logic if needed.
     */
    @Nonnull
    @Override
    protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
        GTCM_ParallelHelper helper = new GTCM_ParallelHelper();
        helper.setRecipe(recipe)
            .setItemInputs(inputItems)
            .setFluidInputs(inputFluids)
            .setAvailableEUt(availableVoltage * availableAmperage)
            .setMachine(machine, protectItems, protectFluids)
            .setRecipeLocked(recipeLockableMachine, isRecipeLocked)
            .setMaxParallel(maxParallel)
            .setEUtModifier(euModifier)
            .enableBatchMode(batchSize)
            .setConsumption(true)
            .setOutputCalculation(true);
        if (machine instanceof GTCM_MultiMachineBase<?>tstMachine && tstMachine.isMEOutputEnabled()) {
            helper.enableLongOutputCalculation();
        }
        return helper;
    }

    @Nonnull
    @Override
    protected CheckRecipeResult applyRecipe(@Nonnull GTRecipe recipe, @Nonnull ParallelHelper helper,
        @Nonnull OverclockCalculator calculator, @Nonnull CheckRecipeResult result) {
        longOutputHelper = null;
        longItemOutputs = null;
        longFluidOutputs = null;
        CheckRecipeResult appliedResult = super.applyRecipe(recipe, helper, calculator, result);
        if (appliedResult.wasSuccessful() && helper instanceof GTCM_ParallelHelper tstHelper
            && tstHelper.isCalculatingOutputsAsLong()) {
            longOutputHelper = tstHelper;
        }
        return appliedResult;
    }

    public boolean hasLongOutputs() {
        return longOutputHelper != null || longItemOutputs != null || longFluidOutputs != null;
    }

    public List<ItemStackLong> getLongItemOutputs() {
        if (longItemOutputs != null) return longItemOutputs;
        return longOutputHelper == null ? Collections.emptyList() : longOutputHelper.getLongItemOutputs();
    }

    public List<FluidStackLong> getLongFluidOutputs() {
        if (longFluidOutputs != null) return longFluidOutputs;
        return longOutputHelper == null ? Collections.emptyList() : longOutputHelper.getLongFluidOutputs();
    }

    public void setLongOutputs(List<ItemStackLong> itemOutputs, List<FluidStackLong> fluidOutputs) {
        longOutputHelper = null;
        longItemOutputs = itemOutputs;
        longFluidOutputs = fluidOutputs;
    }

    public void clearLongOutputs() {
        longOutputHelper = null;
        longItemOutputs = null;
        longFluidOutputs = null;
    }

    public GTCM_ProcessingLogic setOverclockType(OverclockType t) {
        setOverclock(t.timeReduction, t.powerIncrease);
        return this;
    }
}
