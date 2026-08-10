package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class DirectedMobClonerTierDisplayKey extends RecipeMetadataKey<Integer> {

    public static final DirectedMobClonerTierDisplayKey INSTANCE = new DirectedMobClonerTierDisplayKey();

    private DirectedMobClonerTierDisplayKey() {
        super(Integer.class, "directed_mob_cloner_tier_display");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("MegaTreeFarm.nei.structureTier", cast(value, 1)));
    }
}
