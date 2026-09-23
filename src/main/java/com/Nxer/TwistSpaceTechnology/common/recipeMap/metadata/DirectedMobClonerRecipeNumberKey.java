package com.Nxer.TwistSpaceTechnology.common.recipeMap.metadata;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.nei.RecipeDisplayInfo;

public final class DirectedMobClonerRecipeNumberKey extends RecipeMetadataKey<Integer> {

    public static final DirectedMobClonerRecipeNumberKey INSTANCE = new DirectedMobClonerRecipeNumberKey();

    private DirectedMobClonerRecipeNumberKey() {
        super(Integer.class, "directed_mob_cloner_recipe_number");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        // #tr tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.biological_address
        // # Biological Address: %s
        // #zh_CN 生物地址: %s
        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted(
                "tst.ecosphere.recipe.DirectedMobClonerFakeRecipes.biological_address",
                cast(value, 0)));
    }
}
