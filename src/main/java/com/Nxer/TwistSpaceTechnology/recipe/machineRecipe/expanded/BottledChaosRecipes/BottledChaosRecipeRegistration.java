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

/** Registers Bottled Chaos processing chains and their display recipes in the required startup order. */
public final class BottledChaosRecipeRegistration {

    public static void registerRecipes() {
        BottledChaosRecipeProcessor.initializeRecipeCache(catalog -> {
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
            catalog.registerInputSyntheses(
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
            catalog.registerOutputDecompositions(
                MagnesiumChlorideDust,
                AmmoniaGas,
                NitrogenMonoxideGas,
                NitrogenDioxideGas,
                SulfurDioxideGas,
                SulfurTrioxideGas,
                CalciumFluorideFluid);
            // Apply reagent boundaries last so every family retains these inputs and recovered acids can cancel.
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
        });
        BottledChaosFakeRecipes.registerRecipes();
        BottledChaosFakeRecipes.registerCredentialRecipes();
        BottledChaosStructureRecipes.registerRecipes();
    }
}
