package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.awt.image.DataBuffer.TYPE_USHORT;

@Log4j2
public class Writer {
    public static void writeGrayTiff(CT ct, String filename) throws IOException {
        log.info("filename: " + filename);
        int nr = ct.getRows();
        int nc = ct.getColumns();

        File outputFile = new File(filename);
        ImageTypeSpecifier specifier = ImageTypeSpecifier.createGrayscale(16, TYPE_USHORT, false);
        Iterator<ImageWriter> writers = ImageIO.getImageWriters(specifier, "tiff");

        ImageWriter writer = null;
        while (writers.hasNext()) {
            writer = writers.next();
            break;
        }
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        int n = nr * nc;
        for (int i = 0; i < n; ++i) {
            long pixel = ct.getPixelData().get(i);
            if (pixel < min) min = pixel;
            if (pixel > max) max = pixel;
        }
        log.info("min: " + min);
        log.info("max: " + max);
        double imin = Math.abs(min);
        double scale = max + imin;
        if (scale > 65535.0) {
            scale = ((double) 65535.0) / scale;
//            scale /= ((double)65535.0);
        }

        if (writer == null) throw new IOException("Unable to create tiff writer");
        BufferedImage bi = new BufferedImage(nc, nr, BufferedImage.TYPE_USHORT_GRAY);
        WritableRaster raster = bi.getRaster();
        int k = 0;
        for (int j = 0; j < nr; j++) {
            for (int i = 0; i < nc; i++) {
                double p = ct.getPixelData().get(k);
                p += imin;
//                p *= scale;
                raster.setPixel(i, j, new int[]{(int) p});
                ++k;
            }
        }
        ImageOutputStream ios = new FileImageOutputStream(outputFile);
        writer.setOutput(ios);
        writer.write(bi);
    }

    public static Optional<Attributes> rtdose(RTDose dose) {
        var dict = ElementDictionary.getStandardElementDictionary();
        if (dose.getModality() != Modality.RTDOSE) {
            log.error("RTDose instance has an invalid modality: " + dose.getModality().toString());
            return Optional.empty();
        }

        Attributes root = new Attributes();
        root.setString(Tag.SpecificCharacterSet, dict.vrOf(Tag.SpecificCharacterSet), dose.getSpecificCharacterSet());
        root.setString(Tag.InstanceCreationDate, dict.vrOf(Tag.InstanceCreationDate), DicomUtils.getLocalDateNow());
        root.setString(Tag.InstanceCreationTime, dict.vrOf(Tag.InstanceCreationTime), DicomUtils.getLocalTimeNow());
        root.setString(Tag.SOPClassUID, dict.vrOf(Tag.SOPClassUID), dose.getSopClassUID());
        root.setString(Tag.SOPInstanceUID, dict.vrOf(Tag.SOPInstanceUID), dose.getSopInstanceUID());
        root.setString(Tag.StudyDate, dict.vrOf(Tag.StudyDate), dose.getStudyDate().format(DicomUtils.getDateFormatter()));
        root.setString(Tag.StudyTime, dict.vrOf(Tag.StudyTime), dose.getStudyTime().format(DicomUtils.getTimeFormatter()));
        root.setString(Tag.AccessionNumber, dict.vrOf(Tag.AccessionNumber), dose.getAccessionNumber());
        root.setString(Tag.Modality, dict.vrOf(Tag.Modality), dose.getModality().toString());
        root.setString(Tag.Manufacturer, dict.vrOf(Tag.Manufacturer), dose.getManufacturer());
        root.setString(Tag.ReferringPhysicianName, dict.vrOf(Tag.ReferringPhysicianName), dose.getReferringPhysicianName());
        root.setString(Tag.StationName, dict.vrOf(Tag.StationName), dose.getStationName());
        root.setString(Tag.SeriesDescription, dict.vrOf(Tag.SeriesDescription), dose.getSeriesDescription());
        root.setString(Tag.ManufacturerModelName, dict.vrOf(Tag.ManufacturerModelName), dose.getManufacturerModelName());
        root.setString(Tag.PatientName, dict.vrOf(Tag.PatientName), dose.getPatientName());
        root.setString(Tag.PatientID, dict.vrOf(Tag.PatientID), dose.getPatientID());
        if (dose.getPatientBirthDate() != null) {
            root.setString(Tag.PatientBirthDate, dict.vrOf(Tag.PatientBirthDate), dose.getPatientBirthDate().format(DicomUtils.getDateFormatter()));
        }
        root.setString(Tag.PatientSex, dict.vrOf(Tag.PatientSex), dose.getPatientSex());
        root.setDouble(Tag.SliceThickness, dict.vrOf(Tag.SliceThickness), dose.getSliceThicknes());
        root.setString(Tag.DeviceSerialNumber, dict.vrOf(Tag.DeviceSerialNumber), dose.getDeviceSerialNumber());
        root.setString(Tag.SoftwareVersions, dict.vrOf(Tag.SoftwareVersions), dose.getSoftwareVersions());
        root.setString(Tag.StudyInstanceUID, dict.vrOf(Tag.StudyInstanceUID), dose.getStudyInstanceUID());
        root.setString(Tag.SeriesInstanceUID, dict.vrOf(Tag.SeriesInstanceUID), dose.getSeriesInstanceUID());
        root.setString(Tag.StudyID, dict.vrOf(Tag.StudyID), dose.getStudyID());
        root.setInt(Tag.SeriesNumber, dict.vrOf(Tag.SeriesNumber), dose.getSeriesNumber());
        root.setInt(Tag.InstanceNumber, dict.vrOf(Tag.InstanceNumber), dose.getInstanceNumber());
        root.setDouble(Tag.ImagePositionPatient, dict.vrOf(Tag.ImagePositionPatient), dose.getImagePositionPatient());
        root.setDouble(Tag.ImageOrientationPatient, dict.vrOf(Tag.ImageOrientationPatient), dose.getImageOrientationPatient());
        root.setString(Tag.FrameOfReferenceUID, dict.vrOf(Tag.FrameOfReferenceUID), dose.getFrameOfReferenceUID());
        root.setString(Tag.PositionReferenceIndicator, dict.vrOf(Tag.PositionReferenceIndicator), dose.getPositionReferenceIndicator());
        root.setInt(Tag.SamplesPerPixel, dict.vrOf(Tag.SamplesPerPixel), dose.getSamplesPerPixel());
        root.setString(Tag.PhotometricInterpretation, dict.vrOf(Tag.PhotometricInterpretation), dose.getPhotometricInterpretation().toString());
        root.setInt(Tag.NumberOfFrames, dict.vrOf(Tag.NumberOfFrames), dose.getNumberOfFrames());
        root.setInt(Tag.FrameIncrementPointer, dict.vrOf(Tag.FrameIncrementPointer), dose.getFrameIncrementPointer());
        root.setInt(Tag.Rows, dict.vrOf(Tag.Rows), dose.getRows());
        root.setInt(Tag.Columns, dict.vrOf(Tag.Columns), dose.getColumns());
        root.setDouble(Tag.PixelSpacing, dict.vrOf(Tag.PixelSpacing), dose.getPixelSpacing());
        root.setInt(Tag.BitsAllocated, dict.vrOf(Tag.BitsAllocated), dose.getBitsAllocated());
        root.setInt(Tag.BitsStored, dict.vrOf(Tag.BitsStored), dose.getBitsStored());
        root.setInt(Tag.HighBit, dict.vrOf(Tag.HighBit), dose.getHighBit());
        {
            var optPixelRep = toString(dose.getPixelRepresentation());
            if (optPixelRep.isEmpty()) {
                log.error("RTDose is missing the required pixel representation.");
                return Optional.empty();
            }
            root.setString(Tag.PixelRepresentation, dict.vrOf(Tag.PixelRepresentation), optPixelRep.get());
        }
        root.setString(Tag.DoseUnits, dict.vrOf(Tag.DoseUnits), dose.getDoseUnits());
        root.setString(Tag.DoseType, dict.vrOf(Tag.DoseType), dose.getDoseType());
        root.setString(Tag.DoseSummationType, dict.vrOf(Tag.DoseSummationType), dose.getDoseSummationType());
        root.setDouble(Tag.GridFrameOffsetVector, dict.vrOf(Tag.GridFrameOffsetVector), dose.getGridFrameOffsetVector());
        root.setDouble(Tag.DoseGridScaling, dict.vrOf(Tag.DoseGridScaling), dose.getDoseGridScaling());
        root.setString(Tag.TissueHeterogeneityCorrection, dict.vrOf(Tag.TissueHeterogeneityCorrection), dose.getTissueHeterogeneityCorrection());

        if (dose.getDvhSequence() != null && !dose.getDvhSequence().isEmpty()) {
            if (!addSequence(root, Tag.DVHSequence, dose.getDvhSequence(), dict, Writer::dvh)) {
                log.error("Unable to read DVHSequence");
            }
        }

        if (dose.getReferencedRTPlanSequence() != null && !dose.getReferencedRTPlanSequence().isEmpty()) {
            if (!addSequence(root, Tag.ReferencedRTPlanSequence, dose.getReferencedRTPlanSequence(), dict, Writer::referencedSOPClassInstance)) {
                log.error("Unable to read ReferencedRTPlanSequence");
            }
        }

        if (dose.getReferencedStructureSetSequence() != null && !dose.getReferencedStructureSetSequence().isEmpty()) {
            if (!addSequence(root, Tag.ReferencedStructureSetSequence, dose.getReferencedStructureSetSequence(), dict, Writer::referencedSOPClassInstance)) {
                log.error("Unable to read ReferencedStructureSetSequence");
            }
        }

        // dose.getPixelData()
        // Checks based on info found in C.8.8.3.4
        if (dose.getPhotometricInterpretation() != PhotometricInterpretation.MONOCHROME2) {
            log.error("The photometrix interpretation for RTDOSE file must be MONOCHROME2");
            return Optional.empty();
        }
        if (dose.getBitsAllocated() != 16 && dose.getBitsAllocated() != 32) {
            log.error("The number of bits allocated must be either 16 or 32");
            return Optional.empty();
        }
        if (dose.getBitsStored() != dose.getBitsAllocated()) {
            log.error("The number of bits stored but be equal to the number of bits allocated.");
            return Optional.empty();
        }
        if (dose.getPixelRepresentation() != PixelRepresentation.UNSIGNED) {
            log.error("Supported pixel representation for RTDOSE pixel data is unsigned integer values. Not the two's complement integer representation.");
            return Optional.empty();
        }

        // Check if the byteorder of the system is Little endian. If not, the following code will be incorrect.
        // The byte buffer is written for little endian DICOM streams. Big endian DICOM streams are no long supported
        // by the DICOM standard.
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
            log.error("The code that write the bytebuffer for the pixel data is written for little endian DICOM streams and assumes that the system is also little endian.");
            return Optional.empty();
        }

        ByteBuffer pb = null;
        int bs = dose.getBitsAllocated();
        int npixels = dose.getNumberOfFrames() * dose.getRows() * dose.getColumns();
        if (bs == 16) pb = ByteBuffer.allocate(npixels * 2);
        if (bs == 32) pb = ByteBuffer.allocate(npixels * 4);

        assert pb != null;
        pb.order(ByteOrder.LITTLE_ENDIAN);
        int k = 0;
        for (int i = 0; i < npixels; i++) {
            var val = dose.getPixelData().get(i);
            if (bs == 16) {
                pb.put(k++, (byte) ((val) & 0xFF));
                pb.put(k++, (byte) ((val >> 8) & 0xFF));
            } else {
                pb.put(k++, (byte) ((val) & 0xFF));
                pb.put(k++, (byte) ((val >> 8) & 0xFF));
                pb.put(k++, (byte) ((val >> 16) & 0xFF));
                pb.put(k++, (byte) ((val >> 24) & 0xFF));
            }
        }
        if (k != pb.capacity()) {
            log.error(String.format("Index in the byte buffer after filling the buffer should be equal to it's " +
                    "capacity. No remaining bytes should be left. Capacity: %d", pb.capacity()));
            return Optional.empty();
        }
        root.setBytes(Tag.PixelData, VR.OW, pb.array());

        return Optional.of(root);
    }

    private static <T> boolean addSequence(Attributes parent, int seqTag, List<T> items, ElementDictionary dict, BiFunction<T, ElementDictionary, Optional<Attributes>> function) {
        if (parent == null || items == null || dict == null) {
            if (parent == null) log.error("Parent of the sequence can't be null.");
            if (items == null) log.error("List of sequence items can't be null.");
            if (dict == null) log.error("DICOM dictionary can't be null.");
            return false;
        }
        int n = items.size();
        var sequence = parent.newSequence(seqTag, items.size());
        for (T item : items) {
            var optItem = function.apply(item, dict);
            if (optItem.isEmpty()) {
                log.error("Sequence item is empty");
                return false;
            } else {
                sequence.add(optItem.get());
            }
        }
        return true;
    }

    private static Optional<Attributes> dvh(DvhItem item, ElementDictionary dict) {
        if (item == null || dict == null) return Optional.empty();
        Attributes attr = new Attributes();
        attr.setString(Tag.DVHType, dict.vrOf(Tag.DVHType), item.getDvhType());
        attr.setString(Tag.DoseUnits, dict.vrOf(Tag.DoseUnits), item.getDoseUnits());
        attr.setString(Tag.DoseType, dict.vrOf(Tag.DoseType), item.getDoseType());
        attr.setDouble(Tag.DVHDoseScaling, dict.vrOf(Tag.DVHDoseScaling), item.getDvhDoseScaling());
        attr.setString(Tag.DVHVolumeUnits, dict.vrOf(Tag.DVHVolumeUnits), item.getDvhVolumeUnits());
        attr.setDouble(Tag.DVHData, dict.vrOf(Tag.DVHData), item.getDvhData());
        if (item.getDvhReferencedROISequence() != null && !item.getDvhReferencedROISequence().isEmpty()) {
            int n = item.getDvhReferencedROISequence().size();
            var dvhReferencedROISequence = attr.newSequence(Tag.DVHReferencedROISequence, n);
            for (int i = 0; i < n; i++) {
                var optDvhReferencedROIItem = dvhReferencedROI(item.getDvhReferencedROISequence().get(i), dict);
                if (optDvhReferencedROIItem.isEmpty()) {
                    log.error("Dvh referenced ROI item is empty");
                    return Optional.empty();
                }
                dvhReferencedROISequence.add(optDvhReferencedROIItem.get());
            }
        }
        attr.setInt(Tag.DVHNumberOfBins, dict.vrOf(Tag.DVHNumberOfBins), item.getDvhNumberOfBins());
        attr.setDouble(Tag.DVHMinimumDose, dict.vrOf(Tag.DVHMinimumDose), item.getDvhMinimumDose());
        attr.setDouble(Tag.DVHMaximumDose, dict.vrOf(Tag.DVHMaximumDose), item.getDvhMaximumDose());
        attr.setDouble(Tag.DVHMeanDose, dict.vrOf(Tag.DVHMeanDose), item.getDvhMeanDose());
        return Optional.of(attr);
    }

    private static Optional<Attributes> dvhReferencedROI(DVHReferencedROIItem item, ElementDictionary dict) {
        if (item == null || dict == null) return Optional.empty();
        Attributes attr = new Attributes();
        attr.setString(Tag.DVHROIContributionType, dict.vrOf(Tag.DVHROIContributionType), item.getDvhROIContributionType());
        attr.setInt(Tag.ReferencedROINumber, dict.vrOf(Tag.ReferencedROINumber), item.getReferencedROINumber());
        return Optional.of(attr);
    }

    private static Optional<Attributes> referencedSOPClassInstance(ReferencedSOPClassInstanceItem item, ElementDictionary dict) {
        if (item == null || dict == null) return Optional.empty();
        Attributes attr = new Attributes();
        attr.setString(Tag.ReferencedSOPClassUID, dict.vrOf(Tag.ReferencedSOPClassUID), item.getReferencedSOPClassUID());
        attr.setString(Tag.ReferencedSOPInstanceUID, dict.vrOf(Tag.ReferencedSOPInstanceUID), item.getReferencedSOPInstanceUID());
        return Optional.of(attr);
    }

    private static Optional<String> toString(PixelRepresentation pixelRepresentation) {
        final String errMsg = "Invalid pixel representation.";
        switch (pixelRepresentation) {
            case NONE:
                log.error(errMsg);
                return Optional.empty();
            case UNSIGNED:
                return Optional.of("0");
            case TWO_COMPLEMENT:
                return Optional.of("1");
        }
        log.error(errMsg);
        return Optional.empty();

    }
}
