package com.Nxer.TwistSpaceTechnology.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.Nxer.TwistSpaceTechnology.util.text.Style;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.blocks.ItemMachines;

@SideOnly(Side.CLIENT)
public class GuideNHTooltipHandler {

    private static final String GRAY = EnumChatFormatting.GRAY.toString();
    private static final String DARK_GRAY = EnumChatFormatting.DARK_GRAY.toString();
    private static final Pattern PROGRESS_BAR = Pattern.compile(GRAY + "(\\|+)(?:" + DARK_GRAY + "(\\|+))?");

    private KeyBinding guideKey;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null || !(event.itemStack.getItem() instanceof ItemMachines) || event.toolTip.size() < 2)
            return;

        if (!TstCreativeTabs.isRegisteredMachine(event.itemStack)) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (guideKey == null) {
            for (KeyBinding binding : mc.gameSettings.keyBindings) {
                if ("key.guidenh.open_guide".equals(binding.getKeyDescription())) {
                    guideKey = binding;
                    break;
                }
            }
            if (guideKey == null) return;
        }

        String keyName = GameSettings.getKeyDisplayString(guideKey.getKeyCode());
        String label = TSTUtils.tr("guideme.guidebook.HoldToShow", GRAY + keyName + DARK_GRAY);
        // Other tooltip listeners can insert lines between the item name and the hint.
        int hintIndex = event.toolTip.indexOf(DARK_GRAY + label);
        if (hintIndex > 0) {
            event.toolTip.set(
                hintIndex,
                Style.RAINBOW.apply(EnumChatFormatting.getTextWithoutFormattingCodes(label))
                    .get());
            return;
        }

        for (int index = 1; index < event.toolTip.size(); index++) {
            String line = event.toolTip.get(index);
            if (!line.startsWith(GRAY + "|")) continue;
            Matcher bar = PROGRESS_BAR.matcher(line);
            if (!bar.matches()) continue;
            FontRenderer font = mc.fontRenderer;
            int barWidth = font.getCharWidth('|');
            int expectedLength = Math.max(1, font.getStringWidth(label) / (barWidth > 0 ? barWidth : 2));
            if (EnumChatFormatting.getTextWithoutFormattingCodes(line)
                .length() != expectedLength) continue;
            event.toolTip.set(
                index,
                Style.RAINBOW.apply(bar.group(1))
                    .get() + line.substring(bar.end(1)));
            return;
        }
    }
}
