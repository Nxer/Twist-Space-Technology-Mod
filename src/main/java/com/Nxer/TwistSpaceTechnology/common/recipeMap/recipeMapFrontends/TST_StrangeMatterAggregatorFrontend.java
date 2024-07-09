package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import static gregtech.api.util.GT_Utility.formatNumbers;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.nei.RecipeDisplayInfo;

public class TST_StrangeMatterAggregatorFrontend extends RecipeMapFrontend {

    public TST_StrangeMatterAggregatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        // These look odd because recipeInfo.recipe.mEUt is actually the EU per litre of fluid processed, not
        // the EU/t.
        recipeInfo.drawText(
            GT_Utility.trans("152", "Total: ")
                + formatNumbers(1000L * recipeInfo.recipe.mDuration / 100L * recipeInfo.recipe.mEUt)
                + " EU");
        // 1000 / (20 ticks * 5 seconds) = 10L/t. 10L/t * x EU/L = 10 * x EU/t.
        long averageUsage = 256L * recipeInfo.recipe.mEUt;
        // #tr TST_StrangeMatterAggregatorFrontend.drawEnergyInfo.Power
        // # Usage
        // #zh_CN 消耗功率
        recipeInfo.drawText(
            TextEnums.tr("TST_StrangeMatterAggregatorFrontend.drawEnergyInfo.Power") + ": "
                + formatNumbers(averageUsage)
                + " EU/t"
                + GT_Utility.getTierNameWithParentheses(averageUsage));
    }

    @Override
    public @NotNull Pos2d getSpecialItemPosition() {
        return new Pos2d(79, 44);
    }
}
