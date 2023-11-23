package com.Nxer.TwistSpaceTechnology.event;

import com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic.DSP_Event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickingEvent {

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        DSP_Event.onTick();
    }
}
