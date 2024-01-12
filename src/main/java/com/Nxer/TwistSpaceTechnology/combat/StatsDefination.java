package com.Nxer.TwistSpaceTechnology.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StatsDefination {

    public final static ArrayList<String> AllStats = new ArrayList<String>(
        Arrays.asList(
            "Defence",
            "Strength",
            "Intelligence",
            "CritChance",
            "CritDamage",
            "Resistance",
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
        ArmorStats.put("item.ironwoodSword", new int[] { 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.steeleafSword", new int[] { 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.minotaurAxe", new int[] { 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // MV
        ArmorStats.put("item.darkSteel_sword", new int[] { 20, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.extrautils:ethericsword", new int[] { 20, 0, 0, 50, 20, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.skullfire_sword", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.spectreSword", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ItemSwordThaumium", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.fierySword", new int[] { 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // HV
        ArmorStats.put("item.ItemSwordElemental", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ItemSwordVoid", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.bloodSword", new int[] { 15, 0, 30, 0, 0, 0, 0, 0, 0, 0, 0 });;
        ArmorStats.put("item.knightlySword", new int[] { 25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.iceSword", new int[] { 25, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0 });
        // EV
        ArmorStats.put("item.MorphSword", new int[] { 20, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.DiabolistFork", new int[] { 15, 0, 40, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.endSteel_sword", new int[] { 30, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.terraSword", new int[] { 20, 0, 50, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.BloodRapier", new int[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ItemSwordCrimson", new int[] { 20, 0, 60, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ItemCrystalDagger", new int[] { 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.giantSword", new int[] { 35, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.glassSword", new int[] { 500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // IV
        ArmorStats.put("item.elementiumSword", new int[] { 33, 0, 50, 20, 50, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.starSword", new int[] { 40, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ItemShadowmetalSword", new int[] { 40, 0, 80, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.thunderSword", new int[] { 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("ic2.itemNanoSaber", new int[] { 90, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.stellar_sword", new int[] { 50, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.extrautils:lawSword", new int[] { 45, 0, 0, 50, 100, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ichorSword", new int[] { 45, 0, 20, 50, 60, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.ichorSwordGem", new int[] { 50, 0, 30, 50, 80, 0, 0, 0, 0, 0, 0 });
        // LuV
        ArmorStats.put("item.ItemPrimordialEdge", new int[] { 80, 0, 30, 50, 100, 0, 0, 0, 0, 0, 0 });

        // Bows
        // LV
        ArmorStats.put("item.bow", new int[] { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // MV
        ArmorStats.put("item.ItemBowBone", new int[] { 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // EV
        ArmorStats.put("item.tripleBow", new int[] { 33, 0, 0, 0, 40, 60, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.seekerBow", new int[] { 100, 0, 0, 0, 40, 60, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.iceBow", new int[] { 75, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        // End
        ArmorStats.put("item.infinity_bow", new int[] { 500, 0, 200, 100, 200, 0, 0, 0, 0, 0, 0 });

        // Staff
        // LV
        ArmorStats.put("item.scepterTwilight", new int[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
        ArmorStats.put("item.scepterLifeDrain", new int[] { 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
    }
}
