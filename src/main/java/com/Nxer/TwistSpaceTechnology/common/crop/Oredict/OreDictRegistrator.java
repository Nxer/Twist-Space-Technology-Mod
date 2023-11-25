package com.Nxer.TwistSpaceTechnology.common.crop.Oredict;

import cpw.mods.fml.common.Loader;

public interface OreDictRegistrator extends Runnable {

    String getModWhichRegisters();

    default void register() {
        if (Loader.isModLoaded(getModWhichRegisters())) run();
    }

}
