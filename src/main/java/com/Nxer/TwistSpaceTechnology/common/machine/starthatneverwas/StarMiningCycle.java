package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

/** One-second render checkpoints; neither these boundaries nor beam waves extend the paid job. */
public final class StarMiningCycle {

    public static final int TICKS = 20;

    public static int start(double progress, int duration) {
        int last = (duration - 1) / TICKS * TICKS;
        return (int) Math.min(last, Math.floor(Math.max(0, progress) / TICKS) * TICKS);
    }

    public static int end(double progress, int duration) {
        return (int) Math.min(duration, (long) start(progress, duration) + TICKS);
    }

    public static int completed(double progress, int duration) {
        return progress >= duration ? duration : start(progress, duration);
    }

    public static long removed(StarMeteorExcavation excavation, int tier, int completed, int duration) {
        return excavation.removed(tier, completedWork(excavation, tier, completed, duration));
    }

    public static long completedWork(StarMeteorExcavation excavation, int tier, int completed, int duration) {
        if (duration < 1) throw new IllegalArgumentException("Invalid meteor mining duration");
        long units = excavation.workUnits(tier);
        return Math.multiplyExact(units, Math.max(0, Math.min(duration, completed))) / duration;
    }

    /** First render checkpoint at which the next native cut has become visible. */
    public static int nextCutTick(StarMeteorExcavation excavation, int tier, long completedWork, int duration) {
        long units = excavation.workUnits(tier);
        if (duration < 1 || completedWork < 0 || completedWork >= units)
            throw new IllegalArgumentException("Invalid meteor cut");
        long tick = (Math.multiplyExact(completedWork + 1, duration) + units - 1) / units;
        return (int) Math.min(duration, (tick + TICKS - 1) / TICKS * TICKS);
    }
}
