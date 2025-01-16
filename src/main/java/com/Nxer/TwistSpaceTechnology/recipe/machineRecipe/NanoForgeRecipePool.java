package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.setStackSize;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MAX;
import static gregtech.api.recipe.RecipeMaps.nanoForgeRecipes;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.recipes.TST_RecipeBuilder;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.recipe.RecipeMaps;

public class NanoForgeRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        TST_RecipeBuilder.builder()
            .itemInputs(
                GTCMItemList.AnnihilationConstrainer.get(0),
                MaterialsUEVplus.TranscendentMetal.getBlocks(64),
                setStackSize(ItemList.Circuit_Wafer_SoC2.get(1), 128))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(144 * 16))
            .itemOutputs(MaterialsUEVplus.TranscendentMetal.getNanite(12))
            .specialValue(3)
            .eut(2000000000)
            .duration(20 * 500)
            .addTo(RecipeMaps.nanoForgeRecipes);

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
                MaterialsUEVplus.PhononMedium.getFluid(16000),
                Materials.Infinity.getPlasma(8000))
            .itemOutputs(MaterialsTST.Axonium.getNanite(2))
            .eut(RECIPE_MAX)
            .duration(750 * 20)
            .specialValue(3)
            .addTo(nanoForgeRecipes);
    }
}
