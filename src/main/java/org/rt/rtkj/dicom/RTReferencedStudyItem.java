package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RTReferencedStudyItem extends ReferencedSOPClassInstanceItem {
    private List<RTReferencedSeriesItem> rtReferencedSeriesSequence = new ArrayList<>();
}
