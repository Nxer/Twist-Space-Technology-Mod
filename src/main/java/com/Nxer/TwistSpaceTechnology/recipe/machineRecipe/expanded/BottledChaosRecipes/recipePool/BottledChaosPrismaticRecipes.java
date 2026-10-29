package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrystallineAlloyDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrystallineAlloyIngot;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrystallineAlloyMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.DiamondGem;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HotCrystallineAlloyIngot;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HydrogenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Nitrobenzene;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OganessonGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.OxygenGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarineContaminatedHydrogenPeroxide;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarineContaminatedNitrobenzeneSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarinePrecipitate;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarineRichNitrobenzeneSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarineShard;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismarineSolution;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismaticAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismaticCrystal;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PrismaticGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PulsatingCrystal;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PulsatingCrystalDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PulsatingIronMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Autoclave;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalBath;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalPlant;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DissolutionTank;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidSolidifier;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LaserEngraver;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Macerator;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.MixerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFreezer;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RedAlgaeBiomass;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.StrontiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.StrontiumHydroxideDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.StrontiumIngot;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.StrontiumMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.XenonGas;

import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;

/** Prismarine shard processing through prismatic acid. */
public final class BottledChaosPrismaticRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        // Shard stage: precipitation -> recovered solvent -> crystal -> gas -> acid.
        // Starting at precipitation exposes the solvent return before the missing fresh solvent is supplied.
        catalog.registerChain("prismatic_acid", 1, TierEU.RECIPE_UHV, PrismarineShard,
            new Object[] { PrismaticAcid }, new Object[] {},
            catalog.markRecipe(ChemicalPlant, StrontiumHydroxideDust, PrismarinePrecipitate, -1),
            catalog.markRecipe(ChemicalBath, PrismarineContaminatedNitrobenzeneSolution, PrismarineRichNitrobenzeneSolution, 1),
            catalog.markRecipe(LaserEngraver, PrismarinePrecipitate, PrismaticCrystal, -1),
            catalog.markRecipe(VacuumFurnace, PrismaticCrystal, PrismaticGas, -1),
            catalog.markRecipe(VacuumFreezer, PrismaticGas, PrismaticAcid, -1));

        // Independent shard supply; the honeycomb source replaces quartz and doubles the native yield.
        catalog.registerAlternativeChain("prismatic_acid", TierEU.RECIPE_LuV, RedAlgaeBiomass,
            new Object[] { PrismarineShard }, new Object[] { PrismarineShard },
            catalog.markRecipe(Autoclave,
                new Object[] { RedAlgaeBiomass, TST_ItemID.create(Materials.CertusQuartz.getDust(1)) }, PrismarineShard, -1));
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) {
            catalog.registerAugmentedChain("prismatic_acid", "honeycomb", TierEU.RECIPE_LuV, RedAlgaeBiomass,
                new Object[] { PrismarineShard }, new Object[] { PrismarineShard },
                catalog.markRecipe(Autoclave,
                    new Object[] { RedAlgaeBiomass, TST_ItemID.create(GTBees.combs.getStackForType(CombType.PRISMATIC, 1)) }, PrismarineShard, -1));
        }
        catalog.orderDisplayRecipes("prismatic", DiamondGem, RedAlgaeBiomass, PrismarineShard);

        // Input branches: supplement extraction, then reuse the contaminated peroxide in looped leaching.
        catalog.includeSynthesisStep(ChemicalBath, PrismarineRichNitrobenzeneSolution,
            new Object[] { PrismarineSolution, Nitrobenzene }, 1);
        // Preserve the source loop's net contaminated peroxide output after reuse.
        catalog.includeSynthesisStep(DissolutionTank, PrismarineSolution,
            new Object[] { PrismarineShard, PrismarineContaminatedHydrogenPeroxide }, -1);

        // Alloy preparation: autoclave crystals -> powder -> alloy -> protected blast -> cooling -> melt.
        // Both protective gases share the alloy tag, which may pass its melt to the independently authorized acid stage.
        if (BottledChaosRecipeResources.CredentialModel.indexForTag("crystalline_alloy_molten") >= 0) {
            for (Fluid gas : new Fluid[] { OganessonGas, XenonGas }) {
                var steps = new StepRef[] {
                    catalog.markRecipe(Autoclave, new Object[] { DiamondGem, PulsatingIronMolten }, PulsatingCrystal, -1),
                    catalog.markRecipe(Macerator, PulsatingCrystal, PulsatingCrystalDust, -1),
                    catalog.markRecipe(MixerNonCell, PulsatingCrystalDust, CrystallineAlloyDust, 2),
                    catalog.markRecipe(BlastFurnace, new Object[] { CrystallineAlloyDust, gas }, HotCrystallineAlloyIngot, 11),
                    catalog.markRecipe(VacuumFreezer, HotCrystallineAlloyIngot, CrystallineAlloyIngot, -1),
                    catalog.markRecipe(FluidExtractor, CrystallineAlloyIngot, CrystallineAlloyMolten, -1)
                };
                if (gas == OganessonGas) catalog.registerChain("crystalline_alloy_molten", 1, TierEU.RECIPE_UHV, DiamondGem,
                    new Object[] { CrystallineAlloyMolten }, new Object[] { CrystallineAlloyMolten }, steps).hideInputMarker();
                else catalog.registerAlternativeChain("crystalline_alloy_molten", TierEU.RECIPE_UHV, DiamondGem,
                    new Object[] { CrystallineAlloyMolten }, new Object[] { CrystallineAlloyMolten }, steps).hideInputMarker();
            }
            catalog.includeSynthesisPath(PulsatingIronMolten);
        }

        // Strontium return: crystallization melt -> ingot -> dust -> hydroxide.
        catalog.includeDecompositionStep(FluidSolidifier, StrontiumMolten, StrontiumIngot, -1);
        catalog.includeDecompositionStep(Macerator, StrontiumIngot, StrontiumDust, -1);
        catalog.includeSynthesisStep(LargeChemicalReactor, StrontiumHydroxideDust,
            new Object[] { StrontiumDust, HydrogenGas, OxygenGas }, -1);
        // spotless:on
    }
}
