package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.List;

import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.GTNEIDefaultHandler;

public class TST_StellarForgeFrontend extends RecipeMapFrontend {

    public TST_StellarForgeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        String aToolTip = EnumChatFormatting.YELLOW + TextEnums.tr("MiracleDoor.nei.tooltip.0");
        // #tr MiracleDoor.nei.tooltip.0
        // # Place in input bus to melt molten metal into ingots
        // #zh_CN 放入输入总线,将熔融金属熔铸为锭

        // Special Stack
        if (GTUtility.areStacksEqual(
            stack,
            neiCachedRecipe.mInputs
                .get(neiCachedRecipe.mInputs.size() - neiCachedRecipe.mRecipe.mFluidInputs.length - 1).item)) {
            if (!currentTip.contains(aToolTip)) {
                currentTip.add(aToolTip);
            }
            super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
            return currentTip;
        }
        return currentTip;
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(79, 62);
    }
}
