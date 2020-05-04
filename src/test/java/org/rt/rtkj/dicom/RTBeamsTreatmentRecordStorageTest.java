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

        assertEquals(LocalDate.of(2000, 2, 21), record.getInstanceCreationDate());
        assertEquals(LocalTime.of(9, 3, 49), record.getInstanceCreationTime());
        assertEquals(UID.RTBeamsTreatmentRecordStorage, record.getSOPClassUID());
        assertEquals(LocalDate.of(2000, 2, 21), record.getStudyDate());
        assertEquals(LocalTime.of(8, 49, 39), record.getStudyTime());
        assertEquals("2203027", record.getAccessionNumber());
        assertEquals("Varian Medical Systems", record.getManufacturer());
        assertEquals("X^Y", record.getReferringPhysicianName());
        assertEquals("H^J", record.getOperatorsName());
        assertEquals("2300IX", record.getManufacturerModelName());
        assertEquals("FN^LN", record.getPatientName());
        assertEquals("X102kj", record.getPatientID());
        assertEquals("", record.getPatientSex());
        assertNull(record.getPatientBirthDate());
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
    }
}