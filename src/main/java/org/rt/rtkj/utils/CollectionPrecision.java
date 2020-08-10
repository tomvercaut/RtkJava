package org.rt.rtkj.utils;

import org.apache.commons.math3.util.Precision;
import org.rt.rtkj.Option;

import java.util.List;

public class CollectionPrecision {

    /**
     * Returns true if the content of the lists are equal or within the range of allowed error (1 ULP).
     *
     * @param l1 first list
     * @param l2 second list
     * @return {@code true} if both list elements are equal within the range of allowed error.
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
     * @return {@code true} if both list elements are equal within the range of allowed error.
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
     * @return {@code true} if both list elements are equal within the range of allowed error.
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
     * @return {@code true} if both list elements are equal within the range of allowed error.
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
     * @param ol1 first array
     * @param ol2 second array
     * @return {@code true} if both array elements are equal within the range of allowed error.
     * @see Precision#equals(double, double)
     */
    public static boolean equalsDoubles(Option<Double[]> ol1, Option<Double[]> ol2) {
        if (ol1 == null && ol2 == null) return true;
        if (ol1 == null || ol2 == null) return false;
        if (ol1.isEmpty() && ol2.isEmpty()) return true;
        if ((ol1.isPresent() && ol2.isEmpty()) || (ol1.isEmpty() && ol2.isPresent())) return false;
        var l1 = ol1.get();
        var l2 = ol2.get();
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
     * @param l1 first array
     * @param l2 second array
     * @return {@code true} if both array elements are equal within the range of allowed error.
     * @see Precision#equals(double, double)
     */
    public static boolean equalsDoubles(Double[] l1, Double[] l2) {
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
     * @param l1 first array
     * @param l2 second array
     * @return {@code true} if both array elements are equal within the range of allowed error.
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
     * @return {@code true} if both array elements are equal within the range of allowed error.
     * @see Precision#equals(double, double, double)
     */
    public static boolean equalsDoubles(Double[] l1, Double[] l2, double eps) {
        if (l1 == null && l2 == null) return true;
        if (l1 == null || l2 == null) return false;
        int n = l1.length;
        if (n != l2.length) return false;
        for (int i = 0; i < n; i++) {
            if (!Precision.equals(l1[i], l2[i], eps)) return false;
        }
        return true;
    }

    /**
     * Return true if the content of the arrays are equal or within the range of allowed error.
     *
     * @param l1  first array
     * @param l2  second array
     * @param eps Amount of allowed absolute error.
     * @return {@code true} if both array elements are equal within the range of allowed error.
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

    /**
     * Return true if the content of the transform matrices is equal or within the range of allowed error.
     *
     * @param tm1 first transform matrix
     * @param tm2 second transform matrix
     * @return true if both transform matrices are equal within the range of allowed error.
     */
    public static boolean equals(Transform4x4 tm1, Transform4x4 tm2) {
        if (tm1 == null && tm2 == null) return true;
        if (tm1 == null || tm2 == null) return false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                var optVal1 = tm1.get(i, j);
                var optVal2 = tm2.get(i, j);
                if ((optVal1.isPresent() && optVal2.isEmpty()) || (optVal1.isEmpty() && optVal2.isPresent()))
                    return false;
                if (optVal1.isEmpty() && optVal2.isEmpty()) return false;
                var v1 = optVal1.get();
                var v2 = optVal2.get();
                if (!Precision.equals(v1, v2)) return false;
            }
        }
        return true;
    }

    /**
     * Return true if the content of the transform matrices is equal or within the range of allowed error.
     *
     * @param tm1 first transform matrix
     * @param tm2 second transform matrix
     * @param eps amount of allowed absolute error.
     * @return true if both transform matrices are equal within the range of allowed error.
     */
    public static boolean equals(Transform4x4 tm1, Transform4x4 tm2, double eps) {
        if (tm1 == null && tm2 == null) return true;
        if (tm1 == null || tm2 == null) return false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                var optVal1 = tm1.get(i, j);
                var optVal2 = tm2.get(i, j);
                if ((optVal1.isPresent() && optVal2.isEmpty()) || (optVal1.isEmpty() && optVal2.isPresent()))
                    return false;
                if (optVal1.isEmpty() && optVal2.isEmpty()) return false;
                var v1 = optVal1.get();
                var v2 = optVal2.get();
                if (!Precision.equals(v1, v2, eps)) return false;
            }
        }
        return true;
    }
}
