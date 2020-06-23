package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class StudiesContainingOtherReferencedInstancesItem {
    private Option<List<ReferencedSeriesItem>> referencedSeriesSequence = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
}
