package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class EnergyWindowRangeItem {
    private double energyWindowLowerLimit;
    private double energyWindowUpperLimit;
}
