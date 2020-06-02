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
public class RTDose extends MetaHeader //implements DicomImage<Long>
{
    private Optional<String> specificCharacterSet = Optional.empty();
    private Optional<LocalDate> instanceCreationDate = Optional.empty();
    private Optional<LocalTime> instanceCreationTime = Optional.empty();
    private Optional<String> sopClassUID = Optional.empty();
    private Optional<String> sopInstanceUID = Optional.empty();
    private Optional<LocalDate> studyDate = Optional.empty();
    private Optional<LocalTime> studyTime = Optional.empty();
    private Optional<String> accessionNumber = Optional.empty();
    private Optional<Modality> modality = Optional.empty();
    private Optional<String> manufacturer = Optional.empty();
    private Optional<String> referringPhysicianName = Optional.empty();
    private Optional<String> stationName = Optional.empty();
    private Optional<String> seriesDescription = Optional.empty();
    private Optional<String> manufacturerModelName = Optional.empty();
    private Optional<String> patientName = Optional.empty();
    private Optional<String> patientID = Optional.empty();
    private Optional<LocalDate> patientBirthDate = Optional.empty();
    private Optional<String> patientSex = Optional.empty();
    private Optional<Double> sliceThicknes = Optional.empty();
    private Optional<String> deviceSerialNumber = Optional.empty();
    private Optional<String> softwareVersions = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<String> studyID = Optional.empty();
    private Optional<Integer> seriesNumber = Optional.empty();
    private Optional<Integer> instanceNumber = Optional.empty();
    private Optional<Double[]> imagePositionPatient = Optional.empty();
    private Optional<Double[]> imageOrientationPatient = Optional.empty();
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<String> positionReferenceIndicator = Optional.empty();
    private Optional<Integer> samplesPerPixel = Optional.empty();
    private Optional<PhotometricInterpretation> photometricInterpretation = Optional.empty();
    private Optional<Integer> numberOfFrames = Optional.empty();
    private Optional<Integer> frameIncrementPointer = Optional.empty();
    private Optional<Integer> rows = Optional.empty();
    private Optional<Integer> columns = Optional.empty();
    private Optional<Double[]> pixelSpacing = Optional.empty();
    private Optional<Integer> bitsAllocated = Optional.empty();
    private Optional<Integer> bitsStored = Optional.empty();
    private Optional<Integer> highBit = Optional.empty();
    private Optional<PixelRepresentation> pixelRepresentation = Optional.empty();
    private Optional<String> doseUnits = Optional.empty();
    private Optional<String> doseType = Optional.empty();
    private Optional<String> doseSummationType = Optional.empty();
    private Optional<Double[]> gridFrameOffsetVector = Optional.empty();
    private Optional<Double> doseGridScaling = Optional.empty();
    private Optional<String> tissueHeterogeneityCorrection = Optional.empty();
    private Optional<List<DvhItem>> dvhSequence = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedRTPlanSequence = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedStructureSetSequence = Optional.empty();
    private Optional<List<Long>> pixelData = Optional.empty();

    public RTDose(MetaHeader meta) {
        super(meta);
    }

    public Optional<Double> getDose(int index) throws NullPointerException, IndexOutOfBoundsException {
        if (pixelData == null || pixelData.isEmpty()) throw new NullPointerException("PixelData was not initialised.");
        int n = pixelData.get().size();
        if (index >= n)
            throw new IndexOutOfBoundsException("Index [" + index + "] exceeds the pixeldata boundary [" + n + "]");
        return Optional.of(doseGridScaling.get() * (double) pixelData.get().get(index));
    }

}
