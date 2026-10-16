package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.GalenaDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumConcentrate;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LeadDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LeadZincSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PurifiedGalena;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PurifiedSphalerite;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SphaleriteDust;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeMark;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;

/** LuV indium extraction; the native comb boost additionally requires the honeycomb-processing tag. */
public final class BottledChaosIndiumRecipes {

    public static void registerRecipeChains(RecipeCatalog catalog) {
        // spotless:off
        // 1. Leach the native purified ores; replace both ore forms with full mineral dusts in the startup cache.
        RecipeMark leaching = catalog.markRecipeWithSubstitutedInputs(LargeChemicalReactor,
            new TST_ItemID[] { PurifiedGalena, PurifiedSphalerite },
            new TST_ItemID[] { GalenaDust, SphaleriteDust }, IndiumConcentrate, 8);
        RecipeMark recovery = catalog.markRecipe(ElectrolyzerNonCell, LeadZincSolution, LeadDust, -1);

        // 2. Baseline: use the full-dust reduction, then recover every lead-zinc solution coproduct.
        catalog.registerChain("indium", 1, TierEU.RECIPE_LuV, SphaleriteDust,
            new Object[] { IndiumDust, LeadDust }, new Object[] {},
            leaching,
            catalog.markRecipe(LargeChemicalReactor, IndiumConcentrate, IndiumDust, 9),
            recovery);

        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) catalog.requireCircuit("indium", 1);

        // 3. Only the two mineral inputs and indium output participate in material NEI queries.
        catalog.setQueryMarkers("indium", new Object[] { SphaleriteDust, GalenaDust }, new Object[] { IndiumDust });
        // spotless:on
    }

    public static void registerCombRecipeChain(RecipeCatalog catalog) {
        // spotless:off
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) {
            // Four native indium combs boost the same mineral batch; both tags must be installed.
            catalog.registerAuthorizedChain("honeycomb", "indium", 1, TierEU.RECIPE_LuV, SphaleriteDust,
                new Object[] { IndiumDust, LeadDust }, new Object[] {},
                catalog.markRecipeWithSubstitutedInputs(LargeChemicalReactor,
                    new TST_ItemID[] { PurifiedGalena, PurifiedSphalerite },
                    new TST_ItemID[] { GalenaDust, SphaleriteDust }, IndiumConcentrate, 8),
                catalog.markRecipe(LargeChemicalReactor,
                    new Object[] { IndiumConcentrate, TST_ItemID.create(GTBees.combs.getStackForType(CombType.INDIUM, 1)) },
                    IndiumDust, 3),
                catalog.markRecipe(ElectrolyzerNonCell, LeadZincSolution, LeadDust, -1));
            catalog.setQueryMarkers("honeycomb", new Object[] { SphaleriteDust, GalenaDust }, new Object[] { IndiumDust });
        }
        // spotless:on
    }
}
