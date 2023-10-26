package com.GTNH_Community.gtnhcommunitymod.loader;

import com.GTNH_Community.gtnhcommunitymod.common.item.items.ItemList01;
import com.GTNH_Community.gtnhcommunitymod.common.material.MaterialPool;
import com.GTNH_Community.gtnhcommunitymod.loader.loadingUtils.AdderRegistry;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;

/**
 * New Material Pool
 * Used Bartworks Werkstoff system
 *
 */
public class MaterialLoader {

    public static void loadMaterial() {

        WerkstoffAdderRegistry.addWerkstoffAdder(new MaterialPool());
        AdderRegistry.addAdder(new ItemList01());

        AdderRegistry.run();
    }

}
