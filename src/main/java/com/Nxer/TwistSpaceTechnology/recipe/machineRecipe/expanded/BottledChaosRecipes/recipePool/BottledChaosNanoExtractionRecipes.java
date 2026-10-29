package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Digester;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.github.bsideup.jabel.Desugar;

import bartworks.system.material.Werkstoff;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtnhlanth.common.register.WerkstoffMaterialPool;

/** Element-specific preparation feeds one shared intermediate; circuits select the three-stage target. */
public final class BottledChaosNanoExtractionRecipes {

    @Desugar
    private record Element(Materials metal, Werkstoff concentrate, Werkstoff chloride, Werkstoff resin,
        Werkstoff filled) {}

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        // Only samarium concentrate and cerium-rich mixture have obtainable concentrate inputs.
        // Promethium has no native resin or recovery recipe.
        // spotless:off
        Element[] elements = {
            new Element(Materials.Lanthanum, null, WerkstoffMaterialPool.LanthanumChlorideConcentrate, WerkstoffMaterialPool.LanthanumExtractingNanoResin, WerkstoffMaterialPool.FilledLanthanumExtractingNanoResin),
            new Element(Materials.Praseodymium, null, WerkstoffMaterialPool.PraseodymiumChlorideConcentrate, WerkstoffMaterialPool.PraseodymiumExtractingNanoResin, WerkstoffMaterialPool.FilledPraseodymiumExtractingNanoResin),
            new Element(Materials.Cerium, WerkstoffMaterialPool.CeriumRichMixture, WerkstoffMaterialPool.CeriumChlorideConcentrate, WerkstoffMaterialPool.CeriumExtractingNanoResin, WerkstoffMaterialPool.FilledCeriumExtractingNanoResin),
            new Element(Materials.Neodymium, null, WerkstoffMaterialPool.NeodymiumChlorideConcentrate, WerkstoffMaterialPool.NeodymiumExtractingNanoResin, WerkstoffMaterialPool.FilledNeodymiumExtractingNanoResin),
            new Element(Materials.Samarium, WerkstoffMaterialPool.SamariumOreConcentrate, WerkstoffMaterialPool.SamariumChlorideConcentrate, WerkstoffMaterialPool.SamariumExtractingNanoResin, WerkstoffMaterialPool.FilledSamariumExtractingNanoResin),
            new Element(Materials.Europium, null, WerkstoffMaterialPool.EuropiumChlorideConcentrate, WerkstoffMaterialPool.EuropiumExtractingNanoResin, WerkstoffMaterialPool.FilledEuropiumExtractingNanoResin),
            new Element(Materials.Gadolinium, null, WerkstoffMaterialPool.GadoliniumChlorideConcentrate, WerkstoffMaterialPool.GadoliniumExtractingNanoResin, WerkstoffMaterialPool.FilledGadoliniumExtractingNanoResin),
            new Element(Materials.Terbium, null, WerkstoffMaterialPool.TerbiumChlorideConcentrate, WerkstoffMaterialPool.TerbiumExtractingNanoResin, WerkstoffMaterialPool.FilledTerbiumExtractingNanoResin),
            new Element(Materials.Dysprosium, null, WerkstoffMaterialPool.DysprosiumChlorideConcentrate, WerkstoffMaterialPool.DysprosiumExtractingNanoResin, WerkstoffMaterialPool.FilledDysprosiumExtractingNanoResin),
            new Element(Materials.Holmium, null, WerkstoffMaterialPool.HolmiumChlorideConcentrate, WerkstoffMaterialPool.HolmiumExtractingNanoResin, WerkstoffMaterialPool.FilledHolmiumExtractingNanoResin),
            new Element(Materials.Erbium, null, WerkstoffMaterialPool.ErbiumChlorideConcentrate, WerkstoffMaterialPool.ErbiumExtractingNanoResin, WerkstoffMaterialPool.FilledErbiumExtractingNanoResin),
            new Element(Materials.Thulium, null, WerkstoffMaterialPool.ThuliumChlorideConcentrate, WerkstoffMaterialPool.ThuliumExtractingNanoResin, WerkstoffMaterialPool.FilledThuliumExtractingNanoResin),
            new Element(Materials.Ytterbium, null, WerkstoffMaterialPool.YtterbiumChlorideConcentrate, WerkstoffMaterialPool.YtterbiumExtractingNanoResin, WerkstoffMaterialPool.FilledYtterbiumExtractingNanoResin),
            new Element(Materials.Lutetium, null, WerkstoffMaterialPool.LutetiumChlorideConcentrate, WerkstoffMaterialPool.LutetiumExtractingNanoResin, WerkstoffMaterialPool.FilledLutetiumExtractingNanoResin)
        };
        // spotless:on
        Fluid concentrate = WerkstoffMaterialPool.ChlorinatedRareEarthConcentrate.getFluidOrGas(1)
            .getFluid();
        List<Object> roots = new ArrayList<>();
        for (int i = 0; i < elements.length; i++) {
            Element element = elements[i];
            Fluid chloride = element.chloride()
                .getFluidOrGas(1)
                .getFluid();
            Fluid molten = element.metal()
                .getMolten(1)
                .getFluid();
            ItemStack crushed = GTOreDictUnificator.get(OrePrefixes.crushed, element.metal(), 1);
            var feed = recipeItemId(crushed);
            StepRef preparation = catalog.markRecipeSequence(
                LargeChemicalReactor,
                chloride,
                concentrate,
                catalog.markRecipe(
                    LargeChemicalReactor,
                    new Object[] { chloride, element.resin()
                        .getFluidOrGas(1)
                        .getFluid() },
                    concentrate,
                    -1),
                recovery(catalog, element));
            StepRef digestion = catalog.markRecipe(Digester, feed, chloride, -1);
            var ore = i == 0
                ? catalog.registerChain(
                    "nano_extraction",
                    1,
                    TierEU.RECIPE_UV,
                    feed,
                    new Object[] { concentrate, molten },
                    digestion,
                    preparation)
                : catalog.registerAlternativeChain(
                    "nano_extraction",
                    TierEU.RECIPE_UV,
                    feed,
                    new Object[] { concentrate, molten },
                    digestion,
                    preparation);
            ore.setPlanningTargets(concentrate);
            ore.setDisplayOutputs(concentrate);
            roots.add(feed);
            if (element.concentrate() != null) {
                ItemStack powder = element.concentrate()
                    .get(OrePrefixes.dust, 3);
                var powderFeed = recipeItemId(powder);
                var alternative = catalog.registerAlternativeChain(
                    "nano_extraction",
                    TierEU.RECIPE_UV,
                    powderFeed,
                    new Object[] { concentrate, molten },
                    catalog.markRecipe(Digester, powderFeed, chloride, -1),
                    preparation);
                alternative.setPlanningTargets(concentrate);
                alternative.setDisplayOutputs(concentrate);
                // One crushed ore and three concentrate dusts have equal material yield; retain native work per route.
                catalog.groupDisplayRecipes(Arrays.asList(ore, alternative), new ItemStack[] { crushed, powder });
                roots.add(powderFeed);
            }
        }
        for (int i = 0; i < elements.length; i++) {
            Element element = elements[i];
            Fluid resin = element.resin()
                .getFluidOrGas(1)
                .getFluid();
            Fluid molten = element.metal()
                .getMolten(1)
                .getFluid();
            Fluid enriched = WerkstoffMaterialPool.ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1)
                .getFluid();
            Fluid diluted = WerkstoffMaterialPool.ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1)
                .getFluid();
            // Keep a single target through all three shared stages, and recover all three loaded resin portions.
            var extraction = catalog.registerAlternativeChain(
                "nano_extraction",
                TierEU.RECIPE_UV,
                concentrate,
                new Object[] { molten },
                catalog.markRecipe(LargeChemicalReactor, new Object[] { concentrate, resin }, enriched, -1),
                catalog.markRecipe(LargeChemicalReactor, new Object[] { enriched, resin }, diluted, -1),
                catalog.markRecipe(
                    LargeChemicalReactor,
                    new Object[] { diluted, resin },
                    GGMaterial.wasteLiquid.getFluidOrGas(1)
                        .getFluid(),
                    -1),
                recovery(catalog, element));
            extraction.requireCircuit(i + 1);
        }
        roots.add(concentrate);
        catalog.orderDisplayRecipes("nano_extraction", roots.toArray());
    }

    private static StepRef recovery(BottledChaosRecipeCatalog catalog, Element element) {
        return catalog.markRecipe(
            ElectrolyzerNonCell,
            element.filled()
                .getFluidOrGas(1)
                .getFluid(),
            element.metal()
                .getMolten(1)
                .getFluid(),
            -1);
    }
}
