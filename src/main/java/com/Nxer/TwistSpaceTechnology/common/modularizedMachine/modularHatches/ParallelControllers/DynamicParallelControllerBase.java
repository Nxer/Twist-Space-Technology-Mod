package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IDynamicModularHatch;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;

import gregtech.api.interfaces.ITexture;

public abstract class DynamicParallelControllerBase extends ParallelControllerBase implements IDynamicModularHatch {

    public DynamicParallelControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, null);
    }

    public DynamicParallelControllerBase(String aName, int aTier, ITexture[][][] aTextures) {
        super(aName, aTier, null, aTextures);
    }

    @Override
    public void onCheckProcessing(ModularizedMachineBase<?> machine) {
        onChecking(machine);
    }

    public abstract int getMaxParallel();

}
