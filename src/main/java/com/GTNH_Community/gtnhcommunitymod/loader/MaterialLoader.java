package com.GTNH_Community.gtnhcommunitymod.loader;

import com.GTNH_Community.gtnhcommunitymod.common.block.BlockRegister;
import com.GTNH_Community.gtnhcommunitymod.common.item.items.ItemRegister;
import com.GTNH_Community.gtnhcommunitymod.common.material.MaterialPool;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;

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
