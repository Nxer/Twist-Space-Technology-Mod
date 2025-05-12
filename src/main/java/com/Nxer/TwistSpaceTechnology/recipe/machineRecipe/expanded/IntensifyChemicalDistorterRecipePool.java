package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.dreammaster.gthandler.CustomItemList;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.material.MaterialMisc;
import gtnhlanth.common.register.WerkstoffMaterialPool;

public class IntensifyChemicalDistorterRecipePool {

    private static final RecipeMap<?> ICD = GTCMRecipe.IntensifyChemicalDistorterRecipes;

    // spotless:off
    public static void loadRecipes() {

        TwistSpaceTechnology.LOG.info("IntensifyChemicalDistorterRecipePool loading recipes.");

        // region PBI
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
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
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_UV)
            .duration(96)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
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
            .specialValue(9900)
            .eut(RECIPE_UV)
            .duration(96)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
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
            .specialValue(9900)
            .eut(RECIPE_UV)
            .duration(96)
            .addTo(ICD);


        // endregion

        // region Phthalic acid
        GTValues.RA
            .stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(6),
                GTUtility.copyAmountUnsafe(0, Materials.Potassiumdichromate.getDust(1)),
                Materials.Carbon.getDust(8))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*6),
                Materials.Oxygen.getGas(1000*4))

            .fluidOutputs(Materials.PhthalicAcid.getFluid(1000))
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_UV)
            .duration(5)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(7),
                GTUtility.copyAmountUnsafe(0, Materials.Potassiumdichromate.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 8, Materials.Carbon.getDust(1)))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*6*64),
                Materials.Oxygen.getGas(1000*4*64))
            .fluidOutputs(Materials.PhthalicAcid.getFluid(1000*64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(ICD);

        // endregion

        // region Phosphoric Acid
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                Materials.Apatite.getDust(9))
            .fluidInputs(
                Materials.Water.getFluid(1000 * 5))
            .itemOutputs(
                Materials.Calcium.getDust(5))
            .fluidOutputs(
                Materials.PhosphoricAcid.getFluid(1000 * 3),
                Materials.HydrochloricAcid.getFluid(1000))
            .specialValue(3600)
            .eut(RECIPE_MV)
            .duration(20 * 8)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                GTUtility.copyAmountUnsafe(64 * 9, Materials.Apatite.getDust(1)))
            .fluidInputs(Materials.Water.getFluid(1000 * 5 * 64))
            .itemOutputs(GTUtility.copyAmountUnsafe(64 * 5, Materials.Calcium.getDust(1)))
            .fluidOutputs(
                Materials.PhosphoricAcid.getFluid(1000 * 3 * 64),
                Materials.HydrochloricAcid.getFluid(1000 * 1 * 64))
            .specialValue(4500)
            .eut(RECIPE_HV)
            .duration(20 * 8 * 16)
            .addTo(ICD);
        // endregion

        // region Silicone
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(11),
                Materials.Sulfur.getDust(1),
                Materials.Silicon.getDust(3),
                Materials.Carbon.getDust(6))
            .fluidInputs(Materials.Hydrogen.getGas(12000), Materials.Water.getFluid(3000))

            .fluidOutputs(Materials.Silicone.getMolten(1296))
            .noOptimize()
            .specialValue(400)
            .eut(96)
            .duration(128)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                Materials.Sulfur.getDust(64),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 6, Materials.Carbon.getDust(1))
            )
            .fluidInputs(Materials.Hydrogen.getGas(12000 * 64), Materials.Water.getFluid(3000 * 64))
            .fluidOutputs(Materials.Silicone.getMolten(1296 * 64))
            .noOptimize()
            .specialValue(800)
            .eut(96)
            .duration(128 * 64)
            .addTo(ICD);

        // endregion

        // region Polyphenylene sulfide
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.Benzene.getFluid(1000))
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(1500), Materials.Hydrogen.getGas(2000))
            .noOptimize()
            .specialValue(400)
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(19), Materials.Sulfur.getDust(64))
            .fluidInputs(Materials.Benzene.getFluid(64000))
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(96000), Materials.Hydrogen.getGas(128000))
            .noOptimize()
            .specialValue(800)
            .eut(RECIPE_HV)
            .duration(128 * 64)
            .addTo(ICD);

        // endregion

        // region Agar
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), Materials.MeatRaw.getDust(8))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000),
                Materials.PhosphoricAcid.getFluid(1000),
                Materials.Water.getFluid(8000))
            .itemOutputs(GTModHandler.getModItem("dreamcraft", "GTNHBioItems", 8, 2))
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_IV)
            .duration(256)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                GTUtility.copyAmountUnsafe(64 * 6, Materials.MeatRaw.getDust(64)))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000 * 64),
                Materials.PhosphoricAcid.getFluid(64000))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 * 8, GTModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2))
            )
            .noOptimize()
            .specialValue(10800)
            .eut(RECIPE_IV)
            .duration(256 * 36)
            .addTo(ICD);
        // endregion

        // region Kevlar
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(23),
                ItemList.Spinneret.get(0),
                GTUtility.copyAmountUnsafe(64 * 4 + 6, Materials.Carbon.getDust(64)),
                Materials.Calcium.getDust(2))
            .fluidInputs(
                Materials.Chlorine.getGas(1000 * 34),
                Materials.Hydrogen.getGas(1000 * 230),
                Materials.Oxygen.getGas(1000 * 36),
                Materials.Nitrogen.getGas(1000 * 36))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 + 61, ItemList.WovenKevlar.get(64)))
            .specialValue(11700)
            .eut(RECIPE_UIV)
            .duration(20 * 64)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(22),
                Materials.Tin.getDust(45),
                GTUtility.copyAmountUnsafe(64 * 71 + 11, Materials.Carbon.getDust(1)),
                Materials.Nickel.getDust(5),
                Materials.Palladium.getDust(10),
                Materials.Iron.getDust(5),
                Materials.Silicon.getDust(36))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 1964),
                Materials.Hydrogen.getGas(1000 * 5292),
                Materials.Chlorine.getGas(1000 * 87),
                Materials.Nitrogen.getGas(1000 * 450))
            .fluidOutputs(MaterialsKevlar.PolyurethaneResin.getFluid(1000 * 45))
            .specialValue(11700)
            .eut(RECIPE_UIV)
            .duration(20 * 64)
            .addTo(ICD);

        // endregion

        // region PTFE
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), Materials.Carbon.getDust(2 * 9))
            .fluidInputs(Materials.Fluorine.getGas(4000 * 9), Materials.Oxygen.getGas(125000))
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000))
            .noOptimize()
            .specialValue(1800)
            .eut(RECIPE_IV)
            .duration(20 * 12)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                GTUtility.copyAmountUnsafe(64 * 9, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Fluorine.getGas(4000 * 9 * 32), Materials.Oxygen.getGas(125000 * 32))
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000 * 32))
            .specialValue(1800)
            .eut(RECIPE_IV)
            .duration(20 * 12 * 32)
            .addTo(ICD);

        // endregion

        // region HSbF6
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(10), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 5 * 12), Materials.HydrofluoricAcid.getFluid(1000 * 12))
            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(18),
                GTUtility.copyAmountUnsafe(64 * 12, Materials.Antimony.getDust(64)))
            .fluidInputs(
                Materials.Fluorine.getGas(1000 * 5 * 12 * 64),
                Materials.HydrofluoricAcid.getFluid(1000 * 12 * 64))
            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(11), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12), Materials.Hydrogen.getGas(1000 * 12))
            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(19),
                GTUtility.copyAmountUnsafe(64 * 12, Materials.Antimony.getDust(64)))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12 * 64), Materials.Hydrogen.getGas(1000 * 12 * 64))

            .fluidOutputs(GGMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);

        // endregion

        // region Epoxid
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3), Materials.Carbon.getDust(21))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24), Materials.Oxygen.getGas(1000 * 4))
            .fluidOutputs(Materials.Epoxid.getMolten(1000))
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(4),
                GTUtility.copyAmountUnsafe(64 * 11 + 52, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .fluidOutputs(Materials.Epoxid.getMolten(1000 * 36))
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(5),
                GTUtility.copyAmountUnsafe(64 * 13 + 49, Materials.Carbon.getDust(64)))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 * 3 + 58, Materials.EpoxidFiberReinforced.getPlates(64)))
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        // endregion

        // region Bastnasite
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Carbon.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 16),
                GTUtility.copyAmountUnsafe(64 * 2, Materials.Sugar.getDust(1))
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 768),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 + 15, Materials.Cerium.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 2 + 35, Materials.Titanium.getDust(1))
            )
            .fluidOutputs(
                Materials.Fluorine.getGas(1000 * 12),
                Materials.Oxygen.getGas(1000 * 150)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Carbon.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 48),
                GTUtility.copyAmountUnsafe(64 * 2, Materials.Sugar.getDust(1))
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 752),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 + 15, Materials.Cerium.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 2 + 35, Materials.Titanium.getDust(1))
            )
            .fluidOutputs(
                Materials.Fluorine.getGas(1000 * 12),
                Materials.Oxygen.getGas(1000 * 166)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bastnasite, 64),// Bastnasite
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Carbon.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 16),
                GTUtility.copyAmountUnsafe(64 * 2, Materials.Sugar.getDust(1))
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 768),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 + 15, Materials.Cerium.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 2 + 35, Materials.Titanium.getDust(1))
            )
            .fluidOutputs(
                Materials.Fluorine.getGas(1000 * 12),
                Materials.Oxygen.getGas(1000 * 150)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Bastnasite, 64),// Bastnasite
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Carbon.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 48),
                GTUtility.copyAmountUnsafe(64 * 2, Materials.Sugar.getDust(1))

            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 752),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(64 + 15, Materials.Cerium.getDust(1)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GTUtility.copyAmountUnsafe(64 * 3, Materials.Silicon.getDust(1)),
                GTUtility.copyAmountUnsafe(64 * 2 + 35, Materials.Titanium.getDust(1))
            )
            .fluidOutputs(
                Materials.Fluorine.getGas(1000 * 12),
                Materials.Oxygen.getGas(1000 * 166)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        // endregion

        // region Living Solder
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 8),
                Materials.InfinityCatalyst.getDust(2)
            )
            .fluidInputs(
                Materials.Tin.getPlasma(1000*18),
                Materials.Bismuth.getPlasma(1000*18),
                FluidRegistry.getFluidStack("cryotheum", 1000 * 4),
                Materials.Neutronium.getMolten(144*16)
            )
            .fluidOutputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144*280*2)
            )
            .specialValue(11700)
            .eut(RECIPE_UEV)
            .duration(20*400)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(9),
                GTUtility.copyAmountUnsafe(64 * 12, ItemList.Circuit_Chip_Biocell.get(64)),
                GTUtility.copyAmountUnsafe(8 * 12, GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 8)),
                Materials.InfinityCatalyst.getDust(2 * 12)
            )
            .fluidInputs(
                Materials.Tin.getPlasma(1000*18*12),
                Materials.Bismuth.getPlasma(1000*18*12),
                FluidRegistry.getFluidStack("cryotheum", 1000 * 4 *12),
                Materials.Neutronium.getMolten(144*16*12)
            )
            .fluidOutputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(144*280*2*16)
            )
            .specialValue(12600)
            .eut(RECIPE_UEV)
            .duration(20*1600)
            .addTo(ICD);
        // endregion

        // region Ethyl Cyanoacrylate Super Glue
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(15),
                Materials.Carbon.getDust(41)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*142),
                Materials.Oxygen.getGas(1000*41)
            )
            .fluidOutputs(
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(1000*10)
            )
            .specialValue(11700)
            .eut(RECIPE_UEV)
            .duration(20*8)
            .addTo(ICD);
        // endregion

        // region Potassium Ethyl Xanthate
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(14),
                Materials.Carbon.getDust(3),
                Materials.Sulfur.getDust(2),
                Materials.Potassium.getDust(1)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*5),
                Materials.Oxygen.getGas(1000)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(12, GenericChem.mPotassiumEthylXanthate)
            )
            .specialValue(9000)
            .eut(RECIPE_HV)
            .duration(20*30)
            .addTo(ICD);
        // endregion

        // region Sodium Ethyl Xanthate
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.getIntegratedCircuit(14),
                Materials.Carbon.getDust(3),
                Materials.Sulfur.getDust(2),
                Materials.Sodium.getDust(1)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*5),
                Materials.Oxygen.getGas(1000)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(12, GenericChem.mSodiumEthylXanthate)
            )
            .specialValue(9000)
            .eut(RECIPE_HV)
            .duration(20*30)
            .addTo(ICD);
        // endregion

        // region Platinum Group

        // Platinum
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 28)
            )
            .fluidInputs(
                Materials.Chlorine.getGas(3760),
                Materials.Oxygen.getGas(1000*45)
            )
            .itemOutputs(
                Materials.Platinum.getDust(20),
                WerkstoffLoader.PTResidue.get(OrePrefixes.dust, 20),
                WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 36)
            )
            .specialValue(9900)
            .eut(RECIPE_EV)
            .duration(20*20)
            .addTo(ICD);

        // Palladium
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 28),
                Materials.Carbon.getDust(10)
            )
            .fluidInputs(
                Materials.Nitrogen.getGas(1000*70),
                Materials.Hydrogen.getGas(1000*190),
                Materials.Oxygen.getGas(1000*20)
            )
            .itemOutputs(
                Materials.Palladium.getDust(10)
            )
            .specialValue(9900)
            .eut(RECIPE_HV)
            .duration(20*10)
            .addTo(ICD);

        // Platinum Residue
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                GTUtility.copyAmountUnsafe(220, WerkstoffLoader.PTResidue.get(OrePrefixes.dust, 1)),
                Materials.Sulfur.getDust(52),
                GTUtility.copyAmountUnsafe(570, Materials.Saltpeter.getDust(1))
            )
            .fluidInputs(
                Materials.Oxygen.getGas(1000*83),
                Materials.SaltWater.getFluid(1000*57)
            )
            .itemOutputs(
                GTUtility.copyAmountUnsafe(198, WerkstoffLoader.CrudeRhMetall.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(171, WerkstoffLoader.SodiumRuthenate.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(342, WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 1))
            )
            .specialValue(9900)
            .eut(RECIPE_IV)
            .duration(20*100)
            .addTo(ICD);

        // Rhodium
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                GTUtility.copyAmountUnsafe(100, WerkstoffLoader.CrudeRhMetall.get(OrePrefixes.dust, 1)),
                Materials.Sodium.getDust(50)
            )
            .fluidInputs(
                Materials.Nitrogen.getGas(1000),
                Materials.Oxygen.getGas(1000*180),
                Materials.Chlorine.getGas(1000*90)
            )
            .itemOutputs(
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 57)
            )
            .fluidOutputs(
                Materials.Hydrogen.getGas(1000*71)
            )
            .specialValue(9900)
            .eut(RECIPE_IV)
            .duration(20*57)
            .addTo(ICD);

        // Ruthenium
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                WerkstoffLoader.SodiumRuthenate.get(OrePrefixes.dust, 5)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*72)
            )
            .itemOutputs(
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 12),
                Materials.Sodium.getDust(5)
            )
            .fluidOutputs(
                Materials.Chlorine.getGas(2500)
            )
            .specialValue(9900)
            .eut(RECIPE_LuV)
            .duration(20*12)
            .addTo(ICD);

        // Rarest Metal Residue
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 20)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*51),
                Materials.Chlorine.getGas(1000*24)
            )
            .itemOutputs(
                Materials.Osmium.getDust(1),
                Materials.Iridium.getDust(10),
                Materials.Nickel.getDust(5),
                Materials.Copper.getDust(5),
                Materials.SiliconDioxide.getDust(6),
                Materials.Gold.getDust(4)
            )
            .specialValue(10800)
            .eut(RECIPE_UV)
            .duration(25)
            .addTo(ICD);

        // Iridium Residue
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1)),
                WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 10)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*40),
                Materials.Chlorine.getGas(1000*20)
            )
            .itemOutputs(
                Materials.Iridium.getDust(10),
                Materials.Nickel.getDust(5),
                Materials.Copper.getDust(5),
                Materials.SiliconDioxide.getDust(6),
                Materials.Gold.getDust(4)
            )
            .specialValue(10800)
            .eut(RECIPE_ZPM)
            .duration(20*2)
            .addTo(ICD);

        // Acidic Osmium Solution
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, CustomItemList.RadoxPolymerLens.get(1))
            )
            .fluidInputs(
                WerkstoffLoader.AcidicOsmiumSolution.getFluidOrGas(10000),
                Materials.Hydrogen.getGas(1000*6)
            )
            .itemOutputs(
                Materials.Osmium.getDust(1)
            )
            .fluidOutputs(
                Materials.Chlorine.getGas(1000)
            )
            .specialValue(10800)
            .eut(RECIPE_UV)
            .duration(20)
            .addTo(ICD);

        // endregion

        // region Naquadah

        // Naquadah
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Neutronium.getNanite(1)),
                GTUtility.copyAmountUnsafe(480, GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(1128, Materials.Sodium.getDust(1))
            )
            .fluidInputs(Materials.Fluorine.getGas(1000*800))
            .itemOutputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 25),
                GTUtility.copyAmountUnsafe(960, Materials.Gallium.getDust(1)),
                GTUtility.copyAmountUnsafe(384, Materials.Adamantium.getDust(1)),
                GTUtility.copyAmountUnsafe(1210, Materials.Sulfur.getDust(1))
            )
            .fluidOutputs(
                Materials.Naquadah.getMolten(144*960),
                Materials.NaquadahEnriched.getMolten(144*225),
                Materials.Naquadria.getMolten(144*30),
                Materials.Titanium.getMolten(144*480),
                Materials.Hydrogen.getGas(1000*3368),
                Materials.Oxygen.getGas(1000*5816),
                GGMaterial.wasteLiquid.getFluidOrGas(1000*160)
            )
            .specialValue(11700)
            .eut(RECIPE_ZPM)
            .duration(20 * 1425)
            .addTo(ICD);

        // Enriched Naquadah
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Neutronium.getNanite(1)),
                GTUtility.copyAmountUnsafe(220, GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(102, Materials.Sulfur.getDust(1))
            )
            .fluidInputs(Materials.Oxygen.getGas(1000*288))
            .itemOutputs(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 60),
                Materials.Trinium.getDust(10)
            )
            .fluidOutputs(
                Materials.NaquadahEnriched.getMolten(144*180),
                Materials.Naquadria.getMolten(144*24),
                Materials.Hydrogen.getGas(1000*600),
                GGMaterial.wasteLiquid.getFluidOrGas(1000*60)
            )
            .specialValue(11700)
            .eut(RECIPE_ZPM)
            .duration(20 * 228)
            .addTo(ICD);

        // Naquadria
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Neutronium.getNanite(1)),
                GTUtility.copyAmountUnsafe(100, GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(95, Materials.Phosphorus.getDust(1))
            )
            .itemOutputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 50),
                Materials.Barium.getDust(2),
                Materials.Indium.getDust(5),
                GTUtility.copyAmountUnsafe(102, Materials.Sulfur.getDust(1))
            )
            .fluidOutputs(
                Materials.Naquadria.getMolten(144*100),
                Materials.Oxygen.getGas(1000*276)
            )
            .specialValue(11700)
            .eut(RECIPE_ZPM)
            .duration(20 * 100)
            .addTo(ICD);

        // Naquadah
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Gold.getNanite(1)),
                GTUtility.copyAmountUnsafe(480, GGMaterial.naquadahEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(112, Materials.Sodium.getDust(1))
            )
            .fluidInputs(Materials.Fluorine.getGas(1000*800))
            .itemOutputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 25),
                GTUtility.copyAmountUnsafe(960, Materials.Gallium.getDust(1)),
                GTUtility.copyAmountUnsafe(384, Materials.Adamantium.getDust(1)),
                GTUtility.copyAmountUnsafe(1210, Materials.Sulfur.getDust(1))
            )
            .fluidOutputs(
                Materials.Naquadah.getMolten(144*960),
                Materials.NaquadahEnriched.getMolten(144*225),
                Materials.Naquadria.getMolten(144*30),
                Materials.Titanium.getMolten(144*480),
                Materials.Hydrogen.getGas(1000*3368),
                Materials.Oxygen.getGas(1000*5816),
                GGMaterial.wasteLiquid.getFluidOrGas(1000*160)
            )
            .specialValue(12600)
            .eut(RECIPE_UV)
            .duration(2 * 1425)
            .addTo(ICD);

        // Enriched Naquadah
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Gold.getNanite(1)),
                GTUtility.copyAmountUnsafe(220, GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(10, Materials.Sulfur.getDust(1))
            )
            .fluidInputs(Materials.Oxygen.getGas(1000*288))
            .itemOutputs(
                GGMaterial.naquadahEarth.get(OrePrefixes.dust, 60),
                Materials.Trinium.getDust(10)
            )
            .fluidOutputs(
                Materials.NaquadahEnriched.getMolten(144*180),
                Materials.Naquadria.getMolten(144*24),
                Materials.Hydrogen.getGas(1000*600),
                GGMaterial.wasteLiquid.getFluidOrGas(1000*60)
            )
            .specialValue(12600)
            .eut(RECIPE_UV)
            .duration(2 * 228)
            .addTo(ICD);

        // Naquadria
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GTUtility.copyAmountUnsafe(0, Materials.Gold.getNanite(1)),
                GTUtility.copyAmountUnsafe(100, GGMaterial.naquadriaEarth.get(OrePrefixes.dust, 1)),
                GTUtility.copyAmountUnsafe(9, Materials.Phosphorus.getDust(1))
            )
            .itemOutputs(
                GGMaterial.enrichedNaquadahEarth.get(OrePrefixes.dust, 50),
                Materials.Barium.getDust(2),
                Materials.Indium.getDust(5),
                GTUtility.copyAmountUnsafe(102, Materials.Sulfur.getDust(1))
            )
            .fluidOutputs(
                Materials.Naquadria.getMolten(144*100),
                Materials.Oxygen.getGas(1000*276)
            )
            .specialValue(12600)
            .eut(RECIPE_UV)
            .duration(2 * 100)
            .addTo(ICD);

        // Waterline
    GTValue.RA.stdBuilder()
        .itemInputs(
            GTUtlitity.getIntegratedCircuit(19).
            Materials.Eternity.getDust(1))
        .fluidInputs(
            Materials.Water.getFluid(1000*2000000))
        .itemOutputs(
            Material.Hypogen.getDust(12))
        .fluidOutputs(
            Materials.Grade1PurifiedWater.getFluid(1000*200000),
            Materials.Grade2PurifiedWater.getFluid(1000*200000),
            Materials.Grade3PurifiedWater.getFluid(1000*200000),
            Materials.Grade4PurifiedWater.getFluid(1000*200000),
            Materials.Grade5PurifiedWater.getFluid(1000*200000),
            Materials.Grade6PurifiedWater.getFluid(1000*200000),
            Materials.Grade7PurifiedWater.getFluid(1000*200000),
            Materials.Grade8PurifiedWater.getFluid(1000*200000),
            Materials.StabilisedBaryonicMatter.getFluid(1000*200000)
            )
        .specialValue(12600)
        .eut(RECIPE_UXV)
        .duration(2*60)
        .addTo(ICD);
            
        // endregion

    }
    public static void loadRecipePostInit() {
        // region H2O2
        GTValues.RA
            .stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(16))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*128),
                Materials.Oxygen.getGas(1000*128))
            .fluidOutputs(getFluidStack("fluid.hydrogenperoxide",1000*64))
            .specialValue(10800)
            .eut(RECIPE_UHV)
            .duration(32)
            .addTo(ICD);

        // endregion

    }
    // spotless:on
}
