package org.rt.rtkj.dicom;

import org.rt.rtkj.Option;

import java.util.List;

public interface DicomImage<T> extends HasPixelRepresentation {
    Option<String> getFrameOfReferenceUID();

    Option<Modality> getModality();

    Option<PatientPosition> getPatientPosition();

    Option<Double[]> getImagePositionPatient();

    Option<Double[]> getImageOrientationPatient();

    Option<Integer> getRows();

    Option<Integer> getColumns();

    Option<Double[]> getPixelSpacing();

    Option<Integer> getBitsAllocated();

    Option<Integer> getBitsStored();

    Option<Integer> getHighBit();

    Option<Double> getRescaleIntercept();

    Option<Double> getRescaleSlope();

    Option<List<T>> getPixelData();
}
