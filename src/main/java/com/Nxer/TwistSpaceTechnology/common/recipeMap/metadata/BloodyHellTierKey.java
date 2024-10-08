package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

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
        // #tr BloodyHell_Recipe_Tier
        // # Bloody Hell Tier:
        // #zh_CN 血狱等级：
        recipeInfo.drawText(TextEnums.tr("BloodyHell_Recipe_Tier") + tier);
    }
}
