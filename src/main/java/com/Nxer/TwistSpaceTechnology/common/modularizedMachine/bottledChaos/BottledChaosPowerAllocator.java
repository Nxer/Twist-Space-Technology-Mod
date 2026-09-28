package com.Nxer.TwistSpaceTechnology.common.modularizedMachine.bottledChaos;

import java.math.BigInteger;

/** Pure, deterministic scheduling in structure-slot order; idle credentials retain their share. */
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
        return BigInteger.valueOf(power)
            .multiply(BigInteger.valueOf(share))
            .divide(BigInteger.valueOf(totalCapacity))
            .longValueExact();
    }
}
