package com.Nxer.TwistSpaceTechnology.mixin;

import static com.Nxer.TwistSpaceTechnology.loader.RecipeLoader.loadRecipemixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gtPlusPlus.xmod.gregtech.HandlerGT;

@Mixin(HandlerGT.class)
public class mixin {

    // Init Fake Recipe
    @Inject(method = "onLoadComplete", at = @At(value = "RETURN"), remap = false)
    private static void init(FMLLoadCompleteEvent event, CallbackInfo ci) {
        loadRecipemixin();
    }

}
