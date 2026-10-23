package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.ActivatedCarbonFilterMesh;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FlocculationWasteLiquid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OzoneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PolyAluminiumChloride;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumHydroxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SuperCoolant;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getGCD;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBaryonicPerfection;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitUVTreatment;

/** Exact expected resource ledgers for the eight native purification stages. */
public final class BottledChaosPurifiedWaterRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        Materials[] waters = { Materials.Grade1PurifiedWater, Materials.Grade2PurifiedWater,
            Materials.Grade3PurifiedWater, Materials.Grade4PurifiedWater, Materials.Grade5PurifiedWater,
            Materials.Grade6PurifiedWater, Materials.Grade7PurifiedWater, Materials.Grade8PurifiedWater };
        RecipeMap<?>[] pools = { RecipeMaps.purificationClarifierRecipes, RecipeMaps.purificationOzonationRecipes,
            RecipeMaps.purificationFlocculationRecipes, RecipeMaps.purificationPhAdjustmentRecipes,
            RecipeMaps.purificationPlasmaHeatingRecipes, RecipeMaps.purificationUVTreatmentRecipes,
            RecipeMaps.purificationDegasifierRecipes, RecipeMaps.purificationParticleExtractionRecipes };

        for (int grade = 1; grade <= waters.length; grade++) registerStage(catalog, pools[grade - 1], waters, grade);

        // spotless:off
        // Shared output branch: recover waste before supplementing the net aluminium and acid demand.
        catalog.registerOutputDecompositions(FlocculationWasteLiquid);

        // Shared input branches: native consumables for the eight independently chainable water stages.
        catalog.registerInputSyntheses(ActivatedCarbonFilterMesh, OzoneGas, PolyAluminiumChloride,
            SuperCoolant, SodiumHydroxideDust);
        // spotless:on
    }

    private static void registerStage(BottledChaosRecipeCatalog catalog, RecipeMap<?> pool, Materials[] waters,
        int grade) {
        Materials inputWater = grade == 1 ? Materials.Water : waters[grade - 2];
        GTRecipe source = findSource(pool, inputWater, waters[grade - 1], grade);
        int successPercent = grade <= 2 ? boostedSuccessPercent(source) : 100;
        Stage stage = new Stage(pool, source, successPercent);

        switch (grade) {
            case 1 -> {
                // Synthesize the expected loss: the native roll destroys filters on 19 of 100 outcomes.
                stage.itemInput(source.mInputs[0], source.mInputs[0].stackSize * 19L, 100);
                stage.waterBoost();
                stage.failedWater(grade, waters, successPercent);
            }
            case 2 -> {
                stage.waterBoost();
                stage.failedWater(grade, waters, successPercent);
            }
            case 3 -> {
                // Base 10% plus nine exact PAC levels reaches 100%; waste returns even on failure.
                stage.fluidInput(Materials.PolyAluminiumChloride.getFluid(1), 900_000, 1);
                stage.fluidOutput(Materials.FlocculationWasteLiquid.getFluid(1), 900_000, 1);
            }
            case 4 -> {
                // Enumerated native float pH values: the least one-sided dose in the passing range.
                stage.itemInput(Materials.SodiumHydroxide.getDust(1), 121_045, 2_000);
                stage.fluidInput(Materials.HydrochloricAcid.getFluid(1), 1_210_090, 2_000);
            }
            case 5 -> {
                // Three complete thermal cycles give 99%; 10% water feedback supplies the rest.
                stage.fluidInput(Materials.Helium.getPlasma(1), 300, 1);
                stage.fluidInput(Materials.SuperCoolant.getFluid(1), 6_000, 1);
                stage.waterBoost();
            }
            case 6 -> {
                // All nine native lenses are reusable, and the completed sequence gives 90%.
                if (MTEPurificationUnitUVTreatment.LENS_ITEMS.size() != 9)
                    throw new IllegalStateException("unexpected purified-water lens sequence");
                for (ItemStack lens : MTEPurificationUnitUVTreatment.LENS_ITEMS) stage.requireItem(lens, 1);
                stage.waterBoost();
            }
            case 7 -> {
                // Eight non-bypass signals occur at 10% each; the no-input bypass occurs at 20%.
                stage.fluidInput(Materials.SuperCoolant.getFluid(1), 10_000, 10);
                stage.fluidInput(Materials.Helium.getGas(1), 10_000, 10);
                stage.fluidInput(WerkstoffLoader.Neon.getFluidOrGas(1), 7_500, 10);
                stage.fluidInput(WerkstoffLoader.Krypton.getFluidOrGas(1), 5_000, 10);
                stage.fluidInput(WerkstoffLoader.Xenon.getFluidOrGas(1), 2_500, 10);
                stage.fluidInput(Materials.SuperconductorUMVBase.getMolten(1), 10L * INGOTS * 2, 5);
                stage.fluidInput(Materials.Neutronium.getMolten(1), 32L * INGOTS * 2, 5);
                // UMV superconductor doubles only the four signals that request it.
                stage.fluidOutput(source.mFluidOutputs[0], source.mFluidOutputs[0].amount * 2L, 5);
            }
            case 8 -> {
                // Eighteen entries cover every unordered pair, with three occurrences per flavor:
                // Strange, Up, Down, Up, Bottom, Down, Top, Up, Charm, Down, Strange, Bottom,
                // Top, Bottom, Charm, Top, Strange, Charm. Sixteen wrong entries return unchanged.
                ItemList[] catalysts = { ItemList.Quark_Creation_Catalyst_Up, ItemList.Quark_Creation_Catalyst_Down,
                    ItemList.Quark_Creation_Catalyst_Bottom, ItemList.Quark_Creation_Catalyst_Top,
                    ItemList.Quark_Creation_Catalyst_Strange, ItemList.Quark_Creation_Catalyst_Charm };
                for (ItemList catalyst : catalysts) {
                    ItemStack stack = catalyst.get(1);
                    stage.itemInput(stack, 3, 1);
                    stage.itemOutput(stack, 8, 3);
                    stage.requireItem(stack, 3);
                }
                stage.fluidInput(Materials.Infinity.getMolten(1), 6L * INGOTS * (1 + 2 + 4), 1);
                // The real source already contains the additional end-of-cycle 500 L.
                stage.fluidOutput(
                    Materials.StableBaryonicMatter.getFluid(1),
                    MTEPurificationUnitBaryonicPerfection.BARYONIC_MATTER_OUTPUT,
                    1);
            }
            default -> throw new IllegalStateException("unknown purified-water grade " + grade);
        }

        RecipeData data = stage.prepare();
        Object root = source.mFluidInputs[0].getFluid();
        Object product = source.mFluidOutputs[0].getFluid();
        // spotless:off
        // Main trunk: preserve the native stage's expected yield, special inputs and returned catalysts.
        Object[] products = data.outputs.keySet().stream()
            .filter(key -> !key.equals(FluidId.create(FlocculationWasteLiquid))).toArray();
        catalog.registerChain("purified_water_" + grade, 1, source.mEUt, root,
            products, new Object[] { product },
            catalog.markPreparedRecipe(stage.pool, root, product, data));
        catalog.setExecutionTargets("purified_water_" + grade, product);
        if (grade < 8) catalog.setKeyOutputs("purified_water_" + grade, product);
        catalog.registerExternalInputs(data.inputs.keySet().toArray());
        catalog.registerExternalInputs(data.catalysts.keySet().toArray());
        // spotless:on
    }

    private static GTRecipe findSource(RecipeMap<?> pool, Materials input, Materials output, int grade) {
        GTRecipe found = null;
        for (GTRecipe recipe : pool.getAllRecipes()) {
            if (!recipe.mEnabled || recipe.mFakeRecipe
                || recipe.mEUt <= 0
                || recipe.mDuration <= 0
                || recipe.mFluidInputs.length != (grade == 2 ? 2 : 1)
                || recipe.mFluidOutputs.length == 0
                || recipe.mFluidInputs[0] == null
                || recipe.mFluidOutputs[0] == null
                || recipe.mFluidInputs[0].amount <= 0
                || recipe.mFluidOutputs[0].amount <= 0
                || !recipe.mFluidInputs[0].isFluidEqual(input.getFluid(1))
                || !recipe.mFluidOutputs[0].isFluidEqual(output.getFluid(1))) continue;
            if (grade == 1 && (recipe.mInputs.length != 1 || recipe.mInputs[0] == null
                || !recipe.mInputs[0].isItemEqual(ItemList.ActivatedCarbonFilterMesh.get(1)))) continue;
            if (grade != 1 && recipe.mInputs.length != 0) continue;
            if (grade == 2 && (recipe.mFluidInputs[1] == null || recipe.mFluidInputs[1].amount != 1_024_000
                || !recipe.mFluidInputs[1].isFluidEqual(Materials.Ozone.getGas(1)))) continue;
            if (found != null && recipe.mEUt == found.mEUt)
                throw new IllegalStateException("ambiguous native purified-water grade " + grade);
            if (found == null || recipe.mEUt > found.mEUt) found = recipe;
        }
        if (found == null) throw new IllegalStateException("missing native purified-water grade " + grade);
        return found;
    }

    private static int boostedSuccessPercent(GTRecipe recipe) {
        float chance = recipe.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f);
        if (!Float.isFinite(chance) || chance < 0 || chance > 100 || chance != (int) chance)
            throw new IllegalStateException("unsupported native water success chance " + chance);
        return Math.min(100, (int) chance + 15);
    }

    /** Fractions exist only at startup; published inventories and work use exact long whole batches. */
    private static final class Stage {

        private final RecipeMap<?> pool;
        private final GTRecipe source;
        private final Map<Object, Amount> inputs = new LinkedHashMap<>();
        private final Map<Object, Amount> outputs = new LinkedHashMap<>();
        private final Map<Object, Long> catalysts = new LinkedHashMap<>();

        private Stage(RecipeMap<?> pool, GTRecipe source, int successPercent) {
            this.pool = pool;
            this.source = source;
            for (FluidStack stack : source.mFluidInputs) fluidInput(stack, stack.amount, 1);
            for (FluidStack stack : source.mFluidOutputs) {
                if (stack != null) fluidOutput(stack, Math.multiplyExact((long) stack.amount, successPercent), 100);
            }
            for (int i = 0; i < source.mOutputs.length; i++) {
                ItemStack stack = source.mOutputs[i];
                if (stack == null || stack.stackSize <= 0) continue;
                int chance = source.mOutputChances == null || i >= source.mOutputChances.length
                    || source.mOutputChances[i] < 0 ? 10_000 : source.mOutputChances[i];
                if (chance > 10_000) throw new IllegalStateException("invalid native water output chance");
                itemOutput(
                    stack,
                    Math.multiplyExact(Math.multiplyExact((long) stack.stackSize, chance), successPercent),
                    1_000_000);
            }
        }

        private void waterBoost() {
            fluidInput(source.mFluidOutputs[0], source.mFluidOutputs[0].amount, 10);
        }

        private void failedWater(int grade, Materials[] waters, int successPercent) {
            for (int returnedGrade = grade - 1; returnedGrade >= 0; returnedGrade--) {
                FluidStack returnedWater = returnedGrade == 0 ? GTModHandler.getDistilledWater(1)
                    : waters[returnedGrade - 1].getFluid(1);
                fluidOutput(
                    returnedWater,
                    Math.multiplyExact((long) source.mFluidOutputs[0].amount, 100 - successPercent),
                    100L << (grade - returnedGrade + 1));
            }
        }

        private void itemInput(ItemStack stack, long numerator, long denominator) {
            add(
                inputs,
                Objects.requireNonNull(recipeItemId(stack), "purified-water item input"),
                numerator,
                denominator);
        }

        private void itemOutput(ItemStack stack, long numerator, long denominator) {
            add(
                outputs,
                Objects.requireNonNull(recipeItemId(stack), "purified-water item output"),
                numerator,
                denominator);
        }

        private void requireItem(ItemStack stack, long amount) {
            catalysts.merge(Objects.requireNonNull(recipeItemId(stack), "purified-water catalyst"), amount, Math::max);
        }

        private void fluidInput(FluidStack stack, long numerator, long denominator) {
            add(
                inputs,
                FluidId.create(Objects.requireNonNull(stack, "purified-water fluid input")),
                numerator,
                denominator);
        }

        private void fluidOutput(FluidStack stack, long numerator, long denominator) {
            add(
                outputs,
                FluidId.create(Objects.requireNonNull(stack, "purified-water fluid output")),
                numerator,
                denominator);
        }

        private RecipeData prepare() {
            long batches = 1;
            for (Amount amount : inputs.values())
                batches = Math.multiplyExact(batches / getGCD(batches, amount.denominator), amount.denominator);
            for (Amount amount : outputs.values())
                batches = Math.multiplyExact(batches / getGCD(batches, amount.denominator), amount.denominator);
            // Netting feedback and returned catalysts must not reduce the number of source attempts.
            return new RecipeData(
                wholeAmounts(inputs, batches),
                wholeAmounts(outputs, batches),
                catalysts,
                Collections
                    .singletonMap(new TimeKey(pool, source.mEUt), Math.multiplyExact((long) source.mDuration, batches)))
                        .netInputsAndOutputs(FluidId.create(source.mFluidInputs[0]));
        }

        private static void add(Map<Object, Amount> amounts, Object key, long numerator, long denominator) {
            if (numerator == 0) return;
            amounts.merge(key, new Amount(numerator, denominator), Amount::add);
        }

        private static Map<Object, Long> wholeAmounts(Map<Object, Amount> fractions, long batches) {
            Map<Object, Long> amounts = new LinkedHashMap<>();
            fractions.forEach(
                (key, amount) -> amounts.put(key, Math.multiplyExact(amount.numerator, batches / amount.denominator)));
            return amounts;
        }
    }

    private static final class Amount {

        private final long numerator;
        private final long denominator;

        private Amount(long numerator, long denominator) {
            if (numerator < 0 || denominator <= 0)
                throw new IllegalArgumentException("invalid water resource fraction");
            long gcd = getGCD(numerator, denominator);
            this.numerator = numerator / gcd;
            this.denominator = denominator / gcd;
        }

        private Amount add(Amount other) {
            long gcd = getGCD(denominator, other.denominator);
            long leftScale = other.denominator / gcd;
            long rightScale = denominator / gcd;
            return new Amount(
                Math.addExact(
                    Math.multiplyExact(numerator, leftScale),
                    Math.multiplyExact(other.numerator, rightScale)),
                Math.multiplyExact(denominator, leftScale));
        }
    }
}
