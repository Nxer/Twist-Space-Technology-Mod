package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos.processing;

/** Proportional power allocation; credential frame capacities are independent of tag costs. */
public final class BottledChaosPowerAllocator {

    public static long powerForShare(long power, int share, int totalCapacity) {
        if (power <= 0 || share <= 0 || totalCapacity <= 0 || share > totalCapacity) return 0;
        // The remainder and share are bounded by int; their product fits in long even at maximum power.
        return power / totalCapacity * share + (power % totalCapacity) * share / totalCapacity;
    }

    /** Lower hatch numbers receive indivisible EU/t remainders first; zero-weight slots reserve nothing. */
    public static long[] proportionalBudgets(long power, int[] capacities) {
        int total = 0;
        for (int capacity : capacities) {
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
