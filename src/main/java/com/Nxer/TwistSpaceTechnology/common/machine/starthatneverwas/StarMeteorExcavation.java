package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

/** Exact voxel cursor: top to bottom, then rows from -Z to +Z, each from -X to +X. */
public final class StarMeteorExcavation {

    private static final Geometry[] GEOMETRY = new Geometry[StarMath.MAX_RADIUS + 1];
    private static final int[][] DIRECTIONS = { { 0, -1, 0 }, { 0, 1, 0 }, { 0, 0, -1 }, { 0, 0, 1 }, { -1, 0, 0 },
        { 1, 0, 0 } };
    private final Geometry geometry;
    private final int radius, diameter;
    private int cachedLayer = -1;
    private long[] rows;

    public StarMeteorExcavation(int radius) {
        geometry = geometry(radius);
        this.radius = radius;
        diameter = 2 * radius + 1;
    }

    public long workUnits(int tier) {
        return geometry.workUnits(tier);
    }

    public static long workUnits(int radius, int tier) {
        return geometry(radius).workUnits(tier);
    }

    private static Geometry geometry(int radius) {
        if (radius < 1 || radius > StarMath.MAX_RADIUS)
            throw new IllegalArgumentException("Meteor radius out of range");
        synchronized (GEOMETRY) {
            if (GEOMETRY[radius] == null) GEOMETRY[radius] = new Geometry(radius);
            return GEOMETRY[radius];
        }
    }

    /** Only layer prefixes are shared; a cursor materializes at most one row of prefixes. */
    private static final class Geometry {

        private final long[] blocks, rowCounts;

        private Geometry(int radius) {
            int diameter = 2 * radius + 1;
            blocks = new long[diameter + 1];
            rowCounts = new long[diameter + 1];
            long limit = (long) radius * (radius + 1);
            for (int y = 0; y <= radius; y++) {
                long remaining = limit - (long) y * y;
                int extent = Math.min(radius, (int) Math.sqrt(remaining));
                long count = 0;
                for (int z = 0; z <= extent; z++) {
                    long length = 2L * Math.min(radius, (int) Math.sqrt(remaining - (long) z * z)) + 1;
                    count += length * (z == 0 ? 1 : 2);
                }
                blocks[radius - y + 1] = blocks[radius + y + 1] = count;
                rowCounts[radius - y + 1] = rowCounts[radius + y + 1] = 2L * extent + 1;
            }
            for (int i = 1; i <= diameter; i++) {
                blocks[i] += blocks[i - 1];
                rowCounts[i] += rowCounts[i - 1];
            }
        }

        private long workUnits(int tier) {
            return switch (tier) {
                case 1 -> blocks[blocks.length - 1];
                case 2 -> rowCounts[rowCounts.length - 1];
                case 3 -> blocks.length - 1;
                default -> throw new IllegalArgumentException("Invalid meteor mining tier");
            };
        }
    }

    private long[] rows(int layer) {
        if (cachedLayer == layer) return rows;
        if (rows == null) rows = new long[diameter + 1];
        int y = radius - layer;
        long limit = (long) radius * (radius + 1) - (long) y * y;
        for (int row = 0; row < diameter; row++) {
            int z = row - radius;
            long remaining = limit - (long) z * z;
            rows[row + 1] = rows[row] + (remaining < 0 ? 0 : 2L * Math.min(radius, (int) Math.sqrt(remaining)) + 1);
        }
        cachedLayer = layer;
        return rows;
    }

    private static int interval(long[] prefixes, long value) {
        int lo = 0, hi = prefixes.length - 2;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (prefixes[mid] <= value) lo = mid;
            else hi = mid - 1;
        }
        return lo;
    }

    private long removedRows(long completed) {
        int layer = interval(geometry.rowCounts, completed);
        int extent = (int) (geometry.rowCounts[layer + 1] - geometry.rowCounts[layer] - 1) / 2;
        int row = radius - extent + (int) (completed - geometry.rowCounts[layer]);
        return geometry.blocks[layer] + rows(layer)[row];
    }

    /** Completed native operations, rather than an equal fraction of ore volume per layer. */
    public long removed(int tier, long completed) {
        completed = Math.max(0, Math.min(workUnits(tier), completed));
        if (completed == 0) return 0;
        if (completed == workUnits(tier)) return workUnits(1);
        return switch (tier) {
            case 1 -> completed;
            case 2 -> removedRows(completed);
            case 3 -> geometry.blocks[(int) completed];
            default -> throw new IllegalArgumentException("Invalid meteor mining tier");
        };
    }

    public Cut at(long removed) {
        removed = Math.max(0, Math.min(workUnits(1), removed));
        if (removed == workUnits(1)) return new Cut(removed, diameter, -radius - 1, -radius, -radius);
        int layer = interval(geometry.blocks, removed);
        long local = removed - geometry.blocks[layer];
        long[] rows = rows(layer);
        int row = interval(rows, local);
        int extent = (int) (rows[row + 1] - rows[row] - 1) / 2;
        return new Cut(removed, layer, radius - layer, row - radius, -extent + (int) (local - rows[row]));
    }

    public static final class Cut {

        public final long removed;
        public final int layer, y, z, x;

        private Cut(long removed, int layer, int y, int z, int x) {
            this.removed = removed;
            this.layer = layer;
            this.y = y;
            this.z = z;
            this.x = x;
        }

        /** Shape containment is checked separately; this only describes the mining cursor. */
        public boolean remains(int x, int y, int z) {
            return y < this.y || y == this.y && (z > this.z || z == this.z && x >= this.x);
        }

        /** Replaces the active layer and adds only new top faces to the cached layer immediately beneath it. */
        public int replacementFaces(StarMeteorField field, int x, int y, int z) {
            if (y > this.y || y < this.y - 1 || !field.contains(x, y, z) || !remains(x, y, z)) return 0;
            if (y < this.y) return field.contains(x, y + 1, z) && !remains(x, y + 1, z) ? 2 : 0;
            int faces = 0;
            for (int side = 0; side < DIRECTIONS.length; side++) {
                int[] direction = DIRECTIONS[side];
                int nx = x + direction[0], ny = y + direction[1], nz = z + direction[2];
                if (!field.contains(nx, ny, nz) || !remains(nx, ny, nz)) faces |= 1 << side;
            }
            return faces;
        }
    }
}
