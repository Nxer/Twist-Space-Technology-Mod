package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon;
import com.dreammaster.thaumcraft.TCHelper;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

public final class ThaumcraftRecipeHandler {

    private ThaumcraftRecipeHandler() {}

    public static AspectList getAspect(String aspectName, int amount) {
        Aspect aspect = aspectName == null ? null : Aspect.getAspect(aspectName);
        return aspect == null ? new AspectList() : new AspectList().add(aspect, amount);
    }

    // Register exact inputs and GT circuit ore dictionary inputs.
    public static InfusionRecipe addInfusionCraftingRecipeAspectNotNull(String research, Object result, int instability,
        AspectList aspects, ItemStack input, ItemStack[] recipe) {
        if (!(result instanceof ItemStack)) {
            return null;
        }
        return TCHelper.addInfusionCraftingRecipe(
            research,
            (ItemStack) result,
            instability,
            removeMissingAspects(aspects),
            convertCircuitInput(input),
            convertCircuitInputs(recipe));
    }

    // Use Thaumcraft's original ore dictionary conversion instead of the default exact-input registration.
    public static InfusionRecipe addInfusionCraftingRecipeAspectNotNullWithOreDict(String research, Object result,
        int instability, AspectList aspects, ItemStack input, ItemStack[] recipe) {
        return ThaumcraftApi
            .addInfusionCraftingRecipe(research, result, instability, removeMissingAspects(aspects), input, recipe);
    }

    private static AspectList removeMissingAspects(AspectList aspects) {
        if (aspects == null) {
            return null;
        }

        AspectList availableAspects = new AspectList();
        for (Aspect aspect : aspects.getAspects()) {
            if (aspect != null) {
                availableAspects.add(aspect, aspects.getAmount(aspect));
            }
        }
        return availableAspects;
    }

    // Convert GT circuit inputs to ore dictionary inputs.
    private static Object convertCircuitInput(ItemStack input) {
        String oreDict = MiracleTopRecipeCommon.getCircuitOreDict(input);
        return oreDict == null ? input : oreDict;
    }

    private static Object[] convertCircuitInputs(ItemStack[] recipe) {
        if (recipe == null) return null;

        Object[] convertedRecipe = new Object[recipe.length];
        for (int i = 0; i < recipe.length; i++) {
            convertedRecipe[i] = convertCircuitInput(recipe[i]);
        }
        return convertedRecipe;
    }
}
