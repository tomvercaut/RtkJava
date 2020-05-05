package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ControlPointDeliveryItem {
    private Optional<LocalDate> treatmentControlPointDate;
    private Optional<LocalTime> treatmentControlPointTime;
    private double specifiedMeterset;
    private double deliveredMeterset;
    private double doseRateDelivered;
    private String nominalBeamEnergyUnit;
    private double nominalBeamEnergy;
    private double doseRateSet;
    private List<BeamLimitingDevicePositionItem> BeamLimitingDevicePositionSequence = new ArrayList<>();
    private double gantryAngle;
    private RotationDirection gantryRotationDirection;
    private double beamLimitingDeviceAngle;
    private RotationDirection beamLimitingDeviceRotationDirection;
    private double patientSupportAngle;
    private RotationDirection patientSupportRotationDirection;
    private double tableTopEccentricAxisDistance;
    private double tableTopEccentricAngle;
    private RotationDirection tableTopEccentricRotationDirection;
    private double tableTopVerticalPosition;
    private double tableTopLongitudinalPosition;
    private double tableTopLateralPosition;
    private int referencedControlPointIndex;
}
