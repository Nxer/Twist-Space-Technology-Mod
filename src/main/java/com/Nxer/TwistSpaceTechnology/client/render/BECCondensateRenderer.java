package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.common.material.BECCondensates;
import com.Nxer.TwistSpaceTechnology.mixin.NEIFluidRendererAccessor;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BECCondensateRenderer {

    public static void register() {
        for (BECCondensates.Condensate type : BECCondensates.getTypes()) {
            NEIFluidRendererAccessor.tst$registerFluid(type.getEntangledFluid());
        }
        MinecraftForge.EVENT_BUS.register(new BECCondensateRenderer());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTextureStitch(TextureStitchEvent.Post event) {
        if (event.map.getTextureType() != 0) return;
        for (BECCondensates.Condensate type : BECCondensates.getTypes()) {
            Fluid source = type.getSourceFluid()
                .getFluid();
            // Also supports source fluids that GTFluid.withIconsFrom cannot copy.
            type.getEntangledFluid()
                .setIcons(source.getStillIcon(), source.getFlowingIcon());
        }
    }
}
