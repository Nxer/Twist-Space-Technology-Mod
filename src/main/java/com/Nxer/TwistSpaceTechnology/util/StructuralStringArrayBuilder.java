package com.Nxer.TwistSpaceTechnology.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

/**
 * This utility class provides a better to create String arrays with multiple repeating elements, for example,
 * multiblock structure.
 * <p>
 * <b>This API is experimental and requires unit-tests!</b>
 *
 * @author Taskeren
 */
@ApiStatus.Experimental
public class StructuralStringArrayBuilder {

    private StructuralStringArrayBuilder() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] ofInternal(Class<T> clazz, Object... input) {
        // calculate the length of the array
        var size = 0;
        for (int i = 0, inputLength = input.length; i < inputLength; i++) {
            Object value = input[i];
            if (clazz.isInstance(value)) {
                size += 1;
            } else if (value instanceof Integer x) {
                size += x - 1;
            } else {
                throw new IllegalArgumentException("Unexpected input: "
                    + value
                    + " ("
                    + value.getClass()
                    + ")"
                    + " at index "
                    + i
                    + ", expected "
                    + clazz.getSimpleName()
                    + " or Integer");
            }
        }

        // prepare the array
        var ret = (T[]) java.lang.reflect.Array.newInstance(clazz, size);
        var ind = 0;

        // push the items into the array
        var iter = Arrays.asList(input)
            .listIterator();
        while (iter.hasNext()) {
            var v1 = iter.next();
            System.out.println("= " + v1);
            if (v1 instanceof Integer repeatTime) {
                var v2 = iter.next();
                if (clazz.isAssignableFrom(v2.getClass())) {
                    for (int i = 0; i < repeatTime; i++) {
                        ret[ind++] = (T) v2;
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected element "
                        + v2
                        + " ("
                        + v2.getClass()
                        .getSimpleName()
                        + ")"
                        + " at index "
                        + iter.previousIndex()
                        + ", expected "
                        + clazz.getSimpleName()
                        + " pattern");
                }
            } else if (clazz.isAssignableFrom(v1.getClass())) {
                ret[ind++] = (T) v1;
            } else {
                throw new IllegalArgumentException("Unexpected element "
                    + v1
                    + " ("
                    + v1.getClass()
                    .getSimpleName()
                    + ")"
                    + " at index "
                    + iter.previousIndex()
                    + ", expected "
                    + clazz.getSimpleName()
                    + " pattern or Integer repeat time");

            }
        }

        // return the array
        return ret;
    }

    /**
     * Build a String array with specified syntax.
     * <p>
     * The actual type if parameter arr is {@link String} or {@link Integer}.
     * <h2>How to Use</h2>
     * You can pass two types of parameters:
     * <ol>
     *     <li>A simple string pattern: {@code "this is a simple string"}</li>
     *     <li>Two parameters combined together: {@code 5, "this string is going to be repeated 5 times"}</li>
     * </ol>
     * <h2>Examples</h2>
     * For example, you call this like {@code of("Hi!", "I'm Jack", 3, "J", 2, "A", "C", "K", 4, "!")},
     * is going to return you an array like {@code ["Hi!", "I'm Jack", "J", "J", "J", "A", "A", "C", "K", "!", "!", "!", "!"]}.
     */
    public static String[] of(Object... arr) {
        return ofInternal(String.class, arr);
    }

    /**
     * Build an array of String array with specified syntax.
     * <p>
     * The actual type if parameter arr is {@link String[]} or {@link Integer}.
     * <p>
     * For details, you should see {@link #of(Object...)}. The only difference is the type changed from String to
     * String[].
     *
     * @see #of(Object...)
     */
    public static String[][] ofArrays(Object... arr) {
        return ofInternal(String[].class, arr);
    }

}
