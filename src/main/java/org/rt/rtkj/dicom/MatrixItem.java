package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

@Data
public class MatrixItem {
    private Option<TransformationMatrixType> frameOfReferenceTransformationMatrixType = Option.empty();
    private Option<Double[]> frameOfReferenceTransformationMatrix = Option.empty();
}
