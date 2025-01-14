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
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;

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
                GTCMItemList.AnnihilationConstrainer.get(0),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 0, false),
                MaterialsTST.Axonium.getBlocks(8),
                GTCMItemList.InformationHorizonInterventionShell.get(32),
                GTCMItemList.PerfectEngravedEnergyChip.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV,2)
                )
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
