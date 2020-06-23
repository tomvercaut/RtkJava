package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class RegistrationItem {
    private Option<List<ReferencedSOPClassInstanceItem>> ReferencedImageSequence = Option.empty();
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<List<MatrixRegistrationItem>> matrixRegistrationSequence = Option.empty();
}
