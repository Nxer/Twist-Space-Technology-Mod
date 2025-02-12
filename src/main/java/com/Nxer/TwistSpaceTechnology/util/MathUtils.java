package com.Nxer.TwistSpaceTechnology.util;

import java.math.BigInteger;

/**
 * Copy from com.cleanroommc.modularui.utils.MathUtils
 */
public class MathUtils {

    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(v, max));
    }

    public static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(v, max));
    }

    public static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(v, max));
    }

    public static long clamp(long v, long min, long max) {
        return Math.max(min, Math.min(v, max));
    }

    public static int cycler(int x, int min, int max) {
        return x < min ? max : (x > max ? min : x);
    }

    public static float cycler(float x, float min, float max) {
        return x < min ? max : (x > max ? min : x);
    }

    public static double cycler(double x, double min, double max) {
        return x < min ? max : (x > max ? min : x);
    }

    public static long bigToLong(BigInteger x) {
        long result = x.longValue();
        if (x.equals(BigInteger.valueOf(result))) {
            return result;
        }
        return x.signum() > 0 ? Long.MAX_VALUE : Long.MIN_VALUE;
    }

    public static int bigToInt(BigInteger x) {
        int result = x.intValue();
        if (x.equals(BigInteger.valueOf(result))) {
            return result;
        }
        return x.signum() > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
    }

    public static int gridIndex(int x, int y, int size, int width) {
        x = x / size;
        y = y / size;

        return x + y * width / size;
    }

    public static int gridRows(int count, int size, int width) {
        double x = count * size / (double) width;

        return count <= 0 ? 1 : (int) Math.ceil(x);
    }

    public static int min(int... values) {
        if (values == null || values.length == 0) throw new IllegalArgumentException();
        if (values.length == 1) return values[0];
        if (values.length == 2) return Math.min(values[0], values[1]);
        int min = Integer.MAX_VALUE;
        for (int i : values) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    public static int max(int... values) {
        if (values == null || values.length == 0) throw new IllegalArgumentException();
        if (values.length == 1) return values[0];
        if (values.length == 2) return Math.max(values[0], values[1]);
        int max = Integer.MIN_VALUE;
        for (int i : values) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

}
