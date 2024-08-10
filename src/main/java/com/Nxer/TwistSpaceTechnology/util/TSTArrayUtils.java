package com.Nxer.TwistSpaceTechnology.util;

import java.lang.reflect.Array;

public class TSTArrayUtils {

    /**
     * Concat two arrays of the same type element.
     * <p>
     * The length of the result array is the sum of these two.
     *
     * @param clazz  the class of the element
     * @param first  the first array
     * @param second the second array
     * @param <T>    the type of the element
     * @return the result array
     */
    public static <T> T[] concat(Class<T> clazz, T[] first, T[] second) {
        // noinspection unchecked
        T[] ret = (T[]) Array.newInstance(clazz, first.length + second.length);

        System.arraycopy(first, 0, ret, 0, first.length);
        System.arraycopy(second, 0, ret, first.length, second.length);
        return ret;
    }

    /**
     * Add an item to the last of the array.
     *
     * @param clazz   the class of the element
     * @param array   the array
     * @param element the item to be added
     * @param <T>     the type of the element
     * @return the result array
     */
    public static <T> T[] concatToLast(Class<T> clazz, T[] array, T element) {
        // noinspection unchecked
        T[] ret = (T[]) Array.newInstance(clazz, array.length + 1);

        System.arraycopy(array, 0, ret, 0, array.length);
        ret[array.length] = element;
        return ret;
    }

}
