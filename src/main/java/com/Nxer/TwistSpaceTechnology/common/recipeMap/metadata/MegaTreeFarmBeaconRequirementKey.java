package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class MegaTreeFarmBeaconRequirementKey extends RecipeMetadataKey<Integer> {

    public static final MegaTreeFarmBeaconRequirementKey INSTANCE = new MegaTreeFarmBeaconRequirementKey();

    private MegaTreeFarmBeaconRequirementKey() {
        super(Integer.class, "mega_tree_farm_beacon_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // #tr MegaTreeFarm.nei.beaconTier
        // # Beacon Tier: %s
        // #zh_CN 信标等级: %s
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("MegaTreeFarm.nei.beaconTier", cast(value, 1)));
    }
}
