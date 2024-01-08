package com.Nxer.TwistSpaceTechnology.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.Nxer.TwistSpaceTechnology.combat.items.ICombatGear;

public final class PlayerExtendedProperties implements IExtendedEntityProperties {

    public Map<String, Float> CombatStats = new HashMap<String, Float>();
    private final EntityPlayer player;

    public PlayerExtendedProperties(EntityPlayer pl) {
        for (String name : StatsDefination.AllStats) {
            CombatStats.put(name, 0F);
        }
        player = pl;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound stats = new NBTTagCompound();
        for (String stat : StatsDefination.AllStats) {
            stats.setFloat(stat, CombatStats.get(stat));
        }
        compound.setTag("CombatStats", stats);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound stats = (NBTTagCompound) compound.getTag("CombatStats");
        for (String stat : StatsDefination.AllStats) {
            CombatStats.put(stat, stats.getFloat(stat));
        }
    }

    @Override
    public void init(Entity entity, World world) {}

    public void setPlayerStat(String statName, float value) {
        CombatStats.put(statName, value);
    }

    public List<ItemStack> getGears() {
        List<ItemStack> gears = new ArrayList<ItemStack>();
        gears.add(player.getCurrentArmor(0));
        gears.add(player.getCurrentArmor(1));
        gears.add(player.getCurrentArmor(2));
        gears.add(player.getCurrentArmor(3));
        gears.add(player.getCurrentEquippedItem());
        gears.removeIf(Objects::isNull);
        return gears;
    }

    public float getStrength() {
        float value = CombatStats.get("Strength");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getStrength();
        }
        return value;
    }

    public float getDefence() {
        float value = CombatStats.get("Defence");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getDefence();
        }
        return value;
    }

    public float getIntelligence() {
        float value = CombatStats.get("Intelligence");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getIntelligence();
        }
        return value;
    }

    public float getResistance() {
        float value = CombatStats.get("Resistance");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getResistance();
        }
        return value;
    }

    public float getCritChance() {
        float value = CombatStats.get("CritChance");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getCritChance();
        }
        return value;
    }

    public float getCritDamage() {
        float value = CombatStats.get("CritDamage");
        List<ItemStack> gears = getGears();
        for (ItemStack gear : gears) {
            if (gear.getItem() instanceof ICombatGear) value += ((ICombatGear) gear.getItem()).getCritDamage();
        }
        return value;
    }

    public float getRangeDamageMultipiler() {
        return CombatStats.get("RangeDamageMultipiler");
    }

    public float getMeleeDamageMultipiler() {
        return CombatStats.get("MeleeDamageMultipiler");
    }

    public float getMagicDamageMultipiler() {
        return CombatStats.get("MagicDamageMultipiler");
    }

    public float getBaseDamageMultipiler() {
        return CombatStats.get("BaseDamageMultipiler");
    }

}
