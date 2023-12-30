package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import gregtech.api.util.GT_Utility;

public class ItemCooldownSaver {

    private static Map<Integer, Map<String, Long>> Cooldown = new HashMap<Integer, Map<String, Long>>();

    public static void init(EntityPlayer player) {
        if (!Cooldown.containsKey(
            player.getPersistentID()
                .hashCode()))
            Cooldown.put(
                player.getPersistentID()
                    .hashCode(),
                new HashMap<String, Long>());
    }

    public static boolean isUsable(Item item, EntityPlayer player) {

        ItemCooldownSaver.init(player);
        if (item instanceof IItemHasCooldown) {
            GT_Utility.sendChatToPlayer(player, "Check:" + System.currentTimeMillis());
            return getPastTime(item, player) >= ((IItemHasCooldown) item).getCooldown();
        }
        return true;
    }

    public static void onUse(Item item, EntityPlayer player) {
        ItemCooldownSaver.init(player);
        if (item instanceof IItemHasCooldown) {
            GT_Utility.sendChatToPlayer(player, "Use:" + System.currentTimeMillis());
            Map<String, Long> temp = Cooldown.get(
                player.getPersistentID()
                    .hashCode());
            temp.put(item.getUnlocalizedName(), System.currentTimeMillis());
            Cooldown.put(
                player.getPersistentID()
                    .hashCode(),
                temp);
        }
    }

    /** Returns the number of milliseconds since the last time used */
    public static int getPastTime(Item item, EntityPlayer player) {
        ItemCooldownSaver.init(player);
        if (ItemCooldownSaver.Cooldown.get(
            player.getPersistentID()
                .hashCode())
            .containsKey(item.getUnlocalizedName()))
            return (int) (System.currentTimeMillis() - ItemCooldownSaver.Cooldown.get(
                player.getPersistentID()
                    .hashCode())
                .get(item.getUnlocalizedName()));
        else return ((IItemHasCooldown) item).getCooldown();
    }
}
