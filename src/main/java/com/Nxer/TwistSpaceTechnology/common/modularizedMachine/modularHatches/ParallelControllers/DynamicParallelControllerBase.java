package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IDynamicModularHatch;

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
        if (machine instanceof IModularizedMachine.ISupportParallelController parallelSupporter) {
            int p = parallelSupporter.getDynamicParallelParameterValue();
            if (p == Integer.MAX_VALUE) return;
            int tp = getParallel();
            if (p >= Integer.MAX_VALUE - tp) {
                parallelSupporter.setDynamicParallelParameter(Integer.MAX_VALUE);
            } else {
                parallelSupporter.setDynamicParallelParameter(p + tp);
            }
        }
    }

    public abstract int getMaxParallel();

}
