package com.Nxer.TwistSpaceTechnology.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.fluids.FluidRegistry;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.Nxer.TwistSpaceTechnology.common.material.BECCondensates;

import gregtech.client.handler.CondensateAnimationTickHandler;
import gregtech.common.render.FluidDisplayStackRenderer;

@Mixin(value = FluidDisplayStackRenderer.class, remap = false)
public abstract class FluidDisplayStackRendererMixin {

    @Inject(
        method = "renderItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/Tessellator;draw()I",
            ordinal = 0,
            shift = At.Shift.AFTER,
            remap = true),
        require = 1)
    private void tst$renderAdditionalCondensateOverlay(ItemRenderType type, ItemStack item, Object[] data,
        CallbackInfo ci) {
        if (!BECCondensates.isCondensate(FluidRegistry.getFluid(item.getItemDamage()))) return;

        // Share the native overlay and tick frame; the source fluid already supplies the colored base.
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(CondensateAnimationTickHandler.texture);
        GL11.glColor4f(1f, 1f, 1f, 1f);
        float vMin = CondensateAnimationTickHandler.currentFrame / 64f;
        float vMax = (CondensateAnimationTickHandler.currentFrame + 1) / 64f;
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(0, 16, 0, 0, vMax);
        tess.addVertexWithUV(16, 16, 0, 1, vMax);
        tess.addVertexWithUV(16, 0, 0, 1, vMin);
        tess.addVertexWithUV(0, 0, 0, 0, vMin);
        tess.draw();
    }
}
