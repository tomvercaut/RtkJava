package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class MatrixRegistrationItem {
    private Optional<List<MatrixItem>> matrixSequence = Optional.empty();
    private Optional<List<RegistrationTypeCodeItem>> registrationTypeCodeSequence = Optional.empty();
}
