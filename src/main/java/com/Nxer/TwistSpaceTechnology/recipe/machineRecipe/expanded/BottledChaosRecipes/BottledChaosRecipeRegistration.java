package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import java.util.LinkedHashSet;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool.BottledChaosRecipePool;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;

/** Registers Bottled Chaos processing chains and their display recipes in the required startup order. */
public final class BottledChaosRecipeRegistration {

    public static void registerRecipes() {
        registerBaseRecipes();
        BottledChaosFakeRecipes.registerRecipes();
        BottledChaosFakeRecipes.registerCredentialRecipes();
        BottledChaosRecipePool.registerCraftingRecipes();
        BottledChaosStructureRecipes.registerRecipes();
    }

    /** Base sources are available at load-complete; stellar-forge versions are resolved at serverStarted. */
    private static void registerBaseRecipes() {
        BottledChaosRecipeProcessor.clearCache();
        BottledChaosProcessAlternatives.clearPending();
        try {
            BottledChaosRecipeCatalog catalog = new BottledChaosRecipeCatalog();
            BottledChaosRecipePool.registerRecipeChains(catalog);
            var compiled = catalog.compile();
            Set<RecipeMap<?>> selectedPools = new LinkedHashSet<>();
            selectedPools.add(RecipeMaps.fusionRecipes);
            selectedPools.addAll(
                BottledChaosProcessAlternatives.displayGroup()
                    .alternatives());
            for (var route : compiled.routes())
                for (TimeKey time : route.data().times.keySet()) selectedPools.add(time.map());
            BottledChaosMachineConfiguration.initialize(selectedPools.toArray(new RecipeMap<?>[0]));
            CredentialModel.publishCapacityCosts(compiled.capacityCosts());
            BottledChaosRecipeProcessor.installCache(compiled);
            TwistSpaceTechnology.LOG.info("Bottled Chaos resolved {} processing steps.", compiled.resolvedStepCount());
        } catch (RuntimeException exception) {
            TwistSpaceTechnology.LOG
                .error("Bottled Chaos recipe resolution failed; processing routes are disabled.", exception);
        }
    }

    /** Called after StellarForgeRecipePool has built its native recipes; base-cache readiness is independent. */
    public static void resolveLateSources() {
        BottledChaosProcessAlternatives.resolve();
    }
}
