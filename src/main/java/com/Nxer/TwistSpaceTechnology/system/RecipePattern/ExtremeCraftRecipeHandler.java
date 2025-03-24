package com.Nxer.TwistSpaceTechnology.system.RecipePattern;

import static com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology.LOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.recipeMapFrontends.TST_GeneralFrontend;
import com.Nxer.TwistSpaceTechnology.util.TstUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.crafting.ExtremeShapedOreRecipe;
import fox.spiteful.avaritia.crafting.ExtremeShapedRecipe;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class ExtremeCraftRecipeHandler {

    public static RecipeMap<RecipeMapBackend> extremeCraftRecipes = RecipeMapBuilder
        .of("gtcm.recipe.extremeCraftRecipes")
        .maxIO(16, 1, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(TST_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(GTCMItemList.ExtremeCraftCenter.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

    /**
     * temp method
     */
    public static ItemStack mergeStackSize(ItemStack a, ItemStack b) {
        if (a == null) return b;
        if (b == null) return a;
        a.stackSize += b.stackSize;
        return a;
    }

    protected ItemStack[] sortOutInputs(ItemStack... in) {
        Map<TST_ItemID, ItemStack> inputsMap = new LinkedHashMap<>();
        for (ItemStack is : in) {
            if (is == null || is.stackSize < 1) continue;
            TST_ItemID itemID = TST_ItemID.create(is);
            inputsMap.merge(itemID, is.copy(), ExtremeCraftRecipeHandler::mergeStackSize);
        }
        return inputsMap.values()
            .toArray(new ItemStack[0]);
    }

    protected Object[] sortOutInputs(Object... in) {
        Map<Object, Integer> inputsMap = new LinkedHashMap<>();
        for (Object o : in) {
            if (o == null) continue;
            inputsMap.merge(o, 1, Integer::sum);
        }

        List<Object> sorted = new ArrayList<>();
        for (Map.Entry<Object, Integer> pair : inputsMap.entrySet()) {
            Object ingredient = pair.getKey();
            if (ingredient instanceof ItemStack i) {
                sorted.add(GTUtility.copyAmountUnsafe(pair.getValue(), i));
            } else if (ingredient instanceof ArrayList<?>list) {
                @SuppressWarnings("unchecked")
                ArrayList<ItemStack> itemList = (ArrayList<ItemStack>) list;

                String oreName = TstUtils.getOreNameByOreList(itemList);
                if (oreName != null) {
                    // if we managed to get the ore name of the list, we use the ore name.
                    sorted.add(new Object[] { oreName, pair.getValue() });
                } else {
                    // otherwise, just fallback to the first valid item in the list.
                    ItemStack firstValidItem = itemList.stream()
                        .filter(GTUtility::isStackValid)
                        .findFirst()
                        .orElse(null);
                    if (firstValidItem == null) throw new IllegalArgumentException();
                    ItemStack unifiedItem = GTOreDictUnificator.get(false, firstValidItem, true);
                    sorted.add(GTUtility.copyAmountUnsafe(pair.getValue(), unifiedItem));
                }
            } else {
                LOG.info("ERROR ExtremeCraftRecipeHandler.sortOutInputs catch unknown type");
            }
        }

        return sorted.toArray(new Object[0]);

    }

    public void initECRecipe() {

        List<IRecipe> originRecipes = ExtremeCraftingManager.getInstance()
            .getRecipeList();

        LOG.info("start init extreme craft table recipe :" + originRecipes.size());
        for (var Recipe : originRecipes) {
            Object[] inputs = null;
            ItemStack output = null;
            if (Recipe instanceof ExtremeShapedRecipe recipe) {
                inputs = sortOutInputs(recipe.recipeItems);
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ExtremeShapedOreRecipe recipe) {
                inputs = sortOutInputs(recipe.getInput());
                output = recipe.getRecipeOutput();
            } else if (Recipe instanceof ShapelessOreRecipe recipe) {
                inputs = sortOutInputs(
                    recipe.getInput()
                        .toArray());
                output = recipe.getRecipeOutput();
            }

            if (inputs != null && output != null) {
                GTValues.RA.stdBuilder()
                    .ignoreCollision()
                    .itemInputs(inputs)
                    .itemOutputs(output)
                    .eut(0)
                    .duration(128)
                    .addTo(extremeCraftRecipes);
            } else {
                LOG.info("ExtremeCraftRecipeHandler get a null recipe.");
            }
        }
    }

}
