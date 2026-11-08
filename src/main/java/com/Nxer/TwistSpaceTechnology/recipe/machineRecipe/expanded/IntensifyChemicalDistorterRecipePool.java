package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class IntensifyChemicalDistorterRecipePool {

    private static final RecipeMap<?> ICD = GTCMRecipe.IntensifyChemicalDistorterRecipeMap;

    // Circuit allocation, ordered below; alternative routes share a circuit when inputs distinguish them.
    // 1: original PBI routes; 2: direct elemental PBI; 3-4: epoxy / reinforced epoxy.
    // 5-7: silicone / styrene-butadiene rubber / PPS; 8: PTFE and PVC; 9: Kevlar.
    // 10-11: advanced glue / super glue; 12-13: phthalic / phosphoric acid.
    // 14: both fluoroantimonic acid routes; 15: agar; 16: potassium / sodium ethyl xanthate.
    // 17: hydrogen peroxide (registered in loadRecipePostInit after its external fluid is available).

    // Coil enum names are not their crafting voltage tiers: UHV = Trinium, UEV = Electrum Flux,
    // UIV = Awakened Draconium, UMV = Infinity, UXV = Hypogen.
    // The UV controller retains low-coil bulk chemistry; later coils unlock direct and advanced synthesis.
    // Low-voltage batch durations account for perfect overclocks at UV plus the excess-coil speed bonus.

    // spotless:off
    public static void loadRecipes() {

        TwistSpaceTechnology.LOG.info("IntensifyChemicalDistorterRecipePool loading recipes.");

        // region PBI
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTUtility.copyAmountUnsafe(0, Materials.Potassiumdichromate.getDust(1)),
                Materials.Copper.getDust(16),
                GTUtility.copyAmountUnsafe(144, Materials.Zinc.getDust(1)))
            .fluidInputs(
                Materials.Dimethylbenzene.getFluid(144000),
                Materials.Chlorobenzene.getFluid(288000),
                Materials.SulfuricAcid.getFluid(144000),
                Materials.Hydrogen.getGas(1728000),
                Materials.Nitrogen.getGas(576000),
                Materials.Oxygen.getGas(2016000))
            .fluidOutputs(Materials.Polybenzimidazole.getMolten(216000), Materials.HydrochloricAcid.getFluid(288000))
            .specialValue((int) HeatingCoilLevel.UHV.getHeat())
            .eut(RECIPE_UV)
            .duration(20 * 16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                Materials.Copper.getDust(16),
                GTUtility.copyAmountUnsafe(144, Materials.Zinc.getDust(1)))
            .fluidInputs(
                Materials.PhthalicAcid.getFluid(144000),
                Materials.Chlorobenzene.getFluid(288000),
                Materials.SulfuricAcid.getFluid(144000),
                Materials.Hydrogen.getGas(1728000),
                Materials.Nitrogen.getGas(576000),
                Materials.Oxygen.getGas(1152000))
            .fluidOutputs(Materials.Polybenzimidazole.getMolten(216000), Materials.HydrochloricAcid.getFluid(288000))
            .specialValue((int) HeatingCoilLevel.UHV.getHeat())
            .eut(RECIPE_UV)
            .duration(20 * 16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                GTUtility.copyAmountUnsafe(0, Materials.Potassiumdichromate.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 18, Materials.Carbon.getDust(1)),
                Materials.Copper.getDust(16),
                GTUtility.copyAmountUnsafe(144, Materials.Zinc.getDust(1)))
            .fluidInputs(
                Materials.Chlorobenzene.getFluid(288000),
                Materials.SulfuricAcid.getFluid(144000),
                Materials.Hydrogen.getGas(3168000),
                Materials.Nitrogen.getGas(576000),
                Materials.Oxygen.getGas(2016000))
            .fluidOutputs(Materials.Polybenzimidazole.getMolten(216000), Materials.HydrochloricAcid.getFluid(288000))
            .specialValue((int) HeatingCoilLevel.UHV.getHeat())
            .eut(RECIPE_UV)
            .duration(20 * 16)
            .addTo(ICD);

        // Direct C20H12N4 polymerization: one repeat-unit batch per 1000 L.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(2),
                GTUtility.copyAmountUnsafe(20 * 216, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 12 * 216),
                Materials.Nitrogen.getGas(1000 * 4 * 216))
            .fluidOutputs(Materials.Polybenzimidazole.getMolten(1000 * 216))
            .specialValue((int) HeatingCoilLevel.UEV.getHeat())
            .eut(RECIPE_UV)
            .duration(20 * 32)
            .addTo(ICD);
        // endregion

        // region Epoxid
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTUtility.copyAmountUnsafe(64 * 11 + 52, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .fluidOutputs(Materials.Epoxid.getMolten(1000 * 36))
            .specialValue((int) HeatingCoilLevel.HV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 36)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                GTUtility.copyAmountUnsafe(64 * 13 + 49, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 * 3 + 58, Materials.EpoxidFiberReinforced.getPlates(64)))
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 384 * 36)
            .addTo(ICD);
        // endregion

        // region Silicone
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                Materials.Sulfur.getDust(64),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 6, Materials.Carbon.getDust(1))
            )
            .fluidInputs(Materials.Hydrogen.getGas(12000 * 64), Materials.Water.getFluid(3000 * 64))
            .fluidOutputs(Materials.RubberSilicone.getMolten(1296 * 64))
            .specialValue((int) HeatingCoilLevel.LV.getHeat())
            .eut(96)
            .duration(20 * 512 * 64)
            .addTo(ICD);
        // endregion

        // region Styrene-butadiene rubber
        // C8H8 + 3 C4H6 supplies 27 raw rubber; 3 sulfur vulcanizes all 27 ingots.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                GTUtility.copyAmountUnsafe(20 * 64, Materials.Carbon.getDust(1)),
                GTUtility.copyAmountUnsafe(3 * 64, Materials.Sulfur.getDust(1)))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 26 * 64))
            .fluidOutputs(Materials.StyreneButadieneRubber.getMolten(144 * 27 * 64))
            .specialValue((int) HeatingCoilLevel.MV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

        // region Polyphenylene sulfide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(7), Materials.Sulfur.getDust(64))
            .fluidInputs(Materials.Benzene.getFluid(64000))
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(96000), Materials.Hydrogen.getGas(128000))
            .specialValue((int) HeatingCoilLevel.MV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

        // region PTFE
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                GTUtility.copyAmountUnsafe(64 * 9, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Fluorine.getGas(4000 * 9 * 32), Materials.Oxygen.getGas(125000 * 32))
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000 * 32))
            .specialValue((int) HeatingCoilLevel.HV.getHeat())
            .eut(RECIPE_IV)
            .duration(20 * 48 * 32)
            .addTo(ICD);
        // endregion

        // region PVC
        // Direct C2H3Cl polymerization: one repeat-unit batch per 1000 L.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(8),
                GTUtility.copyAmountUnsafe(2 * 64, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 3 * 64),
                Materials.Chlorine.getGas(1000 * 64))
            .fluidOutputs(Materials.PolyvinylChloride.getMolten(1000 * 64))
            .specialValue((int) HeatingCoilLevel.MV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

        // region Kevlar
        // 18 C14H10N2O2 repeat-unit batches yield 18000 L, retaining the 125-ingot batch.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(9),
                GTUtility.copyAmountUnsafe(14 * 18, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 10 * 18),
                Materials.Nitrogen.getGas(1000 * 2 * 18),
                Materials.Oxygen.getGas(1000 * 2 * 18))
            .fluidOutputs(Materials.Kevlar.getMolten(144 * 125))
            .specialValue((int) HeatingCoilLevel.UXV.getHeat())
            .eut(RECIPE_UIV)
            .duration(20 * 64)
            .addTo(ICD);
        // endregion

        // region Advanced glue
        // 2 C4H6O2 (polyvinyl acetate) + 3 C3H6O (acetone) yields 5000 L of glue.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(10),
                GTUtility.copyAmountUnsafe(17 * 64, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 30 * 64),
                Materials.Oxygen.getGas(1000 * 7 * 64))
            .fluidOutputs(Materials.GlueAdvanced.getFluid(1000 * 5 * 64))
            .specialValue((int) HeatingCoilLevel.EV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 384 * 64)
            .addTo(ICD);
        // endregion

        // region Ethyl Cyanoacrylate Super Glue
        // Direct C6H7NO2 synthesis, scaling the original 10000 L batch by 64.
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                GTUtility.copyAmountUnsafe(6 * 10 * 64, Materials.Carbon.getDust(1))
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 7 * 10 * 64),
                Materials.Nitrogen.getGas(1000 * 10 * 64),
                Materials.Oxygen.getGas(1000 * 2 * 10 * 64)
            )
            .fluidOutputs(
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1000 * 10 * 64)
            )
            .specialValue((int) HeatingCoilLevel.UMV.getHeat())
            .eut(RECIPE_UEV)
            .duration(20 * 8 * 64)
            .addTo(ICD);
        // endregion

        // region Phthalic acid
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(12),
                GTUtility.copyAmountUnsafe(0, Materials.Potassiumdichromate.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 8, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*6*64),
                Materials.Oxygen.getGas(1000*4*64))
            .fluidOutputs(Materials.PhthalicAcid.getFluid(1000*64))
            .specialValue((int) HeatingCoilLevel.UEV.getHeat())
            .eut(RECIPE_UHV)
            .duration(20 * 32)
            .addTo(ICD);
        // endregion

        // region Phosphoric Acid
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(13),
                GTUtility.copyAmountUnsafe(64 * 9, Materials.Apatite.getDust(1)))
            .fluidInputs(Materials.Water.getFluid(1000 * 5 * 64))
            .itemOutputs(GTUtility.copyAmountUnsafe(64 * 5, Materials.Calcium.getDust(1)))
            .fluidOutputs(
                Materials.PhosphoricAcid.getFluid(1000 * 3 * 64),
                Materials.HydrochloricAcid.getFluid(1000 * 1 * 64))
            .specialValue((int) HeatingCoilLevel.LV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

        // region HSbF6
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(14),
                GTUtility.copyAmountUnsafe(64 * 12, Materials.Antimony.getDust(64)))
            .fluidInputs(
                Materials.Fluorine.getGas(1000 * 5 * 12 * 64),
                Materials.HydrofluoricAcid.getFluid(1000 * 12 * 64))
            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue((int) HeatingCoilLevel.UIV.getHeat())
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(14),
                GTUtility.copyAmountUnsafe(64 * 12, Materials.Antimony.getDust(64)))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12 * 64), Materials.Hydrogen.getGas(1000 * 12 * 64))

            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue((int) HeatingCoilLevel.UIV.getHeat())
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);
        // endregion

        // region Agar
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(15),
                GTUtility.copyAmountUnsafe(64 * 6, Materials.MeatRaw.getDust(64)))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000 * 64),
                Materials.PhosphoricAcid.getFluid(64000))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 * 8, GTModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2))
            )
            .specialValue((int) HeatingCoilLevel.IV.getHeat())
            .eut(RECIPE_IV)
            .duration(20 * 32 * 64)
            .addTo(ICD);
        // endregion

        // region Potassium Ethyl Xanthate
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTUtility.copyAmountUnsafe(3 * 64, Materials.Carbon.getDust(1)),
                GTUtility.copyAmountUnsafe(2 * 64, Materials.Sulfur.getDust(1)),
                Materials.Potassium.getDust(64)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 5 * 64),
                Materials.Oxygen.getGas(1000 * 64)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(12 * 64, GregtechItemList.PotassiumEthylXanthate.get(1))
            )
            .specialValue((int) HeatingCoilLevel.IV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

        // region Sodium Ethyl Xanthate
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(16),
                GTUtility.copyAmountUnsafe(3 * 64, Materials.Carbon.getDust(1)),
                GTUtility.copyAmountUnsafe(2 * 64, Materials.Sulfur.getDust(1)),
                Materials.Sodium.getDust(64)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 5 * 64),
                Materials.Oxygen.getGas(1000 * 64)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(12 * 64, GregtechItemList.SodiumEthylXanthate.get(1))
            )
            .specialValue((int) HeatingCoilLevel.IV.getHeat())
            .eut(RECIPE_HV)
            .duration(20 * 256 * 64)
            .addTo(ICD);
        // endregion

    }

    public static void loadRecipePostInit() {
        // region H2O2
        GTValues.RA
            .stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(17))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*128),
                Materials.Oxygen.getGas(1000*128))
            .fluidOutputs(getFluidStack("fluid.hydrogenperoxide",1000*64))
            .specialValue((int) HeatingCoilLevel.UEV.getHeat())
            .eut(RECIPE_UHV)
            .duration(20 * 32)
            .addTo(ICD);

        // endregion

    }
    // spotless:on
}
