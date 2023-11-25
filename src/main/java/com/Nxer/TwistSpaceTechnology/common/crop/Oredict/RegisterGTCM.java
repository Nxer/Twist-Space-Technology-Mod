package com.Nxer.TwistSpaceTechnology.common.crop.Oredict;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RegisterGTCM {

    public static void register() {
        OreDictionary.registerOre("cropVine", Item.getItemById(106));
        OreDictionary.registerOre("cropVines", Item.getItemById(106));
        OreDictionary.registerOre("cropGrass", new ItemStack(Item.getItemById(32), 1, 0));
        OreDictionary.registerOre("cropGrass", new ItemStack(Item.getItemById(31), 1, 2));
        OreDictionary.registerOre("cropGrass", new ItemStack(Item.getItemById(31), 1, 1));
        OreDictionary.registerOre("cropGrass", new ItemStack(Item.getItemById(175), 1, 2));
        OreDictionary.registerOre("cropGrass", new ItemStack(Item.getItemById(175), 1, 3));
        OreDictionary.registerOre("cropCacti", new ItemStack(Item.getItemById(81), 1, 0));
        OreDictionary.registerOre("cropCactus", new ItemStack(Item.getItemById(81), 1, 0));
    }
}
