package com.Nxer.TwistSpaceTechnology.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_Utility;

public class StatsDefination {

    public final static String[] BaseStats = { "Defence", "Strength", "Intelligence", "CritChance", "CritDamage",
        "Resistance" };
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

    /*
     * boss list
     * LV: TF:Naga,Lich Tinker:Slime King
     * MV: Vailla:Wither TF:Hydra
     * HV: Vailla:Ender Dragon TF:Ghost
     * EV: TF:Snow Queen
     */

    // BaseDamage,Def,Str,Int,CC,CD,Res,BaseDamageMultipiler,MeleeDamageMultipiler,RangeDamageMultipiler,MagicDamageMultipiler
    public static final Map<String, int[]> ArmorStats = new HashMap<String, int[]>();
    static {
        // Swords
        // ULV
        ArmorStats.put("item.swordWood", new int[] { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.swordGold", new int[] { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.swordStone", new int[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.swordIron", new int[] { 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.swordDiamond", new int[] { 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("tile.extrautils:spike_base_wood", new int[] { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("tile.extrautils:spike_base_gold", new int[] { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("tile.extrautils:spike_base", new int[] { 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("tile.extrautils:spike_base_diamond", new int[] { 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // LV
        // MV
        ArmorStats.put("item.darkSteel_sword", new int[] { 20, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.extrautils:ethericsword", new int[] { 20, 0, 0, 50, 20, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.skullfire_sword", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        // HV
        // EV
        ArmorStats.put("item.terraSword", new int[] { 20, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.endSteel_sword", new int[] { 30, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0 });
        // IV
        ArmorStats.put("item.elementiumSword", new int[] { 33, 0, 50, 20, 50, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.starSword", new int[] { 40, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.thunderSword", new int[] { 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.stellar_sword", new int[] { 50, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.extrautils:lawSword", new int[] { 45, 0, 0, 50, 100, 0, 0, 0, 0, 0, 0 });
        // LuV
        // ZPM
        // ArmorStats.put(,new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    }

    public static void printWeaponStats(ItemStack weapon, EntityPlayer pl) {
        String itemname = weapon.getUnlocalizedName();
        if (!ArmorStats.containsKey(itemname)) {
            GT_Utility.sendChatToPlayer(pl, "Damage: +0");
            return;
        }
        int[] itemstats = ArmorStats.get(itemname);
        GT_Utility.sendChatToPlayer(pl, "Damage: +" + itemstats[0]);
        if (itemstats[2] != 0) GT_Utility.sendChatToPlayer(pl, "Strength: +" + itemstats[2]);
        if (itemstats[3] != 0) GT_Utility.sendChatToPlayer(pl, "Intelligence: +" + itemstats[3]);
        if (itemstats[4] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Chance: +" + itemstats[4]);
        if (itemstats[5] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Damage: +" + itemstats[5]);
        if (itemstats[1] != 0) GT_Utility.sendChatToPlayer(pl, "Defence: +" + itemstats[1]);
        if (itemstats[6] != 0) GT_Utility.sendChatToPlayer(pl, "Resistance: +" + itemstats[6]);
    }

    public static void printArmorStats(ItemStack armor, EntityPlayer pl) {
        String itemname = armor.getUnlocalizedName();
        if (!ArmorStats.containsKey(itemname)) {
            GT_Utility.sendChatToPlayer(pl, "Defence: +0");
            return;
        }
        int[] itemstats = ArmorStats.get(itemname);
        GT_Utility.sendChatToPlayer(pl, "Defence: +" + itemstats[1]);
        if (itemstats[2] != 0) GT_Utility.sendChatToPlayer(pl, "Strength: +" + itemstats[2]);
        if (itemstats[3] != 0) GT_Utility.sendChatToPlayer(pl, "Intelligence: +" + itemstats[3]);
        if (itemstats[4] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Chance: +" + itemstats[4]);
        if (itemstats[5] != 0) GT_Utility.sendChatToPlayer(pl, "Crit Damage: +" + itemstats[5]);
        if (itemstats[6] != 0) GT_Utility.sendChatToPlayer(pl, "Resistance: +" + itemstats[6]);
    }

}
