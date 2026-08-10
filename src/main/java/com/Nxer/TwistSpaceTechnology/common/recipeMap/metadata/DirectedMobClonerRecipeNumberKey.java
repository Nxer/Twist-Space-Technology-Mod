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
        // #tr MegaTreeFarm.nei.recipeNumber
        // # Recipe Number: %s
        // #zh_CN 配方编号: %s
        recipeInfo.drawText(StatCollector.translateToLocalFormatted("MegaTreeFarm.nei.recipeNumber", cast(value, 0)));
    }
}
