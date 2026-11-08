package com.Nxer.TwistSpaceTechnology.mixin;

import net.minecraftforge.fluids.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialFluidTextures;

import gregtech.common.fluid.GTFluid;

@Mixin(value = GTFluid.class, remap = false)
public abstract class GTFluidMixin {

    @Inject(method = "getColor()I", at = @At("HEAD"), cancellable = true, require = 1)
    private void tst$keepFullTextureColor(CallbackInfoReturnable<Integer> cir) {
        if (MaterialFluidTextures.hasFullColorTexture((Fluid) (Object) this)) cir.setReturnValue(0xFFFFFF);
    }
}
