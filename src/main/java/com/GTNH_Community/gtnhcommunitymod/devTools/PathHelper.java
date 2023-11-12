package com.GTNH_Community.gtnhcommunitymod.devTools;

import java.net.URL;

import com.GTNH_Community.gtnhcommunitymod.DistortionSpaceTechnology;

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
            URL tempUrl = DistortionSpaceTechnology.class.getResource("");
            String tempString;
            if (tempUrl != null) {
                /* Cut the String. */
                tempString = tempUrl.getFile()
                    .substring(
                        6,
                        tempUrl.getFile()
                            .indexOf("build"));
                DistortionSpaceTechnology.DevResource = tempString + "src/main/resources";
            }
        }
    }

}
