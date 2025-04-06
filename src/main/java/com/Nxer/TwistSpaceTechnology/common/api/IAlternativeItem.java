package com.Nxer.TwistSpaceTechnology.common.api;

import java.util.Collection;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.Nxer.TwistSpaceTechnology.util.rewrites.TST_ItemID;

/**
 * To conveniently check an alternative items list we can make them a special Object to process.
 * 
 * @author Nxer
 */
public interface IAlternativeItem {

    /**
     * @return True if the item stack is a member of this item list.
     */
    default boolean containsItem(ItemStack itemStack) {
        return containsItem(TST_ItemID.create(itemStack)) || containsItem(TST_ItemID.createAsWildcard(itemStack));
    }

    boolean containsItem(TST_ItemID itemID);

    /**
     * @return True if two lists has same member in it.
     */
    boolean hasSameMember(IAlternativeItem another);

    /**
     * @return The name of this item list.
     */
    @NotNull
    String getName();

    static @Nullable IAlternativeItem checkAlternativeItemContaining(Collection<IAlternativeItem> alternativeItems,
        TST_ItemID itemID) {
        for (IAlternativeItem alternativeItem : alternativeItems) {
            if (alternativeItem.containsItem(itemID)) return alternativeItem;
        }
        return null;
    }

    static @Nullable IAlternativeItem checkAlternativeItemContaining(Collection<IAlternativeItem> alternativeItems,
        ItemStack itemStack) {
        for (IAlternativeItem alternativeItem : alternativeItems) {
            if (alternativeItem.containsItem(itemStack)) return alternativeItem;
        }
        return null;
    }

    static @Nullable IAlternativeItem checkAlternativeItemContainingFast(Collection<IAlternativeItem> alternativeItems,
        ItemStack itemStack) {
        return checkAlternativeItemContaining(alternativeItems, TST_ItemID.create(itemStack));
    }
}
