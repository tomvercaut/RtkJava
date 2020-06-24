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
public class CT extends MetaHeader implements DicomImage<Long>, HasImagePositionPatient {
    private Option<String> specificCharacterSet = Option.empty();
    private Option<List<String>> imageType = Option.empty();
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
    private Option<String> referringPhysicianName = Option.empty();
    private Option<String> stationName = Option.empty();
    private Option<List<ReducedCodeItem>> procedureCodeSequence = Option.empty();
    private Option<String> seriesDescription = Option.empty();
    private Option<String> institutionalDepartmentName = Option.empty();
    private Option<String> manufacturerModelName = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> referencedStudySequence = Option.empty();
    private Option<String> patientName = Option.empty();
    private Option<String> patientID = Option.empty();
    private Option<LocalDate> patientBirthDate = Option.empty();
    private Option<String> patientSex = Option.empty();
    private Option<String> patientAge = Option.empty();
    private Option<String> patientIdentityRemoved = Option.empty();
    private Option<String> deidentificationMethod = Option.empty();
    private Option<String> bodyPartExamined = Option.empty();
    private Option<String> scanOptions = Option.empty();
    private Option<Double> sliceThickness = Option.empty();
    private Option<Double> kVP = Option.empty();
    private Option<Double> dataCollectionDiameter = Option.empty();
    private Option<String> deviceSerialNumber = Option.empty();
    private Option<String> softwareVersions = Option.empty();
    private Option<String> protocolName = Option.empty();
    private Option<Double> reconstructionDiameter = Option.empty();
    private Option<Double> gantryDetectorTilt = Option.empty();
    private Option<Double> tableHeight = Option.empty();
    private Option<String> rotationDirection = Option.empty();
    private Option<Integer> exposureTime = Option.empty();
    private Option<Integer> xRayTubeCurrent = Option.empty();
    private Option<Integer> exposure = Option.empty();
    private Option<Integer> generatorPower = Option.empty();
    private Option<Double[]> focalSpots = Option.empty();
    private Option<String> convolutionKernel = Option.empty();
    private Option<PatientPosition> patientPosition = Option.empty();
    private Option<String> exposureModulationType = Option.empty();
    private Option<Double> estimatedDoseSaving = Option.empty();
    private Option<Double> cTDIvol = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<String> studyID = Option.empty();
    private Option<Integer> seriesNumber = Option.empty();
    private Option<Integer> acquisitionNumber = Option.empty();
    private Option<Integer> instanceNumber = Option.empty();
    private Option<String> patientOrientation = Option.empty();
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
    private Option<Integer> bitsAllocated = Option.empty();
    private Option<Integer> bitsStored = Option.empty();
    private Option<Integer> highBit = Option.empty();
    private Option<PixelRepresentation> pixelRepresentation = Option.empty();
    private Option<Double> windowCenter = Option.empty();
    private Option<Double> windowWidth = Option.empty();
    private Option<Double> rescaleIntercept = Option.empty();
    private Option<Double> rescaleSlope = Option.empty();
    private Option<LocalDate> scheduledProcedureStepStartDate = Option.empty();
    private Option<LocalTime> scheduledProcedureStepStartTime = Option.empty();
    private Option<LocalDate> scheduledProcedureStepEndDate = Option.empty();
    private Option<LocalTime> scheduledProcedureStepEndTime = Option.empty();
    private Option<LocalDate> performedProcedureStepStartDate = Option.empty();
    private Option<LocalTime> performedProcedureStepStartTime = Option.empty();
    private Option<String> performedProcedureStepID = Option.empty();
    private Option<List<ReducedCodeItem>> performedProtocolCodeSequence = Option.empty();
    private Option<List<Long>> pixelData = Option.empty();

    public CT(MetaHeader meta) {
        super(meta);
    }

    /**
     * Clear the meta header
     */
    public void clearMetaHeader() {
        super.clear();
    }
}
