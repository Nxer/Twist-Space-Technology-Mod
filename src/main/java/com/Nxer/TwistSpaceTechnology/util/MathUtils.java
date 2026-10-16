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

    /**
     * Computes the Greatest Common Divisor (GCD) of two integers.
     * Uses the Euclidean algorithm, automatically handles negative numbers, and returns a non-negative result.
     *
     * @param a First integer
     * @param b Second integer
     * @return The greatest common divisor of `a` and `b` (non-negative).
     *
     */
    public static int getGCD(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Computes the Least Common Multiple (LCM) of two integers.
     * Formula: LCM(a, b) = |a * b| / GCD(a, b).
     * Notes:
     * - Returns 0 if either `a` or `b` is 0 (mathematically valid behavior).
     * - May overflow for large values (use `long` for extended range).
     *
     * @param a First integer
     * @param b Second integer
     * @return The least common multiple of `a` and `b` (non-negative), or 0 if either is 0.
     *
     */
    public static int getLCM(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        int gcdValue = getGCD(a, b);
        return Math.abs(a / gcdValue * b);
    }

    /**
     * Rounds up a value to the nearest multiple of a given base.
     *
     * @param base   The base value used for rounding (must be positive).
     * @param number The value to be rounded up.
     * @return The smallest multiple of `base` that is greater than or equal to `number`.
     *
     *         Example: If base = 500 and number = 12300, returns 12500.
     */
    public static int roundUpToMultiple(int base, int number) {
        if (base < 1 || number < 1) return number;
        return (int) (Math.ceil((double) number / base) * base);
    }

    /**
     * n1 approaches n2 with a step of n3. n3 < |n1-n2|, n1 = n2.
     *
     * @param n1 previous number
     * @param n2 target number
     * @param n3 step number, n3 > 0
     * @return modified n1
     */
    public static int numericalApproximation(int n1, int n2, int n3) {
        if (n3 < 0) return n1;
        int delta = n1 - n2;
        int absDelta = Math.abs(delta);
        if (absDelta > n3) {
            n1 += (delta > 0) ? -n3 : n3;
        } else {
            n1 = n2;
        }
        return n1;
    }

    /** Long-valued counterpart for material counts used during recipe composition. */
    public static long getGCD(long left, long right) {
        left = Math.abs(left);
        right = Math.abs(right);
        while (right != 0) {
            long remainder = left % right;
            left = right;
            right = remainder;
        }
        return left;
    }

    /** Least common multiple of non-negative batch sizes, with checked multiplication. */
    public static long getLCM(long left, long right) {
        if (left == 0 || right == 0) return 0;
        return Math.multiplyExact(left / getGCD(left, right), right);
    }

    /** Ceiling division for non-negative counts without overflowing value + divisor - 1. */
    public static long divideCeil(long value, long divisor) {
        if (value < 0 || divisor <= 0) throw new IllegalArgumentException("invalid ceiling division");
        return value / divisor + (value % divisor == 0 ? 0 : 1);
    }

}
