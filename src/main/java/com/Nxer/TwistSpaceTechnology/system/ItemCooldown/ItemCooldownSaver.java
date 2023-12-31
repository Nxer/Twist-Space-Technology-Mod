package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import gregtech.api.util.GT_Utility;

public class ItemCooldownSaver {

    private static Map<UUID, Map<Integer, Long>> Cooldown = new HashMap<UUID, Map<Integer, Long>>();

    public static void init(EntityPlayer player) {
        if (!Cooldown.containsKey(player.getPersistentID())) {
            Cooldown.put(player.getPersistentID(), new HashMap<Integer, Long>());
            GT_Utility.sendChatToPlayer(
                player,
                "Init:" + player.worldObj.getWorldInfo()
                    .getWorldTime());
        }
    }

    public static boolean isUsable(Item item, EntityPlayer player) {

        ItemCooldownSaver.init(player);
        long time = player.worldObj.getWorldInfo()
            .getWorldTime();
        if (item instanceof IItemHasCooldown) {
            GT_Utility.sendChatToPlayer(player, "Check:" + time);
            return getPastTime(item, player) >= ((IItemHasCooldown) item).getCooldown();
        }
        return true;
    }

    public static void onUse(Item item, EntityPlayer player) {
        ItemCooldownSaver.init(player);
        long time = player.worldObj.getWorldInfo()
            .getWorldTime();
        if (item instanceof IItemHasCooldown) {
            GT_Utility.sendChatToPlayer(player, "Use:" + time);
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
