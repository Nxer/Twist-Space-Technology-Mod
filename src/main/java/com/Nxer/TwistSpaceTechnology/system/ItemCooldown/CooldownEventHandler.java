package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CooldownEventHandler {

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.isCanceled()) return;

        if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution scale = event.resolution;

        drawCooldown(mc, scale);

    }

    public static void drawCooldown(Minecraft mc, ScaledResolution scale) {
        int k = scale.getScaledWidth();
        int l = scale.getScaledHeight();
        if(mc.thePlayer.getCurrentEquippedItem()==null)
        return;
        Item holditem = mc.thePlayer.getCurrentEquippedItem()
            .getItem();
        if (!(holditem instanceof IItemHasCooldown)) {
            return;
        }
        float cooldown = ((IItemHasCooldown) holditem).getCooldown();
        mc.getTextureManager()
            .bindTexture(new ResourceLocation("gtnhcommunitymod", "textures/HUD/cooldown_bar.png"));
        mc.ingameGUI.drawTexturedModalRect(k / 2 - 91, l - 32 + 3, 0, 0, 182, 5);
        mc.ingameGUI.drawTexturedModalRect(
            k / 2 - 91,
            l - 32 + 3,
            0,
            5,
            (int) (182 * (Math.max(ItemCooldownSaver.getPastTime(holditem), 0) / cooldown)),
            5);
        mc.getTextureManager()
            .bindTexture(Gui.icons);
    }
}
