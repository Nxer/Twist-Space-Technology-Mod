package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import java.util.Calendar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

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
        if (mc.thePlayer.getCurrentEquippedItem() == null) return;
        ItemStack holditem = mc.thePlayer.getCurrentEquippedItem();
        if (!(holditem.getItem() instanceof IItemHasCooldown)) {
            return;
        }
        long cooldown = ((IItemHasCooldown) holditem.getItem()).getCooldown();
        NBTTagCompound itemNBT = holditem.getTagCompound();
        long pasttime;
        if (itemNBT == null) pasttime = cooldown;
        else if (!itemNBT.hasKey("LastUse")) pasttime = cooldown;
        else pasttime = Calendar.getInstance()
            .getTimeInMillis() - itemNBT.getLong("LastUse");

        GL11.glColor4f(0.5f, 0.5f, 1f, 1f);
        mc.getTextureManager()
            .bindTexture(Gui.icons);
        mc.ingameGUI.drawTexturedModalRect(k / 2 - 91, l - 32 + 3, 0, 64, 182, 5);
        mc.ingameGUI.drawTexturedModalRect(
            k / 2 - 91,
            l - 32 + 3,
            0,
            69,
            Math.min((int) (183F * ((float) Math.max(pasttime, 0) / (float) cooldown)), 182),
            5);

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
}
