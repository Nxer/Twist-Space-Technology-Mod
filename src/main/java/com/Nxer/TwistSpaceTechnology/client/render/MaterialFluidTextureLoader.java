package com.Nxer.TwistSpaceTechnology.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialFluidTextures;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MaterialFluidTextureLoader {

    private final Map<String, IIcon> icons = new HashMap<>();

    @SubscribeEvent
    public void onTextureStitchPre(TextureStitchEvent.Pre event) {
        if (event.map.getTextureType() != 0) return;
        icons.clear();
        for (Map.Entry<String, ResourceLocation> entry : MaterialFluidTextures.getTextures()
            .entrySet()) {
            icons.put(
                entry.getKey(),
                event.map.registerIcon(
                    entry.getValue()
                        .toString()));
        }
    }

    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        if (event.map.getTextureType() != 0) return;
        // GT's icon loading has finished; bind to the existing BW fluid rather than registering a second fluid.
        for (Map.Entry<String, IIcon> entry : icons.entrySet()) {
            Fluid fluid = FluidRegistry.getFluid(entry.getKey());
            if (fluid != null) fluid.setIcons(entry.getValue(), entry.getValue());
        }
    }
}
