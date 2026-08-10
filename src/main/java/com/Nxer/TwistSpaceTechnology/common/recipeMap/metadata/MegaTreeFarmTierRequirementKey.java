package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class MegaTreeFarmTierRequirementKey extends RecipeMetadataKey<Integer> {

    public static final MegaTreeFarmTierRequirementKey INSTANCE = new MegaTreeFarmTierRequirementKey();

    private MegaTreeFarmTierRequirementKey() {
        super(Integer.class, "mega_tree_farm_tier_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        // #tr MegaTreeFarm.nei.structureTier
        // # Structure Tier: %s
        // #zh_CN 结构等级: %s
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("MegaTreeFarm.nei.structureTier", tier));
    }
}
