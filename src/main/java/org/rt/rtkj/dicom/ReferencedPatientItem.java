package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.List;

@Data
public class ReferencedPatientItem {
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private List<CodeItem> purposeOfReferenceCodeSequence;
}