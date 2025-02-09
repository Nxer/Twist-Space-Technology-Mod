package com.Nxer.TwistSpaceTechnology.recipe.machineRecipe.expanded.BOTRecipe;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.OrePrefixes;
import ic2.api.item.IC2Items;
import vazkii.botania.api.BotaniaAPI;

public class BOTRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        BotaniaAPI.elvenTradeRecipes.add(
            new ElvenTradeRecipe(
                GTCMItemList.PurpleMagnoliaSapling.get(1),
                GGMaterial.preciousMetalAlloy.get(OrePrefixes.dust, 1)));
        BotaniaAPI.manaInfusionRecipes.add(
            new ManaInfusionRecipe(MaterialPool.LiquidMana.get(OrePrefixes.cell), IC2Items.getItem("cell"), 10000));
        BotaniaAPI.manaInfusionRecipes.add(
            new ManaInfusionRecipe(IC2Items.getItem("cell"), MaterialPool.LiquidMana.get(OrePrefixes.cell), -10000));
    }
}
