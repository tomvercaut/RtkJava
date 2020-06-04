package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.UID;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
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
        assertEquals(LocalDate.of(2019, 11, 19), ss.getInstanceCreationDate().get());
        assertEquals(LocalTime.of(18, 2, 9), ss.getInstanceCreationTime().get());
        assertEquals(UID.RTStructureSetStorage, ss.getSOPClassUID().get());
        assertEquals("1.2.752.243.1.1.20191119180209579.1200.70340", ss.getSOPInstanceUID().get());
        assertEquals(LocalDate.of(2018, 10, 30), ss.getStudyDate().get());
        assertEquals(LocalTime.of(18, 20, 11), ss.getStudyTime().get());
        assertEquals("1585", ss.getAccessionNumber().get());
        assertEquals(Modality.RTSTRUCT, ss.getModality().get());
        assertEquals("", ss.getReferringPhysicianName().get());
        assertEquals("RS: Unapproved Structure Set", ss.getSeriesDescription().get());
        assertEquals("", ss.getOperatorsName().get());
        assertEquals("RayStation", ss.getManufacturerModelName().get());
        assertTrue(ss.getPatientName().get().startsWith("carpet"));
        assertEquals("X021000", ss.getPatientID().get());
        assertEquals(LocalDate.of(2000, 10, 2), ss.getPatientBirthDate().get());
        assertEquals("M", ss.getPatientSex().get());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", ss.getStudyInstanceUID().get());
        assertEquals("1.2.752.243.1.1.20191119180209579.1200.70340.1", ss.getSeriesInstanceUID().get());
        assertEquals("CT1", ss.getStudyID().get());
        assertEquals(1, ss.getSeriesNumber().get());
        assertEquals("", ss.getFrameOfReferenceUID().get());
        assertEquals("", ss.getPositionReferenceIndicator().get());
        assertEquals("RS: Unapproved", ss.getStructureSetLabel().get());
        assertEquals(LocalDate.of(2019, 11, 19), ss.getStructureSetDate().get());
        assertEquals(LocalTime.of(18, 2, 9), ss.getStructureSetTime().get());

        var referencedFrameOfReferenceSequence = ss.getReferencedFrameOfReferenceSequence();
        assertEquals(1, referencedFrameOfReferenceSequence.get().size());
        var referencedFrameOfReferenceItem = referencedFrameOfReferenceSequence.get().get(0);
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610", referencedFrameOfReferenceItem.getFrameOfReferenceUID().get());
        var rtReferencedStudySequence = referencedFrameOfReferenceItem.getRtReferencedStudySequence();
        assertEquals(1, rtReferencedStudySequence.get().size());
        var rtReferencedStudyItem = rtReferencedStudySequence.get().get(0);
        assertEquals(UID.DetachedStudyManagementSOPClassRetired, rtReferencedStudyItem.getReferencedSOPClassUID().get());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", rtReferencedStudyItem.getReferencedSOPInstanceUID().get());
        var rtReferencedSeriesSequence = rtReferencedStudyItem.getRtReferencedSeriesSequence().get();
        assertEquals(1, rtReferencedSeriesSequence.size());
        var rtReferencedSeriesItem = rtReferencedSeriesSequence.get(0);
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.871910", rtReferencedSeriesItem.getSeriesInstanceUID());
        var contourImageSequence = rtReferencedSeriesItem.getContourImageSequence();
        assertEquals(200, contourImageSequence.get().size());
        var expContourImageItem = new ReferencedSOPClassInstanceItem();
        expContourImageItem.setReferencedSOPClassUID(UID.CTImageStorage);
        expContourImageItem.setReferencedSOPInstanceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.874748");
        assertTrue(contourImageSequence.contains(expContourImageItem));

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

        var roiContourSequence = ss.getRoiContourSequence();
        assertEquals(5, roiContourSequence.size());
        var roiContour0 = roiContourSequence.get(0);
        assertArrayEquals(new int[]{255, 0, 0}, roiContour0.getROIDisplayColor());
        var contourSequence = roiContour0.getContourSequence();
        var expContourItem = new ContourItem();

        expContourImageItem.setReferencedSOPClassUID(UID.CTImageStorage);
        expContourImageItem.setReferencedSOPInstanceUID("1.2.392.200036.9116.2.6.1.16.1613471639.1540891569.28542");
        expContourItem.getContourImageSequence().add(expContourImageItem);
        expContourItem.setContourGeometricType("CLOSED_PLANAR");
        expContourItem.setNumberOfContourPoints(4);
        expContourItem.setContourNumber(0);
        expContourItem.getContourData().add(-10.63135);
        expContourItem.getContourData().add(-5.951199);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-11.47545);
        expContourItem.getContourData().add(-4.770328);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-12.06245);
        expContourItem.getContourData().add(-3.736268);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-12.68112);
        expContourItem.getContourData().add(-2.245236);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-13.07751);
        expContourItem.getContourData().add(-0.6208908);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-13.18278);
        expContourItem.getContourData().add(1.30117);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-13.04892);
        expContourItem.getContourData().add(2.799099);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-12.60062);
        expContourItem.getContourData().add(4.606259);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-11.59987);
        expContourItem.getContourData().add(6.659985);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-10.41402);
        expContourItem.getContourData().add(8.291614);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-9.385978);
        expContourItem.getContourData().add(9.299986);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-7.935406);
        expContourItem.getContourData().add(10.37506);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-6.008111);
        expContourItem.getContourData().add(11.31731);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-3.722468);
        expContourItem.getContourData().add(11.86599);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-2.037528);
        expContourItem.getContourData().add(11.94271);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-0.6581482);
        expContourItem.getContourData().add(11.86544);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(1.555591);
        expContourItem.getContourData().add(11.3264);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(2.979898);
        expContourItem.getContourData().add(10.63934);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(4.33293);
        expContourItem.getContourData().add(9.785913);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(5.091006);
        expContourItem.getContourData().add(9.173659);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(5.992604);
        expContourItem.getContourData().add(8.278795);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(7.162805);
        expContourItem.getContourData().add(6.673681);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(8.170412);
        expContourItem.getContourData().add(4.580839);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(8.605498);
        expContourItem.getContourData().add(2.81587);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(8.751563);
        expContourItem.getContourData().add(1.026244);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(8.653608);
        expContourItem.getContourData().add(-0.5564451);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(8.089637);
        expContourItem.getContourData().add(-2.734057);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(7.631644);
        expContourItem.getContourData().add(-3.732856);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(6.457397);
        expContourItem.getContourData().add(-5.623788);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(5.717219);
        expContourItem.getContourData().add(-6.500515);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(3.967777);
        expContourItem.getContourData().add(-8.012704);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(3.369239);
        expContourItem.getContourData().add(-8.415419);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(2.235971);
        expContourItem.getContourData().add(-8.937058);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(0.8176045);
        expContourItem.getContourData().add(-9.475734);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-0.896889);
        expContourItem.getContourData().add(-9.822879);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-2.108072);
        expContourItem.getContourData().add(-9.957109);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-3.590453);
        expContourItem.getContourData().add(-9.810884);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-5.217731);
        expContourItem.getContourData().add(-9.483482);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-6.615079);
        expContourItem.getContourData().add(-8.957241);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-7.796721);
        expContourItem.getContourData().add(-8.418862);
        expContourItem.getContourData().add(-12.0);
        expContourItem.getContourData().add(-9.393643);
        expContourItem.getContourData().add(-7.224891);
        expContourItem.getContourData().add(-12.0);
        boolean found = false;
        for (var ci : contourSequence) {
            if (ci.getContourNumber() == 0 && ci.getNumberOfContourPoints() == 41 &&
                    ci.getContourGeometricType().equals("CLOSED_PLANAR") &&
                    ci.getContourImageSequence().contains(expContourImageItem)) {
//                found = true;
                if (!found) {
                    if (ci.getContourData().size() == expContourItem.getContourData().size()) {
                        var n = ci.getContourData().size();
                        boolean b = true;
                        for (int i = 0; i < n; i++) {
                            long x = (long) (expContourItem.getContourData().get(i) * 100000);
                            long y = (long) (ci.getContourData().get(i) * 100000);
                            if (x != y) {
                                b = false;
                            }
                        }
                        if (b) found = true;
                    }
                }
            }
        }
        assertTrue(found);

        var rtROIObservationSequence = ss.getRtROIObservationsSequence();
        var rtROIObservation0 = new RTROIObservationsItem();
        rtROIObservation0.setObservationNumber(1);
        rtROIObservation0.setReferencedROINumber(2);
        rtROIObservation0.setROIObservationLabel("GTV");
        rtROIObservation0.setRTROIInterpretedType("GTV");
        rtROIObservation0.setROIInterpreter("");
        rtROIObservation0.setMaterialID("");

        var rtROIObservation1 = new RTROIObservationsItem();
        rtROIObservation1.setObservationNumber(2);
        rtROIObservation1.setReferencedROINumber(3);
        rtROIObservation1.setROIObservationLabel("PTV");
        rtROIObservation1.setRTROIInterpretedType("PTV");
        rtROIObservation1.setROIInterpreter("");
        rtROIObservation1.setMaterialID("");

        var rtROIObservation2 = new RTROIObservationsItem();
        rtROIObservation2.setObservationNumber(3);
        rtROIObservation2.setReferencedROINumber(4);
        rtROIObservation2.setROIObservationLabel("External");
        rtROIObservation2.setRTROIInterpretedType("EXTERNAL");
        rtROIObservation2.setROIInterpreter("");
        rtROIObservation2.setMaterialID("");

        var rtROIObservation3 = new RTROIObservationsItem();
        rtROIObservation3.setObservationNumber(4);
        rtROIObservation3.setReferencedROINumber(5);
        rtROIObservation3.setROIObservationLabel("TAFEL_SYN23");
        rtROIObservation3.setRTROIInterpretedType("SUPPORT");
        rtROIObservation3.setROIInterpreter("");
        var expROIPhysicalProperties0 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties0.setROIPhysicalProperty("REL_MASS_DENSITY");
        expROIPhysicalProperties0.setROIPhysicalPropertyValue(0.25);
        var expROIPhysicalProperties1 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties1.setROIPhysicalProperty("REL_ELEC_DENSITY");
        expROIPhysicalProperties1.setROIPhysicalPropertyValue(0.250037);
        var expROIPhysicalProperties2 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties2.setROIPhysicalProperty("EFFECTIVE_Z");
        expROIPhysicalProperties2.setROIPhysicalPropertyValue(6.600049);
        var expROIPhysicalProperties3 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties3.setROIPhysicalProperty("EFF_Z_PER_A");
        expROIPhysicalProperties3.setROIPhysicalPropertyValue(0.5550822);
        var expROIPhysicalProperties4 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties4.setROIPhysicalProperty("ELEM_FRACTION");
        expROIPhysicalProperties4.setROIPhysicalPropertyValue(0);
        var roiElementalComposition0 = new ROIElementalCompositionItem();
        roiElementalComposition0.setRoiElementalCompositionAtomicNumber(1);
        roiElementalComposition0.setRoiElementalCompositionAtomicMassFraction(0.11189399659633636);
        var roiElementalComposition1 = new ROIElementalCompositionItem();
        roiElementalComposition1.setRoiElementalCompositionAtomicNumber(8);
        roiElementalComposition1.setRoiElementalCompositionAtomicMassFraction(0.8881059885025024);
        expROIPhysicalProperties4.getROIElementalCompositionSequence().add(roiElementalComposition0);
        expROIPhysicalProperties4.getROIElementalCompositionSequence().add(roiElementalComposition1);
        rtROIObservation3.getROIPhysicalPropertiesSequence().add(expROIPhysicalProperties0);
        rtROIObservation3.getROIPhysicalPropertiesSequence().add(expROIPhysicalProperties1);
        rtROIObservation3.getROIPhysicalPropertiesSequence().add(expROIPhysicalProperties2);
        rtROIObservation3.getROIPhysicalPropertiesSequence().add(expROIPhysicalProperties3);
        rtROIObservation3.getROIPhysicalPropertiesSequence().add(expROIPhysicalProperties4);
        rtROIObservation3.setMaterialID("TAFEL_SYN23");

        var rtROIObservation4 = new RTROIObservationsItem();
        rtROIObservation4.setObservationNumber(5);
        rtROIObservation4.setReferencedROINumber(1);
        rtROIObservation4.setROIObservationLabel("iso");
        rtROIObservation4.setRTROIInterpretedType("ISOCENTER");
        rtROIObservation4.setROIInterpreter("");
        rtROIObservation4.setMaterialID("");

        List<RTROIObservationsItem> expRTROIObservationsSequence = new ArrayList<>();
        expRTROIObservationsSequence.add(rtROIObservation0);
        expRTROIObservationsSequence.add(rtROIObservation1);
        expRTROIObservationsSequence.add(rtROIObservation2);
        expRTROIObservationsSequence.add(rtROIObservation3);
        expRTROIObservationsSequence.add(rtROIObservation4);

        assertEquals(expRTROIObservationsSequence, rtROIObservationSequence);
        assertEquals("UNAPPROVED", ss.getApprovalStatus());
    }
}
