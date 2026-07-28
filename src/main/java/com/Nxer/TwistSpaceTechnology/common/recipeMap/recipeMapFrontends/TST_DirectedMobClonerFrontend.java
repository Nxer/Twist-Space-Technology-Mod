package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.RecipeDisplayInfo;

public final class TST_DirectedMobClonerFrontend extends RecipeMapFrontend {

    public TST_DirectedMobClonerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.unificateOutput(false));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}
}
