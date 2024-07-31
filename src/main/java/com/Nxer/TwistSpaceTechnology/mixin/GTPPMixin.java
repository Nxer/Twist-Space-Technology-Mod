package com.Nxer.TwistSpaceTechnology.mixin;

import static com.Nxer.TwistSpaceTechnology.loader.RecipeLoader.loadRecipeMixIN;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.Nxer.TwistSpaceTechnology.config.Config;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gtPlusPlus.xmod.gregtech.HANDLER_GT;

@Mixin(HANDLER_GT.class)
public class GTPPMixin {

    // Tree Farm Init Fake Recipe
    @Inject(method = "onLoadComplete", at = @At(value = "RETURN"), remap = false)
    private static void init(FMLLoadCompleteEvent event, CallbackInfo ci) {
        if (Config.Enable_MegaTreeFarm) loadRecipeMixIN();
    }

}
