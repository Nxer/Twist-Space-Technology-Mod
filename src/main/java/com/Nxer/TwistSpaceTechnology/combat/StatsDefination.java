package com.Nxer.TwistSpaceTechnology.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableMap;

import gregtech.api.util.GT_Utility;

public class StatsDefination {

    public final static String[] BaseStats = {"Defence", "Strength", "Intelligence", "CritChance", "CritDamage", "Resistance" };
    public final static String[] DamageStats = { "BaseDamage", "BaseDamageMultipiler", "MeleeDamageMultipiler",
        "RangeDamageMultipiler", "MagicDamageMultipiler" };
    public final static ArrayList<String> AllStats = new ArrayList<String>(
        Arrays.asList(
            "Defence",
            "Strength",
            "Intelligence",
            "CritChance",
            "CritDamage",
            "Resistance",
            "BaseDamage",
            "BaseDamageMultipiler",
            "MeleeDamageMultipiler",
            "RangeDamageMultipiler",
            "MagicDamageMultipiler"));
    // Str,Int,CC,CD,Res,BaseDamage,BaseDamageMultipiler,MeleeDamageMultipiler,RangeDamageMultipiler,MagicDamageMultipiler
    public static final Map<String, int[]> ArmorStats = ImmutableMap.of(
        "item.swordGold",
        new int[] { 50, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        "item.swordDiamond",
        new int[] { 100, 0, 0, 0, 0, 0, 0, 0, 0, 0 });

    public static void printWeaponStats(ItemStack weapon, EntityPlayer pl) {
        String itemname = weapon.getUnlocalizedName();
        if (weapon.getAttributeModifiers() != null) GT_Utility.sendChatToPlayer(
            pl,
            weapon.getAttributeModifiers()
                .toString());
        int BaseDamage = 0;// weapon.getAttributeModifiers().get("generic.attack_damage")
        if (!ArmorStats.containsKey(itemname)) {
            GT_Utility.sendChatToPlayer(pl, "Damage: +" + BaseDamage);
            return;
        }
        int[] itemstats = ArmorStats.get(itemname);
        GT_Utility.sendChatToPlayer(pl, "Damage: +" + (itemstats[5] + BaseDamage));
        if (itemstats[0] != 0) GT_Utility.sendChatToPlayer(pl, "Strength: +" + itemstats[0]);
        if (itemstats[1] != 0) GT_Utility.sendChatToPlayer(pl, "Intelligence: +" + itemstats[1]);
        if (itemstats[2] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Chance: +" + itemstats[2]);
        if (itemstats[3] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Damage: +" + itemstats[3]);
        if (itemstats[4] != 0) GT_Utility.sendChatToPlayer(pl, "Resistance: +" + itemstats[4]);
    }

    public static void printArmorStats(ItemStack armor, EntityPlayer pl) {
        String itemname = armor.getUnlocalizedName();
        if (armor.getAttributeModifiers() != null) GT_Utility.sendChatToPlayer(
            pl,
            armor.getAttributeModifiers()
                .toString());
        int BaseDefence = 0;
        GT_Utility.sendChatToPlayer(pl, "Defence: +" + BaseDefence);
        if (!ArmorStats.containsKey(itemname)) {
            return;
        }
        int[] itemstats = ArmorStats.get(itemname);
        if (itemstats[0] != 0) GT_Utility.sendChatToPlayer(pl, "Strength: +" + itemstats[0]);
        if (itemstats[1] != 0) GT_Utility.sendChatToPlayer(pl, "Intelligence: +" + itemstats[1]);
        if (itemstats[2] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Chance: +" + itemstats[2]);
        if (itemstats[3] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Damage: +" + itemstats[3]);
        if (itemstats[4] != 0) GT_Utility.sendChatToPlayer(pl, "Resistance: +" + itemstats[4]);
    }

}
