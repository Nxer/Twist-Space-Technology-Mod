package com.Nxer.TwistSpaceTechnology.system.ItemCooldown;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;

import gregtech.api.objects.blockupdate.Cooldown;

public class ItemCooldownSaver {

    private static Map<String, Long> Cooldown = new HashMap<String, Long>();

    public static boolean isUsable(Item item) {
        if (item instanceof IItemHasCooldown) return getPastTime(item) == ((IItemHasCooldown) item).getCooldown();
        return true;
    }

    public static void onUse(Item item) {
        if (item instanceof IItemHasCooldown) {
            Cooldown.put(item.getUnlocalizedName(), System.currentTimeMillis());
        }
    }

    /** Returns the number of milliseconds since the last time used */
    public static int getPastTime(Item item) {
        if (ItemCooldownSaver.Cooldown.containsKey(item.getUnlocalizedName())) return Math.min(
            (int) (System.currentTimeMillis() - ItemCooldownSaver.Cooldown.get(item.getUnlocalizedName())),
            ((IItemHasCooldown) item).getCooldown());
        else return ((IItemHasCooldown) item).getCooldown();
    }
}
