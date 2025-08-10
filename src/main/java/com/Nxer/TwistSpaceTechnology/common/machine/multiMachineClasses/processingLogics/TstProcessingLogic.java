package com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.processingLogics;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.ParallelHelper;

public class TstProcessingLogic extends ProcessingLogic {

    @NotNull
    @Override
    protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
        // @formatter:off
        return new TstParallelHelper().setRecipe(recipe)
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
        // @formatter:on
    }

    public TstProcessingLogic setOverclockType(OverclockType ocType) {
        setOverclock(ocType.timeReduction, ocType.powerIncrease);
        return this;
    }
}
