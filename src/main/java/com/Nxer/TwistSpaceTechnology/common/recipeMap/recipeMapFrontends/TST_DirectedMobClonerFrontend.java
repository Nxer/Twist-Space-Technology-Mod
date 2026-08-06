package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerBossRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerOutputInfoKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerTierDisplayKey;

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
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        DirectedMobClonerTierDisplayKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerTierDisplayKey.INSTANCE));
        drawDurationInfo(recipeInfo);
        DirectedMobClonerOutputInfoKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerOutputInfoKey.INSTANCE));
        DirectedMobClonerBossRequirementKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerBossRequirementKey.INSTANCE));
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}
}
