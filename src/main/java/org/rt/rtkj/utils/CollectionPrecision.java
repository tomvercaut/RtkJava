package org.rt.rtkj.utils;

import org.apache.commons.math3.util.Precision;

import java.util.List;

public class CollectionPrecision {

    /**
     * Returns true if the content of the lists are equal or within the range of allowed error (1 ULP).
     *
     * @param l1 first list
     * @param l2 second list
     * @return {@code true} if both list elements are equal withing the range of allowed error.
     * @see Precision#equals(float, float)
     */
    public static boolean equalsFloat(List<Float> l1, List<Float> l2) {
        return equalsFloat(l1, l2, 1);
    }

    /**
     * Returns true if the content of the lists are equal or within the range of allowed error ({@code maxUlps}).
     *
     * @param l1      first list
     * @param l2      second list
     * @param maxUlps {@code (maxUlps - 1)} is the number of floating point
     *                values between {@code x} and {@code y}.
     * @return {@code true} if both list elements are equal withing the range of allowed error.
     * @see Precision#equals(float, float)
     */
    public static boolean equalsFloat(List<Float> l1, List<Float> l2, float maxUlps) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.size();
        if (n != l2.size()) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1.get(i), l2.get(i), maxUlps)) return false;
        }
        return true;
    }

    /**
     * Return true if the content of the lists are equal or within the range of allowed error.
     *
     * @param l1 first list
     * @param l2 second list
     * @return {@code true} if both list elements are equal withing the range of allowed error.
     * @see Precision#equals(double, double)
     */
    public static boolean equalsDoubles(List<Double> l1, List<Double> l2) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.size();
        if (n != l2.size()) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1.get(i), l2.get(i))) return false;
        }
        return true;
    }

    /**
     * Return true if the content of the lists are equal or within the range of allowed error.
     *
     * @param l1  first list
     * @param l2  second list
     * @param eps Amount of allowed absolute error.
     * @return {@code true} if both list elements are equal withing the range of allowed error.
     * @see Precision#equals(double, double, double)
     */
    public static boolean equalsDoubles(List<Double> l1, List<Double> l2, double eps) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.size();
        if (n != l2.size()) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1.get(i), l2.get(i), eps)) return false;
        }
        return true;
    }

    /**
     * Return true if the content of the arrays are equal or within the range of allowed error.
     *
     * @param l1 first array
     * @param l2 second array
     * @return {@code true} if both array elements are equal withing the range of allowed error.
     * @see Precision#equals(double, double)
     */
    public static boolean equalsDoubles(double[] l1, double[] l2) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.length;
        if (n != l2.length) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1[i], l2[i])) return false;
        }
        return true;
    }

    /**
     * Return true if the content of the arrays are equal or within the range of allowed error.
     *
     * @param l1  first array
     * @param l2  second array
     * @param eps Amount of allowed absolute error.
     * @return {@code true} if both array elements are equal withing the range of allowed error.
     * @see Precision#equals(double, double, double)
     */
    public static boolean equalsDoubles(double[] l1, double[] l2, double eps) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.length;
        if (n != l2.length) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1[i], l2[i], eps)) return false;
        }
        return true;
    }
}
