package com.Nxer.TwistSpaceTechnology.recipe.craftRecipe.item;

import com.Nxer.TwistSpaceTechnology.common.item.ItemCardigan;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CardiganRecipes implements IRecipePool {

    @Override
    public void loadRecipes() {
        GTModHandler.addCraftingRecipe(
            ItemCardigan.CardiganULV,
            new Object[]{
                "W W",
                "WWW",
                "WWW",
                'W', new ItemStack(Blocks.wool),
            }
        );

        GTModHandler.addCraftingRecipe(
            ItemCardigan.CardiganLV,
            new Object[]{
                "WBW",
                "WWW",
                "WWW",
                'W', new ItemStack(Blocks.wool),
                'B', ItemList.Battery_RE_LV_Lithium,
            }
        );

        GTModHandler.addCraftingRecipe(
            ItemCardigan.CardiganMV,
            new Object[]{
                "WBW",
                "WWW",
                "WWW",
                'W', new ItemStack(Blocks.wool),
                'B', ItemList.Battery_RE_MV_Lithium,
            }
        );

        GTModHandler.addCraftingRecipe(
            ItemCardigan.CardiganHV,
            new Object[]{
                "WBW",
                "WWW",
                "WWW",
                'W', new ItemStack(Blocks.wool),
                'B', ItemList.Battery_RE_HV_Lithium,
            }
        );
    }
}
