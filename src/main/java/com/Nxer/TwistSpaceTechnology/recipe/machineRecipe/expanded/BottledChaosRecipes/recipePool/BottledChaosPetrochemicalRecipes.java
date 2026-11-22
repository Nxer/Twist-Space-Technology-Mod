package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Acetone;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Benzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EthyleneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenSulfideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Methanol;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaphthenicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.Entry;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;

public final class BottledChaosPetrochemicalRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        Fluid heavy = Materials.HeavyFuel.getFluid(1)
            .getFluid();
        Fluid light = Materials.LightFuel.getFluid(1)
            .getFluid();
        Fluid naphtha = Materials.Naphtha.getFluid(1)
            .getFluid();
        Fluid gas = Materials.Gas.getGas(1)
            .getFluid();
        Fluid octane = Materials.Octane.getFluid(1)
            .getFluid();
        Fluid propene = Materials.Propene.getGas(1)
            .getFluid();
        Fluid butadiene = Materials.Butadiene.getGas(1)
            .getFluid();
        Fluid butene = Materials.Butene.getGas(1)
            .getFluid();
        Fluid helium = Materials.Helium.getGas(1)
            .getFluid();
        Fluid diesel = Materials.Diesel.getFluid(1)
            .getFluid();
        Fluid boostedDiesel = Materials.NitroFuel.getFluid(1)
            .getFluid();
        Fluid raw = Materials.GasolineRaw.getFluid(1)
            .getFluid();
        Fluid regular = Materials.GasolineRegular.getFluid(1)
            .getFluid();
        Fluid premium = Materials.GasolinePremium.getFluid(1)
            .getFluid();
        Fluid additive = Materials.AntiKnock.getFluid(1)
            .getFluid();
        Fluid nitrousOxide = Materials.NitrousOxide.getGas(1)
            .getFluid();
        Fluid toluene = Materials.Toluene.getFluid(1)
            .getFluid();

        // spotless:off
        var sweetHeavy = desulfurize(catalog, Materials.SulfuricHeavyFuel.getFluid(1), heavy);
        var sweetLight = desulfurize(catalog, Materials.SulfuricLightFuel.getFluid(1), light);
        var sweetNaphtha = desulfurize(catalog, Materials.SulfuricNaphtha.getFluid(1), naphtha);
        var sweetGas = desulfurize(catalog, Materials.SulfuricGas.getGas(1), gas);
        Object[] cuts = { heavy, light, naphtha, gas, NaphthenicAcid, HydrogenSulfideGas };
        for (Materials crude : new Materials[] { Materials.Oil, Materials.OilLight, Materials.OilMedium, Materials.OilHeavy }) {
            Fluid feed = crude.getFluid(1).getFluid();
            var tower = catalog.markRecipe(DistillationTower, feed, NaphthenicAcid, 9);
            var refining = crude == Materials.Oil
                ? catalog.registerChain("petroleum_refining", 2, TierEU.RECIPE_EV, feed, cuts, tower,
                    sweetHeavy, sweetLight, sweetNaphtha, sweetGas)
                : catalog.registerAlternativeChain("petroleum_refining", TierEU.RECIPE_EV, feed, cuts, tower,
                    sweetHeavy, sweetLight, sweetNaphtha, sweetGas);
            refining.useExplicitLinks();
            refining.setDisplayOutputs(heavy, light, naphtha, gas);
            refining.setPlanningTargets(heavy, light, naphtha, gas);
            refining.allowSupplyTo("petroleum_cracking", heavy, light, naphtha, gas);
            refining.allowSupplyTo("diesel_blending", light, heavy);
            refining.allowSupplyTo("gasoline_blending", naphtha, gas);
            refining.allowSupplyTo("ethanol_gasoline_blending", naphtha, gas);
        }

        var naturalGas = Materials.NaturalGas.getGas(1);
        var refineNaturalGas = catalog.registerAlternativeChain("petroleum_refining", TierEU.RECIPE_EV, naturalGas.getFluid(),
            new Object[] { gas, HydrogenSulfideGas }, desulfurize(catalog, naturalGas, gas));
        refineNaturalGas.useExplicitLinks();
        refineNaturalGas.setDisplayOutputs(gas);
        refineNaturalGas.setPlanningTargets(gas);
        refineNaturalGas.allowSupplyTo("petroleum_cracking", gas);
        refineNaturalGas.allowSupplyTo("gasoline_blending", gas);
        refineNaturalGas.allowSupplyTo("ethanol_gasoline_blending", gas);

        // Recovery accepts sulfide or alkanes sent back through an input, avoiding automatic use of coproducts.
        var sulfideRecovery = catalog.registerAlternativeChain("petroleum_refining", TierEU.RECIPE_EV, HydrogenSulfideGas,
            new Object[] { HydrogenGas, SulfurDust },
            catalog.markRecipe(ElectrolyzerNonCell, HydrogenSulfideGas, new Object[] { HydrogenGas, SulfurDust }, -1));
        sulfideRecovery.useExternalInputs(HydrogenSulfideGas);
        sulfideRecovery.useExplicitLinks();
        sulfideRecovery.setDisplayOutputs(HydrogenGas);

        // Circuits select the treatment of the supplied fraction.
        Entry[] cracking = {
            crack(catalog, heavy, Materials.HeavyFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, 1, light, true),
            crack(catalog, heavy, Materials.HeavyFuel.getSeverelySteamCracked(1_000), Steam, 3, 3, Benzene, false),
            crack(catalog, light, Materials.LightFuel.getLightlyHydroCracked(1_000), HydrogenGas, 1, 1, octane, false),
            crack(catalog, light, Materials.LightFuel.getSeverelySteamCracked(1_000), Steam, 3, 3, propene, false),
            crack(catalog, naphtha, Materials.Naphtha.getLightlySteamCracked(1_000), Steam, 1, 2, butadiene, false),
            crack(catalog, naphtha, Materials.Naphtha.getSeverelySteamCracked(1_000), Steam, 3, 3, EthyleneGas, false),
            crack(catalog, gas, Materials.Gas.getSeverelyHydroCracked(1_000), HydrogenGas, 3, 1, helium, false),
            crack(catalog, gas, Materials.Gas.getSeverelySteamCracked(1_000), Steam, 3, 3, EthyleneGas, false)
        };
        for (Entry entry : cracking) {
            entry.allowSupplyTo("petrochemical_oxygenates", EthyleneGas);
            entry.allowSupplyTo("diesel_blending", light, heavy);
            entry.allowSupplyTo("gasoline_blending", naphtha, toluene, butene, octane);
            entry.allowSupplyTo("ethanol_gasoline_blending", naphtha);
            entry.allowSupplyTo("benzene_nitration", Benzene);
        }

        Fluid phenol = Materials.Phenol.getFluid(1).getFluid();
        var heavyDistillation = catalog.registerAlternativeChain("petroleum_cracking", TierEU.RECIPE_EV, heavy,
            new Object[] { Benzene, toluene, phenol }, catalog.markRecipe(DistillationTower, heavy, phenol, -1));
        heavyDistillation.requireCircuit(4);
        heavyDistillation.useExplicitLinks();
        heavyDistillation.allowSupplyTo("gasoline_blending", toluene);
        heavyDistillation.allowSupplyTo("benzene_nitration", Benzene);

        var gasDistillation = catalog.registerAlternativeChain("petroleum_cracking", TierEU.RECIPE_EV, gas,
            new Object[] { helium, Materials.Methane.getGas(1).getFluid() },
            catalog.markRecipe(DistillationTower, gas, helium, -1));
        gasDistillation.requireCircuit(4);
        gasDistillation.useExplicitLinks();
        for (Materials alkane : new Materials[] { Materials.Ethane, Materials.Propane, Materials.Butane }) {
            Fluid feed = alkane.getGas(1).getFluid();
            var alkaneRecovery = catalog.registerAlternativeChain("petroleum_cracking", TierEU.RECIPE_EV, feed,
                new Object[] { HydrogenGas, CarbonDust },
                catalog.markRecipe(ElectrolyzerNonCell, feed, new Object[] { HydrogenGas, CarbonDust }, -1));
            alkaneRecovery.useExternalInputs(feed);
            alkaneRecovery.useExplicitLinks();
            alkaneRecovery.setDisplayOutputs(HydrogenGas);
        }

        Fluid ethanol = Materials.Ethanol.getFluid(1).getFluid();
        var oxidation = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, OxygenGas }, AceticAcid, 8);
        var ethanolSynthesis = catalog.registerChain("petrochemical_oxygenates", 2, TierEU.RECIPE_EV, EthyleneGas,
            new Object[] { ethanol }, catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, Steam }, ethanol, 24));
        ethanolSynthesis.requireCircuit(5);
        ethanolSynthesis.useExplicitLinks();
        ethanolSynthesis.allowSupplyTo("gasoline_blending", ethanol);
        ethanolSynthesis.allowSupplyTo("ethanol_gasoline_blending", ethanol);
        var oxidationEntry = catalog.registerAlternativeChain("petrochemical_oxygenates", TierEU.RECIPE_EV, EthyleneGas,
            new Object[] { AceticAcid }, oxidation);
        oxidationEntry.requireCircuit(6);
        oxidationEntry.useExplicitLinks();
        var acetoneSynthesis = catalog.registerAlternativeChain("petrochemical_oxygenates", TierEU.RECIPE_EV, EthyleneGas,
            new Object[] { Acetone, CarbonDioxideGas, Water }, oxidation,
            catalog.markRecipe(LargeChemicalReactor, new Object[] { AceticAcid, CalciumDust }, Acetone, 3));
        acetoneSynthesis.requireCircuit(7);
        acetoneSynthesis.useExplicitLinks();
        acetoneSynthesis.setDisplayOutputs(Acetone);
        acetoneSynthesis.setPlanningTargets(Acetone);
        acetoneSynthesis.allowSupplyTo("gasoline_blending", Acetone);
        acetoneSynthesis.allowSupplyTo("ethanol_gasoline_blending", Acetone);
        var methanol = catalog.registerAlternativeChain("petrochemical_oxygenates", TierEU.RECIPE_EV, CarbonDioxideGas,
            new Object[] { Methanol, Water },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { CarbonDioxideGas, HydrogenGas }, Methanol, 1));
        methanol.useExternalInputs(CarbonDioxideGas);
        methanol.useExplicitLinks();
        methanol.setDisplayOutputs(Methanol);
        methanol.setPlanningTargets(Methanol);
        methanol.allowSupplyTo("gasoline_blending", Methanol);
        methanol.allowSupplyTo("ethanol_gasoline_blending", Methanol);

        var ordinaryDiesel = catalog.registerChain("diesel_blending", 1, TierEU.RECIPE_EV, light,
            new Object[] { diesel }, catalog.markRecipe(LargeChemicalReactor, new Object[] { light, heavy }, diesel, 24));
        ordinaryDiesel.useExplicitLinks();
        ordinaryDiesel.allowSupplyTo("diesel_blending", diesel);
        catalog.registerAlternativeChain("diesel_blending", TierEU.RECIPE_EV, diesel, new Object[] { boostedDiesel },
            catalog.markRecipe(LargeChemicalReactor, diesel, boostedDiesel, 24))
            .useExplicitLinks();

        var makeRaw = catalog.markRecipe(LargeChemicalReactor, new Object[] { naphtha, gas, Methanol, Acetone }, raw, 24);
        var rawBlending = catalog.registerChain("gasoline_blending", 2, TierEU.RECIPE_EV, naphtha, new Object[] { raw }, makeRaw);
        rawBlending.useExternalInputs(Methanol, Acetone);
        rawBlending.useExplicitLinks();
        rawBlending.allowSupplyTo("gasoline_blending", raw);
        rawBlending.allowSupplyTo("ethanol_gasoline_blending", raw);
        var regularGasoline = catalog.registerAlternativeChain("gasoline_blending", TierEU.RECIPE_EV, raw,
            new Object[] { regular }, catalog.markRecipe(LargeChemicalReactor, new Object[] { raw, toluene }, regular, 24));
        regularGasoline.useExplicitLinks();
        regularGasoline.allowSupplyTo("gasoline_blending", regular);
        Fluid ethanolGasoline = GGMaterial.ethanolGasoline.getFluidOrGas(1).getFluid();
        var mixEthanol = catalog.markRecipe(LargeChemicalReactor, new Object[] { raw, ethanol }, ethanolGasoline, -1);
        catalog.registerChain("ethanol_gasoline_blending", 1, TierEU.RECIPE_EV, raw, new Object[] { ethanolGasoline },
            mixEthanol).useExplicitLinks();
        var ethanolFromNaphtha = catalog.registerAlternativeChain("ethanol_gasoline_blending", TierEU.RECIPE_EV, naphtha,
            new Object[] { ethanolGasoline }, makeRaw, mixEthanol);
        ethanolFromNaphtha.useExternalInputs(Methanol, Acetone);
        ethanolFromNaphtha.useExplicitLinks();
        var blending = catalog.registerAlternativeChain("gasoline_blending", TierEU.RECIPE_EV, regular,
            new Object[] { premium }, catalog.markRecipe(LargeChemicalReactor, regular, premium, 24));
        blending.includeSynthesisStep(LargeChemicalReactor, additive, butene, 24);
        // Supplying nitrous oxide opts in to the premium upgrade.
        blending.useExternalInputs(nitrousOxide);
        blending.useExplicitLinks();
        // spotless:on
    }

    private static StepRef desulfurize(BottledChaosRecipeCatalog catalog, FluidStack sour, Fluid clean) {
        return catalog.markRecipe(LargeChemicalReactor, sour.getFluid(), new Object[] { clean, HydrogenSulfideGas }, 4);
    }

    private static Entry crack(BottledChaosRecipeCatalog catalog, Fluid feed, FluidStack cracked, Fluid agent,
        int nativeCircuit, int selector, Fluid target, boolean primary) {
        var cracking = catalog.markRecipe(OilCracker, new Object[] { feed, agent }, cracked.getFluid(), nativeCircuit);
        var separation = catalog.markDistillation(cracked, target, -1);
        // Steam cracking exports its ethylene even when another hydrocarbon is the selected target.
        Object[] products = agent == Steam && target != EthyleneGas ? new Object[] { target, EthyleneGas }
            : new Object[] { target };
        var entry = primary
            ? catalog.registerChain(
                "petroleum_cracking",
                4,
                TierEU.RECIPE_EV,
                feed,
                products,
                new Object[] { agent },
                cracking,
                separation)
            : catalog.registerAlternativeChain(
                "petroleum_cracking",
                TierEU.RECIPE_EV,
                feed,
                products,
                new Object[] { agent },
                cracking,
                separation);
        entry.requireCircuit(selector);
        entry.useExplicitLinks();
        entry.setDisplayOutputs(products);
        entry.setPlanningTargets(target);
        return entry;
    }

    private BottledChaosPetrochemicalRecipes() {}
}
