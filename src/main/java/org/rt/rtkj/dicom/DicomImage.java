package org.rt.rtkj.dicom;

import java.util.List;

public interface DicomImage<T> extends HasPixelRepresentation {
    String getFrameOfReferenceUID();

    Modality getModality();

    PatientPosition getPatientPosition();

    double[] getImagePositionPatient();

    double[] getImageOrientationPatient();

    int getRows();

    int getColumns();

    double[] getPixelSpacing();

    int getBitsAllocated();

    int getBitsStored();

    int getHighBit();

    double getRescaleIntercept();

    double getRescaleSlope();

    List<T> getPixelData();
}
