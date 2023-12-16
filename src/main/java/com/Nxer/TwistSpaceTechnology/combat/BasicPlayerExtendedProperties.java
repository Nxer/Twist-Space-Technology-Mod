package com.Nxer.TwistSpaceTechnology.combat;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public final class BasicPlayerExtendedProperties implements IExtendedEntityProperties {

    public static final BasicPlayerExtendedProperties instance = new BasicPlayerExtendedProperties();
    public Map<String, Integer> CombatStats = new HashMap<String, Integer>();

    public BasicPlayerExtendedProperties() {
        for (String name : StatsDefination.BaseStats) {
            CombatStats.put(name, 0);
        }
        for (String name : StatsDefination.DamageStats) CombatStats.put(name, 0);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        for (String stat : CombatStats.keySet()) {
            compound.setInteger(stat, CombatStats.get(stat));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        for (String stat : StatsDefination.BaseStats) {
            CombatStats.put(stat, compound.getInteger(stat));
        }
        for (String stat : StatsDefination.DamageStats) {
            CombatStats.put(stat, compound.getInteger(stat));
        }
    }

    @Override
    public void init(Entity entity, World world) {

    }

    public static BasicPlayerExtendedProperties from(EntityPlayer player) {
        return ((BasicPlayerExtendedProperties) player.getExtendedProperties("BASIC_COMBAT_STATS"));
    }

    public static Map<String, Integer> getStats(EntityPlayer player) {
        return from(player).CombatStats;
    }

    public static Integer getStat(EntityPlayer player, String statName) {
        return from(player).CombatStats.get(statName);
    }

    public static void setPlayerStat(EntityPlayer player, String statName, int value) {
        from(player).CombatStats.put(statName, value);
    }

    public static void setPlayerStats(EntityPlayer player, int aStrength, int aIntelligence, int aCritChance,
        int aCritDamage, int aResistance, int aBaseDamage, int aBaseDamageMultipiler, int aMeleeDamageMultipiler,
        int aRangeDamageMultipiler, int aMagicDamageMultipiler) {
        from(player).CombatStats.put("Strength", aStrength);
        from(player).CombatStats.put("Intelligence", aIntelligence);
        from(player).CombatStats.put("CritChance", aCritChance);
        from(player).CombatStats.put("CritDamage", aCritDamage);
        from(player).CombatStats.put("Resistance", aResistance);
        from(player).CombatStats.put("BaseDamage", aBaseDamage);
        from(player).CombatStats.put("BaseDamageMultipiler", aBaseDamageMultipiler);
        from(player).CombatStats.put("MeleeDamageMultipiler", aMeleeDamageMultipiler);
        from(player).CombatStats.put("RangeDamageMultipiler", aRangeDamageMultipiler);
        from(player).CombatStats.put("MagicDamageMultipiler", aMagicDamageMultipiler);
    }
}
