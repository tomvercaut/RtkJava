package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestAttributesItem {
    private String accessionNumber;
    private List<ReferencedSOPClassInstanceItem> referencedStudySequence = new ArrayList<>();
    private String studyInstanceUID;
    private String requestedProcedureDescription;
    private List<ReducedCodeItem> requestedProcedureCodeSequence = new ArrayList<>();
    private String scheduledProcedureStepID;
    private String requestedProcedureID;
}
