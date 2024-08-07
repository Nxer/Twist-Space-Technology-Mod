package com.Nxer.TwistSpaceTechnology.util;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
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
        var list = new ArrayList<String>();
        var iter = Arrays.asList(arr)
            .listIterator();
        while (iter.hasNext()) {
            var v1 = iter.next();
            if (v1 instanceof Integer repeatTime) {
                var v2 = iter.next();
                if (v2 instanceof String pattern) {
                    for (int i = 0; i < repeatTime; i++) {
                        list.add(pattern);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected element " + v2 + " (" + v2.getClass()
                        .getSimpleName() + ")" + " at index " + iter.previousIndex() + ", expected String pattern");
                }
            } else if (v1 instanceof String pattern) {
                list.add(pattern);
            } else {
                throw new IllegalArgumentException("Unexpected element "
                    + v1
                    + " ("
                    + v1.getClass()
                    .getSimpleName()
                    + ")"
                    + " at index "
                    + iter.previousIndex()
                    + ", expected String pattern or Integer repeat time");

            }
        }
        return list.toArray(new String[0]);
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
        var list = new ArrayList<String[]>();
        var iter = Arrays.asList(arr)
            .listIterator();
        while (iter.hasNext()) {
            var v1 = iter.next();
            if (v1 instanceof Integer repeatTime) {
                var v2 = iter.next();
                if (v2 instanceof String[] pattern) {
                    for (int i = 0; i < repeatTime; i++) {
                        list.add(pattern);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected element " + v2 + " (" + v2.getClass()
                        .getSimpleName() + ")" + " at index " + iter.previousIndex() + ", expected String[] pattern");
                }
            } else if (v1 instanceof String[] pattern) {
                list.add(pattern);
            } else {
                throw new IllegalArgumentException("Unexpected element "
                    + v1
                    + " ("
                    + v1.getClass()
                    .getSimpleName()
                    + ")"
                    + " at index "
                    + iter.previousIndex()
                    + ", expected String[] pattern or Integer repeat time");

            }
        }
        return list.toArray(new String[0][]);
    }

}
