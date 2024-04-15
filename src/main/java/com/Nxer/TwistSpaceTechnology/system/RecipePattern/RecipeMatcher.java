package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import static com.Nxer.TwistSpaceTechnology.system.RecipePattern.CustomCraftRecipe.areStacksEqual;
import static gregtech.api.util.GT_Utility.areFluidsEqual;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.storage.data.IAEItemStack;

public class RecipeMatcher {

    public static final boolean DEBUG = true;

    private static final HashMap<Integer, CustomCraftRecipe> recipeMap = new HashMap<>();

    public static int compareItemStack(ItemStack item1, ItemStack item2) {
        return areStacksEqual(item1, item2, false) ? item1.stackSize - item2.stackSize
            : item1.hashCode() - item2.hashCode();
    }

    public static int compareAEItemStack(IAEItemStack item1, IAEItemStack item2) {
        return compareItemStack(item1.getItemStack(), item2.getItemStack());
    }

    public static int compareFluidStack(FluidStack item1, FluidStack item2) {
        return areFluidsEqual(item1, item2, false) ? item1.amount - item2.amount : item1.hashCode() - item2.hashCode();
    }

    public static CustomCraftRecipe matchAndRegenerateNewPattern(ICraftingPatternDetails details) {
        return matchAndRegenerateNewPattern(details, null);
    }

    // if provider is not null, matcher will also match the provider signature, eg: crusher.
    public static CustomCraftRecipe matchAndRegenerateNewPattern(ICraftingPatternDetails details, String provider) {
        if (!details.isCraftable()) return null;
        return recipeMap
            .get(new CustomCraftRecipe(details.getInputs(), details.getOutputs(), null, null, 0, provider).hashCode());
    }

    public static void addRecipe(CustomCraftRecipe recipe) {
        recipeMap.put(recipe.hashCode(), recipe);
    }

    public static ItemStack[] AndItemStack(ItemStack[] stack1, ItemStack[] recipeStack) {
        ArrayList<ItemStack> newStack = new ArrayList<>();
        int x = 0, y = 0;
        while (x != stack1.length && y != recipeStack.length) {
            int tag = compareItemStack(stack1[x], recipeStack[y]);
            if (tag == 0) {
                newStack.add(recipeStack[y]);
                x++;
                y++;
            }
            if (tag > 0) x++;
            if (tag < 0) y++;
        }
        return newStack.toArray(new ItemStack[0]);
    }

    public static FluidStack[] AndFluidStack(FluidStack[] stack1, FluidStack[] recipeStack) {
        ArrayList<FluidStack> newStack = new ArrayList<>();
        int x = 0, y = 0;
        while (x != stack1.length && y != recipeStack.length) {
            int tag = compareFluidStack(stack1[x], recipeStack[y]);
            if (tag == 0) {
                newStack.add(recipeStack[y]);
                x++;
                y++;
            }
            if (tag > 0) x++;
            if (tag < 0) y++;
        }
        return newStack.toArray(new FluidStack[0]);
    }

    public static IAEItemStack[] AndAEItemStack(IAEItemStack[] stack1, IAEItemStack[] recipeStack) {
        ArrayList<IAEItemStack> newStack = new ArrayList<>();
        int x = 0, y = 0;
        while (x != stack1.length && y != recipeStack.length) {
            int tag = compareAEItemStack(stack1[x], recipeStack[y]);
            if (tag == 0) {
                recipeStack[y].setStackSize(Math.min(recipeStack[y].getStackSize(), stack1[x].getStackSize()));
                newStack.add(recipeStack[y]);
                x++;
                y++;
            }
            if (tag > 0) x++;
            if (tag < 0) y++;
        }
        return newStack.toArray(new IAEItemStack[0]);
    }

}
