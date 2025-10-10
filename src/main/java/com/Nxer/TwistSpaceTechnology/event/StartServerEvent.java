package com.Nxer.TwistSpaceTechnology.event;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_SolarSailDecreaser;

import cpw.mods.fml.common.event.FMLServerStartedEvent;

public class StartServerEvent {

    public static StartServerEvent INSTANCE = new StartServerEvent();

    public void onLoading(FMLServerStartedEvent event) {
        DSP_SolarSailDecreaser.init();
    }

}
