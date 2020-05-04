package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ControlPointDeliveryItem {
    private LocalDate treatmentControlPointDate;
    private LocalTime treatmentControlPointTime;
    private String specifiedMeterset;
    private String deliveredMeterset;
    private String doseRateDelivered;
    private String nominalBeamEnergyUnit;
    private String nominalBeamEnergy;
    private String doseRateSet;
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
