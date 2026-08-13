package com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerOutputInfoKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.DirectedMobClonerRecipeNumberKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmBeaconRequirementKey;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata.MegaTreeFarmTierRequirementKey;

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
    public void drawDescription(@NotNull RecipeDisplayInfo recipeInfo) {
        drawDurationInfo(recipeInfo);
        MegaTreeFarmTierRequirementKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(MegaTreeFarmTierRequirementKey.INSTANCE));
        MegaTreeFarmBeaconRequirementKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(MegaTreeFarmBeaconRequirementKey.INSTANCE));
        DirectedMobClonerRecipeNumberKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerRecipeNumberKey.INSTANCE));
        DirectedMobClonerOutputInfoKey.INSTANCE
            .drawInfo(recipeInfo, recipeInfo.recipe.getMetadata(DirectedMobClonerOutputInfoKey.INSTANCE));
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    protected void drawEnergyInfo(@NotNull RecipeDisplayInfo recipeInfo) {}
}
