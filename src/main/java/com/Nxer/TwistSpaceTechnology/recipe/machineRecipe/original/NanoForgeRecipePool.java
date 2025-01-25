package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.original;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static gregtech.api.recipe.RecipeMaps.nanoForgeRecipes;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;

public class NanoForgeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTCMItemList.EnergyFluctuationSelfHarmonizer.get(0),
                GTCMItemList.CoreElement.get(0),
                MaterialsTST.Axonium.getBlocks(8),
                GTCMItemList.AnnihilationConstrainer.get(1),
                GTCMItemList.PerfectEngravedEnergyChip.get(4),
                GTCMItemList.InformationHorizonInterventionShell.get(16))
            .fluidInputs(
                Materials.UUMatter.getFluid(2000000),
                MaterialsUEVplus.PhononMedium.getFluid(4000),
                Materials.Infinity.getPlasma(8000))
            .itemOutputs(MaterialsTST.Axonium.getNanite(2))
            .eut(RECIPE_MAX)
            .duration(750 * 20)
            .specialValue(3)
            .addTo(nanoForgeRecipes);
    }
}
