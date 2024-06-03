package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;

public interface IStaticModularHatch extends IModularHatch {

    void onCheckMachine(ModularizedMachineBase<?> machine);
}
