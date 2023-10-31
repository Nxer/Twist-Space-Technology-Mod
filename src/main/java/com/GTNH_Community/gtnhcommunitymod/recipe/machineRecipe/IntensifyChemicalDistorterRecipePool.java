package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_UV;

import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.recipe.RecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class IntensifyChemicalDistorterRecipePool implements RecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map ICD = GTCMRecipe.instance.IntensifyChemicalDistorterRecipes;

        // PBI
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

        // Silicone
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

        // Polyphenylene sulfide
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

        // Agar
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

        // PTFE
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

    }
}
