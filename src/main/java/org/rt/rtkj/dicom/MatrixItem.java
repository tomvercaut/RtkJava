package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.Optional;

@Data
public class MatrixItem {
    private Optional<TransformationMatrixType> frameOfReferenceTransformationMatrixType = Optional.empty();
    private Optional<Double[]> frameOfReferenceTransformationMatrix = Optional.empty();
}
