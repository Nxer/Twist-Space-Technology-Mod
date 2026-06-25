package com.Nxer.TwistSpaceTechnology.common.cropsNH;

import com.gtnewhorizon.cropsnh.api.ICropCard;
import com.gtnewhorizon.cropsnh.farming.registries.CropRegistry;

import gregtech.api.enums.Mods;

public class Crops {

    public static ICropCard PurpleMagnolia;

    public static void postInit() {
        if (Mods.Thaumcraft.isModLoaded() && Mods.Botania.isModLoaded()) {
            CropRegistry.instance.register(PurpleMagnolia = new CropPurpleMagnolia());
        }
    }
}
