package com.Nxer.TwistSpaceTechnology.common.machine;

import static tectech.thing.CustomItemList.astralArrayFabricator;

import net.minecraft.item.ItemStack;

import com.Nxer.TwistSpaceTechnology.common.GTCMItemList;

public class MiscHelper {

    public static ItemStack ASTRAL_ARRAY_FABRICATOR;
    public static ItemStack CRITICAL_PHOTON;

    public static void initStatics() {
        ASTRAL_ARRAY_FABRICATOR = astralArrayFabricator.get(1);
        CRITICAL_PHOTON = GTCMItemList.CriticalPhoton.get(1);
    }
}
