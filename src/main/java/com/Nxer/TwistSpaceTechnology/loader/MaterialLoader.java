package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.common.block.BlockRegister;
import com.Nxer.TwistSpaceTechnology.common.item.items.ItemRegister;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;

import static com.Nxer.TwistSpaceTechnology.common.material.MaterialPool.pool;

/**
 * New Material Pool
 * Used Bartworks Werkstoff system
 *
 */
public class MaterialLoader {

    public static void load() {

        ItemRegister.registry();
        BlockRegister.registry();

        WerkstoffAdderRegistry.addWerkstoffAdder(pool);


    }

}
