package com.Nxer.TwistSpaceTechnology.util;

/**
 * DON'T USE THIS CLASS IF YOU DO NOT KNOW WHAT YOU ARE DOING!
 * <p>
 * This class defines some functions that I (taskeren) think might be useful for calculating.
 *
 * @author Taskeren
 */
public class TaskerenAdvancedMathUtils {

    private TaskerenAdvancedMathUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see #myMagicFunction(double, double, double)
     */
    private static double myMagicFunction_unchecked(double max, double magicConst, double x) {
        return max * (1 - Math.exp(-x / magicConst));
    }

    /**
     * The magic function by Taskeren.
     * <p>
     * This function is <i>monotonically increasing</i>, and as the x grows, the y increment decreases.
     * And it <b>never</b> reaches to the maximum value.
     * <p>
     * If {@code x2 > x1}, then {@code f(x2) - f(x1) < f(x1) - f(0)}.
     * <p>
     * It looks like:
     *
     * <pre>
     * max -------------------------------------------------------------------------
     * |                                                               x
     * |                                       x
     * |                      x
     * |             x
     * |       x
     * |    x
     * |  x
     * | x
     * -----------------------------------------------------------------------------
     * </pre>
     */
    public static double myMagicFunction(double max, double magicConst, double x) {
        if (max <= 0) throw new IllegalArgumentException("Argument 'max' should be greater than zero");
        if (magicConst <= 0) throw new IllegalArgumentException("Argument 'magicConst' should be greater than zero");
        if (x <= 0) throw new IllegalArgumentException("Argument 'x' should be greater than zero");

        return myMagicFunction_unchecked(max, magicConst, x);
    }

    public static double calcBloodyHellSpeedRuneBonus(int speedRuneCount) {
        // the maximum possible speed rune count for max tier (t5) is about 180,
        // so the `speedRuneCount` is between 0 and 180

        // V1 (Deprecated)
        // max bonus: 500%
        // magic const: 55.0
        // - x at half efficiency (≈250%): 38.1
        // - x at almost efficiency (≈475%): 164.7

        // V2
        // max: 16
        // magic: 80
        // =======================
        // half   62.38  => 8x
        // almost 207.23 => 14.4x
        // max    543    => 15.96x

        return myMagicFunction(16, 80, speedRuneCount);
    }

}
