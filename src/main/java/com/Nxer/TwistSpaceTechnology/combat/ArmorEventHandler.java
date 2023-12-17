package com.Nxer.TwistSpaceTechnology.combat;

import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ArmorEventHandler {

    public static final ArmorEventHandler INSTANCE = new ArmorEventHandler();

    public static Map<String, int[]> ArmorStats;

    // Str,Int,CC,CD,Res,BaseDamage,BaseDamageMultipiler,MeleeDamageMultipiler,RangeDamageMultipiler,MagicDamageMultipiler

    public void init() {
        ArmorEventHandler.ArmorStats.put("item.swordGold", new int[] { 50, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorEventHandler.ArmorStats.put("item.swordDiamond", new int[] { 100, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    }

    public void updatePlayerStats(EntityPlayer pl) {
        for (String stat : PlayerExtendedProperties.from(pl).CombatStats.keySet())
            PlayerExtendedProperties.from(pl).CombatStats
                .put(stat, BasicPlayerExtendedProperties.from(pl).CombatStats.get("Basic" + stat));
        final ItemStack stackBoots = pl.inventory.armorItemInSlot(0);
        final ItemStack stackLegs = pl.inventory.armorItemInSlot(1);
        final ItemStack stackBody = pl.inventory.armorItemInSlot(2);
        final ItemStack stackHelmet = pl.inventory.armorItemInSlot(3);
        final ItemStack weildedItem = pl.getHeldItem();

        final Item boots = stackBoots != null ? stackBoots.getItem() : null;
        final Item legs = stackLegs != null ? stackLegs.getItem() : null;
        final Item body = stackBody != null ? stackBody.getItem() : null;
        final Item helmet = stackHelmet != null ? stackHelmet.getItem() : null;
        final Item sniper = weildedItem != null ? weildedItem.getItem() : null;
        if (weildedItem != null) {
            if (ArmorStats.containsKey(weildedItem.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, ArmorEventHandler.ArmorStats.get(weildedItem.getUnlocalizedName()));
            }
        }
    }
}
