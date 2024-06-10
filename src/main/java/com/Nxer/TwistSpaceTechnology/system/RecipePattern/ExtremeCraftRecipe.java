package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;

import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.TST_RecipeMapBackend;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Utility;

public class ExtremeCraftRecipe extends CustomCraftRecipe {

    public static ExtremeCraftingManager originRecipes = ExtremeCraftingManager.getInstance();

    public static RecipeMap<TST_RecipeMapBackend> extremeCraftRecipes;

    public static void initECRecipe() {
        extremeCraftRecipes = RecipeMapBuilder.of("gtcm.recipe.extremeCraftRecipes", TST_RecipeMapBackend::new)
            .maxIO(16, 16, 0, 0)
            .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
            .frontend(TST_GeneralFrontend::new)
            .neiHandlerInfo(
                builder -> builder.setDisplayStack(GTCMItemList.ExtremeCraftCenter.get(1))
                    .setMaxRecipesPerPage(1))
            .disableOptimize()
            .build();
        LOG.info(
            "start init extreme craft table recipe :" + originRecipes.getRecipeList()
                .size());
        for (var Recipe : originRecipes.getRecipeList()) {
            Object[] inputs = null;
            ItemStack output = null;
            if (Recipe instanceof ExtremeShapedRecipe recipe) {
                inputs = mergeItemstacks((Object[]) recipe.recipeItems);
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ExtremeShapedOreRecipe recipe) {
                inputs = mergeItemstacks(recipe.getInput());
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ShapelessOreRecipe recipe) {
                inputs = mergeItemstacks(
                    recipe.getInput()
                        .toArray());
                output = recipe.getRecipeOutput();
            }

            // LOG.info("-----------------------");
            if (inputs != null) {
                for (var in : inputs) {
                    // LOG.info(in);
                    inputs = mergeItemstacks(inputs);
                }
            } else {
                // LOG.info("input is null!");
            }
            // LOG.info("output:" + output);
            // LOG.info("-----------------------");
            if (inputs != null && output != null) {
                // LOG.info("insert into recipemap");
                int pre = extremeCraftRecipes.getAllRecipes()
                    .size();
                GT_Values.RA.stdBuilder()
                    .ignoreCollision()
                    .itemInputs(inputs)
                    .itemOutputs(output)
                    .eut(RECIPE_ZPM)
                    .duration(1)
                    .addTo(extremeCraftRecipes);
                if (pre == extremeCraftRecipes.getAllRecipes()
                    .size()) {
                    GT_Values.RA.stdBuilder()
                        .ignoreCollision()
                        .itemInputs(inputs)
                        .itemOutputs(output)
                        .eut(RECIPE_ZPM)
                        .duration(1)
                        .addTo(extremeCraftRecipes);
                }
            } else {
                // LOG.info("get a null recipe here");
            }
        }
        // LOG.info("end init extreme craft table recipe");
    }

    public static Object[] addCircuit(Object[] obj, int n) {
        Object[] newObj = new Object[obj.length + 1];
        System.arraycopy(obj, 0, newObj, 1, obj.length);
        newObj[0] = GT_Utility.getIntegratedCircuit(n);
        return newObj;
    }

    public ExtremeCraftRecipe(ItemStack[] inputItem, ItemStack outputItem, int rounds) {
        super(inputItem, null, new ItemStack[] { outputItem }, null, null, null, rounds, "ExtremeShaped");
    }

    public ExtremeCraftRecipe(ExtremeShapedRecipe recipe) {
        this(
            mergeItemstacks(recipe.recipeItems).stream()
                .filter(Objects::nonNull)
                .sorted(RecipeMatcher::compareItemStack)
                .toArray(ItemStack[]::new),
            recipe.getRecipeOutput(),
            0);
    }

    @Override
    public boolean matchRecipe(Object o) {
        return false;
    }

    @Override
    public void initRecipe() {

    }
}
