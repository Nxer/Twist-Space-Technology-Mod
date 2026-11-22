package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.math.BigInteger;
import java.util.Random;

/** Bounded, world-independent arithmetic for one virtual meteor. */
public final class StarMath {

    /** Temporary logical limit for expansion tests; never use this to size render geometry. */
    public static final int MAX_RADIUS = 1024;
    public static final int MAX_RENDER_RADIUS = 48;
    public static final int MAX_DRONES = 4096;
    public static final int MAX_DRONE_TIER = 14;
    private static final int EXPANSION_COST_SCALE = 64;
    /** Fraction of the previous grade's full-fleet parallel gain retained after upgrading the same fleet. */
    private static final double DRONE_UPGRADE_RETENTION = 0.5;

    private static void checkRadius(int radius) {
        if (radius < 1 || radius > MAX_RADIUS) throw new IllegalArgumentException("Meteor radius out of range");
    }

    /** Exact Blood Magic lattice volume, counted by symmetric rows once per requested radius. */
    public static long volume(int radius) {
        return StarMeteorExcavation.workUnits(radius, 1);
    }

    public static int renderRadius(int radius) {
        checkRadius(radius);
        return Math.min(radius, MAX_RENDER_RADIUS);
    }

    public static long workUnits(int radius, int miningTier) {
        return StarMeteorExcavation.workUnits(radius, miningTier);
    }

    /** Cost per meteor: ceil(64 * ((target / original)^2 - 1)), using integer arithmetic. */
    public static long expansionCatalysts(int originalRadius, int radius) {
        checkRadius(originalRadius);
        checkRadius(radius);
        if (radius <= originalRadius) return 0;
        long originalSquared = (long) originalRadius * originalRadius;
        long amount = EXPANSION_COST_SCALE * ((long) radius * radius - originalSquared);
        return 1 + (amount - 1) / originalSquared;
    }

    /** Basic LP consumption follows the original recipe's cost per voxel, rounded once for the whole batch. */
    public static long lifeEssence(int originalCost, int originalRadius, int radius, long parallel) {
        if (originalCost < 0 || parallel < 1) throw new IllegalArgumentException("Invalid meteor cost");
        BigInteger originalVolume = BigInteger.valueOf(volume(originalRadius));
        return BigInteger.valueOf(volume(radius))
            .multiply(BigInteger.valueOf(parallel))
            .multiply(BigInteger.valueOf(Math.max(1, originalCost)))
            .add(originalVolume.subtract(BigInteger.ONE))
            .divide(originalVolume)
            .longValueExact();
    }

    /** Keep the existing R32 = one octuple cobblestone reference, charging by total logical volume. */
    public static long compressedStone(int radius, long parallel) {
        if (parallel < 1) throw new IllegalArgumentException("Invalid meteor parallel");
        return 1 + (Math.multiplyExact(volume(radius), parallel) - 1) / volume(32);
    }

    public static int recipeTier(int radius) {
        checkRadius(radius);
        int tier = 6;
        for (int limit = 1; radius > limit; limit *= 4) tier++;
        return tier;
    }

    public static int droneCapacity(int tier) {
        // UV now needs 24 drones; each grade retains its existing parallel ceiling.
        return Math.max(1, (int) Math.round(24 * Math.pow(1.5, tier - 8)));
    }

    private static double peakParallel(int tier) {
        return Math.pow(1 + 0.48 * Math.pow(1.5, tier - 9), 8 + (tier - 9) / 2.0);
    }

    /** Only the highest participating grade enters the curve; surplus drones still incur their full costs. */
    public static long droneParallel(int tier, int count) {
        if (tier < 0 || tier > MAX_DRONE_TIER || count < 0 || count > MAX_DRONES) {
            throw new IllegalArgumentException("Invalid drone fleet");
        }
        if (count == 0) return 1;
        if (tier == 0) throw new IllegalArgumentException("Missing drone grade");
        int capacity = droneCapacity(tier);
        double ratio = (double) count / capacity;
        double peak = peakParallel(tier);
        double missing = Math.max(0, 1 - ratio);
        double underfillPenalty = 0;
        if (missing > 0) {
            double previousMissing = 1 - (double) droneCapacity(tier - 1) / capacity;
            double curvature = (1 - DRONE_UPGRADE_RETENTION * (peakParallel(tier - 1) - 1) / (peak - 1))
                / (previousMissing * previousMissing);
            underfillPenalty = curvature * missing * missing;
        }
        double surplus = Math.max(0, ratio - 1);
        double surplusSquared = surplus * surplus;
        double factor = 1 - underfillPenalty - surplusSquared / (2 * (1 + surplusSquared));
        double parallel = 1 + (peak - 1) * factor;
        if (!Double.isFinite(parallel) || parallel >= Long.MAX_VALUE) {
            throw new ArithmeticException("Drone parallel overflow");
        }
        return Math.max(1, (long) Math.floor(parallel));
    }

    public static double parallelTimeFactor(long parallel) {
        if (parallel < 1) throw new IllegalArgumentException("Invalid meteor parallel");
        return Math.pow(2, Math.min(2, (parallel - 1) / 16.0));
    }

    public static int duration(int radius, int miningTier, int referenceCycleTicks, long parallel) {
        if (referenceCycleTicks < 1) throw new IllegalArgumentException("Invalid mining cycle");
        long soloTicks = Math.multiplyExact(workUnits(radius, miningTier), referenceCycleTicks);
        double ticks = soloTicks * parallelTimeFactor(parallel);
        if (!Double.isFinite(ticks) || ticks > Integer.MAX_VALUE) {
            throw new ArithmeticException("Processing duration overflow");
        }
        return Math.max(1, (int) Math.ceil(ticks));
    }

    public static long proportionalOutput(long amount, long removed, long volume) {
        if (amount < 0 || removed < 0 || removed > volume || volume < 1)
            throw new IllegalArgumentException("Invalid mined output share");
        return BigInteger.valueOf(amount)
            .multiply(BigInteger.valueOf(removed))
            .divide(BigInteger.valueOf(volume))
            .longValueExact();
    }

    /** The same checked total is used by resource trials and the final paid batch. */
    public static int batchDuration(int radius, int miningTier, int referenceCycleTicks, long parallel) {
        int preparation = Math.addExact(StarMeteorFormation.duration(radius), StarBeamSequence.FIELD_CONNECTION);
        return Math.addExact(preparation, duration(radius, miningTier, referenceCycleTicks, parallel));
    }

    public static long expectedOutput(long volume, double probability, int fortune, Random random) {
        if (volume < 0 || !Double.isFinite(probability)
            || probability < 0
            || probability > 1
            || fortune < 0
            || fortune > 4) throw new IllegalArgumentException("Invalid output distribution");
        double exact = volume * probability * (1 + fortune * 0.5);
        if (exact >= Long.MAX_VALUE) throw new ArithmeticException("Output overflow");
        long whole = (long) Math.floor(exact);
        // Only round the fractional remainder randomly; never roll once per block.
        return whole + (random.nextDouble() < exact - whole ? 1 : 0);
    }
}
