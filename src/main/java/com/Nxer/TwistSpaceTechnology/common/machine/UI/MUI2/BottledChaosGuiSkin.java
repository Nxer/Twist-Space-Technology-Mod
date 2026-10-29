package com.Nxer.TwistSpaceTechnology.common.machine.UI.MUI2;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modularui2.GTGuiTextures;

/** Pixel-aligned chrome. The animated light stays outside slots and text. */
final class BottledChaosGuiSkin {

    final IDrawable BUTTON = GTGuiTextures.BUTTON_STANDARD;
    final IDrawable HOVER = GTGuiTextures.BUTTON_STANDARD;
    final IDrawable SLOT = GTGuiTextures.SLOT_ITEM_STANDARD;
    final IDrawable SELECTED = GTGuiTextures.BUTTON_STANDARD_PRESSED;
    final IDrawable TERMINAL = (context, x, y, width, height, theme) -> {
        // Two pixels of native casing separate the light ring from the black content area.
        GTGuiTextures.BACKGROUND_STANDARD.draw(context, x, y, width, height, theme);
        fill(context, x + 4, y + 4, width - 8, height - 8, 0xFF000000, theme);
        drawLightRing(context, x, y, width, height, theme);
    };

    int buttonText() {
        return 0xFF303030;
    }

    int workspaceText() {
        return 0xFF303030;
    }

    int workspaceMuted() {
        return 0xFF454545;
    }

    IDrawable background(int dividerX, int lowerTop) {
        return (context, x, y, width, height, theme) -> {
            GTGuiTextures.BACKGROUND_STANDARD.draw(context, x, y, width, height, theme);
            fill(context, x + dividerX, y + lowerTop, 1, height - lowerTop - 16, 0xFF606776, theme);
            fill(context, x + dividerX + 1, y + lowerTop, 1, height - lowerTop - 16, 0xFFDCE1F1, theme);
        };
    }

    @SideOnly(Side.CLIENT)
    private static void drawLightRing(GuiContext context, int x, int y, int width, int height, WidgetTheme theme) {
        fill(context, x, y, width, 2, 0xFF284D58, theme);
        fill(context, x, y + height - 2, width, 2, 0xFF284D58, theme);
        fill(context, x, y, 2, height, 0xFF284D58, theme);
        fill(context, x + width - 2, y, 2, height, 0xFF284D58, theme);
        int horizontal = width - 2;
        int vertical = height - 2;
        int perimeter = 2 * (horizontal + vertical);
        // Same 55 GUI pixels/second as the approved preview, independent of frame rate and TPS.
        long distance = (System.nanoTime() / 1_000_000L) * 55 / 1000;
        for (int light = 0; light < 2; light++) {
            int head = (int) ((distance + (long) light * perimeter / 2) % perimeter);
            for (int tail = 48; tail >= 0; tail -= 2) {
                int position = Math.floorMod(head - tail, perimeter);
                int dx;
                int dy;
                if (position < horizontal) {
                    dx = position;
                    dy = 0;
                } else if (position < horizontal + vertical) {
                    dx = horizontal;
                    dy = position - horizontal;
                } else if (position < 2 * horizontal + vertical) {
                    dx = 2 * horizontal + vertical - position;
                    dy = vertical;
                } else {
                    dx = 0;
                    dy = perimeter - position;
                }
                int alpha = (50 - tail) * 180 / 50;
                fill(context, x + dx, y + dy, 2, 2, tail == 0 ? 0xFFC8FCFF : alpha << 24 | 0x27E6FF, theme);
            }
        }
    }

    IDrawable progress(DoubleSupplier value) {
        return (context, x, y, width, height, theme) -> {
            fill(context, x, y, width, height, 0xFF30434D, theme);
            fill(context, x + 1, y + 1, width - 2, height - 2, 0xFF101B22, theme);
            int filled = (int) Math.floor((width - 2) * Math.max(0, Math.min(1, value.getAsDouble())));
            fill(context, x + 1, y + 1, filled, height - 2, 0xFF549532, theme);
        };
    }

    static IDrawable indicator(IntSupplier color) {
        return (context, x, y, width, height, theme) -> {
            fill(context, x, y, width, height, 0xFF252B31, theme);
            fill(context, x + 1, y + 1, width - 2, height - 2, color.getAsInt(), theme);
            fill(context, x + 1, y + 1, width - 2, 1, 0x40FFFFFF, theme);
            fill(context, x + 1, y + 2, 1, height - 3, 0x22FFFFFF, theme);
            fill(context, x + 2, y + 2, width - 4, height - 5, 0x24FFFFFF, theme);
            fill(context, x + 1, y + height - 2, width - 2, 1, 0x50000000, theme);
            fill(context, x + width - 2, y + 2, 1, height - 3, 0x40000000, theme);
        };
    }

    @SideOnly(Side.CLIENT)
    private static void fill(GuiContext context, int x, int y, int width, int height, int color, WidgetTheme theme) {
        if (width > 0 && height > 0) new Rectangle().color(color)
            .draw(context, x, y, width, height, theme);
    }
}
