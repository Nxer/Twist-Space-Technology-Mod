package com.Nxer.TwistSpaceTechnology.util.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;

public class TST_RecipeBuilder {

    public static TST_RecipeBuilder builder() {
        return new TST_RecipeBuilder();
    }

    private ItemStack[] inputItems = new ItemStack[0];
    private ItemStack[] outputItems = new ItemStack[0];
    private FluidStack[] inputFluids = new FluidStack[0];
    private FluidStack[] outputFluids = new FluidStack[0];
    private int[] outputChance;
    private int eut = 0;
    private int duration = 0;
    private int specialValue = 0;

    public TST_RecipeBuilder() {}

    public TST_RecipeBuilder itemInputs(ItemStack... inputItems) {
        if (inputItems != null && inputItems.length > 0) {
            this.inputItems = inputItems;
        }
        return this;
    }

    public TST_RecipeBuilder itemOutputs(ItemStack... outputItems) {
        if (outputItems != null && outputItems.length > 0) {
            this.outputItems = outputItems;
        }
        return this;
    }

    public TST_RecipeBuilder fluidInputs(FluidStack... inputFluids) {
        if (inputFluids != null && inputFluids.length > 0) {
            this.inputFluids = inputFluids;
        }
        return this;
    }

    public TST_RecipeBuilder fluidOutputs(FluidStack... outputFluids) {
        if (outputFluids != null && outputFluids.length > 0) {
            this.outputFluids = outputFluids;
        }
        return this;
    }

    public TST_RecipeBuilder outputChances(int... outputChance) {
        this.outputChance = outputChance;
        return this;
    }

    public TST_RecipeBuilder eut(int eut) {
        this.eut = eut;
        return this;
    }

    public TST_RecipeBuilder eut(long eut) {
        this.eut = (int) eut;
        return this;
    }

    public TST_RecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public TST_RecipeBuilder specialValue(int specialValue) {
        this.specialValue = specialValue;
        return this;
    }

    public TST_RecipeBuilder noOptimize() {
        return this;
    }

    public TST_RecipeBuilder addTo(RecipeMap<?> recipeMap) {
        GT_Recipe tempRecipe = new GT_Recipe(
            false,
            inputItems,
            outputItems,
            null,
            outputChance,
            inputFluids,
            outputFluids,
            duration,
            eut,
            specialValue);

        tempRecipe.mInputs = inputItems.clone();
        tempRecipe.mOutputs = outputItems.clone();

        recipeMap.add(tempRecipe);
        return this;
    }
}
