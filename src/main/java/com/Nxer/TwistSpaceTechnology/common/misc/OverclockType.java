package com.Nxer.TwistSpaceTechnology.common.misc;

import java.util.HashMap;
import java.util.Map;

public enum OverclockType {

    NONE(0, 0),
    NormalOverclock(1, 2),
    LowSpeedPerfectOverclock(1, 1),
    PerfectOverclock(2, 2),
    SingularityPerfectOverclock(3, 2),
    EOHStupidOverclock(1, 3),;

    public static final Map<Integer, OverclockType> TYPE_MAP = new HashMap<>();
    public final int ID;
    public final int timeReduction;
    public final int powerIncrease;
    public final double powerIncreaseMultiplierPerOverclock;
    public final boolean perfectOverclock;

    OverclockType(int timeReduction, int powerIncrease) {
        this.timeReduction = timeReduction;
        this.powerIncrease = powerIncrease;
        this.perfectOverclock = timeReduction >= powerIncrease;
        if (timeReduction == powerIncrease) {
            this.powerIncreaseMultiplierPerOverclock = 1;
        } else {
            this.powerIncreaseMultiplierPerOverclock = Math.pow(2, powerIncrease - timeReduction);
        }
        this.ID = this.ordinal();
    }

    public boolean isPerfectOverclock() {
        return perfectOverclock;
    }

    public static OverclockType checkOverclockType(int timeReduction, int powerIncrease) {
        for (OverclockType t : OverclockType.values()) {
            if (t.timeReduction == timeReduction && t.powerIncrease == powerIncrease) {
                return t;
            }
        }
        return NormalOverclock;
    }

    public int getID() {
        return this.ordinal();
    }

    public static OverclockType getFromID(int ID) {
        return OverclockType.values()[ID];
    }

}
