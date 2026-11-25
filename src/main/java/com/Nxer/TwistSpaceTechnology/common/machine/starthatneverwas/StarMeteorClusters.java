package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.Arrays;
import java.util.Random;

/** Seeded, quota-sized ore groups. Recipe fillers occupy the remaining cells; no vein noise is added. */
final class StarMeteorClusters {

    private enum Phase {
        COLLECT,
        COUNT,
        ASSIGN,
        DONE
    }

    private final StarMeteorField field;
    private final Random random;
    private final int[] quotas;
    private final Integer[] order;
    private short[] materials;
    private int[] available, distances;
    private int remaining, cursor, component;
    private int centerX, centerY, centerZ, threshold, ties;
    private Phase phase = Phase.COLLECT;

    StarMeteorClusters(StarMeteorField field, long seed, double[] weights, boolean[] fillers) {
        if (weights.length != fillers.length || weights.length > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid clustered meteor palette");
        }
        this.field = field;
        random = new Random(seed);
        quotas = new int[weights.length];
        order = new Integer[weights.length];
        double[] remainders = new double[weights.length];
        double total = 0;
        for (double weight : weights) total += weight;
        int volume = (int) StarMath.volume(field.radius);
        int missing = volume;
        for (int i = 0; i < weights.length; i++) {
            double exact = volume * (weights[i] / total);
            quotas[i] = (int) Math.floor(exact);
            remainders[i] = exact - quotas[i];
            missing -= quotas[i];
            order[i] = i;
        }
        // Largest remainders preserve the complete recipe volume, including rare and filler components.
        Arrays.sort(order, (a, b) -> Double.compare(remainders[b], remainders[a]));
        for (int i = 0; i < missing; i++) quotas[order[i]]++;
        Arrays.sort(order, (a, b) -> {
            int group = Boolean.compare(fillers[a], fillers[b]);
            int size = Integer.compare(quotas[b], quotas[a]);
            return group != 0 ? group : size != 0 ? size : Integer.compare(a, b);
        });
    }

    /** All volume-dependent work is resumable and charged to the mesh's shared frame budget. */
    boolean prepare(long budgetNanos) {
        if (phase == Phase.DONE) return true;
        if (budgetNanos <= 0) return false;
        long start = System.nanoTime();
        if (materials == null) {
            materials = new short[field.diameter * field.diameter * field.diameter];
            available = new int[(int) StarMath.volume(field.radius)];
            distances = new int[field.diameter * field.diameter];
        }
        do {
            // No sorting or object allocation per voxel, and a clock check at least every 256 cells.
            for (int work = 0; work < 256 && phase != Phase.DONE; work++) {
                switch (phase) {
                    case COLLECT -> {
                        int cell = cursor++;
                        if (field.contains(field.x(cell), field.y(cell), field.z(cell))) available[remaining++] = cell;
                        if (cursor == materials.length) nextComponent();
                    }
                    case COUNT -> {
                        distances[distance(available[cursor++])]++;
                        if (cursor == remaining) {
                            threshold = 0;
                            ties = quotas[order[component]];
                            while (distances[threshold] < ties) ties -= distances[threshold++];
                            cursor = 0;
                            phase = Phase.ASSIGN;
                        }
                    }
                    case ASSIGN -> {
                        int cell = available[cursor];
                        int distance = threshold == Integer.MAX_VALUE ? 0 : distance(cell);
                        if (distance < threshold || (distance == threshold && ties > 0)) {
                            if (distance == threshold) ties--;
                            materials[cell] = order[component].shortValue();
                            available[cursor] = available[--remaining];
                        } else cursor++;
                        if (cursor == remaining) {
                            component++;
                            nextComponent();
                        }
                    }
                    default -> {}
                }
            }
        } while (phase != Phase.DONE && System.nanoTime() - start < budgetNanos);
        return phase == Phase.DONE;
    }

    private void nextComponent() {
        cursor = 0;
        if (remaining == 0) {
            available = distances = null;
            phase = Phase.DONE;
            return;
        }
        while (quotas[order[component]] == 0) component++;
        if (quotas[order[component]] == remaining) {
            threshold = Integer.MAX_VALUE;
            phase = Phase.ASSIGN;
            return;
        }
        int center = available[random.nextInt(remaining)];
        centerX = field.x(center);
        centerY = field.y(center);
        centerZ = field.z(center);
        Arrays.fill(distances, 0);
        phase = Phase.COUNT;
    }

    private int distance(int cell) {
        int dx = field.x(cell) - centerX, dy = field.y(cell) - centerY, dz = field.z(cell) - centerZ;
        return dx * dx + dy * dy + dz * dz;
    }

    int materialAt(int x, int y, int z) {
        if (phase != Phase.DONE) throw new IllegalStateException("Meteor clusters are still being prepared");
        return materials[((x + field.radius) * field.diameter + y + field.radius) * field.diameter + z + field.radius];
    }
}
