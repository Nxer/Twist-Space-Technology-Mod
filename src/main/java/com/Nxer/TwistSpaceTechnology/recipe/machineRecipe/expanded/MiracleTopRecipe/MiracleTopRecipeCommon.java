package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.circuitGTOreDict;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.circuitItemWrappedMap;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.recipeComparisonWhitelist;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.specialMaterialCantAutoModify;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.superConductorMaterialList;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.targetModifyOreDict;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import bartworks.util.BWUtil;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

public final class MiracleTopRecipeCommon {

    private MiracleTopRecipeCommon() {}

    private static final RecipeMap<?> MT = GTCMRecipe.MiracleTopRecipes;
    private static final Map<RecipeOutputKey, List<GTRecipe>> MT_RECIPE_CACHE = new LinkedHashMap<>();

    // Groups recipes by output item types without using output amounts.
    private static final class RecipeOutputKey {

        private final Set<TST_ItemID> itemOutputs = new HashSet<>();

        private RecipeOutputKey(GTRecipe recipe) {
            if (recipe.mOutputs != null) {
                for (ItemStack stack : recipe.mOutputs) {
                    if (stack != null) itemOutputs.add(TST_ItemID.create(stack));
                }
            }
        }

        @Override
        public boolean equals(Object object) {
            if (this == object) return true;
            if (!(object instanceof RecipeOutputKey)) return false;
            RecipeOutputKey key = (RecipeOutputKey) object;
            return itemOutputs.equals(key.itemOutputs);
        }

        @Override
        public int hashCode() {
            return itemOutputs.hashCode();
        }
    }

    // Wrap circuit parts, multiply other item inputs and fluid inputs by 16, and triple the duration.
    static GTRecipe packageCircuitRecipe(GTRecipe recipe) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        if (recipe.mInputs != null) {
            for (ItemStack stack : recipe.mInputs) {
                ItemStack wrappedStack = wrapCircuitComponent(stack);
                inputItems.add(
                    wrappedStack == null ? copyAmountUnsafe(stack.stackSize * 16, stack)
                        : copyAmountUnsafe(stack.stackSize, wrappedStack));
            }
        }

        return reduplicateRecipe(
            new GTRecipe(
                false,
                inputItems.toArray(new ItemStack[0]),
                recipe.mOutputs == null ? null : recipe.mOutputs.clone(),
                null,
                null,
                null,
                null,
                null,
                recipe.mFluidInputs == null ? null : recipe.mFluidInputs.clone(),
                recipe.mFluidOutputs == null ? null : recipe.mFluidOutputs.clone(),
                recipe.mDuration,
                recipe.mEUt,
                0),
            1,
            16,
            16,
            1,
            1,
            3);
    }

    // Get the wrapped item for a circuit part without changing its amount.
    private static ItemStack wrapCircuitComponent(ItemStack stack) {
        if (stack == null) return null;
        ItemStack wrapped = circuitItemWrappedMap.get(TST_ItemID.create(stack));
        return wrapped == null ? null : wrapped.copy();
    }

    // Convert supported GT parts into fluids and superconductors into wireGt16 inputs.
    static GTRecipe convertCircuitRecipeItems(GTRecipe recipe) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();
        if (recipe.mFluidInputs != null) Collections.addAll(inputFluids, recipe.mFluidInputs);

        if (recipe.mInputs != null) {
            for (ItemStack stack : recipe.mInputs) {
                boolean converted = false;
                boolean checkSpecialMaterial = true;

                if (BWUtil.checkStackAndPrefix(stack)) {
                    ItemData data = Objects.requireNonNull(GTOreDictUnificator.getAssociation(stack));
                    Materials material = data.mMaterial.mMaterial;
                    OrePrefixes prefix = data.mPrefix;

                    if (material.getMolten(1) != null && targetModifyOreDict.contains(prefix)) {
                        FluidStack convertedFluid = material
                            .getMolten(prefix.getMaterialAmount() * INGOTS * stack.stackSize / GTValues.M);
                        if (convertedFluid.isFluidEqual(Materials.Copper.getMolten(1))) {
                            convertedFluid = Materials.AnnealedCopper.getMolten(convertedFluid.amount);
                        } else if (convertedFluid.isFluidEqual(Materials.TengamAttuned.getMolten(1))) {
                            convertedFluid = Materials.TengamPurified.getMolten(convertedFluid.amount);
                        }
                        inputFluids.add(convertedFluid);
                        converted = true;
                    } else if (superConductorMaterialList.contains(material) && prefix != OrePrefixes.circuit) {
                        // Calculate from the x16 input in one formula to avoid rounding loss.
                        inputItems.add(
                            copyAmountUnsafe(
                                (int) (prefix.getMaterialAmount() * stack.stackSize * 2 / (GTValues.M * 16)),
                                GTOreDictUnificator.get(OrePrefixes.wireGt16, material, 1)));
                        converted = true;
                    }
                    checkSpecialMaterial = false;
                }

                if (!converted && checkSpecialMaterial) {
                    for (Map.Entry<ItemStack, FluidStack> entry : specialMaterialCantAutoModify.entrySet()) {
                        if (GTUtility.areStacksEqual(entry.getKey(), stack)) {
                            FluidStack convertedFluid = copyAmount(
                                entry.getValue().amount * stack.stackSize,
                                entry.getValue());
                            inputFluids.add(convertedFluid);
                            converted = true;
                            break;
                        }
                    }
                }

                if (!converted) inputItems.add(stack.copy());
            }
        }

        return new GTRecipe(
            false,
            mergeSameItem(inputItems.toArray(new ItemStack[0])),
            recipe.mOutputs == null ? null : recipe.mOutputs.clone(),
            null,
            null,
            null,
            null,
            null,
            mergeSameFluid(inputFluids.toArray(new FluidStack[0])),
            recipe.mFluidOutputs == null ? null : recipe.mFluidOutputs.clone(),
            recipe.mDuration,
            recipe.mEUt,
            0);
    }

    // Multiply item amounts, fluid amounts, EU/t and duration by the given values.
    static GTRecipe reduplicateRecipe(GTRecipe oRecipe, int inputItemMultiTimes, int inputFluidMultiTimes,
        int outputItemMultiTimes, int outputFluidMultiTimes, int eutMultiTimes, int durationMultiTimes) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        if (oRecipe == null) return null;

        if (oRecipe.mInputs != null) {
            for (ItemStack aStack : oRecipe.mInputs) {
                inputItems.add(copyAmountUnsafe(aStack.stackSize * inputItemMultiTimes, aStack));
            }
        }
        if (oRecipe.mFluidInputs != null) {
            for (FluidStack aStack : oRecipe.mFluidInputs) {
                inputFluids.add(copyAmount(aStack.amount * inputFluidMultiTimes, aStack));
            }
        }

        if (oRecipe.mOutputs != null) {
            for (ItemStack aStack : oRecipe.mOutputs) {
                outputItems.add(copyAmountUnsafe(aStack.stackSize * outputItemMultiTimes, aStack));
            }
        }
        if (oRecipe.mFluidOutputs != null) {
            for (FluidStack aStack : oRecipe.mFluidOutputs) {
                outputFluids.add(copyAmount(aStack.amount * outputFluidMultiTimes, aStack));
            }
        }

        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            outputItems.toArray(new ItemStack[0]),
            null,
            null,
            null,
            null,
            null,
            inputFluids.toArray(new FluidStack[0]),
            outputFluids.toArray(new FluidStack[0]),
            oRecipe.mDuration * durationMultiTimes,
            oRecipe.mEUt * eutMultiTimes,
            0);
    }

    // Add the programming circuit as the first item input.
    static GTRecipe addIntegratedCircuitToRecipe(GTRecipe oRecipe, int circuitNum) {
        ArrayList<ItemStack> inputItems = new ArrayList<>();
        inputItems.add(GTUtility.getIntegratedCircuit(circuitNum));

        if (oRecipe == null) return null;
        Collections.addAll(inputItems, oRecipe.mInputs);

        return new GTRecipe(
            false,
            inputItems.toArray(new ItemStack[0]),
            oRecipe.mOutputs,
            null,
            null,
            null,
            null,
            null,
            oRecipe.mFluidInputs,
            oRecipe.mFluidOutputs,
            oRecipe.mDuration,
            oRecipe.mEUt,
            0);
    }

    // Combine equal fluids and add their amounts.
    static FluidStack[] mergeSameFluid(FluidStack[] fluidStacks) {

        Map<Fluid, Integer> fluidMap = new LinkedHashMap<>();

        for (FluidStack aStack : fluidStacks) {
            fluidMap.put(aStack.getFluid(), fluidMap.getOrDefault(aStack.getFluid(), 0) + aStack.amount);
        }

        ArrayList<FluidStack> mergedList = new ArrayList<>();
        for (Map.Entry<Fluid, Integer> entry : fluidMap.entrySet()) {
            mergedList.add(new FluidStack(entry.getKey(), entry.getValue()));
        }

        return mergedList.toArray(new FluidStack[0]);
    }

    // Combine equal items with the same Meta and NBT, then add their amounts.
    static ItemStack[] mergeSameItem(ItemStack[] itemStacks) {

        Map<TST_ItemID, ItemStack> itemMap = new LinkedHashMap<>();

        for (ItemStack aStack : itemStacks) {
            if (aStack == null) continue;
            TST_ItemID key = TST_ItemID.create(aStack);
            ItemStack mergedStack = itemMap.get(key);
            if (mergedStack == null) {
                itemMap.put(key, aStack.copy());
            } else {
                mergedStack.stackSize += aStack.stackSize;
            }
        }

        ArrayList<ItemStack> mergedList = new ArrayList<>(itemMap.values());

        return mergedList.toArray(new ItemStack[0]);
    }

    // Clear queued MT recipes before scanning recipe maps.
    static void clearMTRecipeCache() {
        MT_RECIPE_CACHE.clear();
    }

    // Register queued MT recipes and clear the cache.
    static void flushMTRecipeCache() {
        ArrayList<GTRecipe> recipes = new ArrayList<>();
        for (List<GTRecipe> recipeGroup : MT_RECIPE_CACHE.values()) {
            recipes.addAll(recipeGroup);
        }
        // MiracleTopRecipeExporter.export(recipes); // Test
        for (GTRecipe recipe : recipes) {
            TST_RecipeBuilder.builder()
                .itemInputs(recipe.mInputs)
                .fluidInputs(recipe.mFluidInputs)
                .itemOutputs(recipe.mOutputs)
                .eut(recipe.mEUt)
                .duration(recipe.mDuration)
                .addTo(MT);
        }
        MT_RECIPE_CACHE.clear();
    }

    // Compare recipes with the same output, unless the output is listed to keep every recipe.
    static void addRecipeMT(GTRecipe aRecipe) {
        if (aRecipe == null) return;

        RecipeOutputKey outputKey = new RecipeOutputKey(aRecipe);
        List<GTRecipe> recipeGroup = MT_RECIPE_CACHE.computeIfAbsent(outputKey, key -> new ArrayList<>());
        for (TST_ItemID output : outputKey.itemOutputs) {
            if (recipeComparisonWhitelist.contains(output)) {
                recipeGroup.add(aRecipe);
                return;
            }
        }

        ArrayList<Integer> matchingRecipes = new ArrayList<>();
        GTRecipe preferredRecipe = aRecipe;
        for (int i = 0; i < recipeGroup.size(); i++) {
            GTRecipe cachedRecipe = recipeGroup.get(i);
            if (!haveMostlySameInputs(aRecipe, cachedRecipe)) continue;

            matchingRecipes.add(i);
            if (cachedRecipe.mDuration < preferredRecipe.mDuration
                || cachedRecipe.mDuration == preferredRecipe.mDuration && cachedRecipe.mEUt >= preferredRecipe.mEUt) {
                preferredRecipe = cachedRecipe;
            }
        }
        if (matchingRecipes.isEmpty()) {
            recipeGroup.add(aRecipe);
            return;
        }

        int firstMatch = matchingRecipes.get(0);
        for (int i = matchingRecipes.size() - 1; i > 0; i--) {
            recipeGroup.remove((int) matchingRecipes.get(i));
        }
        recipeGroup.set(firstMatch, preferredRecipe);
    }

    // Ignore amounts and programming circuits, then check whether more than half of the input types match.
    private static boolean haveMostlySameInputs(GTRecipe firstRecipe, GTRecipe secondRecipe) {
        Set<TST_ItemID> firstItems = new HashSet<>();
        Set<TST_ItemID> secondItems = new HashSet<>();
        Set<Fluid> firstFluids = new HashSet<>();
        Set<Fluid> secondFluids = new HashSet<>();

        if (firstRecipe.mInputs != null) {
            for (ItemStack stack : firstRecipe.mInputs) {
                if (stack.getItem() != ItemList.Circuit_Integrated.getItem()) {
                    firstItems.add(TST_ItemID.create(stack));
                }
            }
        }
        if (secondRecipe.mInputs != null) {
            for (ItemStack stack : secondRecipe.mInputs) {
                if (stack.getItem() != ItemList.Circuit_Integrated.getItem()) {
                    secondItems.add(TST_ItemID.create(stack));
                }
            }
        }
        if (firstRecipe.mFluidInputs != null) {
            for (FluidStack stack : firstRecipe.mFluidInputs) {
                firstFluids.add(stack.getFluid());
            }
        }
        if (secondRecipe.mFluidInputs != null) {
            for (FluidStack stack : secondRecipe.mFluidInputs) {
                secondFluids.add(stack.getFluid());
            }
        }

        Set<TST_ItemID> firstSuperconductors = new HashSet<>();
        Set<TST_ItemID> secondSuperconductors = new HashSet<>();
        for (TST_ItemID item : firstItems) {
            if (isSuperconductor(item)) firstSuperconductors.add(item);
        }
        for (TST_ItemID item : secondItems) {
            if (isSuperconductor(item)) secondSuperconductors.add(item);
        }

        // Recipes that differ only by their superconductor remain separate choices.
        Set<TST_ItemID> firstNonSuperconductors = new HashSet<>(firstItems);
        Set<TST_ItemID> secondNonSuperconductors = new HashSet<>(secondItems);
        firstNonSuperconductors.removeAll(firstSuperconductors);
        secondNonSuperconductors.removeAll(secondSuperconductors);
        if (!firstSuperconductors.isEmpty() && !secondSuperconductors.isEmpty()
            && !firstSuperconductors.equals(secondSuperconductors)
            && firstNonSuperconductors.equals(secondNonSuperconductors)
            && firstFluids.equals(secondFluids)) {
            return false;
        }

        // Keep the silicone rubber and SBR versions when that is the only fluid difference.
        if (firstItems.equals(secondItems)) {
            Set<Fluid> firstDifferentFluids = new HashSet<>(firstFluids);
            Set<Fluid> secondDifferentFluids = new HashSet<>(secondFluids);
            firstDifferentFluids.removeAll(secondFluids);
            secondDifferentFluids.removeAll(firstFluids);
            if (firstDifferentFluids.size() == 1 && secondDifferentFluids.size() == 1) {
                Fluid firstFluid = firstDifferentFluids.iterator()
                    .next();
                Fluid secondFluid = secondDifferentFluids.iterator()
                    .next();
                Fluid siliconeRubber = Materials.RubberSilicone.getMolten(1)
                    .getFluid();
                Fluid styreneButadieneRubber = Materials.StyreneButadieneRubber.getMolten(1)
                    .getFluid();
                if (firstFluid == siliconeRubber && secondFluid == styreneButadieneRubber
                    || firstFluid == styreneButadieneRubber && secondFluid == siliconeRubber) {
                    return false;
                }
            }
        }

        Set<TST_ItemID> commonItems = new HashSet<>(firstItems);
        commonItems.retainAll(secondItems);
        Set<Fluid> commonFluids = new HashSet<>(firstFluids);
        commonFluids.retainAll(secondFluids);

        // Different superconductors count as the same input when other inputs also differ.
        Set<TST_ItemID> unmatchedFirstSuperconductors = new HashSet<>(firstSuperconductors);
        Set<TST_ItemID> unmatchedSecondSuperconductors = new HashSet<>(secondSuperconductors);
        unmatchedFirstSuperconductors.removeAll(commonItems);
        unmatchedSecondSuperconductors.removeAll(commonItems);
        int matchingInputTypes = commonItems.size() + commonFluids.size()
            + Math.min(unmatchedFirstSuperconductors.size(), unmatchedSecondSuperconductors.size());
        int largestInputTypeCount = Math
            .max(firstItems.size() + firstFluids.size(), secondItems.size() + secondFluids.size());
        return largestInputTypeCount == 0 || matchingInputTypes * 2 > largestInputTypeCount;
    }

    // Check whether an item uses one of the supported superconductor materials.
    private static boolean isSuperconductor(TST_ItemID item) {
        ItemStack stack = item.getItemStack();
        if (!BWUtil.checkStackAndPrefix(stack)) return false;
        ItemData data = GTOreDictUnificator.getAssociation(stack);
        return data != null && superConductorMaterialList.contains(data.mMaterial.mMaterial);
    }

    static void addGTCircuitOreDictNames(ItemStack stack) {
        if (stack == null) return;

        for (int oreId : OreDictionary.getOreIDs(stack)) {
            circuitGTOreDict.add(OreDictionary.getOreName(oreId));
        }
    }

    public static String getCircuitOreDict(ItemStack stack) {
        if (stack == null) return null;

        if (circuitGTOreDict.isEmpty()) {
            MiracleTopRecipeInitialization.initializeGTCircuitOreDict();
        }

        for (int oreId : OreDictionary.getOreIDs(stack)) {
            String oreName = OreDictionary.getOreName(oreId);
            if (circuitGTOreDict.contains(oreName)) return oreName;
        }

        return null;
    }

    static boolean hasCircuitOreDict(ItemStack stack) {
        return getCircuitOreDict(stack) != null;
    }

}
