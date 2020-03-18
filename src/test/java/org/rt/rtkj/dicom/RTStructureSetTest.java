package org.rt.rtkj.dicom;

import org.dcm4che3.data.UID;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RTStructureSetTest {

    @Test
    public void read() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var file = Paths.get(resourceDirectory.toAbsolutePath().toString(), "carpet", "ct3",
                "RS1.2.752.243.1.1.20191119180209579.1200.70340.dcm").toFile();
        assertTrue(file.exists());
        assertTrue(file.isFile());
        var doj = DicomFactory.read(file);
        assertTrue(doj != null);
        assertTrue(doj.getErrors().isEmpty());
        assertTrue(doj.hasStructureSet());
        var optSs = doj.getRtstruct();
        assertTrue(optSs.isPresent());
        var ss = optSs.get();
        assertEquals(LocalDate.of(2019, 11, 19), ss.getInstanceCreationDate());
        assertEquals(LocalTime.of(18, 2, 9), ss.getInstanceCreationTime());
        assertEquals(UID.RTStructureSetStorage, ss.getSOPClassUID());
        assertEquals("1.2.752.243.1.1.20191119180209579.1200.70340", ss.getSOPInstanceUID());
        assertEquals(LocalDate.of(2018, 10, 30), ss.getStudyDate());
        assertEquals(LocalTime.of(18, 20, 11), ss.getStudyTime());
        assertEquals("1585", ss.getAccessionNumber());
        assertEquals(Modality.RTSTRUCT, ss.getModality());
        assertEquals("", ss.getReferringPhysicianName());
        assertEquals("RS: Unapproved Structure Set", ss.getSeriesDescription());
        assertEquals("", ss.getOperatorsName());
        assertEquals("RayStation", ss.getManufacturerModelName());
        assertTrue(ss.getPatientName().startsWith("carpet"));
        assertEquals("X021000", ss.getPatientID());
        assertEquals(LocalDate.of(2000, 10, 2), ss.getPatientBirthDate());
        assertEquals("M", ss.getPatientSex());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", ss.getStudyInstanceUID());
        assertEquals("1.2.752.243.1.1.20191119180209579.1200.70340.1", ss.getSeriesInstanceUID());
        assertEquals("CT1", ss.getStudyID());
        assertEquals(1, ss.getSeriesNumber());
        assertEquals("", ss.getFrameOfReferenceUID());
        assertEquals("", ss.getPositionReferenceIndicator());
        assertEquals("RS: Unapproved", ss.getStructureSetLabel());
        assertEquals(LocalDate.of(2019, 11, 19), ss.getStructureSetDate());
        assertEquals(LocalTime.of(18, 2, 9), ss.getStructureSetTime());

        var referencedFrameOfReferenceSequence = ss.getReferencedFrameOfReferenceSequence();
        assertEquals(1, referencedFrameOfReferenceSequence.size());
        var referencedFrameOfReferenceItem = referencedFrameOfReferenceSequence.get(0);
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610", referencedFrameOfReferenceItem.getFrameOfReferenceUID());
        var rtReferencedStudySequence = referencedFrameOfReferenceItem.getRtReferencedStudySequence();
        assertEquals(1, rtReferencedStudySequence.size());
        var rtReferencedStudyItem = rtReferencedStudySequence.get(0);
        assertEquals(UID.DetachedStudyManagementSOPClassRetired, rtReferencedStudyItem.getReferencedSOPClassUID());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", rtReferencedStudyItem.getReferencedSOPInstanceUID());
        var rtReferencedSeriesSequence = rtReferencedStudyItem.getRtReferencedSeriesSequence();
        assertEquals(1, rtReferencedSeriesSequence.size());
        var rtReferencedSeriesItem = rtReferencedSeriesSequence.get(0);
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.871910", rtReferencedSeriesItem.getSeriesInstanceUID());
        var contourImageSequence = rtReferencedSeriesItem.getContourImageSequence();
        assertEquals(200, contourImageSequence.size());
        var expContourImageSequence = new ReferencedSOPClassInstanceItem();
        expContourImageSequence.setReferencedSOPClassUID(UID.CTImageStorage);
        expContourImageSequence.setReferencedSOPInstanceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.874748");
        assertTrue(contourImageSequence.contains(expContourImageSequence));

        var structureSetROISequence = ss.getStructureSetROISequence();
        assertEquals(5, structureSetROISequence.size());
        var structureSetROI0 = new StructureSetROIItem();
        structureSetROI0.setROINumber(2);
        structureSetROI0.setReferencedFrameOfReferenceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610");
        structureSetROI0.setROIName("GTV");
        structureSetROI0.setROIGenerationAlgorithm("SEMIAUTOMATIC");

        var structureSetROI1 = new StructureSetROIItem();
        structureSetROI1.setROINumber(3);
        structureSetROI1.setReferencedFrameOfReferenceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610");
        structureSetROI1.setROIName("PTV");
        structureSetROI1.setROIGenerationAlgorithm("SEMIAUTOMATIC");

        var structureSetROI2 = new StructureSetROIItem();
        structureSetROI2.setROINumber(4);
        structureSetROI2.setReferencedFrameOfReferenceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610");
        structureSetROI2.setROIName("External");
        structureSetROI2.setROIGenerationAlgorithm("SEMIAUTOMATIC");

        var structureSetROI3 = new StructureSetROIItem();
        structureSetROI3.setROINumber(5);
        structureSetROI3.setReferencedFrameOfReferenceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610");
        structureSetROI3.setROIName("TAFEL_SYN23");
        structureSetROI3.setROIGenerationAlgorithm("SEMIAUTOMATIC");

        var structureSetROI4 = new StructureSetROIItem();
        structureSetROI4.setROINumber(1);
        structureSetROI4.setReferencedFrameOfReferenceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610");
        structureSetROI4.setROIName("iso");
        structureSetROI4.setROIGenerationAlgorithm("SEMIAUTOMATIC");

        assertTrue(structureSetROISequence.contains(structureSetROI0));
        assertTrue(structureSetROISequence.contains(structureSetROI1));
        assertTrue(structureSetROISequence.contains(structureSetROI2));
        assertTrue(structureSetROISequence.contains(structureSetROI3));
        assertTrue(structureSetROISequence.contains(structureSetROI4));

    }
}
