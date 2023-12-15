package com.Nxer.TwistSpaceTechnology.combat;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public final class PlayerExtendedProperties implements IExtendedEntityProperties {

    public static final PlayerExtendedProperties instance = new PlayerExtendedProperties();
    public Map<String, Integer> CombatStats = new HashMap<String, Integer>();

    public PlayerExtendedProperties() {
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

    public static PlayerExtendedProperties from(EntityPlayer player) {
        return ((PlayerExtendedProperties) player.getExtendedProperties("COMBAT_STATS"));
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
}
