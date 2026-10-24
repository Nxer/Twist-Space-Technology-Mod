package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosStructureDisplay;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

/** Fixed and variable materials are separate regions; one synchronized frame represents one complete structure. */
public final class TST_BottledChaosStructureFrontend extends RecipeMapFrontend {

    public static final int REGION_SLOTS = 48;
    public static final int CARRIER_SLOT = REGION_SLOTS * 2;
    public static final int INPUT_SLOTS = CARRIER_SLOT + 1;
    public static final int DESCRIPTION_Y = 271;
    public static final int PAGE_HEIGHT = 400;
    public static final Pos2d CARRIER_POSITION = new Pos2d(46, 248);
    public static final Pos2d OUTPUT_POSITION = new Pos2d(114, 248);

    public TST_BottledChaosStructureFrontend(BasicUIPropertiesBuilder ui, NEIRecipePropertiesBuilder nei) {
        super(
            ui.progressBarPos(new Pos2d(80, 248))
                .logoPos(new Pos2d(151, 248)),
            nei.unificateOutput(false));
    }

    @Override
    protected @NotNull NEIRecipePropertiesBuilder modifyNEIProperties(NEIRecipePropertiesBuilder builder) {
        return builder.recipeBackgroundSize(new Size(170, PAGE_HEIGHT));
    }

    public static Pos2d inputPosition(int slot) {
        if (slot == CARRIER_SLOT) return CARRIER_POSITION;
        int index = slot % REGION_SLOTS;
        return new Pos2d((slot < REGION_SLOTS ? 7 : 91) + index % 4 * 18, 24 + index / 4 * 18);
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int count) {
        List<Pos2d> result = new ArrayList<>();
        for (int i = 0; i < count; i++) result.add(inputPosition(i));
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
        ModularWindow.Builder builder = ModularWindow.builder(neiProperties.recipeBackgroundSize)
            .setBackground(GTUITextures.BACKGROUND_NEI_SINGLE_RECIPE);
        for (int x : new int[] { 4, 88 }) {
            builder.widget(
                new DrawableWidget().setDrawable(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY)
                    .setPos(new Pos2d(x, 20).add(ctx.windowOffset))
                    .setSize(78, 223));
        }
        // #tr tst.bottledchaos.recipe.structure.fixed_inputs
        // # Fixed inputs
        // #zh_CN 固定输入
        builder.widget(
            TextWidget.localised("tst.bottledchaos.recipe.structure.fixed_inputs")
                .setTextAlignment(Alignment.Center)
                .setPos(new Pos2d(4, 6).add(ctx.windowOffset))
                .setSize(78, 12));
        // #tr tst.bottledchaos.recipe.structure.variable_inputs
        // # Variable inputs
        // #zh_CN 可变输入
        builder.widget(
            TextWidget.localised("tst.bottledchaos.recipe.structure.variable_inputs")
                .setTextAlignment(Alignment.Center)
                .setPos(new Pos2d(88, 6).add(ctx.windowOffset))
                .setSize(78, 12));
        for (int i = 0; i < INPUT_SLOTS; i++) {
            int slot = i;
            builder.widget(
                SlotWidget.phantom(ctx.itemInputsInventory, slot)
                    .setBackground(ModularUITextures.ITEM_SLOT)
                    .setPos(inputPosition(slot).add(ctx.windowOffset))
                    .setSize(18, 18)
                    .setEnabled(widget -> {
                        var recipe = ctx.recipeSupplier.get();
                        return recipe != null && slot < recipe.mRecipe.mInputs.length
                            && recipe.mRecipe.mInputs[slot] != null;
                    }));
        }
        for (int i = 0; i < 2; i++) {
            int slot = i;
            builder.widget(
                SlotWidget.phantom(ctx.itemOutputsInventory, slot)
                    .setBackground(ModularUITextures.ITEM_SLOT)
                    .setPos(
                        OUTPUT_POSITION.add(slot * 18, 0)
                            .add(ctx.windowOffset))
                    .setSize(18, 18)
                    .setEnabled(widget -> {
                        var recipe = ctx.recipeSupplier.get();
                        return recipe != null && slot < recipe.mRecipe.mOutputs.length
                            && recipe.mRecipe.mOutputs[slot] != null;
                    }));
        }
        addProgressBar(builder, ctx);
        addGregTechLogo(builder, ctx.windowOffset);
        // #tr tst.bottledchaos.recipe.structure.disk
        // # ME disk: load / unload
        // #zh_CN ME磁盘: 加载 / 卸载

        // #tr tst.bottledchaos.recipe.structure.capacity
        // # Unloading requires free capacity and item-type slots.
        // #zh_CN 磁盘容量或物品类型槽不足时无法卸载.
        return builder;
    }

    @Override
    public void drawDescription(@NotNull RecipeDisplayInfo info) {
        var display = info.recipe.getMetadata(BottledChaosStructureDisplay.DISPLAY);
        if (display == null) return;
        if (info.recipe.mSpecialValue == 0) {
            drawLines(info, StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.disk"));
            drawLines(info, StatCollector.translateToLocal("tst.bottledchaos.recipe.structure.capacity"));
        }
        drawLines(
            info,
            StatCollector.translateToLocalFormatted("tst.bottledchaos.machine_module.pool", display.poolNames()));
        String[] notes = info.recipe.getNeiDesc();
        if (notes != null) for (String note : notes) drawLines(info, note);
    }

    private static void drawLines(RecipeDisplayInfo info, String text) {
        for (String line : Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(text, 156))
            info.drawText(line, 7, 10);
    }
}
