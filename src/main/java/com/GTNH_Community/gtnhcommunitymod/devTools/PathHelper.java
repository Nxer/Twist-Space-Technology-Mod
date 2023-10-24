package com.GTNH_Community.gtnhcommunitymod.devTools;

import java.net.URL;

import com.GTNH_Community.gtnhcommunitymod.GTNHCommunityMod;

/**
 * Some Dev Tools about Path.
 */
public class PathHelper {

    /**
     * Auto init the workspace 'resources' folder's path.
     * 
     * @param isInDevMode
     */
    public static void initResourceAbsolutePath(boolean isInDevMode) {
        if (isInDevMode) {
            /* Get the URL(Path) of the mod when RUN. */
            URL tempUrl = GTNHCommunityMod.class.getResource("");
            String tempString;
            if (tempUrl != null) {
                /* Cut the String. */
                tempString = tempUrl.getFile()
                    .substring(
                        6,
                        tempUrl.getFile()
                            .indexOf("build"));
                GTNHCommunityMod.DevResource = tempString + "src/main/resources";
            }
        }
    }

}
