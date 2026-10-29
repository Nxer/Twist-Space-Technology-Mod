package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EnderQuantumComponent;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EnrichedSunnarium;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.IrradiantUranium;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;

import java.util.function.Function;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.Entry;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;

/** Keep the three native catalyst tiers distinct, including their component inputs and residue outputs. */
public final class BottledChaosTesseractRecipes {

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        var tesseract = recipeItemId(ItemList.Tesseract.get(1));
        var dust = recipeItemId(Materials.TranscendentMetal.getDust(1));
        var hotIngot = recipeItemId(GTOreDictUnificator.get(OrePrefixes.ingotHot, Materials.TranscendentMetal, 1));
        var ingot = recipeItemId(Materials.TranscendentMetal.getIngots(1));
        var rod = recipeItemId(GTOreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 1));
        // Only the rods needed by the forge are returned; the remaining tesseracts stay as the final product.
        var recycling = catalog.markRecipeSequence(
            RecipeMaps.maceratorRecipes,
            tesseract,
            rod,
            catalog.markRecipe(RecipeMaps.maceratorRecipes, tesseract, dust, -1),
            catalog.markRecipe(RecipeMaps.blastFurnaceRecipes, dust, hotIngot, 1),
            catalog.markRecipe(RecipeMaps.vacuumFreezerRecipes, hotIngot, ingot, -1),
            catalog.markRecipe(RecipeMaps.extruderRecipes, ingot, rod, -1));
        var catalysts = new Materials[] { Materials.ExcitedDTRC, Materials.ExcitedDTEC, Materials.ExcitedDTSC };
        long[] power = { 32_000_000L, 128_000_000L, 512_000_000L };
        for (int tier = 0; tier < catalysts.length; tier++) {
            var catalyst = catalysts[tier].getFluid(1)
                .getFluid();
            // The stellar catalyst also appears in MAX spacetime conversions returning a raw tesseract.
            var step = catalog.markRecipe(
                RecipeMaps.plasmaForgeRecipes,
                new Object[] { catalyst,
                    tier == 0 ? recipeItemId(GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 1))
                        : rod },
                tesseract,
                -1);
            var entry = tier == 0
                ? catalog.registerChain("tesseract", 1, power[tier], catalyst, new Object[] { tesseract }, step)
                : catalog
                    .registerAlternativeChain("tesseract", power[tier], catalyst, new Object[] { tesseract }, step);
            entry.hideInputMarker();
            if (tier > 0) {
                entry.includeSynthesisStep(rod, recycling);
                includeBlastProduct(
                    catalog,
                    entry,
                    prefix -> GTOreDictUnificator.get(prefix, Materials.Tungsten, 1),
                    Materials.Tungsten.getMolten(1)
                        .getFluid(),
                    RecipeMaps.fluidExtractionRecipes,
                    -1);
                entry.includeSynthesisStep(
                    RecipeMaps.alloyBlastSmelterRecipes,
                    MaterialsAlloy.TITANSTEEL.getFluidStack(1)
                        .getFluid(),
                    recipeItemId(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.getDust(1)),
                    4);
                entry.includeSynthesisStep(
                    RecipeMaps.mixerRecipes,
                    recipeItemId(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.getDust(1)),
                    new Object[] { recipeItemId(Materials.TungstenCarbide.getDust(1)),
                        recipeItemId(Materials.Titanium.getDust(1)) },
                    12);
                entry.includeSynthesisStep(
                    RecipeMaps.mixerRecipes,
                    recipeItemId(Materials.TungstenCarbide.getDust(1)),
                    new Object[] { recipeItemId(Materials.Tungsten.getDust(1)),
                        recipeItemId(Materials.Carbon.getDust(1)) },
                    1);
            }
            if (tier < 2) {
                includeBlastRod(
                    catalog,
                    entry,
                    prefix -> GTOreDictUnificator.get(prefix, Materials.CosmicNeutronium, 1));
                includeBlastRod(catalog, entry, prefix -> GGMaterial.tairitsu.get(prefix, 1));
            }
            // spotless:off
            if (tier == 0) {
                includeBlastRod(catalog, entry, prefix -> GTOreDictUnificator.get(prefix, Materials.Sunnarium, 1));
                includeAlloyComponent(catalog, entry, MaterialsAlloy.OCTIRON, OrePrefixes.stick,
                    MaterialsAlloy.ARCANITE.getDust(1), 5);
                includeAlloyComponent(catalog, entry, MaterialsAlloy.ABYSSAL, OrePrefixes.plate,
                    MaterialsAlloy.STAINLESS_STEEL.getDust(1), 8);
                includeAlloyComponent(catalog, entry, MaterialsAlloy.BOTMIUM, OrePrefixes.screw,
                    MaterialsAlloy.NITINOL_60.getDust(1), 4);
            } else if (tier == 1) {
                includeEnderQuantumComponent(catalog, entry);
                includeAlloyComponent(catalog, entry, MaterialsAlloy.BOTMIUM, OrePrefixes.plate,
                    MaterialsAlloy.NITINOL_60.getDust(1), 4);
                includeAlloyComponent(catalog, entry, MaterialsAlloy.ARCANITE, OrePrefixes.screw,
                    MaterialsAlloy.ENERGYCRYSTAL.getDust(1), 4);
            } else {
                includeAlloyComponent(catalog, entry, MaterialsAlloy.BLACK_TITANIUM, OrePrefixes.plate,
                    Materials.Titanium.getDust(1), -1);
                includeAlloyComponent(catalog, entry, MaterialsAlloy.ZERON_100, OrePrefixes.screw,
                    Materials.Chrome.getDust(1), 6);
            }
            // spotless:on
        }
    }

    private static void includeEnderQuantumComponent(BottledChaosRecipeCatalog catalog, Entry entry) {
        if (EnderQuantumComponent == null || EnrichedSunnarium == null || IrradiantUranium == null)
            throw new IllegalStateException("Missing solar component inputs for the UMV tesseract route");
        var densePlate = recipeItemId(GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfinityCatalyst, 1));
        var plate = recipeItemId(Materials.ElectrumFlux.getPlates(1));
        var sunnarium = Materials.Sunnarium.getMolten(1)
            .getFluid();
        var uranium = recipeItemId(Materials.Uranium.getIngots(1));
        entry.includeSynthesisStep(
            RecipeMaps.assemblerRecipes,
            EnderQuantumComponent,
            new Object[] { densePlate, plate, EnrichedSunnarium },
            -1);
        entry.includeSynthesisStep(
            RecipeMaps.autoclaveRecipes,
            EnrichedSunnarium,
            new Object[] { IrradiantUranium, sunnarium },
            -1);
        entry.includeSynthesisStep(
            RecipeMaps.autoclaveRecipes,
            IrradiantUranium,
            new Object[] { uranium, sunnarium },
            -1);
        includeBlastProduct(
            catalog,
            entry,
            prefix -> GTOreDictUnificator.get(prefix, Materials.InfinityCatalyst, 1),
            densePlate,
            RecipeMaps.benderRecipes,
            9);
        includeBlastProduct(
            catalog,
            entry,
            prefix -> GTOreDictUnificator.get(prefix, Materials.ElectrumFlux, 1),
            plate,
            RecipeMaps.benderRecipes,
            1);
        includeBlastProduct(
            catalog,
            entry,
            prefix -> GTOreDictUnificator.get(prefix, Materials.Sunnarium, 1),
            sunnarium,
            RecipeMaps.fluidExtractionRecipes,
            -1);
        entry.includeSynthesisStep(
            uranium,
            catalog.markFixedRecipe(
                RecipeMaps.furnaceRecipes,
                recipeItemId(Materials.Uranium.getDust(1)),
                uranium,
                BottledChaosSourceResolver
                    .require(RecipeMaps.furnaceRecipes, new ItemStack[] { Materials.Uranium.getDust(1) })));
    }

    /** Keep native alloy inputs and forming work; screws use cast ingots, extruded bolts and a lathe. */
    private static void includeAlloyComponent(BottledChaosRecipeCatalog catalog, Entry entry, Material material,
        OrePrefixes form, ItemStack alloyFeed, int circuit) {
        var feed = recipeItemId(alloyFeed);
        var component = recipeItemId(material.getComponentByPrefix(form, 1));
        var molten = material.getFluidStack(1)
            .getFluid();
        var forming = form == OrePrefixes.screw
            ? catalog.markRecipeSequence(
                RecipeMaps.fluidSolidifierRecipes,
                molten,
                component,
                catalog.markRecipe(RecipeMaps.fluidSolidifierRecipes, molten, recipeItemId(material.getIngot(1)), -1),
                catalog.markRecipe(
                    RecipeMaps.extruderRecipes,
                    recipeItemId(material.getIngot(1)),
                    recipeItemId(material.getBolt(1)),
                    -1),
                catalog.markRecipe(RecipeMaps.latheRecipes, recipeItemId(material.getBolt(1)), component, -1))
            : catalog.markRecipe(RecipeMaps.fluidSolidifierRecipes, molten, component, -1);
        entry.includeSynthesisStep(
            component,
            catalog.markRecipeSequence(
                RecipeMaps.alloyBlastSmelterRecipes,
                feed,
                component,
                catalog.markRecipe(RecipeMaps.alloyBlastSmelterRecipes, feed, molten, circuit),
                forming));
    }

    /** Keep native furnace, cooling and shaping costs; Og selects one protective-gas variant. */
    private static void includeBlastRod(BottledChaosRecipeCatalog catalog, Entry entry,
        Function<OrePrefixes, ItemStack> material) {
        includeBlastProduct(
            catalog,
            entry,
            material,
            recipeItemId(material.apply(OrePrefixes.stick)),
            RecipeMaps.extruderRecipes,
            -1);
    }

    private static void includeBlastProduct(BottledChaosRecipeCatalog catalog, Entry entry,
        Function<OrePrefixes, ItemStack> material, Object product, RecipeMap<?> forming, int circuit) {
        var dust = recipeItemId(material.apply(OrePrefixes.dust));
        var hotIngot = recipeItemId(material.apply(OrePrefixes.ingotHot));
        var ingot = recipeItemId(material.apply(OrePrefixes.ingot));
        entry.includeSynthesisStep(
            product,
            catalog.markRecipeSequence(
                RecipeMaps.blastFurnaceRecipes,
                dust,
                product,
                catalog.markRecipe(
                    RecipeMaps.blastFurnaceRecipes,
                    new Object[] { dust, WerkstoffLoader.Oganesson.getFluidOrGas(1)
                        .getFluid() },
                    hotIngot,
                    11),
                catalog.markRecipe(RecipeMaps.vacuumFreezerRecipes, hotIngot, ingot, -1),
                catalog.markRecipe(forming, ingot, product, circuit)));
    }
}
