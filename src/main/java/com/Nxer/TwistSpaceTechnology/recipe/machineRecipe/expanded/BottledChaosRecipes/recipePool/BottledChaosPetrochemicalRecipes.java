package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Acetone;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Benzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EthyleneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenSulfideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MethaneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Methanol;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaphthenicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

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
    private static final String DIESEL = "diesel_blending";
    private static final String GASOLINE = "gasoline_blending";
    private static final String ETHANOL_GASOLINE = "ethanol_gasoline_blending";

    private static final int REFINING_CIRCUIT = 5;
    private static final int HYDROCRACKING_CIRCUIT = 6;
    private static final int LIGHT_STEAM_CRACKING_CIRCUIT = 7;
    private static final int HEAVY_STEAM_CRACKING_CIRCUIT = 8;
    private static final int SEPARATION_CIRCUIT = 9;
    private static final int OCTANE_CIRCUIT = 10;
    private static final int DIESEL_CIRCUIT = 11;
    private static final int RAW_GASOLINE_CIRCUIT = 12;
    private static final int REGULAR_GASOLINE_CIRCUIT = 13;
    private static final int PREMIUM_GASOLINE_CIRCUIT = 14;
    private static final int ETHANOL_GASOLINE_CIRCUIT = 15;

    private static final Fluid HEAVY = fluid(Materials.HeavyFuel.getFluid(1));
    private static final Fluid LIGHT = fluid(Materials.LightFuel.getFluid(1));
    private static final Fluid NAPHTHA = fluid(Materials.Naphtha.getFluid(1));
    private static final Fluid GAS = fluid(Materials.Gas.getGas(1));
    private static final Fluid OCTANE = fluid(Materials.Octane.getFluid(1));
    private static final Fluid TOLUENE = fluid(Materials.Toluene.getFluid(1));
    private static final Fluid BUTENE = fluid(Materials.Butene.getGas(1));

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        catalog.registerExternalReagents(HydrogenSulfideGas, CarbonDioxideGas);
        RecipeData sulfurRecovery = prepare(ElectrolyzerNonCell, -1, HydrogenGas, HydrogenSulfideGas);
        registerRefining(catalog, sulfurRecovery);
        registerCracking(catalog);
        registerFuels(catalog);
        BottledChaosWoodChemistryRecipes.registerRecipeChains(catalog);
    }

    private static void registerRefining(BottledChaosRecipeCatalog catalog, RecipeData sulfurRecovery) {
        Fluid sourHeavy = fluid(Materials.SulfuricHeavyFuel.getFluid(1));
        Fluid sourLight = fluid(Materials.SulfuricLightFuel.getFluid(1));
        Fluid sourNaphtha = fluid(Materials.SulfuricNaphtha.getFluid(1));
        Fluid sourGas = fluid(Materials.SulfuricGas.getGas(1));
        RecipeData sweetHeavy = desulfurize(sourHeavy, HEAVY, sulfurRecovery);
        RecipeData sweetLight = desulfurize(sourLight, LIGHT, sulfurRecovery);
        RecipeData sweetNaphtha = desulfurize(sourNaphtha, NAPHTHA, sulfurRecovery);
        RecipeData sweetGas = desulfurize(sourGas, GAS, sulfurRecovery);
        for (Materials crude : Arrays
            .asList(Materials.Oil, Materials.OilLight, Materials.OilMedium, Materials.OilHeavy)) {
            Fluid feed = fluid(crude.getFluid(1));
            RecipeData data = prepare(DistillationTower, 9, NaphthenicAcid, feed)
                .appendBalancedRecipe(sweetHeavy, FluidId.create(sourHeavy))
                .appendBalancedRecipe(sweetLight, FluidId.create(sourLight))
                .appendBalancedRecipe(sweetNaphtha, FluidId.create(sourNaphtha))
                .appendBalancedRecipe(sweetGas, FluidId.create(sourGas));
            int capacity = crude == Materials.Oil ? 2 : 0;
            register(catalog, REFINING, capacity, feed, data, REFINING_CIRCUIT, HEAVY)
                .setDisplayOutputs(HEAVY, LIGHT, NAPHTHA, GAS, NaphthenicAcid);
        }
        Fluid naturalGas = fluid(Materials.NaturalGas.getGas(1));
        register(catalog, REFINING, 0, naturalGas, desulfurize(naturalGas, GAS, sulfurRecovery), 0, GAS);
        catalog.setExistingEntriesKeyOutputs(REFINING, HEAVY, LIGHT, NAPHTHA, GAS);
    }

    private static void registerCracking(BottledChaosRecipeCatalog catalog) {
        Fluid propene = fluid(Materials.Propene.getGas(1));
        Fluid butadiene = fluid(Materials.Butadiene.getGas(1));
        Fluid helium = fluid(Materials.Helium.getGas(1));
        Fluid phenol = fluid(Materials.Phenol.getFluid(1));
        // Keep butane available for the MTBE blend.
        Fluid[] recoveryFeeds = { fluid(Materials.Ethane.getGas(1)), fluid(Materials.Propane.getGas(1)), propene,
            butadiene };
        Object[] intermediates = Stream
            .concat(
                Stream.of(
                    HEAVY,
                    LIGHT,
                    NAPHTHA,
                    GAS,
                    OCTANE,
                    EthyleneGas,
                    TOLUENE,
                    fluid(Materials.Butane.getGas(1)),
                    BUTENE),
                Arrays.stream(recoveryFeeds))
            .toArray();
        Object[] displayedProducts = Stream
            .concat(Arrays.stream(intermediates), Stream.of(Benzene, phenol, helium, MethaneGas))
            .toArray();
        List<Cracking> recipes = Arrays.asList(
            cracking(HEAVY, Materials.HeavyFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, HYDROCRACKING_CIRCUIT, LIGHT),
            cracking(LIGHT, Materials.LightFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, OCTANE_CIRCUIT, OCTANE),
            cracking(GAS, Materials.Gas.getSeverelyHydroCracked(1_000), HydrogenGas, 3, HYDROCRACKING_CIRCUIT, helium),
            cracking(NAPHTHA, Materials.Naphtha.getLightlySteamCracked(1_000), Steam, 1, LIGHT_STEAM_CRACKING_CIRCUIT, butadiene),
            cracking(HEAVY, Materials.HeavyFuel.getSeverelySteamCracked(1_000), Steam, 3, HEAVY_STEAM_CRACKING_CIRCUIT, Benzene),
            cracking(LIGHT, Materials.LightFuel.getSeverelySteamCracked(1_000), Steam, 3, HEAVY_STEAM_CRACKING_CIRCUIT, propene),
            cracking(NAPHTHA, Materials.Naphtha.getSeverelySteamCracked(1_000), Steam, 3, HEAVY_STEAM_CRACKING_CIRCUIT, EthyleneGas),
            cracking(GAS, Materials.Gas.getSeverelySteamCracked(1_000), Steam, 3, HEAVY_STEAM_CRACKING_CIRCUIT, EthyleneGas),
            new Cracking(HEAVY, phenol, SEPARATION_CIRCUIT, prepare(DistillationTower, -1, phenol, HEAVY)),
            new Cracking(GAS, helium, SEPARATION_CIRCUIT, prepare(DistillationTower, -1, helium, GAS)));
        for (Cracking recipe : recipes) {
            int capacity = recipe == recipes.get(0) ? 4 : 0;
            register(catalog, CRACKING, capacity, recipe.feed(), recipe.data(), recipe.circuit(), recipe.product())
                .setDisplayOutputs(displayedProducts);
        }
        for (Fluid feed : recoveryFeeds) {
            register(catalog, CRACKING, 0, feed, prepare(ElectrolyzerNonCell, -1, HydrogenGas, feed), 0, HydrogenGas)
                .setDisplayOutputs(CarbonDust, HydrogenGas);
        }

        catalog.setExistingEntriesKeyOutputs(CRACKING, intermediates);
    }

    private static void registerFuels(BottledChaosRecipeCatalog catalog) {
        Fluid diesel = fluid(Materials.Diesel.getFluid(1));
        RecipeData blendDiesel = prepare(LargeChemicalReactor, 24, diesel, LIGHT, HEAVY);
        register(catalog, DIESEL, 1, LIGHT, blendDiesel, DIESEL_CIRCUIT, diesel);

        Fluid raw = fluid(Materials.GasolineRaw.getFluid(1));
        Fluid regular = fluid(Materials.GasolineRegular.getFluid(1));
        Fluid premium = fluid(Materials.GasolinePremium.getFluid(1));
        Fluid ethanol = fluid(Materials.Ethanol.getFluid(1));
        Fluid antiKnock = fluid(Materials.AntiKnock.getFluid(1));
        Fluid butane = fluid(Materials.Butane.getGas(1));
        Fluid butaneMtbe = fluid(Materials.MTBEMixtureAlt.getGas(1));
        Fluid nitrousOxide = fluid(Materials.NitrousOxide.getGas(1));
        Fluid ethanolGasoline = fluid(GGMaterial.ethanolGasoline.getFluidOrGas(1));

        RecipeData makeEthanol = prepare(LargeChemicalReactor, 24, ethanol, EthyleneGas, Steam);
        RecipeData makeNitrousOxide = prepare(LargeChemicalReactor, 24, nitrousOxide, NitrogenGas, OxygenGas);
        RecipeData blendRaw = prepare(LargeChemicalReactor, 24, raw, NAPHTHA, GAS, Methanol, Acetone);
        RecipeData blendRegular = prepare(LargeChemicalReactor, 24, regular, raw, TOLUENE);
        RecipeData finishPremium = makeNitrousOxide.appendBalancedRecipe(
            prepare(LargeChemicalReactor, 24, premium, regular, OCTANE, nitrousOxide, TOLUENE, antiKnock),
            FluidId.create(nitrousOxide));
        RecipeData blendPremium = makeEthanol
            .appendBalancedRecipe(
                prepare(LargeChemicalReactor, 24, antiKnock, ethanol, BUTENE),
                FluidId.create(ethanol))
            .appendBalancedRecipe(finishPremium, FluidId.create(antiKnock));
        // Credit the methanol and butane returned during MTBE distillation.
        RecipeData mtbePremium = prepare(LargeChemicalReactor, 24, butaneMtbe, Methanol, butane)
            .appendBalancedRecipe(prepare(DistillationTower, -1, antiKnock, butaneMtbe), FluidId.create(butaneMtbe))
            .netInputsAndOutputs(FluidId.create(butane))
            .appendBalancedRecipe(finishPremium, FluidId.create(antiKnock));
        RecipeData ethanolFromEthylene = makeEthanol.appendBalancedRecipe(
            prepare(LargeChemicalReactor, -1, ethanolGasoline, raw, ethanol),
            FluidId.create(ethanol));
        register(catalog, GASOLINE, 2, regular, blendPremium, PREMIUM_GASOLINE_CIRCUIT, premium);
        register(catalog, GASOLINE, 0, regular, mtbePremium, PREMIUM_GASOLINE_CIRCUIT, premium);
        register(catalog, GASOLINE, 0, raw, blendRegular, REGULAR_GASOLINE_CIRCUIT, regular);
        register(catalog, GASOLINE, 0, NAPHTHA, blendRaw, RAW_GASOLINE_CIRCUIT, raw);
        // Leave crude gasoline and ethylene to the runtime chain so each stage keeps its own selector.
        register(catalog, ETHANOL_GASOLINE, 1, raw, ethanolFromEthylene, ETHANOL_GASOLINE_CIRCUIT, ethanolGasoline);

        catalog.setExistingEntriesKeyOutputs(DIESEL);
        catalog.setExistingEntriesKeyOutputs(GASOLINE, raw, regular);
        catalog.setExistingEntriesKeyOutputs(ETHANOL_GASOLINE);
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

    private static RecipeData desulfurize(Fluid sour, Fluid clean, RecipeData sulfurRecovery) {
        return prepare(LargeChemicalReactor, 4, clean, sour, HydrogenGas)
            .appendBalancedRecipe(sulfurRecovery, FluidId.create(HydrogenSulfideGas))
            .netInputsAndOutputs(FluidId.create(sour));
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

    private static BottledChaosRecipeCatalog.Entry register(BottledChaosRecipeCatalog catalog, String tag, int capacity,
        Fluid feed, RecipeData source, int circuit, Object product) {
        RecipeData data = source.netInputsAndOutputs(FluidId.create(feed));
        Object[] outputs = data.outputs.keySet()
            .toArray();
        var step = catalog.markPreparedRecipe(
            data.times.keySet()
                .iterator()
                .next()
                .map(),
            feed,
            product,
            data);
        var entry = capacity > 0 ? catalog.registerChain(tag, capacity, TierEU.RECIPE_EV, feed, outputs, step)
            : catalog.registerAlternativeChain(tag, TierEU.RECIPE_EV, feed, outputs, step);
        entry.setDisplayOutputs(product);
        entry.setPlanningTargets(product);
        if (circuit > 0) entry.requireCircuit(circuit);
        return entry;
    }

    private static Fluid fluid(FluidStack stack) {
        return stack.getFluid();
    }

    @Desugar
    private record Cracking(Fluid feed, Fluid product, int circuit, RecipeData data) {}

    private BottledChaosPetrochemicalRecipes() {}
}
