package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ROIContourItem {
    private Option<Integer[]> rOIDisplayColor = Option.empty();
    private Option<List<ContourItem>> contourSequence = Option.empty();
    private Option<Integer> referencedROINumber = Option.empty();
}
