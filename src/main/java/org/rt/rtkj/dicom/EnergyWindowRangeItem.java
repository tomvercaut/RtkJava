package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.Optional;

@Data
public class EnergyWindowRangeItem {
    private Optional<Double> energyWindowLowerLimit = Optional.empty();
    private Optional<Double> energyWindowUpperLimit = Optional.empty();
}
