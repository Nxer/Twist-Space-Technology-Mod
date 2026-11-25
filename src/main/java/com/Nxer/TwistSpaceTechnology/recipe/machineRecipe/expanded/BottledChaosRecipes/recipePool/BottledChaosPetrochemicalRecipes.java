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
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;

import java.util.Arrays;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.Entry;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

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
        registerRefining(catalog);
        registerCracking(catalog);
        registerFuels(catalog);
    }

    private static void registerRefining(BottledChaosRecipeCatalog catalog) {
        Fluid oil = fluid(Materials.Oil.getFluid(1));
        Fluid lightOil = fluid(Materials.OilLight.getFluid(1));
        Fluid mediumOil = fluid(Materials.OilMedium.getFluid(1));
        Fluid heavyOil = fluid(Materials.OilHeavy.getFluid(1));
        Fluid naturalGas = fluid(Materials.NaturalGas.getGas(1));

        // spotless:off
        // Each fraction recovers hydrogen before its desulfurization is balanced against the other fractions.
        var sulfurRecovery = catalog.markRecipe(ElectrolyzerNonCell, HydrogenSulfideGas, HydrogenGas, -1);
        var desulfurizeHeavy = markDesulfurization(catalog, fluid(Materials.SulfuricHeavyFuel.getFluid(1)), HEAVY, sulfurRecovery);
        var desulfurizeLight = markDesulfurization(catalog, fluid(Materials.SulfuricLightFuel.getFluid(1)), LIGHT, sulfurRecovery);
        var desulfurizeNaphtha = markDesulfurization(catalog, fluid(Materials.SulfuricNaphtha.getFluid(1)), NAPHTHA, sulfurRecovery);
        var desulfurizeGas = markDesulfurization(catalog, fluid(Materials.SulfuricGas.getGas(1)), GAS, sulfurRecovery);

        Object[] refinedProducts = { HEAVY, LIGHT, NAPHTHA, GAS, NaphthenicAcid, SulfurDust };
        configureRefining(catalog.registerChain(REFINING, 2, TierEU.RECIPE_EV, oil, refinedProducts,
            catalog.markRecipe(DistillationTower, oil, NaphthenicAcid, 9),
            desulfurizeHeavy, desulfurizeLight, desulfurizeNaphtha, desulfurizeGas));

        configureRefining(catalog.registerAlternativeChain(REFINING, TierEU.RECIPE_EV, lightOil, refinedProducts,
            catalog.markRecipe(DistillationTower, lightOil, NaphthenicAcid, 9),
            desulfurizeHeavy, desulfurizeLight, desulfurizeNaphtha, desulfurizeGas));

        configureRefining(catalog.registerAlternativeChain(REFINING, TierEU.RECIPE_EV, mediumOil, refinedProducts,
            catalog.markRecipe(DistillationTower, mediumOil, NaphthenicAcid, 9),
            desulfurizeHeavy, desulfurizeLight, desulfurizeNaphtha, desulfurizeGas));

        configureRefining(catalog.registerAlternativeChain(REFINING, TierEU.RECIPE_EV, heavyOil, refinedProducts,
            catalog.markRecipe(DistillationTower, heavyOil, NaphthenicAcid, 9),
            desulfurizeHeavy, desulfurizeLight, desulfurizeNaphtha, desulfurizeGas));

        var gasRefining = catalog.registerAlternativeChain(REFINING, TierEU.RECIPE_EV, naturalGas,
            new Object[] { GAS, SulfurDust }, markDesulfurization(catalog, naturalGas, GAS, sulfurRecovery));
        gasRefining.setDisplayOutputs(GAS);
        gasRefining.setPlanningTargets(GAS);
        catalog.setExistingEntriesKeyOutputs(REFINING, HEAVY, LIGHT, NAPHTHA, GAS);
        // spotless:on
    }

    private static StepRef markDesulfurization(BottledChaosRecipeCatalog catalog, Fluid sour, Fluid clean,
        StepRef sulfurRecovery) {
        return catalog.markRecipeSequence(
            LargeChemicalReactor,
            sour,
            clean,
            catalog.markRecipe(LargeChemicalReactor, new Object[] { sour, HydrogenGas }, clean, 4),
            sulfurRecovery);
    }

    private static void configureRefining(Entry entry) {
        entry.requireCircuit(REFINING_CIRCUIT);
        entry.setDisplayOutputs(HEAVY, LIGHT, NAPHTHA, GAS, NaphthenicAcid);
        entry.setPlanningTargets(HEAVY);
    }

    private static void registerCracking(BottledChaosRecipeCatalog catalog) {
        Fluid propene = fluid(Materials.Propene.getGas(1));
        Fluid butadiene = fluid(Materials.Butadiene.getGas(1));
        Fluid helium = fluid(Materials.Helium.getGas(1));
        Fluid phenol = fluid(Materials.Phenol.getFluid(1));
        Fluid ethane = fluid(Materials.Ethane.getGas(1));
        Fluid propane = fluid(Materials.Propane.getGas(1));

        Fluid lightlyHydrocrackedHeavy = fluid(Materials.HeavyFuel.getLightlyHydroCracked(1));
        Fluid lightlyHydrocrackedLight = fluid(Materials.LightFuel.getLightlyHydroCracked(1));
        Fluid severelyHydrocrackedGas = fluid(Materials.Gas.getSeverelyHydroCracked(1));
        Fluid lightlySteamCrackedNaphtha = fluid(Materials.Naphtha.getLightlySteamCracked(1));
        Fluid severelySteamCrackedHeavy = fluid(Materials.HeavyFuel.getSeverelySteamCracked(1));
        Fluid severelySteamCrackedLight = fluid(Materials.LightFuel.getSeverelySteamCracked(1));
        Fluid severelySteamCrackedNaphtha = fluid(Materials.Naphtha.getSeverelySteamCracked(1));
        Fluid severelySteamCrackedGas = fluid(Materials.Gas.getSeverelySteamCracked(1));

        // spotless:off
        Object[] displayedProducts = { HEAVY, LIGHT, NAPHTHA, GAS, OCTANE, EthyleneGas, TOLUENE,
            fluid(Materials.Butane.getGas(1)), BUTENE, ethane, propane, propene, butadiene, Benzene, phenol, helium, MethaneGas };

        var heavyHydrocracking = catalog.registerChain(CRACKING, 4, TierEU.RECIPE_EV, HEAVY,
            new Object[] { LIGHT }, new Object[] { HydrogenGas },
            catalog.markRecipe(OilCracker, new Object[] { HEAVY, HydrogenGas }, lightlyHydrocrackedHeavy, 1),
            catalog.markRecipe(DistillationTower, lightlyHydrocrackedHeavy, LIGHT, -1));
        configureCracking(heavyHydrocracking, HYDROCRACKING_CIRCUIT, LIGHT, displayedProducts);

        var octaneRecovery = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, LIGHT,
            new Object[] { OCTANE }, new Object[] { HydrogenGas },
            catalog.markRecipe(OilCracker, new Object[] { LIGHT, HydrogenGas }, lightlyHydrocrackedLight, 1),
            catalog.markRecipe(DistillationTower, lightlyHydrocrackedLight, OCTANE, -1));
        configureCracking(octaneRecovery, OCTANE_CIRCUIT, OCTANE, displayedProducts);

        var gasHydrocracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, GAS,
            new Object[] { helium }, new Object[] { HydrogenGas },
            catalog.markRecipe(OilCracker, new Object[] { GAS, HydrogenGas }, severelyHydrocrackedGas, 3),
            catalog.markRecipe(DistillationTower, severelyHydrocrackedGas, helium, -1));
        configureCracking(gasHydrocracking, HYDROCRACKING_CIRCUIT, helium, displayedProducts);

        var lightSteamCracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, NAPHTHA,
            new Object[] { butadiene, EthyleneGas },
            catalog.markRecipe(OilCracker, new Object[] { NAPHTHA, Steam }, lightlySteamCrackedNaphtha, 1),
            markWholeDustDistillation(catalog, lightlySteamCrackedNaphtha, butadiene));
        configureCracking(lightSteamCracking, LIGHT_STEAM_CRACKING_CIRCUIT, butadiene, displayedProducts);

        var heavySteamCracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, HEAVY,
            new Object[] { Benzene, EthyleneGas },
            catalog.markRecipe(OilCracker, new Object[] { HEAVY, Steam }, severelySteamCrackedHeavy, 3),
            markWholeDustDistillation(catalog, severelySteamCrackedHeavy, Benzene));
        configureCracking(heavySteamCracking, HEAVY_STEAM_CRACKING_CIRCUIT, Benzene, displayedProducts);

        var lightFuelSteamCracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, LIGHT,
            new Object[] { propene, EthyleneGas },
            catalog.markRecipe(OilCracker, new Object[] { LIGHT, Steam }, severelySteamCrackedLight, 3),
            markWholeDustDistillation(catalog, severelySteamCrackedLight, propene));
        configureCracking(lightFuelSteamCracking, HEAVY_STEAM_CRACKING_CIRCUIT, propene, displayedProducts);

        var naphthaSteamCracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, NAPHTHA,
            new Object[] { EthyleneGas },
            catalog.markRecipe(OilCracker, new Object[] { NAPHTHA, Steam }, severelySteamCrackedNaphtha, 3),
            markWholeDustDistillation(catalog, severelySteamCrackedNaphtha, EthyleneGas));
        configureCracking(naphthaSteamCracking, HEAVY_STEAM_CRACKING_CIRCUIT, EthyleneGas, displayedProducts);

        var gasSteamCracking = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, GAS,
            new Object[] { EthyleneGas },
            catalog.markRecipe(OilCracker, new Object[] { GAS, Steam }, severelySteamCrackedGas, 3),
            markWholeDustDistillation(catalog, severelySteamCrackedGas, EthyleneGas));
        configureCracking(gasSteamCracking, HEAVY_STEAM_CRACKING_CIRCUIT, EthyleneGas, displayedProducts);

        var heavyDistillation = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, HEAVY,
            new Object[] { phenol },
            catalog.markRecipe(DistillationTower, HEAVY, phenol, -1));
        configureCracking(heavyDistillation, SEPARATION_CIRCUIT, phenol, displayedProducts);

        var gasDistillation = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, GAS,
            new Object[] { helium },
            catalog.markRecipe(DistillationTower, GAS, helium, -1));
        configureCracking(gasDistillation, SEPARATION_CIRCUIT, helium, displayedProducts);

        // Break down excess light hydrocarbons. Butane remains available for the MTBE blend.
        var ethaneRecovery = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, ethane,
            new Object[] { HydrogenGas, CarbonDust },
            catalog.markRecipe(ElectrolyzerNonCell, ethane, HydrogenGas, -1));
        ethaneRecovery.setDisplayOutputs(CarbonDust, HydrogenGas);
        ethaneRecovery.setPlanningTargets(HydrogenGas);

        var propaneRecovery = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, propane,
            new Object[] { HydrogenGas, CarbonDust },
            catalog.markRecipe(ElectrolyzerNonCell, propane, HydrogenGas, -1));
        propaneRecovery.setDisplayOutputs(CarbonDust, HydrogenGas);
        propaneRecovery.setPlanningTargets(HydrogenGas);

        var propeneRecovery = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, propene,
            new Object[] { HydrogenGas, CarbonDust },
            catalog.markRecipe(ElectrolyzerNonCell, propene, HydrogenGas, -1));
        propeneRecovery.setDisplayOutputs(CarbonDust, HydrogenGas);
        propeneRecovery.setPlanningTargets(HydrogenGas);

        var butadieneRecovery = catalog.registerAlternativeChain(CRACKING, TierEU.RECIPE_EV, butadiene,
            new Object[] { HydrogenGas, CarbonDust },
            catalog.markRecipe(ElectrolyzerNonCell, butadiene, HydrogenGas, -1));
        butadieneRecovery.setDisplayOutputs(CarbonDust, HydrogenGas);
        butadieneRecovery.setPlanningTargets(HydrogenGas);

        catalog.setExistingEntriesKeyOutputs(CRACKING, HEAVY, LIGHT, NAPHTHA, GAS, OCTANE, EthyleneGas, TOLUENE,
            fluid(Materials.Butane.getGas(1)), BUTENE, ethane, propane, propene, butadiene);
        // spotless:on
    }

    private static void configureCracking(Entry entry, int circuit, Fluid target, Object[] displayedProducts) {
        entry.requireCircuit(circuit);
        entry.setDisplayOutputs(displayedProducts);
        entry.setPlanningTargets(target);
    }

    private static StepRef markWholeDustDistillation(BottledChaosRecipeCatalog catalog, Fluid feed, Fluid product) {
        // This source yields fractional dust; combine native batches before forming the route.
        var source = DistillationTower.findRecipeQuery()
            .fluids(new FluidStack(feed, 128_000))
            .filter(
                recipe -> Arrays.stream(recipe.mFluidOutputs)
                    .anyMatch(output -> output != null && output.getFluid() == product))
            .find();
        if (source == null)
            throw new IllegalStateException("Missing cracked-fraction distillation for " + feed.getName());
        return catalog.markPreparedRecipe(
            DistillationTower,
            feed,
            product,
            prepareSourceRecipe(DistillationTower, source).withFullDustOutputs());
    }

    private static void registerFuels(BottledChaosRecipeCatalog catalog) {
        Fluid diesel = fluid(Materials.Diesel.getFluid(1));
        Fluid raw = fluid(Materials.GasolineRaw.getFluid(1));
        Fluid regular = fluid(Materials.GasolineRegular.getFluid(1));
        Fluid premium = fluid(Materials.GasolinePremium.getFluid(1));
        Fluid ethanol = fluid(Materials.Ethanol.getFluid(1));
        Fluid antiKnock = fluid(Materials.AntiKnock.getFluid(1));
        Fluid butane = fluid(Materials.Butane.getGas(1));
        Fluid butaneMtbe = fluid(Materials.MTBEMixtureAlt.getGas(1));
        Fluid nitrousOxide = fluid(Materials.NitrousOxide.getGas(1));
        Fluid ethanolGasoline = fluid(GGMaterial.ethanolGasoline.getFluidOrGas(1));

        // spotless:off
        var dieselBlending = catalog.registerChain(DIESEL, 1, TierEU.RECIPE_EV, LIGHT, new Object[] { diesel },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { LIGHT, HEAVY }, diesel, 24));
        dieselBlending.requireCircuit(DIESEL_CIRCUIT);
        dieselBlending.setDisplayOutputs(diesel);

        var makeEthanol = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, Steam }, ethanol, 24);
        var finishPremium = catalog.markRecipeSequence(LargeChemicalReactor, NitrogenGas, premium,
            catalog.markRecipe(LargeChemicalReactor, new Object[] { NitrogenGas, OxygenGas }, nitrousOxide, 24),
            catalog.markRecipe(LargeChemicalReactor, new Object[] { regular, OCTANE, nitrousOxide, TOLUENE, antiKnock },
                premium, 24));

        // Ethylene can arrive from a cracking route and must remain a feed for both blends.
        var premiumFromEthylene = catalog.registerChain(GASOLINE, 2, TierEU.RECIPE_EV, regular,
            new Object[] { premium }, new Object[] { EthyleneGas }, makeEthanol,
            catalog.markRecipe(LargeChemicalReactor, new Object[] { ethanol, BUTENE }, antiKnock, 24),
            finishPremium);
        premiumFromEthylene.requireCircuit(PREMIUM_GASOLINE_CIRCUIT);
        premiumFromEthylene.setDisplayOutputs(premium);

        // Distillation returns part of the MTBE feed. Balance those returns before blending.
        var mtbe = catalog.markRecipeSequence(LargeChemicalReactor, butane, antiKnock,
            catalog.markRecipe(LargeChemicalReactor, butane, butaneMtbe, 24),
            catalog.markRecipe(DistillationTower, butaneMtbe, antiKnock, -1));
        var premiumFromMtbe = catalog.registerAlternativeChain(GASOLINE, TierEU.RECIPE_EV, regular,
            new Object[] { premium }, mtbe, finishPremium);
        premiumFromMtbe.requireCircuit(PREMIUM_GASOLINE_CIRCUIT);
        premiumFromMtbe.setDisplayOutputs(premium);

        catalog.registerAlternativeChain(GASOLINE, TierEU.RECIPE_EV, raw, new Object[] { regular },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { raw, TOLUENE }, regular, 24))
            .requireCircuit(REGULAR_GASOLINE_CIRCUIT);

        var rawGasoline = catalog.registerAlternativeChain(GASOLINE, TierEU.RECIPE_EV, NAPHTHA,
            new Object[] { raw, Water },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { NAPHTHA, GAS, Methanol, Acetone }, raw, 24));
        rawGasoline.requireCircuit(RAW_GASOLINE_CIRCUIT);
        rawGasoline.setPlanningTargets(raw);
        rawGasoline.setDisplayOutputs(raw);

        var ethanolBlending = catalog.registerChain(ETHANOL_GASOLINE, 1, TierEU.RECIPE_EV, raw,
            new Object[] { ethanolGasoline }, new Object[] { EthyleneGas }, makeEthanol,
            catalog.markRecipe(LargeChemicalReactor, new Object[] { raw, ethanol }, ethanolGasoline, -1));
        ethanolBlending.requireCircuit(ETHANOL_GASOLINE_CIRCUIT);
        ethanolBlending.setDisplayOutputs(ethanolGasoline);

        catalog.setExistingEntriesKeyOutputs(DIESEL);
        catalog.setExistingEntriesKeyOutputs(GASOLINE, raw, regular);
        catalog.setExistingEntriesKeyOutputs(ETHANOL_GASOLINE);
        // spotless:on
    }

    private static Fluid fluid(FluidStack stack) {
        return stack.getFluid();
    }
}
