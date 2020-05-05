package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RadiopharmaceuticalInformationItem {
    private String radiopharmaceutical;
    private Optional<LocalTime> radiopharmaceuticalStartTime;
    private double radionuclideTotalDose;
    private double radionuclideHalfLife;
    private double radionuclidePositronFraction;
    private Optional<LocalDateTime> radiopharmaceuticalStartDateTime;
    private List<CodeItem> radionuclideCodeSequence = new ArrayList<>();
}
