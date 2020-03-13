package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationItem {
    private List<ReferencedSOPClassInstanceItem> ReferencedImageSequence = new ArrayList<>();
    private String frameOfReferenceUID;
    private List<MatrixRegistrationItem> matrixRegistrationSequence = new ArrayList<>();
}
