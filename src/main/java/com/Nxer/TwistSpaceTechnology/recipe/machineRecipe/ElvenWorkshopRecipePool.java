package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_ULV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.util.GT_RecipeConstants.OREDICT_INPUT;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.recipeMap.GTCMRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.dreammaster.gthandler.CustomItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;

import cofh.lib.util.helpers.ItemHelper;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Stones;

// spotless:off
public class ElvenWorkshopRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        final GT_Recipe.GT_Recipe_Map EW=GTCMRecipe.instance.ElvenWorkshopRecipes;
        // region Lapotron circuit
        // Shard
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Steel.getIngots(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(300))
            .itemOutputs(MaterialsBotania.Manasteel.getIngots(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Steel.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(2700))
            .itemOutputs(MaterialsBotania.Manasteel.getBlocks(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Thaumium.getIngots(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(150))
            .itemOutputs(MaterialsBotania.Manasteel.getIngots(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Thaumium.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1350))
            .itemOutputs(MaterialsBotania.Manasteel.getBlocks(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getGems(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(4000))
            .itemOutputs(MaterialsBotania.ManaDiamond.getGems(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Diamond.getBlocks(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(36000))
            .itemOutputs(MaterialsBotania.ManaDiamond.getBlocks(1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 9)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Quartz.getGems(1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(100))
            .itemOutputs(new ItemStack(ModItems.quartz,1,1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.glass,1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(25))
            .itemOutputs(new ItemStack(ModBlocks.manaGlass,1,0))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.string,1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,16))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(ModItems.manaResource,4,16))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,22))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.ender_pearl, 1))
            .fluidInputs(MaterialPool.LiquidMana.getFluidOrGas(1500))
            .itemOutputs(new ItemStack(ModItems.manaResource,1,1))
            .noFluidOutputs()
            .eut(RECIPE_LV)
            .duration(20 * 1)
            .addTo(EW);
        for(ItemStack woods : OreDictionary.getOres("logWood")){
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemHelper.cloneStack(woods, 16))
            .noFluidInputs()
            .itemOutputs(new ItemStack(ModBlocks.livingwood,16,0))
            .noFluidOutputs()
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);}
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone,16))
            .noFluidInputs()
            .itemOutputs(new ItemStack(ModBlocks.livingrock,16,0))
            .noFluidOutputs()
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.end_stone,16),new ItemStack(Items.glass_bottle, 16))
            .noFluidInputs()
            .itemOutputs(new ItemStack(GT_Block_Stones.getBlockById(8),16),new ItemStack(ModItems.manaResource,16,15))
            .noFluidOutputs()
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.end_stone,16),new ItemStack(Items.glass_bottle, 16))
            .noFluidInputs()
            .itemOutputs(new ItemStack(GT_Block_Stones.getBlockById(8),16),new ItemStack(ModItems.manaResource,16,15))
            .noFluidOutputs()
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(EW);
        
    }
}
// spotless:on
