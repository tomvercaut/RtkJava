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
    private Optional<String> specificCharacterSet = Optional.empty();
    private Optional<String> imageType = Optional.empty();
    private Optional<String> sOPClassUID = Optional.empty();
    private Optional<String> sOPInstanceUID = Optional.empty();
    private Optional<LocalDate> studyDate = Optional.empty();
    private Optional<LocalDate> seriesDate = Optional.empty();
    private Optional<LocalDate> acquisitionDate = Optional.empty();
    private Optional<LocalDate> contentDate = Optional.empty();
    private Optional<LocalTime> studyTime = Optional.empty();
    private Optional<LocalTime> seriesTime = Optional.empty();
    private Optional<LocalTime> acquisitionTime = Optional.empty();
    private Optional<LocalTime> contentTime = Optional.empty();
    private Optional<String> accessionNumber = Optional.empty();
    private Optional<Modality> modality = Optional.empty();
    private Optional<String> manufacturer = Optional.empty();
    private Optional<String> institutionName = Optional.empty();
    private Optional<String> institutionAddress = Optional.empty();
    private Optional<String> referringPhysicianName = Optional.empty();
    private Optional<String> stationName = Optional.empty();
    private Optional<String> studyDescription = Optional.empty();
    private Optional<String> seriesDescription = Optional.empty();
    private Optional<String> institutionalDepartmentName = Optional.empty();
    private Optional<String> physiciansOfRecord = Optional.empty();
    private Optional<String> performingPhysicianName = Optional.empty();
    private Optional<String> operatorsName = Optional.empty();
    private Optional<String> manufacturerModelName = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedPatientSequence  = Optional.empty();
    private Optional<List<CodeItem>> purposeOfReferenceCodeSequence  = Optional.empty();
    private Optional<String> patientName = Optional.empty();
    private Optional<String> patientID = Optional.empty();
    private Optional<String> issuerOfPatientID = Optional.empty();
    private Optional<LocalDate> patientBirthDate = Optional.empty();
    private Optional<String> patientSex = Optional.empty();
    private Optional<String> patientAge = Optional.empty();
    private Optional<Double> patientSize = Optional.empty();
    private Optional<Double> patientWeight = Optional.empty();
    private Optional<String> patientAddress = Optional.empty();
    private Optional<String> branchOfService = Optional.empty();
    private Optional<String> pregnancyStatus = Optional.empty();
    private Optional<String> bodyPartExamined = Optional.empty();
    private Optional<Double> sliceThickness = Optional.empty();
    private Optional<String> deviceSerialNumber = Optional.empty();
    private Optional<String> softwareVersions = Optional.empty();
    private Optional<String> collimatorType = Optional.empty();
    private Optional<LocalDate> dateOfLastCalibration = Optional.empty();
    private Optional<LocalTime> timeOfLastCalibration = Optional.empty();
    private Optional<String> convolutionKernel = Optional.empty();
    private Optional<Integer> actualFrameDuration = Optional.empty();
    private Optional<PatientPosition> patientPosition = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<String> studyID = Optional.empty();
    private Optional<Integer> seriesNumber = Optional.empty();
    private Optional<Integer> acquisitionNumber = Optional.empty();
    private Optional<Integer> instanceNumber = Optional.empty();
    private Optional<Double[]> imagePositionPatient = Optional.empty();
    private Optional<Double[]> imageOrientationPatient = Optional.empty();
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<String> positionReferenceIndicator = Optional.empty();
    private Optional<Double> sliceLocation = Optional.empty();
    private Optional<String> imageComments = Optional.empty();
    private Optional<Integer> samplesPerPixel = Optional.empty();
    private Optional<PhotometricInterpretation> photometricInterpretation = Optional.empty();
    private Optional<Integer> rows = Optional.empty();
    private Optional<Integer> columns = Optional.empty();
    private Optional<Double[]> pixelSpacing; // 0: row spacing / 1: column spacin = Optional.empty()g
    private Optional<List<String>> correctedImage = Optional.empty();
    private Optional<Integer> bitsAllocated = Optional.empty();
    private Optional<Integer> bitsStored = Optional.empty();
    private Optional<Integer> highBit = Optional.empty();
    private Optional<PixelRepresentation> pixelRepresentation = Optional.empty();
    private Optional<Integer> smallestImagePixelValue = Optional.empty();
    private Optional<Integer> largestImagePixelValue = Optional.empty();
    private Optional<Double> windowCenter = Optional.empty();
    private Optional<Double> windowWidth = Optional.empty();
    private Optional<Double> rescaleIntercept = Optional.empty();
    private Optional<Double> rescaleSlope = Optional.empty();
    private Optional<String> rescaleType = Optional.empty();
    private Optional<String> requestingPhysician = Optional.empty();
    private Optional<String> requestingService = Optional.empty();
    private Optional<String> requestedProcedureDescription = Optional.empty();
    private Optional<String> currentPatientLocation = Optional.empty();
    private Optional<LocalDate> performedProcedureStepStartDate = Optional.empty();
    private Optional<LocalTime> performedProcedureStepStartTime = Optional.empty();
    private Optional<List<RequestAttributesItem>> requestAttributesSequence = Optional.empty();
    private Optional<String> requestedProcedureID = Optional.empty();
    private Optional<List<EnergyWindowRangeItem>> energyWindowRangeSequence  = Optional.empty();
    private Optional<List<RadiopharmaceuticalInformationItem>> radiopharmaceuticalInformationSequence  = Optional.empty();
    private Optional<Integer> numberOfSlices = Optional.empty();
    private Optional<List<PatientOrientationCodeItem>> patientOrientationCodeSequence  = Optional.empty();
    private Optional<List<CodeItem>> patientGantryRelationshipCodeSequence  = Optional.empty();
    private Optional<String> seriesType = Optional.empty();
    private Optional<String> units = Optional.empty();
    private Optional<String> countsSource = Optional.empty();
    private Optional<String> randomsCorrectionMethod = Optional.empty();
    private Optional<String> attenuationCorrectionMethod = Optional.empty();
    private Optional<String> decayCorrection = Optional.empty();
    private Optional<String> reconstructionMethod = Optional.empty();
    private Optional<String> scatterCorrectionMethod = Optional.empty();
    private Optional<Double> axialAcceptance = Optional.empty();
    private Optional<Integer[]> axialMash = Optional.empty();
    private Optional<Double> frameReferenceTime = Optional.empty();
    private Optional<Double> decayFactor = Optional.empty();
    private Optional<Double> doseCalibrationFactor = Optional.empty();
    private Optional<Double> scatterFractionFactor = Optional.empty();
    private Optional<Integer> imageIndex = Optional.empty();
    private Optional<List<Long>> pixelData  = Optional.empty();

    public PT(MetaHeader meta) {
        super(meta);
    }

}
