package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.recipePool;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidSolidifier;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Fusion;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.GodforgePlasma;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LaserEngraver;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Macerator;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.MixerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFreezer;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeCatalog.StepRef;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosSourceResolver;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;

/** Native plasma outputs and previously registered fusion stages remain explicit chain boundaries. */
public final class BottledChaosFusionRecipes {

    private static final String TAG = "fusion";
    private static final String PREPARATION_TAG = "naquadah_fuel_preparation";

    private final BottledChaosRecipeCatalog catalog;
    private final Map<Fluid, StepRef> moltenSources = new HashMap<>();
    private boolean firstFusion = true;

    private BottledChaosFusionRecipes(BottledChaosRecipeCatalog catalog) {
        this.catalog = catalog;
    }

    public static void registerRecipeChains(BottledChaosRecipeCatalog catalog) {
        var recipes = new BottledChaosFusionRecipes(catalog);
        recipes.registerLaserRoutes();
        recipes.registerPlasmaCoolingRoutes();
        recipes.registerSpecialPlasmas();
        recipes.registerNuclearPlasmas();
    }

    private void registerLaserRoutes() {
        // spotless:off
        catalog.registerAlternativeChain(PREPARATION_TAG, TierEU.RECIPE_UHV, recipeItemId(Materials.Titanium.getDust(1)),
            new Object[] { recipeItemId(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1)) },
            catalog.markRecipe(LaserEngraver, recipeItemId(Materials.Titanium.getDust(1)),
                recipeItemId(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1)), -1));
        catalog.registerAlternativeChain(PREPARATION_TAG, TierEU.RECIPE_UEV, recipeItemId(Materials.Tungsten.getDust(1)),
            new Object[] { recipeItemId(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1)) },
            catalog.markRecipe(LaserEngraver, recipeItemId(Materials.Tungsten.getDust(1)),
                recipeItemId(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1)), -1));
        // spotless:on
    }

    private void registerPlasmaCoolingRoutes() {
        for (Material material : new Material[] { MaterialsElements.STANDALONE.ASTRAL_TITANIUM,
            MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN }) {
            var ingot = recipeItemId(material.getIngot(1));
            var dust = recipeItemId(material.getDust(1));
            // Native plasma cooling uses MV; solidification uses the material's voltage multiplier.
            // spotless:off
            catalog.registerAlternativeChain(PREPARATION_TAG, Math.max(TierEU.RECIPE_MV, material.voltageMultiplier),
                material.getPlasma(), new Object[] { dust },
                catalog.markRecipe(VacuumFreezer, material.getPlasma(), material.getFluid(), -1),
                catalog.markRecipe(FluidSolidifier, material.getFluid(), ingot, -1),
                catalog.markRecipe(Macerator, ingot, dust, -1));
            // spotless:on
        }
    }

    private void registerSpecialPlasmas() {
        Material[] materials = { MaterialsElements.STANDALONE.ASTRAL_TITANIUM,
            MaterialsElements.STANDALONE.ADVANCED_NITINOL, MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN };
        for (int stage = 0; stage < materials.length; stage++) {
            Material material = materials[stage];
            StepRef fusion = stage == 0 ? astralFusion()
                : stage == 1 ? advancedNitinolFusion()
                    : catalog.markRecipe(
                        Fusion,
                        new Object[] { MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(),
                            Materials.Tartarite.getMolten(1)
                                .getFluid() },
                        material.getPlasma(),
                        -1);
            if (stage == 2) {
                var entry = registerFusion(
                    TierEU.RECIPE_UV,
                    MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(),
                    material.getPlasma(),
                    fusion);
                molten(entry, Materials.Tartarite);
                continue;
            }
            for (boolean plasmaMaker : new boolean[] { false, true }) {
                Object root = stage == 0 ? recipeItemId((plasmaMaker ? Materials.Force : Materials.Cobalt).getDust(1))
                    : plasmaMaker ? recipeItemId(MaterialsElements.STANDALONE.RUNITE.getDust(1))
                        : MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma();
                var entry = registerFusion(
                    plasmaMaker ? TierEU.RECIPE_UXV : stage == 0 ? TierEU.RECIPE_ZPM : TierEU.RECIPE_UV,
                    root,
                    material.getPlasma(),
                    fusion);
                registerFusionInputs(entry, stage, plasmaMaker);
            }
        }
    }

    private void registerNuclearPlasmas() {
        var elements = MaterialsElements.getInstance();
        // spotless:off
        // Keep the higher-tier native reactions, with fusion and plasma-maker feed variants.
        for (boolean plasmaMaker : new boolean[] { false, true }) {
            var fermium = registerFusion(plasmaMaker ? TierEU.RECIPE_UXV : TierEU.RECIPE_UEV,
                recipeItemId(plasmaMaker ? Materials.Force.getDust(1) : elements.THORIUM232.getDust(1)), elements.FERMIUM.getPlasma(),
                catalog.markRecipe(Fusion, new Object[] { MaterialsElements.STANDALONE.FORCE.getPlasma(), Materials.Rubidium.getMolten(1).getFluid() }, elements.FERMIUM.getPlasma(), -1));
            if (plasmaMaker) synthesize(fermium, GodforgePlasma, recipeItemId(Materials.Force.getDust(1)), MaterialsElements.STANDALONE.FORCE.getPlasma());
            else synthesizeForce(fermium);
            molten(fermium, Materials.Rubidium);

            var neptunium = registerFusion(plasmaMaker ? TierEU.RECIPE_UXV : TierEU.RECIPE_UEV,
                plasmaMaker ? elements.XENON.getFluid() : recipeItemId(Materials.Plutonium241.getDust(1)), elements.NEPTUNIUM.getPlasma(),
                catalog.markRecipe(Fusion, new Object[] { elements.XENON.getPlasma(), Materials.Yttrium.getMolten(1).getFluid() }, elements.NEPTUNIUM.getPlasma(), -1));
            if (plasmaMaker) synthesize(neptunium, GodforgePlasma, elements.XENON.getFluid(), elements.XENON.getPlasma());
            else synthesizeXenon(neptunium);
            molten(neptunium, Materials.Yttrium);
        }
        // spotless:on
    }

    private BottledChaosRecipeCatalog.Entry registerFusion(long voltage, Object root, Object output, StepRef... steps) {
        var entry = firstFusion ? catalog.registerChain(TAG, 1, voltage, root, new Object[] { output }, steps)
            : catalog.registerAlternativeChain(TAG, voltage, root, new Object[] { output }, steps);
        firstFusion = false;
        entry.hideInputMarker();
        return entry;
    }

    private StepRef astralFusion() {
        return catalog.markRecipe(
            Fusion,
            new Object[] { MaterialsElements.getInstance().KRYPTON.getPlasma(),
                MaterialsElements.STANDALONE.FORCE.getPlasma() },
            MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(),
            -1);
    }

    private StepRef advancedNitinolFusion() {
        return catalog.markRecipe(
            Fusion,
            new Object[] { MaterialsElements.getInstance().XENON.getPlasma(),
                MaterialsElements.STANDALONE.RUNITE.getPlasma() },
            MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlasma(),
            -1);
    }

    private StepRef curiumFusion() {
        return catalog.markRecipe(
            Fusion,
            new Object[] { Materials.Plutonium241.getMolten(1)
                .getFluid(),
                Materials.Helium.getGas(1)
                    .getFluid() },
            MaterialsElements.getInstance().CURIUM.getFluid(),
            -1);
    }

    private void registerFusionInputs(BottledChaosRecipeCatalog.Entry entry, int stage, boolean plasmaMaker) {
        var elements = MaterialsElements.getInstance();
        // spotless:off
        if (plasmaMaker) {
            // Runite has a native Everglades ore vein; Force has a native bee source.
            if (stage == 0) {
                synthesize(entry, GodforgePlasma, recipeItemId(Materials.Force.getDust(1)), MaterialsElements.STANDALONE.FORCE.getPlasma());
                synthesize(entry, GodforgePlasma, elements.KRYPTON.getFluid(), elements.KRYPTON.getPlasma());
            } else {
                synthesize(entry, GodforgePlasma, recipeItemId(MaterialsElements.STANDALONE.RUNITE.getDust(1)), MaterialsElements.STANDALONE.RUNITE.getPlasma());
                synthesize(entry, GodforgePlasma, elements.XENON.getFluid(), elements.XENON.getPlasma());
            }
            return;
        }
        if (stage == 0) {
            synthesizeForce(entry);
            synthesize(entry, Fusion, new Object[] { Materials.Niobium.getPlasma(1).getFluid(), Materials.Zinc.getPlasma(1).getFluid() }, elements.KRYPTON.getPlasma());
            synthesize(entry, Fusion, new Object[] { Materials.Cobalt.getMolten(1).getFluid(), Materials.Silicon.getMolten(1).getFluid() }, Materials.Niobium.getPlasma(1).getFluid());
            synthesize(entry, Fusion, new Object[] { Materials.Copper.getMolten(1).getFluid(), Materials.Tritium.getGas(1).getFluid() }, Materials.Zinc.getPlasma(1).getFluid());
            molten(entry, Materials.Cobalt, Materials.Silicon, Materials.Copper);
        } else {
            synthesize(entry, Fusion, new Object[] { MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getPlasma(), MaterialsAlloy.TITANSTEEL.getFluid() }, MaterialsElements.STANDALONE.RUNITE.getPlasma());
            synthesizeXenon(entry);
            molten(entry, MaterialsAlloy.TITANSTEEL);
            synthesize(entry, MixerNonCell, recipeItemId(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.getDust(1)), recipeItemId(MaterialsAlloy.TITANSTEEL.getDust(1)));
            synthesize(entry, MixerNonCell, recipeItemId(Materials.TungstenCarbide.getDust(1)), recipeItemId(MaterialsAlloy.TUNGSTEN_TITANIUM_CARBIDE.getDust(1)));
            entry.includeSynthesisStep(MixerNonCell, recipeItemId(Materials.TungstenCarbide.getDust(1)),
                new Object[] { recipeItemId(Materials.Tungsten.getDust(1)), recipeItemId(Materials.Carbon.getDust(1)) }, 1);
        }
        // spotless:on
    }

    private void synthesizeBoron(BottledChaosRecipeCatalog.Entry entry) {
        // spotless:off
        synthesize(entry, Fusion, new Object[] { Materials.Helium.getPlasma(1).getFluid(), Materials.Lithium.getMolten(1).getFluid() }, Materials.Boron.getPlasma(1).getFluid());
        synthesize(entry, Fusion, new Object[] { Materials.Deuterium.getGas(1).getFluid(), Materials.Tritium.getGas(1).getFluid() }, Materials.Helium.getPlasma(1).getFluid());
        molten(entry, Materials.Lithium);
        // spotless:on
    }

    private void synthesizeForce(BottledChaosRecipeCatalog.Entry entry) {
        var elements = MaterialsElements.getInstance();
        // spotless:off
        synthesize(entry, Fusion, new Object[] { elements.NEON.getPlasma(), MaterialsAlloy.ARCANITE.getFluid() }, MaterialsElements.STANDALONE.FORCE.getPlasma());
        synthesize(entry, Fusion, new Object[] { Materials.Boron.getPlasma(1).getFluid(), Materials.Calcium.getPlasma(1).getFluid() }, elements.NEON.getPlasma());
        synthesizeBoron(entry);
        synthesize(entry, Fusion, new Object[] { Materials.Magnesium.getMolten(1).getFluid(), Materials.Oxygen.getGas(1).getFluid() }, Materials.Calcium.getPlasma(1).getFluid());
        molten(entry, Materials.Magnesium);
        molten(entry, MaterialsAlloy.ARCANITE);
        synthesize(entry, MixerNonCell, recipeItemId(elements.THORIUM232.getDust(1)), recipeItemId(MaterialsAlloy.ARCANITE.getDust(1)));
        synthesize(entry, MixerNonCell, recipeItemId(Materials.InfusedAir.getDust(1)), recipeItemId(MaterialsAlloy.ENERGYCRYSTAL.getDust(1)));
        // spotless:on
    }

    private void synthesizeAmericium(BottledChaosRecipeCatalog.Entry entry) {
        // spotless:off
        synthesize(entry, Fusion, new Object[] { Materials.Plutonium241.getMolten(1).getFluid(), Materials.Hydrogen.getGas(1).getFluid() }, Materials.Americium.getPlasma(1).getFluid());
        molten(entry, Materials.Plutonium241);
        // spotless:on
    }

    private void synthesizeXenon(BottledChaosRecipeCatalog.Entry entry) {
        // spotless:off
        synthesize(entry, Fusion, new Object[] { MaterialsElements.getInstance().CURIUM.getFluid(), Materials.Americium.getPlasma(1).getFluid() }, MaterialsElements.getInstance().XENON.getPlasma());
        entry.includeSynthesisStep(MaterialsElements.getInstance().CURIUM.getFluid(), curiumFusion());
        synthesizeAmericium(entry);
        // spotless:on
    }

    private void synthesize(BottledChaosRecipeCatalog.Entry entry, RecipeMap<?> pool, Object inputs, Object output) {
        entry.includeSynthesisStep(pool, output, inputs, -1);
    }

    private void molten(BottledChaosRecipeCatalog.Entry entry, Materials... materials) {
        for (Materials material : materials) molten(
            entry,
            material.getDust(1),
            material.getIngots(1),
            material.getMolten(1)
                .getFluid());
    }

    private void molten(BottledChaosRecipeCatalog.Entry entry, Material material) {
        molten(entry, material.getDust(1), material.getIngot(1), material.getFluid());
    }

    private void molten(BottledChaosRecipeCatalog.Entry entry, ItemStack dust, ItemStack ingot, Fluid fluid) {
        StepRef mark = moltenSources.get(fluid);
        if (mark == null) {
            ItemStack suppliedDust = GTUtility.copyAmount(64, dust);
            GTRecipe extraction = BottledChaosSourceResolver.query(FluidExtractor, new ItemStack[] { suppliedDust });
            RecipeData data;
            if (extraction != null && extraction.mFluidOutputs.length == 1
                && extraction.mFluidOutputs[0].getFluid() == fluid) {
                data = prepareSourceRecipe(FluidExtractor, extraction);
            } else {
                // Prefer Og for recipes that use a protective gas; gas-free smelting keeps its native recipe.
                GTRecipe smelting = BottledChaosSourceResolver.query(
                    BlastFurnace,
                    new ItemStack[] { suppliedDust, GTUtility.getIntegratedCircuit(11) },
                    WerkstoffLoader.Oganesson.getFluidOrGas(Integer.MAX_VALUE));
                if (smelting == null) smelting = BottledChaosSourceResolver
                    .query(BlastFurnace, new ItemStack[] { suppliedDust, GTUtility.getIntegratedCircuit(1) });
                if (smelting == null) throw new IllegalStateException("Missing fusion feedstock smelting: " + dust);
                data = prepareSourceRecipe(BlastFurnace, smelting);
                if (!recipeItemId(smelting.mOutputs[0]).equals(recipeItemId(ingot))) {
                    data = data.appendBalancedRecipe(
                        prepareSourceRecipe(
                            VacuumFreezer,
                            BottledChaosSourceResolver.require(VacuumFreezer, smelting.mOutputs)),
                        recipeItemId(smelting.mOutputs[0]));
                }
                data = data.appendBalancedRecipe(
                    prepareSourceRecipe(
                        FluidExtractor,
                        BottledChaosSourceResolver.require(FluidExtractor, new ItemStack[] { ingot })),
                    recipeItemId(ingot));
            }
            mark = catalog.markPreparedRecipe(FluidExtractor, recipeItemId(dust), fluid, data);
            moltenSources.put(fluid, mark);
        }
        entry.includeSynthesisStep(fluid, mark);
    }

}
