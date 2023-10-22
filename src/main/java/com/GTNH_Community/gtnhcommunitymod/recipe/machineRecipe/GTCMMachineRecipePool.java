package com.GTNH_Community.gtnhcommunitymod.recipe.machineRecipe;

import com.GTNH_Community.gtnhcommunitymod.common.machine.recipeMap.GTCMRecipe;
import com.GTNH_Community.gtnhcommunitymod.common.material.MaterialPool;
import com.GTNH_Community.gtnhcommunitymod.loader.MachineLoader;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GTCMMachineRecipePool {

    public static void loadGTCMMachineRecipes() {

        // test machine recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(1000), Materials.Lava.getFluid(1000))
            .fluidOutputs(MaterialPool.TestingMaterial.getMolten(144))
            .noOptimize()
            .eut(114514)
            .duration(1919 * 20)
            .addTo(GTCMRecipe.instance.IntensifyChemicalDistorterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Machine_Multi_LargeChemicalReactor.get(16),
                ItemList.Emitter_UHV.get(16),
                GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.SolderingAlloy.getSolid(144 * 16))
            .itemOutputs(GT_Utility.copyAmount(1, MachineLoader.IntensifyChemicalDistorter))
            .eut(1919810)
            .duration(114 * 20)
            .addTo(GT_Recipe.GT_Recipe_Map.sAssemblerRecipes);

    }
}
