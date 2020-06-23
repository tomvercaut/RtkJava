package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

@Data
public class EnergyWindowRangeItem {
    private Option<Double> energyWindowLowerLimit = Option.empty();
    private Option<Double> energyWindowUpperLimit = Option.empty();
}
