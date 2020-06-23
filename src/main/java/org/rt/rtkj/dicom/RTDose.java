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
public class RTDose extends MetaHeader //implements DicomImage<Long>
{
    private Option<String> specificCharacterSet = Option.empty();
    private Option<LocalDate> instanceCreationDate = Option.empty();
    private Option<LocalTime> instanceCreationTime = Option.empty();
    private Option<String> sopClassUID = Option.empty();
    private Option<String> sopInstanceUID = Option.empty();
    private Option<LocalDate> studyDate = Option.empty();
    private Option<LocalTime> studyTime = Option.empty();
    private Option<String> accessionNumber = Option.empty();
    private Option<Modality> modality = Option.empty();
    private Option<String> manufacturer = Option.empty();
    private Option<String> referringPhysicianName = Option.empty();
    private Option<String> stationName = Option.empty();
    private Option<String> seriesDescription = Option.empty();
    private Option<String> manufacturerModelName = Option.empty();
    private Option<String> patientName = Option.empty();
    private Option<String> patientID = Option.empty();
    private Option<LocalDate> patientBirthDate = Option.empty();
    private Option<String> patientSex = Option.empty();
    private Option<Double> sliceThicknes = Option.empty();
    private Option<String> deviceSerialNumber = Option.empty();
    private Option<String> softwareVersions = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<String> studyID = Option.empty();
    private Option<Integer> seriesNumber = Option.empty();
    private Option<Integer> instanceNumber = Option.empty();
    private Option<Double[]> imagePositionPatient = Option.empty();
    private Option<Double[]> imageOrientationPatient = Option.empty();
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<String> positionReferenceIndicator = Option.empty();
    private Option<Integer> samplesPerPixel = Option.empty();
    private Option<PhotometricInterpretation> photometricInterpretation = Option.empty();
    private Option<Integer> numberOfFrames = Option.empty();
    private Option<Integer> frameIncrementPointer = Option.empty();
    private Option<Integer> rows = Option.empty();
    private Option<Integer> columns = Option.empty();
    private Option<Double[]> pixelSpacing = Option.empty();
    private Option<Integer> bitsAllocated = Option.empty();
    private Option<Integer> bitsStored = Option.empty();
    private Option<Integer> highBit = Option.empty();
    private Option<PixelRepresentation> pixelRepresentation = Option.empty();
    private Option<String> doseUnits = Option.empty();
    private Option<String> doseType = Option.empty();
    private Option<String> doseSummationType = Option.empty();
    private Option<Double[]> gridFrameOffsetVector = Option.empty();
    private Option<Double> doseGridScaling = Option.empty();
    private Option<String> tissueHeterogeneityCorrection = Option.empty();
    private Option<List<DvhItem>> dvhSequence = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> referencedRTPlanSequence = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> referencedStructureSetSequence = Option.empty();
    private Option<List<Long>> pixelData = Option.empty();

    public RTDose(MetaHeader meta) {
        super(meta);
    }

    public Option<Double> getDose(int index) throws NullPointerException, IndexOutOfBoundsException {
        if (pixelData == null || pixelData.isEmpty()) throw new NullPointerException("PixelData was not initialised.");
        int n = pixelData.get().size();
        if (index >= n)
            throw new IndexOutOfBoundsException("Index [" + index + "] exceeds the pixeldata boundary [" + n + "]");
        return Option.of(doseGridScaling.get() * (double) pixelData.get().get(index));
    }

}
