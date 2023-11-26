package com.Nxer.TwistSpaceTechnology.recipe.specialRecipe;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.MyMaterial;
import gregtech.api.enums.OrePrefixes;
import ic2.api.item.IC2Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.BOTRecipe.ElvenTradeRecipe;
import com.Nxer.TwistSpaceTechnology.recipe.specialRecipe.BOTRecipe.ManaInfusionRecipe;
public class BOTRecipePool implements IRecipePool{

    @Override
    public void loadRecipes() {
        BotaniaAPI.elvenTradeRecipes.add(new ElvenTradeRecipe(GTCMItemList.PurpleMagnoliaSapling.get(1),MyMaterial.preciousMetalAlloy.get(OrePrefixes.dust, 1)));
        BotaniaAPI.manaInfusionRecipes.add(new ManaInfusionRecipe(MaterialPool.LiquidMana.get(OrePrefixes.cell),IC2Items.getItem("cell") , 1000));
        BotaniaAPI.manaInfusionRecipes.add(new ManaInfusionRecipe(IC2Items.getItem("cell") ,MaterialPool.LiquidMana.get(OrePrefixes.cell), -1000));
    }
}