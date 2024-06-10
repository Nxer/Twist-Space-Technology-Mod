package com.Nxer.TwistSpaceTechnology.compatibility;

import com.dreammaster.lib.Refstrings;

public class GTNHVersion {

    public static final Version version;
    static {
        switch (Refstrings.MODPACKPACK_VERSION) {
            case "2.6.1" -> version = Version.GTNH261;
            case "2.6.0" -> version = Version.GTNH260;
            default -> version = Version.Unknown;
        }
    }

    public enum Version {
        GTNH261,
        GTNH260,
        Unknown;
    }

}
