package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReferencedFrameOfReferenceItem {
    private String frameOfReferenceUID;
    private List<RTReferencedStudyItem> rtReferencedStudySequence = new ArrayList<>();
}
