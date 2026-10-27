package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.ChronicSingularity;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EternalSingularity;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EternityDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EternityMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MagMatterRound;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhononCrystalSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhononMedium;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhononicSeedCrystal;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.AlloyBlastSmelter;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ArcFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Autoclave;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.QuantumForceTransformer;

import java.util.Objects;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;

/** Native advanced-material chains; all balancing and QFT expectation calculations happen during cache loading. */
public final class BottledChaosAdvancedMaterialRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        // Main trunk: grow seeds and close the solution return before producing the transfer medium.
        var crystalSolution = catalog.markRecipeSequence(Autoclave, MagMatterRound, PhononCrystalSolution,
            catalog.markRecipe(Autoclave, MagMatterRound, PhononicSeedCrystal, -1),
            catalog.markRecipe(BlastFurnace, new Object[] { PhononicSeedCrystal, EternityDust }, PhononCrystalSolution, 2));
        catalog.registerChain("phonon_medium", 1, TierEU.RECIPE_UXV, MagMatterRound,
            new Object[] { PhononMedium },
            crystalSolution,
            catalog.markRecipe(AlloyBlastSmelter, PhononCrystalSolution, PhononMedium, 5));
        catalog.setExistingEntriesKeyOutputs("phonon_medium", new Object[] { PhononMedium });

        // Main trunk: arc-furnace preparation followed by the expected QFT coproducts.
        if (!Mods.EternalSingularity.isModLoaded()) return;
        Objects.requireNonNull(EternalSingularity, "eternal singularity");
        Objects.requireNonNull(ChronicSingularity, "chronic singularity");
        catalog.registerChain("eternity", 1, TierEU.RECIPE_UMV, EternalSingularity,
            new Object[] { EternityMolten },
            catalog.markRecipe(ArcFurnace, EternalSingularity, ChronicSingularity, -1),
            catalog.markRecipe(QuantumForceTransformer, ChronicSingularity, EternityMolten, -1));
        catalog.setExistingEntriesKeyOutputs("eternity", new Object[] { EternityMolten });
        // spotless:on
    }
}
