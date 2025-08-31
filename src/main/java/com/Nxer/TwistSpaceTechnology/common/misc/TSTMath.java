package com.Nxer.TwistSpaceTechnology.common.misc;

public final class TSTMath {

    private TSTMath() {}

    public static final double SQRT2PI = Math.sqrt(2.0d * Math.PI);

    /**
     * Computes base raised to the power of an integer exponent.
     * Typically faster than {@link java.lang.Math#pow(double, double)} when {@code exp} is an integer.
     */
    public static double powInt(double base, int exp) {
        if (exp > 0) return powBySquaring(base, exp);
        if (exp < 0) return 1.0 / powBySquaring(base, -exp);
        return 1.0;
    }

    /**
     * Computes base raised to non-negative integer exponent.
     */
    public static double powBySquaring(double base, int exp) {
        if (base == 2) return (long)1 << exp;
        if (base == 4) return (long)1 << 2 * exp;
        double result = 1.0;
        while (exp > 0) {
            if ((exp & 1) == 1) result *= base;
            base *= base;
            exp >>= 1;
        }
        return result;
    }

    /**
     * Evaluates the value of y for a standard normal distribution
     *
     * @param x The value of x to evaluate
     * @return The value of y
     */
    public static double stdNormDistr(double x) {

        return Math.exp(-0.5 * (x * x)) / SQRT2PI;
    }

    /**
     * Calculates the weighted drop chance using
     *
     * @param x      The value rolled by nextGaussian
     * @param chance the base drop chance
     * @return the weighted drop chance
     */
    public static double getWeightedDropChance(double x, double chance) {
        return Math.max(0L, Math.round(x * chance * 0.6827d + chance)) * stdNormDistr(x);
    }
}
