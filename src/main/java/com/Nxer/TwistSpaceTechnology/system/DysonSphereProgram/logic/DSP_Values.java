package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import com.Nxer.TwistSpaceTechnology.config.Config;

public final class DSP_Values {
    // spotless:off

    public static final long EUPerCriticalPhoton = Config.EUPerCriticalPhoton;// default Integer.MAX_VALUE;
    public static final long solarSailPowerPoint = Config.solarSailPowerPoint;// default 524288;
    public static final long solarSailCanHoldPerNode = Config.solarSailCanHoldPerNode;// default 256;
    public static final long solarSailCanHoldDefault = Config.solarSailCanHoldDefault;// default 2048;
    public static final long maxPowerPointPerReceiver = Config.maxPowerPointPerReceiver;// default 1024L * Integer.MAX_VALUE;
    public static final long EUEveryAntimatterFuelRod = Config.EUEveryAntimatterFuelRod;// default 1024L * Integer.MAX_VALUE;
    public static final long EUEveryAntimatter = Config.EUEveryAntimatter;// default 4L * Integer.MAX_VALUE;
    public static final double secondsOfArtificialStarProgressCycleTime = Config.secondsOfArtificialStarProgressCycleTime;// default 6.4
    public static final int secondsOfEverySpaceWarperProvideToOverloadTime = Config.secondsOfEverySpaceWarperProvideToOverloadTime;// default 60*15
    public static final int overloadSpeedUpMultiplier = Config.overloadSpeedUpMultiplier; // default 30
    public static final double gravitationalLensSpeedMultiplier = Config.gravitationalLensSpeedMultiplier;
    public static final int secondsOfEveryGravitationalLensProvideToIntensifyTime = Config.secondsOfEveryGravitationalLensProvideToIntensifyTime;

    // spotless:on
}
