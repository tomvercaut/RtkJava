package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class StudiesContainingOtherReferencedInstancesItem {
    private Optional<List<ReferencedSeriesItem>> referencedSeriesSequence = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
}
