package org.rt.rtkj.utils;

import org.ejml.simple.SimpleMatrix;

public class Transform4x4 {
    public SimpleMatrix mat;

    /**
     * Constructor initialises all elements in the transformation matrix to zero.
     */
    public Transform4x4() {
        mat = new SimpleMatrix(4, 4);
        zero();
    }

    public SimpleMatrix apply(SimpleMatrix p) {
        return mat.mult(p);
    }

    public Transform4x4 invert() {
        Transform4x4 tf = new Transform4x4();
//        SimpleSVD<SimpleMatrix> svd = new SimpleSVD<>(this.tm, false);
        tf.mat = mat.invert();
        return tf;
    }

    public void zero() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat.set(i, j, 0.0);
            }
        }
    }
}
