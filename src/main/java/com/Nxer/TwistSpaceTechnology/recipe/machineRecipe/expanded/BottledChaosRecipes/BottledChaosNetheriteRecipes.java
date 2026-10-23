package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AluminaMillingBall;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AncientDebris;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.BrittleNetheriteScrap;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Grade2PurifiedWater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HeavyHellishMud;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HellishMetalBlock;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HellishMetalMolten;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.HotNetheriteScrap;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IntenselyBondedNetheriteNanoparticles;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.MilledNetherrack;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NefariousGas;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NefariousOil;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NetherAir;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NetherSemiFluid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NetheriteNanoparticles;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NetheriteScrap;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Netherrack;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NetherrackFlotationFroth;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NitricAcid;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PoorNetherWaste;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalBath;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FlotationCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FusionCrafting;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Macerator;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Milling;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFreezer;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RhodiumBlock;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RhodiumDust;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;

/** Netherite trunks and nether-air reagent branches, resolved from live source recipes. */
public final class BottledChaosNetheriteRecipes {

    public static void registerRecipeChains(RecipeCatalog catalog) {
        if (!Mods.EtFuturumRequiem.isModLoaded()) return;
        if (AncientDebris == null || NetheriteScrap == null)
            throw new IllegalStateException("missing Et Futurum netherite process items");

        // spotless:off
        // Main trunk: UHV chemical bath -> brittle scrap -> ordinary nanoparticles.
        // Nether air remains a consumed execution root after the returned hot scraps cancel.
        catalog.registerChain("ancient_debris", 1, TierEU.RECIPE_UHV, NetherAir,
            new Object[] { NetheriteNanoparticles, NitricAcid },
            new Object[] {},
            catalog.markRecipe(ChemicalBath, new Object[] { HotNetheriteScrap, HeavyHellishMud, PoorNetherWaste }, BrittleNetheriteScrap, -1),
            catalog.markRecipe(Macerator, BrittleNetheriteScrap, NetheriteNanoparticles, -1));

        // Supply the bonding metal through either native process; one rhodium block represents nine dusts.
        catalog.registerAlternativeChain("ancient_debris", TierEU.RECIPE_UV, RhodiumDust,
            new Object[] { HellishMetalMolten },
            catalog.markRecipe(FusionCrafting, RhodiumDust, HellishMetalMolten, -1));
        catalog.registerAlternativeChain("ancient_debris", TierEU.RECIPE_ZPM, RhodiumDust,
            new Object[] { HellishMetalMolten },
            catalog.markRecipeWithSubstitutedInput(ElectrolyzerNonCell, RhodiumBlock, RhodiumDust, 9, HellishMetalBlock, -1),
            catalog.markRecipe(FluidExtractor, HellishMetalBlock, HellishMetalMolten, -1));

        // Keep the two native bonding recipes as independently searchable, chainable finishing stages.
        catalog.registerAlternativeChain("ancient_debris", TierEU.RECIPE_ZPM, NetheriteNanoparticles,
            new Object[] { IntenselyBondedNetheriteNanoparticles }, new Object[] {},
            catalog.markRecipe(BlastFurnace, NetheriteNanoparticles, IntenselyBondedNetheriteNanoparticles, 1));
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) {
            catalog.registerAugmentedChain("ancient_debris", "honeycomb", TierEU.RECIPE_ZPM, NetheriteNanoparticles,
                new Object[] { IntenselyBondedNetheriteNanoparticles }, new Object[] {},
                catalog.markRecipe(BlastFurnace,
                    new Object[] { NetheriteNanoparticles, TST_ItemID.create(GTBees.combs.getStackForType(CombType.NETHERITE, 1)) },
                    IntenselyBondedNetheriteNanoparticles, 2));
        }

        // 1. Reheat returned scraps and cancel them against the UHV bath's hot-scrap input.
        catalog.registerOutputDecomposition(ChemicalBath, NetheriteScrap, HotNetheriteScrap, -1);

        // 2. Waste branch: poor waste -> flotation froth -> milled netherrack -> netherrack.
        // Select the alumina-ball milling recipe explicitly.
        catalog.registerInputSynthesis(VacuumFurnace, PoorNetherWaste, NetherrackFlotationFroth, -1);
        catalog.registerInputSynthesis(FlotationCell, NetherrackFlotationFroth, new Object[] { MilledNetherrack, NefariousOil }, -1);
        catalog.registerInputSynthesis(Milling, MilledNetherrack, new Object[] { Netherrack, AluminaMillingBall }, 10);

        // 3. Oil branch: grade-2 purified water + nefarious gas -> oil for flotation.
        catalog.registerInputSynthesis(OilCracker, NefariousOil, new Object[] { Grade2PurifiedWater, NefariousGas }, 1);

        // 4. Air branch: vacuum freezing -> distillation. Reuse the tower's joint gas/mud outputs first.
        // Resolve gas before mud so its coproduct can satisfy the waste branch and the UHV trunk.
        catalog.registerInputSynthesis(DistillationTower, NefariousGas, NetherSemiFluid, -1);
        catalog.registerInputSynthesis(DistillationTower, HeavyHellishMud, NetherSemiFluid, -1);
        catalog.registerInputSynthesis(VacuumFreezer, NetherSemiFluid, NetherAir, -1);
        // spotless:on
    }
}
