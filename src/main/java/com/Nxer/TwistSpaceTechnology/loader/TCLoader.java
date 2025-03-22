package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.system.Thaumcraft.TCBasic;

public class TCLoader {

    public static void preInit() {
        TCBasic.registerAspect();
    }

    public static void postInit() {
        TCBasic.setupItemAspects();
    }
}
