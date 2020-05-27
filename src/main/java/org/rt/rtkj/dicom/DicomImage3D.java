package org.rt.rtkj.dicom;

import java.util.Optional;

public interface DicomImage3D<T> extends HasPixelRepresentation {
    Optional<String> getFrameOfReferenceUID();

    Optional<Modality> getModality();

    Optional<PatientPosition> getPatientPosition();

    Optional<double[]> getImagePositionPatient();

    Optional<double[]> getImageOrientationPatient();

    Optional<Integer> getRows();

    Optional<Integer> getColumns();

    Optional<double[]> getPixelSpacing();

    Optional<Integer> getBitsAllocated();

    Optional<Integer> getBitsStored();

    Optional<Integer> getHighBit();

    Optional<String> getStudyInstanceUID();

    Optional<String> getSeriesInstanceUID();
}
