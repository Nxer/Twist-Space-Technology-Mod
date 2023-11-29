package com.Nxer.TwistSpaceTechnology.system.OreProcess.logic;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;

import gregtech.api.util.GT_Recipe;

public final class OP_Values {

    public static final int OreProcessRecipeDuration = 128;
    public static final int OreProcessRecipeEUt = 30;
    public static final short ticksOfPerFluidConsuming = 256;
    public static final int LubricantCost = 3200;
    public static final boolean moveUnprocessedItemsToOutputs = true;
    public static final Set<String> OreRecipesInputs = new HashSet<>(1763);

    public static void initOreRecipesInputs() {
        for (GT_Recipe recipe : GTCMRecipe.instance.OreProcessingRecipes.mRecipeList) {
            for (ItemStack item : recipe.mInputs) {
                OreRecipesInputs.add(item.getUnlocalizedName() + item.getItemDamage());
            }
        }
    }
}
