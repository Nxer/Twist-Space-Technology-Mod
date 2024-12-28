package com.Nxer.TwistSpaceTechnology.loader;

import static com.Nxer.TwistSpaceTechnology.loader.RecipeLoader.loadRecipesServerStarted;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

public class ServerInitLoader {

    /**
     * Run on client side when player joined a server.
     */
    public static void initOnPlayerJoinedServer() {
        TwistSpaceTechnology.LOG.info("initOnPlayerJoinedServer");
        loadRecipesServerStarted();
    }

}
