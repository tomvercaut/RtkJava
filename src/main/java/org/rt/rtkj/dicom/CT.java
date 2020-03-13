package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CT extends MetaHeader implements DicomImage<Long> {
    private String specificCharacterSet;
    private List<String> imageType;
    private String sOPClassUID;
    private String sOPInstanceUID;
    private LocalDate studyDate;
    private LocalDate seriesDate;
    private LocalDate acquisitionDate;
    private LocalDate contentDate;
    private LocalTime studyTime;
    private LocalTime seriesTime;
    private LocalTime acquisitionTime;
    private LocalTime contentTime;
    private String accessionNumber;
    private Modality modality;
    private String manufacturer;
    private String institutionName;
    private String referringPhysicianName;
    private String stationName;
    private List<ReducedCodeItem> procedureCodeSequence = new ArrayList<>();
    private String seriesDescription;
    private String institutionalDepartmentName;
    private String manufacturerModelName;
    private List<ReferencedSOPClassInstanceItem> referencedStudySequence = new ArrayList<>();
    private String patientName;
    private String patientID;
    private LocalDate patientBirthDate;
    private String patientSex;
    private String patientAge;
    private String patientIdentityRemoved;
    private String deidentificationMethod;
    private String bodyPartExamined;
    private String scanOptions;
    private double sliceThickness;
    private double kVP;
    private double dataCollectionDiameter;
    private String deviceSerialNumber;
    private String softwareVersions;
    private String protocolName;
    private double reconstructionDiameter;
    private double gantryDetectorTilt;
    private double tableHeight;
    private String rotationDirection;
    private int exposureTime;
    private int xRayTubeCurrent;
    private int exposure;
    private int generatorPower;
    private double[] focalSpots;
    private String convolutionKernel;
    private PatientPosition patientPosition;
    private String exposureModulationType;
    private double estimatedDoseSaving;
    private double cTDIvol;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private String studyID;
    private int seriesNumber;
    private int acquisitionNumber;
    private int instanceNumber;
    private String patientOrientation;
    private double[] imagePositionPatient;
    private double[] imageOrientationPatient;
    private String frameOfReferenceUID;
    private String positionReferenceIndicator;
    private double sliceLocation;
    private String imageComments;
    private int samplesPerPixel;
    private PhotometricInterpretation photometricInterpretation;
    private int rows;
    private int columns;
    private double[] pixelSpacing;
    private int bitsAllocated;
    private int bitsStored;
    private int highBit;
    private PixelRepresentation pixelRepresentation;
    private double windowCenter;
    private double windowWidth;
    private double rescaleIntercept;
    private double rescaleSlope;
    private LocalDate scheduledProcedureStepStartDate;
    private LocalTime scheduledProcedureStepStartTime;
    private LocalDate scheduledProcedureStepEndDate;
    private LocalTime scheduledProcedureStepEndTime;
    private LocalDate performedProcedureStepStartDate;
    private LocalTime performedProcedureStepStartTime;
    private String performedProcedureStepID;
    private List<ReducedCodeItem> performedProtocolCodeSequence = new ArrayList<>();
    private List<Long> pixelData = new ArrayList<>();

    public CT(MetaHeader meta) {
        super(meta);
    }
}
