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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(ss.getReferringPhysicianName().isEmpty());
        assertEquals("RS: Unapproved Structure Set", ss.getSeriesDescription().get());
        assertTrue(ss.getOperatorsName().isEmpty());
        assertEquals("RayStation", ss.getManufacturerModelName().get());
        assertTrue(ss.getPatientName().get().startsWith("carpet"));
        assertEquals("X021000", ss.getPatientID().get());
        assertEquals(LocalDate.of(2000, 10, 2), ss.getPatientBirthDate().get());
        assertEquals("M", ss.getPatientSex().get());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", ss.getStudyInstanceUID().get());
        assertEquals("1.2.752.243.1.1.20191119180209579.1200.70340.1", ss.getSeriesInstanceUID().get());
        assertEquals("CT1", ss.getStudyID().get());
        assertEquals(1, ss.getSeriesNumber().get());
        assertTrue(ss.getFrameOfReferenceUID().isEmpty());
        assertTrue(ss.getPositionReferenceIndicator().isEmpty());
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
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.871910", rtReferencedSeriesItem.getSeriesInstanceUID().get());
        var contourImageSequence = rtReferencedSeriesItem.getContourImageSequence();
        assertEquals(200, contourImageSequence.get().size());
        var expContourImageItem = new ReferencedSOPClassInstanceItem();
        expContourImageItem.setReferencedSOPClassUID(Optional.of(UID.CTImageStorage));
        expContourImageItem.setReferencedSOPInstanceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.874748"));
        assertTrue(contourImageSequence.get().contains(expContourImageItem));

        var structureSetROISequence = ss.getStructureSetROISequence();
        assertEquals(5, structureSetROISequence.get().size());
        var structureSetROI0 = new StructureSetROIItem();
        structureSetROI0.setROINumber(Optional.of(2));
        structureSetROI0.setReferencedFrameOfReferenceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610"));
        structureSetROI0.setROIName(Optional.of("GTV"));
        structureSetROI0.setROIGenerationAlgorithm(Optional.of("SEMIAUTOMATIC"));

        var structureSetROI1 = new StructureSetROIItem();
        structureSetROI1.setROINumber(Optional.of(3));
        structureSetROI1.setReferencedFrameOfReferenceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610"));
        structureSetROI1.setROIName(Optional.of("PTV"));
        structureSetROI1.setROIGenerationAlgorithm(Optional.of("SEMIAUTOMATIC"));

        var structureSetROI2 = new StructureSetROIItem();
        structureSetROI2.setROINumber(Optional.of(4));
        structureSetROI2.setReferencedFrameOfReferenceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610"));
        structureSetROI2.setROIName(Optional.of("External"));
        structureSetROI2.setROIGenerationAlgorithm(Optional.of("SEMIAUTOMATIC"));

        var structureSetROI3 = new StructureSetROIItem();
        structureSetROI3.setROINumber(Optional.of(5));
        structureSetROI3.setReferencedFrameOfReferenceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610"));
        structureSetROI3.setROIName(Optional.of("TAFEL_SYN23"));
        structureSetROI3.setROIGenerationAlgorithm(Optional.of("SEMIAUTOMATIC"));

        var structureSetROI4 = new StructureSetROIItem();
        structureSetROI4.setROINumber(Optional.of(1));
        structureSetROI4.setReferencedFrameOfReferenceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610"));
        structureSetROI4.setROIName(Optional.of("iso"));
        structureSetROI4.setROIGenerationAlgorithm(Optional.of("SEMIAUTOMATIC"));

        assertTrue(structureSetROISequence.get().contains(structureSetROI0));
        assertTrue(structureSetROISequence.get().contains(structureSetROI1));
        assertTrue(structureSetROISequence.get().contains(structureSetROI2));
        assertTrue(structureSetROISequence.get().contains(structureSetROI3));
        assertTrue(structureSetROISequence.get().contains(structureSetROI4));

        var roiContourSequence = ss.getRoiContourSequence();
        assertEquals(5, roiContourSequence.get().size());
        var roiContour0 = roiContourSequence.get().get(0);
        assertArrayEquals(new Integer[]{255, 0, 0}, roiContour0.getROIDisplayColor().get());
        var contourSequence = roiContour0.getContourSequence();
        var expContourItem = new ContourItem();

        expContourImageItem.setReferencedSOPClassUID(Optional.of(UID.CTImageStorage));
        expContourImageItem.setReferencedSOPInstanceUID(Optional.of("1.2.392.200036.9116.2.6.1.16.1613471639.1540891569.28542"));
        expContourItem.getContourImageSequence().orElse(new ArrayList<>()).add(expContourImageItem);
        expContourItem.setContourGeometricType(Optional.of("CLOSED_PLANAR"));
        expContourItem.setNumberOfContourPoints(Optional.of(4));
        expContourItem.setContourNumber(Optional.of(0));
        expContourItem.setContourData(Optional.of(new ArrayList<>()));
        expContourItem.getContourData().get().add(-10.63135);
        expContourItem.getContourData().get().add(-5.951199);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-11.47545);
        expContourItem.getContourData().get().add(-4.770328);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-12.06245);
        expContourItem.getContourData().get().add(-3.736268);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-12.68112);
        expContourItem.getContourData().get().add(-2.245236);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-13.07751);
        expContourItem.getContourData().get().add(-0.6208908);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-13.18278);
        expContourItem.getContourData().get().add(1.30117);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-13.04892);
        expContourItem.getContourData().get().add(2.799099);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-12.60062);
        expContourItem.getContourData().get().add(4.606259);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-11.59987);
        expContourItem.getContourData().get().add(6.659985);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-10.41402);
        expContourItem.getContourData().get().add(8.291614);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-9.385978);
        expContourItem.getContourData().get().add(9.299986);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-7.935406);
        expContourItem.getContourData().get().add(10.37506);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-6.008111);
        expContourItem.getContourData().get().add(11.31731);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-3.722468);
        expContourItem.getContourData().get().add(11.86599);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-2.037528);
        expContourItem.getContourData().get().add(11.94271);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-0.6581482);
        expContourItem.getContourData().get().add(11.86544);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(1.555591);
        expContourItem.getContourData().get().add(11.3264);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(2.979898);
        expContourItem.getContourData().get().add(10.63934);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(4.33293);
        expContourItem.getContourData().get().add(9.785913);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(5.091006);
        expContourItem.getContourData().get().add(9.173659);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(5.992604);
        expContourItem.getContourData().get().add(8.278795);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(7.162805);
        expContourItem.getContourData().get().add(6.673681);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(8.170412);
        expContourItem.getContourData().get().add(4.580839);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(8.605498);
        expContourItem.getContourData().get().add(2.81587);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(8.751563);
        expContourItem.getContourData().get().add(1.026244);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(8.653608);
        expContourItem.getContourData().get().add(-0.5564451);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(8.089637);
        expContourItem.getContourData().get().add(-2.734057);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(7.631644);
        expContourItem.getContourData().get().add(-3.732856);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(6.457397);
        expContourItem.getContourData().get().add(-5.623788);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(5.717219);
        expContourItem.getContourData().get().add(-6.500515);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(3.967777);
        expContourItem.getContourData().get().add(-8.012704);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(3.369239);
        expContourItem.getContourData().get().add(-8.415419);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(2.235971);
        expContourItem.getContourData().get().add(-8.937058);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(0.8176045);
        expContourItem.getContourData().get().add(-9.475734);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-0.896889);
        expContourItem.getContourData().get().add(-9.822879);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-2.108072);
        expContourItem.getContourData().get().add(-9.957109);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-3.590453);
        expContourItem.getContourData().get().add(-9.810884);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-5.217731);
        expContourItem.getContourData().get().add(-9.483482);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-6.615079);
        expContourItem.getContourData().get().add(-8.957241);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-7.796721);
        expContourItem.getContourData().get().add(-8.418862);
        expContourItem.getContourData().get().add(-12.0);
        expContourItem.getContourData().get().add(-9.393643);
        expContourItem.getContourData().get().add(-7.224891);
        expContourItem.getContourData().get().add(-12.0);
        boolean found = false;
        for (var ci : contourSequence.get()) {
            if (ci.getContourNumber().get()== 0 && ci.getNumberOfContourPoints().get() == 41 &&
                    ci.getContourGeometricType().get().equals("CLOSED_PLANAR") &&
                    ci.getContourImageSequence().get().contains(expContourImageItem)) {
//                found = true;
                if (!found) {
                    if (ci.getContourData().get().size() == expContourItem.getContourData().get().size()) {
                        var n = ci.getContourData().get().size();
                        boolean b = true;
                        for (int i = 0; i < n; i++) {
                            long x = (long) (expContourItem.getContourData().get().get(i) * 100000);
                            long y = (long) (ci.getContourData().get().get(i) * 100000);
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
        rtROIObservation0.setObservationNumber(Optional.of(1));
        rtROIObservation0.setReferencedROINumber(Optional.of(2));
        rtROIObservation0.setROIObservationLabel(Optional.of("GTV"));
        rtROIObservation0.setRTROIInterpretedType(Optional.of("GTV"));
//        rtROIObservation0.setROIInterpreter(Optional.of(""));
//        rtROIObservation0.setMaterialID(Optional.of(""));

        var rtROIObservation1 = new RTROIObservationsItem();
        rtROIObservation1.setObservationNumber(Optional.of(2));
        rtROIObservation1.setReferencedROINumber(Optional.of(3));
        rtROIObservation1.setROIObservationLabel(Optional.of("PTV"));
        rtROIObservation1.setRTROIInterpretedType(Optional.of("PTV"));
//        rtROIObservation1.setROIInterpreter(Optional.of(""));
//        rtROIObservation1.setMaterialID(Optional.of(""));

        var rtROIObservation2 = new RTROIObservationsItem();
        rtROIObservation2.setObservationNumber(Optional.of(3));
        rtROIObservation2.setReferencedROINumber(Optional.of(4));
        rtROIObservation2.setROIObservationLabel(Optional.of("External"));
        rtROIObservation2.setRTROIInterpretedType(Optional.of("EXTERNAL"));
//        rtROIObservation2.setROIInterpreter(Optional.of(""));
//        rtROIObservation2.setMaterialID(Optional.of(""));

        var rtROIObservation3 = new RTROIObservationsItem();
        rtROIObservation3.setObservationNumber(Optional.of(4));
        rtROIObservation3.setReferencedROINumber(Optional.of(5));
        rtROIObservation3.setROIObservationLabel(Optional.of("TAFEL_SYN23"));
        rtROIObservation3.setRTROIInterpretedType(Optional.of("SUPPORT"));
//        rtROIObservation3.setROIInterpreter(Optional.of(""));
        var expROIPhysicalProperties0 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties0.setROIPhysicalProperty(Optional.of("REL_MASS_DENSITY"));
        expROIPhysicalProperties0.setROIPhysicalPropertyValue(Optional.of(0.25));
        var expROIPhysicalProperties1 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties1.setROIPhysicalProperty(Optional.of("REL_ELEC_DENSITY"));
        expROIPhysicalProperties1.setROIPhysicalPropertyValue(Optional.of(0.250037));
        var expROIPhysicalProperties2 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties2.setROIPhysicalProperty(Optional.of("EFFECTIVE_Z"));
        expROIPhysicalProperties2.setROIPhysicalPropertyValue(Optional.of(6.600049));
        var expROIPhysicalProperties3 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties3.setROIPhysicalProperty(Optional.of("EFF_Z_PER_A"));
        expROIPhysicalProperties3.setROIPhysicalPropertyValue(Optional.of(0.5550822));
        var expROIPhysicalProperties4 = new ROIPhysicalPropertiesItem();
        expROIPhysicalProperties4.setROIPhysicalProperty(Optional.of("ELEM_FRACTION"));
        expROIPhysicalProperties4.setROIPhysicalPropertyValue(Optional.of(0.0));
        var roiElementalComposition0 = new ROIElementalCompositionItem();
        roiElementalComposition0.setRoiElementalCompositionAtomicNumber(Optional.of(1));
        roiElementalComposition0.setRoiElementalCompositionAtomicMassFraction(Optional.of(0.11189399659633636));
        var roiElementalComposition1 = new ROIElementalCompositionItem();
        roiElementalComposition1.setRoiElementalCompositionAtomicNumber(Optional.of(8));
        roiElementalComposition1.setRoiElementalCompositionAtomicMassFraction(Optional.of(0.8881059885025024));
        expROIPhysicalProperties4.setROIElementalCompositionSequence(Optional.of(new ArrayList<>()));
        expROIPhysicalProperties4.getROIElementalCompositionSequence().get().add(roiElementalComposition0);
        expROIPhysicalProperties4.getROIElementalCompositionSequence().get().add(roiElementalComposition1);
        rtROIObservation3.setROIPhysicalPropertiesSequence(Optional.of(new ArrayList<>()));
        rtROIObservation3.getROIPhysicalPropertiesSequence().get().add(expROIPhysicalProperties0);
        rtROIObservation3.getROIPhysicalPropertiesSequence().get().add(expROIPhysicalProperties1);
        rtROIObservation3.getROIPhysicalPropertiesSequence().get().add(expROIPhysicalProperties2);
        rtROIObservation3.getROIPhysicalPropertiesSequence().get().add(expROIPhysicalProperties3);
        rtROIObservation3.getROIPhysicalPropertiesSequence().get().add(expROIPhysicalProperties4);
        rtROIObservation3.setMaterialID(Optional.of("TAFEL_SYN23"));

        var rtROIObservation4 = new RTROIObservationsItem();
        rtROIObservation4.setObservationNumber(Optional.of(5));
        rtROIObservation4.setReferencedROINumber(Optional.of(1));
        rtROIObservation4.setROIObservationLabel(Optional.of("iso"));
        rtROIObservation4.setRTROIInterpretedType(Optional.of("ISOCENTER"));
//        rtROIObservation4.setROIInterpreter(Optional.of(""));
//        rtROIObservation4.setMaterialID(Optional.of(""));

        List<RTROIObservationsItem> expRTROIObservationsSequence = new ArrayList<>();
        expRTROIObservationsSequence.add(rtROIObservation0);
        expRTROIObservationsSequence.add(rtROIObservation1);
        expRTROIObservationsSequence.add(rtROIObservation2);
        expRTROIObservationsSequence.add(rtROIObservation3);
        expRTROIObservationsSequence.add(rtROIObservation4);

        assertEquals(expRTROIObservationsSequence, rtROIObservationSequence.get());
        assertEquals("UNAPPROVED", ss.getApprovalStatus().get());
    }
}
