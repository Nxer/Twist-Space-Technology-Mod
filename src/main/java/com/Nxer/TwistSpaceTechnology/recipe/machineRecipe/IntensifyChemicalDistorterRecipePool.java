package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraftforge.fluids.FluidRegistry;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.MISC_MATERIALS;

public class IntensifyChemicalDistorterRecipePool implements IRecipePool {

    final RecipeMap<?> ICD = GTCMRecipe.IntensifyChemicalDistorterRecipes;

    // spotless:off
    @Override
    public void loadRecipes() {

        TwistSpaceTechnology.LOG.info("IntensifyChemicalDistorterRecipePool loading recipes.");

        // region PBI
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                copyAmount(0, Materials.Potassiumdichromate.getDust(1)),
                Materials.Copper.getDust(16),
                setStackSize(Materials.Zinc.getDust(1), 144))
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
                GT_Utility.getIntegratedCircuit(19),
                Materials.Copper.getDust(16),
                setStackSize(Materials.Zinc.getDust(1), 144))
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
                GT_Utility.getIntegratedCircuit(18),
                copyAmount(0, Materials.Potassiumdichromate.getDust(1)),
                setStackSize(Materials.Carbon.getDust(1), 64*18),
                Materials.Copper.getDust(16),
                setStackSize(Materials.Zinc.getDust(1), 144))
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
        GT_Values.RA
            .stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(6),
                copyAmount(0,Materials.Potassiumdichromate.getDust(1)),
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
                GT_Utility.getIntegratedCircuit(7),
                copyAmount(0,Materials.Potassiumdichromate.getDust(1)),
                setStackSize(Materials.Carbon.getDust(1),64*8))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*6*64),
                Materials.Oxygen.getGas(1000*4*64))
            .fluidOutputs(Materials.PhthalicAcid.getFluid(1000*64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(20)
            .addTo(ICD);

        // region Phosphoric Acid
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(11),
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
                GT_Utility.getIntegratedCircuit(19),
                setStackSize(Materials.Apatite.getDust(1), 64*9))
            .fluidInputs(Materials.Water.getFluid(1000 * 5 * 64))
            .itemOutputs(setStackSize(Materials.Calcium.getDust(1), 64*5))
            .fluidOutputs(
                Materials.PhosphoricAcid.getFluid(1000 * 3 * 64),
                Materials.HydrochloricAcid.getFluid(1000 * 1 * 64))
            .specialValue(4500)
            .eut(RECIPE_HV)
            .duration(20 * 8 * 16)
            .addTo(ICD);
        // endregion

        // region Silicone
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(11),
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
                GT_Utility.getIntegratedCircuit(19),
                Materials.Sulfur.getDust(64),
                setStackSize(Materials.Silicon.getDust(1), 64*3),
                setStackSize(Materials.Carbon.getDust(1), 64*6)
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
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Sulfur.getDust(1))
            .fluidInputs(Materials.Benzene.getFluid(1000))
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(1500), Materials.Hydrogen.getGas(2000))
            .noOptimize()
            .specialValue(400)
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(19), Materials.Sulfur.getDust(64))
            .fluidInputs(Materials.Benzene.getFluid(64000))
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(96000), Materials.Hydrogen.getGas(128000))
            .noOptimize()
            .specialValue(800)
            .eut(RECIPE_HV)
            .duration(128 * 64)
            .addTo(ICD);

        // endregion

        // region Agar
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.MeatRaw.getDust(8))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000),
                Materials.PhosphoricAcid.getFluid(1000),
                Materials.Water.getFluid(8000))
            .itemOutputs(GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 8, 2))
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_IV)
            .duration(256)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                setStackSize(Materials.MeatRaw.getDust(64), 64*6))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000 * 64),
                Materials.PhosphoricAcid.getFluid(64000))
            .itemOutputs(
                setStackSize(
                    GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                    64*8)
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
                GT_Utility.getIntegratedCircuit(23),
                ItemList.Spinneret.get(0),
                setStackSize(Materials.Carbon.getDust(64),64*4+6),
                Materials.Calcium.getDust(2))
            .fluidInputs(
                Materials.Chlorine.getGas(1000 * 34),
                Materials.Hydrogen.getGas(1000 * 230),
                Materials.Oxygen.getGas(1000 * 36),
                Materials.Nitrogen.getGas(1000 * 36))
            .itemOutputs(
                setStackSize(ItemList.WovenKevlar.get(64),64+61))
            .specialValue(11700)
            .eut(RECIPE_UIV)
            .duration(20 * 64)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                Materials.Tin.getDust(45),
                setStackSize(Materials.Carbon.getDust(1),64*71+11),
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
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Carbon.getDust(2 * 9))
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
                GT_Utility.getIntegratedCircuit(19),
                setStackSize(Materials.Carbon.getDust(64), 64*9))
            .fluidInputs(Materials.Fluorine.getGas(4000 * 9 * 32), Materials.Oxygen.getGas(125000 * 32))
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000 * 32))
            .specialValue(1800)
            .eut(RECIPE_IV)
            .duration(20 * 12 * 32)
            .addTo(ICD);

        // endregion

        // region HSbF6
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(10), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 5 * 12), Materials.HydrofluoricAcid.getFluid(1000 * 12))
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(18),
                setStackSize(Materials.Antimony.getDust(64),64*12))
            .fluidInputs(
                Materials.Fluorine.getGas(1000 * 5 * 12 * 64),
                Materials.HydrofluoricAcid.getFluid(1000 * 12 * 64))
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12), Materials.Hydrogen.getGas(1000 * 12))
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                setStackSize(Materials.Antimony.getDust(64),64*12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12 * 64), Materials.Hydrogen.getGas(1000 * 12 * 64))

            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(16 * 64)
            .addTo(ICD);

        // endregion

        // region Epoxid
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3), Materials.Carbon.getDust(21))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24), Materials.Oxygen.getGas(1000 * 4))
            .fluidOutputs(Materials.Epoxid.getMolten(1000))
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                setStackSize(Materials.Carbon.getDust(64), 64*11+52))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .fluidOutputs(Materials.Epoxid.getMolten(1000 * 36))
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                setStackSize(Materials.Carbon.getDust(64), 64*13+49))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .itemOutputs(
                setStackSize(Materials.EpoxidFiberReinforced.getPlates(64),64*3+58))
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        // endregion

        // region Bastnasite
        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                setStackSize(Materials.Carbon.getDust(1), 64*3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 16),
                setStackSize(Materials.Sugar.getDust(1), 64*2)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 768),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                setStackSize(Materials.Cerium.getDust(1), 64+15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                setStackSize(Materials.Silicon.getDust(1), 64*3),
                setStackSize(Materials.Titanium.getDust(1), 64*2+35)
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
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                setStackSize(Materials.Carbon.getDust(1), 64*3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 48),
                setStackSize(Materials.Sugar.getDust(1), 64*2)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 752),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                setStackSize(Materials.Cerium.getDust(1), 64+15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                setStackSize(Materials.Silicon.getDust(1), 64*3),
                setStackSize(Materials.Titanium.getDust(1), 64*2+35)
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
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bastnasite, 64),// Bastnasite
                setStackSize(Materials.Carbon.getDust(1), 64*3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 16),
                setStackSize(Materials.Sugar.getDust(1), 64*2)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 768),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                setStackSize(Materials.Cerium.getDust(1), 64+15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                setStackSize(Materials.Silicon.getDust(1), 64*3),
                setStackSize(Materials.Titanium.getDust(1), 64*2+35)
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
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bastnasite, 64),// Bastnasite
                setStackSize(Materials.Carbon.getDust(1), 64*3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 48),
                setStackSize(Materials.Sugar.getDust(1), 64*2)

            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 752),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260)
            )
            .itemOutputs(
                setStackSize(Materials.Cerium.getDust(1), 64+15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                setStackSize(Materials.Silicon.getDust(1), 64*3),
                setStackSize(Materials.Titanium.getDust(1), 64*2+35)
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
                GT_Utility.getIntegratedCircuit(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 8),
                Materials.InfinityCatalyst.getDust(2)
            )
            .fluidInputs(
                Materials.Tin.getPlasma(1000*18),
                Materials.Bismuth.getPlasma(1000*18),
                FluidRegistry.getFluidStack("cryotheum", 1000 * 4),
                Materials.Neutronium.getMolten(144*16)
            )
            .fluidOutputs(
                MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144*280*2)
            )
            .specialValue(11700)
            .eut(RECIPE_UEV)
            .duration(20*400)
            .addTo(ICD);

        TST_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(9),
                setStackSize(ItemList.Circuit_Chip_Biocell.get(64),64*12),
                setStackSize(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 8),8*12),
                Materials.InfinityCatalyst.getDust(2*12)
            )
            .fluidInputs(
                Materials.Tin.getPlasma(1000*18*12),
                Materials.Bismuth.getPlasma(1000*18*12),
                FluidRegistry.getFluidStack("cryotheum", 1000 * 4 *12),
                Materials.Neutronium.getMolten(144*16*12)
            )
            .fluidOutputs(
                MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144*280*2*16)
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
                GT_Utility.getIntegratedCircuit(15),
                Materials.Carbon.getDust(41)
            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*142),
                Materials.Oxygen.getGas(1000*41)
            )
            .fluidOutputs(
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(1000*10)
            )
            .specialValue(11700)
            .eut(RECIPE_UEV)
            .duration(20*8)
            .addTo(ICD);
        // endregion

    }
    public void loadRecipePostInit() {
        // region H2O2
        GT_Values.RA
            .stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(16))
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
