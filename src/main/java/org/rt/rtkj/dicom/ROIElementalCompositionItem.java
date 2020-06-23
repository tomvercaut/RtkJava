package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

@Data
public class ROIElementalCompositionItem {
    private Option<Integer> roiElementalCompositionAtomicNumber = Option.empty();
    private Option<Double> roiElementalCompositionAtomicMassFraction = Option.empty();
}
