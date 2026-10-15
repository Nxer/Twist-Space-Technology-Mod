package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.math.BigInteger;

/** Round-by-round allocation in hatch-number order; enabled credentials retain their share. */
public final class BottledChaosPowerAllocator {

    private BottledChaosPowerAllocator() {}

    /** Returns null for overcapacity, without changing the supplied capacities. */
    public static long[] allocate(long totalCapacity, long[] capacities) {
        if (totalCapacity <= 0) return null;
        long remaining = totalCapacity;
        for (long capacity : capacities) {
            if (capacity < 0 || capacity > remaining) return null;
            remaining -= capacity;
        }
        long[] shares = capacities.clone();
        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < shares.length; i++) {
                long share = shares[i];
                if (share > 0 && share <= remaining / 3) {
                    remaining -= share * 3;
                    shares[i] = share * 4;
                    changed = true;
                }
            }
        } while (changed);
        return shares;
    }

    public static long powerForShare(long power, long share, long totalCapacity) {
        if (power <= 0 || share <= 0 || totalCapacity <= 0 || share > totalCapacity) return 0;
        // Splitting before multiplication keeps the ordinary <=1024 capacity path entirely in long arithmetic.
        try {
            return power / totalCapacity * share + Math.multiplyExact(power % totalCapacity, share) / totalCapacity;
        } catch (ArithmeticException exception) {
            return BigInteger.valueOf(power)
                .multiply(BigInteger.valueOf(share))
                .divide(BigInteger.valueOf(totalCapacity))
                .longValueExact();
        }
    }

    /** Shares power by actual credential cost; lower hatch numbers receive indivisible EU/t remainders first. */
    public static long[] concentratedBudgets(long power, long[] capacities) {
        long total = 0;
        for (long capacity : capacities) {
            if (capacity < 0) throw new IllegalArgumentException("Negative capacity");
            total = Math.addExact(total, capacity);
        }
        long[] budgets = new long[capacities.length];
        if (power <= 0 || total == 0) return budgets;
        long remaining = power;
        for (int i = 0; i < capacities.length; i++) {
            budgets[i] = powerForShare(power, capacities[i], total);
            remaining -= budgets[i];
        }
        for (int slot = 0; slot < capacities.length; slot++) {
            if (remaining == 0) break;
            if (capacities[slot] == 0) continue;
            budgets[slot]++;
            remaining--;
        }
        return budgets;
    }
}
