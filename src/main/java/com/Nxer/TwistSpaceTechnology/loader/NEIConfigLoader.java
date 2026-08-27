package com.Nxer.TwistSpaceTechnology.loader;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

import gregtech.api.GregTechAPI;
import gregtech.common.config.Client;

public final class NEIConfigLoader {

    private NEIConfigLoader() {}

    public static void registerPreloadHook() {
        GregTechAPI.sBeforeGTPreload.add(NEIConfigLoader::enableRecipeOwnerDisplay);
    }

    private static void enableRecipeOwnerDisplay() {
        if (!Client.nei.NEIRecipeOwner) {
            Client.nei.NEIRecipeOwner = true;
            Client.save();
            TwistSpaceTechnology.LOG.info("Enabled GregTech NEI recipe owner display.");
        }
    }
}
