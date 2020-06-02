package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ROIPhysicalPropertiesItem {
    private Optional<String> rOIPhysicalProperty = Optional.empty();
    private Optional<Double> rOIPhysicalPropertyValue = Optional.empty();
    private Optional<List<ROIElementalCompositionItem>> rOIElementalCompositionSequence = Optional.empty();
}
