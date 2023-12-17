package com.Nxer.TwistSpaceTechnology.combat;

import java.util.ArrayList;
import java.util.Arrays;

public class StatsDefination {

    public final static String[] BaseStats = { "Strength", "Intelligence", "CritChance", "CritDamage", "Resistance" };
    public final static String[] DamageStats = { "BaseDamage", "BaseDamageMultipiler", "MeleeDamageMultipiler",
        "RangeDamageMultipiler", "MagicDamageMultipiler" };
    public final static ArrayList<String> AllStats = new ArrayList<String>(
        Arrays.asList(
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
    public static ArmorEventHandler ArmorStats;
}
