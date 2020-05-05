package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CT extends MetaHeader implements DicomImage<Long> {
    private String specificCharacterSet;
    private List<String> imageType;
    private String sOPClassUID;
    private String sOPInstanceUID;
    private Optional<LocalDate> studyDate;
    private Optional<LocalDate> seriesDate;
    private Optional<LocalDate> acquisitionDate;
    private Optional<LocalDate> contentDate;
    private Optional<LocalTime> studyTime;
    private Optional<LocalTime> seriesTime;
    private Optional<LocalTime> acquisitionTime;
    private Optional<LocalTime> contentTime;
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
    private Optional<LocalDate> patientBirthDate;
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
    private double[] pixelSpacing; // 0: row spacing / 1: column spacing
    private int bitsAllocated;
    private int bitsStored;
    private int highBit;
    private PixelRepresentation pixelRepresentation;
    private double windowCenter;
    private double windowWidth;
    private double rescaleIntercept;
    private double rescaleSlope;
    private Optional<LocalDate> scheduledProcedureStepStartDate;
    private Optional<LocalTime> scheduledProcedureStepStartTime;
    private Optional<LocalDate> scheduledProcedureStepEndDate;
    private Optional<LocalTime> scheduledProcedureStepEndTime;
    private Optional<LocalDate> performedProcedureStepStartDate;
    private Optional<LocalTime> performedProcedureStepStartTime;
    private String performedProcedureStepID;
    private List<ReducedCodeItem> performedProtocolCodeSequence = new ArrayList<>();
    private List<Long> pixelData = new ArrayList<>();

    public CT(MetaHeader meta) {
        super(meta);
    }
}
