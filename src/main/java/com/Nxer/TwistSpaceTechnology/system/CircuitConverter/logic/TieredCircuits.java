package com.Nxer.TwistSpaceTechnology.system.CircuitConverter.logic;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.util.Utils;
import com.dreammaster.item.ItemList;

public enum TieredCircuits {

    ULV(ItemList.CircuitULV.getIS(), StaticMiscs.CircuitULV),
    LV(ItemList.CircuitLV.getIS(), StaticMiscs.CircuitLV),
    MV(ItemList.CircuitMV.getIS(), StaticMiscs.CircuitMV),
    HV(ItemList.CircuitHV.getIS(), StaticMiscs.CircuitHV),
    EV(ItemList.CircuitEV.getIS(), StaticMiscs.CircuitEV),
    IV(ItemList.CircuitIV.getIS(), StaticMiscs.CircuitIV),
    LuV(ItemList.CircuitLuV.getIS(), StaticMiscs.CircuitLuV),
    ZPM(ItemList.CircuitZPM.getIS(), StaticMiscs.CircuitZPM),
    UV(ItemList.CircuitUV.getIS(), StaticMiscs.CircuitUV),
    UHV(ItemList.CircuitUHV.getIS(), StaticMiscs.CircuitUHV),
    UEV(ItemList.CircuitUEV.getIS(), StaticMiscs.CircuitUEV),
    UIV(ItemList.CircuitUIV.getIS(), StaticMiscs.CircuitUIV),
    UMV(ItemList.CircuitUMV.getIS(), StaticMiscs.CircuitUMV),
    UXV(ItemList.CircuitUXV.getIS(), StaticMiscs.CircuitUXV),
    MAX(ItemList.CircuitMAX.getIS(), StaticMiscs.CircuitMAX);

    private final List<ItemStack> containedCircuits;
    private final ItemStack patternCircuit;

    TieredCircuits(ItemStack patternCircuit, List<ItemStack> containedCircuits) {
        this.patternCircuit = patternCircuit;
        this.containedCircuits = containedCircuits;
    }

    public boolean contains(ItemStack items) {
        for (ItemStack contained : containedCircuits) {
            if (Utils.metaItemEqual(contained, items)) return true;
        }
        return false;
    }

    public ItemStack getPatternCircuit() {
        return patternCircuit.copy();
    }

    public ItemStack getPatternCircuit(int amount) {
        return Utils.setStackSize(patternCircuit.copy(), amount);
    }

}
