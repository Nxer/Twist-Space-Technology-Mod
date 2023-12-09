package com.Nxer.TwistSpaceTechnology.devTools;

import java.net.URL;

import com.Nxer.TwistSpaceTechnology.TwistSpaceTechnology;

/**
 * Some Dev Tools about Path.
 */
public class PathHelper {
    // spotless:off

    /**
     * Auto init the workspace 'resources' folder's path.
     */
    public static String initResourceAbsolutePath() {
        /* Get the URL(Path) of the mod when RUN. */
        URL tempUrl = TwistSpaceTechnology.class.getResource("");
        String tempString;
        if (tempUrl != null) {
            /* Cut the String. */
            tempString = tempUrl.getFile().substring(6, tempUrl.getFile().indexOf("build"));
            return tempString + "src/main/resources";
        } else {
            return "";
        }
    }

    // spotless:on
}
