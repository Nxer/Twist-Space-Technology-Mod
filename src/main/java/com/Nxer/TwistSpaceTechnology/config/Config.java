package com.Nxer.TwistSpaceTechnology.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

// spotless:off
public class Config {
    // region Region
    public static final String DSP = "DSP";

    // endregion

//    public static String preInitSign = "pre init GTNH Community Mod";

    // region DSP
    public static long EUPerCriticalPhoton = Integer.MAX_VALUE;
    public static long solarSailPowerPoint = 524288;
    public static long solarSailCanHoldPerNode = 256;
    public static long solarSailCanHoldDefault = 2048;
    public static long maxPowerPointPerReceiver = 1024L * Integer.MAX_VALUE;

    // endregion

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
//        preInitSign = configuration.getString("preInitSign", Configuration.CATEGORY_GENERAL, preInitSign, "GTNH Community Mod preInit Sign");

        EUPerCriticalPhoton = Long.parseLong(configuration.getString("EUPerCriticalPhoton", DSP, String.valueOf(EUPerCriticalPhoton), "EU per Critical Photon Cost."));
        solarSailPowerPoint = Long.parseLong(configuration.getString("solarSailPowerPoint", DSP, String.valueOf(solarSailPowerPoint), "DSP Power Point per Solar Sail can produce."));
        solarSailCanHoldPerNode = Long.parseLong(configuration.getString("solarSailCanHoldPerNode", DSP, String.valueOf(solarSailCanHoldPerNode), "Solar Sail amount per DSP Node can hold."));
        solarSailCanHoldDefault = Long.parseLong(configuration.getString("solarSailCanHoldDefault", DSP, String.valueOf(solarSailCanHoldDefault), "Default Solar Sail amount can hold."));
        maxPowerPointPerReceiver = Long.parseLong(configuration.getString("maxPowerPointPerReceiver", DSP, String.valueOf(maxPowerPointPerReceiver), "Max DSP Power Point per DSP Receiver can request."));






        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
// spotless:on
