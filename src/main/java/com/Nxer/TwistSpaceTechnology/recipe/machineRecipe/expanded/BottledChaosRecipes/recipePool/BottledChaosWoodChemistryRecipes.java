package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Acetone;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Benzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Dimethylbenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EthyleneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MethaneGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Methanol;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.IndustrialCokeOven;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver;

import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public final class BottledChaosWoodChemistryRecipes {

    private static final String WOOD_DISTILLATION = "wood_distillation";
    private static final String WOOD_OXYGENATES = "wood_oxygenates";

    private static final int ETHANOL_CIRCUIT = 1;
    private static final int ACETIC_ACID_CIRCUIT = 2;
    private static final int ACETONE_CIRCUIT = 3;

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
        Object[] aromaticMarkers = { Benzene, toluene, phenol, Dimethylbenzene };
        Object[] vinegarMarkers = { Methanol, ethanol, AceticAcid, Acetone, methylAcetate };
        Object[] gasMarkers = { MethaneGas, EthyleneGas };

        var pyrolysisRoute = catalog.registerChain(WOOD_DISTILLATION, 3, TierEU.RECIPE_HV, logs,
            new Object[] { charcoal, byproducts }, woodPyrolysis);
        pyrolysisRoute.setDisplayOutputs(byproducts);
        pyrolysisRoute.setPlanningTargets(byproducts);
        catalog.registerAlternativeChain(WOOD_DISTILLATION, TierEU.RECIPE_HV, byproducts,
            new Object[] { tar, vinegar, woodGas, Dimethylbenzene }, separateByproducts)
            .setDisplayOutputs(tar, vinegar, woodGas, Dimethylbenzene);
        catalog.registerAlternativeChain(WOOD_DISTILLATION, TierEU.RECIPE_HV, charcoal, new Object[] { tar },
            catalog.markRecipe(FluidExtractor, charcoal, tar, -1)).setDisplayOutputs(tar);

        var woodAromatics = catalog.registerAlternativeChain(
            WOOD_DISTILLATION, TierEU.RECIPE_HV, tar, tarProducts, separateTar);
        woodAromatics.setDisplayOutputs(aromaticMarkers);
        woodAromatics.setPlanningTargets(Benzene, Dimethylbenzene);

        var woodOxygenates = catalog.registerAlternativeChain(
            WOOD_DISTILLATION, TierEU.RECIPE_HV, vinegar, vinegarProducts, separateVinegar);
        woodOxygenates.setDisplayOutputs(vinegarMarkers);
        woodOxygenates.setPlanningTargets(Methanol, AceticAcid, Acetone);

        var woodGases = catalog.registerAlternativeChain(
            WOOD_DISTILLATION, TierEU.RECIPE_HV, woodGas, gasProducts, separateGas);
        woodGases.setDisplayOutputs(gasMarkers);
        woodGases.setPlanningTargets(MethaneGas, EthyleneGas);

        var hydration = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, Steam }, ethanol, 24);
        var oxidation = catalog.markRecipe(LargeChemicalReactor, new Object[] { EthyleneGas, OxygenGas }, AceticAcid, 8);
        var acetoneSynthesis = catalog.markRecipe(LargeChemicalReactor, new Object[] { AceticAcid, CalciumDust }, Acetone, 3);
        catalog.registerChain(WOOD_OXYGENATES, 2, TierEU.RECIPE_HV, EthyleneGas, new Object[] { ethanol }, hydration)
            .requireCircuit(ETHANOL_CIRCUIT);
        var aceticAcidRoute = catalog.registerAlternativeChain(
            WOOD_OXYGENATES, TierEU.RECIPE_HV, EthyleneGas, new Object[] { AceticAcid }, oxidation);
        aceticAcidRoute.requireCircuit(ACETIC_ACID_CIRCUIT);
        aceticAcidRoute.setDisplayOutputs(AceticAcid);
        // Retain carbon dioxide as in wood-gas distillation to avoid expanding the acetone batch for dust output.
        var acetoneRoute = catalog.registerAlternativeChain(WOOD_OXYGENATES, TierEU.RECIPE_HV, EthyleneGas,
            new Object[] { Acetone, CarbonDioxideGas }, oxidation, acetoneSynthesis);
        acetoneRoute.requireCircuit(ACETONE_CIRCUIT);
        acetoneRoute.setPlanningTargets(Acetone);

        // Keep charcoal as a byproduct while feeding the separated fractions to their own routes.
        catalog.setExistingEntriesKeyOutputs(WOOD_DISTILLATION,
            byproducts, tar, vinegar, woodGas, EthyleneGas, ethanol, Methanol, Acetone);
        catalog.setExistingEntriesKeyOutputs(WOOD_OXYGENATES, ethanol, Methanol, Acetone);
        catalog.orderDisplayRecipes("wood_chemistry", logs, byproducts, charcoal, tar, vinegar, woodGas, EthyleneGas);
        // spotless:on
    }

    private BottledChaosWoodChemistryRecipes() {}
}
