package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import com.Nxer.TwistSpaceTechnology.common.misc.OverclockType;
import com.Nxer.TwistSpaceTechnology.config.Config;

/** Shared source normalization and final execution overclocks. Only final execution uses the configured time target. */
public final class BottledChaosOverclock {

    public static int minimumOverclockTicks() {
        return Config.BottledChaosMinimumOverclockSeconds * 20;
    }

    /** Compare speed at equal power growth; keep the stronger native step on equal efficiency. */
    public static boolean isBetterSourceOverclock(OverclockType candidate, double timeDivisor, double powerMultiplier) {
        // Fixed-speed sources retain their explicit no-overclock behavior.
        if (candidate == OverclockType.NONE || powerMultiplier <= 1) return false;
        int efficiency = Double.compare(
            Math.log(candidate.timeReduction) / Math.log(candidate.powerIncrease),
            Math.log(timeDivisor) / Math.log(powerMultiplier));
        return efficiency > 0 || efficiency == 0 && candidate.timeReduction > timeDivisor;
    }

    /** Apply the final host overclock curve without a wired power ceiling or per-stage iteration. */
    public static BigInteger wirelessCost(double power, double workTicks, double timeReduction, double powerIncrease) {
        if (!Double.isFinite(power) || power <= 0 || !Double.isFinite(workTicks) || workTicks <= 0)
            throw new IllegalArgumentException("Invalid wireless work");
        validateMultipliers(timeReduction, powerIncrease);
        double speed = Math.max(1, workTicks / minimumOverclockTicks());
        double energyMultiplier = timeReduction > 1
            ? Math.pow(speed, scalingExponent(timeReduction, powerIncrease / timeReduction))
            : 1;
        return BigDecimal.valueOf(power)
            .multiply(BigDecimal.valueOf(workTicks))
            .multiply(BigDecimal.valueOf(energyMultiplier))
            .setScale(0, RoundingMode.CEILING)
            .toBigIntegerExact();
    }

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
        int perfectOverclocks, int[] overclockLimitAlternatives, boolean normalizeEnergy) {
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
        if (!Double.isFinite(modifiedPower) || modifiedPower <= 0
            || !normalizeEnergy && modifiedPower >= Long.MAX_VALUE)
            throw new ArithmeticException("Recipe power overflow");
        if (!Double.isFinite(work) || work <= 0) throw new ArithmeticException("Duration overflow");
        // Modes explicitly disabling overclocks keep their fixed source speed, including at 1/1.
        if (powerMultiplier == 1) return normalizeEnergy ? work * modifiedPower / targetEUt : work;
        double basePower = Math.ceil(modifiedPower);
        while (basePower > targetEUt) {
            basePower = Math.ceil(basePower / powerMultiplier);
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
                false,
                normalizeEnergy);
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
                        false,
                        normalizeEnergy)
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
        int minimumTicks = minimumOverclockTicks();
        double targetSpeed = Math.max(1, workTicks / minimumTicks);
        double speed = Math.min(maximumSpeed, targetSpeed);
        double power = Math.ceil(basePower);
        if (timeReduction > 1 && powerIncrease > 1) {
            power *= Math.pow(speed, scalingExponent(timeReduction, powerIncrease));
        }
        long eut = (long) Math.min(availablePower, Math.ceil(power));
        double ticks = workTicks > minimumTicks && maximumSpeed >= targetSpeed ? minimumTicks
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
        return overclockSpeed(Math.ceil(basePower), availablePower, timeReduction, powerIncrease, -1, 0, true, false);
    }

    private static void validateMultipliers(double timeDivisor, double powerMultiplier) {
        if (!Double.isFinite(timeDivisor) || timeDivisor < 1
            || !Double.isFinite(powerMultiplier)
            || powerMultiplier < 1) throw new IllegalArgumentException("Invalid overclock parameters");
    }

    private static double scalingExponent(double timeReduction, double multiplier) {
        return Math.log(multiplier) / Math.log(timeReduction);
    }

    /** Wireless folding discounts unused target power; wired folding and final execution retain their speed curve. */
    private static double overclockSpeed(double basePower, long availablePower, double timeDivisor,
        double powerMultiplier, long maximumOverclocks, long perfectOverclocks, boolean fractionalLastStep,
        boolean normalizeEnergy) {
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
        if (normalizeEnergy) speed *= availablePower / power;
        return Double.isFinite(speed) ? speed : Double.MAX_VALUE;
    }
}
