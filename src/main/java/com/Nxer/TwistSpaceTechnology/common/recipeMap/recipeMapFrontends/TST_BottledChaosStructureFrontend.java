package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

/** Results stay above the selectors and a fixed-size material grid. */
public final class TST_BottledChaosStructureFrontend extends TST_RecipeMapFrontend {

    public static final int MATERIAL_COLUMNS = 8;
    public static final int MATERIAL_ROWS = 12;
    public static final int MATERIAL_SLOTS = MATERIAL_COLUMNS * MATERIAL_ROWS;
    public static final int MATERIAL_HEIGHT = MATERIAL_ROWS * 18;
    public static final int MATERIALS_Y = 72;
    public static final int CARRIER_SLOT = MATERIAL_SLOTS;
    public static final int INPUT_SLOTS = CARRIER_SLOT + 1;
    public static final int DESCRIPTION_Y = 40;
    public static final int PAGE_HEIGHT = MATERIALS_Y + MATERIAL_HEIGHT + 12;
    public static final Pos2d CARRIER_POSITION = new Pos2d(46, 16);
    public static final Pos2d OUTPUT_POSITION = new Pos2d(114, 16);

    public TST_BottledChaosStructureFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(
            ui.progressBarPos(new Pos2d(80, 16))
                .logoPos(new Pos2d(151, 16)),
            nei.unificateOutput(false));
    }

    @Override
    protected @NotNull NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder builder) {
        return builder.recipeBackgroundSize(new Size(170, PAGE_HEIGHT));
    }

    public static Pos2d inputPosition(int slot, int materialsY) {
        if (slot == CARRIER_SLOT) return CARRIER_POSITION;
        return new Pos2d(13 + slot % MATERIAL_COLUMNS * 18, materialsY + slot / MATERIAL_COLUMNS * 18);
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int count) {
        List<Pos2d> result = new ArrayList<>();
        for (int i = 0; i < count; i++) result.add(inputPosition(i, MATERIALS_Y));
        return result;
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int count) {
        List<Pos2d> result = new ArrayList<>();
        for (int i = 0; i < count; i++) result.add(OUTPUT_POSITION.add(i * 18, 0));
        return result;
    }

    @Override
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        // The handler draws the grid from the same origin as its positioned stacks.
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize);
        addProgressBar(builder, ctx);
        addGregTechLogo(builder, ctx.windowOffset);
        return builder;
    }

    public static void drawBackground(int materialsY, int height, Pos2d offset) {
        GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE.draw(offset, new Size(170, height - offset.y), 0);
        GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY
            .draw(new Pos2d(10, materialsY - 3).add(offset), new Size(150, MATERIAL_HEIGHT + 6), 0);
        for (int slot = 0; slot < INPUT_SLOTS; slot++)
            ModularUITextures.ITEM_SLOT.draw(inputPosition(slot, materialsY).add(offset), new Size(18, 18), 0);
        for (int slot = 0; slot < 2; slot++) ModularUITextures.ITEM_SLOT.draw(
            OUTPUT_POSITION.add(slot * 18, 0)
                .add(offset),
            new Size(18, 18),
            0);
        // #tr tst.bottledchaos.recipe.structure.materials
        // # Materials
        // #zh_CN 材料
        Minecraft.getMinecraft().fontRenderer.drawString(
            StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.materials"),
            13 + offset.x,
            materialsY - 14 + offset.y,
            0x404040);
    }

    public static String[] diskNotes() {
        // #tr tst.bottledchaos.recipe.structure.disk
        // # ME disk: load / unload
        // #zh_CN ME磁盘: 加载 / 卸载

        // #tr tst.bottledchaos.recipe.structure.capacity
        // # Unloading requires free capacity and item-type slots.
        // #zh_CN 磁盘容量或物品类型槽不足时无法卸载.
        return new String[] { StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.disk"),
            StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.capacity") };
    }

    @Override
    public void drawDescription(@NotNull RecipeDisplayInfo info) {
        var display = info.recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY);
        if (display == null) return;
        drawLines(info, poolText(display));
        // The handler places selectors above the grid and notes below it.
    }

    public static String poolText(BottledChaosStructureDisplay display) {
        return StatCollector
            .translateToLocalFormatted("item.tst.bottledchaos.machine_module.info.pool", display.poolNames());
    }

    private static void drawLines(RecipeDisplayInfo info, String text) {
        for (String line : Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 156))
            info.drawText(line, 7, 10);
    }
}
