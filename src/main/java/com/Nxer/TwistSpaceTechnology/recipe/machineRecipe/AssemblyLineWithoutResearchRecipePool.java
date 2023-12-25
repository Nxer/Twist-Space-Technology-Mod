package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic.TieredCircuits;
import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Recipe;

public class AssemblyLineWithoutResearchRecipePool implements IRecipePool {

    public ItemStack transToWildCircuit(ItemStack items) {
        for (TieredCircuits circuit : TieredCircuits.values()) {
            if (circuit.contains(items)) {
                return circuit.getPatternCircuit(items.stackSize);
            }
        }
        return null;
    }

    public static List<ItemStack[]> generateAllItemInput(ItemStack[] baseStack, ItemStack[][] wildCard) {
        List<ItemStack[]> result = new ArrayList<>();
        result.add(Utils.copyItemStackArray(baseStack));
        int len = baseStack.length;
        for (int i = 0; i < len; i++) {
            if (wildCard[i] == null) continue;
            for (int j = 1; j < wildCard[i].length; j++) {
                if (wildCard[i][j] == null) continue;
                ItemStack wildCardCopy = wildCard[i][j].copy();
                int resultSize = result.size();
                for (int k = 0; k < resultSize; k++) {
                    ItemStack[] inputList = Utils.copyItemStackArray(result.get(k));
                    inputList[i] = wildCardCopy;
                    result.add(inputList);
                }
            }
        }
        return result;
    }

    @Override
    public void loadRecipes() {
        TwistSpaceTechnology.LOG.info("Loading Mega Assembly Line Recipes.");

        // skip these recipes
        ItemStack[] skipRecipeOutputs = new ItemStack[] { ItemList.Circuit_Wetwaremainframe.get(1),
            ItemList.Circuit_Biowaresupercomputer.get(1), ItemList.Circuit_Biomainframe.get(1),
            ItemList.Circuit_OpticalAssembly.get(1), ItemList.Circuit_OpticalComputer.get(1),
            ItemList.Circuit_OpticalMainframe.get(1) };

        // start check assembly line recipes
        checkRecipe: for (var recipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {

            for (ItemStack skip : skipRecipeOutputs) {
                // skip recipes need skip
                if (Utils.metaItemEqual(recipe.mOutput, skip)) continue checkRecipe;
            }

            ItemStack[] inputItems = new ItemStack[recipe.mInputs.length];
            ItemStack[][] inputWildcards = new ItemStack[recipe.mInputs.length][];
            boolean hasCustomWildcardItemList = false;

            if (recipe.mOreDictAlt != null && recipe.mOreDictAlt.length > 0) {
                // wildcards recipe
                for (int i = 0; i < recipe.mOreDictAlt.length; i++) {
                    if (recipe.mOreDictAlt[i] != null && recipe.mOreDictAlt[i].length > 0) {
                        ItemStack circuitStack = transToWildCircuit(recipe.mOreDictAlt[i][0]);
                        if (circuitStack != null) {
                            // this wildcard is a circuit stack
                            // replace it by dreamcraft:anyCircuit then the recipe will check this stack by any circuit
                            inputItems[i] = circuitStack;
                        } else {
                            // this wildcard is a custom list
                            hasCustomWildcardItemList = true;
                            inputWildcards[i] = recipe.mOreDictAlt[i];
                        }
                    } else {
                        // this stack is normal
                        inputItems[i] = recipe.mInputs[i];
                    }
                }
            } else {
                // no wildcards recipe
                inputItems = recipe.mInputs;
            }

            if (!hasCustomWildcardItemList) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(inputItems)
                    .itemOutputs(recipe.mOutput)
                    .fluidInputs(recipe.mFluidInputs)
                    .noOptimize()
                    .eut(recipe.mEUt)
                    .duration(recipe.mDuration)
                    .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
            } else {
                for (int i = 0; i < inputItems.length; i++) {
                    if (inputItems[i] == null) {
                        inputItems[i] = inputWildcards[i][0];
                    }
                }
                List<ItemStack[]> inputCombine = generateAllItemInput(inputItems, inputWildcards);
                for (ItemStack[] inputs : inputCombine) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(inputs)
                        .itemOutputs(recipe.mOutput)
                        .fluidInputs(recipe.mFluidInputs)
                        .noOptimize()
                        .eut(recipe.mEUt)
                        .duration(recipe.mDuration)
                        .addTo(GTCMRecipe.AssemblyLineWithoutResearchRecipe);
                }
            }
        }

        TwistSpaceTechnology.LOG.info("Mega Assembly Line Recipes loading complete.");
    }

}
