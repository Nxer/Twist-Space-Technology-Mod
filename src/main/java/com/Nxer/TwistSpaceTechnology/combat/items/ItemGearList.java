package com.Nxer.TwistSpaceTechnology.combat.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.Utils;

import gregtech.api.util.GTLog;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public enum ItemGearList {

    WoodenSword;

    private boolean mHasNotBeenSet;

    private ItemStack mStack;

    // endregion

    ItemGearList() {
        mHasNotBeenSet = true;
    }

    public Item getItem() {
        sanityCheck();
        if (Utils.isStackInvalid(mStack)) return null;// TODO replace a default issue item
        return mStack.getItem();
    }

    public ItemStack get(int aAmount, Object... aReplacements) {
        sanityCheck();
        // if invalid, return a replacements
        if (Utils.isStackInvalid(mStack)) {
            GTLog.out.println("Object in the ItemList is null at:");
            new NullPointerException().printStackTrace(GTLog.out);
            return GTUtility.copyAmountUnsafe(aAmount, WoodenSword.get(1));
        }
        return GTUtility.copyAmountUnsafe(aAmount, GTOreDictUnificator.get(mStack));
    }

    public ItemGearList set(Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = GTUtility.copyAmountUnsafe(1, aStack);
        return this;
    }

    public ItemGearList set(ItemStack aStack) {
        if (aStack != null) {
            mHasNotBeenSet = false;
            mStack = GTUtility.copyAmountUnsafe(1, aStack);
        }
        return this;
    }

    public boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    private void sanityCheck() {
        if (mHasNotBeenSet)
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
    }
}
