package com.Nxer.TwistSpaceTechnology.common.machine;

import com.Nxer.TwistSpaceTechnology.config.Config;

/**
 * ValueEnum are stored here.
 */
public final class ValueEnum {

    public static final int MAX_PARALLEL_LIMIT = Config.MAX_PARALLEL_LIMIT; // default 8388608
    public static final int SPACE_ELEVATOR_BASE_CASING_INDEX = 4096;
    public static final int ticksOfMiracleDoorProcessingTime = (int) (20 * Config.secondsOfMiracleDoorProcessingTime);
    public static final int amountOfPhotonsEveryMiracleDoorProcessingCost = Config.amountOfPhotonsEveryMiracleDoorProcessingCost;
    public static final int multiplierOfMiracleDoorEUCost = Config.multiplierOfMiracleDoorEUCost;

}
