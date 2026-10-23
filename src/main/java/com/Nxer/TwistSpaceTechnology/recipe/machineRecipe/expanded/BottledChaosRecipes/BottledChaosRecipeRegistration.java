package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AceticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniaGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AmmoniumNitrateSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumFluorideFluid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedHydrochloricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedSulfuricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FermentationBase;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FormicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HexafluorosilicicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrochloricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrofluoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LiquidNitrogen;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MagnesiumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaphthenicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Nitrobenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitrogenDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitrogenMonoxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxalicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhosphoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhthalicAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PotassiumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumNitrateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurDioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurTrioxideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfuricAcid;

import java.util.LinkedHashSet;
import java.util.Set;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.BottledChaosMachineConfiguration;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CredentialModel;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.TimeKey;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;

/** Registers Bottled Chaos processing chains and their display recipes in the required startup order. */
public final class BottledChaosRecipeRegistration {

    public static void registerRecipes() {
        registerBaseRecipes();
        BottledChaosFakeRecipes.registerRecipes();
        BottledChaosFakeRecipes.registerCredentialRecipes();
        BottledChaosStructureRecipes.registerRecipes();
    }

    /** Base sources are available at load-complete; stellar-forge versions are resolved at serverStarted. */
    private static void registerBaseRecipes() {
        BottledChaosRecipeProcessor.clearCache();
        BottledChaosProcessAlternatives.clearPending();
        try {
            BottledChaosRecipeCatalog catalog = new BottledChaosRecipeCatalog();
            BottledChaosTitaniumRecipes.registerRecipeChains(catalog);
            BottledChaosTungstenRecipes.registerRecipeChains(catalog);
            BottledChaosIndiumRecipes.registerRecipeChains(catalog);
            BottledChaosPlatinumRecipes.registerRecipeChains(catalog);
            BottledChaosNetheriteRecipes.registerRecipeChains(catalog);
            BottledChaosPrismaticRecipes.registerRecipeChains(catalog);
            BottledChaosLanthanideRecipes.registerRecipeChains(catalog);
            BottledChaosNaquadahRecipes.registerRecipeChains(catalog);
            BottledChaosNaquadahFuelRecipes.registerRecipeChains(catalog);
            BottledChaosFusionRecipes.registerRecipeChains(catalog);
            BottledChaosBiologicalRecipes.registerRecipeChains(catalog);
            BottledChaosRadoxRecipes.registerRecipeChains(catalog);
            BottledChaosKevlarRecipes.registerRecipeChains(catalog);
            BottledChaosPurifiedWaterRecipes.registerRecipeChains(catalog);
            BottledChaosExoticMatterRecipes.registerRecipeChains(catalog);
            BottledChaosAdvancedMaterialRecipes.registerRecipeChains(catalog);
            BottledChaosIndiumRecipes.registerCombRecipeChains(catalog);
            BottledChaosIsaMillRecipes.registerRecipeChains(catalog);
            BottledChaosPlatinumRecipes.registerCombRecipeChains(catalog);
            catalog.registerDeferredReagents(AmmoniaGas);
            catalog.includeSynthesisPath(
                Nitrobenzene,
                LiquidNitrogen,
                SodiumNitrateDust,
                AmmoniumNitrateDust,
                AmmoniumNitrateSolution,
                AmmoniaGas,
                OxalicAcid,
                HexafluorosilicicAcid,
                AceticAcid,
                BoricAcid,
                FormicAcid,
                FermentationBase);
            catalog.includeDecompositionPath(
                MagnesiumChlorideDust,
                AmmoniaGas,
                NitrogenMonoxideGas,
                NitrogenDioxideGas,
                SulfurDioxideGas,
                SulfurTrioxideGas,
                CalciumFluorideFluid);
            // Compilation applies external reagent boundaries after collecting every synthesis path.
            catalog.registerExternalReagents(
                NitricAcid,
                HydrofluoricAcid,
                HydrochloricAcid,
                SulfuricAcid,
                PhosphoricAcid,
                PhthalicAcid,
                NaphthenicAcid,
                DilutedSulfuricAcid,
                DilutedHydrochloricAcid,
                SodiumChlorideDust,
                PotassiumNitrateDust);
            var compiled = catalog.compile();
            Set<RecipeMap<?>> selectedPools = new LinkedHashSet<>();
            selectedPools.add(RecipeMaps.fusionRecipes);
            selectedPools.add(BottledChaosProcessAlternatives.STELLAR_FORGE);
            selectedPools.add(BottledChaosProcessAlternatives.GODFORGE_MOLTEN);
            selectedPools.add(BottledChaosProcessAlternatives.COOLING);
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
