package com.Nxer.TwistSpaceTechnology.common.api;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.Nxer.TwistSpaceTechnology.common.init.GTCMItemList;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

public class ModItemsHandler {

    // region Blood Arsenal
    public static Pair<ItemStack, Integer> AmorphicCatalyst;

    // endregion

    public void initStatics() {
        if (Mods.BloodArsenal.isModLoaded()) {
            AmorphicCatalyst = Pair.of(GTModHandler.getModItem(Mods.BloodArsenal.ID, "amorphic_catalyst", 1), 0);
        } else {
            AmorphicCatalyst = Pair.of(GTCMItemList.TestItem0.get(1), 0);
        }
    }
}
