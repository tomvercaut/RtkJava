package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ROIPhysicalPropertiesItem {
    private Option<String> rOIPhysicalProperty = Option.empty();
    private Option<Double> rOIPhysicalPropertyValue = Option.empty();
    private Option<List<ROIElementalCompositionItem>> rOIElementalCompositionSequence = Option.empty();
}
