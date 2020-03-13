package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ROIContourItem {
    private int[] rOIDisplayColor;
    private List<ContourItem> contourSequence = new ArrayList<>();
    private int referencedROINumber;
}
