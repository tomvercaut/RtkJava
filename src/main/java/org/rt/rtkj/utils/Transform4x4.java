package org.rt.rtkj.utils;

import org.ejml.simple.SimpleMatrix;

public class Transform4x4 {
    SimpleMatrix mat;

    public Transform4x4() {
        mat = new SimpleMatrix(4, 4);
    }

    public SimpleMatrix apply(SimpleMatrix p) {
        return mat.mult(p);
    }

    public Transform4x4 invert() {
        Transform4x4 tf = new Transform4x4();
        tf.mat = mat.invert();
        return tf;
    }
}
