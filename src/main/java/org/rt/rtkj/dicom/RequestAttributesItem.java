package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RequestAttributesItem {
    private Optional<String> accessionNumber = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedStudySequence = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> requestedProcedureDescription = Optional.empty();
    private Optional<List<ReducedCodeItem>> requestedProcedureCodeSequence = Optional.empty();
    private Optional<String> scheduledProcedureStepID = Optional.empty();
    private Optional<String> requestedProcedureID = Optional.empty();
}
