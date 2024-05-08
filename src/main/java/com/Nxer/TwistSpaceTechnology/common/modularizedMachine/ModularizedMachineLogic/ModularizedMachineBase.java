package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic;

import com.Nxer.TwistSpaceTechnology.common.machine.multiMachineClasses.GTCM_MultiMachineBase;

public abstract class ModularizedMachineBase<T extends ModularizedMachineBase<T>> extends GTCM_MultiMachineBase<T> implements IModularizedMachine{

    // region Class Constructor
    public ModularizedMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ModularizedMachineBase(String aName) {
        super(aName);
    }

    // endregion

    // region Modular Logic

    // endregion

    // region Processing Logic

    // endregion
}
