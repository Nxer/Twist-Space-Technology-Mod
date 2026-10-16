package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Agar;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BacterialSludge;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BioCells;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BioMediumRaw;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BioMediumSterilized;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CosmicNeutroniumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DilutedSulfuricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DistilledWater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EnergiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EnrichedBacterialSludge;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.FermentedBacterialSludge;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.GrowthMediumRaw;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.GrowthMediumSterilized;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenSulfideGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.InfinityCatalystDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IridiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MeatExtract;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Mutagen;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MysteriousCrystalDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MytrylDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OsmiridiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OsmiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PamSeaweed;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PhosphoricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RawMeatDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Autoclave;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BacterialVat;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Brewery;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.CentrifugeNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Distillery;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Extractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Fermenter;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidHeater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.MixerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RedstoneDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RubyDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SeaweedBroth;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SodiumChlorideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.StemCells;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SulfurDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TauCetiESeaweed;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.TauCetiESeaweedExtract;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.UnknownCrystal;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.UnknownNutrientAgar;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.UraniumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;

import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.TierEU;

/** Six biological production stages; stem-cell coproducts include their sludge-recovery steps. */
public final class BottledChaosBiologicalRecipes {

    public static void registerRecipeChains(RecipeCatalog catalog) {
        // spotless:off
        // Stem-cell finishing entry; preparation entries execute before this primary entry.
        catalog.registerChain("stem_cells", 1, TierEU.RECIPE_UHV, GrowthMediumSterilized,
            new Object[] { StemCells }, new Object[] { StemCells, Mutagen },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { UnknownCrystal, OsmiridiumDust },
                new Object[] { StemCells, BacterialSludge }, -1),
            catalog.markRecipe(Brewery, new Object[] { BacterialSludge, UraniumDust }, EnrichedBacterialSludge, -1),
            catalog.markRecipe(Fermenter, EnrichedBacterialSludge, FermentedBacterialSludge, -1),
            catalog.markRecipe(Distillery, FermentedBacterialSludge, Mutagen, 3));

        // Stage 1: raw materials -> mutagen growth medium -> sterile growth medium.
        catalog.registerChain("growth_medium_sterilized", 1, TierEU.RECIPE_UHV, RawMeatDust,
            new Object[] { GrowthMediumSterilized }, new Object[] { GrowthMediumSterilized },
            catalog.markRecipe(BacterialVat, new Object[] { RawMeatDust, Agar, Mutagen }, GrowthMediumRaw, -1),
            catalog.markRecipe(FluidHeater, GrowthMediumRaw, GrowthMediumSterilized, -1));

        // Resolve the native centrifuge selector for the registered Tau Ceti E crop variant.
        int seaweedCircuit = 0;
        var variants = OreDictionary.getOres("cropTcetiESeaweed");
        for (int index = 0; index < variants.size(); index++) {
            if (TauCetiESeaweed.equals(TST_ItemID.create(variants.get(index)))) {
                seaweedCircuit = index + 1;
                break;
            }
        }
        if (seaweedCircuit == 0) throw new IllegalStateException("missing Tau Ceti E seaweed crop variant");

        // Biological-cell finishing entry; preparation entries execute before this primary entry.
        catalog.registerChain("bio_cells", 1, TierEU.RECIPE_UHV, BioMediumSterilized,
            new Object[] { BioCells }, new Object[] { Mutagen },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { StemCells, CosmicNeutroniumDust },
                new Object[] { BioCells, Mutagen }, -1));

        // Stage 3: stem cells and raw materials -> nutrient agar -> seaweed broth.
        catalog.registerChain("seaweed_broth", 1, TierEU.RECIPE_UHV, StemCells,
            new Object[] { SeaweedBroth }, new Object[] { SeaweedBroth },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { Agar, StemCells, MeatExtract }, UnknownNutrientAgar, -1),
            catalog.markRecipe(BacterialVat, new Object[] { EnergiumDust, MytrylDust, PamSeaweed }, SeaweedBroth, -1));

        // Stage 4: seaweed broth -> seaweed -> seaweed extract.
        catalog.registerChain("tau_ceti_e_seaweed_extract", 1, TierEU.RECIPE_UHV, SeaweedBroth,
            new Object[] { TauCetiESeaweedExtract }, new Object[] { TauCetiESeaweedExtract },
            catalog.markRecipe(CentrifugeNonCell, SeaweedBroth, TauCetiESeaweed, seaweedCircuit),
            catalog.markRecipe(Extractor, TauCetiESeaweed, TauCetiESeaweedExtract, -1));

        // Stage 5: produce the biological-cell seed from stem cells, then cancel its medium and mutagen return.
        catalog.registerChain("bio_medium_sterilized", 1, TierEU.RECIPE_UHV, TauCetiESeaweedExtract,
            new Object[] { BioMediumSterilized }, new Object[] { BioMediumSterilized },
            catalog.markRecipe(LargeChemicalReactor, new Object[] { StemCells, CosmicNeutroniumDust }, BioCells, -1),
            catalog.markRecipe(BacterialVat, new Object[] { BioCells, TauCetiESeaweedExtract, InfinityCatalystDust }, BioMediumRaw, -1),
            catalog.markRecipe(FluidHeater, BioMediumRaw, BioMediumSterilized, -1));

        // Shared input branches: fresh raw growth medium uses externally supplied agar.
        catalog.registerInputSynthesis(BacterialVat, GrowthMediumRaw, new Object[] { Agar, Mutagen }, -1);
        catalog.registerInputSynthesis(Autoclave, UnknownCrystal,
            new Object[] { MysteriousCrystalDust, DistilledWater }, -1);
        catalog.registerInputSynthesis(MixerNonCell, OsmiridiumDust, new Object[] { OsmiumDust, IridiumDust }, 1);

        // Seaweed preparation inputs; intermediate stage outputs have no automatic input synthesis.
        catalog.registerInputSynthesis(FluidExtractor, MeatExtract, RawMeatDust, -1);
        catalog.registerInputSynthesis(MixerNonCell, EnergiumDust, new Object[] { RedstoneDust, RubyDust }, -1);
        catalog.registerInputSyntheses(SodiumChlorideDust, PhosphoricAcid, DistilledWater);
        // Avoid an input/output acid cycle; supply this dilute input through its native hydrogen-sulfide source.
        catalog.registerInputSynthesis(LargeChemicalReactor, DilutedSulfuricAcid, new Object[] { HydrogenSulfideGas, Water }, -1);
        catalog.registerInputSynthesis(LargeChemicalReactor, HydrogenSulfideGas, new Object[] { SulfurDust, HydrogenGas }, 1);

        catalog.setQueryMarkers("growth_medium_sterilized", new Object[] { Agar, Mutagen }, null);

        catalog.registerExternalInputs(Agar, StemCells, RawMeatDust, Mutagen);
        // spotless:on
    }
}
