package com.Nxer.TwistSpaceTechnology.mixin;

import net.minecraftforge.fluids.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import gregtech.common.render.NEIFluidRenderer;

@Mixin(value = NEIFluidRenderer.class, remap = false)
public interface NEIFluidRendererAccessor {

    @Invoker("register")
    static void tst$registerFluid(Fluid fluid) {
        throw new AssertionError();
    }
}
