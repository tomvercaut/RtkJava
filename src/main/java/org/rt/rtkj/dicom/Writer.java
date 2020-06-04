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
        int nr = ct.getRows().orElse(0);
        int nc = ct.getColumns().orElse(0);
        if (nr == 0 || nc == 0) {
            throw new IOException(String.format("Unable to write tiff file when the row[%d] or column[%d] dimensions are 0.", nr, nc));
        }
        if (ct.getPixelData().isEmpty() || ct.getPixelData().get().isEmpty()) {
            throw new IOException("No pixeldata is available to write to a tiff file.");
        }

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
            long pixel = ct.getPixelData().get().get(i);
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
                double p = ct.getPixelData().get().get(k);
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

    private static void writeString(Attributes attr, int tag, VR vr, Optional<String> optionalValue) {
        if (attr == null || optionalValue.isEmpty()) return;
        attr.setString(tag, vr, optionalValue.get());
    }

    private static void writeString(Attributes attr, ElementDictionary dict, int tag, Optional<String> optionalValue) {
        if (attr == null || dict == null || optionalValue.isEmpty()) return;
        attr.setString(tag, dict.vrOf(tag), optionalValue.get());
    }

    private static void writeInt(Attributes attr, ElementDictionary dict, int tag, Optional<Integer> optionalValue) {
        if (attr == null || dict == null || optionalValue.isEmpty()) return;
        attr.setInt(tag, dict.vrOf(tag), optionalValue.get());
    }

    private static void writeDouble(Attributes attr, ElementDictionary dict, int tag, Optional<Double> optionalValue) {
        if (attr == null || dict == null || optionalValue.isEmpty()) return;
        attr.setDouble(tag, dict.vrOf(tag), optionalValue.get());
    }

    private static void writeDoubles(Attributes attr, ElementDictionary dict, int tag, Optional<Double[]> optionalValue) {
        if (attr == null || dict == null || optionalValue.isEmpty()) return;
        var value = optionalValue.get();
        var n = value.length;
        if (n == 0) return;
        var tmp = new double[n];
        for (int i = 0; i < n; i++) {
            tmp[i] = value[i];
        }
        attr.setDouble(tag, dict.vrOf(tag), tmp);
    }

    public static Optional<Attributes> rtdose(RTDose dose) {
        var dict = ElementDictionary.getStandardElementDictionary();
        if (dose.getModality().isEmpty() || dose.getModality().get() != Modality.RTDOSE) {
            log.error("RTDose instance has an invalid modality: " + dose.getModality().toString());
            return Optional.empty();
        }

        Attributes root = new Attributes();
        writeString(root, dict, Tag.SpecificCharacterSet, dose.getSpecificCharacterSet());
        root.setString(Tag.InstanceCreationDate, dict.vrOf(Tag.InstanceCreationDate), DicomUtils.getLocalDateNow());
        root.setString(Tag.InstanceCreationTime, dict.vrOf(Tag.InstanceCreationTime), DicomUtils.getLocalTimeNow());
        writeString(root, dict, Tag.SOPClassUID, dose.getSopClassUID());
        writeString(root, dict, Tag.SOPInstanceUID, dose.getSopInstanceUID());
        writeString(root, dict, Tag.StudyDate, dose.getStudyDate().map(localDate -> localDate.format(DicomUtils.getDateFormatter())));
        writeString(root, dict, Tag.StudyTime, dose.getStudyTime().map(localDate -> localDate.format(DicomUtils.getTimeFormatter())));
        writeString(root, dict, Tag.AccessionNumber, dose.getAccessionNumber());
        writeString(root, dict, Tag.Modality, dose.getModality().map(Enum::toString));
        writeString(root, dict, Tag.Manufacturer, dose.getManufacturer());
        writeString(root, dict, Tag.ReferringPhysicianName, dose.getReferringPhysicianName());
        writeString(root, dict, Tag.StationName, dose.getStationName());
        writeString(root, dict, Tag.SeriesDescription, dose.getSeriesDescription());
        writeString(root, dict, Tag.ManufacturerModelName, dose.getManufacturerModelName());
        writeString(root, dict, Tag.PatientName, dose.getPatientName());
        writeString(root, dict, Tag.PatientID, dose.getPatientID());
        writeString(root, dict, Tag.PatientBirthDate, dose.getPatientBirthDate().map(localDate -> {
            return localDate.format(DicomUtils.getDateFormatter());
        }));
        writeString(root, dict, Tag.PatientSex, dose.getPatientSex());
        writeDouble(root, dict, Tag.SliceThickness, dose.getSliceThicknes());
        writeString(root, dict, Tag.DeviceSerialNumber, dose.getDeviceSerialNumber());
        writeString(root, dict, Tag.SoftwareVersions, dose.getSoftwareVersions());
        writeString(root, dict, Tag.StudyInstanceUID, dose.getStudyInstanceUID());
        writeString(root, dict, Tag.SeriesInstanceUID, dose.getSeriesInstanceUID());
        writeString(root, dict, Tag.StudyID, dose.getStudyID());
        writeInt(root, dict, Tag.SeriesNumber, dose.getSeriesNumber());
        writeInt(root, dict, Tag.InstanceNumber, dose.getInstanceNumber());
        writeDoubles(root, dict, Tag.ImagePositionPatient, dose.getImagePositionPatient());
        writeDoubles(root, dict, Tag.ImageOrientationPatient, dose.getImageOrientationPatient());
        writeString(root, dict, Tag.FrameOfReferenceUID, dose.getFrameOfReferenceUID());
        writeString(root, dict, Tag.PositionReferenceIndicator, dose.getPositionReferenceIndicator());
        writeInt(root, dict, Tag.SamplesPerPixel, dose.getSamplesPerPixel());
        writeString(root, dict, Tag.PhotometricInterpretation, dose.getPhotometricInterpretation().map(Enum::toString));
        writeInt(root, dict, Tag.NumberOfFrames, dose.getNumberOfFrames());
        writeInt(root, dict, Tag.FrameIncrementPointer, dose.getFrameIncrementPointer());
        writeInt(root, dict, Tag.Rows, dose.getRows());
        writeInt(root, dict, Tag.Columns, dose.getColumns());
        writeDoubles(root, dict, Tag.PixelSpacing, dose.getPixelSpacing());
        writeInt(root, dict, Tag.BitsAllocated, dose.getBitsAllocated());
        writeInt(root, dict, Tag.BitsStored, dose.getBitsStored());
        writeInt(root, dict, Tag.HighBit, dose.getHighBit());
        if (dose.getPixelRepresentation().isEmpty()) {
            log.error("RTDose is missing the required pixel representation.");
            return Optional.empty();
        }
        writeString(root, dict, Tag.PixelRepresentation, dose.getPixelRepresentation().map(Enum::toString));
        writeString(root, dict, Tag.DoseUnits, dose.getDoseUnits());
        writeString(root, dict, Tag.DoseType, dose.getDoseType());
        writeString(root, dict, Tag.DoseSummationType, dose.getDoseSummationType());
        writeDoubles(root, dict, Tag.GridFrameOffsetVector, dose.getGridFrameOffsetVector());
        writeDouble(root, dict, Tag.DoseGridScaling, dose.getDoseGridScaling());
        writeString(root, dict, Tag.TissueHeterogeneityCorrection, dose.getTissueHeterogeneityCorrection());

        if (!addSequence(root, Tag.DVHSequence, dose.getDvhSequence(), dict, Writer::dvh)) {
            log.error("Unable to read DVHSequence");
        }

        if (!addSequence(root, Tag.ReferencedRTPlanSequence, dose.getReferencedRTPlanSequence(), dict, Writer::referencedSOPClassInstance)) {
            log.error("Unable to read ReferencedRTPlanSequence");
        }

        if (!addSequence(root, Tag.ReferencedStructureSetSequence, dose.getReferencedStructureSetSequence(), dict, Writer::referencedSOPClassInstance)) {
            log.error("Unable to read ReferencedStructureSetSequence");
        }

        // dose.getPixelData()
        // Checks based on info found in C.8.8.3.4
        if (dose.getPhotometricInterpretation().isEmpty() || dose.getPhotometricInterpretation().get() != PhotometricInterpretation.MONOCHROME2) {
            log.error("The photometrix interpretation for RTDOSE file must be MONOCHROME2");
            return Optional.empty();
        }
        if (dose.getBitsAllocated().isEmpty() || (dose.getBitsAllocated().get() != 16 && dose.getBitsAllocated().get() != 32)) {
            log.error("The number of bits allocated must be either 16 or 32");
            return Optional.empty();
        }
        if (dose.getBitsAllocated().isEmpty() || dose.getBitsStored().get() != dose.getBitsAllocated().get()) {
            log.error("The number of bits stored but be equal to the number of bits allocated.");
            return Optional.empty();
        }
        if (dose.getPixelRepresentation().isEmpty() || dose.getPixelRepresentation().get() != PixelRepresentation.UNSIGNED) {
            log.error("Supported pixel representation for RTDOSE pixel data is unsigned integer values. Not the two's complement integer representation.");
            return Optional.empty();
        }
        if (dose.getNumberOfFrames().isEmpty() || dose.getNumberOfFrames().get() < 1) {
            log.error("Number of frames must be equal to 1 or higher.");
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
        int bs = dose.getBitsAllocated().get();
        int npixels = dose.getNumberOfFrames().get() * dose.getRows().get() * dose.getColumns().get();
        if (bs == 16) pb = ByteBuffer.allocate(npixels * 2);
        if (bs == 32) pb = ByteBuffer.allocate(npixels * 4);

        assert pb != null;
        pb.order(ByteOrder.LITTLE_ENDIAN);
        int k = 0;
        for (int i = 0; i < npixels; i++) {
            var val = dose.getPixelData().get().get(i);
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

    private static <T> boolean addSequence(Attributes parent, int seqTag, Optional<List<T>> optionalItems, ElementDictionary dict, BiFunction<T, ElementDictionary, Optional<Attributes>> function) {
        if (parent == null || dict == null) {
            if (parent == null) log.error("Parent of the sequence can't be null.");
            if (dict == null) log.error("DICOM dictionary can't be null.");
            return false;
        }
        if (optionalItems.isEmpty()) {
            log.info(String.format("skipping empty sequence tag: %s", dict.keywordOf(seqTag)));
            return true;
        }
        var items = optionalItems.get();
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
        writeString(attr, dict, Tag.DVHType, item.getDvhType());
        writeString(attr, dict, Tag.DoseUnits, item.getDoseUnits());
        writeString(attr, dict, Tag.DoseType, item.getDoseType());
        writeDouble(attr, dict, Tag.DVHDoseScaling, item.getDvhDoseScaling());
        writeString(attr, dict, Tag.DVHVolumeUnits, item.getDvhVolumeUnits());
        writeDoubles(attr, dict, Tag.DVHData, item.getDvhData());
        if (!addSequence(attr, Tag.DVHReferencedROISequence, item.getDvhReferencedROISequence(), dict, Writer::dvhReferencedROI)) {

            log.error("Unable to write DVHReferencedROISequence");
            return Optional.empty();
        }
        writeInt(attr, dict, Tag.DVHNumberOfBins, item.getDvhNumberOfBins());
        writeDouble(attr, dict, Tag.DVHMinimumDose, item.getDvhMinimumDose());
        writeDouble(attr, dict, Tag.DVHMaximumDose, item.getDvhMaximumDose());
        writeDouble(attr, dict, Tag.DVHMeanDose, item.getDvhMeanDose());
        return Optional.of(attr);
    }

    private static Optional<Attributes> dvhReferencedROI(DVHReferencedROIItem item, ElementDictionary dict) {
        if (item == null || dict == null) return Optional.empty();
        Attributes attr = new Attributes();
        writeString(attr, dict, Tag.DVHROIContributionType, item.getDvhROIContributionType());
        writeInt(attr, dict, Tag.ReferencedROINumber, item.getReferencedROINumber());
        return Optional.of(attr);
    }

    private static Optional<Attributes> referencedSOPClassInstance(ReferencedSOPClassInstanceItem item, ElementDictionary dict) {
        if (item == null || dict == null) return Optional.empty();
        Attributes attr = new Attributes();
        writeString(attr, dict, Tag.ReferencedSOPClassUID, item.getReferencedSOPClassUID());
        writeString(attr, dict, Tag.ReferencedSOPInstanceUID, item.getReferencedSOPInstanceUID());
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
