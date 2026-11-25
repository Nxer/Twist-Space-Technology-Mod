package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import java.util.BitSet;

/** An implicit, deterministic volume of meteor blocks. Nothing is placed in a world. */
public final class StarMeteorField {

    private static final int[][] SURFACES = new int[StarMath.MAX_RENDER_RADIUS + 1][];
    private static final int[][] DIRECTIONS = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 },
        { 1, 0, 0 } };
    public final int radius;
    public final int diameter;
    private final long seed;
    private final double[] cumulative;
    private final StarMeteorClusters clusters;

    public StarMeteorField(int radius, long seed, double[] weights) {
        this(radius, seed, weights, null);
    }

    /** A filler mask selects clustered rendering; null retains coordinate-random scattered rendering. */
    public StarMeteorField(int radius, long seed, double[] weights, boolean[] fillers) {
        if (radius < 1 || radius > StarMath.MAX_RENDER_RADIUS)
            throw new IllegalArgumentException("Meteor projection radius out of range");
        if (weights.length == 0) throw new IllegalArgumentException("Empty meteor palette");
        this.radius = radius;
        diameter = radius * 2 + 1;
        this.seed = seed;
        cumulative = new double[weights.length];
        double total = 0;
        for (int i = 0; i < weights.length; i++) {
            if (!Double.isFinite(weights[i]) || weights[i] < 0) {
                throw new IllegalArgumentException("Invalid meteor material weight");
            }
            total += weights[i];
            cumulative[i] = total;
        }
        if (!Double.isFinite(total) || total <= 0) throw new IllegalArgumentException("Empty meteor distribution");
        for (int i = 0; i < cumulative.length; i++) cumulative[i] /= total;
        cumulative[cumulative.length - 1] = 1;
        clusters = fillers == null ? null : new StarMeteorClusters(this, seed, weights, fillers);
    }

    /** Client load-complete hook: share only seed/recipe-independent boundary and excavation indices. */
    public static void preloadGeometry() {
        for (int radius = 1; radius <= StarMath.MAX_RENDER_RADIUS; radius++) {
            new StarMeteorField(radius, 0, new double[] { 1 }).surfaceCells();
            new StarMeteorExcavation(radius);
        }
    }

    public boolean prepare(long budgetNanos) {
        return clusters == null || clusters.prepare(budgetNanos);
    }

    public boolean contains(int x, int y, int z) {
        return x >= -radius && x <= radius
            && y >= -radius
            && y <= radius
            && z >= -radius
            && z <= radius
            && 4L * (x * x + y * y + z * z) < (2L * radius + 1) * (2L * radius + 1);
    }

    /** Coordinate sampling preserves every block when the camera, visibility or mesh build order changes. */
    public int materialAt(int x, int y, int z) {
        if (!contains(x, y, z)) return -1;
        if (clusters != null) return clusters.materialAt(x, y, z);
        long hash = seed ^ x * 0x9E3779B97F4A7C15L ^ y * 0xC2B2AE3D27D4EB4FL ^ z * 0x165667B19E3779F9L;
        hash = (hash ^ (hash >>> 30)) * 0xBF58476D1CE4E5B9L;
        hash = (hash ^ (hash >>> 27)) * 0x94D049BB133111EBL;
        double sample = ((hash ^ (hash >>> 31)) >>> 11) * 0x1.0p-53;
        int lo = 0, hi = cumulative.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (sample < cumulative[mid]) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    public int exposedFaces(int x, int y, int z) {
        int mask = 0;
        for (int side = 0; side < DIRECTIONS.length; side++) {
            int[] d = DIRECTIONS[side];
            if (!contains(x + d[0], y + d[1], z + d[2])) mask |= 1 << side;
        }
        return mask;
    }

    /** Six axial projections find the opaque boundary in O(radius squared), ordered in top-to-bottom block layers. */
    public int[] surfaceCells() {
        synchronized (SURFACES) {
            if (SURFACES[radius] != null) return SURFACES[radius];
            BitSet cells = new BitSet(diameter * diameter * diameter);
            long limit = (2L * radius + 1) * (2L * radius + 1);
            for (int a = -radius; a <= radius; a++) for (int b = -radius; b <= radius; b++) {
                long remaining = limit - 4L * (a * a + b * b);
                if (remaining <= 0) continue;
                int extent = Math.min(radius, (int) Math.sqrt((remaining - 1) / 4));
                for (int c : new int[] { -extent, extent }) {
                    cells.set(encode(a, b, c));
                    cells.set(encode(a, c, b));
                    cells.set(encode(c, a, b));
                }
            }
            int[] result = new int[cells.cardinality()];
            int[] nextByLayer = new int[diameter];
            for (int i = cells.nextSetBit(0); i >= 0; i = cells.nextSetBit(i + 1)) nextByLayer[radius - y(i)]++;
            int next = 0;
            for (int layer = 0; layer < diameter; layer++) {
                int count = nextByLayer[layer];
                nextByLayer[layer] = next;
                next += count;
            }
            for (int i = cells.nextSetBit(0); i >= 0; i = cells.nextSetBit(i + 1))
                result[nextByLayer[radius - y(i)]++] = i;
            SURFACES[radius] = result;
            return result;
        }
    }

    private int encode(int x, int y, int z) {
        return ((x + radius) * diameter + y + radius) * diameter + z + radius;
    }

    public int x(int cell) {
        return cell / (diameter * diameter) - radius;
    }

    public int y(int cell) {
        return cell / diameter % diameter - radius;
    }

    public int z(int cell) {
        return cell % diameter - radius;
    }
}
