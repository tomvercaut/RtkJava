package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ReferencedFrameOfReferenceItem {
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<List<RTReferencedStudyItem>> rtReferencedStudySequence = Option.empty();
}
