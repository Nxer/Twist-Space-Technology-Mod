package com.Nxer.TwistSpaceTechnology.system.DysonSphereProgram.logic;

import com.Nxer.TwistSpaceTechnology.config.Config;

public final class DSP_Values {

    public static final long EUPerCriticalPhoton = Config.EUPerCriticalPhoton;// default Integer.MAX_VALUE;
    public static final long solarSailPowerPoint = Config.solarSailPowerPoint;// default 524288;
    public static final long solarSailCanHoldPerNode = Config.solarSailCanHoldPerNode;// default 256;
    public static final long solarSailCanHoldDefault = Config.solarSailCanHoldDefault;// default 2048;
    public static final long maxPowerPointPerReceiver = Config.maxPowerPointPerReceiver;// default 1024L *
                                                                                        // Integer.MAX_VALUE;
    public static final long EUEveryAntimatterFuelRod = 1024L * Integer.MAX_VALUE;// default 1024L * Integer.MAX_VALUE;
    public static final long EUEveryAntimatter = 4L * Integer.MAX_VALUE;// default 1024L * Integer.MAX_VALUE;
    public static final double secondsOfArtificialStarProgressCycleTime = 6.4;

}
