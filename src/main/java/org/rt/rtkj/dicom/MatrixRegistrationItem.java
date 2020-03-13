package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatrixRegistrationItem {
    private List<MatrixItem> matrixSequence = new ArrayList<>();
    private List<RegistrationTypeCodeItem> registrationTypeCodeSequence = new ArrayList<>();
}
