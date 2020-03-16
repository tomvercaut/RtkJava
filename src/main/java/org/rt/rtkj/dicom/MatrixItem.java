package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class MatrixItem {
    private TransformationMatrixType frameOfReferenceTransformationMatrixType;
    private double[] frameOfReferenceTransformationMatrix;
}
