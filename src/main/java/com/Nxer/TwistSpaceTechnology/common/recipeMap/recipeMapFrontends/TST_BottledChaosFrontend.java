package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosRecipeMetadata.DISPLAY_BATCH;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTUtility;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

/** NEI presentation for the dynamically folded Bottled Chaos route examples. */
public class TST_BottledChaosFrontend extends TST_GeneralFrontend {

    public TST_BottledChaosFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        this(uiPropertiesBuilder, neiPropertiesBuilder, new Pos2d(79, 7));
    }

    protected TST_BottledChaosFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder, Pos2d logoPos) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.unificateOutput(false), logoPos);
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(79, 44);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        addQuantityBadges(recipe, recipe.mInputs, true);
        addQuantityBadges(recipe, recipe.mOutputs, false);
    }

    private void addQuantityBadges(GTNEIDefaultHandler.CachedDefaultRecipe recipe, List<PositionedStack> stacks,
        boolean input) {
        var display = recipe.mRecipe.getMetadata(DISPLAY_BATCH);
        if (display == null) return;
        for (var stack : stacks) {
            if (!(stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed)) continue;
            long amount = 0;
            if (fixed.isFluid()) {
                var fluid = GTUtility.getFluidFromDisplayStack(fixed.item);
                for (var candidate : input ? display.fluidInputs()
                    : display.plan()
                        .fluidOutputs()) {
                    if (candidate.fluidStack()
                        .isFluidEqual(fluid)) {
                        amount = candidate.amount();
                        break;
                    }
                }
            } else {
                for (var candidate : input ? display.itemInputs()
                    : display.plan()
                        .itemOutputs()) {
                    if (fixed.containsWithNBT(candidate.itemStack())) {
                        amount = candidate.stackSize();
                        break;
                    }
                }
            }
            if (amount <= Integer.MAX_VALUE) continue;
            fixed.setCustomBadge(String.format(Locale.ROOT, "%.3g", (double) amount), formatNumber(amount));
        }
    }

    @Override
    public void drawDescription(@NotNull RecipeDisplayInfo recipeInfo) {
        var display = recipeInfo.recipe.getMetadata(DISPLAY_BATCH);
        if (display == null) {
            drawEnergyInfo(recipeInfo);
            drawDurationInfo(recipeInfo);
            return;
        }
        if (display.plan()
            .durationTicks() <= Integer.MAX_VALUE) {
            drawEnergyInfo(recipeInfo);
        } else {
            double duration = Math.ceil(
                display.plan()
                    .durationTicks());
            recipeInfo.drawText(
                StatCollector
                    .translateToLocalFormatted("GT5U.nei.display.total", formatNumber(display.baseEUt() * duration)));
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.nei.display.usage", formatNumber(display.baseEUt()), ""));
        }
        // #tr tst.bottledchaos.recipe.total_time
        // # Reference Time: %s s
        // #zh_CN 参考用时: %s秒
        drawFittedText(
            recipeInfo,
            StatCollector.translateToLocalFormatted(
                "tst.bottledchaos.recipe.total_time",
                formatNumber(
                    display.plan()
                        .durationTicks() / 20.0)));
        // #tr tst.bottledchaos.recipe.time_adjustment
        // # Actual duration reflects installed modules and controller parameters
        // #zh_CN 实际耗时根据已装模组及主机参数折算
        drawFittedText(recipeInfo, StatCollector.translateToLocal("tst.bottledchaos.recipe.time_adjustment"));
        // #tr tst.bottledchaos.recipe.recipe_maps
        // # Recipe Maps:
        // #zh_CN 配方池:
        recipeInfo.drawText(StatCollector.translateToLocal("tst.bottledchaos.recipe.recipe_maps"));
        // #tr tst.bottledchaos.recipe.recipe_map_time
        // # %s: %s s
        // #zh_CN %s: %s秒
        for (var process : display.processingTimes()) {
            drawFittedText(
                recipeInfo,
                StatCollector.translateToLocalFormatted(
                    "tst.bottledchaos.recipe.recipe_map_time",
                    BottledChaosMachineConfiguration.poolName(process.map()),
                    formatNumber(process.durationTicks() / 20.0)));
        }
    }

    private static void drawFittedText(RecipeDisplayInfo recipeInfo, String text) {
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        if (width <= 160) {
            recipeInfo.drawText(text);
            return;
        }
        float scale = 160.0f / width;
        GL11.glPushMatrix();
        GL11.glScalef(scale, 1.0f, 1.0f);
        recipeInfo.drawText(text, Math.round(5.0f / scale), 10);
        GL11.glPopMatrix();
    }
}
