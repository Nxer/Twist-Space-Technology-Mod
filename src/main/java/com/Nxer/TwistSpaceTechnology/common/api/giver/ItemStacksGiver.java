package com.Nxer.TwistSpaceTechnology.common.api.giver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;
import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

/**
 * Package a factor to create ItemStack arrays from giving pattern conveniently.
 * But without sanity check so this factor may cause data overflow or performance issues.
 *
 * @author Nxer
 */
public class ItemStacksGiver {

    public static ItemStacksGiver create(ItemStack... items) {
        ItemStacksGiver g = new ItemStacksGiver();

        if (items == null || items.length < 1) {
            return g;
        }

        for (ItemStack is : items) {
            if (is == null) {
                TwistSpaceTechnology.LOG.warn("Creating ItemStacksGiver but have null in {} ", (Object[]) items);
                continue;
            }

            if (is.stackSize <= 0) {
                TwistSpaceTechnology.LOG.warn("Creating ItemStacksGiver but have empty at {} ", is.getDisplayName());
                continue;
            }

            g.cache.merge(TST_ItemID.create(is), (long) is.stackSize, Long::sum);
        }

        return g;
    }

    public static ItemStacksGiver create(Collection<ItemStack> items) {
        ItemStacksGiver g = new ItemStacksGiver();

        if (items == null || items.isEmpty()) {
            return g;
        }

        for (ItemStack is : items) {
            if (is == null) {
                TwistSpaceTechnology.LOG.warn("Creating ItemStacksGiver but have null in {} ", items);
                continue;
            }

            if (is.stackSize <= 0) {
                TwistSpaceTechnology.LOG.warn("Creating ItemStacksGiver but have empty at {} ", is.getDisplayName());
                continue;
            }

            g.cache.merge(TST_ItemID.create(is), (long) is.stackSize, Long::sum);
        }

        return g;
    }

    public Map<TST_ItemID, Long> cache = new HashMap<>();

    public ItemStacksGiver() {}

    public boolean isEmpty() {
        return cache == null || cache.isEmpty();
    }

    public void merge(TST_ItemID i, long amount) {
        cache.merge(i, amount, Long::sum);
    }

    public void merge(ItemStack item) {
        merge(TST_ItemID.create(item), item.stackSize);
    }

    public void merge(ItemStacksGiver g, int times) {
        g.cache.forEach((i, l) -> { cache.merge(i, l * times, Long::sum); });
    }

    public void merge(ItemStacksGiver g) {
        g.cache.forEach((i, l) -> { cache.merge(i, l, Long::sum); });
    }

    public List<ItemStack> getAsList(int times) {
        if (times <= 0 || cache.isEmpty()) {
            return new ArrayList<>();
        }

        List<ItemStack> o = new ArrayList<>();

        for (Map.Entry<TST_ItemID, Long> e : cache.entrySet()) {
            long amount = (long) times * e.getValue();

            while (amount > Integer.MAX_VALUE) {
                o.add(
                    e.getKey()
                        .getItemStack(Integer.MAX_VALUE));
                amount -= Integer.MAX_VALUE;
            }

            if (amount > 0) {
                o.add(
                    e.getKey()
                        .getItemStack((int) amount));
            }
        }
        return o;
    }

    public ItemStack[] getAsArray(int times) {
        if (times <= 0 || cache.isEmpty()) {
            return new ItemStack[0];
        }

        return getAsList(times).toArray(new ItemStack[0]);
    }

    public ItemStack[] getAsArray() {
        return getAsArray(1);
    }

    public ItemStack[] toArray() {
        return getAsArray(1);
    }

    public Map<TST_ItemID, Long> getAsMap(int times) {
        if (times <= 0 || cache.isEmpty()) {
            return new HashMap<>();
        }

        Map<TST_ItemID, Long> o = new HashMap<>();
        for (Map.Entry<TST_ItemID, Long> e : cache.entrySet()) {
            o.put(e.getKey(), e.getValue() * times);
        }

        return o;

    }

}
