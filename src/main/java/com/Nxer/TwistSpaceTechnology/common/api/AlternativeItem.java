package com.Nxer.TwistSpaceTechnology.common.api;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

public class AlternativeItem implements IAlternativeItem {

    public final String name;
    private final Set<TST_ItemID> containedItems = new HashSet<>();

    public AlternativeItem(@NotNull String name, ItemStack... itemStacks) {
        this.name = name;
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null) continue;
            containedItems.add(TST_ItemID.create(itemStack));
        }
    }

    @Override
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
    public @NotNull String getName() {
        return name;
    }

    /**
     * Add a new member to containing list. It would be better not to use this method because it will break consistency.
     * 
     * @return True if adding is success.
     */
    public boolean addMember(TST_ItemID itemID) {
        return containedItems.add(itemID);
    }

    /**
     * Add a new member to containing list. It would be better not to use this method because it will break consistency.
     * 
     * @return True if adding is success.
     */
    public boolean addMember(ItemStack itemStack) {
        return addMember(TST_ItemID.create(itemStack));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AlternativeItem that)) {
            return false;
        }
        return Objects.equals(getName(), that.getName()) && Objects.equals(containedItems, that.containedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), containedItems);
    }
}
