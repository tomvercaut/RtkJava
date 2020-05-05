package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RTDose //implements DicomImage<Long>
{
    // Meta
    private int fileMetaInformationGroupLength;
    private byte[] fileMetaInformationVersion;
    private String mediaStorageSOPClassUID;
    private String mediaStorageSOPInstanceUID;
    private String transferSyntaxUID;
    private String implementationClassUID;
    private String implementationVersionName;
    // Main
    private String specificCharacterSet = "";
    private Optional<LocalDate> instanceCreationDate;
    private Optional<LocalTime> instanceCreationTime;
    private String sopClassUID;
    private String sopInstanceUID;
    private Optional<LocalDate> studyDate;
    private Optional<LocalTime> studyTime;
    private String accessionNumber;
    private Modality modality;
    private String manufacturer;
    private String referringPhysicianName;
    private String stationName;
    private String seriesDescription;
    private String manufacturerModelName;
    private String patientName;
    private String patientID;
    private Optional<LocalDate> patientBirthDate;
    private String patientSex;
    private double sliceThicknes;
    private String deviceSerialNumber;
    private String softwareVersions;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private String studyID;
    private int seriesNumber;
    private int instanceNumber;
    private double[] imagePositionPatient;
    private double[] imageOrientationPatient;
    private String frameOfReferenceUID;
    private String positionReferenceIndicator;
    private int samplesPerPixel;
    private PhotometricInterpretation photometricInterpretation;
    private int numberOfFrames;
    private int frameIncrementPointer;
    private int rows;
    private int columns;
    private double[] pixelSpacing;
    private int bitsAllocated;
    private int bitsStored;
    private int highBit;
    private PixelRepresentation pixelRepresentation;
    private String doseUnits;
    private String doseType;
    private String doseSummationType;
    private double[] gridFrameOffsetVector;
    private double doseGridScaling;
    private String tissueHeterogeneityCorrection;
    private List<DvhItem> dvhSequence = new ArrayList<>();
    private List<ReferencedSOPClassInstanceItem> referencedRTPlanSequence = new ArrayList<>();
    private List<ReferencedSOPClassInstanceItem> referencedStructureSetSequence = new ArrayList<>();
    private List<Long> pixelData = new ArrayList<>();

    public double getDose(int index) throws NullPointerException, IndexOutOfBoundsException {
        if (pixelData == null) throw new NullPointerException("PixelData was not initialised.");
        int n = pixelData.size();
        if (index >= n)
            throw new IndexOutOfBoundsException("Index [" + index + "] exceeds the pixeldata boundary [" + n + "]");
        return doseGridScaling * (double) pixelData.get(index);
    }

//    @Override
//    public double getRescaleIntercept() {
//        return 0;
//    }
//
//    @Override
//    public double getRescaleSlope() {
//        return getDoseGridScaling();
//    }
}
