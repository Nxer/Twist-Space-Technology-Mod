package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.machine;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.github.bsideup.jabel.Desugar;

/** Shared integer stacking costs; quantities remain exact even after combining several long-sized packages. */
public final class BottledChaosModuleStacking {

    private static final BigInteger EIGHT = BigInteger.valueOf(8);
    private static final BigInteger THIRTY_SIX = BigInteger.valueOf(36);

    public static BigInteger speed(BigInteger quantity) {
        if (quantity.signum() <= 0) return BigInteger.ZERO;
        if (quantity.compareTo(THIRTY_SIX) >= 0) return EIGHT.add(
            quantity.subtract(THIRTY_SIX)
                .divide(EIGHT));
        int speed = 1;
        int threshold = 3;
        while (quantity.intValue() >= threshold) {
            speed++;
            threshold += speed + 1;
        }
        return BigInteger.valueOf(speed);
    }

    public static BigInteger cost(BigInteger speed) {
        return speed.compareTo(EIGHT) <= 0 ? speed.multiply(speed.add(BigInteger.ONE))
            .divide(BigInteger.valueOf(2))
            : THIRTY_SIX.add(
                speed.subtract(EIGHT)
                    .multiply(EIGHT));
    }

    @Desugar
    public record Combination(int parallel, BigInteger lowerSpeed, int faster, BigInteger unused) {

        public BigInteger throughput() {
            return lowerSpeed.multiply(BigInteger.valueOf(parallel))
                .add(BigInteger.valueOf(faster));
        }

        public double speedMultiplier() {
            return throughput().doubleValue() / parallel;
        }
    }

    /**
     * Every parallel count remains a candidate. Convex tier costs make adjacent speed tiers optimal at that count.
     * The caller compares actual recipe durations, not just parallel times speed.
     */
    public static List<Combination> combinations(BigInteger quantity, int positions, BigInteger stacks) {
        List<Combination> result = new ArrayList<>();
        if (quantity.signum() <= 0 || stacks.signum() <= 0 || positions <= 0) return result;
        int maximum = quantity.min(BigInteger.valueOf(positions))
            .intValueExact();
        BigInteger maximumSpeed = quantity.compareTo(BigInteger.valueOf(positions)) <= 0 ? BigInteger.ONE
            : speed(stacks);
        for (int parallel = 1; parallel <= maximum; parallel++) {
            BigInteger count = BigInteger.valueOf(parallel);
            BigInteger base = speed(quantity.divide(count));
            BigInteger nextCost = cost(base.add(BigInteger.ONE)).subtract(cost(base));
            BigInteger throughput = count.multiply(base)
                .add(
                    quantity.subtract(count.multiply(cost(base)))
                        .divide(nextCost))
                .min(count.multiply(maximumSpeed));
            BigInteger[] tiers = throughput.divideAndRemainder(count);
            int faster = tiers[1].intValueExact();
            BigInteger used = cost(tiers[0]).multiply(BigInteger.valueOf(parallel - faster))
                .add(cost(tiers[0].add(BigInteger.ONE)).multiply(BigInteger.valueOf(faster)));
            result.add(new Combination(parallel, tiers[0], faster, quantity.subtract(used)));
        }
        return result;
    }
}
