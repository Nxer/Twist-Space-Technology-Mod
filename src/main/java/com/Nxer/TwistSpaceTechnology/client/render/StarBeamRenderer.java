package com.Nxer.TwistSpaceTechnology.client.render;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarBeamSequence;
import com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas.StarStructure;
import com.Nxer.TwistSpaceTechnology.common.tile.TileStarContainment;

/** Fixed-size geometry sampled from a shared sequence; no per-wave entities, meshes or network packets. */
public final class StarBeamRenderer {

    private static final int SEGMENTS = 96;
    private static final int COUPLING_STEPS = 8;
    private static final double CORE_TOP = .51, COUPLING_START = 7.5, COUPLING_SPREAD = 4.5 / .68;
    private static final double LENS_TOP = 1.65, FAN_TOP = 11.5, DISSOLVE_EDGE = 1.5;
    private static final double SHELL_RADIUS = 58.16, SHELL_CENTER = 68;
    private static final double CONTACT_GLOW_WIDTH = Math.asin(7.5 / SHELL_RADIUS) / 2;
    private static final double CONTACT_LATITUDE = Math.asin((FAN_TOP - SHELL_CENTER) / SHELL_RADIUS);
    private static final double CONTACT_RADIUS = SHELL_RADIUS * Math.cos(CONTACT_LATITUDE);
    private static final double[] COIL_HEIGHTS = { LENS_TOP, 2.1, 3, 4.3, 5.6, 6.9, 8.2, 9.5, FAN_TOP };
    private static final double[] COIL_RADII = { 2.5, 5.7, 7.6, 8.7, 9.8, 10.9, 12, 13, CONTACT_RADIUS };
    private static final double[] COS = new double[SEGMENTS + 1], SIN = new double[SEGMENTS + 1];
    private static final double[] HEX_X = new double[SEGMENTS + 1], HEX_Z = new double[SEGMENTS + 1];
    static {
        for (int i = 0; i <= SEGMENTS; i++) {
            double angle = i * Math.PI * 2 / SEGMENTS;
            COS[i] = Math.cos(angle);
            SIN[i] = Math.sin(angle);
        }
        for (int i = 0; i <= SEGMENTS; i++) {
            int edge = Math.min(5, i / (SEGMENTS / 6));
            int from = edge * SEGMENTS / 6, to = (edge + 1) * SEGMENTS / 6;
            double along = (i - from) / (double) (SEGMENTS / 6);
            HEX_X[i] = COS[from] + (COS[to] - COS[from]) * along;
            HEX_Z[i] = SIN[from] + (SIN[to] - SIN[from]) * along;
        }
    }

    public static void miner(StarBeamSequence sequence, double length, double clock) {
        float pulseOpacity = (float) sequence.opacity(clock);
        if (pulseOpacity <= 0 || length <= 0) return;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        double head = Math.min(length - .5, length * sequence.beamExtension(clock));
        double cutoff = shutdownHeight(sequence, clock, length);
        GL11.glPushMatrix();
        try {
            GL11.glRotated(beamAngle(clock), 0, 1, 0);
            fadingTube(0, head, .42, .95f, .1f, .14f, .48f, cutoff);
            fadingTube(0, head, .15, 1, .48f, .24f, .72f, cutoff);
            pulse(sequence, clock, length, false, pulseOpacity);
        } finally {
            GL11.glPopMatrix();
        }
    }

    public static void receiver(TileStarContainment star, double clock) {
        StarBeamSequence sequence = star.beam;
        float pulseOpacity = (float) sequence.opacity(clock);
        double extension = sequence.fanExtension(clock);
        if (pulseOpacity <= 0 || extension <= 0 || star.beamSourceDepth <= 0) return;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        double pulseAge = sequence.pulseAge(clock);
        double cutoff = shutdownHeight(sequence, clock, star.beamSourceDepth) - star.beamSourceDepth;
        coils(extension, pulseAge, star.beamSourceDepth, clock, cutoff, pulseOpacity, false);
        coils(extension, pulseAge, star.beamSourceDepth, clock, cutoff, pulseOpacity, true);
        // The anchor origin is its center: clip both the steady beam and light packet above its top face.
        GL11.glPushMatrix();
        try {
            GL11.glRotated(beamAngle(clock), 0, 1, 0);
            double head = StarBeamSequence.FIELD_BOTTOM * extension;
            upperTube(CORE_TOP, head, .68, .95f, .3f, .18f, .5f, cutoff);
            upperTube(CORE_TOP, head, .28, 1, .65f, .38f, .82f, cutoff);
            pulse(sequence, clock, star.beamSourceDepth, true, pulseOpacity);
        } finally {
            GL11.glPopMatrix();
        }
        // A narrow, curved receiving collar lies on the shell rather than forming a bright flat disc below it.
        float contact = (float) clamp((extension - .8) / .2);
        if (contact > 0) {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glPushMatrix();
            GL11.glTranslated(0, SHELL_CENTER, 0);
            // A soft patch follows the shell curvature where the flared beam becomes the travelling ripple.
            float surge = (float) coilSurge(pulseAge, star.beamSourceDepth, StarBeamSequence.FIELD_BOTTOM);
            float contactOpacity = (float) steadyAlpha(StarBeamSequence.FIELD_BOTTOM, cutoff);
            band(
                -Math.PI / 2 + CONTACT_GLOW_WIDTH,
                CONTACT_GLOW_WIDTH,
                SHELL_RADIUS + .02,
                .38f,
                .68f,
                .86f,
                contact * (.18f * contactOpacity + .2f * surge * pulseOpacity));
            band(
                CONTACT_LATITUDE,
                .016,
                SHELL_RADIUS,
                .38f,
                .68f,
                .86f,
                .42f * contact * (float) steadyAlpha(FAN_TOP, cutoff));
            GL11.glPopMatrix();
        }
    }

    public static void floorSeal(boolean powered) {
        double floor = StarStructure.MINER_FLOOR_Y - StarStructure.RECEIVER_Y + .515;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, powered ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        if (powered) t.setColorRGBA_F(.77f, .40f, .96f, .66f);
        else t.setColorRGBA_F(.26f, .18f, .30f, .58f);
        circuitArc(t, 11, floor, .065, 0, SEGMENTS);
        circuitArc(t, 18, floor, .085, 0, SEGMENTS);
        int sector = SEGMENTS / 8;
        for (int start = 0; start < SEGMENTS; start += sector) {
            double x = COS[start], z = SIN[start];
            circuitStroke(t, 11 * x, 11 * z, 18 * x, 18 * z, floor, .065);
            // A broken middle track turns out to the rim at both ends of each sector.
            int first = start + 2, last = start + sector - 2;
            circuitArc(t, 14.5, floor, .055, first, last);
            circuitStroke(
                t,
                14.5 * COS[first],
                14.5 * SIN[first],
                18 * COS[first - 1],
                18 * SIN[first - 1],
                floor,
                .055);
            circuitStroke(t, 14.5 * COS[last], 14.5 * SIN[last], 18 * COS[last + 1], 18 * SIN[last + 1], floor, .055);
        }
        t.draw();
    }

    private static void circuitArc(Tessellator t, double radius, double y, double width, int from, int to) {
        for (int i = from; i < to; i++) {
            circuitStroke(t, COS[i] * radius, SIN[i] * radius, COS[i + 1] * radius, SIN[i + 1] * radius, y, width);
        }
    }

    private static void circuitStroke(Tessellator t, double x0, double z0, double x1, double z1, double y,
        double width) {
        double length = Math.hypot(x1 - x0, z1 - z0);
        if (length < 1e-6) return;
        double nx = (z1 - z0) * width / length, nz = (x0 - x1) * width / length;
        t.addVertex(x0 - nx, y, z0 - nz);
        t.addVertex(x1 - nx, y, z1 - nz);
        t.addVertex(x1 + nx, y, z1 + nz);
        t.addVertex(x0 + nx, y, z0 + nz);
    }

    private static double beamAngle(double clock) {
        // Both render origins sample the same world clock, independent of packet arrival and machine restart.
        return (clock % 600) * .6;
    }

    private static double shutdownHeight(StarBeamSequence sequence, double clock, double lowerLength) {
        return -DISSOLVE_EDGE + (lowerLength + FAN_TOP + DISSOLVE_EDGE) * sequence.shutdownProgress(clock);
    }

    private static double steadyAlpha(double height, double cutoff) {
        return clamp((height - cutoff) / DISSOLVE_EDGE);
    }

    /** Two batched passes draw the whole stack. Every surge follows the beam/ripple clock, including its tail. */
    private static void coils(double extension, double pulseAge, double lowerLength, double clock, double cutoff,
        float pulseOpacity, boolean core) {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        for (int i = 0; i < COIL_HEIGHTS.length; i++) {
            double reveal = clamp(extension * COIL_HEIGHTS.length - i);
            if (reveal <= 0) break;
            double y = COIL_HEIGHTS[i];
            double height = (y - LENS_TOP) / (FAN_TOP - LENS_TOP);
            double surge = coilSurge(pulseAge, lowerLength, y);
            // Keep the final collar on the sphere. The lower coils breathe slightly as the light packet passes.
            double radius = COIL_RADII[i] + (i == COIL_HEIGHTS.length - 1 ? 0 : .16 * surge);
            double width = (.12 + .04 * height + .06 * surge) * (core ? 1 : 2.5);
            double opacity = steadyAlpha(y, cutoff);
            t.setColorRGBA_F(
                (float) ((core ? 1 : .95) - .4 * height),
                (float) (core ? .7 + .08 * height : .2 + .4 * height),
                (float) (core ? .34 + .58 * height : .26 + .58 * height),
                (float) (((core ? .45 : .14) * opacity + surge * (core ? .4 : .23) * pulseOpacity) * reveal));
            appendSeal(t, radius, width, y, i, clock);
        }
        t.draw();
    }

    private static double coilSurge(double age, double lowerLength, double y) {
        if (age < 0) return 0;
        if (y > StarBeamSequence.FIELD_BOTTOM) {
            if (age < StarBeamSequence.PULSE_TRAVEL) return 0;
            // Above the bottom contact, use the same latitude as the sphere ripple, not a second timer.
            double latitude = Math.asin((y - SHELL_CENTER) / SHELL_RADIUS);
            return clamp(1 - Math.abs(waveLatitude(age) - latitude) / .08);
        }
        double total = lowerLength + StarBeamSequence.FIELD_BOTTOM;
        double head = age / StarBeamSequence.PULSE_TRAVEL * total - lowerLength;
        double halfLength = total * .075;
        return clamp(1 - Math.abs(head - halfLength - y) / halfLength);
    }

    private static void appendSeal(Tessellator t, double radius, double width, double y, int layer, double clock) {
        boolean hexagon = (layer & 1) != 0;
        double[] xs = hexagon ? HEX_X : COS, zs = hexagon ? HEX_Z : SIN;
        int sector = SEGMENTS / (hexagon ? 6 : 12), gap = hexagon ? 3 : 2;
        int gapStart = (sector - gap) / 2;
        double angle = Math.toRadians(layer * 30 + (hexagon ? -1 : 1) * ((clock * (.1 + .025 * (layer % 3))) % 360));
        double cos = Math.cos(angle), sin = Math.sin(angle);
        for (int i = 0; i < SEGMENTS; i++) {
            int local = i % sector;
            if (local >= gapStart && local < gapStart + gap) continue;
            double x0 = xs[i] * cos - zs[i] * sin, z0 = xs[i] * sin + zs[i] * cos;
            double x1 = xs[i + 1] * cos - zs[i + 1] * sin, z1 = xs[i + 1] * sin + zs[i + 1] * cos;
            t.addVertex(x0 * (radius - width), y, z0 * (radius - width));
            t.addVertex(x1 * (radius - width), y, z1 * (radius - width));
            t.addVertex(x1 * (radius + width), y, z1 * (radius + width));
            t.addVertex(x0 * (radius + width), y, z0 * (radius + width));
        }
    }

    private static void pulse(StarBeamSequence sequence, double clock, double lowerLength, boolean amplified,
        float opacity) {
        double age = sequence.pulseAge(clock);
        if (age < 0) return;
        double total = lowerLength + StarBeamSequence.FIELD_BOTTOM;
        double head = age / StarBeamSequence.PULSE_TRAVEL * total;
        double offset = amplified ? lowerLength : 0;
        double lo = Math.max(amplified ? CORE_TOP : 0, head - total * .15 - offset);
        // The input beam ends at the core's bottom face; transit through the opaque block keeps its original timing.
        double hi = Math.min(amplified ? StarBeamSequence.FIELD_BOTTOM : lowerLength - .5, head - offset);
        if (lo >= hi) return;
        if (amplified) {
            upperTube(lo, hi, .54 * 1.7, 1, .64f, .28f, .68f * opacity);
            upperTube(lo, hi, .23 * 1.7, 1, .86f, .58f, .85f * opacity);
        } else {
            tube(lo, hi, .54, 1, .64f, .28f, .68f * opacity);
            tube(lo, hi, .23, 1, .86f, .58f, .85f * opacity);
        }
    }

    /** Blend the rotating square column into a round footprint on the shell; one fixed-size pass per beam layer. */
    private static void upperTube(double lo, double hi, double radius, float red, float green, float blue,
        float alpha) {
        upperTube(lo, hi, radius, red, green, blue, alpha, Double.NEGATIVE_INFINITY);
    }

    private static void upperTube(double lo, double hi, double radius, float red, float green, float blue, float alpha,
        double cutoff) {
        lo = Math.max(CORE_TOP, lo);
        if (hi <= lo || alpha <= 0) return;
        fadingTube(lo, Math.min(hi, COUPLING_START), radius, red, green, blue, alpha, cutoff);
        if (hi <= COUPLING_START) return;
        double span = StarBeamSequence.FIELD_BOTTOM - COUPLING_START;
        double from = clamp((lo - COUPLING_START) / span), to = clamp((hi - COUPLING_START) / span);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        for (int step = 0; step < COUPLING_STEPS; step++) {
            double lower = Math.max(from, step / (double) COUPLING_STEPS);
            double upper = Math.min(to, (step + 1.0) / COUPLING_STEPS);
            if (upper <= lower) continue;
            for (int i = 0; i < SEGMENTS; i += 2) {
                couplingVertex(t, i, lower, radius, red, green, blue, alpha, cutoff);
                couplingVertex(t, i + 2, lower, radius, red, green, blue, alpha, cutoff);
                couplingVertex(t, i + 2, upper, radius, red, green, blue, alpha, cutoff);
                couplingVertex(t, i, upper, radius, red, green, blue, alpha, cutoff);
            }
        }
        t.draw();
    }

    private static void couplingVertex(Tessellator t, int angle, double fraction, double radius, float red, float green,
        float blue, float alpha, double cutoff) {
        double blend = fraction * fraction * (3 - 2 * fraction);
        double square = radius / (Math.abs(COS[angle]) + Math.abs(SIN[angle]));
        double r = square + (radius * COUPLING_SPREAD - square) * blend;
        double shellY = SHELL_CENTER - Math.sqrt(SHELL_RADIUS * SHELL_RADIUS - r * r);
        double y = COUPLING_START + (shellY - COUPLING_START) * fraction;
        t.setColorRGBA_F(
            (float) (red + (.38 - red) * blend),
            (float) (green + (.68 - green) * blend),
            (float) (blue + (.86 - blue) * blend),
            (float) (alpha * (1 - blend) * steadyAlpha(y, cutoff)));
        t.addVertex(COS[angle] * r, y, SIN[angle] * r);
    }

    /** Called at the sphere center, after drawing the transparent containment shell. */
    public static void ripple(TileStarContainment star, double clock) {
        double age = star.beam.pulseAge(clock);
        if (age < StarBeamSequence.PULSE_TRAVEL) return;
        double wave = age - StarBeamSequence.PULSE_TRAVEL;
        double travel = StarBeamSequence.BOTTOM_CHARGE + StarBeamSequence.SWEEP;
        double theta = waveLatitude(age);
        float opacity = (float) star.beam.opacity(clock);
        // Begin spreading on contact. Fade while moving, without holding a white cap at either pole.
        opacity *= (float) (clamp(wave / StarBeamSequence.BOTTOM_CHARGE)
            * clamp((travel + StarBeamSequence.CREST_FADE - wave) / (2 * StarBeamSequence.CREST_FADE)));
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        band(theta, .045, SHELL_RADIUS + .06, .3f, .66f, .86f, .38f * opacity);
    }

    private static double waveLatitude(double age) {
        return -Math.PI / 2 + Math.PI
            * clamp((age - StarBeamSequence.PULSE_TRAVEL) / (StarBeamSequence.BOTTOM_CHARGE + StarBeamSequence.SWEEP));
    }

    private static void band(double theta, double halfWidth, double radius, float red, float green, float blue,
        float alpha) {
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        // Two adjacent strips give the ripple soft edges without overlapping additive shells.
        for (int strip = 0; strip < 2; strip++) {
            double lower = Math.max(-Math.PI / 2, theta + (strip - 1) * halfWidth);
            double upper = Math.min(Math.PI / 2, theta + strip * halfWidth);
            if (lower >= upper) continue;
            double r0 = radius * Math.cos(lower), r1 = radius * Math.cos(upper);
            double y0 = radius * Math.sin(lower), y1 = radius * Math.sin(upper);
            float a0 = strip == 0 ? 0 : alpha, a1 = strip == 0 ? alpha : 0;
            for (int i = 0; i < SEGMENTS; i++) {
                t.setColorRGBA_F(red, green, blue, a0);
                t.addVertex(COS[i] * r0, y0, SIN[i] * r0);
                t.addVertex(COS[i + 1] * r0, y0, SIN[i + 1] * r0);
                t.setColorRGBA_F(red, green, blue, a1);
                t.addVertex(COS[i + 1] * r1, y1, SIN[i + 1] * r1);
                t.addVertex(COS[i] * r1, y1, SIN[i] * r1);
            }
        }
        t.draw();
    }

    private static void fadingTube(double lo, double hi, double radius, float red, float green, float blue, float alpha,
        double cutoff) {
        lo = Math.max(lo, cutoff);
        if (hi <= lo) return;
        double edge = Math.min(hi, cutoff + DISSOLVE_EDGE);
        if (edge > lo) {
            tube(
                lo,
                edge,
                radius,
                red,
                green,
                blue,
                alpha * (float) steadyAlpha(lo, cutoff),
                alpha * (float) steadyAlpha(edge, cutoff));
            lo = edge;
        }
        tube(lo, hi, radius, red, green, blue, alpha);
    }

    private static void tube(double lo, double hi, double radius, float red, float green, float blue, float alpha) {
        tube(lo, hi, radius, red, green, blue, alpha, alpha);
    }

    private static void tube(double lo, double hi, double radius, float red, float green, float blue, float lowerAlpha,
        float upperAlpha) {
        if (hi <= lo) return;
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        for (int side = 0; side < 4; side++) {
            int a = side * SEGMENTS / 4, b = (side + 1) * SEGMENTS / 4;
            t.setColorRGBA_F(red, green, blue, lowerAlpha);
            t.addVertex(COS[a] * radius, lo, SIN[a] * radius);
            t.addVertex(COS[b] * radius, lo, SIN[b] * radius);
            t.setColorRGBA_F(red, green, blue, upperAlpha);
            t.addVertex(COS[b] * radius, hi, SIN[b] * radius);
            t.addVertex(COS[a] * radius, hi, SIN[a] * radius);
        }
        t.draw();
    }

    private static double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }
}
