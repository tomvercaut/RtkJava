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
public class CT extends MetaHeader implements DicomImage<Long>, HasImagePositionPatient {
    private Optional<String> specificCharacterSet = Optional.empty();
    private Optional<List<String>> imageType = Optional.empty();
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
    private Optional<String> referringPhysicianName = Optional.empty();
    private Optional<String> stationName = Optional.empty();
    private Optional<List<ReducedCodeItem>> procedureCodeSequence  = Optional.empty();
    private Optional<String> seriesDescription = Optional.empty();
    private Optional<String> institutionalDepartmentName = Optional.empty();
    private Optional<String> manufacturerModelName = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedStudySequence  = Optional.empty();
    private Optional<String> patientName = Optional.empty();
    private Optional<String> patientID = Optional.empty();
    private Optional<LocalDate> patientBirthDate = Optional.empty();
    private Optional<String> patientSex = Optional.empty();
    private Optional<String> patientAge = Optional.empty();
    private Optional<String> patientIdentityRemoved = Optional.empty();
    private Optional<String> deidentificationMethod = Optional.empty();
    private Optional<String> bodyPartExamined = Optional.empty();
    private Optional<String> scanOptions = Optional.empty();
    private Optional<Double> sliceThickness = Optional.empty();
    private Optional<Double> kVP = Optional.empty();
    private Optional<Double> dataCollectionDiameter = Optional.empty();
    private Optional<String> deviceSerialNumber = Optional.empty();
    private Optional<String> softwareVersions = Optional.empty();
    private Optional<String> protocolName = Optional.empty();
    private Optional<Double> reconstructionDiameter = Optional.empty();
    private Optional<Double> gantryDetectorTilt = Optional.empty();
    private Optional<Double> tableHeight = Optional.empty();
    private Optional<String> rotationDirection = Optional.empty();
    private Optional<Integer> exposureTime = Optional.empty();
    private Optional<Integer> xRayTubeCurrent = Optional.empty();
    private Optional<Integer> exposure = Optional.empty();
    private Optional<Integer> generatorPower = Optional.empty();
    private Optional<Double[]> focalSpots = Optional.empty();
    private Optional<String> convolutionKernel = Optional.empty();
    private Optional<PatientPosition> patientPosition = Optional.empty();
    private Optional<String> exposureModulationType = Optional.empty();
    private Optional<Double> estimatedDoseSaving = Optional.empty();
    private Optional<Double> cTDIvol = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<String> studyID = Optional.empty();
    private Optional<Integer> seriesNumber = Optional.empty();
    private Optional<Integer> acquisitionNumber = Optional.empty();
    private Optional<Integer> instanceNumber = Optional.empty();
    private Optional<String> patientOrientation = Optional.empty();
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
    private Optional<Integer> bitsAllocated = Optional.empty();
    private Optional<Integer> bitsStored = Optional.empty();
    private Optional<Integer> highBit = Optional.empty();
    private Optional<PixelRepresentation> pixelRepresentation = Optional.empty();
    private Optional<Double> windowCenter = Optional.empty();
    private Optional<Double> windowWidth = Optional.empty();
    private Optional<Double> rescaleIntercept = Optional.empty();
    private Optional<Double> rescaleSlope = Optional.empty();
    private Optional<LocalDate> scheduledProcedureStepStartDate = Optional.empty();
    private Optional<LocalTime> scheduledProcedureStepStartTime = Optional.empty();
    private Optional<LocalDate> scheduledProcedureStepEndDate = Optional.empty();
    private Optional<LocalTime> scheduledProcedureStepEndTime = Optional.empty();
    private Optional<LocalDate> performedProcedureStepStartDate = Optional.empty();
    private Optional<LocalTime> performedProcedureStepStartTime = Optional.empty();
    private Optional<String> performedProcedureStepID = Optional.empty();
    private Optional<List<ReducedCodeItem>> performedProtocolCodeSequence  = Optional.empty();
    private Optional<List<Long>> pixelData  = Optional.empty();

    public CT(MetaHeader meta) {
        super(meta);
    }

}
