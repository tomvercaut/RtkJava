package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class ReferencedPatientItem {
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<List<CodeItem>> purposeOfReferenceCodeSequence = Optional.empty();
}