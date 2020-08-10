package org.rt.rtkj.utils;

import org.junit.jupiter.api.Test;

class Transform4x4Test {

    @Test
    void apply() {
        var tm = Transform4x4.fromDicom(new double[]{
                0.9991632, 0.04040774, 0.006344315, -8.238469,
                -0.03957824, 0.9942595, -0.09940596, -26.36613,
                -0.01032467, 0.09907167, 0.9950267, 179.1217,
                0, 0, 0, 1
        });
        var itm = tm.invert();
        var c1 = new double[]{
                -45.15591004116277,
                -3.5583975515800442,
                115.93701355855103, 1.0};
        var tc = tm.apply(c1);
        var itc = itm.apply(c1);

        System.out.println("tm = " + System.lineSeparator() + tm);
        System.out.println("itm = " + System.lineSeparator() + itm);
        System.out.println("c1 = " + c1.toString());
        System.out.println("tc = " + System.lineSeparator() + tc);
        System.out.println("itc = " + System.lineSeparator() + itc);

        //TODO complete the validation
    }

    @Test
    void invert() {
        final double values[][] = {
                {
                        1, 0, 0, -76.89132,
                        0, 1, 0, -40.52152,
                        0, 0, 1, -40.24639,
                        0, 0, 0, 1
                },
                {
                        0.9991632, 0.04040774, 0.006344315, -8.238469,
                        -0.03957824, 0.9942595, -0.09940596, -26.36613,
                        -0.01032467, 0.09907167, 0.9950267, 179.1217,
                        0, 0, 0, 1
                }
        };
        final double expValues[][] =
                {
                        {
                                1, 0, 0, 76.89132,
                                0, 1, 0, 40.52152,
                                0, 0, 1, 40.24639,
                                0, 0, 0, 1
                        },
                        {
                                0.9991630638714991, -0.03957823831164073, -0.010324665647709088, 9.037420613903917,
                                0.04040773927922751, 0.9942595647930151, 0.09907168028858579, 8.801787051340074,
                                0.006344318900532503, -0.09940596342516865, 0.9950267710589379, -180.7995698574429,
                                0, 0, 0, 1
                        }
                };
        int n = values.length;
        for (int k = 0; k < n; k++) {
            Transform4x4 tm = new Transform4x4();
            Transform4x4 check = new Transform4x4();
            int c = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tm.set(i, j, values[k][c]);
                    check.set(i, j, expValues[k][c]);
                    c++;
                }
            }
            var itm = tm.invert();

            System.out.println("tm:" + System.lineSeparator() + tm.toString());
            System.out.println("itm:" + System.lineSeparator() + itm);
            assert (CollectionPrecision.equals(itm, check, 1e-7));
        }
    }
}