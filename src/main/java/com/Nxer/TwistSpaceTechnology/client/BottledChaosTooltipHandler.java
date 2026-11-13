package com.Nxer.TwistSpaceTechnology.client;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.MM_BottledChaos;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.slprime.chromatictooltips.event.TitleEnricherEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BottledChaosTooltipHandler {

    @SubscribeEvent
    public void onTitle(TitleEnricherEvent event) {
        ItemStack stack = event.target.getItem();
        if (MM_BottledChaos.isPrototype(stack) && !stack.hasDisplayName()) {
            event.displayName = TSTUtils.tr(MM_BottledChaos.PROTOTYPE_NAME_KEY);
        }
    }
}
