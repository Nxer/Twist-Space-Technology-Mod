package com.Nxer.TwistSpaceTechnology.common.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

public class OreDictItem implements IAlternativeItem {

    public static final Map<String, OreDictItem> UsedOreDictItems = new HashMap<>();

    public static OreDictItem getOreDictItems(String oreDictName) {
        if (UsedOreDictItems.containsKey(oreDictName)) return UsedOreDictItems.get(oreDictName);

        OreDictItem o = new OreDictItem(oreDictName);
        UsedOreDictItems.put(oreDictName, o);
        return o;
    }

    public final String oreDictName;
    private final Set<TST_ItemID> containedItems = new HashSet<>();

    public OreDictItem(@NotNull String oreDictName) {
        this.oreDictName = oreDictName;
        for (ItemStack itemStack : OreDictionary.getOres(oreDictName)) {
            if (itemStack == null) continue;
            containedItems.add(TST_ItemID.createNoNBT(itemStack));
        }

    }

    @Override
    public boolean containsItem(ItemStack itemStack) {
        return containedItems.contains(TST_ItemID.createNoNBT(itemStack))
            || containedItems.contains(TST_ItemID.createAsWildcard(itemStack));
    }

    @Override
    public @NotNull String getName() {
        return oreDictName;
    }

    public boolean containsItem(TST_ItemID itemID) {
        return containedItems.contains(itemID);
    }

    @Override
    public boolean hasSameMember(IAlternativeItem another) {
        for (TST_ItemID itemID : containedItems) {
            if (another.containsItem(itemID)) return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OreDictItem that)) {
            return false;
        }
        return Objects.equals(oreDictName, that.oreDictName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oreDictName);
    }

}
