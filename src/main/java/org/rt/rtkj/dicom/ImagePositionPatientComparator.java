package org.rt.rtkj.dicom;

import java.util.Comparator;

public class ImagePositionPatientComparator implements Comparator<HasImagePositionPatient> {

    @Override
    public int compare(HasImagePositionPatient o1, HasImagePositionPatient o2) {
        var optIpp1 = o1.getImagePositionPatient();
        var optIpp2 = o2.getImagePositionPatient();
        assert (optIpp1 != null);
        assert (optIpp2 != null);
        if (optIpp1.isEmpty() && optIpp2.isEmpty()) return 0;
        if (!optIpp1.isEmpty() && optIpp2.isEmpty()) return Integer.compare(1, 0);
        if (optIpp1.isEmpty() && !optIpp2.isEmpty()) return Integer.compare(0, 1);
        var ipp1 = optIpp1.get();
        var ipp2 = optIpp2.get();
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
