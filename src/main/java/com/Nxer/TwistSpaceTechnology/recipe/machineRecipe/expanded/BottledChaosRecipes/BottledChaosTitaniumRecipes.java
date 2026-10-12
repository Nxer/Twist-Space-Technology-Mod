package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AluminiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AluminiumHydroxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AluminiumOxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BauxiteDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BauxiteSlagDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BauxiteSlurry;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CalciteDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CarbonDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrushedBauxite;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrushedIlmenite;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HeatedBauxiteSlurry;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HotTitaniumIngot;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IlmeniteDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IlmeniteSlagDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IronDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IronIISulfateSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.QuicklimeDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.CentrifugeNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalBath;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidHeater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.MixerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFreezer;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RutileDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SiliconDioxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SiliconDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SluiceJuice;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumCarbonateDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumHydroxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TitaniumIngot;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TitaniumTetrachloride;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;

import gregtech.api.enums.TierEU;

/** LuV mineral processing; alternative sources and consecutive stages share one credential tag. */
public final class BottledChaosTitaniumRecipes {

    public static void registerRecipeChains(RecipeCatalog catalog) {
        // spotless:off
        // 1. Final stage: chlorinate rutile, reduce its chloride, then cool to titanium ingots.
        catalog.registerChain("titanium", 1, TierEU.RECIPE_LuV, RutileDust,
            new Object[] { TitaniumIngot }, new Object[] {},
            catalog.markRecipe(LargeChemicalReactor, RutileDust, TitaniumTetrachloride, -1),
            catalog.markRecipe(BlastFurnace, TitaniumTetrachloride, HotTitaniumIngot, 12),
            catalog.markRecipe(VacuumFreezer, HotTitaniumIngot, TitaniumIngot, -1));

        // 2. Ilmenite preparation: use the native crushed-ore bath, retaining its 60% slag yield.
        // Replace only the ore input with full mineral dust before balancing the cached chain.
        catalog.registerAlternativeChain("titanium", TierEU.RECIPE_LuV, IlmeniteDust,
            new Object[] { RutileDust }, new Object[] { RutileDust },
            catalog.markRecipeWithSubstitutedInput(ChemicalBath, CrushedIlmenite, IlmeniteDust, RutileDust, -1));
        catalog.registerChainOutputDecomposition("titanium", CentrifugeNonCell, IlmeniteSlagDust, IronDust, -1);
        catalog.registerChainOutputDecomposition("titanium", ElectrolyzerNonCell, IronIISulfateSolution, IronDust, -1);

        // 3. Bauxite preparation: slurry mixing, heating, precipitation and slag separation.
        catalog.registerAlternativeChain("titanium", TierEU.RECIPE_LuV, BauxiteDust,
            new Object[] { RutileDust }, new Object[] { RutileDust },
            catalog.markRecipeWithSubstitutedInput(MixerNonCell, CrushedBauxite, BauxiteDust, BauxiteSlurry, 8),
            catalog.markRecipe(FluidHeater, BauxiteSlurry, HeatedBauxiteSlurry, -1),
            catalog.markRecipe(LargeChemicalReactor, HeatedBauxiteSlurry, BauxiteSlagDust, -1),
            catalog.markRecipe(CentrifugeNonCell, BauxiteSlagDust, RutileDust, -1));

        // 4. Bauxite coproducts: recover sluice metals and decompose the remaining mineral compounds.
        catalog.registerChainOutputDecomposition("titanium", CentrifugeNonCell, SluiceJuice, IronDust, -1);
        catalog.registerChainOutputDecomposition("titanium", ElectrolyzerNonCell, AluminiumOxideDust, AluminiumDust, -1);
        catalog.registerChainOutputDecomposition("titanium", ElectrolyzerNonCell, SodiumCarbonateDust, SodiumDust, -1);
        catalog.registerChainOutputDecomposition("titanium", ElectrolyzerNonCell, CalciteDust, CarbonDust, -1);
        catalog.registerChainOutputDecomposition("titanium", ElectrolyzerNonCell, SiliconDioxideDust, SiliconDust, -1);
        catalog.registerOutputDecompositions(QuicklimeDust);
        catalog.registerInputSyntheses(AluminiumHydroxideDust, SodiumHydroxideDust, QuicklimeDust);

        // NEI uses each stage's declared root and terminal products as its only material query markers.
        // spotless:on
    }
}
