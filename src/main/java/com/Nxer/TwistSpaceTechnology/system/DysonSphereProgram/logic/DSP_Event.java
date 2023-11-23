package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

public class DSP_Event {

    public static void onTick() {
        DSP_SolarSailDecreaser.INSTANCE.onTick();
    }
}
