package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class DvhItem {
    private Optional<String> dvhType = Optional.empty();
    private Optional<String> doseUnits = Optional.empty();
    private Optional<String> doseType = Optional.empty();
    private Optional<Double> dvhDoseScaling = Optional.empty();
    private Optional<String> dvhVolumeUnits = Optional.empty();
    private Optional<Double[]> dvhData = Optional.empty();
    private Optional<List<DVHReferencedROIItem>> dvhReferencedROISequence = Optional.empty();
    private Optional<Integer> dvhNumberOfBins = Optional.empty();
    private Optional<Double> dvhMinimumDose = Optional.empty();
    private Optional<Double> dvhMaximumDose = Optional.empty();
    private Optional<Double> dvhMeanDose = Optional.empty();
}
