package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class RequestAttributesItem {
    private Option<String> accessionNumber = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> referencedStudySequence = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> requestedProcedureDescription = Option.empty();
    private Option<List<ReducedCodeItem>> requestedProcedureCodeSequence = Option.empty();
    private Option<String> scheduledProcedureStepID = Option.empty();
    private Option<String> requestedProcedureID = Option.empty();
}
