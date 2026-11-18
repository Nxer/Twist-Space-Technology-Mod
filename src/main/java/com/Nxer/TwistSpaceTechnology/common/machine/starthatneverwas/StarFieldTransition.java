package com.Nxer.TwistSpaceTechnology.common.machine.starthatneverwas;

import net.minecraft.nbt.NBTTagCompound;

/** A server-clock envelope shared by recipe admission and both sides of the renderer. */
public final class StarFieldTransition {

    public static final int DURATION = 40;
    private double from, target, start, duration;

    public double sample(double clock, boolean enabled) {
        double value = value(clock);
        double next = enabled ? 1 : 0;
        if (next != target) {
            from = value;
            target = next;
            start = clock;
            duration = DURATION * Math.abs(target - from);
        }
        return value;
    }

    public double value(double clock) {
        if (duration <= 0) return target;
        double t = Math.max(0, Math.min(1, (clock - start) / duration));
        return from + (target - from) * t * t * (3 - 2 * t);
    }

    public boolean isOpen(double clock) {
        return target == 1 && clock >= start + duration;
    }

    public boolean matches(StarFieldTransition other) {
        return from == other.from && target == other.target && start == other.start && duration == other.duration;
    }

    public void copyFrom(StarFieldTransition other) {
        from = other.from;
        target = other.target;
        start = other.start;
        duration = other.duration;
    }

    public void write(NBTTagCompound tag) {
        tag.setDouble("fieldFrom", from);
        tag.setBoolean("fieldOpen", target == 1);
        tag.setDouble("fieldStart", start);
        tag.setDouble("fieldDuration", duration);
    }

    public void read(NBTTagCompound tag) {
        from = Math.max(0, Math.min(1, tag.getDouble("fieldFrom")));
        target = tag.getBoolean("fieldOpen") ? 1 : 0;
        start = tag.getDouble("fieldStart");
        duration = Math.max(0, Math.min(DURATION, tag.getDouble("fieldDuration")));
        if (!Double.isFinite(from) || !Double.isFinite(start) || !Double.isFinite(duration)) {
            from = target = start = duration = 0;
        }
    }
}
