package com.Nxer.TwistSpaceTechnology.recipe.commonRecipe;

import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OreDictNames;
import gtPlusPlus.core.material.ALLOY;

public class ShapedCraftRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {
        //
        addCraftingRecipe(
            GTCMItemList.LargeSteamForgeHammer.get(1),
            new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Casing_BronzePlatedBricks, 'B',
                new ItemStack(Blocks.anvil, 1, 0), 'C', OreDictNames.craftingPiston, 'D',
                ALLOY.TUMBAGA.getFrameBox(1) });

        //
    }
}
