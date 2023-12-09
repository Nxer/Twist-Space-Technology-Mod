package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class IntensifyChemicalDistorterRecipePool implements IRecipePool {

    final GT_Recipe.GT_Recipe_Map ICD = GTCMRecipe.instance.IntensifyChemicalDistorterRecipes;

    // spotless:off
    @Override
    public void loadRecipes() {

        TwistSpaceTechnology.LOG.info("IntensifyChemicalDistorterRecipePool loading recipes.");

        // region PBI
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.Potassiumdichromate.getDust(16),
                Materials.Copper.getDust(16),
                Materials.Zinc.getDust(64),
                Materials.Zinc.getDust(64),
                Materials.Zinc.getDust(16))
            .fluidInputs(
                Materials.Dimethylbenzene.getFluid(144000),
                Materials.Chlorobenzene.getFluid(288000),
                Materials.SulfuricAcid.getFluid(144000),
                Materials.Hydrogen.getGas(1728000),
                Materials.Nitrogen.getGas(576000),
                Materials.Oxygen.getGas(2016000))
            .noItemOutputs()
            .fluidOutputs(Materials.Polybenzimidazole.getMolten(144000), Materials.HydrochloricAcid.getFluid(288000))
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_UV)
            .duration(96)
            .addTo(ICD);

        // endregion

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
                Materials.HydrochloricAcid.getFluid(1000 * 1))
            .specialValue(3600)
            .eut(RECIPE_MV)
            .duration(20 * 8)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64),
                Materials.Apatite.getDust(64))
            .fluidInputs(
                Materials.Water.getFluid(1000 * 5 * 64))
            .itemOutputs(
                Materials.Calcium.getDust(64),
                Materials.Calcium.getDust(64),
                Materials.Calcium.getDust(64),
                Materials.Calcium.getDust(64),
                Materials.Calcium.getDust(64))
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
            .noItemOutputs()
            .fluidOutputs(Materials.Silicone.getMolten(1296))
            .noOptimize()
            .specialValue(400)
            .eut(96)
            .duration(128)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.Sulfur.getDust(64),
                Materials.Silicon.getDust(64),
                Materials.Silicon.getDust(64),
                Materials.Silicon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64))
            .fluidInputs(Materials.Hydrogen.getGas(12000 * 64), Materials.Water.getFluid(3000 * 64))
            .noItemOutputs()
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
            .noItemOutputs()
            .fluidOutputs(Materials.PolyphenyleneSulfide.getMolten(1500), Materials.Hydrogen.getGas(2000))
            .noOptimize()
            .specialValue(400)
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(19), Materials.Sulfur.getDust(64))
            .fluidInputs(Materials.Benzene.getFluid(64000))
            .noItemOutputs()
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
            .noFluidOutputs()
            .noOptimize()
            .specialValue(9900)
            .eut(RECIPE_IV)
            .duration(256)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.MeatRaw.getDust(64),
                Materials.MeatRaw.getDust(64),
                Materials.MeatRaw.getDust(64),
                Materials.MeatRaw.getDust(64),
                Materials.MeatRaw.getDust(64),
                Materials.MeatRaw.getDust(64))
            .fluidInputs(
                Materials.SulfuricAcid.getFluid(4000 * 64),
                Materials.PhosphoricAcid.getFluid(64000),
                Materials.Water.getFluid(8000 * 64))
            .itemOutputs(
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2),
                GT_ModHandler.getModItem("dreamcraft", "GTNHBioItems", 64, 2))
            .noFluidOutputs()
            .noOptimize()
            .specialValue(10800)
            .eut(RECIPE_IV)
            .duration(256 * 48)
            .addTo(ICD);
        // endregion

        // region Kevlar
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(23),
                ItemList.Spinneret.get(0),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(6),
                Materials.Calcium.getDust(2))
            .fluidInputs(
                Materials.Chlorine.getGas(1000 * 34),
                Materials.Hydrogen.getGas(1000 * 230),
                Materials.Oxygen.getGas(1000 * 36),
                Materials.Nitrogen.getGas(1000 * 36))
            .itemOutputs(
                ItemList.WovenKevlar.get(64),
                ItemList.WovenKevlar.get(61))
            .noFluidOutputs()
            .noOptimize()
            .specialValue(11700)
            .eut(RECIPE_UIV)
            .duration(20 * 64)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(22),
                Materials.Tin.getDust(45),
                Materials.Carbon.getDust(11),
                Materials.Diamond.getDust(64),
                Materials.Diamond.getDust(7),
                Materials.Nickel.getDust(5),
                Materials.Palladium.getDust(10),
                Materials.Iron.getDust(5),
                Materials.Silicon.getDust(36))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 1964),
                Materials.Hydrogen.getGas(1000 * 5292),
                Materials.Chlorine.getGas(1000 * 87),
                Materials.Nitrogen.getGas(1000 * 450))
            .noItemOutputs()
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
            .noItemOutputs()
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000))
            .noOptimize()
            .specialValue(1800)
            .eut(RECIPE_IV)
            .duration(20 * 12)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64))
            .fluidInputs(Materials.Fluorine.getGas(4000 * 9 * 32), Materials.Oxygen.getGas(125000 * 32))
            .noItemOutputs()
            .fluidOutputs(Materials.Polytetrafluoroethylene.getMolten(36000 * 32))
            .noOptimize()
            .specialValue(1800)
            .eut(RECIPE_IV)
            .duration(20 * 12 * 32)
            .addTo(ICD);

        // endregion

        // region HSbF6
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(10), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 5 * 12), Materials.HydrofluoricAcid.getFluid(1000 * 12))
            .noItemOutputs()
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(64)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(18),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64))
            .fluidInputs(
                Materials.Fluorine.getGas(1000 * 5 * 12 * 64),
                Materials.HydrofluoricAcid.getFluid(1000 * 12 * 64))
            .noItemOutputs()
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(64 * 64)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(11), Materials.Antimony.getDust(12))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12), Materials.Hydrogen.getGas(1000 * 12))
            .noItemOutputs()
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(64)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(19),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64),
                Materials.Antimony.getDust(64))
            .fluidInputs(Materials.Fluorine.getGas(1000 * 6 * 12 * 64), Materials.Hydrogen.getGas(1000 * 12 * 64))
            .noItemOutputs()
            .fluidOutputs(MyMaterial.fluoroantimonicAcid.getFluidOrGas(1000 * 12 * 64))
            .specialValue(9900)
            .eut(RECIPE_UHV)
            .duration(64 * 64)
            .addTo(ICD);

        // endregion

        // region Epoxid
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3), Materials.Carbon.getDust(21))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24), Materials.Oxygen.getGas(1000 * 4))
            .noItemOutputs()
            .fluidOutputs(Materials.Epoxid.getMolten(1000))
            .eut(RECIPE_HV)
            .duration(128)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(52))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .noItemOutputs()
            .fluidOutputs(Materials.Epoxid.getMolten(1000 * 36))
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(64),
                Materials.Carbon.getDust(49))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 24 * 36), Materials.Oxygen.getGas(1000 * 4 * 36))
            .itemOutputs(
                Materials.EpoxidFiberReinforced.getPlates(64),
                Materials.EpoxidFiberReinforced.getPlates(64),
                Materials.EpoxidFiberReinforced.getPlates(64),
                Materials.EpoxidFiberReinforced.getPlates(58))
            .noFluidOutputs()
            .specialValue(5400)
            .eut(RECIPE_HV)
            .duration(128 * 36)
            .addTo(ICD);

        // endregion

        // region Bastnasite
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), // Carbon
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), // Carbon

                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 16),

                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 64)

            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 768),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260),
                Materials.Fluorine.getGas(1000 * 75)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 35)
            )
            .fluidOutputs(
                Materials.Oxygen.getGas(1000 * 150)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(3),
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 64),// Bastnasite
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), // Carbon
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64), // Carbon

                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 59),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 48),

                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sugar, 64)

            )
            .fluidInputs(
                Materials.Hydrogen.getGas(1000 * 752),
                Materials.Nitrogen.getGas(1000 * 210),
                Materials.Chlorine.getGas(1000 * 260),
                Materials.Fluorine.getGas(1000 * 75)
            )
            .itemOutputs(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 15),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Neodymium, 42),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lanthanum, 26),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Holmium, 17),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Samarium, 11),
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.dust, 11),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Gadolinium, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Terbium, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 64),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 35)
            )
            .fluidOutputs(
                Materials.Oxygen.getGas(1000 * 166)
            )
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(20 * 20)
            .addTo(ICD);

        // endregion

    }
    public void loadRecipePostInit() {
        // region H2O2
        GT_Values.RA
            .stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(16))
            .fluidInputs(
                Materials.Hydrogen.getGas(1000*64),
                Materials.Oxygen.getGas(1000*64))
            .noItemOutputs()
            .fluidOutputs(getFluidStack("fluid.hydrogenperoxide",1000*32))
            .specialValue(11700)
            .eut(RECIPE_UHV)
            .duration(64)
            .addTo(ICD);

        // endregion

    }
    // spotless:on
}
