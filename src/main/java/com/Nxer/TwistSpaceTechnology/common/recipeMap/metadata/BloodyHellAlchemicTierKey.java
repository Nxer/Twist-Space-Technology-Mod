package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;
import org.jetbrains.annotations.Nullable;

public class BloodyHellAlchemicTierKey extends RecipeMetadataKey<Integer> {

    public static final BloodyHellAlchemicTierKey INSTANCE = new BloodyHellAlchemicTierKey();

    public BloodyHellAlchemicTierKey() {
        super(Integer.class, "bloody_hell_alchemic_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        // #tr BloodyHell_Alchemic_Recipe_Tier
        // # Blood Orb Tier:
        // #zh_CN 气血宝珠等级：
        recipeInfo.drawText(TextEnums.tr("BloodyHell_Alchemic_Recipe_Tier") + tier);
    }
}
