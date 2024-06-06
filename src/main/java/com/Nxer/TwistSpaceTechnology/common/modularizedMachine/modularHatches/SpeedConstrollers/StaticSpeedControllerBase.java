package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.SpeedConstrollers;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.IModularizedMachine;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.ModularizedMachineLogic.ModularizedMachineBase;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.modularHatches.IStaticModularHatch;

import gregtech.api.interfaces.ITexture;

public abstract class StaticSpeedControllerBase extends SpeedControllerBase implements IStaticModularHatch {

    public StaticSpeedControllerBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public StaticSpeedControllerBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public void onCheckMachine(ModularizedMachineBase<?> machine) {
        if (machine instanceof IModularizedMachine.ISupportSpeedController speedSupporter) {
            float s = speedSupporter.getStaticSpeedParameterValue();
            if (s <= 0) {
                throw new RuntimeException("Error: Speed Bonus is 0 at machine - " + machine);
            }

            float ts = getSpeedBonus();
            if (ts <= 0) {
                throw new RuntimeException("Error: Speed Bonus is 0. Please try to change your settings at " + this);
            }

            speedSupporter.setStaticSpeedParameterValue(s * ts);

        }
    }

}
