package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.ParallelControllers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IStaticModularHatch;

import gregtech.api.interfaces.ITexture;

public abstract class StaticParallelControllerBase extends ParallelControllerBase implements IStaticModularHatch {

    public StaticParallelControllerBase(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public StaticParallelControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }
}
