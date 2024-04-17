package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import static gregtech.api.recipe.RecipeMap.ALL_RECIPE_MAPS;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEItemStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;

public class GeneralGTRecipe extends CustomCraftRecipe {

    public static final Map<String, RecipeMap<?>> AllRecipeMaps = ALL_RECIPE_MAPS;

    public static void initGTRecipes() {
        for (var map : AllRecipeMaps.values()) {
            if (map == null) continue;
            for (var recipe : map.getAllRecipes()) {
                if (recipe == null) continue;
                RecipeMatcher.addRecipe(new GeneralGTRecipe(recipe, map.unlocalizedName));
            }
        }
    }

    public GeneralGTRecipe(IAEItemStack[] in, IAEItemStack[] out, double[] itemPriority, double[] fluidPriority,
        int rounds, String provider) {
        super(in, out, itemPriority, fluidPriority, rounds, provider);
    }

    public GeneralGTRecipe(ItemStack[] inputItem, FluidStack[] inputFluid, ItemStack[] outputItem,
        FluidStack[] outputFluid, double[] itemPriority, double[] fluidPriority, int rounds, String provider) {
        super(inputItem, inputFluid, outputItem, outputFluid, itemPriority, fluidPriority, rounds, provider);
    }

    public GeneralGTRecipe(GT_Recipe recipe, String provider) {
        this(
            recipe.mInputs == null ? new ItemStack[0]
                : (ItemStack[]) Arrays.stream(recipe.mInputs)
                    .filter(Objects::nonNull)
                    .sorted(RecipeMatcher::compareItemStack)
                    .toArray(),
            recipe.mFluidInputs == null ? new FluidStack[0]
                : (FluidStack[]) Arrays.stream(recipe.mFluidInputs)
                    .filter(Objects::nonNull)
                    .toArray(),
            recipe.mOutputs == null ? new ItemStack[0]
                : (ItemStack[]) Arrays.stream(recipe.mOutputs)
                    .filter(Objects::nonNull)
                    .sorted(RecipeMatcher::compareItemStack)
                    .toArray(),
            recipe.mFluidOutputs == null ? new FluidStack[0]
                : (FluidStack[]) Arrays.stream(recipe.mFluidOutputs)
                    .filter(Objects::nonNull)
                    .toArray(),
            Arrays.stream(recipe.mChances)
                .mapToDouble((num) -> (double) num / 10000.0)
                .toArray(),
            null,
            0,
            provider);
    }

    @Override
    public boolean matchRecipe(Object o) {
        return false;
    }

    @Override
    public void initRecipe() {}

}
