package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class DirectedMobClonerBossRequirementKey extends RecipeMetadataKey<ItemStack> {

    public static final DirectedMobClonerBossRequirementKey INSTANCE = new DirectedMobClonerBossRequirementKey();

    private DirectedMobClonerBossRequirementKey() {
        super(ItemStack.class, "directed_mob_cloner_boss_requirement");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        ItemStack requiredItem = cast(value, null);
        if (requiredItem != null) recipeInfo.drawText("Requires: " + requiredItem.getDisplayName());
    }
}
