package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.ArrayList;
import java.util.Collection;

import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.IExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.recipe.check.CheckRecipeResult;

public abstract class MultiExecutionCoreMachineBase<T extends MultiExecutionCoreMachineBase<T>>
    extends ModularizedMachineBase<T> implements IModularizedMachine.ISupportExecutionCore, IExecutionCore {

    public MultiExecutionCoreMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MultiExecutionCoreMachineBase(String aName) {
        super(aName);
    }

    @Override
    public Collection<IExecutionCore> getIdleExecutionCores() {
        Collection<IExecutionCore> cores = new ArrayList<>();
        for (IModularHatch modularHatch : modularHatches.get(ModularHatchTypes.EXECUTION_CORE)) {
            if (modularHatch instanceof IExecutionCore idleExecutionCore) {
                if (idleExecutionCore.isIdle()) {
                    cores.add(idleExecutionCore);
                }
            }
        }
        return cores;
    }

    public static class NoIdleExecutionCore implements CheckRecipeResult {

        public static final NoIdleExecutionCore INSTANCE = new NoIdleExecutionCore();

        static {
            CheckRecipeResultRegistry.register(INSTANCE);
        }

        @NotNull
        @Override
        public String getID() {
            return "NoIdleExecutionCore";
        }

        @Override
        public boolean wasSuccessful() {
            return false;
        }

        @NotNull
        @Override
        public String getDisplayString() {
            // #tr CheckRecipeResult.NoIdleExecutionCore
            // # No idle execution core.
            // #zh_CN 没有空闲的执行核心
            return TextEnums.tr("CheckRecipeResult.NoIdleExecutionCore");
        }

        @NotNull
        @Override
        public CheckRecipeResult newInstance() {
            return INSTANCE;
        }

        @Override
        public void encode(@NotNull PacketBuffer buffer) {}

        @Override
        public void decode(PacketBuffer buffer) {}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            return o instanceof NoIdleExecutionCore;
        }

    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        // check every 20tick
        mMaxProgresstime = 20;

        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        return super.checkProcessing();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessingMM() {
        Collection<IExecutionCore> idleExecutionCores = getIdleExecutionCores();
        if (idleExecutionCores.isEmpty() && !this.isIdle()) {
            return NoIdleExecutionCore.INSTANCE;
        }

        // TODO

        return super.checkProcessing();
    }
}
