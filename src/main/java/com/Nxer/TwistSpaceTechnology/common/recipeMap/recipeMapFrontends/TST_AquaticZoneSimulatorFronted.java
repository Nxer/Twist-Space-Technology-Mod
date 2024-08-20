package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class TST_AquaticZoneSimulatorFronted extends RecipeMapFrontend {

    private static final int SLOT_SIZE = 18;
    private static final int CENTER_X = 90;
    private static final int INPUTFLUID_X = CENTER_X - SLOT_SIZE * 3;
    private static final int INPUTFLUID_Y = SLOT_SIZE * 2;
    private static final int INPUTS_X = CENTER_X - SLOT_SIZE * 2;
    private static final int INPUTS_Y = INPUTFLUID_Y;
    private static final int OUTPUTS_X = CENTER_X + SLOT_SIZE;
    private static final int OUTPUTS_Y = INPUTFLUID_Y;

    public TST_AquaticZoneSimulatorFronted(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder
                .addNEITransferRect(
                    new Rectangle(72, 18, SLOT_SIZE * 2, SLOT_SIZE))
                .progressBarPos(new Pos2d(CENTER_X, INPUTS_Y + SLOT_SIZE / 2)),
            neiPropertiesBuilder.neiSpecialInfoFormatter(
                new TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter()));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // null
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(1, INPUTS_X, INPUTS_Y, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(1, OUTPUTS_X, OUTPUTS_Y, 1);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(1, INPUTFLUID_X, INPUTFLUID_Y, 1);
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        // Fluid Stack
        if (stack == neiCachedRecipe.mInputs.get(neiCachedRecipe.mInputs.size() - 1).item) {
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESS.AquaticZoneSimulator.nei.tooltip.0"));
            // #tr ESS.AquaticZone.nei.tooltip.0
            // # Input fluid to simulate waters
            // #zh_CN 输入流体以模拟水域
            super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
            return currentTip;
        }

        // Input Stack
        if (stack == neiCachedRecipe.mInputs.get(0).item) {
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESS.AquaticZoneSimulator.nei.tooltip.1")
            // #tr ESS.AquaticZone.nei.tooltip.1
            // # Place in machine controller slot to target aquaculture
            // #zh_CN 放入控制器插槽以定向产物
            );
            return currentTip;
        }

        // Output Stack
        if (stack == neiCachedRecipe.mOutputs.get(0).item) {
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESS.AquaticZoneSimulator.nei.tooltip.2")
            // #tr ESS.AquaticZone.nei.tooltip.2
            // # Recipe size determines output chance.
            // #zh_CN 配方数值决定输出权重
            );
            return currentTip;
        }
        return currentTip;
    }

    public static class AquaticZoneSimulator_SpecialValueFormatter implements INEISpecialInfoFormatter {

        public static final TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter INSTANCE = new TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter();

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            return Arrays.asList(
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.1"),
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.2"));
        }
    }
}
