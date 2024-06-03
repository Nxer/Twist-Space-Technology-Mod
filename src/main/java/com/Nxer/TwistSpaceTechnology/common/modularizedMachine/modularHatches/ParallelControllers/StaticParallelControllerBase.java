package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IStaticModularHatch;

import gregtech.api.interfaces.ITexture;

public abstract class StaticParallelControllerBase extends ParallelControllerBase implements IStaticModularHatch {

    public StaticParallelControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, null);
    }

    public StaticParallelControllerBase(String aName, int aTier, ITexture[][][] aTextures) {
        super(aName, aTier, null, aTextures);
    }

    @Override
    public void onCheckMachine(ModularizedMachineBase<?> machine) {
        if (machine instanceof IModularizedMachine.ISupportParallelController parallelSupporter) {
            int p = parallelSupporter.getStaticParallelParameterValue();
            if (p == Integer.MAX_VALUE) return;
            int tp = getParallel();
            if (p >= Integer.MAX_VALUE - tp) {
                parallelSupporter.setStaticParallelParameter(Integer.MAX_VALUE);
            } else {
                parallelSupporter.setStaticParallelParameter(p + tp);
            }
        }
    }

}
