package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded;

import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_LV;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.interfaces.IRecipeMap;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.ModItems;

// spotless:off
public class ElvenWorkshopRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final IRecipeMap EW = GTCMRecipe.ElvenWorkshopRecipes;
        //terrastrial recipe
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsBotania.Manasteel.getIngots(1),MaterialsBotania.ManaDiamond.getGems(1),new ItemStack(ModItems.manaResource,1,1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(50000))
            .itemOutputs(MaterialsBotania.Terrasteel.getIngots(1))
            .eut(RECIPE_HV)
            .duration(20 * 1)
            .addTo(EW);

        //mana infusion recipe
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Steel.getIngots(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(300))
            .itemOutputs(MaterialsBotania.Manasteel.getIngots(1))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Steel.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2700))
            .itemOutputs(MaterialsBotania.Manasteel.getBlocks(1))
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thaumium.getIngots(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(150))
            .itemOutputs(MaterialsBotania.Manasteel.getIngots(1))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thaumium.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1350))
            .itemOutputs(MaterialsBotania.Manasteel.getBlocks(1))
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getGems(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(4000))
            .itemOutputs(MaterialsBotania.ManaDiamond.getGems(1))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(36000))
            .itemOutputs(MaterialsBotania.ManaDiamond.getBlocks(1))
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Quartz.getGems(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(100))
            .itemOutputs(new ItemStack(ModItems.quartz,1,1))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass,1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(25))
            .itemOutputs(new ItemStack(ModBlocks.manaGlass,1,0))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.string,1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,16))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.manaResource,4,16))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,22))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.ender_pearl, 1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,1))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.glowstone_dust, 1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(75))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,23))
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);

        //pure daisy recipe
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.stick,16,0))
            .itemOutputs(new ItemStack(ModBlocks.livingwood,16,0))
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone,16))
            .itemOutputs(new ItemStack(ModBlocks.livingrock,16,0))
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.end_stone,16),new ItemStack(Items.glass_bottle, 16))
            .itemOutputs(new ItemStack(ModFluffBlocks.stone,16,1),new ItemStack(ModItems.manaResource,16,15))
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);

    }
}
// spotless:on
