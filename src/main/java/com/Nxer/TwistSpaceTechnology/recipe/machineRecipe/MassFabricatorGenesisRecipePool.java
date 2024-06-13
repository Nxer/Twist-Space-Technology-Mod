package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;

public class MassFabricatorGenesisRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidOutputs(Materials.UUMatter.getFluid(1000))
            .eut(200000)
            .duration(1000)
            .addTo(GTCMRecipe.MassFabricatorGenesis);
    }
}
