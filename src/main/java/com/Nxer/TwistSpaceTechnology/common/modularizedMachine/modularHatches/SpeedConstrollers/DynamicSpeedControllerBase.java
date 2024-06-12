package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IDynamicModularHatch;

import gregtech.api.interfaces.ITexture;

public abstract class DynamicSpeedControllerBase extends SpeedControllerBase implements IDynamicModularHatch {

    public DynamicSpeedControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public DynamicSpeedControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void onCheckProcessing(ModularizedMachineBase<?> machine) {
        if (machine instanceof IModularizedMachine.ISupportSpeedController speedSupporter) {
            float s = speedSupporter.getDynamicSpeedParameterValue();
            if (s <= 0) {
                throw new RuntimeException("Error: Speed Bonus is 0 at machine - " + machine);
            }

            float ts = getSpeedBonus();
            if (ts <= 0) {
                throw new RuntimeException("Error: Speed Bonus is 0. Please try to change your settings at " + this);
            }

            speedSupporter.setDynamicSpeedParameterValue(s * ts); // TODO rework multi speed controller logic

        }
    }

    public abstract int getMaxSpeedMultiplier();

}
