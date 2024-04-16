package com.Nxer.TwistSpaceTechnology.recipe.commonRecipe;

import static gregtech.api.util.GT_ModHandler.addCraftingRecipe;
import static vazkii.botania.common.item.ModItems.rune;
import static vazkii.botania.common.item.ModItems.spark;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;
import com.Nxer.TwistSpaceTechnology.config.Config;
import com.Nxer.TwistSpaceTechnology.recipe.IRecipePool;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.OreDictNames;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.ALLOY;

public class ShapedCraftRecipePool implements IRecipePool {

    @Override
    public void loadRecipes() {

        // Large Steam Forge Hammer
        addCraftingRecipe(
            GTCMItemList.LargeSteamForgeHammer.get(1),
            new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Casing_BronzePlatedBricks, 'B',
                new ItemStack(Blocks.anvil, 1, 0), 'C', OreDictNames.craftingPiston, 'D',
                ALLOY.TUMBAGA.getFrameBox(1) });

        // Large Steam Alloy Smelter
        addCraftingRecipe(
            GTCMItemList.LargeSteamAlloySmelter.get(1),
            new Object[] { "ABA", "BCB", "ABA", 'A',
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Steel, 1), 'B',
                ItemList.Machine_HP_AlloySmelter.get(1), 'C', ALLOY.TUMBAGA.getFrameBox(1) });

        // Mana Hatch
        addCraftingRecipe(
            GTCMItemList.ManaHatch.get(1),
            new Object[] { "ABA", "ACA", "ADA", 'A',
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsBotania.ElvenElementium, 1), 'B',
                new ItemStack(spark), 'C', ItemList.Hatch_Input_HV.get(1), 'D', new ItemStack(rune, 1, 0) });

        if (Config.Enable_IndustrialMagnetarSeparator) {
            // Industrial Magnetar Separator
            addCraftingRecipe(
                GTCMItemList.IndustrialMagnetarSeparator.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', ALLOY.TALONITE.getPlate(1), 'B',
                    OrePrefixes.circuit.get(Materials.Elite), 'C', ItemList.IV_Coil.get(1), 'D',
                    ItemList.Machine_IV_ElectromagneticSeparator.get(1) });

            // Anti Magnetic Casing
            addCraftingRecipe(
                GTCMItemList.AntiMagneticCasing.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', ALLOY.TALONITE.getPlate(1), 'B',
                    ALLOY.MARAGING300.getLongRod(1), 'C', ALLOY.MARAGING250.getRod(1), 'D',
                    ALLOY.MARAGING300.getFrameBox(1) });
        }
    }
}
