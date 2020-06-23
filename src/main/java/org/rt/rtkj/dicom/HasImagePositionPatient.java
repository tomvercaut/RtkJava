package org.rt.rtkj.dicom;

import org.rt.rtkj.Option;

public interface HasImagePositionPatient {
    Option<Double[]> getImagePositionPatient();
}
