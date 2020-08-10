package org.rt.rtkj.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.simple.SimpleMatrix;
import org.rt.rtkj.Option;

import java.util.Objects;

/**
 * A 4x4 affine transformation matrix.
 */
public class Transform4x4 {
    private SimpleMatrix mat;

    /**
     * Constructor initialises all elements in the transformation matrix to zero.
     */
    public Transform4x4() {
        mat = new SimpleMatrix(4, 4);
        zero();
    }

    /**
     * Convert an array of values with a length of 16, into a transformation matrix.
     * The order of these values are in row-major (corresponding to the DICOM standard).
     *
     * @param values row-major array of values.
     * @return Transformation matrix.
     */
    public static Transform4x4 fromDicom(Double[] values) {
        Objects.requireNonNull(values);
        return fromDicom(ArrayUtils.toPrimitive(values));
    }

    /**
     * Convert an array of values with a length of 16, into a transformation matrix.
     * The order of these values are in row-major (corresponding to the DICOM standard).
     *
     * @param values row-major array of values.
     * @return Transformation matrix.
     */
    public static Transform4x4 fromDicom(double[] values) {
        Objects.requireNonNull(values);
        if (values.length != 16) {
            throw new IllegalArgumentException("Number of values has to be equal to 16 [4x4 matrix] as in a DICOM transformation matrix (e.g. (3006,00c6)).");
        }
        Transform4x4 tm = new Transform4x4();
        int c = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tm.set(i, j, values[c]);
                c++;
            }
        }
        return tm;
    }

    /**
     * Apply a transformation matrix to a coordinate.
     *
     * @param p vector with 4 elements
     * @return Vector with the transformed coordinates.
     */
    public SimpleMatrix apply(SimpleMatrix p) {
        return mat.mult(p);
    }

    /**
     * Apply a transformation matrix to a coordinate.
     *
     * @param x value
     * @param y value
     * @param z value
     * @return Vector with the transformed coordinates.
     */
    public SimpleMatrix apply(double x, double y, double z) {
        return apply(x, y, z, 1.0);
    }

    /**
     * Apply a transformation matrix to a coordinate.
     *
     * @param x value
     * @param y value
     * @param z value
     * @param w value
     * @return Vector with the transformed coordinates.
     */
    public SimpleMatrix apply(double x, double y, double z, double w) {
        SimpleMatrix sm = new SimpleMatrix(4, 1);
        sm.set(0, 0, x);
        sm.set(1, 0, y);
        sm.set(2, 0, z);
        sm.set(3, 0, w);
        return apply(sm);
    }

    /**
     * Apply a transformation matrix (M) to a vector (v).
     *
     * @param v coordinate vector with a length of 3 or 4
     * @return Vector with the transformed coordinates.
     */
    public SimpleMatrix apply(double[] v) {
        Objects.requireNonNull(v);
        SimpleMatrix sm = new SimpleMatrix(4, 1);
        sm.set(0, 0, v[0]);
        sm.set(1, 0, v[1]);
        sm.set(2, 0, v[2]);
        if (v.length == 4) {
            sm.set(3, 0, v[3]);
        } else {
            sm.set(3, 0, 1.0);
        }
        return apply(sm);
    }

    /**
     * Set the value in the transformation matrix at a given row and column index.
     *
     * @param row   index [0,4[
     * @param col   index [0,4[
     * @param value
     * @return A reference to this transformation matrix.
     */
    public Transform4x4 set(int row, int col, double value) {
        if (mat != null && row >= 0 && col >= 0 && row < 4 && col < 4) mat.set(row, col, value);
        return this;
    }

    /**
     * Get the value in the transformation matrix at a given row and column index.
     *
     * @param row index [0,4[
     * @param col index [0,4[
     * @return An option with the matrix value at the row and column index.
     * If the internal matrix is null or if the indices are out of bound, an empty option is returned.
     */
    public Option<Double> get(int row, int col) {
        if (mat == null && row >= 0 && col >= 0 && row < 4 && col < 4) return Option.empty();
        return Option.of(mat.get(row, col));
    }

    /**
     * Get the value in the transformation matrix at a given row and column index.
     * This function doesn't check if the internal matrix is null or if the indices are not
     * exceeding their bounds.
     *
     * @param row index [0,4[
     * @param col index [0,4[
     * @return Matrix value at the row and column index.
     */
    public double getUnsafe(int row, int col) {
        return mat.get(row, col);
    }

    /**
     * Computes the inverse of the transform.
     * <p>
     * If the matrix could not be inverted then SingularMatrixException is thrown. Even if no exception is thrown
     * the matrix could still be singular or nearly singular.
     *
     * @return The inverse of the transform.
     */
    public Transform4x4 invert() {
        Transform4x4 tf = new Transform4x4();
//        SimpleSVD<SimpleMatrix> svd = new SimpleSVD<>(this.tm, false);
        tf.mat = mat.invert();
        return tf;
    }

    /**
     * Set all elements in the transformation matrix to zero.
     */
    public void zero() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat.set(i, j, 0.0);
            }
        }
    }

    @Override
    public String toString() {
        return "Transform4x4{" +
                "mat=" + mat +
                '}';
    }
}
