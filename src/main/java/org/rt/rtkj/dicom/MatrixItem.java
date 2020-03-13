package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class MatrixItem {
    private String frameOfReferenceTransformationMatrixType;
    private double[] frameOfReferenceTransformationMatrix;
}
