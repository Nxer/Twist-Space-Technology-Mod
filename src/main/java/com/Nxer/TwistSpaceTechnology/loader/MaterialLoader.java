package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.block.BlockRegister;
import com.Nxer.TwistSpaceTechnology.common.item.items.ItemRegister;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;
import com.Nxer.TwistSpaceTechnology.common.material.MaterialsTST;

import bartworks.API.WerkstoffAdderRegistry;

/**
 * New Material Pool
 * Used Bartworks Werkstoff system
 *
 */
public class MaterialLoader {

    public static void load() {

        WerkstoffAdderRegistry.addWerkstoffAdder(new MaterialPool());
        MaterialsTST.init();

        ItemRegister.registry();
        BlockRegister.registry();

    }

}
