package com.Nxer.TwistSpaceTechnology.common.misc;

import net.minecraft.util.EnumChatFormatting;

import com.Nxer.TwistSpaceTechnology.util.TextEnums;

public enum OverclockType {

    NONE(1, 1),
    NormalOverclock(2, 4),
    LowSpeedPerfectOverclock(2, 2),
    PerfectOverclock(4, 4),
    SingularityPerfectOverclock(8, 4),
    EOHStupidOverclock(2, 8);

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

    // spotless:off
    public String getDescription() {
        // 每次超频耗时除以A, 耗电乘以B
        // #tr OverclockType.Description.01
        // # Every overclock, Time divide by
        // #zh_CN 每次超频时, 耗时除以

        // #tr OverclockType.Description.02
        // # Power Consumption multiply by
        // #zh_CN 耗电乘以
        return TextEnums.tr("OverclockType.Description.01") + " " + EnumChatFormatting.AQUA + timeReduction + EnumChatFormatting.GRAY + " , " + TextEnums.tr("OverclockType.Description.02") + " " + EnumChatFormatting.RED + powerIncrease + EnumChatFormatting.GRAY + " .";
    }
    // spotless:on

    public static OverclockType getFromID(int ID) {
        return OverclockType.values()[ID];
    }

}
