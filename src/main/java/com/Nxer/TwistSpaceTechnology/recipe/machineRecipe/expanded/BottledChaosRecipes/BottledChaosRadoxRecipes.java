package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrackedRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedXenoxene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FermentedBacterialSludge;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HeavyRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LightRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Mutagen;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RadoxGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RadoxPolymerMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RawRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.CentrifugeNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Distillery;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.IndustrialCokeOven;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SuperHeavyRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SuperLightRadox;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Xenoxene;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;

import gregtech.api.enums.TierEU;

/** Xenoxene fractionation and four independent fraction routes through molten Radox polymer. */
public final class BottledChaosRadoxRecipes {

    public static void registerRecipeChains(RecipeCatalog catalog) {
        // spotless:off
        // Close the native return once: superlight -> cracked -> gas + light -> returned superlight.
        var recoveredGas = catalog.markRecipeSequence(OilCracker, SuperLightRadox, RadoxGas,
            catalog.markRecipe(OilCracker, SuperLightRadox, CrackedRadox, 24),
            catalog.markRecipe(DistillationTower, CrackedRadox, RadoxGas, -1),
            catalog.markRecipe(OilCracker, LightRadox, SuperLightRadox, 24));

        var heavyCracking = catalog.markRecipe(OilCracker, HeavyRadox, LightRadox, 24);
        var lightCracking = catalog.markRecipe(OilCracker, LightRadox, SuperLightRadox, 24);
        var polymerization = catalog.markRecipe(LargeChemicalReactor, RadoxGas, RadoxPolymerMolten, 2);

        // Each fraction has an independent tag; display order follows fractionation before fraction processing.
        catalog.registerChain("super_light_radox", 1, TierEU.RECIPE_UHV, SuperLightRadox,
            new Object[] { RadoxPolymerMolten }, new Object[] {}, recoveredGas, polymerization);

        // Main line: xenoxene -> coke -> raw Radox distillation -> xenoxene recovery.
        // Cancel the recovered xenoxene against the input; retain every fraction and native coproduct.
        catalog.registerChain("xenoxene", 1, TierEU.RECIPE_UHV, Xenoxene,
            new Object[] { SuperHeavyRadox, HeavyRadox, LightRadox, SuperLightRadox },
            new Object[] { SuperHeavyRadox, HeavyRadox, LightRadox, SuperLightRadox, Mutagen },
            catalog.markRecipe(IndustrialCokeOven, Xenoxene, RawRadox, 24),
            catalog.markRecipe(DistillationTower, RawRadox, DilutedXenoxene, -1),
            catalog.markRecipe(DistillationTower, DilutedXenoxene, Xenoxene, -1),
            // Recover the fermented sludge coproduct within the fractionation entry.
            catalog.markRecipe(Distillery, FermentedBacterialSludge, Mutagen, 3));

        // Superheavy fraction: centrifuge -> heavy -> light -> closed return -> polymer.
        catalog.registerChain("super_heavy_radox", 1, TierEU.RECIPE_UHV, SuperHeavyRadox,
            new Object[] { RadoxPolymerMolten }, new Object[] {},
            catalog.markRecipe(CentrifugeNonCell, SuperHeavyRadox, HeavyRadox, -1),
            heavyCracking, lightCracking, recoveredGas, polymerization);

        // Heavy fraction: heavy -> light -> closed return -> polymer.
        catalog.registerChain("heavy_radox", 1, TierEU.RECIPE_UHV, HeavyRadox,
            new Object[] { RadoxPolymerMolten }, new Object[] {},
            heavyCracking, lightCracking, recoveredGas, polymerization);

        // Light fraction: light -> closed return -> polymer.
        catalog.registerChain("light_radox", 1, TierEU.RECIPE_UHV, LightRadox,
            new Object[] { RadoxPolymerMolten }, new Object[] {}, lightCracking, recoveredGas, polymerization);

        catalog.setQueryMarkers("xenoxene", null,
            new Object[] { SuperHeavyRadox, HeavyRadox, LightRadox, SuperLightRadox, Mutagen });
        catalog.orderDisplayRecipes("radox", Xenoxene, SuperHeavyRadox, HeavyRadox, LightRadox, SuperLightRadox);
        // spotless:on
    }
}
