package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ReferencedFrameOfReferenceItem {
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<List<RTReferencedStudyItem>> rtReferencedStudySequence = Optional.empty();
}
