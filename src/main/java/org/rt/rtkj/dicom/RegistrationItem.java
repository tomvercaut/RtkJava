package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RegistrationItem {
    private Optional<List<ReferencedSOPClassInstanceItem>> ReferencedImageSequence = Optional.empty();
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<List<MatrixRegistrationItem>> matrixRegistrationSequence = Optional.empty();
}
