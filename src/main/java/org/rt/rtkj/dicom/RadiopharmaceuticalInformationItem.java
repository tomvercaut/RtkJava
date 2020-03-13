package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RadiopharmaceuticalInformationItem {
    private String radiopharmaceutical;
    private LocalTime radiopharmaceuticalStartTime;
    private double radionuclideTotalDose;
    private double radionuclideHalfLife;
    private double radionuclidePositronFraction;
    private LocalDateTime radiopharmaceuticalStartDateTime;
    private List<CodeItem> radionuclideCodeSequence = new ArrayList<>();
}
