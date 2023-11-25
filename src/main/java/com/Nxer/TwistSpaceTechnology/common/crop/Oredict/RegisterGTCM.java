package com.Nxer.TwistSpaceTechnology.common.crop.Oredict;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RegisterGTCM {

    public static void register() {
        OreDictionary.registerOre("cropPurpleMagnolia", GTCMItemList.PurpleMagnoliaPetal.getItem());
    }
}
