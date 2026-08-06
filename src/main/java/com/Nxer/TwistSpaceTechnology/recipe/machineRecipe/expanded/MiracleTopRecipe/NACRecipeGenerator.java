package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe;

import static com.Nxer.TwistSpaceTechnology.common.api.ModItemHandler.ModItem.getModItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addIntegratedCircuitToRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.addRecipeMT;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.convertCircuitRecipeItems;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.mergeSameFluid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.mergeSameItem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.packageCircuitRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeCommon.reduplicateRecipe;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.MiracleTopRecipe.MiracleTopRecipeInitialization.NAC_UNWRAP_RECIPE_MAPS;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.dreammaster.item.NHItemList;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public final class NACRecipeGenerator {

    private NACRecipeGenerator() {}

    public static void load() {
        for (GTRecipe aRecipe : RecipeMaps.nanochipAssemblyMatrixRecipes.getAllRecipes()) {
            GTRecipe unwrappedRecipe = unwrapNACRecipe(aRecipe);
            ItemStack output = unwrappedRecipe.mOutputs[0];
            if (getNACComponent(output) != null) continue;

            GTRecipe generatedRecipe = packageCircuitRecipe(
                new GTRecipe(
                    false,
                    unwrappedRecipe.mInputs,
                    new ItemStack[] { output },
                    null,
                    null,
                    null,
                    null,
                    null,
                    unwrappedRecipe.mFluidInputs,
                    null,
                    unwrappedRecipe.mDuration,
                    unwrappedRecipe.mEUt,
                    0));
            generatedRecipe = convertCircuitRecipeItems(generatedRecipe);
            generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
            addRecipeMT(addIntegratedCircuitToRecipe(generatedRecipe, 16));
        }
    }

    private static ItemStack tryUnwrapNACComponent(ItemStack stack) {
        CircuitComponent component = getNACComponent(stack);
        if (component == null) return stack;

        if (component.isProcessed && component.componentForProcessed != null) {
            component = component.componentForProcessed.get();
        }

        if (component == null || component.realComponent == null) return stack;

        ItemStack realStack = component.realComponent.get();
        if (realStack == null) {
            realStack = switch (component) {
                case ProcessedPicoCircuitCasing -> NHItemList.PikoCircuit.get(1);
                case ProcessedQuantumCircuitCasing -> NHItemList.QuantumCircuit.get(1);
                case ProcessedPlanckCircuitCasing -> getModItem("dreamcraft", "PlanckCircuit", 1);
                default -> null;
            };
        }
        if (realStack == null) return stack;

        return copyAmountUnsafe(stack.stackSize, realStack);
    }

    private static CircuitComponent getNACComponent(ItemStack stack) {
        if (stack == null || stack.getItem() != CircuitComponentFakeItem.INSTANCE) return null;
        return CircuitComponent.tryGetFromFakeStack(stack);
    }

    // Recursively unpack NAC inputs and add their items, fluids and duration to the final recipe.
    private static GTRecipe unwrapNACRecipe(GTRecipe recipe) {
        ArrayList<ItemStack> unwrappedInputs = new ArrayList<>();
        ArrayList<FluidStack> extraFluids = new ArrayList<>();
        int extraDuration = 0;

        if (recipe.mInputs != null) {
            for (ItemStack input : recipe.mInputs) {
                NACUnwrapResult result = unwrapNACInputStack(input, new HashSet<>());
                result.appendTo(unwrappedInputs, extraFluids);
                extraDuration += result.extraDuration;
            }
        }

        ItemStack[] unwrappedOutputs = recipe.mOutputs == null ? null : recipe.mOutputs.clone();
        if (unwrappedOutputs != null) {
            for (int i = 0; i < unwrappedOutputs.length; i++) {
                unwrappedOutputs[i] = tryUnwrapNACComponent(unwrappedOutputs[i]);
            }
        }

        ArrayList<FluidStack> mergedFluids = new ArrayList<>();
        if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
            Collections.addAll(mergedFluids, recipe.mFluidInputs.clone());
        }
        mergedFluids.addAll(extraFluids);

        return new GTRecipe(
            false,
            mergeSameItem(unwrappedInputs.toArray(new ItemStack[0])),
            unwrappedOutputs,
            null,
            null,
            null,
            null,
            null,
            mergeSameFluid(mergedFluids.toArray(new FluidStack[0])),
            null,
            recipe.mDuration + extraDuration,
            recipe.mEUt,
            0);
    }

    private static NACUnwrapResult unwrapNACInputStack(ItemStack stack, HashSet<String> visiting) {
        if (stack == null) return NACUnwrapResult.empty();

        CircuitComponent component = getNACComponent(stack);
        if (component == null) {
            return NACUnwrapResult.of(copyAmountUnsafe(stack.stackSize, stack));
        }

        String visitKey = TST_ItemID.create(stack) + ":" + stack.stackSize;
        if (!visiting.add(visitKey)) {
            return NACUnwrapResult.of(copyAmountUnsafe(stack.stackSize, tryUnwrapNACComponent(stack)));
        }

        try {
            GTRecipe subRecipe = findNACUnwrapRecipe(stack);
            if (subRecipe == null || subRecipe.mOutputs == null || subRecipe.mOutputs.length == 0) {
                return NACUnwrapResult.of(copyAmountUnsafe(stack.stackSize, tryUnwrapNACComponent(stack)));
            }

            int outputAmount = Math.max(1, subRecipe.mOutputs[0].stackSize);
            int multiplier = Math.max(1, stack.stackSize / outputAmount);
            if (stack.stackSize % outputAmount != 0) multiplier++;

            ArrayList<ItemStack> itemInputs = new ArrayList<>();
            ArrayList<FluidStack> fluidInputs = new ArrayList<>();
            // Add the full duration of the internal NAC recipe.
            int duration = subRecipe.mDuration * multiplier;

            if (subRecipe.mInputs != null) {
                for (ItemStack subInput : subRecipe.mInputs) {
                    NACUnwrapResult childResult = unwrapNACInputStack(
                        copyAmountUnsafe(subInput.stackSize * multiplier, subInput),
                        visiting);
                    childResult.appendTo(itemInputs, fluidInputs);
                    duration += childResult.extraDuration;
                }
            }

            if (subRecipe.mFluidInputs != null) {
                for (FluidStack fluid : subRecipe.mFluidInputs) {
                    if (fluid.isFluidEqual(Materials.Lubricant.getFluid(1))) continue;
                    // Add internal NAC fluid inputs to the final recipe at x4 amount.
                    fluidInputs.add(copyAmount(fluid.amount * multiplier * 4, fluid));
                }
            }

            return new NACUnwrapResult(
                mergeSameItem(itemInputs.toArray(new ItemStack[0])),
                mergeSameFluid(fluidInputs.toArray(new FluidStack[0])),
                duration);
        } finally {
            visiting.remove(visitKey);
        }
    }

    private static GTRecipe findNACUnwrapRecipe(ItemStack output) {
        for (RecipeMap<?> recipeMap : NAC_UNWRAP_RECIPE_MAPS) {
            for (GTRecipe recipe : recipeMap.getAllRecipes()) {
                if (recipe.mOutputs != null && recipe.mOutputs.length > 0
                    && GTUtility.areStacksEqual(recipe.mOutputs[0], output)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Desugar
    private record NACUnwrapResult(ItemStack[] itemInputs, FluidStack[] fluidInputs, int extraDuration) {

        private static final NACUnwrapResult EMPTY = new NACUnwrapResult(new ItemStack[0], new FluidStack[0], 0);

        private static NACUnwrapResult empty() {
            return EMPTY;
        }

        private static NACUnwrapResult of(ItemStack stack) {
            return new NACUnwrapResult(
                stack == null ? new ItemStack[0] : new ItemStack[] { stack },
                new FluidStack[0],
                0);
        }

        private void appendTo(List<ItemStack> itemList, List<FluidStack> fluidList) {
            Collections.addAll(itemList, itemInputs);
            Collections.addAll(fluidList, fluidInputs);
        }
    }
}
