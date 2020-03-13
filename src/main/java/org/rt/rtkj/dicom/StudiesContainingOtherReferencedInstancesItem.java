package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudiesContainingOtherReferencedInstancesItem {
    private List<ReferencedSeriesItem> referencedSeriesSequence = new ArrayList<>();
    private String studyInstanceUID;
}
