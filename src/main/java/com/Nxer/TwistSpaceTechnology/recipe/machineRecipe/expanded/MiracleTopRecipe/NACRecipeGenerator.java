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
import static gregtech.api.util.GTUtility.areStacksEqual;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.dreammaster.item.NHItemList;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTRecipe;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public final class NACRecipeGenerator {

    private NACRecipeGenerator() {}

    public static void load() {
        Map<TST_ItemID, CircuitComponent> realComponents = new LinkedHashMap<>();
        for (CircuitComponent component : CircuitComponent.VALUES) {
            if (component.realComponent == null) continue;
            ItemStack realStack = component.realComponent.get();
            if (realStack != null) realComponents.put(TST_ItemID.create(realStack), component);
        }
        Map<TST_ItemID, NACProducer> producers = buildNACProducerIndex(realComponents);

        for (GTRecipe aRecipe : RecipeMaps.nanochipAssemblyMatrixRecipes.getAllRecipes()) {
            if (aRecipe.mFakeRecipe) continue;
            GTRecipe unwrappedRecipe = unwrapNACRecipe(aRecipe, producers, realComponents);
            if (unwrappedRecipe == null || unwrappedRecipe.mOutputs == null || unwrappedRecipe.mOutputs.length == 0) {
                continue;
            }
            ItemStack output = unwrappedRecipe.mOutputs[0];
            if (getNACComponent(output) != null) continue;

            GTRecipe generatedRecipe = packageCircuitRecipe(
                new GTRecipe(
                    false,
                    unwrappedRecipe.mInputs,
                    unwrappedRecipe.mOutputs,
                    null,
                    null,
                    null,
                    null,
                    null,
                    addNACBoardProcessingFluid(output, unwrappedRecipe.mFluidInputs),
                    null,
                    unwrappedRecipe.mDuration,
                    unwrappedRecipe.mEUt,
                    0));
            generatedRecipe = convertCircuitRecipeItems(generatedRecipe);
            generatedRecipe = reduplicateRecipe(generatedRecipe, 3, 3, 4, 4, 1, 3);
            generatedRecipe = addIntegratedCircuitToRecipe(generatedRecipe, 16);
            generatedRecipe.setRecipeCategory(GTCMRecipe.MiracleTopNACRecipeCategory);
            addRecipeMT(generatedRecipe);
        }
    }

    // Add the board-processing fluid before MT conversion; final cost is base consumption x 64 outputs x 3/4.
    private static FluidStack[] addNACBoardProcessingFluid(ItemStack output, FluidStack[] inputFluids) {
        int amount = areStacksEqual(output, NHItemList.PikoCircuit.get(1)) ? 100
            : areStacksEqual(output, NHItemList.QuantumCircuit.get(1)) ? 400
                : areStacksEqual(output, NHItemList.PlanckCircuit.get(1)) ? 1600 : 0;
        if (amount == 0) return inputFluids;

        ArrayList<FluidStack> fluids = new ArrayList<>();
        if (inputFluids != null) Collections.addAll(fluids, inputFluids);
        fluids.add(MaterialPool.EntropicFlux.getFluidOrGas(amount * output.stackSize));
        return mergeSameFluid(fluids.toArray(new FluidStack[0]));
    }

    // Index every real NAC output so recipes with several outputs are expanded only once.
    // Optical matrix recipes have a 50% chance to return a circuit that still needs packaging. MT always uses the
    // packaged result like other circuit tiers, then lets the normal unwrapping path handle it.
    private static Map<TST_ItemID, NACProducer> buildNACProducerIndex(
        Map<TST_ItemID, CircuitComponent> realComponents) {
        Map<TST_ItemID, NACProducer> producers = new LinkedHashMap<>();

        for (RecipeMap<?> recipeMap : NAC_UNWRAP_RECIPE_MAPS) {
            for (GTRecipe recipe : recipeMap.getAllRecipes()) {
                if (recipe.mFakeRecipe || recipe.mOutputs == null) continue;
                for (ItemStack output : recipe.mOutputs) {
                    CircuitComponent component = getNACComponent(output);
                    if (component != null && component.xorResult != null) continue;
                    producers.putIfAbsent(TST_ItemID.create(output), new NACProducer(recipe, false));
                }
            }
        }

        // Matrix recipes are normally nesting boundaries. Only internal outputs without a real item are expanded.
        for (GTRecipe recipe : RecipeMaps.nanochipAssemblyMatrixRecipes.getAllRecipes()) {
            if (recipe.mFakeRecipe || recipe.mOutputs == null) continue;
            for (ItemStack output : recipe.mOutputs) {
                if (getNACComponent(output) == null) continue;
                ItemStack realOutput = tryUnwrapNACComponent(output);
                if (realOutput != null && realOutput.getItem() == CircuitComponentFakeItem.INSTANCE) {
                    producers.putIfAbsent(TST_ItemID.create(output), new NACProducer(recipe, false));
                }
            }
        }

        HashSet<TST_ItemID> bridgeOutputs = new HashSet<>();
        bridgeOutputs.add(TST_ItemID.create(ItemList.RealizedQuantumCircuitRack.get(1)));
        bridgeOutputs.add(TST_ItemID.create(ItemList.UnboundWDMStrands.get(1)));
        bridgeOutputs.add(TST_ItemID.create(ItemList.IsolatedBDMStrands.get(1)));

        RecipeMap<?>[] bridgeRecipeMaps = { RecipeMaps.autoclaveRecipes, RecipeMaps.centrifugeNonCellRecipes };
        for (RecipeMap<?> recipeMap : bridgeRecipeMaps) {
            for (GTRecipe recipe : recipeMap.getAllRecipes()) {
                if (recipe.mFakeRecipe || recipe.mOutputs == null) continue;
                for (ItemStack output : recipe.mOutputs) {
                    if (!bridgeOutputs.contains(TST_ItemID.create(output))) continue;
                    ItemStack normalizedOutput = normalizeExternalNACComponent(output, realComponents);
                    producers.put(TST_ItemID.create(normalizedOutput), new NACProducer(recipe, true));
                }
            }
        }

        return producers;
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

    // Expand the complete NAC chain while sharing byproducts between every input branch.
    private static GTRecipe unwrapNACRecipe(GTRecipe recipe, Map<TST_ItemID, NACProducer> producers,
        Map<TST_ItemID, CircuitComponent> realComponents) {
        NACExpansionContext context = new NACExpansionContext(producers, realComponents);

        if (recipe.mInputs != null) {
            for (ItemStack input : recipe.mInputs) {
                expandNACInput(input, context);
                if (!context.valid) return null;
            }
        }

        ArrayList<ItemStack> unwrappedOutputs = new ArrayList<>();
        if (recipe.mOutputs != null) {
            for (ItemStack output : recipe.mOutputs) {
                unwrappedOutputs.add(tryUnwrapNACComponent(output));
            }
        }
        // Keep unused byproducts as outputs when upstream NAC recipe ratios change.
        for (Map.Entry<TST_ItemID, Integer> entry : context.availableItems.entrySet()) {
            if (entry.getValue() <= 0) continue;
            unwrappedOutputs.add(tryUnwrapNACComponent(entry.getKey()
                .getItemStack(entry.getValue())));
        }

        ArrayList<FluidStack> mergedFluids = new ArrayList<>();
        if (recipe.mFluidInputs != null && recipe.mFluidInputs.length > 0) {
            Collections.addAll(mergedFluids, recipe.mFluidInputs.clone());
        }
        mergedFluids.addAll(context.extraFluids);

        return new GTRecipe(
            false,
            mergeSameItem(context.itemInputs.toArray(new ItemStack[0])),
            mergeSameItem(unwrappedOutputs.toArray(new ItemStack[0])),
            null,
            null,
            null,
            null,
            null,
            mergeSameFluid(mergedFluids.toArray(new FluidStack[0])),
            null,
            recipe.mDuration + context.extraDuration,
            recipe.mEUt,
            0);
    }

    // Satisfy one input from shared byproducts before running its producer recipe again.
    private static void expandNACInput(ItemStack stack, NACExpansionContext context) {
        if (stack == null || !context.valid) return;

        TST_ItemID inputId = TST_ItemID.create(stack);
        int required = stack.stackSize;
        int available = context.availableItems.getOrDefault(inputId, 0);
        if (available > 0) {
            int consumed = Math.min(required, available);
            required -= consumed;
            if (consumed == available) {
                context.availableItems.remove(inputId);
            } else {
                context.availableItems.put(inputId, available - consumed);
            }
        }
        if (required <= 0) return;

        NACProducer producer = context.producers.get(inputId);
        if (producer == null) {
            ItemStack unwrappedStack = tryUnwrapNACComponent(copyAmountUnsafe(required, stack));
            // if (getNACComponent(unwrappedStack) != null) {
            // context.valid = false;
            // return;
            // }
            context.itemInputs.add(unwrappedStack);
            return;
        }

        if (!context.expanding.add(inputId)) {
            context.valid = false;
            return;
        }
        try {
            int outputAmount = 0;
            for (ItemStack output : producer.recipe.mOutputs) {
                if (output == null) continue;
                ItemStack normalizedOutput = producer.external
                    ? normalizeExternalNACComponent(output, context.realComponents)
                    : output;
                if (TST_ItemID.create(normalizedOutput)
                    .equals(inputId)) outputAmount += output.stackSize;
            }
            if (outputAmount <= 0) {
                context.valid = false;
                return;
            }
            int multiplier = (required - 1) / outputAmount + 1;

            if (producer.recipe.mInputs != null) {
                for (ItemStack input : producer.recipe.mInputs) {
                    ItemStack normalizedInput = producer.external
                        ? normalizeExternalNACComponent(input, context.realComponents)
                        : input;
                    expandNACInput(copyAmountUnsafe(input.stackSize * multiplier, normalizedInput), context);
                    if (!context.valid) return;
                }
            }

            if (producer.recipe.mFluidInputs != null) {
                for (FluidStack fluid : producer.recipe.mFluidInputs) {
                    if (fluid.isFluidEqual(Materials.Lubricant.getFluid(1))) continue;
                    int amountMultiplier = producer.external ? multiplier : multiplier * 4;
                    context.extraFluids.add(copyAmount(fluid.amount * amountMultiplier, fluid));
                }
            }
            context.extraDuration += producer.recipe.mDuration * multiplier;

            for (ItemStack output : producer.recipe.mOutputs) {
                ItemStack normalizedOutput = producer.external
                    ? normalizeExternalNACComponent(output, context.realComponents)
                    : output;
                TST_ItemID outputId = TST_ItemID.create(normalizedOutput);
                context.availableItems.merge(outputId, output.stackSize * multiplier, Integer::sum);
            }

            int remaining = context.availableItems.getOrDefault(inputId, 0) - required;
            if (remaining < 0) {
                context.valid = false;
            } else if (remaining == 0) {
                context.availableItems.remove(inputId);
            } else {
                context.availableItems.put(inputId, remaining);
            }
        } finally {
            context.expanding.remove(inputId);
        }
    }

    // Convert real bridge items back to their NAC component form so expansion can continue.
    private static ItemStack normalizeExternalNACComponent(ItemStack stack,
        Map<TST_ItemID, CircuitComponent> realComponents) {
        CircuitComponent component = realComponents.get(TST_ItemID.create(stack));
        return component == null ? stack : component.getFakeStack(stack.stackSize);
    }

    @Desugar
    private record NACProducer(GTRecipe recipe, boolean external) {}

    private static final class NACExpansionContext {

        private final Map<TST_ItemID, NACProducer> producers;
        private final Map<TST_ItemID, CircuitComponent> realComponents;
        private final Map<TST_ItemID, Integer> availableItems = new LinkedHashMap<>();
        private final ArrayList<ItemStack> itemInputs = new ArrayList<>();
        private final ArrayList<FluidStack> extraFluids = new ArrayList<>();
        private final HashSet<TST_ItemID> expanding = new HashSet<>();
        private int extraDuration;
        private boolean valid = true;

        private NACExpansionContext(Map<TST_ItemID, NACProducer> producers,
            Map<TST_ItemID, CircuitComponent> realComponents) {
            this.producers = producers;
            this.realComponents = realComponents;
        }
    }
}
