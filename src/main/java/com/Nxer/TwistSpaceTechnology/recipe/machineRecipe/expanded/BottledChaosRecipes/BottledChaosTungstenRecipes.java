package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciteDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciumTungstateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrochloricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IronTungstateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.LithiumTungstateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.ManganeseTungstateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Autoclave;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumTungstate;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TungstenTrioxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TungsticAcidDust;

import gregtech.api.enums.TierEU;

/** LuV mineral processing; alternative sources and consecutive stages share one credential tag. */
public final class BottledChaosTungstenRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        // 1. Final stage: acidify scheelite, then dehydrate tungstic acid to tungsten trioxide.
        catalog.registerChain("tungsten", 1, TierEU.RECIPE_LuV, CalciumTungstateDust,
            new Object[] { TungstenTrioxideDust }, new Object[] {},
            catalog.markRecipe(LargeChemicalReactor, CalciumTungstateDust, TungsticAcidDust, -1),
            catalog.markRecipe(BlastFurnace, TungsticAcidDust, TungstenTrioxideDust, -1));

        // 2. Alternative mineral entries: preprocess each complete line through scheelite to tungsten trioxide.
        catalog.registerAlternativeChain("tungsten", TierEU.RECIPE_LuV, LithiumTungstateDust,
            new Object[] { TungstenTrioxideDust }, new Object[] {},
            catalog.markRecipe(Autoclave, LithiumTungstateDust, SodiumTungstate, -1),
            catalog.markRecipe(LargeChemicalReactor, SodiumTungstate, CalciumTungstateDust, -1),
            catalog.markRecipe(LargeChemicalReactor, CalciumTungstateDust, TungsticAcidDust, -1),
            catalog.markRecipe(BlastFurnace, TungsticAcidDust, TungstenTrioxideDust, -1));
        catalog.registerChainInputSynthesis("tungsten", LargeChemicalReactor, CalciumChlorideDust,
            new Object[] { CalciteDust, HydrochloricAcid }, -1);
        catalog.registerAlternativeChain("tungsten", TierEU.RECIPE_LuV, ManganeseTungstateDust,
            new Object[] { TungstenTrioxideDust }, new Object[] {},
            catalog.markRecipe(Autoclave, ManganeseTungstateDust, SodiumTungstate, -1),
            catalog.markRecipe(LargeChemicalReactor, SodiumTungstate, CalciumTungstateDust, -1),
            catalog.markRecipe(LargeChemicalReactor, CalciumTungstateDust, TungsticAcidDust, -1),
            catalog.markRecipe(BlastFurnace, TungsticAcidDust, TungstenTrioxideDust, -1));
        catalog.registerChainInputSynthesis("tungsten", LargeChemicalReactor, CalciumChlorideDust,
            new Object[] { CalciteDust, HydrochloricAcid }, -1);
        catalog.registerAlternativeChain("tungsten", TierEU.RECIPE_LuV, IronTungstateDust,
            new Object[] { TungstenTrioxideDust }, new Object[] {},
            catalog.markRecipe(Autoclave, IronTungstateDust, SodiumTungstate, -1),
            catalog.markRecipe(LargeChemicalReactor, SodiumTungstate, CalciumTungstateDust, -1),
            catalog.markRecipe(LargeChemicalReactor, CalciumTungstateDust, TungsticAcidDust, -1),
            catalog.markRecipe(BlastFurnace, TungsticAcidDust, TungstenTrioxideDust, -1));
        catalog.registerChainInputSynthesis("tungsten", LargeChemicalReactor, CalciumChlorideDust,
            new Object[] { CalciteDust, HydrochloricAcid }, -1);

        // 3. Cancel returned calcium chloride first; any remaining calcite input uses calcium, carbon and oxygen.
        catalog.registerInputSyntheses(CalciteDust);
        catalog.registerOutputDecompositions(CalciumChlorideDust);

        // NEI uses each stage's declared root and terminal products as its only material query markers.
        // spotless:on
    }
}
