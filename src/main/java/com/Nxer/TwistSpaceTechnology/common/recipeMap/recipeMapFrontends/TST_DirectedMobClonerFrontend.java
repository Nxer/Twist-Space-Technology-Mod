package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerOutputInfoKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerRecipeNumberKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorExecutionProtocolRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.EcoSphereSimulatorTierRequirementKey;
import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

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
        EcoSphereSimulatorExecutionProtocolRequirementKey.INSTANCE.drawInfo(
            recipeInfo,
            recipeInfo.recipe.getMetadata(EcoSphereSimulatorExecutionProtocolRequirementKey.INSTANCE));
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
                currentTip.add(
                    EnumChatFormatting.YELLOW
                        + TSTUtils.tr("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.address_input"));
                // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.address_input
                // # Select this biological address in the input interface
                // #zh_CN 在输入接口中选择此生物地址
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
                    currentTip.add(
                        EnumChatFormatting.YELLOW
                            + TSTUtils.tr("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.target"));
                    // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.target
                    // # Cloning target
                    // #zh_CN 克隆目标
                } else {
                    currentTip.add(
                        EnumChatFormatting.YELLOW
                            + TSTUtils.tr("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.first_drop"));
                    // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.first_drop
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
            currentTip.add(
                EnumChatFormatting.YELLOW + TSTUtils.tr("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.blood"));
            // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.blood
            // # Blood for initial reconstruction
            // #zh_CN 用于初始重构的血液
        } else if ("lifeessence".equals(fluidName)) {
            currentTip.add(
                EnumChatFormatting.YELLOW
                    + TSTUtils.tr("tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.life_essence"));
            // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.life_essence
            // # Life Essence for directed cloning
            // #zh_CN 用于定向克隆的生命本源
        }
    }
}
