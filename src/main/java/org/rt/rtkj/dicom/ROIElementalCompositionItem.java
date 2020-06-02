package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.Optional;

@Data
public class ROIElementalCompositionItem {
    private Optional<Integer> roiElementalCompositionAtomicNumber = Optional.empty();
    private Optional<Double> roiElementalCompositionAtomicMassFraction = Optional.empty();
}
