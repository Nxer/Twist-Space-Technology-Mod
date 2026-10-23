package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.github.bsideup.jabel.Desugar;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;

/** Startup source lookup. Native queries select one result; selectors retain all matches for ambiguity checks. */
final class BottledChaosSourceResolver {

    private final Map<NativeSelector, List<GTRecipe>> matches;
    private final Map<Preparation, List<RecipeData>> prepared = new LinkedHashMap<>();

    BottledChaosSourceResolver(Set<NativeSelector> selectors) {
        matches = scan(selectors);
    }

    @Desugar
    private record Preparation(NativeSelector selector, int roundedChanceBatch) {}

    /** Reuse source data only; each StepRef applies its own substitutions after this lookup. */
    List<RecipeData> preparedSources(NativeSelector selector, int roundedChanceBatch) {
        return prepared.computeIfAbsent(new Preparation(selector, roundedChanceBatch), key -> {
            List<RecipeData> result = new ArrayList<>();
            for (GTRecipe recipe : matches.get(selector))
                result.add(BottledChaosRecipeSupport.prepareSourceRecipe(selector.pool(), recipe, roundedChanceBatch));
            return result;
        });
    }

    /** Conditions are immutable, content-equal all-of lists, independent of a step's transformation rules. */
    @Desugar
    record NativeSelector(RecipeMap<?> pool, List<Object> inputs, List<Object> outputs, int circuit) {

        NativeSelector(RecipeMap<?> pool, Object inputs, Object outputs, int circuit) {
            this(pool, identities(inputs), identities(outputs), circuit);
        }

        private static List<Object> identities(Object value) {
            return value == null ? Collections.emptyList()
                : value instanceof Object[]values ? Collections.unmodifiableList(Arrays.asList(values.clone()))
                    : Collections.singletonList(value);
        }

        @Override
        public String toString() {
            return pool.unlocalizedName + " [inputs=" + inputs + ", outputs=" + outputs + ", circuit=" + circuit + "]";
        }
    }

    /** Each selected pool is scanned once; only selectors belonging to that pool are examined. */
    private static Map<NativeSelector, List<GTRecipe>> scan(Set<NativeSelector> selectors) {
        Map<RecipeMap<?>, List<NativeSelector>> byPool = new LinkedHashMap<>();
        Map<NativeSelector, List<GTRecipe>> matches = new LinkedHashMap<>();
        for (NativeSelector selector : selectors) {
            byPool.computeIfAbsent(selector.pool(), ignored -> new ArrayList<>())
                .add(selector);
            matches.put(selector, new ArrayList<>());
        }
        for (var entry : byPool.entrySet()) {
            for (GTRecipe recipe : entry.getKey()
                .getAllRecipes()) {
                if (!recipe.mEnabled || recipe.mFakeRecipe
                    || recipe.mEUt <= 0
                    || containsFractionalPowder(recipe.mInputs)
                    || containsFractionalPowder(recipe.mOutputs)) continue;
                Set<Integer> circuits = new LinkedHashSet<>();
                Set<Object> inputs = collectIdentities(recipe.mInputs, recipe.mFluidInputs, circuits);
                Set<Object> outputs = collectIdentities(recipe.mOutputs, recipe.mFluidOutputs, null);
                for (NativeSelector selector : entry.getValue()) {
                    if (inputs.containsAll(selector.inputs()) && outputs.containsAll(selector.outputs())
                        && (selector.circuit() < 0 || circuits.contains(selector.circuit())))
                        matches.get(selector)
                            .add(recipe);
                }
            }
        }
        return matches;
    }

    static GTRecipe query(RecipeMap<?> pool, ItemStack[] items, FluidStack... fluids) {
        return pool.findRecipeQuery()
            .items(items)
            .fluids(fluids)
            .find();
    }

    /** Required GT query, including native fallback recipes that are absent from getAllRecipes(). */
    static GTRecipe require(RecipeMap<?> pool, ItemStack[] items, FluidStack... fluids) {
        GTRecipe recipe = query(pool, items, fluids);
        if (recipe == null) throw new IllegalStateException(
            "Missing native source: " + pool.unlocalizedName
                + " [items="
                + Arrays.toString(items)
                + ", fluids="
                + Arrays.toString(fluids)
                + "]");
        return recipe;
    }

    private static Set<Object> collectIdentities(ItemStack[] items, FluidStack[] fluids, Set<Integer> circuits) {
        Set<Object> result = new LinkedHashSet<>();
        if (items != null) for (ItemStack stack : items) {
            if (stack == null || stack.getItem() == null) continue;
            if (circuits != null && GTUtility.isAnyIntegratedCircuit(stack)) circuits.add(stack.getItemDamage());
            else result.add(recipeItemId(stack));
        }
        if (fluids != null) for (FluidStack stack : fluids) {
            if (stack != null && stack.getFluid() != null) result.add(stack.getFluid());
        }
        return result;
    }

    private static boolean containsFractionalPowder(ItemStack[] stacks) {
        if (stacks != null) for (ItemStack stack : stacks) {
            if (RecipeMaterialUtils.isFractionalDust(stack)) return true;
        }
        return false;
    }
}
