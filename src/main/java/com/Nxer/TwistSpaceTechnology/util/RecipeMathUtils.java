package com.Nxer.TwistSpaceTechnology.util;

public class RecipeMathUtils {

    // Some mathematical methods used when writing recipes or machine processing

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
        return ((number + base - 1) / base) * base;
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
}
