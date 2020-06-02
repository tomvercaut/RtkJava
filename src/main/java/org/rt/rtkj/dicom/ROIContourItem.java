package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ROIContourItem {
    private Optional<Integer[]> rOIDisplayColor = Optional.empty();
    private Optional<List<ContourItem>> contourSequence = Optional.empty() ;
    private Optional<Integer> referencedROINumber = Optional.empty();
}
