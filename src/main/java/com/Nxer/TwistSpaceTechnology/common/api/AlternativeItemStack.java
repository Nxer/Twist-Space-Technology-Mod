package com.Nxer.TwistSpaceTechnology.common.api;

public class AlternativeItemStack {

    public final IAlternativeItem alternativeItem;
    public int stackSize;

    public AlternativeItemStack(IAlternativeItem alternativeItem, int stackSize) {
        this.alternativeItem = alternativeItem;
        this.stackSize = stackSize;
    }

    public boolean stackEqual(AlternativeItemStack another) {
        return alternativeItem.equals(another.alternativeItem) && stackSize == another.stackSize;
    }

    public boolean isSameAlternativeItem(AlternativeItemStack another) {
        return alternativeItem.equals(another.alternativeItem);
    }

    public AlternativeItemStack setStackSize(int stackSize) {
        this.stackSize = stackSize;
        return this;
    }
}
