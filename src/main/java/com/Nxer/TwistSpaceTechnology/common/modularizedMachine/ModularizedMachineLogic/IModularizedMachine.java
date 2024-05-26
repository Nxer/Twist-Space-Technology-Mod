package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Collection;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ExecutionCores.IExecutionCore;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IModularHatch;

public interface IModularizedMachine {

    Collection<ModularHatchTypes> getSupportedModularHatchTypes();

    Collection<ModularBlockTypes> getSupportedModularBlockTypes();

    /**
     * Reset static settings, when checkMachine.
     */
    void resetModularStaticSettings();

    /**
     * Apply static settings, when checkMachine.
     */
    void applyModularStaticSettings();

    /**
     * Called when checkMachine.
     */
    default void checkModularStaticSettings() {
        resetModularStaticSettings();
        applyModularStaticSettings();
    }

    /**
     * Reset dynamic parameters, when checkProcessing.
     */
    void resetModularDynamicParameters();

    /**
     * Apply dynamic parameters, when checkProcessing.
     */
    void applyModularDynamicParameters();

    /**
     * Called when checkMachine.
     */
    default void checkModularDynamicParameters() {
        resetModularDynamicParameters();
        applyModularDynamicParameters();
    }

    /**
     * The original method to get all modular hatches.
     *
     * @return All modular hatches.
     */
    Collection<IModularHatch> getAllModularHatches();

    void resetModularHatchCollections();

    interface ISupportParallelController extends IModularizedMachine {

        int getStaticParallelParameterValue();

        void setStaticParallelParameter(int value);

        int getDynamicParallelParameterValue();

        void setDynamicParallelParameter(int value);
    }

    interface ISupportSpeedController extends IModularizedMachine {

        float getStaticSpeedParameterValue();

        void setStaticSpeedParameterValue(float value);

        float getDynamicSpeedParameterValue();

        void setDynamicSpeedParameterValue(float value);
    }

    interface ISupportPowerConsumptionController extends IModularizedMachine {

        float getStaticPowerConsumptionParameterValue();

        void setStaticPowerConsumptionParameterValue(float value);

    }

    interface ISupportOverclockController extends IModularizedMachine {

        void setOverclockParameter(int timeReduction, int powerIncrease);
    }

    interface ISupportExecutionCore extends IModularizedMachine {

        Collection<IExecutionCore> getIdleNormalExecutionCores();

        Collection<IExecutionCore> getAllWorkingExecutionCores();

        int getParallelOfEveryNormalExecutionCore();

        void mergeOutputItems(ItemStack... outputs);

        void mergeOutputFluids(FluidStack... outputs);

        boolean tryUseEut(long eut);

        boolean tryDecreaseUsedEut(long eut);

        void forceCheckProcessing();
    }

    interface ISupportAllModularHatches extends ISupportParallelController, ISupportSpeedController,
        ISupportPowerConsumptionController, ISupportOverclockController {
    }

}
