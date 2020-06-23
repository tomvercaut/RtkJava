package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class RadiopharmaceuticalInformationItem {
    private Option<String> radiopharmaceutical = Option.empty();
    private Option<LocalTime> radiopharmaceuticalStartTime = Option.empty();
    private Option<Double> radionuclideTotalDose = Option.empty();
    private Option<Double> radionuclideHalfLife = Option.empty();
    private Option<Double> radionuclidePositronFraction = Option.empty();
    private Option<LocalDateTime> radiopharmaceuticalStartDateTime = Option.empty();
    private Option<List<CodeItem>> radionuclideCodeSequence = Option.empty();
}
