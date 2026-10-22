package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes;

import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.EnrichedNaquadahDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.NaquadriaDust;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Assembler;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Bender;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.BlastFurnace;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.CentrifugeNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Compressor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Cutter;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.DistillationTower;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectricImplosionCompressor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.ElectrolyzerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidExtractor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.FluidSolidifier;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.LargeChemicalReactor;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.Macerator;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.MixerNonCell;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.OilCracker;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.RecipePools.VacuumFreezer;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeResources.recipeItemId;
import static com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.prepareSourceRecipe;
import static com.Nxer.TwistSpaceTechnology.util.MathUtils.getGCD;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeProcessor.RecipeCatalog;
import com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BottledChaosRecipes.BottledChaosRecipeSupport.RecipeData;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;

/** Small fuel stages; paid intermediate leftovers are carried between executions by the material cache. */
public final class BottledChaosNaquadahFuelRecipes {

    private static final String PREPARATION_TAG = "naquadah_fuel_preparation";
    private static final String FUEL_TAG = "naquadah_fuel_4";

    public static void registerRecipeChains(RecipeCatalog catalog) {
        registerPreparationChains(catalog);
        registerFuelChains(catalog);

        // spotless:off
        // Display order follows preparation before the independently chainable refining stages.
        catalog.orderDisplayRecipes(PREPARATION_TAG,
            recipeItemId(Materials.Uranium.getDust(1)), recipeItemId(GGMaterial.orundum.get(OrePrefixes.dust, 1)),
            EnrichedNaquadahDust, NaquadriaDust,
            GGMaterial.lightNaquadahFuel.getFluidOrGas(1).getFluid(), recipeItemId(GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1)));
        catalog.orderDisplayRecipes(FUEL_TAG, GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1).getFluid(),
            recipeItemId(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1)));
        // spotless:on
    }

    private static void registerPreparationChains(RecipeCatalog catalog) {
        var enriched = source(
            BlastFurnace,
            new ItemStack[] { EnrichedNaquadahDust.getItemStack(16), GTUtility.getIntegratedCircuit(16) },
            Materials.HydrofluoricAcid.getFluid(3_000));
        var naquadria = source(
            BlastFurnace,
            new ItemStack[] { NaquadriaDust.getItemStack(32), GTUtility.getIntegratedCircuit(16) },
            GGMaterial.fluoroantimonicAcid.getFluidOrGas(4_000));
        var neutralization = source(
            LargeChemicalReactor,
            new ItemStack[] { Materials.Quicklime.getDust(8), GTUtility.getIntegratedCircuit(3) },
            GGMaterial.acidNaquadahEmulsion.getFluidOrGas(1_000));
        var centrifugation = source(
            CentrifugeNonCell,
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            GGMaterial.naquadahEmulsion.getFluidOrGas(1_000));
        var distillation = source(DistillationTower, new ItemStack[0], GGMaterial.naquadahSolution.getFluidOrGas(20));
        var antimonyDecomposition = source(
            ElectrolyzerNonCell,
            new ItemStack[] { Materials.AntimonyTrioxide.getDust(5) });
        var sludgeCentrifugation = source(
            CentrifugeNonCell,
            new ItemStack[] { GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 4) });
        var gasCracking = source(
            OilCracker,
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            GGMaterial.naquadahGas.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(4));
        var lightCracking = source(
            OilCracker,
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            GGMaterial.lightNaquadahFuel.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(4));
        var asphaltCracking = source(
            OilCracker,
            new ItemStack[] { GTUtility.getIntegratedCircuit(1) },
            GGMaterial.naquadahAsphalt.getFluidOrGas(1_000),
            GGMaterial.atomicSeparationCatalyst.getMolten(12));
        var lightDistillation = source(DistillationTower, new ItemStack[0], lightCracking.mFluidOutputs);
        var asphaltDistillation = source(DistillationTower, new ItemStack[0], asphaltCracking.mFluidOutputs);
        var gasDistillation = source(DistillationTower, new ItemStack[0], gasCracking.mFluidOutputs);
        RecipeData[] fuelPrecursors = {
            denseFuelPrecursor(
                source(MixerNonCell, new ItemStack[] { Materials.Graphite.getDust(3), Materials.Uranium.getDust(1) }),
                Materials.TungstenCarbide,
                GGMaterial.graphiteUraniumMixture.get(OrePrefixes.dust, 4),
                ItemRefer.Wrapped_Uranium_Ingot.get(4),
                ItemRefer.High_Density_Uranium_Nugget.get(5)),
            denseFuelPrecursor(
                source(
                    MixerNonCell,
                    new ItemStack[] { Materials.Plutonium.getDust(10), Materials.Uranium.getDust(2),
                        Materials.Carbon.getDust(8) },
                    Materials.Oxygen.getGas(12_000)),
                Materials.HSSS,
                GGMaterial.plutoniumOxideUraniumMixture.get(OrePrefixes.dust, 8),
                ItemRefer.Wrapped_Plutonium_Ingot.get(2),
                ItemRefer.High_Density_Plutonium_Nugget.get(5)) };

        Fluid acid = GGMaterial.acidNaquadahEmulsion.getFluidOrGas(1)
            .getFluid();
        Fluid emulsion = GGMaterial.naquadahEmulsion.getFluidOrGas(1)
            .getFluid();
        Fluid solution = GGMaterial.naquadahSolution.getFluidOrGas(1)
            .getFluid();
        Fluid heavy = GGMaterial.heavyNaquadahFuel.getFluidOrGas(1)
            .getFluid();
        Fluid light = GGMaterial.lightNaquadahFuel.getFluidOrGas(1)
            .getFluid();
        Fluid asphalt = GGMaterial.naquadahAsphalt.getFluidOrGas(1)
            .getFluid();
        Fluid gas = GGMaterial.naquadahGas.getFluidOrGas(1)
            .getFluid();
        Fluid moltenCatalyst = GGMaterial.atomicSeparationCatalyst.getMolten(1)
            .getFluid();
        var unstable = recipeItemId(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1));
        var denseUranium = recipeItemId(ItemRefer.High_Density_Uranium.get(1));
        var densePlutonium = recipeItemId(ItemRefer.High_Density_Plutonium.get(1));
        var sludge = recipeItemId(GGMaterial.radioactiveSludge.get(OrePrefixes.dust, 1));
        var naquadah = recipeItemId(Materials.Naquadah.getDust(1));
        RecipeData gasRecovery = prepareSourceRecipe(OilCracker, gasCracking).appendBalancedRecipe(
            prepareSourceRecipe(DistillationTower, gasDistillation),
            GTUtility.FluidId.create(gasCracking.mFluidOutputs[0]));
        long gasVoltage = Math.max(gasCracking.mEUt, gasDistillation.mEUt);
        RecipeData asphaltRecovery = prepareSourceRecipe(OilCracker, asphaltCracking).appendBalancedRecipe(
            prepareSourceRecipe(DistillationTower, asphaltDistillation),
            GTUtility.FluidId.create(asphaltCracking.mFluidOutputs[0]));
        long asphaltVoltage = Math.max(asphaltCracking.mEUt, asphaltDistillation.mEUt);
        RecipeData processing = prepareSourceRecipe(LargeChemicalReactor, neutralization)
            .appendBalancedRecipe(
                prepareSourceRecipe(CentrifugeNonCell, centrifugation),
                GTUtility.FluidId.create(emulsion))
            .appendBalancedRecipe(
                prepareSourceRecipe(DistillationTower, distillation),
                GTUtility.FluidId.create(solution))
            .appendBalancedRecipe(asphaltRecovery, GTUtility.FluidId.create(asphalt))
            .appendBalancedRecipe(gasRecovery, GTUtility.FluidId.create(gas))
            .withFullDustOutputs()
            .appendBalancedRecipe(
                prepareSourceRecipe(ElectrolyzerNonCell, antimonyDecomposition),
                recipeItemId(Materials.AntimonyTrioxide.getDust(1)));
        long processingVoltage = Math.max(
            Math.max(asphaltVoltage, gasVoltage),
            Math.max(
                antimonyDecomposition.mEUt,
                Math.max(neutralization.mEUt, Math.max(centrifugation.mEUt, distillation.mEUt))));
        // spotless:off
        // Main trunks: preserve the exact native balance, including asphalt, gas and antimony recovery.
        catalog.registerChain(PREPARATION_TAG, 1, Math.max(naquadria.mEUt, processingVoltage), NaquadriaDust,
            new Object[] { heavy, light, unstable },
            catalog.markPreparedRecipe(BlastFurnace, NaquadriaDust, heavy, 0,
                prepareSourceRecipe(BlastFurnace, naquadria)
                    .appendBalancedRecipe(processing, GTUtility.FluidId.create(acid))
                    .withFullDustOutputs()));
        catalog.setLatestQueryInputs(PREPARATION_TAG, NaquadriaDust, moltenCatalyst);

        // Recover scarce heavy fuel from existing light fuel; asphalt and gas are processed in the same step.
        catalog.registerAlternativeChain(PREPARATION_TAG,
            Math.max(Math.max(asphaltVoltage, gasVoltage), Math.max(lightCracking.mEUt, lightDistillation.mEUt)),
            light, new Object[] { heavy },
            catalog.markPreparedRecipe(OilCracker, light, heavy, 0,
                prepareSourceRecipe(OilCracker, lightCracking)
                    .appendBalancedRecipe(
                        prepareSourceRecipe(DistillationTower, lightDistillation),
                        GTUtility.FluidId.create(lightCracking.mFluidOutputs[0]))
                    .appendBalancedRecipe(asphaltRecovery, GTUtility.FluidId.create(asphalt))
                    .appendBalancedRecipe(gasRecovery, GTUtility.FluidId.create(gas))
                    .withFullDustOutputs()));
        catalog.setLatestQueryInputs(PREPARATION_TAG, light, moltenCatalyst);
        catalog.useAvailableInputsOnly(PREPARATION_TAG);
        catalog.registerAlternativeChain(PREPARATION_TAG, Math.max(enriched.mEUt, processingVoltage), EnrichedNaquadahDust,
            new Object[] { heavy, light },
            catalog.markPreparedRecipe(BlastFurnace, EnrichedNaquadahDust, heavy, 0,
                prepareSourceRecipe(BlastFurnace, enriched)
                    .appendBalancedRecipe(processing, GTUtility.FluidId.create(acid))
                    .withFullDustOutputs()));
        catalog.setLatestQueryInputs(PREPARATION_TAG, EnrichedNaquadahDust, moltenCatalyst);
        // Independent sludge recovery; it never pulls additional upstream fuel production.
        catalog.registerAlternativeChain(PREPARATION_TAG, sludgeCentrifugation.mEUt, sludge,
            new Object[] { naquadah },
            catalog.markPreparedRecipe(CentrifugeNonCell, sludge, naquadah, 0,
                prepareSourceRecipe(CentrifugeNonCell, sludgeCentrifugation)));
        catalog.useAvailableInputsOnly(PREPARATION_TAG);
        // spotless:on

        registerCatalysts(catalog, moltenCatalyst);
        Object[] precursorInputs = { recipeItemId(Materials.Uranium.getDust(1)),
            recipeItemId(Materials.Plutonium.getDust(1)) };
        long uraniumOutput = fuelPrecursors[0].outputs.get(denseUranium);
        long pairedPlutoniumOutput = Math.multiplyExact(fuelPrecursors[1].outputs.get(densePlutonium), 2);
        long precursorGcd = getGCD(uraniumOutput, pairedPlutoniumOutput);
        RecipeData precursor = fuelPrecursors[0]
            .combineRecipe(fuelPrecursors[1], pairedPlutoniumOutput / precursorGcd, uraniumOutput / precursorGcd);
        // spotless:off
        // Paired dense fuels retain the exact 2:1 uranium/plutonium output ratio.
        catalog.registerAlternativeChain(PREPARATION_TAG,
            precursor.times.keySet().stream().mapToLong(time -> time.eut()).max().getAsLong(), precursorInputs[0],
            new Object[] { denseUranium, densePlutonium },
            catalog.markPreparedRecipe(ElectricImplosionCompressor, precursorInputs[0], denseUranium, 0, precursor));
        catalog.setLatestQueryInputs(PREPARATION_TAG, precursorInputs);
        // spotless:on
    }

    private static void registerFuelChains(RecipeCatalog catalog) {
        RecipeMap<?> refinery = GoodGeneratorRecipeMaps.naquadahFuelRefineFactoryRecipes;
        Fluid fuelIII = GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1)
            .getFluid();
        Fluid fuel = GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1)
            .getFluid();
        Fluid heavy = GGMaterial.heavyNaquadahFuel.getFluidOrGas(1)
            .getFluid();
        Fluid light = GGMaterial.lightNaquadahFuel.getFluidOrGas(1)
            .getFluid();
        Fluid moltenCatalyst = GGMaterial.atomicSeparationCatalyst.getMolten(1)
            .getFluid();
        var unstable = recipeItemId(GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 1));
        var denseUranium = recipeItemId(ItemRefer.High_Density_Uranium.get(1));
        var densePlutonium = recipeItemId(ItemRefer.High_Density_Plutonium.get(1));
        // Read source voltages instead of replacing the refinery's nonstandard power values with tier constants.
        var regularIII = source(
            refinery,
            new ItemStack[] { GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 27), ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1) },
            GGMaterial.heavyNaquadahFuel.getFluidOrGas(800),
            GGMaterial.lightNaquadahFuel.getFluidOrGas(1_000));
        var advancedIII = source(
            refinery,
            new ItemStack[] { GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 8),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(9), ItemRefer.High_Density_Uranium.get(2),
                ItemRefer.High_Density_Plutonium.get(1) },
            GGMaterial.heavyNaquadahFuel.getFluidOrGas(800),
            GGMaterial.lightNaquadahFuel.getFluidOrGas(1_000));
        var regularIV = source(
            refinery,
            new ItemStack[] { GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                Materials.NetherStar.getDust(64), Materials.DraconiumAwakened.getDust(64),
                GGMaterial.orundum.get(OrePrefixes.dust, 32) },
            GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2_000),
            Materials.Praseodymium.getMolten(9_216));
        var advancedIV = source(
            refinery,
            new ItemStack[] { GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                Materials.Bedrockium.getDust(64), Materials.DraconiumAwakened.getDust(64),
                GGMaterial.orundum.get(OrePrefixes.dust, 64) },
            GGMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2_000),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(240));
        var directIV = source(
            refinery,
            new ItemStack[] { GGMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 54),
                GGMaterial.orundum.get(OrePrefixes.dust, 32), ItemRefer.High_Density_Uranium.get(10),
                ItemRefer.High_Density_Plutonium.get(5) },
            GGMaterial.heavyNaquadahFuel.getFluidOrGas(4_000),
            GGMaterial.lightNaquadahFuel.getFluidOrGas(5_000),
            MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(120),
            MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(6_480));
        // spotless:off
        // Main trunks: native refining variants keep their own voltage, quantities and coil requirements.
        catalog.registerChain("naquadah_fuel_3", 1, regularIII.mEUt, unstable,
            new Object[] { fuelIII },
            catalog.markRecipe(refinery, new Object[] { unstable, recipeItemId(WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 1)) }, fuelIII, -1));
        catalog.setLatestQueryInputs("naquadah_fuel_3", unstable, heavy, light, denseUranium, densePlutonium);
        catalog.registerAlternativeChain("naquadah_fuel_3", advancedIII.mEUt, unstable,
            new Object[] { fuelIII },
            catalog.markRecipe(refinery, new Object[] { unstable, recipeItemId(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(1)) }, fuelIII, -1));
        catalog.setLatestQueryInputs("naquadah_fuel_3", unstable, heavy, light, denseUranium, densePlutonium);

        catalog.registerChain(FUEL_TAG, 1, regularIV.mEUt, fuelIII,
            new Object[] { fuel },
            catalog.markRecipe(refinery, new Object[] { fuelIII, recipeItemId(Materials.NetherStar.getDust(1)) }, fuel, -1));
        catalog.setLatestQueryInputs(FUEL_TAG, fuelIII, unstable);
        catalog.registerAlternativeChain(FUEL_TAG, advancedIV.mEUt, fuelIII,
            new Object[] { fuel },
            catalog.markRecipe(refinery, new Object[] { fuelIII, recipeItemId(Materials.Bedrockium.getDust(1)) }, fuel, -1));
        catalog.setLatestQueryInputs(FUEL_TAG, fuelIII, unstable);
        catalog.registerAlternativeChain(FUEL_TAG, directIV.mEUt, unstable,
            new Object[] { fuel },
            catalog.markRecipe(refinery, new Object[] { unstable, heavy, light, denseUranium, densePlutonium }, fuel, -1));
        catalog.setLatestQueryInputs(FUEL_TAG, unstable, heavy, light, denseUranium, densePlutonium);
        // spotless:on

        Fluid fuelV = GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(1)
            .getFluid();
        GTRecipe regularV = source(
            refinery,
            new ItemStack[] { Materials.Infinity.getDust(8),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 32) },
            GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2_000),
            Materials.RadoxHeavy.getFluid(250));
        GTRecipe advancedV = source(
            refinery,
            new ItemStack[] { Materials.TranscendentMetal.getDust(16),
                GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 48) },
            GGMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2_000),
            Materials.RadoxHeavy.getFluid(250),
            Materials.Mellion.getMolten(288),
            Materials.Creon.getMolten(288));
        GTRecipe regularVI = source(
            refinery,
            new ItemStack[] { MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(64),
                Materials.Tritanium.getDust(32) },
            GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(2_000),
            GGMaterial.shirabon.getMolten(360));
        GTRecipe advancedVI = source(
            refinery,
            new ItemStack[] { MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(64),
                Materials.Tritanium.getDust(48) },
            GGMaterial.naquadahBasedFuelMkV.getFluidOrGas(2_000),
            GGMaterial.shirabon.getMolten(360),
            Materials.RawStarMatter.getFluid(60));
        Fluid fuelVI = GGMaterial.naquadahBasedFuelMkVI.getFluidOrGas(1)
            .getFluid();
        var infinityDust = recipeItemId(Materials.Infinity.getDust(1));
        var infinityIngot = recipeItemId(Materials.Infinity.getIngots(1));
        var catalystDust = recipeItemId(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 1));
        var catalystIngot = recipeItemId(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1));

        // spotless:off
        // Input branches: molten feeds use native solidification and maceration before dust-based refining.
        var infinitySolidification = catalog.markRecipeSequence(FluidSolidifier, Materials.Infinity.getMolten(1).getFluid(), infinityDust,
            catalog.markRecipe(FluidSolidifier, Materials.Infinity.getMolten(1).getFluid(), infinityIngot, -1),
            catalog.markRecipe(Macerator, infinityIngot, infinityDust, -1));
        var catalystSolidification = catalog.markRecipeSequence(FluidSolidifier, moltenCatalyst, catalystDust,
            catalog.markRecipe(FluidSolidifier, moltenCatalyst, catalystIngot, -1),
            catalog.markRecipe(Macerator, catalystIngot, catalystDust, -1));

        catalog.registerChain("naquadah_fuel_5", 1, regularV.mEUt, fuel,
            new Object[] { fuelV },
            catalog.markRecipe(refinery, new Object[] { fuel, infinityDust }, fuelV, -1));
        catalog.setLatestQueryInputs("naquadah_fuel_5", fuel, moltenCatalyst);
        catalog.registerChainInputSynthesis("naquadah_fuel_5", infinityDust, infinitySolidification);
        catalog.registerChainInputSynthesis("naquadah_fuel_5", catalystDust, catalystSolidification);
        catalog.registerAlternativeChain("naquadah_fuel_5", advancedV.mEUt, fuel,
            new Object[] { fuelV },
            catalog.markRecipe(refinery, new Object[] { fuel, recipeItemId(Materials.TranscendentMetal.getDust(1)) }, fuelV, -1));
        catalog.setLatestQueryInputs("naquadah_fuel_5", fuel, moltenCatalyst);
        catalog.registerChainInputSynthesis("naquadah_fuel_5", catalystDust, catalystSolidification);

        catalog.registerChain("naquadah_fuel_6", 1, regularVI.mEUt, fuelV,
            new Object[] { fuelVI },
            catalog.markRecipe(refinery, new Object[] { fuelV, recipeItemId(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1)) }, fuelVI, -1));
        catalog.registerAlternativeChain("naquadah_fuel_6", advancedVI.mEUt, fuelV,
            new Object[] { fuelVI },
            catalog.markRecipe(refinery, new Object[] { fuelV, recipeItemId(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1)) }, fuelVI, -1));
        // spotless:on
    }

    private static RecipeData denseFuelPrecursor(GTRecipe mixture, Materials foil, ItemStack mixedDust,
        ItemStack wrapped, ItemStack nuggets) {
        var smelting = source(BlastFurnace, new ItemStack[] { foil.getDust(1), GTUtility.getIntegratedCircuit(1) });
        RecipeData foilProduction = prepareSourceRecipe(BlastFurnace, smelting)
            .appendBalancedRecipe(
                prepareSourceRecipe(VacuumFreezer, source(VacuumFreezer, smelting.mOutputs)),
                recipeItemId(smelting.mOutputs[0]))
            .appendBalancedRecipe(
                prepareSourceRecipe(
                    Bender,
                    source(Bender, new ItemStack[] { foil.getIngots(1), GTUtility.getIntegratedCircuit(10) })),
                recipeItemId(foil.getIngots(1)));
        RecipeData data = prepareSourceRecipe(MixerNonCell, mixture)
            .appendBalancedRecipe(
                prepareSourceRecipe(
                    Assembler,
                    source(
                        Assembler,
                        new ItemStack[] { mixedDust, GTOreDictUnificator.get(OrePrefixes.foil, foil, 4),
                            GTUtility.getIntegratedCircuit(1) })),
                recipeItemId(mixedDust))
            .appendBalancedRecipe(
                prepareSourceRecipe(
                    ElectricImplosionCompressor,
                    source(ElectricImplosionCompressor, new ItemStack[] { wrapped })),
                recipeItemId(wrapped))
            .appendBalancedRecipe(
                prepareSourceRecipe(
                    ElectricImplosionCompressor,
                    source(
                        ElectricImplosionCompressor,
                        new ItemStack[] { nuggets },
                        Materials.Neutronium.getMolten(72))),
                recipeItemId(nuggets));
        data = foilProduction
            .appendBalancedRecipe(data, recipeItemId(GTOreDictUnificator.get(OrePrefixes.foil, foil, 1)))
            .withFullDustOutputs()
            .cancelReturnedResource(recipeItemId(foil.getDust(1)));
        if (foil == Materials.TungstenCarbide) {
            return prepareSourceRecipe(
                MixerNonCell,
                source(
                    MixerNonCell,
                    new ItemStack[] { Materials.Tungsten.getDust(1), Materials.Carbon.getDust(1),
                        GTUtility.getIntegratedCircuit(1) })).appendBalancedRecipe(data, recipeItemId(foil.getDust(1)));
        }
        data = prepareSourceRecipe(
            MixerNonCell,
            source(
                MixerNonCell,
                new ItemStack[] { Materials.HSSG.getDust(6), Materials.Iridium.getDust(2), Materials.Osmium.getDust(1),
                    GTUtility.getIntegratedCircuit(2) })).appendBalancedRecipe(data, recipeItemId(foil.getDust(1)));
        data = prepareSourceRecipe(
            MixerNonCell,
            source(
                MixerNonCell,
                new ItemStack[] { Materials.TungstenSteel.getDust(5), Materials.Chrome.getDust(1),
                    Materials.Molybdenum.getDust(2), Materials.Vanadium.getDust(1),
                    GTUtility.getIntegratedCircuit(1) }))
                        .appendBalancedRecipe(data, recipeItemId(Materials.HSSG.getDust(1)));
        return prepareSourceRecipe(
            MixerNonCell,
            source(
                MixerNonCell,
                new ItemStack[] { Materials.Tungsten.getDust(1), Materials.Steel.getDust(1),
                    GTUtility.getIntegratedCircuit(1) }))
                        .appendBalancedRecipe(data, recipeItemId(Materials.TungstenSteel.getDust(1)));
    }

    private static void registerCatalysts(RecipeCatalog catalog, Fluid moltenCatalyst) {
        var raw = recipeItemId(ItemRefer.Raw_Atomic_Separation_Catalyst.get(1));
        var orundum = recipeItemId(GGMaterial.orundum.get(OrePrefixes.dust, 1));
        var ingot = GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingot, 1);
        RecipeData catalyst = prepareSourceRecipe(
            ElectricImplosionCompressor,
            source(
                ElectricImplosionCompressor,
                new ItemStack[] { GGMaterial.orundum.get(OrePrefixes.dust, 4), GTUtility.getIntegratedCircuit(1) }))
                    .appendBalancedRecipe(
                        prepareSourceRecipe(
                            Compressor,
                            source(Compressor, new ItemStack[] { GGMaterial.orundum.get(OrePrefixes.gem, 9) })),
                        recipeItemId(GGMaterial.orundum.get(OrePrefixes.gem, 1)))
                    .appendBalancedRecipe(
                        prepareSourceRecipe(
                            Cutter,
                            source(
                                Cutter,
                                new ItemStack[] { GGMaterial.orundum.get(OrePrefixes.block, 1) },
                                Materials.Water.getFluid(1_000))),
                        recipeItemId(GGMaterial.orundum.get(OrePrefixes.block, 1)))
                    .appendBalancedRecipe(
                        prepareSourceRecipe(
                            BlastFurnace,
                            source(
                                BlastFurnace,
                                new ItemStack[] { GGMaterial.orundum.get(OrePrefixes.plate, 2),
                                    ItemRefer.Raw_Atomic_Separation_Catalyst.get(4) },
                                Materials.Plutonium.getMolten(144))),
                        recipeItemId(GGMaterial.orundum.get(OrePrefixes.plate, 1)))
                    .appendBalancedRecipe(
                        prepareSourceRecipe(
                            VacuumFreezer,
                            source(
                                VacuumFreezer,
                                new ItemStack[] { GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1) })),
                        recipeItemId(GGMaterial.atomicSeparationCatalyst.get(OrePrefixes.ingotHot, 1)))
                    .withFullDustOutputs();
        catalyst = prepareSourceRecipe(
            FluidExtractor,
            source(FluidExtractor, new ItemStack[] { Materials.Plutonium.getDust(1) }))
                .appendBalancedRecipe(catalyst, GTUtility.FluidId.create(Materials.Plutonium.getMolten(1)));
        catalyst = catalyst.appendBalancedRecipe(
            prepareSourceRecipe(FluidExtractor, source(FluidExtractor, new ItemStack[] { ingot })),
            recipeItemId(ingot));
        var naquadahSmelting = source(
            BlastFurnace,
            new ItemStack[] { Materials.Naquadah.getDust(1), GTUtility.getIntegratedCircuit(11) },
            WerkstoffLoader.Xenon.getFluidOrGas(1_000));
        RecipeData moltenNaquadah = prepareSourceRecipe(BlastFurnace, naquadahSmelting)
            .appendBalancedRecipe(
                prepareSourceRecipe(VacuumFreezer, source(VacuumFreezer, naquadahSmelting.mOutputs)),
                recipeItemId(naquadahSmelting.mOutputs[0]))
            .appendBalancedRecipe(
                prepareSourceRecipe(
                    FluidExtractor,
                    source(FluidExtractor, new ItemStack[] { Materials.Naquadah.getIngots(1) })),
                recipeItemId(Materials.Naquadah.getIngots(1)));
        ItemStack[][] rawInputs = { { Materials.Blaze.getDust(32), Materials.Firestone.getDust(4) },
            { Materials.Draconium.getDust(4), Materials.Quantium.getDust(4) },
            { Materials.Ardite.getDust(4), Materials.Manyullyn.getDust(4) } };
        int variant = 0;
        for (ItemStack first : rawInputs[0]) {
            for (ItemStack second : rawInputs[1]) {
                for (ItemStack third : rawInputs[2]) {
                    RecipeData data = moltenNaquadah
                        .appendBalancedRecipe(
                            prepareSourceRecipe(
                                MixerNonCell,
                                source(
                                    MixerNonCell,
                                    new ItemStack[] { first, second, third, GTUtility.getIntegratedCircuit(4) },
                                    Materials.Naquadah.getMolten(288))),
                            GTUtility.FluidId.create(Materials.Naquadah.getMolten(1)))
                        .appendBalancedRecipe(catalyst, raw);
                    // spotless:off
                    catalog.registerAlternativeChain(PREPARATION_TAG,
                        data.times.keySet().stream().mapToLong(time -> time.eut()).max().getAsLong(), orundum,
                        new Object[] { moltenCatalyst },
                        catalog.markPreparedRecipe(FluidExtractor, orundum, moltenCatalyst, variant++, data));
                    catalog.setLatestQueryInputs(PREPARATION_TAG, orundum, recipeItemId(first));
                    // spotless:on
                }
            }
        }
        catalog.groupLatestDisplayRecipes(PREPARATION_TAG, variant, rawInputs);
    }

    private static GTRecipe source(RecipeMap<?> map, ItemStack[] items, FluidStack... fluids) {
        GTRecipe recipe = map.findRecipeQuery()
            .items(items)
            .fluids(fluids)
            .find();
        if (recipe == null)
            throw new IllegalStateException("Missing naquadah fuel source recipe: " + map.unlocalizedName);
        return recipe;
    }
}
