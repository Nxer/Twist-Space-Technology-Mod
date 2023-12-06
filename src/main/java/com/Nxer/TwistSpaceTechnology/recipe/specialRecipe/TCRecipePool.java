package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ItemList;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.crafting.InfusionRecipe;

import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.block.ModBlocks;

public class TCRecipePool implements IRecipePool{

    @Override
    public void loadRecipes() {
        /*Elven Workshop */
        ThaumcraftApi.addInfusionCraftingRecipe("BH_ELVEN_WORKSHOP",GTCMItemList.ElvenWorkshop.get(1, 0),
        10, 
        (new AspectList()).merge(Aspect.LIFE, 64).merge(Aspect.EARTH, 64).merge(Aspect.MAGIC, 64).merge(Aspect.MECHANISM, 64),
        new ItemStack(ModBlocks.terraPlate.getItemDropped(0, null, 0))
        ,new ItemStack[]{ItemList.Field_Generator_EV.get(2, null),ItemList.Casing_IV.get(2,null),Materials.Steeleaf.getPlates(2),new ItemStack(ModItems.spawnerMover,2)});
    }
    
}
