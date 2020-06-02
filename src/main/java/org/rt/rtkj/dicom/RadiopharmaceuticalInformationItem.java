package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RadiopharmaceuticalInformationItem {
    private Optional<String> radiopharmaceutical = Optional.empty();
    private Optional<LocalTime> radiopharmaceuticalStartTime = Optional.empty();
    private Optional<Double> radionuclideTotalDose = Optional.empty();
    private Optional<Double> radionuclideHalfLife = Optional.empty();
    private Optional<Double> radionuclidePositronFraction = Optional.empty();
    private Optional<LocalDateTime> radiopharmaceuticalStartDateTime = Optional.empty();
    private Optional<List<CodeItem>> radionuclideCodeSequence = Optional.empty();
}
