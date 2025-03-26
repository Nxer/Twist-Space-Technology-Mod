package com.Nxer.TwistSpaceTechnology.mixin;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.util.LanguageManager;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.GTMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GTMod.class, remap = false)
public class HookMaterials_Mixin {

    @Inject(method = "onPreLoad", at = @At(value = "INVOKE", target = "Lgregtech/api/enums/Materials;init()V"))
    private void tst$runBeforeMaterialsInit(FMLPreInitializationEvent aEvent, CallbackInfo ci) {
        MaterialsTST.init();
        LanguageManager.initGTMaterials();
        TwistSpaceTechnology.LOG.info("Materials Hooked!");
    }

}
