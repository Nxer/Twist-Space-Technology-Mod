package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getGCD;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getLCM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosItemInputs.Requirement;
import com.Nxer.TwistSpaceTechnology.util.recipes.RecipeMaterialUtils;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.CentrifugeRecipeKey;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipe.RecipeItemInput;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEQuantumForceTransformer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;
import tectech.recipe.TecTechRecipeMaps;

/** Bottled Chaos recipe preprocessing and source-machine duration calculations. */
final class BottledChaosRecipeSupport {

    // Core source preprocessing and runtime duration operations.
    /**
     * Converts one live recipe into exact whole-batch data. Item and fluid chances use one shared LCM batch;
     * programming circuits are selectors, and zero-sized item inputs remain unscaled catalysts.
     */
    static RecipeData prepareSourceRecipe(RecipeMap<?> map, GTRecipe recipe) {
        return prepareSourceRecipe(map, recipe, 0);
    }

    static RecipeData prepareSourceRecipe(RecipeMap<?> map, GTRecipe recipe, int roundedOutputChanceBatch) {
        Objects.requireNonNull(map, "recipe map");
        Objects.requireNonNull(recipe, "recipe");
        if (recipe.mDuration <= 0 || recipe.mEUt <= 0) {
            throw new IllegalArgumentException("invalid source recipe duration or power");
        }
        // Read source requirements before GTRecipe.copy() drops metadata.
        SourceRequirements requirements = sourceRequirements(map, recipe);
        Map<Object, Long> catalysts = new LinkedHashMap<>();

        // Use 1000x fluid efficiency for half-full bacterial vats.
        if (map == BottledChaosRecipeResources.RecipePools.BacterialVat) {
            recipe = recipe.copy();
            recipe.mFluidInputs[0].amount = Math.multiplyExact(recipe.mFluidInputs[0].amount, 1000);
            recipe.mFluidOutputs[0].amount = Math.multiplyExact(recipe.mFluidOutputs[0].amount, 1000);
        }
        // Native QFT assigns the same base chance to every item and fluid output, without focusing or plasma.
        if (map == BottledChaosRecipeResources.RecipePools.QuantumForceTransformer) {
            catalysts.merge(Objects.requireNonNull(requirements.specialInput(), "QFT catalyst"), 1L, Math::max);
            recipe = recipe.copy();
            int chance = MTEQuantumForceTransformer.getBaseOutputChance(recipe);
            recipe.mOutputChances = new int[recipe.mOutputs.length];
            recipe.mFluidOutputChances = new int[recipe.mFluidOutputs.length];
            Arrays.fill(recipe.mOutputChances, chance);
            Arrays.fill(recipe.mFluidOutputChances, chance);
        }
        if (map == BottledChaosRecipeResources.RecipePools.Milling) {
            recipe = recipe.copy();
            for (ItemStack input : recipe.mInputs) {
                if (MTEIsaMill.isMillingBall(input)) {
                    input.stackSize = 0;
                    input.setTagCompound(null);
                }
            }
        }
        long probabilityBatch = probabilityBatch(recipe, roundedOutputChanceBatch);
        Map<Object, Long> inputs = new LinkedHashMap<>();
        Map<Object, Long> outputs = new LinkedHashMap<>();
        List<Requirement> itemRequirements = new ArrayList<>();
        RecipeItemInput[] nativeInputs = recipe.copy()
            .getCachedCombinedItemInputs();

        // Scale all four chance arrays into deterministic whole-batch resource totals.
        addItems(
            inputs,
            catalysts,
            recipe.mInputs,
            recipe.mInputChances,
            probabilityBatch,
            0,
            itemRequirements,
            nativeInputs);
        addFluids(inputs, recipe.mFluidInputs, recipe.mFluidInputChances, probabilityBatch);
        addItems(
            outputs,
            null,
            recipe.mOutputs,
            recipe.mOutputChances,
            probabilityBatch,
            roundedOutputChanceBatch,
            null,
            null);
        addFluids(outputs, recipe.mFluidOutputs, recipe.mFluidOutputChances, probabilityBatch);
        // Culture dishes are non-consumed special-slot requirements in native bacterial-vat recipes.
        if (map == BottledChaosRecipeResources.RecipePools.BacterialVat && requirements.specialInput() != null) {
            catalysts.merge(requirements.specialInput(), 1L, Math::max);
            itemRequirements.add(BottledChaosItemInputs.exact(requirements.specialInput(), 0));
        }
        if (map == BottledChaosRecipeResources.RecipePools.QuantumForceTransformer)
            itemRequirements.add(BottledChaosItemInputs.exact(requirements.specialInput(), 0));
        if (inputs.isEmpty() || outputs.isEmpty() || inputs.equals(outputs)) {
            throw new IllegalStateException("invalid marked recipe");
        }

        // Keep raw source work grouped by machine and EU/t for runtime overclock normalization.
        Map<TimeKey, Long> times = new LinkedHashMap<>();
        times.put(
            new TimeKey(map, recipe.mEUt, recipe.mDuration, requirements),
            Math.multiplyExact((long) recipe.mDuration, probabilityBatch));
        return new RecipeData(inputs, outputs, catalysts, times, itemRequirements);
    }

    private static SourceRequirements sourceRequirements(RecipeMap<?> map, GTRecipe recipe) {
        int heat = recipe.getMetadataOrDefault(GTRecipeConstants.COIL_HEAT, 0);
        if (map == RecipeMaps.blastFurnaceRecipes || map == RecipeMaps.vacuumFurnaceRecipes
            || map == RecipeMaps.alloyBlastSmelterRecipes) heat = Math.max(heat, recipe.mSpecialValue);
        int casing = 0;
        if (map == RecipeMaps.chemicalPlantRecipes)
            casing = recipe.getMetadataOrDefault(GTRecipeConstants.CHEMPLANT_CASING_TIER, recipe.mSpecialValue);
        if (map == GoodGeneratorRecipeMaps.preciseAssemblerRecipes)
            casing = recipe.getMetadataOrDefault(GTRecipeConstants.PRECISE_ASSEMBLER_CASING_TIER, recipe.mSpecialValue);
        if (map == BottledChaosRecipeResources.RecipePools.QuantumForceTransformer) casing = recipe.mSpecialValue;
        if (map == BottledChaosRecipeResources.RecipePools.FusionCrafting)
            casing = recipe.getMetadataOrDefault(GTRecipeConstants.DEFC_CASING_TIER, 1);
        if (map == GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes)
            casing = recipe.getMetadataOrDefault(GTRecipeConstants.NFR_COIL_TIER, recipe.mSpecialValue);
        ItemStack special = map == BottledChaosRecipeResources.RecipePools.QuantumForceTransformer
            ? recipe.getMetadata(GTRecipeConstants.QFT_CATALYST)
            : map == BottledChaosRecipeResources.RecipePools.BacterialVat
                && recipe.mSpecialItems instanceof ItemStack culture ? culture : null;
        return new SourceRequirements(
            Math.max(0, heat),
            recipe.getMetadataOrDefault(GTRecipeConstants.GLASS, 0),
            Math.max(0, casing),
            recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0),
            recipe.getMetadataOrDefault(CentrifugeRecipeKey.INSTANCE, false),
            special == null ? null : recipeItemId(special.copy()),
            map == TecTechRecipeMaps.godforgePlasmaRecipes
                ? recipe.getMetadataOrDefault(GTRecipeConstants.FOG_PLASMA_TIER, 0)
                : 0,
            map == TecTechRecipeMaps.godforgePlasmaRecipes
                && recipe.getMetadataOrDefault(GTRecipeConstants.FOG_PLASMA_MULTISTEP, false));
    }

    /** Average actual normalized costs, rather than rounding a fractional expected overclock count. */
    public static double calculateNormalizedDuration(long duration, long recipeEUt, long targetEUt, long parallelLimit,
        double speedMultiplier, double euMultiplier, double timeDivisor, double powerMultiplier, int maximumOverclocks,
        int heatOverclocks, int[] overclockLimitAlternatives) {
        if (overclockLimitAlternatives == null || overclockLimitAlternatives.length == 0)
            return calculateNormalizedDuration(
                duration,
                recipeEUt,
                targetEUt,
                parallelLimit,
                speedMultiplier,
                euMultiplier,
                timeDivisor,
                powerMultiplier,
                maximumOverclocks,
                heatOverclocks);
        double mean = 0;
        for (int limit : overclockLimitAlternatives) mean += calculateNormalizedDuration(
            duration,
            recipeEUt,
            targetEUt,
            parallelLimit,
            speedMultiplier,
            euMultiplier,
            timeDivisor,
            powerMultiplier,
            limit,
            heatOverclocks) / overclockLimitAlternatives.length;
        return mean;
    }

    /** Applies source-machine modifiers and whole overclocks, preserving fractional work for composition. */
    public static double calculateNormalizedDuration(long duration, long recipeEUt, long targetEUt, long parallelLimit,
        double speedMultiplier, double euMultiplier, double timeDivisor, double powerMultiplier) {
        return calculateNormalizedDuration(
            duration,
            recipeEUt,
            targetEUt,
            parallelLimit,
            speedMultiplier,
            euMultiplier,
            timeDivisor,
            powerMultiplier,
            -1,
            0);
    }

    public static double calculateNormalizedDuration(long duration, long recipeEUt, long targetEUt, long parallelLimit,
        double speedMultiplier, double euMultiplier, double timeDivisor, double powerMultiplier, int maximumOverclocks,
        int heatOverclocks) {
        if (duration <= 0 || recipeEUt <= 0
            || targetEUt <= 0
            || parallelLimit <= 0
            || !Double.isFinite(speedMultiplier)
            || speedMultiplier <= 0
            || !Double.isFinite(euMultiplier)
            || euMultiplier <= 0
            || !Double.isFinite(timeDivisor)
            || timeDivisor < 1
            || !Double.isFinite(powerMultiplier)
            || powerMultiplier < 1
            || maximumOverclocks < -1
            || heatOverclocks < 0) {
            throw new IllegalArgumentException("invalid overclock parameters");
        }
        // Apply machine speed and EU multipliers before counting available overclocks.
        double modifiedEUt = recipeEUt * euMultiplier;
        double result = duration / speedMultiplier;
        if (!Double.isFinite(modifiedEUt) || modifiedEUt <= 0 || modifiedEUt >= Long.MAX_VALUE) {
            throw new ArithmeticException("recipe power overflow");
        }
        if (!Double.isFinite(result) || result <= 0) throw new ArithmeticException("duration overflow");
        long eut = (long) Math.ceil(modifiedEUt);
        if (powerMultiplier == 1) return result;
        // Reverse overclocks until the recipe fits the target power.
        while (eut > targetEUt) {
            eut = (long) Math.ceil(eut / powerMultiplier);
            result *= timeDivisor;
            if (!Double.isFinite(result)) throw new ArithmeticException("duration overflow");
        }
        int overclocks;
        if (powerMultiplier == 4) {
            // Native GT counts power-limited whole overclocks; recover fractional work from its count.
            overclocks = new OverclockCalculator().setRecipeEUt(eut)
                .setEUt(targetEUt)
                .setAmperageOC(true)
                .setDuration(1)
                .setMaxOverclocks(maximumOverclocks < 0 ? Integer.MAX_VALUE : maximumOverclocks)
                .calculate()
                .getPerformedOverclocks();
        } else {
            overclocks = 0;
            double power = eut;
            while (power <= targetEUt / powerMultiplier && (maximumOverclocks < 0 || overclocks < maximumOverclocks)) {
                power *= powerMultiplier;
                overclocks++;
            }
        }
        double best = Double.POSITIVE_INFINITY;
        for (int count = 0; count <= overclocks; count++) {
            long power = (long) Math.ceil(eut * GTUtility.powInt(powerMultiplier, count));
            if (power <= 0 || power > targetEUt) continue;
            int perfect = Math.min(count, heatOverclocks);
            double candidate = result / GTUtility.powInt(4, perfect) / GTUtility.powInt(timeDivisor, count - perfect);
            // Only replace lossy costs. Never trade available perfect heat OCs for extra parallelism.
            if (timeDivisor < powerMultiplier && count >= heatOverclocks) {
                long parallel = Math.min(parallelLimit, targetEUt / power);
                candidate /= parallel;
            }
            best = Math.min(best, candidate);
        }
        if (!Double.isFinite(best) || best <= 0) throw new ArithmeticException("duration overflow or underflow");
        return best;
    }

    public static double addDuration(double current, double perBatch, long batches) {
        double result = current + perBatch * batches;
        if (!Double.isFinite(result)) throw new ArithmeticException("duration overflow");
        return result;
    }

    // Source probability batches and resource conversion.
    private static long probabilityBatch(GTRecipe recipe, int roundedOutputChanceBatch) {
        long result = Math.max(1, roundedOutputChanceBatch);
        result = includeChances(result, recipe.mInputChances);
        if (roundedOutputChanceBatch == 0) result = includeChances(result, recipe.mOutputChances);
        result = includeChances(result, recipe.mFluidInputChances);
        return includeChances(result, recipe.mFluidOutputChances);
    }

    private static long includeChances(long current, int[] chances) {
        if (chances == null) return current;
        for (int chance : chances) {
            if (chance <= 0 || chance >= 10_000) continue;
            current = getLCM(current, 10_000L / getGCD(10_000L, chance));
        }
        return current;
    }

    private static void addItems(Map<Object, Long> target, Map<Object, Long> catalysts, ItemStack[] stacks,
        int[] chances, long probabilityBatch, int roundedOutputChanceBatch, List<Requirement> requirements,
        RecipeItemInput[] nativeInputs) {
        if (stacks == null) return;
        for (int i = 0; i < stacks.length; i++) {
            ItemStack stack = stacks[i];
            if (stack == null || GTUtility.isAnyIntegratedCircuit(stack)) continue;
            RecipeItemInput nativeInput = null;
            if (requirements != null) {
                for (RecipeItemInput candidate : nativeInputs) {
                    // Cached templates are unified; original temporary recipes may still use another ore variant.
                    if (candidate.matchesRecipe(GTOreDictUnificator.getAssociation(stack), stack)) {
                        nativeInput = candidate;
                        break;
                    }
                }
                if (nativeInput == null) throw new IllegalStateException("missing native item input matcher");
                // Preserve the existing native wear-independent catalyst handling.
                if (MTEChemicalPlant.isCatalyst(stack) || MTEIsaMill.isMillingBall(stack)) {
                    ItemStack template = stack.copy();
                    template.setTagCompound(null);
                    nativeInput = new RecipeItemInput(template, false);
                }
            }
            TST_ItemID key = requirements == null ? recipeItemId(stack.copy())
                : BottledChaosRecipeResources.recipeItemId(stack.copy(), nativeInput.usesNbtMatching);
            if (catalysts != null && stack.stackSize == 0) {
                catalysts.merge(Objects.requireNonNull(key, "item identity"), 1L, Math::max);
                requirements.add(new Requirement(key, 0, Collections.singletonList(nativeInput)));
            } else if (stack.stackSize > 0) {
                long amount = applyChance(stack.stackSize, chances, i, probabilityBatch, roundedOutputChanceBatch);
                if (amount > 0) {
                    target.merge(Objects.requireNonNull(key, "item identity"), amount, Math::addExact);
                    if (requirements != null)
                        requirements.add(new Requirement(key, amount, Collections.singletonList(nativeInput)));
                }
            }
        }
    }

    private static long applyChance(long amount, int[] chances, int index, long probabilityBatch,
        int roundedOutputChanceBatch) {
        int chance = chances == null || index >= chances.length || chances[index] < 0 ? 10_000 : chances[index];
        if (roundedOutputChanceBatch > 0) {
            long perBatch = Math.round(amount * (double) chance * roundedOutputChanceBatch / 10_000);
            return Math.multiplyExact(perBatch, probabilityBatch / roundedOutputChanceBatch);
        }
        return Math.multiplyExact(Math.multiplyExact(amount, probabilityBatch), chance) / 10_000;
    }

    private static void addFluids(Map<Object, Long> target, FluidStack[] stacks, int[] chances, long probabilityBatch) {
        if (stacks == null) return;
        for (int i = 0; i < stacks.length; i++) {
            FluidStack stack = stacks[i];
            if (stack == null || stack.amount <= 0) continue;
            long amount = applyChance(stack.amount, chances, i, probabilityBatch, 0);
            if (amount > 0) target.merge(GTUtility.FluidId.create(stack), amount, Math::addExact);
        }
    }

    // Route composition and its shared amount helpers.
    /** Immutable prepared recipe data; composition always returns a new independent value. */
    static final class RecipeData {

        final Map<Object, Long> inputs;
        final Map<Object, Long> outputs;
        final Map<Object, Long> catalysts;
        final Map<TimeKey, Long> times;
        final List<Requirement> itemRequirements;

        /** Consolidate native small/tiny coproducts into exact full-dust batches without changing source work. */
        RecipeData withFullDustOutputs() {
            Map<Object, Integer> divisors = new LinkedHashMap<>();
            Map<Object, Object> fullDusts = new LinkedHashMap<>();
            long scale = 1;
            for (var output : outputs.entrySet()) {
                if (!(output.getKey() instanceof TST_ItemID item)) continue;
                var association = GTOreDictUnificator.getAssociation(item.getItemStack(1));
                if (association == null) continue;
                int divisor = association.mPrefix == OrePrefixes.dustSmall ? 4
                    : association.mPrefix == OrePrefixes.dustTiny ? 9 : 1;
                if (divisor == 1) continue;
                divisors.put(item, divisor);
                fullDusts.put(item, recipeItemId(RecipeMaterialUtils.nativeDust(association.mMaterial.mMaterial)));
                scale = getLCM(scale, divisor / getGCD(output.getValue(), divisor));
            }
            Map<Object, Long> fullOutputs = new LinkedHashMap<>();
            for (var output : outputs.entrySet()) fullOutputs.merge(
                fullDusts.getOrDefault(output.getKey(), output.getKey()),
                Math.multiplyExact(output.getValue(), scale) / divisors.getOrDefault(output.getKey(), 1),
                Math::addExact);
            return new RecipeData(
                multiplyAmounts(inputs, scale),
                fullOutputs,
                catalysts,
                multiplyAmounts(times, scale),
                BottledChaosItemInputs.scale(itemRequirements, scale, 1, null));
        }

        /** Add a fixed non-consumed selector after material balancing and batch reduction. */
        RecipeData requireCatalyst(Object key) {
            Map<Object, Long> required = new LinkedHashMap<>(catalysts);
            required.put(key, Math.max(1, required.getOrDefault(key, 0L)));
            List<Requirement> items = new ArrayList<>(itemRequirements);
            if (key instanceof TST_ItemID item) items.add(BottledChaosItemInputs.exact(item, 0));
            return new RecipeData(inputs, outputs, required, times, items);
        }

        /** Combine independent routes at fixed batch counts, preserving their native input rules and work. */
        RecipeData combineRecipe(RecipeData other, long ownBatches, long otherBatches) {
            Map<Object, Long> combinedInputs = multiplyAmounts(inputs, ownBatches);
            mergeAmounts(combinedInputs, other.inputs, otherBatches);
            Map<Object, Long> combinedOutputs = multiplyAmounts(outputs, ownBatches);
            mergeAmounts(combinedOutputs, other.outputs, otherBatches);
            Map<Object, Long> combinedCatalysts = new LinkedHashMap<>(catalysts);
            other.catalysts.forEach((key, value) -> combinedCatalysts.merge(key, value, Math::max));
            Map<TimeKey, Long> combinedTimes = multiplyAmounts(times, ownBatches);
            mergeAmounts(combinedTimes, other.times, otherBatches);
            List<Requirement> items = BottledChaosItemInputs.scale(itemRequirements, ownBatches, 1, null);
            items.addAll(BottledChaosItemInputs.scale(other.itemRequirements, otherBatches, 1, null));
            return new RecipeData(combinedInputs, combinedOutputs, combinedCatalysts, combinedTimes, items);
        }

        RecipeData appendBalancedRecipe(RecipeData downstream, Object intermediate) {
            Objects.requireNonNull(downstream, "downstream route");
            Objects.requireNonNull(intermediate, "route intermediate");

            // Scale both sides to the smallest whole batches that exactly balance the connection.
            long produced = requiredAmount(outputs, intermediate, "upstream output");
            long consumed = requiredAmount(downstream.inputs, intermediate, "downstream input");
            long gcd = getGCD(produced, consumed);
            long upstreamScale = consumed / gcd;
            long downstreamScale = produced / gcd;

            Map<Object, Long> combinedInputs = multiplyAmounts(inputs, upstreamScale);
            mergeAmountsExcept(combinedInputs, downstream.inputs, downstreamScale, intermediate);
            Map<Object, Long> combinedOutputs = new LinkedHashMap<>();
            mergeAmountsExcept(combinedOutputs, outputs, upstreamScale, intermediate);
            mergeAmounts(combinedOutputs, downstream.outputs, downstreamScale);

            // Catalysts remain templates; source work follows the exact material batch scaling.
            Map<Object, Long> combinedCatalysts = new LinkedHashMap<>(catalysts);
            for (Map.Entry<Object, Long> entry : downstream.catalysts.entrySet()) {
                combinedCatalysts.merge(entry.getKey(), entry.getValue(), Math::max);
            }
            Map<TimeKey, Long> combinedTimes = multiplyAmounts(times, upstreamScale);
            mergeAmounts(combinedTimes, downstream.times, downstreamScale);
            List<Requirement> items = BottledChaosItemInputs.scale(itemRequirements, upstreamScale, 1, null);
            items.addAll(BottledChaosItemInputs.scale(downstream.itemRequirements, downstreamScale, 1, intermediate));
            return new RecipeData(combinedInputs, combinedOutputs, combinedCatalysts, combinedTimes, items);
        }

        /** Cancel matching materials before deferred reagents reveal the final entry input. */
        RecipeData netInputsAndOutputs() {
            Map<Object, Long> netInputs = new LinkedHashMap<>(inputs);
            Map<Object, Long> netOutputs = new LinkedHashMap<>(outputs);
            cancelMatchingInputsAndOutputs(netInputs, netOutputs);
            return new RecipeData(netInputs, netOutputs, this);
        }

        RecipeData netInputsAndOutputs(Object root) {
            RecipeData result = netInputsAndOutputs();
            if (!result.inputs.containsKey(root)) {
                throw new IllegalStateException("processing root must have positive net consumption: " + root);
            }
            return result;
        }

        /** Reduce by the material GCD; source work uses integer division with accepted tick rounding. */
        RecipeData reduceBatch() {
            long divisor = 0;
            for (long amount : inputs.values()) divisor = getGCD(divisor, amount);
            for (long amount : outputs.values()) divisor = getGCD(divisor, amount);
            for (Requirement input : itemRequirements) if (input.amount > 0) divisor = getGCD(divisor, input.amount);
            if (divisor <= 1) return this;
            Map<TimeKey, Long> reducedTimes = new LinkedHashMap<>();
            for (var entry : times.entrySet())
                reducedTimes.put(entry.getKey(), Math.max(1, entry.getValue() / divisor));
            return new RecipeData(
                divideAmounts(inputs, divisor),
                divideAmounts(outputs, divisor),
                catalysts,
                reducedTimes,
                BottledChaosItemInputs.scale(itemRequirements, 1, divisor, null));
        }

        /** Reuse an already available coproduct before expanding its remaining demand or surplus. */
        RecipeData cancelReturnedResource(Object key) {
            Long input = inputs.get(key);
            Long output = outputs.get(key);
            if (input == null || output == null) return this;
            Map<Object, Long> netInputs = new LinkedHashMap<>(inputs);
            Map<Object, Long> netOutputs = new LinkedHashMap<>(outputs);
            long cancelled = Math.min(input, output);
            if (input == cancelled) netInputs.remove(key);
            else netInputs.put(key, input - cancelled);
            if (output == cancelled) netOutputs.remove(key);
            else netOutputs.put(key, output - cancelled);
            return new RecipeData(netInputs, netOutputs, this);
        }

        /** Convert a returned reagent only up to the other reagent's net demand, in exact whole batches. */
        RecipeData recycleReagent(RecipeData conversion, Object returned, Object needed) {
            RecipeData net = cancelReturnedResource(returned).cancelReturnedResource(needed);
            long supply = net.outputs.getOrDefault(returned, 0L);
            long demand = net.inputs.getOrDefault(needed, 0L);
            if (supply == 0 || demand == 0) return net;
            long consumed = requiredAmount(conversion.inputs, returned, "recycling input");
            long produced = requiredAmount(conversion.outputs, needed, "recycling output");
            boolean useSupply = Math.multiplyExact(supply, produced) <= Math.multiplyExact(demand, consumed);
            long amount = useSupply ? supply : demand;
            long perBatch = useSupply ? consumed : produced;
            long gcd = getGCD(amount, perBatch);
            long routeScale = perBatch / gcd;
            long conversionScale = amount / gcd;
            Map<Object, Long> combinedInputs = multiplyAmounts(net.inputs, routeScale);
            Map<Object, Long> combinedOutputs = multiplyAmounts(net.outputs, routeScale);
            mergeAmounts(combinedInputs, conversion.inputs, conversionScale);
            mergeAmounts(combinedOutputs, conversion.outputs, conversionScale);
            cancelMatchingInputsAndOutputs(combinedInputs, combinedOutputs);
            Map<Object, Long> combinedCatalysts = new LinkedHashMap<>(net.catalysts);
            conversion.catalysts.forEach((key, value) -> combinedCatalysts.merge(key, value, Math::max));
            Map<TimeKey, Long> combinedTimes = multiplyAmounts(net.times, routeScale);
            mergeAmounts(combinedTimes, conversion.times, conversionScale);
            List<Requirement> items = BottledChaosItemInputs.scale(net.itemRequirements, routeScale, 1, null);
            items.addAll(BottledChaosItemInputs.scale(conversion.itemRequirements, conversionScale, 1, null));
            return new RecipeData(
                combinedInputs,
                combinedOutputs,
                combinedCatalysts,
                combinedTimes,
                BottledChaosItemInputs.retain(items, combinedInputs));
        }

        RecipeData withAlternativeInput(Object original, Object alternative, long multiplier) {
            Long amount = inputs.get(original);
            if (amount == null) return null;
            if (amount > Long.MAX_VALUE / multiplier) return null;
            Map<Object, Long> alternativeInputs = new LinkedHashMap<>(inputs);
            alternativeInputs.remove(original);
            alternativeInputs.merge(alternative, Math.multiplyExact(amount, multiplier), Math::addExact);
            List<Requirement> items = new ArrayList<>();
            for (Requirement input : itemRequirements)
                if (input.amount == 0 || !input.key.equals(original)) items.add(input);
            if (alternative instanceof TST_ItemID item)
                items.add(BottledChaosItemInputs.exact(item, Math.multiplyExact(amount, multiplier)));
            return new RecipeData(alternativeInputs, outputs, catalysts, times, items);
        }

        RecipeData(Map<Object, Long> inputs, Map<Object, Long> outputs, Map<Object, Long> catalysts,
            Map<TimeKey, Long> times) {
            this(inputs, outputs, catalysts, times, BottledChaosItemInputs.defaults(inputs, catalysts));
        }

        private RecipeData(Map<Object, Long> inputs, Map<Object, Long> outputs, Map<Object, Long> catalysts,
            Map<TimeKey, Long> times, List<Requirement> itemRequirements) {
            this.inputs = immutableMap(inputs);
            this.outputs = immutableMap(outputs);
            this.catalysts = immutableMap(catalysts);
            this.times = immutableMap(times);
            this.itemRequirements = Collections.unmodifiableList(BottledChaosItemInputs.compact(itemRequirements));
        }

        private RecipeData(Map<Object, Long> inputs, Map<Object, Long> outputs, RecipeData source) {
            this.inputs = immutableMap(inputs);
            this.outputs = immutableMap(outputs);
            this.catalysts = source.catalysts;
            this.times = source.times;
            this.itemRequirements = Collections
                .unmodifiableList(BottledChaosItemInputs.retain(source.itemRequirements, inputs));
        }

        /** Equal registered GT variants may differ only in which native items fulfill their input slots. */
        RecipeData withInputAlternatives(RecipeData variant) {
            if (itemRequirements.size() != variant.itemRequirements.size()) return this;
            List<Requirement> items = new ArrayList<>();
            boolean[] used = new boolean[variant.itemRequirements.size()];
            int changed = 0;
            for (Requirement input : itemRequirements) {
                int selected = -1;
                for (int i = 0; i < used.length; i++) {
                    Requirement candidate = variant.itemRequirements.get(i);
                    if (used[i] || !input.key.equals(candidate.key) || input.amount != candidate.amount) continue;
                    // Ambiguous duplicate slots retain the first complete native recipe instead of mixing constraints.
                    if (selected >= 0) return this;
                    selected = i;
                }
                if (selected < 0) return this;
                used[selected] = true;
                Requirement alternative = variant.itemRequirements.get(selected);
                if (input.sameRules(alternative) && alternative.sameRules(input)) items.add(input);
                else {
                    // Do not turn correlated alternatives in separate slots into a new, unregistered combination.
                    if (++changed > 1) return this;
                    items.add(input.withAlternative(alternative));
                }
            }
            return new RecipeData(inputs, outputs, catalysts, times, items);
        }
    }

    private static long requiredAmount(Map<Object, Long> amounts, Object key, String role) {
        Long amount = amounts.get(key);
        if (amount == null || amount <= 0) throw new IllegalArgumentException("missing " + role + ' ' + key);
        return amount;
    }

    private static <K> Map<K, Long> multiplyAmounts(Map<K, Long> amounts, long factor) {
        Map<K, Long> result = new LinkedHashMap<>();
        for (Map.Entry<K, Long> entry : amounts.entrySet()) {
            result.put(entry.getKey(), Math.multiplyExact(entry.getValue(), factor));
        }
        return result;
    }

    private static <K> Map<K, Long> divideAmounts(Map<K, Long> amounts, long divisor) {
        Map<K, Long> result = new LinkedHashMap<>();
        amounts.forEach((key, amount) -> result.put(key, amount / divisor));
        return result;
    }

    private static void mergeAmountsExcept(Map<Object, Long> target, Map<Object, Long> source, long factor,
        Object excluded) {
        for (Map.Entry<Object, Long> entry : source.entrySet()) {
            if (!entry.getKey()
                .equals(excluded)) {
                target.merge(entry.getKey(), Math.multiplyExact(entry.getValue(), factor), Math::addExact);
            }
        }
    }

    private static <K> void mergeAmounts(Map<K, Long> target, Map<K, Long> source, long factor) {
        for (Map.Entry<K, Long> entry : source.entrySet()) {
            target.merge(entry.getKey(), Math.multiplyExact(entry.getValue(), factor), Math::addExact);
        }
    }

    /** Cancels matching identities in place, retaining only the positive remainder on either side. */
    static void cancelMatchingInputsAndOutputs(Map<Object, Long> inputs, Map<Object, Long> outputs) {
        for (var iterator = inputs.entrySet()
            .iterator(); iterator.hasNext();) {
            Map.Entry<Object, Long> input = iterator.next();
            Long output = outputs.get(input.getKey());
            if (output == null) continue;
            long cancelled = Math.min(input.getValue(), output);
            long remainingInput = input.getValue() - cancelled;
            long remainingOutput = output - cancelled;
            if (remainingOutput == 0) outputs.remove(input.getKey());
            else outputs.put(input.getKey(), remainingOutput);
            if (remainingInput == 0) iterator.remove();
            else input.setValue(remainingInput);
        }
    }

    private static <K> Map<K, Long> immutableMap(Map<K, Long> map) {
        Objects.requireNonNull(map, "recipe amounts");
        return map.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    /** Only immutable source requirements survive startup; radiation is satisfied by the packaged radio hatch. */
    @Desugar
    record SourceRequirements(int heat, int glassTier, int casingTier, int compressionTier, boolean heavyCentrifuge,
        TST_ItemID specialInput, int plasmaTier, boolean multiStepPlasma) {

        private static final SourceRequirements NONE = new SourceRequirements(0, 0, 0, 0, false, null, 0, false);
    }

    /**
     * Keep source duration and requirements separate, including the native neutron activator's per-recipe tick floor.
     */
    @Desugar
    record TimeKey(RecipeMap<?> map, long eut, int recipeTicks, SourceRequirements requirements) {

        TimeKey(RecipeMap<?> map, long eut) {
            this(map, eut, 1, SourceRequirements.NONE);
        }
    }
}
