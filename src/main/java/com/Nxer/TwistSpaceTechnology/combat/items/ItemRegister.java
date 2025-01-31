package com.Nxer.TwistSpaceTechnology.combat.items;

import net.minecraft.item.Item.ToolMaterial;

import com.Nxer.TwistSpaceTechnology.client.TstCreativeTabs;
import com.Nxer.TwistSpaceTechnology.combat.items.ItemAdders.Swords.WoodenSword;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegister {

    public static void registry() {
        registryItems();
    }

    private static void registryItems() {
        GameRegistry.registerItem(
            new WoodenSword(ToolMaterial.WOOD, "Wooden Sword", "combatrework.swordWooden", TstCreativeTabs.TabGears),
            "combatrework.swordWooden");
        ItemGearList.WoodenSword.set(
            new WoodenSword(ToolMaterial.WOOD, "Wooden Sword", "combatrework.swordWooden", TstCreativeTabs.TabGears));
    }
}
