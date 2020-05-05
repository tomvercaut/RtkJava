package org.rt.rtkj.dicom;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.UID;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RTBeamsTreatmentRecordStorageTest {

    @Test
    public void read() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var file = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "rtrecord.dcm").toFile();
        assertTrue(file.exists());
        assertTrue(file.isFile());
        DicomInputStream dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
        Attributes meta = dis.readFileMetaInformation();
        var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        Attributes dataset = dis.readDataset(-1, -1);
        var optRecord = Reader.rtBeamsTreatmentRecordStorage(meta, dataset, bo);
        assertTrue(optRecord.isPresent());
        var record = optRecord.get();

        assertEquals(LocalDate.of(2000, 2, 21), record.getInstanceCreationDate().get());
        assertEquals(LocalTime.of(9, 3, 49), record.getInstanceCreationTime().get());
        assertEquals(UID.RTBeamsTreatmentRecordStorage, record.getSOPClassUID());
        assertEquals(LocalDate.of(2000, 2, 21), record.getStudyDate().get());
        assertEquals(LocalTime.of(8, 49, 39), record.getStudyTime().get());
        assertEquals("2203027", record.getAccessionNumber());
        assertEquals("Varian Medical Systems", record.getManufacturer());
        assertEquals("X^Y", record.getReferringPhysicianName());
        assertEquals("H^J", record.getOperatorsName());
        assertEquals("2300IX", record.getManufacturerModelName());
        assertEquals("FN^LN", record.getPatientName());
        assertEquals("X102kj", record.getPatientID());
        assertEquals("", record.getPatientSex());
        assertTrue(record.getPatientBirthDate().isEmpty());
        assertEquals("White", record.getEthnicGroup());
        assertEquals("7.8.05", record.getSoftwareVersions());
        assertEquals("CT1", record.getStudyID());
        assertEquals(1, record.getSeriesNumber());
        assertEquals(1, record.getInstanceNumber());

        var treatmentSessionBeamSequence = record.getTreatmentSessionBeamSequence();
        assertNotNull(treatmentSessionBeamSequence);
        assertEquals(1, treatmentSessionBeamSequence.size());
        var treatmentSessionBeamItem = treatmentSessionBeamSequence.get(0);
        assertEquals(8, treatmentSessionBeamItem.getCurrentFractionNumber());
        assertEquals("NORMAL", treatmentSessionBeamItem.getTreatmentTerminationStatus());
        assertEquals("DOSE", treatmentSessionBeamItem.getTreatmentTerminationCode());
        assertEquals("VERIFIED", treatmentSessionBeamItem.getTreatmentVerificationStatus());
        assertEquals(13, treatmentSessionBeamItem.getDeliveredTreatmentTime());

        var controlPointDeliverySequence = treatmentSessionBeamItem.getControlPointDeliverySequence();
        assertEquals(2, controlPointDeliverySequence.size());
        var controlPointDelivery0 = controlPointDeliverySequence.get(0);
        assertEquals(LocalDate.of(2000, 2, 21), controlPointDelivery0.getTreatmentControlPointDate().get());
        assertEquals(LocalTime.of(9, 3, 49), controlPointDelivery0.getTreatmentControlPointTime().get());
        assertEquals(0.0, controlPointDelivery0.getSpecifiedMeterset());
        assertEquals(0.0, controlPointDelivery0.getDeliveredMeterset());
        assertEquals(400, controlPointDelivery0.getDoseRateDelivered());
        assertEquals("MV", controlPointDelivery0.getNominalBeamEnergyUnit());
        assertEquals(6, controlPointDelivery0.getNominalBeamEnergy());
        assertEquals(400, controlPointDelivery0.getDoseRateSet());
        assertEquals(2, controlPointDelivery0.getBeamLimitingDevicePositionSequence().size());
        assertEquals("ASYMX", controlPointDelivery0.getBeamLimitingDevicePositionSequence().get(0).getRtBeamLimitingDeviceType());
        assertEquals(List.of(-48., 70.), controlPointDelivery0.getBeamLimitingDevicePositionSequence().get(0).getLeafJawPositions());
        assertEquals("ASYMY", controlPointDelivery0.getBeamLimitingDevicePositionSequence().get(1).getRtBeamLimitingDeviceType());
        assertEquals(List.of(-33., 40.), controlPointDelivery0.getBeamLimitingDevicePositionSequence().get(1).getLeafJawPositions());
        assertEquals(60, controlPointDelivery0.getGantryAngle());
        assertEquals(RotationDirection.None, controlPointDelivery0.getGantryRotationDirection());
        assertEquals(0, controlPointDelivery0.getBeamLimitingDeviceAngle());
        assertEquals(RotationDirection.None, controlPointDelivery0.getBeamLimitingDeviceRotationDirection());
        assertEquals(270, controlPointDelivery0.getPatientSupportAngle());
        assertEquals(RotationDirection.None, controlPointDelivery0.getPatientSupportRotationDirection());
        assertEquals(0, controlPointDelivery0.getTableTopEccentricAxisDistance());
        assertEquals(RotationDirection.None, controlPointDelivery0.getTableTopEccentricRotationDirection());
        assertEquals(-110, controlPointDelivery0.getTableTopVerticalPosition());
        assertEquals(987, controlPointDelivery0.getTableTopLongitudinalPosition());
        assertEquals(32, controlPointDelivery0.getTableTopLateralPosition());
        assertEquals(0, controlPointDelivery0.getReferencedControlPointIndex());

        var controlPointDelivery1 = controlPointDeliverySequence.get(1);

        assertEquals(LocalDate.of(2000, 2, 21), controlPointDelivery1.getTreatmentControlPointDate().get());
        assertEquals(LocalTime.of(9, 3, 49), controlPointDelivery1.getTreatmentControlPointTime().get());
        assertEquals(90, controlPointDelivery1.getSpecifiedMeterset());
        assertEquals(90, controlPointDelivery1.getDeliveredMeterset());
        assertEquals(DicomUtils.UNDEFINED_DOUBLE, controlPointDelivery1.getDoseRateDelivered());
        assertEquals(DicomUtils.UNDEFINED_DOUBLE, controlPointDelivery1.getDoseRateSet());
        assertEquals(149, controlPointDelivery1.getReferencedControlPointIndex());

        var referencedCalculatedDoseReferenceSequence = treatmentSessionBeamItem.getReferencedCalculatedDoseReferenceSequence();
        assertEquals(3, referencedCalculatedDoseReferenceSequence.size());
        List<ReferencedCalculatedDoseReferenceItem> expReferencedCalculatedDoseReferenceSequence = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            expReferencedCalculatedDoseReferenceSequence.add(new ReferencedCalculatedDoseReferenceItem());
        expReferencedCalculatedDoseReferenceSequence.get(0).setCalculatedDoseReferenceDoseValue(7.7181e-4);
        expReferencedCalculatedDoseReferenceSequence.get(0).setReferencedDoseReferenceNumber(2);
        expReferencedCalculatedDoseReferenceSequence.get(1).setCalculatedDoseReferenceDoseValue(3.890609e-2);
        expReferencedCalculatedDoseReferenceSequence.get(1).setReferencedDoseReferenceNumber(1);
        expReferencedCalculatedDoseReferenceSequence.get(2).setCalculatedDoseReferenceDoseValue(3.0e-1);
        expReferencedCalculatedDoseReferenceSequence.get(2).setReferencedDoseReferenceNumber(3);
        assertEquals(expReferencedCalculatedDoseReferenceSequence, referencedCalculatedDoseReferenceSequence);

        var beamLimitingDeviceLeafPairsSequence = treatmentSessionBeamItem.getBeamLimitingDeviceLeafPairsSequence();
        assertEquals(3, beamLimitingDeviceLeafPairsSequence.size());
        List<BeamLimitingDeviceLeafPairsItem> expBeamLimitingDeviceLeafPairsSequence = new ArrayList<>();
        for (int i = 0; i < 3; ++i)
            expBeamLimitingDeviceLeafPairsSequence.add(new BeamLimitingDeviceLeafPairsItem());
        expBeamLimitingDeviceLeafPairsSequence.get(0).setRTBeamLimitingDeviceType("ASYMX");
        expBeamLimitingDeviceLeafPairsSequence.get(0).setNumberOfLeafJawPairs(1);
        expBeamLimitingDeviceLeafPairsSequence.get(1).setRTBeamLimitingDeviceType("ASYMY");
        expBeamLimitingDeviceLeafPairsSequence.get(1).setNumberOfLeafJawPairs(1);
        expBeamLimitingDeviceLeafPairsSequence.get(2).setRTBeamLimitingDeviceType("MLCX");
        expBeamLimitingDeviceLeafPairsSequence.get(2).setNumberOfLeafJawPairs(60);
        assertEquals(expBeamLimitingDeviceLeafPairsSequence, beamLimitingDeviceLeafPairsSequence);

        assertEquals(1000, treatmentSessionBeamItem.getSourceAxisDistance());
        assertEquals("Field 5", treatmentSessionBeamItem.getBeamName());
        assertEquals("DYNAMIC", treatmentSessionBeamItem.getBeamType());
        assertEquals("PHOTON", treatmentSessionBeamItem.getRadiationType());
        assertEquals("TREATMENT", treatmentSessionBeamItem.getTreatmentDeliveryType());
        assertEquals(0, treatmentSessionBeamItem.getNumberOfWedges());
        assertEquals(0, treatmentSessionBeamItem.getNumberOfCompensators());
        assertEquals(0, treatmentSessionBeamItem.getNumberOfBoli());
        assertEquals(0, treatmentSessionBeamItem.getNumberOfBlocks());
        assertEquals(2, treatmentSessionBeamItem.getNumberOfControlPoints());
        assertEquals(5, treatmentSessionBeamItem.getReferencedBeamNumber());
        assertEquals(LocalDate.of(2000, 2, 21), record.getTreatmentDate().get());
        assertEquals(LocalTime.of(9, 3, 49), record.getTreatmentTime().get());
        assertEquals(13, record.getNumberOfFractionsPlanned());
        assertEquals("MU", record.getPrimaryDosimeterUnit());

        var treatmentMachineSequence = record.getTreatmentMachineSequence();
        assertEquals(1, treatmentMachineSequence.size());
        var treatmentMachine = treatmentMachineSequence.get(0);
        assertEquals("Varian Medical Systems", treatmentMachine.getManufacturer());
        assertEquals("Unknown", treatmentMachine.getInstitutionName());
        assertEquals("2300IX", treatmentMachine.getManufacturerModelName());
        assertEquals("4164", treatmentMachine.getDeviceSerialNumber());
        assertEquals("CLINACIX_1", treatmentMachine.getTreatmentMachineName());

        var referencedRTPlanSequence = record.getReferencedRTPlanSequence();
        assertEquals(1, referencedRTPlanSequence.size());
        var referencedRTPlan = referencedRTPlanSequence.get(0);
        assertEquals(UID.RTPlanStorage, referencedRTPlan.getReferencedSOPClassUID());
    }
}