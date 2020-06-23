package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class MatrixRegistrationItem {
    private Option<List<MatrixItem>> matrixSequence = Option.empty();
    private Option<List<RegistrationTypeCodeItem>> registrationTypeCodeSequence = Option.empty();
}
