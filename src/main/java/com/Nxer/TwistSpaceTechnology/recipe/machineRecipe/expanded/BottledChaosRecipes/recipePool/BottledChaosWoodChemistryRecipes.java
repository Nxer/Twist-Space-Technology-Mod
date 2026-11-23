package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Acetone;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Benzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Dimethylbenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DistilledWater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EthyleneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MethaneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Methanol;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Nitrobenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalPlant;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.IndustrialCokeOven;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfuricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.Arrays;
import java.util.stream.Stream;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public final class BottledChaosWoodChemistryRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        Fluid tar = Materials.WoodTar.getFluid(1)
            .getFluid();
        Fluid vinegar = Materials.WoodVinegar.getFluid(1)
            .getFluid();
        Fluid woodGas = Materials.WoodGas.getGas(1)
            .getFluid();
        Fluid byproducts = Materials.CharcoalByproducts.getGas(1)
            .getFluid();
        var charcoal = recipeItemId(Materials.Charcoal.getGems(1));
        var byproductStack = Materials.CharcoalByproducts.getGas(1_000);

        // Look up with oak but retain the native logWood matcher and its actual representative in the route.
        var oven = BottledChaosSourceResolver.require(
            IndustrialCokeOven,
            new ItemStack[] { new ItemStack(Blocks.log, 16), GTUtility.getIntegratedCircuit(3) });
        var pyrolysis = BottledChaosRecipeSupport.prepareSourceRecipe(IndustrialCokeOven, oven);
        if (pyrolysis.inputs.size() != 1)
            throw new IllegalStateException("Expected a single log feed for wood distillation");
        Object logs = pyrolysis.inputs.keySet()
            .iterator()
            .next();

        // spotless:off
        var woodPyrolysis = catalog.markPreparedRecipe(
            IndustrialCokeOven, logs, new Object[] { charcoal, byproducts }, pyrolysis);
        var nativeByproductSeparation = BottledChaosSourceResolver.require(
            DistillationTower, new ItemStack[] { GTUtility.getIntegratedCircuit(1) }, byproductStack);
        var separateByproducts = catalog.markPreparedRecipe(DistillationTower, byproducts, Dimethylbenzene,
            BottledChaosRecipeSupport.prepareSourceRecipe(DistillationTower, nativeByproductSeparation).withFullDustOutputs());
        var separateTar = catalog.markRecipe(DistillationTower, tar, Dimethylbenzene, 1);
        var separateVinegar = catalog.markRecipe(DistillationTower, vinegar, Methanol, 1);
        var separateGas = catalog.markRecipe(DistillationTower, woodGas, EthyleneGas, 1);

        Fluid phenol = Materials.Phenol.getFluid(1).getFluid();
        Fluid toluene = Materials.Toluene.getFluid(1).getFluid();
        Fluid ethanol = Materials.Ethanol.getFluid(1).getFluid();
        Fluid methylAcetate = Materials.MethylAcetate.getFluid(1).getFluid();
        Object[] tarProducts = { Materials.Creosote.getFluid(1).getFluid(), phenol, Benzene, toluene, Dimethylbenzene };
        Object[] vinegarProducts = { AceticAcid, Water, ethanol, Methanol, Acetone, methylAcetate };
        Object[] gasProducts = { Materials.CarbonDioxide.getGas(1).getFluid(), EthyleneGas, MethaneGas,
            Materials.CarbonMonoxide.getGas(1).getFluid(), HydrogenGas };
        Object[] woodProducts = Stream.of(new Object[] { charcoal }, tarProducts, vinegarProducts, gasProducts)
            .flatMap(Arrays::stream)
            .toArray();
        Object[] aromaticMarkers = { Benzene, toluene, phenol, Dimethylbenzene };
        Object[] vinegarMarkers = { Methanol, ethanol, AceticAcid, Acetone, methylAcetate };
        Object[] gasMarkers = { MethaneGas, EthyleneGas };

        StepRef[] woodSeparation = { woodPyrolysis, separateByproducts, separateTar, separateVinegar, separateGas };
        var woodDistillation = catalog.registerChain("wood_distillation", 3, TierEU.RECIPE_HV, logs, woodProducts,
            woodSeparation);
        woodDistillation.setDisplayOutputs(Stream.of(aromaticMarkers, vinegarMarkers, gasMarkers)
            .flatMap(Arrays::stream).toArray());
        woodDistillation.setPlanningTargets(Benzene, Methanol, MethaneGas);

        // The standalone intermediate entries let players stop at a feedstock of their choice.
        catalog.registerAlternativeChain("wood_distillation", TierEU.RECIPE_HV, byproducts,
            new Object[] { tar, vinegar, woodGas, Dimethylbenzene }, separateByproducts)
            .setDisplayOutputs(tar, vinegar, woodGas, Dimethylbenzene);
        catalog.registerAlternativeChain("wood_distillation", TierEU.RECIPE_HV, charcoal, new Object[] { tar },
            catalog.markRecipe(FluidExtractor, charcoal, tar, -1)).setDisplayOutputs(tar);

        var woodAromatics = catalog.registerAlternativeChain(
            "wood_distillation", TierEU.RECIPE_HV, tar, tarProducts, separateTar);
        woodAromatics.setDisplayOutputs(aromaticMarkers);
        woodAromatics.setPlanningTargets(Benzene, Dimethylbenzene);

        var woodOxygenates = catalog.registerAlternativeChain(
            "wood_distillation", TierEU.RECIPE_HV, vinegar, vinegarProducts, separateVinegar);
        woodOxygenates.setDisplayOutputs(vinegarMarkers);
        woodOxygenates.setPlanningTargets(Methanol, AceticAcid, Acetone);

        var woodGases = catalog.registerAlternativeChain(
            "wood_distillation", TierEU.RECIPE_HV, woodGas, gasProducts, separateGas);
        woodGases.setDisplayOutputs(gasMarkers);
        woodGases.setPlanningTargets(MethaneGas, EthyleneGas);

        var nitration = catalog.markRecipe(ChemicalPlant,
            new Object[] { Benzene, SulfuricAcid, NitricAcid, DistilledWater }, Nitrobenzene, 12);
        catalog.registerChain("benzene_nitration", 1, TierEU.RECIPE_HV, Benzene, new Object[] { Nitrobenzene },
            nitration).setDisplayOutputs(Nitrobenzene);
        var tarNitration = catalog.registerAlternativeChain("benzene_nitration", TierEU.RECIPE_HV, tar,
            productsAfter(tarProducts, Nitrobenzene, Benzene), separateTar, nitration);
        tarNitration.setDisplayOutputs(Nitrobenzene);
        tarNitration.setPlanningTargets(Nitrobenzene);
        // Water from the vinegar fraction can be consumed by the existing distilled-water synthesis.
        var woodNitration = catalog.registerAlternativeChain("benzene_nitration", TierEU.RECIPE_HV, logs,
            productsAfter(woodProducts, Nitrobenzene, Benzene, Water), appendSteps(woodSeparation, nitration));
        woodNitration.setDisplayOutputs(Nitrobenzene);
        woodNitration.setPlanningTargets(Nitrobenzene);

        var hydration = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, Steam }, ethanol, 24);
        var oxidation = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, OxygenGas }, AceticAcid, 8);
        var acetoneSynthesis = catalog.markRecipe(LargeChemicalReactor, new Object[] { AceticAcid, CalciumDust }, Acetone, 3);
        registerEthyleneProducts(catalog, woodGas, gasProducts, new StepRef[] { separateGas }, ethanol, hydration,
            oxidation, acetoneSynthesis);
        registerEthyleneProducts(catalog, logs, woodProducts, woodSeparation, ethanol, hydration, oxidation,
            acetoneSynthesis);
        catalog.setExistingEntriesKeyOutputs("wood_distillation", ethanol, Methanol, Acetone);
        catalog.orderDisplayRecipes("wood_chemistry", logs, byproducts, charcoal, tar, vinegar, woodGas, Benzene);
        // spotless:on
    }

    private static void registerEthyleneProducts(BottledChaosRecipeCatalog catalog, Object feed, Object[] coproducts,
        StepRef[] separation, Fluid ethanol, StepRef hydration, StepRef oxidation, StepRef acetoneSynthesis) {
        var ethanolRoute = catalog.registerAlternativeChain(
            "petrochemical_oxygenates",
            TierEU.RECIPE_EV,
            feed,
            productsAfter(coproducts, ethanol, EthyleneGas),
            appendSteps(separation, hydration));
        ethanolRoute.requireCircuit(5);
        ethanolRoute.setDisplayOutputs(ethanol);
        ethanolRoute.setPlanningTargets(ethanol);

        var aceticAcidRoute = catalog.registerAlternativeChain(
            "petrochemical_oxygenates",
            TierEU.RECIPE_EV,
            feed,
            productsAfter(coproducts, AceticAcid, EthyleneGas),
            appendSteps(separation, oxidation));
        aceticAcidRoute.requireCircuit(6);
        aceticAcidRoute.setDisplayOutputs(AceticAcid);
        aceticAcidRoute.setPlanningTargets(AceticAcid);

        var acetoneRoute = catalog.registerAlternativeChain(
            "petrochemical_oxygenates",
            TierEU.RECIPE_EV,
            feed,
            productsAfter(coproducts, Acetone, EthyleneGas, AceticAcid),
            appendSteps(separation, oxidation, acetoneSynthesis));
        acetoneRoute.requireCircuit(7);
        acetoneRoute.setDisplayOutputs(Acetone);
        acetoneRoute.setPlanningTargets(Acetone);
    }

    private static Object[] productsAfter(Object[] products, Object product, Object... consumed) {
        var consumedProducts = Arrays.asList(consumed);
        return Stream.concat(
            Arrays.stream(products)
                .filter(output -> !consumedProducts.contains(output)),
            Stream.of(product))
            .distinct()
            .toArray();
    }

    private static StepRef[] appendSteps(StepRef[] steps, StepRef... downstream) {
        StepRef[] chain = Arrays.copyOf(steps, steps.length + downstream.length);
        System.arraycopy(downstream, 0, chain, steps.length, downstream.length);
        return chain;
    }

    private BottledChaosWoodChemistryRecipes() {}
}
