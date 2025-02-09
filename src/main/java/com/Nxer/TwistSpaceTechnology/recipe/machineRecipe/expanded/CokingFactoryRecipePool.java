package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class CokingFactoryRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap coke = GTCMRecipe.CokingFactoryRecipes;

        // Raw Radox
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24), GTModHandler.getModItem("GalaxySpace", "barnardaClog", 64))
            .fluidInputs(Materials.Xenoxene.getFluid(1000))
            .fluidOutputs(Materials.RawRadox.getFluid(1000 * 2))
            .eut(RECIPE_UV)
            .duration(20 * 120)
            .addTo(coke);

        // generals
        for (ItemStack logWood : OreDictionary.getOres("logWood")) {
            // Charcoal Byproducts
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(1), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.CharcoalByproducts.getGas(1000 * 32))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Tar
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(2), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodTar.getFluid(1000 * 12))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Vinegar
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(3), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodVinegar.getFluid(1000 * 24))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Gas
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(4), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodGas.getGas(1000 * 12))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Creosote Oil
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(5), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.Creosote.getFluid(1000 * 32))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Heavy Oil
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(6), GTUtility.copyAmountUnsafe(64, logWood))
                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.OilHeavy.getFluid(100 * 16))
                .eut(RECIPE_MV)
                .duration(1024)
                .addTo(coke);
        }
    }
}
