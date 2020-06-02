package org.rt.rtkj.dicom;

import java.util.Optional;

public interface HasImagePositionPatient {
    Optional<Double[]> getImagePositionPatient();
}
