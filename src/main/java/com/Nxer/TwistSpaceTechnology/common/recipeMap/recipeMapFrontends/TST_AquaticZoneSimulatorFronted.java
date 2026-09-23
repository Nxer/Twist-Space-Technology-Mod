package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.GuiRecipe;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
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
            uiPropertiesBuilder.addNEITransferRect(new Rectangle(72, 18, SLOT_SIZE * 2, SLOT_SIZE))
                .progressBarPos(new Pos2d(CENTER_X - 10, INPUTS_Y)),
            neiPropertiesBuilder.neiSpecialInfoFormatter(
                new TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter()));
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawDurationInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawSpecialInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // null
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(1, INPUTS_X, INPUTS_Y, 1);
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, OUTPUTS_X, OUTPUTS_Y, 3);
    }

    @Override
    public @NotNull List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(1, INPUTFLUID_X, INPUTFLUID_Y, 1);
    }

    @Override
    public @NotNull List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (!(NEIClientUtils.getGuiContainer() instanceof GuiRecipe<?>guiRecipe)) return currentTip;

        for (var input : neiCachedRecipe.mInputs) {
            if (!(input instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)
                || !guiRecipe.isMouseOver(positionedStack, 0)) continue;
            if (positionedStack.isFluid()) {
                currentTip.add(EnumChatFormatting.YELLOW + TSTUtils.tr("EcoSphereSimulator.nei.aquatic.medium"));
                // #tr EcoSphereSimulator.nei.aquatic.medium
                // # Operating medium for aquatic simulation
                // #zh_CN 水域模拟所需运行介质
            } else {
                currentTip.add(EnumChatFormatting.YELLOW + TSTUtils.tr("EcoSphereSimulator.nei.aquatic.focus"));
                // #tr EcoSphereSimulator.nei.aquatic.focus
                // # Register in the input interface to focus this output
                // #zh_CN 在输入接口中登记以定向此产物
            }
            return currentTip;
        }

        for (var output : neiCachedRecipe.mOutputs) {
            if (!(output instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)
                || !guiRecipe.isMouseOver(positionedStack, 0)) continue;
            currentTip.add(EnumChatFormatting.YELLOW + TSTUtils.tr("EcoSphereSimulator.nei.aquatic.weight")
            // #tr EcoSphereSimulator.nei.aquatic.weight
            // # Displayed stack size represents base output weight
            // #zh_CN 显示堆叠数代表基础产出权重
            );
            return currentTip;
        }
        return currentTip;
    }

    public static class AquaticZoneSimulator_SpecialValueFormatter implements INEISpecialInfoFormatter {

        public static final TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter INSTANCE = new TST_AquaticZoneSimulatorFronted.AquaticZoneSimulator_SpecialValueFormatter();

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            return Arrays.asList(TSTUtils.tr("EcoSphereSimulator.nei.parallel"));
        }
    }
}
