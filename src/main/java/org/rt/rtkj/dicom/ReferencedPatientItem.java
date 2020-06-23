package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ReferencedPatientItem {
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<List<CodeItem>> purposeOfReferenceCodeSequence = Option.empty();
}