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
public class PT extends MetaHeader implements DicomImage<Long> {
    private String specificCharacterSet;
    private String imageType;
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
    private String institutionAddress;
    private String referringPhysicianName;
    private String stationName;
    private String studyDescription;
    private String seriesDescription;
    private String institutionalDepartmentName;
    private String physiciansOfRecord;
    private String performingPhysicianName;
    private String operatorsName;
    private String manufacturerModelName;
    private List<ReferencedSOPClassInstanceItem> referencedPatientSequence = new ArrayList<>();
    private List<CodeItem> purposeOfReferenceCodeSequence = new ArrayList<>();
    private String patientName;
    private String patientID;
    private String issuerOfPatientID;
    private LocalDate patientBirthDate;
    private String patientSex;
    private String patientAge;
    private double patientSize;
    private double patientWeight;
    private String patientAddress;
    private String branchOfService;
    private String pregnancyStatus;
    private String bodyPartExamined;
    private double sliceThickness;
    private String deviceSerialNumber;
    private String softwareVersions;
    private String collimatorType;
    private LocalDate dateOfLastCalibration;
    private LocalTime timeOfLastCalibration;
    private String convolutionKernel;
    private int actualFrameDuration;
    private PatientPosition patientPosition;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private String studyID;
    private int seriesNumber;
    private int acquisitionNumber;
    private int instanceNumber;
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
    private List<String> correctedImage;
    private int bitsAllocated;
    private int bitsStored;
    private int highBit;
    private PixelRepresentation pixelRepresentation;
    private int smallestImagePixelValue;
    private int largestImagePixelValue;
    private double windowCenter;
    private double windowWidth;
    private double rescaleIntercept;
    private double rescaleSlope;
    private String rescaleType;
    private String requestingPhysician;
    private String requestingService;
    private String requestedProcedureDescription;
    private String currentPatientLocation;
    private LocalDate performedProcedureStepStartDate;
    private LocalTime performedProcedureStepStartTime;
    private List<RequestAttributesItem> requestAttributesSequence;
    private String requestedProcedureID;
    private List<EnergyWindowRangeItem> energyWindowRangeSequence = new ArrayList<>();
    private List<RadiopharmaceuticalInformationItem> radiopharmaceuticalInformationSequence = new ArrayList<>();
    private int numberOfSlices;
    private List<PatientOrientationCodeItem> patientOrientationCodeSequence = new ArrayList<>();
    private List<CodeItem> patientGantryRelationshipCodeSequence = new ArrayList<>();
    private String seriesType;
    private String units;
    private String countsSource;
    private String randomsCorrectionMethod;
    private String attenuationCorrectionMethod;
    private String decayCorrection;
    private String reconstructionMethod;
    private String scatterCorrectionMethod;
    private double axialAcceptance;
    private int[] axialMash;
    private double frameReferenceTime;
    private double decayFactor;
    private double doseCalibrationFactor;
    private double scatterFractionFactor;
    private int imageIndex;
    private List<Long> pixelData = new ArrayList<>();

    public PT(MetaHeader meta) {
        super(meta);
    }

    public Optional<PixelRepresentation> getPixelRepresentation() {
        return Optional.ofNullable(pixelRepresentation);
    }
}
