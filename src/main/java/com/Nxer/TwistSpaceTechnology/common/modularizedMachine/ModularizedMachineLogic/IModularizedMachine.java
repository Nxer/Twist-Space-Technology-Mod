package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import java.util.Collection;

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

    interface ISupportParallelController extends IModularizedMachine {

        int getParallelParameterValue();

        void setParallelParameter(int value);
    }

    interface ISupportSpeedController extends IModularizedMachine {

    }
}
