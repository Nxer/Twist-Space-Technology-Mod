package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.TSTUtils;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public class BloodyHellTierKey extends RecipeMetadataKey<Integer> {

    public static final BloodyHellTierKey INSTANCE = new BloodyHellTierKey();

    public BloodyHellTierKey() {
        super(Integer.class, "bloody_hell_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        // #tr tst.common.recipe.BloodyHellRecipeMap.tier
        // # Bloody Hell Tier:
        // #zh_CN 血狱等级：
        recipeInfo.drawText(TSTUtils.tr("tst.common.recipe.BloodyHellRecipeMap.tier") + tier);
    }
}
