package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import java.util.Arrays;
import java.util.List;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

public final class TST_DirectedMobClonerFrontend extends RecipeMapFrontend {

    public TST_DirectedMobClonerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder,
            neiPropertiesBuilder.unificateOutput(false)
                .neiSpecialInfoFormatter(OutputBoostFormatter.INSTANCE));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    private static final class OutputBoostFormatter implements INEISpecialInfoFormatter {

        private static final OutputBoostFormatter INSTANCE = new OutputBoostFormatter();

        @Override
        public List<String> format(RecipeDisplayInfo recipeInfo) {
            if (recipeInfo.recipe.mFluidOutputs.length > 0) {
                return Arrays.asList(
                    TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.1"),
                    TextEnums.tr("ESS.TreeGrowthSimulator.nei.info.2"));
            }
            return Arrays.asList(TextEnums.tr("DirectedMobCloner.nei.perfectOverclock"));
        }
    }
}
