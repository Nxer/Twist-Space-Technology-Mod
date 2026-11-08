package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.AluminaMillingBall;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.CrushedPineMaterials;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IndiumDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PineOil;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.PlatinumMetallicPowder;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.CentrifugeNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ChemicalPlant;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FlotationCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidHeater;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Milling;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RedMud;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Steam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.SuperheatedSteam;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.Water;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;

/** Complete mineral flotation lines, with separate pine-oil production and red-mud recovery. */
public final class BottledChaosIsaMillRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        // Main trunks: crushed minerals -> milling -> flotation -> native drying products.
        registerMineral(catalog, Materials.Sphalerite, GTPPFluids.SphaleriteFlotationFroth, IndiumDust, TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Chalcopyrite, GTPPFluids.ChalcopyriteFlotationFroth, IndiumDust, TierEU.RECIPE_IV, 0);
        registerMineral(catalog, Materials.Nickel, GTPPFluids.NickelFlotationFroth, recipeItemId(Materials.Nickel.getDust(1)), TierEU.RECIPE_IV, 0);
        registerMineral(catalog, Materials.Platinum, GTPPFluids.PlatinumFlotationFroth, PlatinumMetallicPowder, TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Pentlandite, GTPPFluids.PentlanditeFlotationFroth, recipeItemId(Materials.Iron.getDust(1)), TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Redstone, GTPPFluids.RedstoneFlotationFroth, recipeItemId(Materials.Redstone.getDust(1)), TierEU.RECIPE_IV, 0);
        registerMineral(catalog, Materials.Spessartine, GTPPFluids.SpessartineFlotationFroth, recipeItemId(Materials.Manganese.getDust(1)), TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Grossular, GTPPFluids.GrossularFlotationFroth, recipeItemId(Materials.Calcium.getDust(1)), TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Almandine, GTPPFluids.AlmandineFlotationFroth, recipeItemId(Materials.Aluminium.getDust(1)), TierEU.RECIPE_LuV, 0);
        registerMineral(catalog, Materials.Pyrope, GTPPFluids.PyropeFlotationFroth, recipeItemId(Materials.Magnesium.getDust(1)), TierEU.RECIPE_EV, 0);
        registerMineral(catalog, Materials.Monazite, GTPPFluids.MonaziteFlotationFroth, recipeItemId(Materials.Erbium.getDust(1)), TierEU.RECIPE_UV, 0);
        // Honeycomb variants keep their additional authorization and native drying inputs.
        if (Mods.Forestry.isModLoaded() && GTBees.combs != null) {
            registerMineral(catalog, Materials.Sphalerite, GTPPFluids.SphaleriteFlotationFroth, IndiumDust, TierEU.RECIPE_LuV, 256);
            registerMineral(catalog, Materials.Chalcopyrite, GTPPFluids.ChalcopyriteFlotationFroth, IndiumDust, TierEU.RECIPE_IV, 40);
        }
        // Independent coproduct recovery and flotation-reagent production.
        registerRedMudRecipe(catalog);
        registerPineOilRecipe(catalog);
        // spotless:on
    }

    private static void registerMineral(BottledChaosRecipeCatalog catalog, Materials mineral, Fluid froth,
        Object target, long baseEUt, int combCount) {
        TST_ItemID crushed = recipeItemId(GTOreDictUnificator.get(OrePrefixes.crushed, mineral, 1));
        TST_ItemID milled = recipeItemId(GTOreDictUnificator.get(OrePrefixes.milled, mineral, 1));
        int circuit = combCount > 0 ? 2 : mineral == Materials.Sphalerite || mineral == Materials.Chalcopyrite ? 1 : -1;
        ItemStack[] furnaceItems = combCount > 0
            ? new ItemStack[] { GTBees.combs.getStackForType(CombType.INDIUM, combCount),
                GTUtility.getIntegratedCircuit(2) }
            : circuit > 0 ? new ItemStack[] { GTUtility.getIntegratedCircuit(circuit) } : new ItemStack[0];
        var drying = BottledChaosRecipeSupport.prepareSourceRecipe(
            VacuumFurnace,
            BottledChaosSourceResolver.require(VacuumFurnace, furnaceItems, new FluidStack(froth, 4_000)));
        Object[] products = drying.outputs.keySet()
            .stream()
            .filter(key -> !key.equals(GTUtility.FluidId.create(Water)))
            .toArray();
        // spotless:off
        StepRef[] steps = {
            catalog.markRecipe(Milling, new Object[] { crushed, AluminaMillingBall }, milled, 10),
            catalog.markRecipe(FlotationCell, milled, froth, -1),
            catalog.markPreparedRecipe(VacuumFurnace,
                combCount > 0 ? new Object[] { froth, recipeItemId(GTBees.combs.getStackForType(CombType.INDIUM, 1)) } : froth,
                target, drying)
        };
        BottledChaosRecipeCatalog.Entry mineralEntry;
        if (combCount > 0) {
            mineralEntry = catalog.registerAugmentedChain("isamill", "honeycomb", baseEUt, crushed, products, new Object[] {}, steps);
        } else if (mineral == Materials.Sphalerite) {
            mineralEntry = catalog.registerChain("isamill", 1, baseEUt, crushed, products, new Object[] {}, steps);
        } else {
            mineralEntry = catalog.registerAlternativeChain("isamill", baseEUt, crushed, products, new Object[] {}, steps);
        }
        mineralEntry.setPlanningTargets(target);
        mineralEntry.setDisplayOutputs(target, RedMud);
        if (combCount == 0 && circuit == 1 && Mods.Forestry.isModLoaded() && GTBees.combs != null)
            mineralEntry.requireCircuit(1);
        // spotless:on
    }

    private static void registerRedMudRecipe(BottledChaosRecipeCatalog catalog) {
        var recovery = BottledChaosRecipeSupport
            .prepareSourceRecipe(
                CentrifugeNonCell,
                BottledChaosSourceResolver.require(
                    CentrifugeNonCell,
                    new ItemStack[] { GTUtility.getIntegratedCircuit(10) },
                    new FluidStack(RedMud, 1_000)))
            .withFullDustOutputs();
        // spotless:off
        catalog.registerAlternativeChain("isamill", TierEU.RECIPE_LV, RedMud,
            recovery.outputs.keySet().toArray(), new Object[] {},
            catalog.markPreparedRecipe(CentrifugeNonCell, RedMud, Water, recovery))
            .setDisplayOutputs();
        // spotless:on
    }

    private static void registerPineOilRecipe(BottledChaosRecipeCatalog catalog) {
        // spotless:off
        TST_ItemID pineLog = TST_ItemID.create(new ItemStack(BOPBlockRegistrator.log_Pine));
        var pineOil = catalog.registerAlternativeChain("isamill", TierEU.RECIPE_LuV, pineLog,
            new Object[] { PineOil }, new Object[] {},
            catalog.markPreparedRecipe(ChemicalPlant, CrushedPineMaterials, PineOil,
                BottledChaosRecipeSupport.prepareSourceRecipe(ChemicalPlant,
                    BottledChaosSourceResolver.require(ChemicalPlant,
                        new ItemStack[] { CrushedPineMaterials.getItemStack(64), GTUtility.getIntegratedCircuit(18) },
                        new FluidStack(SuperheatedSteam, 5_000))).withFullDustOutputs()));
        pineOil.includeSynthesisStep(ChemicalPlant, CrushedPineMaterials, pineLog, 14);
        pineOil.includeSynthesisStep(FluidHeater, SuperheatedSteam, Steam, -1);
        pineOil.includeSynthesisStep(FluidHeater, Steam, Water, 1);
        // spotless:on
    }
}
