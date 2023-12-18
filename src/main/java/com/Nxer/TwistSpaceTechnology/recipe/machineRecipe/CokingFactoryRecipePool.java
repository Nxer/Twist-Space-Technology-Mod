package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static com.Nxer.TwistSpaceTechnology.util.Utils.copyAmount;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_MV;
import static com.Nxer.TwistSpaceTechnology.util.enums.TierEU.RECIPE_UV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.dreammaster.gthandler.GT_CoreModSupport;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;

public class CokingFactoryRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap coke = GTCMRecipe.CokingFactoryRecipes;

        // Raw Radox
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(24),
                GT_ModHandler.getModItem("GalaxySpace", "barnardaClog", 64))
            .fluidInputs(GT_CoreModSupport.Xenoxene.getFluid(1000))

            .fluidOutputs(GT_CoreModSupport.RawRadox.getFluid(1000 * 2))
            .eut(RECIPE_UV)
            .duration(20 * 180)
            .addTo(coke);

        // generals
        for (ItemStack logWood : OreDictionary.getOres("logWood")) {
            // Charcoal Byproducts
            GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack[] { GT_Utility.getIntegratedCircuit(1), copyAmount(64, logWood) })

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.CharcoalByproducts.getGas(1000 * 32))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Tar
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(2), copyAmount(64, logWood))

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodTar.getFluid(1000 * 12))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Vinegar
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(3), copyAmount(64, logWood))

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodVinegar.getFluid(1000 * 24))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Wood Gas
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(4), copyAmount(64, logWood))

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.WoodGas.getGas(1000 * 12))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Creosote Oil
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(5), copyAmount(64, logWood))

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.Creosote.getFluid(1000 * 32))
                .eut(RECIPE_MV)
                .duration(512)
                .addTo(coke);

            // Heavy Oil
            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.getIntegratedCircuit(6), copyAmount(64, logWood))

                .itemOutputs(Materials.Charcoal.getGems(64), Materials.Charcoal.getGems(64))
                .fluidOutputs(Materials.OilHeavy.getFluid(100 * 16))
                .eut(RECIPE_MV)
                .duration(1024)
                .addTo(coke);
        }
    }
}
