package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

/** Size-dependent formation on the server clock, independent of drone speed and client frame rate. */
public final class StarMeteorFormation {

    public static final int NUCLEATION_TICKS = 20;
    public static final int STEPS_PER_BLOCK = 4;
    private static final int MAX_GROWTH_TICKS = 10 * 20;

    private StarMeteorFormation() {}

    /** One second for the seed, then size-proportional growth lasting one to ten seconds. */
    public static int duration(int radius) {
        int renderedRadius = StarMath.renderRadius(radius);
        int growth = Math
            .max(20, (MAX_GROWTH_TICKS * renderedRadius + StarMath.MAX_RENDER_RADIUS - 1) / StarMath.MAX_RENDER_RADIUS);
        return NUCLEATION_TICKS + growth;
    }

    /** The single compressed-cobblestone seed condenses before any surrounding ore appears. */
    public static double coreSize(double progress) {
        return smooth(Math.max(0, Math.min(1, progress / NUCLEATION_TICKS)));
    }

    /** A constant-speed growth front, with no acceleration ramp or pause before the laser warmup. */
    public static double size(double progress, int formationTicks) {
        if (formationTicks <= 0) return 1;
        int growth = Math.max(1, formationTicks - NUCLEATION_TICKS);
        return Math.max(0, Math.min(1, (progress - NUCLEATION_TICKS) / growth));
    }

    /** Substeps reveal whole blocks in small patches, without scaling blocks or delaying the final shell. */
    public static int stageAt(int radius, double progress, int formationTicks) {
        return (int) Math.floor(radius * STEPS_PER_BLOCK * size(progress, formationTicks));
    }

    public static double glow(double progress, int formationTicks) {
        if (formationTicks <= 0 || progress >= formationTicks) return 0;
        double arrival = Math.max(0, Math.min(1, progress / 10));
        double departure = Math.max(0, Math.min(1, (formationTicks - progress) / 10));
        return smooth(arrival) * smooth(departure);
    }

    private static double smooth(double value) {
        return value * value * (3 - 2 * value);
    }
}
