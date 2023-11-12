package com.Nxer.TwistSpaceTechnology.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String preInitSign = "pre init GTNH Community Mod";

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        preInitSign = configuration
            .getString("preInitSign", Configuration.CATEGORY_GENERAL, preInitSign, "GTNH Community Mod preInit Sign");

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
