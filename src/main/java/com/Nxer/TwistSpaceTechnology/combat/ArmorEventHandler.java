package com.Nxer.TwistSpaceTechnology.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ArmorEventHandler {

    public static final ArmorEventHandler INSTANCE = new ArmorEventHandler();

    public void updatePlayerStats(EntityPlayer pl) {
        for (String stat : PlayerExtendedProperties.from(pl).CombatStats.keySet())
            PlayerExtendedProperties.from(pl).CombatStats
                .put(stat, BasicPlayerExtendedProperties.from(pl).CombatStats.get("Basic" + stat));
        final ItemStack stackBoots = pl.inventory.armorItemInSlot(0);
        final ItemStack stackLegs = pl.inventory.armorItemInSlot(1);
        final ItemStack stackBody = pl.inventory.armorItemInSlot(2);
        final ItemStack stackHelmet = pl.inventory.armorItemInSlot(3);
        final ItemStack weildedItem = pl.getHeldItem();

        final Item Boots = stackBoots != null ? stackBoots.getItem() : null;
        final Item Legs = stackLegs != null ? stackLegs.getItem() : null;
        final Item Body = stackBody != null ? stackBody.getItem() : null;
        final Item helmet = stackHelmet != null ? stackHelmet.getItem() : null;
        if (weildedItem != null) {
            if (StatsDefination.ArmorStats.containsKey(weildedItem.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, StatsDefination.ArmorStats.get(weildedItem.getUnlocalizedName()));
            }
        }
        if (stackHelmet != null) {
            if (StatsDefination.ArmorStats.containsKey(stackHelmet.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, StatsDefination.ArmorStats.get(stackHelmet.getUnlocalizedName()));
            } else if (helmet instanceof ItemArmor) {
                PlayerExtendedProperties.addBonusPlayerStat(
                    pl,
                    "Defence",
                    ((ItemArmor) helmet).getArmorMaterial()
                        .getDamageReductionAmount(((ItemArmor) helmet).armorType) * 5);
            }
        }
        if (stackLegs != null) {
            if (StatsDefination.ArmorStats.containsKey(stackLegs.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, StatsDefination.ArmorStats.get(stackLegs.getUnlocalizedName()));
            } else if (Legs instanceof ItemArmor) {
                PlayerExtendedProperties.addBonusPlayerStat(
                    pl,
                    "Defence",
                    ((ItemArmor) Legs).getArmorMaterial()
                        .getDamageReductionAmount(((ItemArmor) Legs).armorType) * 5);
            }
        }
        if (stackBody != null) {
            if (StatsDefination.ArmorStats.containsKey(stackBody.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, StatsDefination.ArmorStats.get(stackBody.getUnlocalizedName()));
            } else if (Body instanceof ItemArmor) {
                PlayerExtendedProperties.addBonusPlayerStat(
                    pl,
                    "Defence",
                    ((ItemArmor) Body).getArmorMaterial()
                        .getDamageReductionAmount(((ItemArmor) Body).armorType) * 5);
            }
        }
        if (stackBoots != null) {
            if (StatsDefination.ArmorStats.containsKey(stackBoots.getUnlocalizedName())) {
                PlayerExtendedProperties
                    .addBonusPlayerStats(pl, StatsDefination.ArmorStats.get(stackBoots.getUnlocalizedName()));
            } else if (Boots instanceof ItemArmor) {
                PlayerExtendedProperties.addBonusPlayerStat(
                    pl,
                    "Defence",
                    ((ItemArmor) Boots).getArmorMaterial()
                        .getDamageReductionAmount(((ItemArmor) Boots).armorType) * 5);
            }
        }
    }
}
