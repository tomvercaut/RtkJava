package org.rt.rtkj.dicom;

import org.apache.commons.math3.util.Precision;
import org.dcm4che3.data.UID;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.Option;
import org.rt.rtkj.ResourceFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpatialRegistrationTest {

    @Test
    public void read() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var file = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "carpet", "ct1",
                "REG1.2.752.243.1.1.20191009143733562.5000.74370.dcm").toFile();
        assertTrue(file.exists());
        assertTrue(file.isFile());
        var dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
        var meta = dis.readFileMetaInformation();
        var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        var attr = dis.readDataset(-1, -1);
        var optSr = Reader.spatialRegistration(meta, attr, bo);
        assertTrue(optSr.isPresent());
        var sr = optSr.get();

        assertEquals("ISO_IR 100", sr.getSpecificCharacterSet().get());
        assertEquals(LocalDate.of(2020, 2, 19), sr.getInstanceCreationDate().get());
        assertEquals(LocalTime.of(18, 12, 33), sr.getInstanceCreationTime().get());
        assertEquals(UID.SpatialRegistrationStorage, sr.getSOPClassUID().get());
        assertEquals("1.2.752.243.1.1.20191009143733562.5000.74370", sr.getSOPInstanceUID().get());
        assertEquals(LocalDate.of(2019, 10, 2), sr.getStudyDate().get());
        assertEquals(LocalDate.of(2020, 2, 29), sr.getSeriesDate().get());
        assertEquals(LocalDate.of(2019, 10, 9), sr.getContentDate().get());
        assertEquals(LocalTime.of(10, 53, 9), sr.getStudyTime().get());
        assertEquals(LocalTime.of(16, 23, 54), sr.getSeriesTime().get());
        assertEquals(LocalTime.of(14, 37, 33), sr.getContentTime().get());
        assertEquals("1662", sr.getAccessionNumber().get());
        assertEquals(Modality.REG, sr.getModality().get());
        assertEquals("RaySearch Laboratories", sr.getManufacturer().get());
        assertTrue(sr.getReferringPhysicianName().isEmpty());
        assertTrue(sr.getSeriesDescription().isEmpty());
        {
            var referencedSeriesSequence = sr.getReferencedSeriesSequence();
            assertEquals(1, referencedSeriesSequence.get().size());
            var referencedSerie = referencedSeriesSequence.get().get(0);
            var referencedInstanceSequence = referencedSerie.getReferencedInstanceSequence().get();
            var referencedInstanceItem = new ReferencedSOPClassInstanceItem();
            referencedInstanceItem.setReferencedSOPClassUID(Option.of(UID.CTImageStorage));
            referencedInstanceItem.setReferencedSOPInstanceUID(Option.of("1.2.392.200036.9116.2.6.1.16.1613471639.1569981394.848382"));
            assertEquals(referencedInstanceItem, referencedInstanceSequence.get(0));
            assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1569981382.889372", referencedSerie.getSeriesInstanceUID().get());
        }
        {
            var studiesContainingOtherReferencedInstancesSequence = sr.getStudiesContainingOtherReferencedInstancesSequence().get();
            var studiesContainingOtherReferencedInstancesItem = studiesContainingOtherReferencedInstancesSequence.get(0);
            var referencedSeriesSequence = studiesContainingOtherReferencedInstancesItem.getReferencedSeriesSequence();
            ReferencedSeriesItem referencedSeriesItem = referencedSeriesSequence.get().get(0);
            var referencedInstanceItem = new ReferencedSOPClassInstanceItem();
            referencedInstanceItem.setReferencedSOPClassUID(Option.of(UID.CTImageStorage));
            referencedInstanceItem.setReferencedSOPInstanceUID(Option.of("1.2.392.200036.9116.2.6.1.16.1613471639.1543482020.16715"));
            assertEquals(referencedInstanceItem, referencedSeriesItem.getReferencedInstanceSequence().get().get(0));
            assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1543481929.823084", referencedSeriesItem.getSeriesInstanceUID().get());
            assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1543481030.280100", studiesContainingOtherReferencedInstancesItem.getStudyInstanceUID().get());
        }

        assertEquals("carpet^kimmanon", sr.getPatientName().get());
        assertEquals("X021000", sr.getPatientID().get());
        assertEquals("20001002", sr.getPatientBirthDate().get());
        assertEquals("M", sr.getPatientSex().get());
        assertEquals("6.1.1.2 (Dicom Export)", sr.getSoftwareVersions().get());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1569981043.583561", sr.getStudyInstanceUID().get());
        assertEquals("1.2.752.243.1.1.20191009143733562.5000.74370.1", sr.getSeriesInstanceUID().get());
        assertEquals("CT1", sr.getStudyID().get());
        assertTrue(sr.getSeriesNumber().isEmpty());
        assertEquals(0, sr.getInstanceNumber().get());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1569981196.949149", sr.getFrameOfReferenceUID().get());
        assertTrue(sr.getPositionReferenceIndicator().isEmpty());
        assertEquals("REGISTRATION", sr.getContentLabel().get());
        assertTrue(sr.getContentDescription().isEmpty());
        assertEquals("RayStation", sr.getContentCreatorName().get());
//        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1569981196.949149", .getFrameOfReferenceUID());

        var registrationSequence = sr.getRegistrationSequence();
        assertEquals(2, registrationSequence.get().size());
        var registrationItem0 = registrationSequence.get().get(0);
        {
            assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1569981196.949149", registrationItem0.getFrameOfReferenceUID().get());
            var referencedImageSequence = registrationItem0.getReferencedImageSequence().get();
            var b = referencedImageSequence.get(0);
            var a = new ReferencedSOPClassInstanceItem();
            a.setReferencedSOPClassUID(Option.of(UID.CTImageStorage));
            a.setReferencedSOPInstanceUID(Option.of("1.2.392.200036.9116.2.6.1.16.1613471639.1569981394.848382"));
            assertEquals(a, b);

            var matrixRegistrationSequence = registrationItem0.getMatrixRegistrationSequence();
            assertEquals(1, matrixRegistrationSequence.get().size());
            var matrixSequence = matrixRegistrationSequence.get().get(0).getMatrixSequence();
            assertEquals(1, matrixSequence.get().size());
            var matrix = matrixSequence.get().get(0);
            assertEquals(TransformationMatrixType.RIGID, matrix.getFrameOfReferenceTransformationMatrixType().get());
            double exp[] = new double[]{1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
            Double[] actual = matrix.getFrameOfReferenceTransformationMatrix().get();
            assertEquals(exp.length, actual.length);
            for (int i = 0; i < exp.length; i++) {
                assertEquals(exp[i], (double) actual[i], Precision.EPSILON);
            }
        }

        var registrationItem1 = registrationSequence.get().get(1);
        {

            assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1543481617.727710", registrationItem1.getFrameOfReferenceUID().get());
            var referencedImageSequence = registrationItem1.getReferencedImageSequence();
            var b = referencedImageSequence.get().get(0);
            var a = new ReferencedSOPClassInstanceItem();
            a.setReferencedSOPClassUID(Option.of(UID.CTImageStorage));
            a.setReferencedSOPInstanceUID(Option.of("1.2.392.200036.9116.2.6.1.16.1613471639.1543482020.16715"));
            assertEquals(a, b);

            var matrixRegistrationSequence = registrationItem1.getMatrixRegistrationSequence().get();
            assertEquals(1, matrixRegistrationSequence.size());
            var matrixSequence = matrixRegistrationSequence.get(0).getMatrixSequence();
            assertEquals(1, matrixSequence.get().size());
            var matrix = matrixSequence.get().get(0);
            assertEquals(TransformationMatrixType.RIGID, matrix.getFrameOfReferenceTransformationMatrixType().get());
            double exp[] = new double[]{1, 0, 0, -76.89132, 0, 1, 0, -40.52152, 0, 0, 1, -40.24639, 0, 0, 0, 1};
            Double actual[] = matrix.getFrameOfReferenceTransformationMatrix().get();
            assertEquals(exp.length, actual.length);
            for (int i = 0; i < exp.length; i++) {
                assertEquals(exp[i], (double) actual[i], Precision.EPSILON);
            }
        }

    }
}
