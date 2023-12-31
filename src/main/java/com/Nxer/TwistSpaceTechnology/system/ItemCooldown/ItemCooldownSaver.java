package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public class ItemCooldownSaver {

    private static Map<UUID, Map<Integer, Long>> Cooldown = new HashMap<UUID, Map<Integer, Long>>();

    private static Map<UUID, Boolean> removeServerCheck = new HashMap<UUID, Boolean>();

    public static void init(EntityPlayer player) {
        if (!Cooldown.containsKey(player.getPersistentID())) {
            Cooldown.put(player.getPersistentID(), new HashMap<Integer, Long>());
        }
    }

    public static boolean isUsable(Item item, EntityPlayer player) {

        if (!(item instanceof IItemHasCooldown)) return true;
        if (removeServerCheck.containsKey(player.getPersistentID())) {
            if (removeServerCheck.get(player.getPersistentID())) {
                removeServerCheck.put(player.getPersistentID(), false);
                return true;
            }
        }
        ItemCooldownSaver.init(player);
        long time = player.worldObj.getWorldInfo()
            .getWorldTime();
        TwistSpaceTechnology.LOG.info("Check:" + time);
        if (getPastTime(item, player) >= ((IItemHasCooldown) item).getCooldown()) {
            removeServerCheck.put(player.getPersistentID(), true);
            return true;
        }
        return false;
    }

    public static void onUse(Item item, EntityPlayer player) {
        ItemCooldownSaver.init(player);
        long time = player.worldObj.getWorldInfo()
            .getWorldTime();
        if (item instanceof IItemHasCooldown) {
            TwistSpaceTechnology.LOG.info("Use:" + time);
            Map<Integer, Long> temp = Cooldown.get(player.getPersistentID());
            temp.put(item.hashCode(), time);
            Cooldown.put(player.getPersistentID(), temp);
        }
    }

    /** Returns the number of ticks since the last time used */
    public static int getPastTime(Item item, EntityPlayer player) {
        ItemCooldownSaver.init(player);
        long time = player.worldObj.getWorldInfo()
            .getWorldTime();
        Map<Integer, Long> temp = ItemCooldownSaver.Cooldown.get(player.getPersistentID());
        if (temp.containsKey(item.hashCode())) return (int) (time - temp.get(item.hashCode()));
        else return ((IItemHasCooldown) item).getCooldown();
    }
}
