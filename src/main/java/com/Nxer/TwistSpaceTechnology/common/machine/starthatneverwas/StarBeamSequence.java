package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import net.minecraft.nbt.NBTTagCompound;

/** One world-clock sequence for the miner beam, receiver fan and bottom-to-top containment ripple. */
public final class StarBeamSequence {

    public static final int BEAM_GROW = 10, FAN_GROW = 10;
    public static final int FIELD_CONNECTION = BEAM_GROW + FAN_GROW;
    public static final int PULSE_TRAVEL = 20, BOTTOM_CHARGE = 10, SWEEP = 60, CREST_FADE = 10;
    public static final int PULSE_ACTIVE = PULSE_TRAVEL + BOTTOM_CHARGE + SWEEP + CREST_FADE;
    public static final int MIN_PULSE_TICKS = 90;
    public static final int BEAM_FADE = 10;
    public static final double FIELD_BOTTOM = 10;
    private long start = -1, stop = -1;
    private long miningClock = -1;
    private int miningTick, miningDuration, resumeTick;
    private boolean advancing;
    private double stoppedMiningTick;

    public boolean request(boolean active, long clock) {
        if (active) {
            if (!visible(clock) || stop >= 0) {
                clear();
                start = clock;
                return true;
            }
        } else if (start >= 0 && stop < 0) {
            stoppedMiningTick = miningProgress(clock);
            stop = clock;
            return true;
        }
        return false;
    }

    /** Checkpoint the paid mining clock, not frame rate. Unchanged ticks do not create extra packets. */
    public void sampleMining(int progress, int duration, boolean advancing, long clock) {
        if (start < 0 || fanExtension(clock) < 1 || duration < 1) return;
        progress = Math.max(0, Math.min(duration, progress));
        if (miningClock >= 0 && miningDuration == duration
            && this.advancing == advancing
            && miningProgress(clock) == progress
            && clock - miningClock < 20) return;
        // After a fault, begin a fresh visual wave without changing the remaining paid work.
        if (miningClock < 0) resumeTick = progress <= 1 ? 0 : progress;
        miningTick = progress;
        miningDuration = duration;
        miningClock = clock;
        this.advancing = advancing;
    }

    public double miningProgress(double clock) {
        if (miningClock < 0) return 0;
        if (stop >= 0) return stoppedMiningTick;
        return Math.min(miningDuration, miningTick + (advancing ? Math.max(0, Math.min(20, clock - miningClock)) : 0));
    }

    public void clear() {
        start = stop = -1;
        miningClock = -1;
        miningTick = miningDuration = resumeTick = 0;
        advancing = false;
        stoppedMiningTick = 0;
    }

    public double age(double clock) {
        return start < 0 ? -1 : Math.max(0, clock - start);
    }

    public double end() {
        if (start < 0) return -1;
        if (stop < 0) return Double.POSITIVE_INFINITY;
        double remaining = miningClock < 0 || stoppedMiningTick <= resumeTick ? 0
            : pulseStart(Math.nextDown(stoppedMiningTick)) + pulseDuration() - stoppedMiningTick;
        return stop + Math.max(FIELD_CONNECTION, Math.max(0, remaining) + BEAM_FADE);
    }

    public boolean visible(double clock) {
        return start >= 0 && clock < end();
    }

    public double opacity(double clock) {
        return visible(clock) ? clamp((end() - clock) / BEAM_FADE) : 0;
    }

    /** A single bottom-to-top wipe for both emitters and the coils; emitted waves keep their own tail. */
    public double shutdownProgress(double clock) {
        return stop < 0 ? 0 : smooth((clock - stop) / FIELD_CONNECTION);
    }

    public double beamExtension(double clock) {
        return smooth(age(stop < 0 ? clock : Math.min(clock, stop)) / BEAM_GROW);
    }

    public double fanExtension(double clock) {
        return smooth((age(stop < 0 ? clock : Math.min(clock, stop)) - BEAM_GROW) / FAN_GROW);
    }

    /** Fit whole waves to the mining interval. Very short jobs finish while their single wave drains. */
    private double pulseDuration() {
        int remaining = Math.max(1, miningDuration - resumeTick);
        int waves = Math.max(1, remaining / PULSE_ACTIVE);
        return Math.max(MIN_PULSE_TICKS, (double) remaining / waves);
    }

    private double pulseStart(double progress) {
        double period = pulseDuration();
        return resumeTick + Math.floor(Math.max(0, progress - resumeTick) / period) * period;
    }

    /** Travel stays one second; only the shell sweep stretches to fit the independent visual period. */
    public double pulseAge(double clock) {
        if (!visible(clock) || miningClock < 0) return -1;
        double progress = miningProgress(clock);
        double cursor = stop < 0 ? progress : Math.nextDown(progress);
        if (cursor < resumeTick || cursor >= miningDuration) return -1;
        double from = pulseStart(cursor);
        double period = pulseDuration();
        if (stop >= 0) progress += Math.max(0, clock - stop);
        double elapsed = progress - from;
        if (elapsed >= period) return -1;
        return elapsed <= PULSE_TRAVEL ? elapsed
            : PULSE_TRAVEL + (elapsed - PULSE_TRAVEL) * (PULSE_ACTIVE - PULSE_TRAVEL) / (period - PULSE_TRAVEL);
    }

    public boolean matches(StarBeamSequence other) {
        return start == other.start && stop == other.stop
            && miningClock == other.miningClock
            && miningTick == other.miningTick
            && miningDuration == other.miningDuration
            && resumeTick == other.resumeTick
            && advancing == other.advancing
            && stoppedMiningTick == other.stoppedMiningTick;
    }

    public void copyFrom(StarBeamSequence other) {
        start = other.start;
        stop = other.stop;
        miningClock = other.miningClock;
        miningTick = other.miningTick;
        miningDuration = other.miningDuration;
        resumeTick = other.resumeTick;
        advancing = other.advancing;
        stoppedMiningTick = other.stoppedMiningTick;
    }

    public void write(NBTTagCompound tag) {
        tag.setLong("beamStart", start);
        tag.setLong("beamStop", stop);
        tag.setLong("beamMiningClock", miningClock);
        tag.setInteger("beamMiningTick", miningTick);
        tag.setInteger("beamMiningDuration", miningDuration);
        tag.setInteger("beamResumeTick", resumeTick);
        tag.setBoolean("beamAdvancing", advancing);
        tag.setDouble("beamStoppedMiningTick", stoppedMiningTick);
    }

    public void read(NBTTagCompound tag) {
        start = tag.hasKey("beamStart") ? tag.getLong("beamStart") : -1;
        stop = tag.hasKey("beamStop") ? tag.getLong("beamStop") : -1;
        miningClock = tag.getLong("beamMiningClock");
        miningDuration = Math.max(0, tag.getInteger("beamMiningDuration"));
        miningTick = Math.max(0, Math.min(miningDuration, tag.getInteger("beamMiningTick")));
        resumeTick = Math.max(0, Math.min(miningTick, tag.getInteger("beamResumeTick")));
        advancing = tag.getBoolean("beamAdvancing");
        stoppedMiningTick = Math.max(0, Math.min(miningDuration, tag.getDouble("beamStoppedMiningTick")));
        if (miningDuration == 0) miningClock = -1;
        if (start < 0) clear();
        else if (stop >= 0) stop = Math.max(start, stop);
    }

    private static double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }

    private static double smooth(double value) {
        double t = clamp(value);
        return t * t * (3 - 2 * t);
    }
}
