package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreatmentSessionBeamItem {
    private int currentFractionNumber;
    private String treatmentTerminationStatus;
    private String treatmentTerminationCode;
    private String treatmentVerificationStatus;
    private double deliveredTreatmentTime;
    private List<ControlPointDeliveryItem> controlPointDeliverySequence = new ArrayList<>();
    private List<ReferencedCalculatedDoseReferenceItem> referencedCalculatedDoseReferenceSequence = new ArrayList<>();
    private double sourceAxisDistance;
    private String beamName;
    private String beamType;
    private String radiationType;
    private String treatmentDeliveryType;
    private int numberOfWedges;
    private int numberOfCompensators;
    private int numberOfBoli;
    private int numberOfBlocks;
    private int numberOfControlPoints;
    private int referencedBeamNumber;
}
