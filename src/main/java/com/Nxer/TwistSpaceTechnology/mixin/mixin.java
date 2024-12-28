package com.Nxer.TwistSpaceTechnology.mixin;

import org.spongepowered.asm.mixin.Mixin;

import gtPlusPlus.xmod.gregtech.HandlerGT;

@Mixin(HandlerGT.class)
public class mixin {

    // Init Fake Recipe
    // @Inject(method = "onLoadComplete", at = @At(value = "RETURN"), remap = false)
    // private static void init(FMLLoadCompleteEvent event, CallbackInfo ci) {
    // loadRecipemixin();
    // }

}
