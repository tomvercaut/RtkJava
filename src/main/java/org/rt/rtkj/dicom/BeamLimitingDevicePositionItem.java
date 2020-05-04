package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BeamLimitingDevicePositionItem {
    private String rtBeamLimitingDeviceType;
    private List<Double> leafJawPositions = new ArrayList<>();
}
