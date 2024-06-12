package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;

public interface IDynamicModularHatch extends IModularHatch {

    void onCheckProcessing(ModularizedMachineBase<?> machine);
}
