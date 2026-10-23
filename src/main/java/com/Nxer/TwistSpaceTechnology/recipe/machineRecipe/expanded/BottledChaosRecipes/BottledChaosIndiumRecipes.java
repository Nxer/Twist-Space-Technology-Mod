package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.GalenaDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumConcentrate;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumPhosphateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LeadDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LeadZincSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PurifiedGalena;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PurifiedSphalerite;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RoquesiteDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SphaleriteDust;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;

/** Native indium extraction from mineral dusts, including separately authorized honeycomb boosts. */
public final class BottledChaosIndiumRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        // 1. Leach the native purified ores; replace both ore forms with full mineral dusts in the startup cache.
        StepRef leaching = catalog.markRecipeWithSubstitutedInputs(LargeChemicalReactor,
            new TST_ItemID[] { PurifiedGalena, PurifiedSphalerite },
            new TST_ItemID[] { GalenaDust, SphaleriteDust }, IndiumConcentrate, 8);
        StepRef recovery = catalog.markRecipe(ElectrolyzerNonCell, LeadZincSolution, LeadDust, -1);

        // 2. Baseline: use the full-dust reduction, then recover every lead-zinc solution coproduct.
        var sphalerite = catalog.registerChain("indium", 1, TierEU.RECIPE_LuV, SphaleriteDust,
            new Object[] { IndiumDust, LeadDust }, new Object[] {},
            leaching,
            catalog.markRecipe(LargeChemicalReactor, IndiumConcentrate, IndiumDust, 9),
            recovery);

        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) sphalerite.requireCircuit(1);
        catalog.setExistingEntriesKeyOutputs("indium", new Object[] { IndiumDust });

        var indiumPhosphate = catalog.registerAlternativeChain("indium", TierEU.RECIPE_LV, IndiumPhosphateDust,
            new Object[] { IndiumDust }, new Object[] {},
            catalog.markRecipe(LargeChemicalReactor, IndiumPhosphateDust, IndiumDust, 1));
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) indiumPhosphate.requireCircuit(1);
        var roquesite = catalog.registerAlternativeChain("indium", TierEU.RECIPE_MV, RoquesiteDust,
            new Object[] { IndiumDust }, new Object[] {},
            catalog.markRecipe(ElectrolyzerNonCell, RoquesiteDust, IndiumDust, -1));
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) roquesite.requireCircuit(1);
        // spotless:on
    }

    public static void registerCombRecipeChains(BottledChaosRecipeCatalog catalog) {
        if (!Mods.Forestry.isModLoaded() || GTBees.combs == null) return;
        // spotless:off
        TST_ItemID indiumComb = TST_ItemID.create(GTBees.combs.getStackForType(CombType.INDIUM, 1));
        catalog.registerAuthorizedChain("honeycomb", "indium", 1, TierEU.RECIPE_LuV, SphaleriteDust,
            new Object[] { IndiumDust, LeadDust }, new Object[] {},
            catalog.markRecipeWithSubstitutedInputs(LargeChemicalReactor,
                new TST_ItemID[] { PurifiedGalena, PurifiedSphalerite },
                new TST_ItemID[] { GalenaDust, SphaleriteDust }, IndiumConcentrate, 8),
            catalog.markRecipe(LargeChemicalReactor, new Object[] { IndiumConcentrate, indiumComb }, IndiumDust, 3),
            catalog.markRecipe(ElectrolyzerNonCell, LeadZincSolution, LeadDust, -1));
        catalog.setExistingEntriesKeyOutputs("honeycomb", new Object[] { IndiumDust });
        catalog.registerAugmentedChain("honeycomb", "indium", TierEU.RECIPE_LV, IndiumPhosphateDust,
            new Object[] { IndiumDust }, new Object[] {},
            catalog.markRecipe(LargeChemicalReactor, new Object[] { IndiumPhosphateDust, indiumComb }, IndiumDust, 2));
        catalog.registerAugmentedChain("honeycomb", "indium", TierEU.RECIPE_HV, RoquesiteDust,
            new Object[] { IndiumDust }, new Object[] {},
            catalog.markRecipe(LargeChemicalReactor, new Object[] { RoquesiteDust, indiumComb }, IndiumDust, -1));
        // spotless:on
    }
}
