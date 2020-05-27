package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.rt.rtkj.model.Image3D;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Optional;

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
        ;
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

    public Optional<Attributes> rtdose(CT3d volume, Image3D dose) {
        if (volume.isEmpty()) {
            log.error("CT does not contain any slices.");
            return Optional.empty();
        }
        var optCt = volume.get(0);
        if (optCt.isEmpty()) {
            log.error("No CT slice available in 3D volume.");
            return Optional.empty();
        }
        var slice = optCt.get();


        Attributes root = new Attributes();
        root.setSpecificCharacterSet("ISO_IR 100");
        root.setString(Tag.InstanceCreationDate, VR.DA, DicomUtils.getLocalDateNow());
        root.setString(Tag.InstanceCreationTime, VR.TM, DicomUtils.getLocalTimeNow());
        root.setString(Tag.SOPClassUID, VR.UI, UID.RTDoseStorage);
        {
            var sopInstanceUID = slice.getSOPInstanceUID();
            int i = sopInstanceUID.lastIndexOf(".");
            if (i != -1) {
                sopInstanceUID = sopInstanceUID.substring(0, i + 1);
            }
            sopInstanceUID += DicomUtils.getLocalDateTimeNow();
            root.setString(Tag.SOPInstanceUID, VR.UI, sopInstanceUID);
        }
        root.setString(Tag.StudyDate, VR.DA, slice.getStudyDate().format(DicomUtils.getDateFormatter()));
        root.setString(Tag.StudyTime, VR.DA, slice.getStudyTime().format(DicomUtils.getTimeFormatter()));
        root.setString(Tag.AccessionNumber, VR.SH, "");
        root.setString(Tag.Modality, VR.CS, "RTDOSE");
        root.setString(Tag.Manufacturer, VR.LO, "");
        root.setString(Tag.ReferringPhysicianName, VR.PN, slice.getReferringPhysicianName());
        try {
            root.setString(Tag.StationName, VR.SH, InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            root.setString(Tag.StationName, VR.SH, "unknown hostname");
        }
        root.setString(Tag.SeriesDescription, VR.LO, "");
        root.setString(Tag.ManufacturerModelName, VR.LO, "");
        root.setString(Tag.PatientName, VR.PN, slice.getPatientName());
        root.setString(Tag.PatientID, VR.LO, slice.getPatientID());
        if (slice.getPatientBirthDate() != null) {
            root.setString(Tag.PatientBirthDate, VR.DA, slice.getPatientBirthDate().format(DicomUtils.getDateFormatter()));
        }
        root.setString(Tag.PatientSex, VR.CS, slice.getPatientSex());
        root.setString(Tag.SliceThickness, VR.DS, "");
        root.setString(Tag.DeviceSerialNumber, VR.LO, "0");
        root.setString(Tag.SoftwareVersions, VR.LO, "0.0.1");

        if (slice.getStudyInstanceUID().isBlank()) {
            log.error("CT volume requires a study instance UID.");
            return Optional.empty();
        }
        root.setString(Tag.StudyInstanceUID, VR.UI, slice.getStudyInstanceUID());
        {
            var optSeriesInstance = volume.getStudyInstanceUID();
            if (optSeriesInstance.isEmpty()) {
                log.error("CT volume requires a series instance UID.");
                return Optional.empty();
            }
            root.setString(Tag.SeriesInstanceUID, VR.UI, optSeriesInstance.get());
        }
        root.setString(Tag.StudyID, VR.SH, slice.getStudyID());
        root.setInt(Tag.SeriesNumber, VR.IS, slice.getSeriesNumber());
        root.setString(Tag.InstanceNumber, VR.IS, "");
        {
            var optImagePositionPatient = dose.getImagePositionPatient();
            if (optImagePositionPatient.isEmpty()) {
                log.error("Image position patient in dose grid is empty.");
                return Optional.empty();
            }
            root.setDouble(Tag.ImagePositionPatient, VR.DS, optImagePositionPatient.get());
        }
        if (volume.getFrameOfReferenceUID().isEmpty() || !volume.getFrameOfReferenceUID().get().equals(slice.getFrameOfReferenceUID())) {
            log.error(String.format("Frame of reference UID in the CT [%s] and the dose grid [%s] is not equal.", volume.getFrameOfReferenceUID().orElse(""), dose.getFrameOfReferenceUID()));
            return Optional.empty();
        }
        root.setString(Tag.FrameOfReferenceUID, VR.UI, slice.getFrameOfReferenceUID());

        return Optional.of(root);
    }
}
