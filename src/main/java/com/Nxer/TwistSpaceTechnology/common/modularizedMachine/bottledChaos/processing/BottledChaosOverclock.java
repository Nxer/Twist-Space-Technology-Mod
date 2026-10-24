package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

/** Shared source normalization and final execution overclocks. Only final execution has a two-second stop. */
public final class BottledChaosOverclock {

    public static final int MIN_OVERCLOCK_TICKS = 40;

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

    /** Normalize original source work once; stochastic limits average costs rather than overclock counts. */
    public static double calculateNormalizedDuration(long duration, long recipeEUt, long targetEUt, long parallelLimit,
        double speedMultiplier, double euMultiplier, double timeDivisor, double powerMultiplier, int maximumOverclocks,
        int perfectOverclocks, int[] overclockLimitAlternatives) {
        if (duration <= 0 || recipeEUt <= 0
            || targetEUt <= 0
            || parallelLimit <= 0
            || !Double.isFinite(speedMultiplier)
            || speedMultiplier <= 0
            || !Double.isFinite(euMultiplier)
            || euMultiplier <= 0
            || maximumOverclocks < -1
            || perfectOverclocks < 0) {
            throw new IllegalArgumentException("Invalid source overclock parameters");
        }
        validateMultipliers(timeDivisor, powerMultiplier);
        long parallelTier = 1;
        int parallelOverclocks = 0;
        while (parallelTier <= parallelLimit / 4) {
            parallelTier *= 4;
            parallelOverclocks++;
        }
        // User-defined approximation: 12 parallel grants one 4/4 step and a 12/16 power multiplier.
        double parallelPower = parallelLimit == parallelTier ? 1 : parallelLimit / (parallelTier * 4.0);
        double modifiedPower = recipeEUt * euMultiplier * parallelPower;
        double work = duration / speedMultiplier;
        if (!Double.isFinite(modifiedPower) || modifiedPower <= 0 || modifiedPower >= Long.MAX_VALUE)
            throw new ArithmeticException("Recipe power overflow");
        if (!Double.isFinite(work) || work <= 0) throw new ArithmeticException("Duration overflow");
        // Modes explicitly disabling overclocks keep their fixed source speed, including at 1/1.
        if (powerMultiplier == 1) return work;
        long basePower = (long) Math.ceil(modifiedPower);
        while (basePower > targetEUt) {
            basePower = (long) Math.ceil(basePower / powerMultiplier);
            work *= timeDivisor;
            if (!Double.isFinite(work)) throw new ArithmeticException("Duration overflow");
        }
        // Parallel capacity adds stages independently of the source machine's native overclock limit.
        // Keep native 4/4 or stronger steps; 2/2 machines still need the parallel capacity's 4/4 steps.
        long finitePerfect = timeDivisor >= powerMultiplier && timeDivisor >= 4 ? 0
            : (long) perfectOverclocks + parallelOverclocks;
        double normalized;
        if (overclockLimitAlternatives == null || overclockLimitAlternatives.length == 0) {
            normalized = work / overclockSpeed(
                basePower,
                targetEUt,
                timeDivisor,
                powerMultiplier,
                maximumOverclocks < 0 ? -1 : (long) maximumOverclocks + parallelOverclocks,
                finitePerfect,
                false);
        } else {
            normalized = 0;
            for (int limit : overclockLimitAlternatives) {
                if (limit < -1) throw new IllegalArgumentException("Invalid source overclock limit");
                normalized += work
                    / overclockSpeed(
                        basePower,
                        targetEUt,
                        timeDivisor,
                        powerMultiplier,
                        limit < 0 ? -1 : (long) limit + parallelOverclocks,
                        finitePerfect,
                        false)
                    / overclockLimitAlternatives.length;
            }
        }
        if (!Double.isFinite(normalized) || normalized <= 0)
            throw new ArithmeticException("Duration overflow or underflow");
        return normalized;
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
        validateMultipliers(timeReduction, powerIncrease);
        if (timeReduction == 1) return 1;
        if (powerIncrease == 1) return Double.MAX_VALUE;
        return overclockSpeed(Math.ceil(basePower), availablePower, timeReduction, powerIncrease, -1, 0, true);
    }

    private static void validateMultipliers(double timeDivisor, double powerMultiplier) {
        if (!Double.isFinite(timeDivisor) || timeDivisor < 1
            || !Double.isFinite(powerMultiplier)
            || powerMultiplier < 1) throw new IllegalArgumentException("Invalid overclock parameters");
    }

    /** Whole source steps and the master's optional fractional last step follow the same power curve. */
    private static double overclockSpeed(double basePower, long availablePower, double timeDivisor,
        double powerMultiplier, long maximumOverclocks, long perfectOverclocks, boolean fractionalLastStep) {
        double power = basePower;
        double speed = 1;
        int count = 0;
        while (maximumOverclocks < 0 || count < maximumOverclocks) {
            double nextPower = count < perfectOverclocks ? 4 : powerMultiplier;
            double nextSpeed = count < perfectOverclocks ? 4 : timeDivisor;
            if (power > availablePower / nextPower) {
                if (fractionalLastStep)
                    speed *= Math.pow(Math.max(1, availablePower / power), Math.log(nextSpeed) / Math.log(nextPower));
                break;
            }
            power *= nextPower;
            speed *= nextSpeed;
            count++;
        }
        return Double.isFinite(speed) ? speed : Double.MAX_VALUE;
    }
}
