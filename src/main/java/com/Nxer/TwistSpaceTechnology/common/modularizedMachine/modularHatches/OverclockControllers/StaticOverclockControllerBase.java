package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.OverclockControllers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IStaticModularHatch;

import gregtech.api.interfaces.ITexture;

public abstract class StaticOverclockControllerBase extends OverclockControllerBase implements IStaticModularHatch {

    public StaticOverclockControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public StaticOverclockControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void onCheckMachine(ModularizedMachineBase<?> machine) {
        if (machine instanceof IModularizedMachine.ISupportOverclockController supportOverclockController) {
            supportOverclockController.setOverclockParameter(getTimeReduction(), getPowerIncrease());
        }
    }

}
