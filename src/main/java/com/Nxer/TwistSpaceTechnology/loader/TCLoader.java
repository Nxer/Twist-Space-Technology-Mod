package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic;

import gregtech.api.enums.Mods;

public class TCLoader {

    public static final boolean TC_Installed = Mods.Thaumcraft.isModLoaded();

    public static void preInit() {
        if (TC_Installed) {
            TCBasic.registerAspect();
        }
    }

    public static void postInit() {
        if (TC_Installed) {
            TCBasic.setupItemAspects();
        }
    }
}
