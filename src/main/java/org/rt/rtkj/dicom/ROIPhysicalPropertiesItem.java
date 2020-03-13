package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ROIPhysicalPropertiesItem {
    private String rOIPhysicalProperty;
    private String rOIPhysicalPropertyValue;
    private List<ROIElementalCompositionItem> rOIElementalCompositionSequence = new ArrayList<>();
}
