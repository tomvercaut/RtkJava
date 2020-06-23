package org.rt.rtkj.utils;

import org.rt.rtkj.Option;

public class OptUtils {

    /**
     * Compare if two optional values are equal. They are equal if both are empty or if the values of optionals are equal.
     *
     * @param a   first optional value
     * @param b   second optional value
     * @param <T> type of value
     * @return True if both optionals are empty or if the value of both optionals is equal, false otherwise.
     */
    public static <T> boolean equalsIfPresent(Option<T> a, Option<T> b) {
        if (a.isEmpty() && b.isEmpty()) return true;
        return a.isPresent() && b.isPresent() && a.get().equals(b.get());
    }

    /**
     * Compare if two optional values are equal. Both are considered equal if they have values present in the optional and if their values are equal.
     *
     * @param a   first optional value
     * @param b   second optional value
     * @param <T> type of value
     * @return True if both optionals are present and their values values are equal, false otherwise.
     */
    public static <T> boolean equalsNotEmpty(Option<T> a, Option<T> b) {
        return a.isPresent() && equalsIfPresent(a, b);
    }
}
