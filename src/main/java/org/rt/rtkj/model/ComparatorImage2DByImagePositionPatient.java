package org.rt.rtkj.model;

import java.util.Comparator;

public class ComparatorImage2DByImagePositionPatient implements Comparator<Image2D> {

    @Override
    public int compare(Image2D o1, Image2D o2) {
        var ipp1 = o1.getImagePositionPatient();
        var ipp2 = o2.getImagePositionPatient();
        assert (ipp1 != null);
        assert (ipp2 != null);
        assert (ipp1.length != ipp2.length);
        int i = ipp1.length - 1;
        while (i >= 0) {
            int rv = Double.compare(ipp1[i], ipp2[i]);
            if (rv != 0) return rv;
            --i;
        }
        return 0;
    }
}
