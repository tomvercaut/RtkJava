package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class DvhItem {
    private Option<String> dvhType = Option.empty();
    private Option<String> doseUnits = Option.empty();
    private Option<String> doseType = Option.empty();
    private Option<Double> dvhDoseScaling = Option.empty();
    private Option<String> dvhVolumeUnits = Option.empty();
    private Option<Double[]> dvhData = Option.empty();
    private Option<List<DVHReferencedROIItem>> dvhReferencedROISequence = Option.empty();
    private Option<Integer> dvhNumberOfBins = Option.empty();
    private Option<Double> dvhMinimumDose = Option.empty();
    private Option<Double> dvhMaximumDose = Option.empty();
    private Option<Double> dvhMeanDose = Option.empty();
}
