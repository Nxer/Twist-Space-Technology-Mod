package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Utility;

public class ChemicalReactorRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap LCR = RecipeMaps.multiblockChemicalReactorRecipes;

        // Lithium Chloride
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2), Materials.Lithium.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(1000))
            .itemOutputs(MyMaterial.lithiumChloride.get(OrePrefixes.dust, 2))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(64)
            .addTo(LCR);

        // One Step K2Cr2O7
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(21),
                Materials.Chrome.getDust(32),
                Materials.Saltpeter.getDust(64),
                Materials.Saltpeter.getDust(64),
                Materials.Saltpeter.getDust(32))
            .fluidInputs(Materials.Oxygen.getGas(96 * 1000), Materials.Water.getFluid(16 * 1000))
            .itemOutputs(
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(48))
            .fluidOutputs(Materials.NitricAcid.getFluid(32 * 1000))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(64)
            .addTo(LCR);

        // Naquadah Emulsion
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(4), Materials.Quicklime.getDust(32))
            .fluidInputs(MyMaterial.acidNaquadahEmulsion.getFluidOrGas(4 * 1000))
            .itemOutputs(Materials.AntimonyTrioxide.getDust(1), WerkstoffLoader.Fluorspar.get(OrePrefixes.dust, 16))
            .fluidOutputs(MyMaterial.naquadahEmulsion.getFluidOrGas(4 * 1000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(240)
            .addTo(LCR);
    }
}
