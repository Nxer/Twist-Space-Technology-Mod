package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import gregtech.api.util.OverclockCalculator;

/** Keeps GT's whole overclocks, with a partial final overclock up to the two-second stopping point. */
public final class BottledChaosOverclock {

    private static final int MIN_OVERCLOCK_TICKS = 40;

    public static final class Result {

        private final long eut;
        private final int duration;

        private Result(long eut, int duration) {
            this.eut = eut;
            this.duration = duration;
        }

        public long eut() {
            return eut;
        }

        public int duration() {
            return duration;
        }
    }

    public static Result calculate(double basePower, double workTicks, long availablePower, double timeReduction,
        double powerIncrease) {
        double maximumSpeed = maximumSpeed(basePower, availablePower, timeReduction, powerIncrease);
        if (!Double.isFinite(workTicks) || workTicks <= 0) {
            throw new IllegalArgumentException("Invalid recipe duration");
        }
        double targetSpeed = Math.max(1, workTicks / MIN_OVERCLOCK_TICKS);
        double speed = Math.min(maximumSpeed, targetSpeed);
        double power = Math.ceil(basePower);
        if (timeReduction > 1 && powerIncrease > 1) {
            power *= Math.pow(speed, Math.log(powerIncrease) / Math.log(timeReduction));
        }
        long eut = (long) Math.min(availablePower, Math.ceil(power));
        double ticks = workTicks > MIN_OVERCLOCK_TICKS && maximumSpeed >= targetSpeed ? MIN_OVERCLOCK_TICKS
            : Math.ceil(workTicks / speed);
        return new Result(eut, ticks >= Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) Math.max(1, ticks));
    }

    /** Planning uses the available power; actual consumption is calculated only after integer batches are selected. */
    public static double maximumSpeed(double basePower, long availablePower, double timeReduction,
        double powerIncrease) {
        if (!Double.isFinite(basePower) || basePower <= 0
            || basePower >= Long.MAX_VALUE
            || Math.ceil(basePower) > availablePower) {
            throw new IllegalArgumentException("Invalid recipe power");
        }
        if (!Double.isFinite(timeReduction) || timeReduction < 1
            || !Double.isFinite(powerIncrease)
            || powerIncrease < 1) {
            throw new IllegalArgumentException("Invalid overclock parameters");
        }
        if (timeReduction == 1) return 1;
        if (powerIncrease == 1) return Double.MAX_VALUE;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt((long) Math.ceil(basePower))
            .setEUt(availablePower)
            .setAmperage(1)
            .setAmperageOC(true)
            .setParallel(1)
            .setDuration(1)
            .setDurationDecreasePerOC(timeReduction)
            .setEUtIncreasePerOC(powerIncrease);
        if (powerIncrease > 1) {
            int powerLimitedOverclocks = 0;
            double consumption = Math.ceil(basePower);
            while (consumption <= availablePower / powerIncrease) {
                consumption *= powerIncrease;
                powerLimitedOverclocks++;
            }
            calculator.setMaxOverclocks(powerLimitedOverclocks);
        }
        calculator.setCurrentParallel(1)
            .calculate();
        int overclocks = calculator.getPerformedOverclocks();
        double power = Math.ceil(basePower) * Math.pow(powerIncrease, overclocks);
        // A fractional step also works when there is not enough power for the next complete tier.
        double fraction = Math.max(1, Math.min(powerIncrease, availablePower / power));
        double speed = Math.pow(timeReduction, overclocks)
            * Math.pow(fraction, Math.log(timeReduction) / Math.log(powerIncrease));
        return Double.isFinite(speed) ? speed : Double.MAX_VALUE;
    }
}
