package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.GuiRecipe;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.MTETreeFarm;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public class TST_TreeGrowthSimulatorFrontend extends RecipeMapFrontend {

    private static final int SLOT_SIZE = 18;
    private static final int CENTER_X = 90;
    private static final int SPECIAL_X = CENTER_X - SLOT_SIZE - SLOT_SIZE / 2;
    private static final int SPECIAL_Y = 9;
    private static final int INPUTFLUID_X = CENTER_X + SLOT_SIZE - SLOT_SIZE / 2;
    private static final int INPUTFLUID_Y = SPECIAL_Y;
    private static final int INPUTS_X = CENTER_X - SLOT_SIZE * 3;
    private static final int INPUTS_Y = SPECIAL_Y + SLOT_SIZE + SLOT_SIZE / 2;
    private static final int OUTPUTS_X = CENTER_X + SLOT_SIZE;
    private static final int OUTPUTS_Y = INPUTS_Y;

    public TST_TreeGrowthSimulatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.addNEITransferRect(new Rectangle(72, 18, SLOT_SIZE * 2, SLOT_SIZE))
                .progressBarPos(new Pos2d(CENTER_X - 10, INPUTS_Y + SLOT_SIZE / 2)),
            neiPropertiesBuilder.neiSpecialInfoFormatter(new MegaTreeGrowthSimulator_SpecialValueFormatter()));
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
    public Pos2d getSpecialItemPosition() {
        return new Pos2d(SPECIAL_X, SPECIAL_Y);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(MTETreeFarm.Mode.values().length, INPUTS_X, INPUTS_Y, 2);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(MTETreeFarm.Mode.values().length, OUTPUTS_X, OUTPUTS_Y, 2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(1, INPUTFLUID_X, INPUTFLUID_Y, 1);
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (!(NEIClientUtils.getGuiContainer() instanceof GuiRecipe<?>guiRecipe)) return currentTip;

        String[] tooltipInputs = { TextEnums.tr("ESP.TreeGrowthSimulator.nei.tooltip.2"),
            // #tr ESP.TreeGrowthSimulator.nei.tooltip.2
            // # Place in an input bus to harvest logs
            // #zh_CN 放入输入总线以收获原木
            TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.3"),
            // #tr ESS.TreeGrowthSimulator.nei.tooltip.3
            // # Place in an input bus to harvest saplings
            // #zh_CN 放入输入总线以收获树苗
            TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.4"),
            // #tr ESS.TreeGrowthSimulator.nei.tooltip.4
            // # Place in an input bus to harvest leaves
            // #zh_CN 放入输入总线以收获树叶
            TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.5")
            // #tr ESS.TreeGrowthSimulator.nei.tooltip.5
            // # Place in an input bus to harvest fruit
            // #zh_CN 放入输入总线以收获果实
        };

        for (var input : neiCachedRecipe.mInputs) {
            if (!(input instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)
                || !guiRecipe.isMouseOver(positionedStack, 0)) continue;
            if (positionedStack.isFluid()) {
                currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESP.TreeGrowthSimulator.nei.tooltip.1"));
                // #tr ESP.TreeGrowthSimulator.nei.tooltip.1
                // # Input fluid to grow trees
                // #zh_CN 输入流体以拟生树木
            } else if (positionedStack.isInput()) {
                int circuitConfiguration = positionedStack.item.getItemDamage();
                if (circuitConfiguration >= 1 && circuitConfiguration <= tooltipInputs.length) {
                    currentTip.add(EnumChatFormatting.YELLOW + tooltipInputs[circuitConfiguration - 1]);
                }
            } else {
                currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESP.TreeGrowthSimulator.nei.tooltip.0"));
                // #tr ESP.TreeGrowthSimulator.nei.tooltip.0
                // # Place in input bus
                // #zh_CN 放入输入总线
                if (neiCachedRecipe.mRecipe.mFluidInputs.length > 0
                    && neiCachedRecipe.mRecipe.mFluidInputs[0].getFluid() != FluidRegistry.WATER) {
                    currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.7"));
                    // #tr ESS.TreeGrowthSimulator.nei.tooltip.7
                    // # Any Sapling
                    // #zh_CN 任意树苗
                }
            }
            return currentTip;
        }

        for (var output : neiCachedRecipe.mOutputs) {
            if (!(output instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)
                || !guiRecipe.isMouseOver(positionedStack, 0)) continue;
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("ESS.TreeGrowthSimulator.nei.tooltip.6")
            // #tr ESS.TreeGrowthSimulator.nei.tooltip.6
            // # Requires correct Integrated Circuit to harvest
            // #zh_CN 需要正确的编程电路才能收获
            );
            return currentTip;
        }
        return currentTip;
    }

    public static class MegaTreeGrowthSimulator_SpecialValueFormatter implements INEISpecialInfoFormatter {

        public static final MegaTreeGrowthSimulator_SpecialValueFormatter INSTANCE = new MegaTreeGrowthSimulator_SpecialValueFormatter();

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            return Arrays.asList(
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.1"),
                // #tr ESS.TreeGrowthSimulator.nei.info.1
                // # Output is further boosted
                // #zh_CN 产量随电压等级进一步提高
                TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.2")
            // #tr ESS.TreeGrowthSimulator.nei.info.2
            // # by machine energy tier
            // #zh_CN {\SPACE}
            );
        }
    }
}
