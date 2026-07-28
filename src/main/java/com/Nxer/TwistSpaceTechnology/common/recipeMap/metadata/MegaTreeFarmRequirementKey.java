package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class MegaTreeFarmRequirementKey extends RecipeMetadataKey<String> {

    public static final MegaTreeFarmRequirementKey INSTANCE = new MegaTreeFarmRequirementKey();

    private MegaTreeFarmRequirementKey() {
        super(String.class, "mega_tree_farm_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        String requirement = cast(value, null);
        if (requirement != null) recipeInfo.drawText(requirement);
    }
}
