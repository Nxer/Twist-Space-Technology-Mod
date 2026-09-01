package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerOutputInfoKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerRecipeNumberKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorBeaconRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.recipe.GuiRecipe;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

public final class TST_DirectedMobClonerFrontend extends RecipeMapFrontend {

    public TST_DirectedMobClonerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.unificateOutput(false));
    }

    @Override
    public void drawDescription(@NotNull RecipeDisplayInfo recipeInfo) {
        drawDurationInfo(recipeInfo);
        EcoSphereSimulatorTierRequirementKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(EcoSphereSimulatorTierRequirementKey.INSTANCE));
        EcoSphereSimulatorBeaconRequirementKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(EcoSphereSimulatorBeaconRequirementKey.INSTANCE));
        DirectedMobClonerRecipeNumberKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerRecipeNumberKey.INSTANCE));
        DirectedMobClonerOutputInfoKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerOutputInfoKey.INSTANCE));
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    protected void drawEnergyInfo(@NotNull RecipeDisplayInfo recipeInfo) {}

    @Override
    public @NotNull List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (!(NEIClientUtils.getGuiContainer() instanceof GuiRecipe<?>guiRecipe)) return currentTip;

        for (var input : neiCachedRecipe.mInputs) {
            if (!(input instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)
                || !guiRecipe.isMouseOver(positionedStack, 0)) continue;
            if (positionedStack.isFluid()) {
                addFluidInputTooltip(currentTip, neiCachedRecipe.mRecipe.mFluidInputs);
            } else if (positionedStack.isInput()) {
                currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("DirectedMobCloner.nei.tooltip.circuit"));
                // #tr DirectedMobCloner.nei.tooltip.circuit
                // # The sum of all programmed circuit configurations in the input buses determines the recipe number
                // #zh_CN 输入总线内所有编程电路的配置值之和决定配方编号
            }
            return currentTip;
        }

        if (!neiCachedRecipe.mRecipe.getMetadataOrDefault(DirectedMobClonerOutputInfoKey.INSTANCE, false)) {
            return currentTip;
        }
        int outputIndex = 0;
        for (var output : neiCachedRecipe.mOutputs) {
            if (!(output instanceof GTNEIDefaultHandler.FixedPositionedStack positionedStack)) continue;
            if (guiRecipe.isMouseOver(positionedStack, 0)) {
                if (outputIndex == 0) {
                    currentTip
                        .add(EnumChatFormatting.YELLOW + TextEnums.tr("DirectedMobCloner.nei.tooltip.cloneTarget"));
                    // #tr DirectedMobCloner.nei.tooltip.cloneTarget
                    // # Cloning target
                    // #zh_CN 克隆目标
                } else {
                    currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("DirectedMobCloner.nei.tooltip.firstDrop"));
                    // #tr DirectedMobCloner.nei.tooltip.firstDrop
                    // # The first valid drop of this target
                    // #zh_CN 该目标的第一个有效掉落物
                }
                return currentTip;
            }
            outputIndex++;
        }
        return currentTip;
    }

    private static void addFluidInputTooltip(List<String> currentTip, FluidStack[] fluidInputs) {
        if (fluidInputs.length == 0 || fluidInputs[0] == null || fluidInputs[0].getFluid() == null) return;
        String fluidName = fluidInputs[0].getFluid()
            .getName();
        if ("blood".equals(fluidName)) {
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("DirectedMobCloner.nei.tooltip.blood"));
            // #tr DirectedMobCloner.nei.tooltip.blood
            // # Input blood to generate Life Essence
            // #zh_CN 输入血液以生成生命本源
        } else if ("lifeessence".equals(fluidName)) {
            currentTip.add(EnumChatFormatting.YELLOW + TextEnums.tr("DirectedMobCloner.nei.tooltip.lifeEssence"));
            // #tr DirectedMobCloner.nei.tooltip.lifeEssence
            // # Input Life Essence to perform directed cloning
            // #zh_CN 输入生命本源以执行定向克隆
        }
    }
}
