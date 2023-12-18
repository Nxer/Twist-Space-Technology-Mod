package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_IV;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.util.GT_Utility;
import vazkii.botania.common.item.ModItems;

// spotless:off
public class RuneEngraverRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap RE=GTCMRecipe.RuneEngraverRecipes;

        //region basic rune

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.StainlessSteel.getPlates(1),Materials.InfusedWater.getGems(4),new ItemStack(ModItems.manaResource,1,23),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2000))
            .itemOutputs(new ItemStack(ModItems.rune,8,0))

            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.StainlessSteel.getPlates(1),Materials.InfusedFire.getGems(4),new ItemStack(ModItems.manaResource,1,23),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2000))
            .itemOutputs(new ItemStack(ModItems.rune,8,1))

            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.StainlessSteel.getPlates(1),Materials.InfusedEarth.getGems(4),new ItemStack(ModItems.manaResource,1,23),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2000))
            .itemOutputs(new ItemStack(ModItems.rune,8,2))

            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.StainlessSteel.getPlates(1),Materials.InfusedAir.getGems(4),new ItemStack(ModItems.manaResource,1,23),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2000))
            .itemOutputs(new ItemStack(ModItems.rune,8,3))

            .eut(RECIPE_EV)
            .duration(20 * 10)
            .addTo(RE);

        //end region

        //region advanced rune

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SterlingSilver.getPlates(1),new ItemStack(ModItems.rune,1,0),new ItemStack(ModItems.rune,1,1),Materials.Olivine.getGems(1),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.rune,4,4))

            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SterlingSilver.getPlates(1),new ItemStack(ModItems.rune,1,2),new ItemStack(ModItems.rune,1,3),Materials.Jasper.getGems(1),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.rune,4,5))

            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SterlingSilver.getPlates(1),new ItemStack(ModItems.rune,1,1),new ItemStack(ModItems.rune,1,3),Materials.Topaz.getGems(1),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.rune,4,6))

            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SterlingSilver.getPlates(1),new ItemStack(ModItems.rune,1,0),new ItemStack(ModItems.rune,1,2),Materials.Amethyst.getGems(1),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.rune,4,7))

            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RE);

        //end region

        //region noble rune

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SterlingSilver.getPlates(4),MaterialsBotania.Manasteel.getPlates(4),Materials.Thaumium.getPlates(4),new ItemStack(ModItems.manaResource,4,1),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,6,8))

            .eut(RECIPE_EV)
            .duration(20 * 20)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,3),new ItemStack(ModItems.rune,1,5),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,9))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,1),new ItemStack(ModItems.rune,1,7),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,10))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(0),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,0),new ItemStack(ModItems.rune,1,4),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,11))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(3),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,3),new ItemStack(ModItems.rune,1,6),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,12))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(2),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,2),new ItemStack(ModItems.rune,1,7),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,13))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(0),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,0),new ItemStack(ModItems.rune,1,7),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,14))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1),Materials.Titanium.getPlates(2),new ItemStack(ModItems.rune,1,1),new ItemStack(ModItems.rune,1,5),MaterialsBotania.ManaDiamond.getGems(4),MaterialsBotania.Livingrock.getPlates(4))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(3000))
            .itemOutputs(new ItemStack(ModItems.rune,3,15))

            .eut(RECIPE_IV)
            .duration(20 * 10)
            .addTo(RE);

        //end region

        //region legendary rune

//        GT_Values.RA.stdBuilder();

        //end region
    }
}
// spotless:on
