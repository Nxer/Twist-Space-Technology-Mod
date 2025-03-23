package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.init.BlockRegister;
import com.Nxer.TwistSpaceTechnology.common.init.ItemRegister;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import bartworks.API.WerkstoffAdderRegistry;

/**
 * New Material Pool
 * Used Bartworks Werkstoff system
 *
 */
public class MaterialLoader {

    public static void load() {

        ItemRegister.registry();
        BlockRegister.registry();

        WerkstoffAdderRegistry.addWerkstoffAdder(new MaterialPool());
    }

}
