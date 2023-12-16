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
            CombatStats.put("Basic" + name, 0);
        }
        for (String name : StatsDefination.DamageStats) CombatStats.put("Basic" + name, 0);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        for (String stat : StatsDefination.BaseStats) {
            compound.setInteger("Basic" + stat, CombatStats.get(stat));
        }
        for (String stat : StatsDefination.DamageStats) {
            compound.setInteger("Basic" + stat, CombatStats.get(stat));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        for (String stat : StatsDefination.BaseStats) {
            CombatStats.put("Basic" + stat, compound.getInteger(stat));
        }
        for (String stat : StatsDefination.DamageStats) {
            CombatStats.put("Basic" + stat, compound.getInteger(stat));
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
        return from(player).CombatStats.get("Basic" + statName);
    }

    public static void setPlayerStat(EntityPlayer player, String statName, int value) {
        from(player).CombatStats.put("Basic" + statName, value);
    }

    public static void setPlayerStats(EntityPlayer player, int aStrength, int aIntelligence, int aCritChance,
        int aCritDamage, int aResistance, int aBaseDamage, int aBaseDamageMultipiler, int aMeleeDamageMultipiler,
        int aRangeDamageMultipiler, int aMagicDamageMultipiler) {
        from(player).CombatStats.put("BasicStrength", aStrength);
        from(player).CombatStats.put("BasicIntelligence", aIntelligence);
        from(player).CombatStats.put("BasicCritChance", aCritChance);
        from(player).CombatStats.put("BasicCritDamage", aCritDamage);
        from(player).CombatStats.put("BasicResistance", aResistance);
        from(player).CombatStats.put("BasicBaseDamage", aBaseDamage);
        from(player).CombatStats.put("BasicBaseDamageMultipiler", aBaseDamageMultipiler);
        from(player).CombatStats.put("BasicMeleeDamageMultipiler", aMeleeDamageMultipiler);
        from(player).CombatStats.put("BasicRangeDamageMultipiler", aRangeDamageMultipiler);
        from(player).CombatStats.put("BasicMagicDamageMultipiler", aMagicDamageMultipiler);
    }
}
