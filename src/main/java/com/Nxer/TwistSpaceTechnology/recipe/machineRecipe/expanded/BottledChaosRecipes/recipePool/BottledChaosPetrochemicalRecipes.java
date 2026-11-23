package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Acetone;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Benzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DistilledWater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EthyleneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenSulfideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Methanol;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaphthenicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Nitrobenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalPlant;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfuricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getGCD;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getLCM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.github.bsideup.jabel.Desugar;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;

public final class BottledChaosPetrochemicalRecipes {

    private static final String REFINING = "petroleum_refining";
    private static final String CRACKING = "petroleum_cracking";
    private static final String OXYGENATES = "petrochemical_oxygenates";
    private static final String DIESEL = "diesel_blending";
    private static final String GASOLINE = "gasoline_blending";
    private static final String ETHANOL_GASOLINE = "ethanol_gasoline_blending";
    private static final String NITRATION = "benzene_nitration";

    private static final int OCTANE_CIRCUIT = 8;
    private static final int DIESEL_CIRCUIT = 9;
    private static final int CETANE_CIRCUIT = 10;
    private static final int RAW_GASOLINE_CIRCUIT = 11;
    private static final int REGULAR_GASOLINE_CIRCUIT = 12;
    private static final int PREMIUM_GASOLINE_CIRCUIT = 13;

    private static final Fluid HEAVY = fluid(Materials.HeavyFuel.getFluid(1));
    private static final Fluid LIGHT = fluid(Materials.LightFuel.getFluid(1));
    private static final Fluid NAPHTHA = fluid(Materials.Naphtha.getFluid(1));
    private static final Fluid GAS = fluid(Materials.Gas.getGas(1));
    private static final Fluid OCTANE = fluid(Materials.Octane.getFluid(1));
    private static final Fluid TOLUENE = fluid(Materials.Toluene.getFluid(1));

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // Recovery routes consume these gases directly. Expanding their synthesis would undo the recovery.
        catalog.registerExternalReagents(HydrogenSulfideGas, CarbonDioxideGas);
        var refineries = refineries();
        registerRefining(catalog, refineries);
        var cracking = registerCracking(catalog, refineries);
        registerOxygenates(catalog, refineries, cracking);
        registerFuels(catalog, refineries, cracking);
        BottledChaosWoodChemistryRecipes.registerRecipeChains(catalog);
        registerNitrationRoutes(catalog, refineries);
        catalog.setExistingEntriesKeyOutputs(OXYGENATES, fluid(Materials.Ethanol.getFluid(1)), Methanol, Acetone);
    }

    /** Add petroleum feedstocks only after wood chemistry has registered the primary nitration route. */
    private static void registerNitrationRoutes(BottledChaosRecipeCatalog catalog, List<Refinery> refineries) {
        RecipeData nitrate = prepare(
            ChemicalPlant,
            12,
            Nitrobenzene,
            Benzene,
            SulfuricAcid,
            NitricAcid,
            DistilledWater);
        Fluid phenol = fluid(Materials.Phenol.getFluid(1));
        RecipeData splitHeavy = prepare(DistillationTower, -1, phenol, HEAVY);
        RecipeData fromHeavy = splitHeavy.appendBalancedRecipe(nitrate, FluidId.create(Benzene));
        register(catalog, NITRATION, 0, HEAVY, fromHeavy, 0, Nitrobenzene);
        for (Refinery refinery : refineries) {
            RecipeData fromOil = refinery.data()
                .appendBalancedRecipe(fromHeavy, FluidId.create(HEAVY));
            register(catalog, NITRATION, 0, refinery.feed(), fromOil, 0, Nitrobenzene);
        }
        catalog.setExistingEntriesKeyOutputs(NITRATION, Nitrobenzene);
    }

    private static List<Refinery> refineries() {
        Fluid sourHeavy = fluid(Materials.SulfuricHeavyFuel.getFluid(1));
        Fluid sourLight = fluid(Materials.SulfuricLightFuel.getFluid(1));
        Fluid sourNaphtha = fluid(Materials.SulfuricNaphtha.getFluid(1));
        Fluid sourGas = fluid(Materials.SulfuricGas.getGas(1));
        RecipeData sweetHeavy = desulfurize(sourHeavy, HEAVY);
        RecipeData sweetLight = desulfurize(sourLight, LIGHT);
        RecipeData sweetNaphtha = desulfurize(sourNaphtha, NAPHTHA);
        RecipeData sweetGas = desulfurize(sourGas, GAS);
        List<Refinery> result = new ArrayList<>();
        for (Materials crude : Arrays
            .asList(Materials.Oil, Materials.OilLight, Materials.OilMedium, Materials.OilHeavy)) {
            Fluid feed = fluid(crude.getFluid(1));
            RecipeData data = prepare(DistillationTower, 9, NaphthenicAcid, feed)
                .appendBalancedRecipe(sweetHeavy, FluidId.create(sourHeavy))
                .appendBalancedRecipe(sweetLight, FluidId.create(sourLight))
                .appendBalancedRecipe(sweetNaphtha, FluidId.create(sourNaphtha))
                .appendBalancedRecipe(sweetGas, FluidId.create(sourGas));
            result.add(new Refinery(feed, data));
        }
        return result;
    }

    private static void registerRefining(BottledChaosRecipeCatalog catalog, List<Refinery> refineries) {
        for (Refinery refinery : refineries) {
            int capacity = refinery == refineries.get(0) ? 2 : 0;
            register(catalog, REFINING, capacity, refinery.feed(), refinery.data(), 0, HEAVY, LIGHT, NAPHTHA, GAS);
        }
        Fluid naturalGas = fluid(Materials.NaturalGas.getGas(1));
        register(catalog, REFINING, 0, naturalGas, desulfurize(naturalGas, GAS), 0, GAS);
        RecipeData recovery = prepare(ElectrolyzerNonCell, -1, HydrogenGas, HydrogenSulfideGas);
        register(catalog, REFINING, 0, HydrogenSulfideGas, recovery, 0, HydrogenGas);
        // The downstream credentials have their own crude-oil recipes. Keep the refinery cuts available as outputs.
        catalog.setExistingEntriesKeyOutputs(REFINING);
    }

    private static List<Cracking> registerCracking(BottledChaosRecipeCatalog catalog, List<Refinery> refineries) {
        Fluid propene = fluid(Materials.Propene.getGas(1));
        Fluid butadiene = fluid(Materials.Butadiene.getGas(1));
        Fluid helium = fluid(Materials.Helium.getGas(1));
        Fluid phenol = fluid(Materials.Phenol.getFluid(1));
        List<Cracking> recipes = Arrays.asList(
            cracking(HEAVY, Materials.HeavyFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, 1, LIGHT),
            cracking(LIGHT, Materials.LightFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, OCTANE_CIRCUIT, OCTANE),
            cracking(GAS, Materials.Gas.getSeverelyHydroCracked(1_000), HydrogenGas, 3, 1, helium),
            cracking(NAPHTHA, Materials.Naphtha.getLightlySteamCracked(1_000), Steam, 1, 2, butadiene),
            cracking(HEAVY, Materials.HeavyFuel.getSeverelySteamCracked(1_000), Steam, 3, 3, Benzene),
            cracking(LIGHT, Materials.LightFuel.getSeverelySteamCracked(1_000), Steam, 3, 3, propene),
            cracking(NAPHTHA, Materials.Naphtha.getSeverelySteamCracked(1_000), Steam, 3, 3, EthyleneGas),
            cracking(GAS, Materials.Gas.getSeverelySteamCracked(1_000), Steam, 3, 3, EthyleneGas),
            new Cracking(HEAVY, phenol, 4, prepare(DistillationTower, -1, phenol, HEAVY)),
            new Cracking(GAS, helium, 4, prepare(DistillationTower, -1, helium, GAS)));
        for (Cracking recipe : recipes) {
            int capacity = recipe == recipes.get(0) ? 4 : 0;
            register(catalog, CRACKING, capacity, recipe.feed(), recipe.data(), recipe.circuit(), recipe.product());
        }
        for (Materials alkane : Arrays.asList(Materials.Ethane, Materials.Propane, Materials.Butane)) {
            Fluid feed = fluid(alkane.getGas(1));
            register(catalog, CRACKING, 0, feed, prepare(ElectrolyzerNonCell, -1, HydrogenGas, feed), 0, HydrogenGas);
        }

        for (int circuit : new int[] { 1, 2, 3, 4, OCTANE_CIRCUIT }) {
            List<Cracking> selected = new ArrayList<>();
            for (Cracking recipe : recipes) if (recipe.circuit() == circuit) selected.add(recipe);
            Object[] products = selected.stream()
                .map(Cracking::product)
                .distinct()
                .toArray();
            for (Refinery refinery : refineries) {
                RecipeData data = crackRefineryFractions(refinery.data(), selected);
                // Light fuel can cancel against the light fraction charged to this same recipe.
                Object[] markers = Arrays.stream(products)
                    .filter(product -> data.outputs.containsKey(FluidId.create((Fluid) product)))
                    .toArray();
                register(catalog, CRACKING, 0, refinery.feed(), data, circuit, markers);
            }
        }
        // Each cracking circuit delivers its products. Octane can also supply the premium gasoline route.
        catalog.setExistingEntriesKeyOutputs(CRACKING, OCTANE);
        return recipes;
    }

    private static void registerOxygenates(BottledChaosRecipeCatalog catalog, List<Refinery> refineries,
        List<Cracking> cracking) {
        Fluid ethanol = fluid(Materials.Ethanol.getFluid(1));
        RecipeData hydration = prepare(LargeChemicalReactor, 24, ethanol, EthyleneGas, Steam);
        RecipeData oxidation = prepare(LargeChemicalReactor, 8, AceticAcid, EthyleneGas, OxygenGas);
        RecipeData acetone = prepare(
            LargeChemicalReactor,
            3,
            Acetone,
            new ItemStack[] { Materials.Calcium.getDust(1) },
            AceticAcid);
        RecipeData acetoneLine = oxidation.appendBalancedRecipe(acetone, FluidId.create(AceticAcid));
        register(catalog, OXYGENATES, 2, EthyleneGas, hydration, 5, ethanol);
        register(catalog, OXYGENATES, 0, EthyleneGas, oxidation, 6, AceticAcid);
        register(catalog, OXYGENATES, 0, EthyleneGas, acetoneLine, 7, Acetone);
        register(
            catalog,
            OXYGENATES,
            0,
            CarbonDioxideGas,
            prepare(LargeChemicalReactor, 1, Methanol, CarbonDioxideGas, HydrogenGas),
            0,
            Methanol);

        RecipeData steamCracking = findCracking(cracking, NAPHTHA, 3);
        var products = Arrays.asList(ethanol, AceticAcid, Acetone);
        var derivatives = Arrays.asList(hydration, oxidation, acetoneLine);
        for (int i = 0; i < products.size(); i++) {
            RecipeData fromNaphtha = steamCracking
                .appendBalancedRecipe(derivatives.get(i), FluidId.create(EthyleneGas));
            register(catalog, OXYGENATES, 0, NAPHTHA, fromNaphtha, 5 + i, products.get(i));
            for (Refinery refinery : refineries) {
                RecipeData fromOil = refinery.data()
                    .appendBalancedRecipe(fromNaphtha, FluidId.create(NAPHTHA));
                register(catalog, OXYGENATES, 0, refinery.feed(), fromOil, 5 + i, products.get(i));
            }
        }
    }

    private static void registerFuels(BottledChaosRecipeCatalog catalog, List<Refinery> refineries,
        List<Cracking> cracking) {
        Fluid diesel = fluid(Materials.Diesel.getFluid(1));
        Fluid cetaneDiesel = fluid(Materials.NitroFuel.getFluid(1));
        Fluid cetaneAdditive = fluid(Materials.Tetranitromethane.getFluid(1));
        RecipeData blendDiesel = prepare(LargeChemicalReactor, 24, diesel, LIGHT, HEAVY);
        RecipeData boostDiesel = prepare(LargeChemicalReactor, 24, cetaneDiesel, diesel, cetaneAdditive);
        RecipeData blendCetane = blendDiesel.appendBalancedRecipe(boostDiesel, FluidId.create(diesel));
        register(catalog, DIESEL, 1, diesel, boostDiesel, CETANE_CIRCUIT, cetaneDiesel);
        register(catalog, DIESEL, 0, LIGHT, blendCetane, CETANE_CIRCUIT, cetaneDiesel);
        register(catalog, DIESEL, 0, LIGHT, blendDiesel, DIESEL_CIRCUIT, diesel);

        Fluid raw = fluid(Materials.GasolineRaw.getFluid(1));
        Fluid regular = fluid(Materials.GasolineRegular.getFluid(1));
        Fluid premium = fluid(Materials.GasolinePremium.getFluid(1));
        Fluid ethanol = fluid(Materials.Ethanol.getFluid(1));
        Fluid antiKnock = fluid(Materials.AntiKnock.getFluid(1));
        Fluid butene = fluid(Materials.Butene.getGas(1));
        Fluid nitrousOxide = fluid(Materials.NitrousOxide.getGas(1));
        Fluid ethanolGasoline = fluid(GGMaterial.ethanolGasoline.getFluidOrGas(1));

        RecipeData blendRaw = prepare(LargeChemicalReactor, 24, raw, NAPHTHA, GAS, Methanol, Acetone);
        RecipeData blendRegular = prepare(LargeChemicalReactor, 24, regular, raw, TOLUENE);
        RecipeData blendPremium = prepare(LargeChemicalReactor, 24, antiKnock, ethanol, butene).appendBalancedRecipe(
            prepare(LargeChemicalReactor, 24, premium, regular, OCTANE, nitrousOxide, TOLUENE, antiKnock),
            FluidId.create(antiKnock));
        RecipeData blendEthanol = prepare(LargeChemicalReactor, -1, ethanolGasoline, raw, ethanol);
        RecipeData premiumFromRaw = blendRegular.appendBalancedRecipe(blendPremium, FluidId.create(regular));
        RecipeData regularFromCuts = blendRaw.appendBalancedRecipe(blendRegular, FluidId.create(raw));
        RecipeData premiumFromCuts = blendRaw.appendBalancedRecipe(premiumFromRaw, FluidId.create(raw));
        RecipeData ethanolFromCuts = blendRaw.appendBalancedRecipe(blendEthanol, FluidId.create(raw));

        register(catalog, GASOLINE, 2, regular, blendPremium, PREMIUM_GASOLINE_CIRCUIT, premium);
        register(catalog, GASOLINE, 0, raw, premiumFromRaw, PREMIUM_GASOLINE_CIRCUIT, premium);
        register(catalog, GASOLINE, 0, raw, blendRegular, REGULAR_GASOLINE_CIRCUIT, regular);
        register(catalog, GASOLINE, 0, NAPHTHA, premiumFromCuts, PREMIUM_GASOLINE_CIRCUIT, premium);
        register(catalog, GASOLINE, 0, NAPHTHA, regularFromCuts, REGULAR_GASOLINE_CIRCUIT, regular);
        register(catalog, GASOLINE, 0, NAPHTHA, blendRaw, RAW_GASOLINE_CIRCUIT, raw);
        register(catalog, ETHANOL_GASOLINE, 1, raw, blendEthanol, 0, ethanolGasoline);
        register(catalog, ETHANOL_GASOLINE, 0, NAPHTHA, ethanolFromCuts, 0, ethanolGasoline);

        RecipeData splitHeavy = findCracking(cracking, HEAVY, 4);
        RecipeData makeOctane = findCracking(cracking, LIGHT, OCTANE_CIRCUIT);
        for (Refinery refinery : refineries) {
            Fluid feed = refinery.feed();
            RecipeData data = refinery.data();
            register(
                catalog,
                DIESEL,
                0,
                feed,
                blendFractions(data, blendCetane, LIGHT, HEAVY),
                CETANE_CIRCUIT,
                cetaneDiesel);
            register(catalog, DIESEL, 0, feed, blendFractions(data, blendDiesel, LIGHT, HEAVY), DIESEL_CIRCUIT, diesel);

            RecipeData withToluene = data.appendBalancedRecipe(splitHeavy, FluidId.create(HEAVY));
            RecipeData withOctane = withToluene.appendBalancedRecipe(makeOctane, FluidId.create(LIGHT));
            register(catalog, GASOLINE, 0, feed, blendFractions(data, blendRaw, NAPHTHA, GAS), RAW_GASOLINE_CIRCUIT, raw);
            register(
                catalog,
                GASOLINE,
                0,
                feed,
                blendFractions(withOctane, premiumFromCuts, NAPHTHA, GAS, OCTANE, TOLUENE),
                PREMIUM_GASOLINE_CIRCUIT,
                premium);
            register(
                catalog,
                GASOLINE,
                0,
                feed,
                blendFractions(withToluene, regularFromCuts, NAPHTHA, GAS, TOLUENE),
                REGULAR_GASOLINE_CIRCUIT,
                regular);
            register(
                catalog,
                ETHANOL_GASOLINE,
                0,
                feed,
                blendFractions(data, ethanolFromCuts, NAPHTHA, GAS),
                0,
                ethanolGasoline);
        }
        catalog.setExistingEntriesKeyOutputs(DIESEL, cetaneDiesel);
        catalog.setExistingEntriesKeyOutputs(GASOLINE, premium);
        catalog.setExistingEntriesKeyOutputs(ETHANOL_GASOLINE, ethanolGasoline);
    }

    /** Size every cracker from the original cuts so another branch's returns stay as coproducts. */
    private static RecipeData crackRefineryFractions(RecipeData refinery, List<Cracking> cracking) {
        long refineryBatches = 1;
        for (Cracking recipe : cracking) {
            FluidId key = FluidId.create(recipe.feed());
            long supplied = refinery.outputs.get(key);
            long needed = recipe.data().inputs.get(key);
            refineryBatches = getLCM(refineryBatches, needed / getGCD(supplied, needed));
        }
        RecipeData result = refinery;
        long upstreamBatches = refineryBatches;
        for (Cracking recipe : cracking) {
            FluidId key = FluidId.create(recipe.feed());
            long batches = Math.multiplyExact(refinery.outputs.get(key), refineryBatches)
                / recipe.data().inputs.get(key);
            result = result.combineRecipe(recipe.data(), upstreamBatches, batches);
            upstreamBatches = 1;
        }
        return result;
    }

    /** Use the limiting cut to size the blend and retain all surplus cuts at their native ratios. */
    private static RecipeData blendFractions(RecipeData refinery, RecipeData blend, Fluid... fractions) {
        FluidId limiting = FluidId.create(fractions[0]);
        for (Fluid fraction : fractions) {
            FluidId key = FluidId.create(fraction);
            if (Math.multiplyExact(blend.inputs.get(key), refinery.outputs.get(limiting))
                > Math.multiplyExact(blend.inputs.get(limiting), refinery.outputs.get(key))) limiting = key;
        }
        return refinery.appendBalancedRecipe(blend, limiting);
    }

    private static RecipeData findCracking(List<Cracking> recipes, Fluid feed, int circuit) {
        return recipes.stream()
            .filter(recipe -> recipe.feed() == feed && recipe.circuit() == circuit)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Missing cracking feed " + feed.getName()))
            .data();
    }

    private static Cracking cracking(Fluid input, FluidStack cracked, Fluid agent, int operation, int selector,
        Fluid product) {
        Fluid intermediate = fluid(cracked);
        RecipeData cracking = prepare(OilCracker, operation, intermediate, input, agent);
        RecipeData separation = prepare(DistillationTower, -1, product, intermediate).withFullDustOutputs();
        return new Cracking(
            input,
            product,
            selector,
            cracking.appendBalancedRecipe(separation, FluidId.create(cracked)));
    }

    private static RecipeData desulfurize(Fluid sour, Fluid clean) {
        return prepare(LargeChemicalReactor, 4, clean, sour, HydrogenGas);
    }

    private static RecipeData prepare(RecipeMap<?> pool, int circuit, Fluid product, Fluid... fluids) {
        return prepare(pool, circuit, product, new ItemStack[0], fluids);
    }

    private static RecipeData prepare(RecipeMap<?> pool, int circuit, Fluid product, ItemStack[] items,
        Fluid... fluids) {
        if (circuit > 0) {
            items = Arrays.copyOf(items, items.length + 1);
            items[items.length - 1] = GTUtility.getIntegratedCircuit(circuit);
        }
        FluidStack[] supplied = Arrays.stream(fluids)
            .map(fluid -> new FluidStack(fluid, 128_000))
            .toArray(FluidStack[]::new);
        var recipe = pool.findRecipeQuery()
            .items(items)
            .fluids(supplied)
            .filter(
                candidate -> Arrays.stream(candidate.mFluidOutputs)
                    .anyMatch(output -> output != null && output.getFluid() == product))
            .find();
        if (recipe == null) throw new IllegalStateException(
            "Missing petroleum source in " + pool.unlocalizedName
                + " for "
                + product.getName()
                + " (circuit "
                + circuit
                + ", fluids "
                + Arrays.toString(supplied)
                + ")");
        return prepareSourceRecipe(pool, recipe);
    }

    private static void register(BottledChaosRecipeCatalog catalog, String tag, int capacity, Fluid feed,
        RecipeData source, int circuit, Object... displayProducts) {
        RecipeData data = source.netInputsAndOutputs(FluidId.create(feed));
        Object[] outputs = data.outputs.keySet()
            .toArray();
        Object marker = displayProducts[0];
        var step = catalog.markPreparedRecipe(
            data.times.keySet()
                .iterator()
                .next()
                .map(),
            feed,
            marker,
            data);
        var entry = capacity > 0 ? catalog.registerChain(tag, capacity, TierEU.RECIPE_EV, feed, outputs, step)
            : catalog.registerAlternativeChain(tag, TierEU.RECIPE_EV, feed, outputs, step);
        entry.setDisplayOutputs(displayProducts);
        entry.setPlanningTargets(marker);
        if (circuit > 0) entry.requireCircuit(circuit);
    }

    private static Fluid fluid(FluidStack stack) {
        return stack.getFluid();
    }

    @Desugar
    private record Refinery(Fluid feed, RecipeData data) {}

    @Desugar
    private record Cracking(Fluid feed, Fluid product, int circuit, RecipeData data) {}

    private BottledChaosPetrochemicalRecipes() {}
}
