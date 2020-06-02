package org.rt.rtkj.dicom;

import java.util.List;
import java.util.Optional;

public interface DicomImage<T> extends HasPixelRepresentation {
    Optional<String> getFrameOfReferenceUID();

    Optional<Modality> getModality();

    Optional<PatientPosition> getPatientPosition();

    Optional<Double[]> getImagePositionPatient();

    Optional<Double[]> getImageOrientationPatient();

    Optional<Integer> getRows();

    Optional<Integer> getColumns();

    Optional<Double[]> getPixelSpacing();

    Optional<Integer> getBitsAllocated();

    Optional<Integer> getBitsStored();

    Optional<Integer> getHighBit();

    Optional<Double> getRescaleIntercept();

    Optional<Double> getRescaleSlope();

    Optional<List<T>> getPixelData();
}
