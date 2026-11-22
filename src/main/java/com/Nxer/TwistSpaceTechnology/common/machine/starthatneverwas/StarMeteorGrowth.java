package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

/**
 * Deterministic condensation of whole ore blocks. Blood Magic's seven lobes lead the growth front; their recesses
 * fill progressively. A face exists from its block's birth until its neighbour appears, so every substep is closed.
 */
public final class StarMeteorGrowth {

    private static final int[][] BOXES = { { -8, -8, -8, 8, 8, 8 }, { 3, -10, -1, 15, 2, 11 }, { 0, 0, -10, 12, 12, 2 },
        { 1, 2, 2, 13, 14, 14 }, { -12, -5, 0, 0, 7, 12 }, { -13, -2, -11, -1, 10, 1 }, { -6, -14, -9, 6, -2, 3 } };
    private final StarMeteorField field;
    private final long seed;
    private final short[] births;
    private final double delay;
    public final int stages;

    public StarMeteorGrowth(StarMeteorField field, long seed) {
        this.field = field;
        this.seed = seed;
        stages = field.radius * StarMeteorFormation.STEPS_PER_BLOCK;
        births = new short[field.diameter * field.diameter * field.diameter];
        delay = Math.min(.75, field.radius * .12);
    }

    /** Cached lazily, only as the bounded frontier builder visits a block or one of its neighbours. */
    public int birth(int x, int y, int z) {
        if (!field.contains(x, y, z)) return stages + 1;
        int cell = encode(x, y, z);
        if (births[cell] != 0) return births[cell] - 1;
        double distance = Math.max(0, Math.sqrt(x * x + y * y + z * z) - .5);
        double padding = Double.POSITIVE_INFINITY;
        double scale = field.radius / 24.0;
        for (int[] box : BOXES) {
            double needed = Math.max(
                Math.max(box[0] * scale - x, x - box[3] * scale),
                Math.max(
                    Math.max(box[1] * scale - y, y - box[4] * scale),
                    Math.max(box[2] * scale - z, z - box[5] * scale)));
            padding = Math.min(padding, Math.max(0, needed));
        }
        // Start filling recesses after the first third, rather than leaving them for the final shell.
        double arrival = Math.max(distance, padding <= 0 ? 0 : field.radius * .3 + padding * 1.05);
        double t = Math.min(1, arrival / field.radius);
        long hash = seed ^ Math.floorDiv(x, 3) * 0x9E3779B97F4A7C15L
            ^ Math.floorDiv(y, 3) * 0xC2B2AE3D27D4EB4FL
            ^ Math.floorDiv(z, 3) * 0x165667B19E3779F9L;
        hash = (hash ^ (hash >>> 30)) * 0xBF58476D1CE4E5B9L;
        hash = (hash ^ (hash >>> 27)) * 0x94D049BB133111EBL;
        double offset = ((hash ^ (hash >>> 31)) >>> 11) * 0x1.0p-53;
        // Neighbouring blocks condense together. The offset vanishes at the end instead of being clamped there.
        arrival += delay * offset * 4 * t * (1 - t);
        int stage = Math.min(stages, (int) Math.ceil(arrival * StarMeteorFormation.STEPS_PER_BLOCK));
        births[cell] = (short) (stage + 1);
        return stage;
    }

    public Band band(int index) {
        if (index < 0 || index >= field.radius) throw new IllegalArgumentException("Invalid growth band");
        return new Band(index);
    }

    private int encode(int x, int y, int z) {
        return ((x + field.radius) * field.diameter + y + field.radius) * field.diameter + z + field.radius;
    }

    private double padding(double radius) {
        return Math.max(0, (radius - field.radius * .3) / 1.05);
    }

    /**
     * Candidate iterator for one block of radial travel. Merged row intervals visit only the spherical band and
     * the seven expanding lobe boundaries, including a neighbour margin. No volume scan or duplicate faces.
     */
    public final class Band {

        public final int index, first, last;
        private final int outer;
        private final double innerDistance;
        private final int[][] outerBoxes = new int[BOXES.length][6], innerBoxes = new int[BOXES.length][6];
        private final int[] starts = new int[2 + BOXES.length * 2], ends = new int[starts.length];
        private int y, z, x, interval, count;

        private Band(int index) {
            this.index = index;
            first = index * StarMeteorFormation.STEPS_PER_BLOCK;
            last = first + StarMeteorFormation.STEPS_PER_BLOCK;
            outer = index + 1;
            innerDistance = index - delay - .5;
            y = -outer;
            z = -outer - 1;
            double scale = field.radius / 24.0;
            double outerPad = padding(outer), innerPad = padding(index - delay);
            for (int i = 0; i < BOXES.length; i++) for (int axis = 0; axis < 3; axis++) {
                outerBoxes[i][axis] = (int) Math.ceil(BOXES[i][axis] * scale - outerPad);
                outerBoxes[i][axis + 3] = (int) Math.floor(BOXES[i][axis + 3] * scale + outerPad);
                innerBoxes[i][axis] = (int) Math.ceil(BOXES[i][axis] * scale - innerPad) + 1;
                innerBoxes[i][axis + 3] = (int) Math.floor(BOXES[i][axis + 3] * scale + innerPad) - 1;
            }
        }

        /** Returns one encoded candidate, or -1 when this band is complete. */
        public int next() {
            while (y <= outer) {
                if (interval < count) {
                    int cell = encode(x, y, z);
                    if (++x > ends[interval] && ++interval < count) x = starts[interval];
                    return cell;
                }
                if (++z > outer) {
                    z = -outer;
                    if (++y > outer) break;
                }
                row();
            }
            return -1;
        }

        private void row() {
            interval = count = 0;
            double square = (outer + .5) * (outer + .5) - y * y - z * z;
            if (square < 0) return;
            int extent = Math.min(outer, (int) Math.sqrt(square));
            for (int[] out : outerBoxes) {
                if (y < out[1] || y > out[4] || z < out[2] || z > out[5]) continue;
                add(out[0], out[3], extent);
            }
            // Merge the possible body, then subtract the guaranteed interior of the union of all seven lobes.
            // A lobe's hidden boundary inside another lobe must not become extra frontier work.
            int merged = 0;
            for (int i = 0; i < count; i++) {
                if (merged > 0 && starts[i] <= ends[merged - 1] + 1)
                    ends[merged - 1] = Math.max(ends[merged - 1], ends[i]);
                else {
                    starts[merged] = starts[i];
                    ends[merged++] = ends[i];
                }
            }
            count = merged;
            double innerSquare = innerDistance * innerDistance - y * y - z * z;
            if (innerDistance > 0 && innerSquare >= 0) {
                int inside = (int) Math.sqrt(innerSquare);
                for (int[] in : innerBoxes) {
                    if (y < in[1] || y > in[4] || z < in[2] || z > in[5]) continue;
                    subtract(Math.max(-inside, in[0]), Math.min(inside, in[3]));
                }
            }
            if (count > 0) x = starts[0];
        }

        private void subtract(int low, int high) {
            if (low > high) return;
            for (int i = 0; i < count;) {
                if (high < starts[i] || low > ends[i]) i++;
                else if (low <= starts[i] && high >= ends[i]) {
                    System.arraycopy(starts, i + 1, starts, i, count - i - 1);
                    System.arraycopy(ends, i + 1, ends, i, count - i - 1);
                    count--;
                } else if (low <= starts[i]) starts[i++] = high + 1;
                else if (high >= ends[i]) ends[i++] = low - 1;
                else {
                    System.arraycopy(starts, i + 1, starts, i + 2, count - i - 1);
                    System.arraycopy(ends, i + 1, ends, i + 2, count - i - 1);
                    starts[i + 1] = high + 1;
                    ends[i + 1] = ends[i];
                    ends[i] = low - 1;
                    count++;
                    i += 2;
                }
            }
        }

        private void add(int low, int high, int extent) {
            low = Math.max(low, -extent);
            high = Math.min(high, extent);
            if (low > high) return;
            int at = count++;
            while (at > 0 && starts[at - 1] > low) {
                starts[at] = starts[at - 1];
                ends[at] = ends[at - 1];
                at--;
            }
            starts[at] = low;
            ends[at] = high;
        }
    }
}
