package org.rt.rtkj.dicom;

import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DicomFactoryTest {
    @Test
    void readOne() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var dir = Path.of(resourceDirectory.toAbsolutePath().toString(), "carpet", "ct3");
        var dcmfile = Files.list(dir).filter(path -> path.toString().endsWith(".dcm") && !path.getFileName().toString().startsWith("RP")).map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList()).get(0);
        var doj = DicomFactory.read(dcmfile);
        assertTrue(doj != null);
        assertTrue(doj.getErrors().isEmpty());
    }

    @Test
    void readOne2() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var dcmfile = Path.of(resourceDirectory.toAbsolutePath().toString(), "carpet", "CT1.2.392.200036.9116.2.6.1.16.1613471639.1540891553.633500.dcm");
        var doj = DicomFactory.read(dcmfile);
        assertTrue(doj != null);
        assertTrue(doj.getErrors().isEmpty());
    }

    @Test
    void readMany() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var dir = Path.of(resourceDirectory.toAbsolutePath().toString(), "carpet", "ct3");
        var listDcm = Files.list(dir).filter(path -> path.toString().endsWith(".dcm") && !path.getFileName().toString().startsWith("RP")).map(path -> path.toAbsolutePath().toString()).collect(Collectors.toList());
        listDcm = listDcm.subList(listDcm.size() - 30, listDcm.size());
        var ldoj = DicomFactory.read(listDcm);
        ldoj.forEach(dicomObject -> assertTrue(dicomObject.getErrors().isEmpty()));
        ldoj.forEach(dicomObject -> assertFalse(dicomObject.getPathname().isEmpty()));
        ldoj.forEach(dicomObject -> {
            String pathname = dicomObject.getPathname();
            if (pathname.startsWith("CT")) {
                assertTrue(dicomObject.hasCT());
                assertFalse(dicomObject.hasPT());
                assertFalse(dicomObject.hasRTDose());
                assertFalse(dicomObject.hasSpatialRegistration());
                assertFalse(dicomObject.hasStructureSet());
            } else if (pathname.startsWith("RD")) {
                assertFalse(dicomObject.hasCT());
                assertFalse(dicomObject.hasPT());
                assertTrue(dicomObject.hasRTDose());
                assertFalse(dicomObject.hasSpatialRegistration());
                assertFalse(dicomObject.hasStructureSet());
            } else if (pathname.startsWith("RS")) {
                assertFalse(dicomObject.hasCT());
                assertFalse(dicomObject.hasPT());
                assertFalse(dicomObject.hasRTDose());
                assertFalse(dicomObject.hasSpatialRegistration());
                assertTrue(dicomObject.hasStructureSet());
            }
        });
    }
}