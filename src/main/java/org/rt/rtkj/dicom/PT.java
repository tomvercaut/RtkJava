package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rt.rtkj.Option;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PT extends MetaHeader implements DicomImage<Long> {
    private Option<String> specificCharacterSet = Option.empty();
    private Option<String> imageType = Option.empty();
    private Option<String> sOPClassUID = Option.empty();
    private Option<String> sOPInstanceUID = Option.empty();
    private Option<LocalDate> studyDate = Option.empty();
    private Option<LocalDate> seriesDate = Option.empty();
    private Option<LocalDate> acquisitionDate = Option.empty();
    private Option<LocalDate> contentDate = Option.empty();
    private Option<LocalTime> studyTime = Option.empty();
    private Option<LocalTime> seriesTime = Option.empty();
    private Option<LocalTime> acquisitionTime = Option.empty();
    private Option<LocalTime> contentTime = Option.empty();
    private Option<String> accessionNumber = Option.empty();
    private Option<Modality> modality = Option.empty();
    private Option<String> manufacturer = Option.empty();
    private Option<String> institutionName = Option.empty();
    private Option<String> institutionAddress = Option.empty();
    private Option<String> referringPhysicianName = Option.empty();
    private Option<String> stationName = Option.empty();
    private Option<String> studyDescription = Option.empty();
    private Option<String> seriesDescription = Option.empty();
    private Option<String> institutionalDepartmentName = Option.empty();
    private Option<String> physiciansOfRecord = Option.empty();
    private Option<String> performingPhysicianName = Option.empty();
    private Option<String> operatorsName = Option.empty();
    private Option<String> manufacturerModelName = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> referencedPatientSequence = Option.empty();
    private Option<List<CodeItem>> purposeOfReferenceCodeSequence = Option.empty();
    private Option<String> patientName = Option.empty();
    private Option<String> patientID = Option.empty();
    private Option<String> issuerOfPatientID = Option.empty();
    private Option<LocalDate> patientBirthDate = Option.empty();
    private Option<String> patientSex = Option.empty();
    private Option<String> patientAge = Option.empty();
    private Option<Double> patientSize = Option.empty();
    private Option<Double> patientWeight = Option.empty();
    private Option<String> patientAddress = Option.empty();
    private Option<String> branchOfService = Option.empty();
    private Option<String> pregnancyStatus = Option.empty();
    private Option<String> bodyPartExamined = Option.empty();
    private Option<Double> sliceThickness = Option.empty();
    private Option<String> deviceSerialNumber = Option.empty();
    private Option<String> softwareVersions = Option.empty();
    private Option<String> collimatorType = Option.empty();
    private Option<LocalDate> dateOfLastCalibration = Option.empty();
    private Option<LocalTime> timeOfLastCalibration = Option.empty();
    private Option<String> convolutionKernel = Option.empty();
    private Option<Integer> actualFrameDuration = Option.empty();
    private Option<PatientPosition> patientPosition = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<String> studyID = Option.empty();
    private Option<Integer> seriesNumber = Option.empty();
    private Option<Integer> acquisitionNumber = Option.empty();
    private Option<Integer> instanceNumber = Option.empty();
    private Option<Double[]> imagePositionPatient = Option.empty();
    private Option<Double[]> imageOrientationPatient = Option.empty();
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<String> positionReferenceIndicator = Option.empty();
    private Option<Double> sliceLocation = Option.empty();
    private Option<String> imageComments = Option.empty();
    private Option<Integer> samplesPerPixel = Option.empty();
    private Option<PhotometricInterpretation> photometricInterpretation = Option.empty();
    private Option<Integer> rows = Option.empty();
    private Option<Integer> columns = Option.empty();
    private Option<Double[]> pixelSpacing; // 0: row spacing / 1: column spacin = Option.empty()g
    private Option<List<String>> correctedImage = Option.empty();
    private Option<Integer> bitsAllocated = Option.empty();
    private Option<Integer> bitsStored = Option.empty();
    private Option<Integer> highBit = Option.empty();
    private Option<PixelRepresentation> pixelRepresentation = Option.empty();
    private Option<Integer> smallestImagePixelValue = Option.empty();
    private Option<Integer> largestImagePixelValue = Option.empty();
    private Option<Double> windowCenter = Option.empty();
    private Option<Double> windowWidth = Option.empty();
    private Option<Double> rescaleIntercept = Option.empty();
    private Option<Double> rescaleSlope = Option.empty();
    private Option<String> rescaleType = Option.empty();
    private Option<String> requestingPhysician = Option.empty();
    private Option<String> requestingService = Option.empty();
    private Option<String> requestedProcedureDescription = Option.empty();
    private Option<String> currentPatientLocation = Option.empty();
    private Option<LocalDate> performedProcedureStepStartDate = Option.empty();
    private Option<LocalTime> performedProcedureStepStartTime = Option.empty();
    private Option<List<RequestAttributesItem>> requestAttributesSequence = Option.empty();
    private Option<String> requestedProcedureID = Option.empty();
    private Option<List<EnergyWindowRangeItem>> energyWindowRangeSequence = Option.empty();
    private Option<List<RadiopharmaceuticalInformationItem>> radiopharmaceuticalInformationSequence = Option.empty();
    private Option<Integer> numberOfSlices = Option.empty();
    private Option<List<PatientOrientationCodeItem>> patientOrientationCodeSequence = Option.empty();
    private Option<List<CodeItem>> patientGantryRelationshipCodeSequence = Option.empty();
    private Option<String> seriesType = Option.empty();
    private Option<String> units = Option.empty();
    private Option<String> countsSource = Option.empty();
    private Option<String> randomsCorrectionMethod = Option.empty();
    private Option<String> attenuationCorrectionMethod = Option.empty();
    private Option<String> decayCorrection = Option.empty();
    private Option<String> reconstructionMethod = Option.empty();
    private Option<String> scatterCorrectionMethod = Option.empty();
    private Option<Double> axialAcceptance = Option.empty();
    private Option<Integer[]> axialMash = Option.empty();
    private Option<Double> frameReferenceTime = Option.empty();
    private Option<Double> decayFactor = Option.empty();
    private Option<Double> doseCalibrationFactor = Option.empty();
    private Option<Double> scatterFractionFactor = Option.empty();
    private Option<Integer> imageIndex = Option.empty();
    private Option<List<Long>> pixelData = Option.empty();

    public PT(MetaHeader meta) {
        super(meta);
    }

    /**
     * Clear the meta header
     */
    public void clearMetaHeader() {
        super.clear();
    }
}
