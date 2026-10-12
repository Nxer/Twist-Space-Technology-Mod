package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;

/** Generic authorization metadata for folded recipes; individual recipe registrations are intentionally separate. */
public final class BottledChaosRecipeMetadata {

    public static final RecipeMetadataKey<Long> REQUIRED_CATEGORY_MASK = SimpleRecipeMetadataKey
        .create(Long.class, "bottled_chaos_required_category_mask");
    public static final RecipeMetadataKey<ItemStack> REQUIRED_RECIPE_TOKEN = SimpleRecipeMetadataKey
        .create(ItemStack.class, "bottled_chaos_required_recipe_token");
    public static final RecipeMetadataKey<BottledChaosRecipeProcessor.DisplayRecipe> DISPLAY_BATCH = SimpleRecipeMetadataKey
        .create(BottledChaosRecipeProcessor.DisplayRecipe.class, "bottled_chaos_display_batch");

    private BottledChaosRecipeMetadata() {}
}
