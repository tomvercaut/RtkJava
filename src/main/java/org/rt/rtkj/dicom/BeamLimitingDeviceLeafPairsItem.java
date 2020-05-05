package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class BeamLimitingDeviceLeafPairsItem {
    private String rTBeamLimitingDeviceType;
    private int numberOfLeafJawPairs;
}
