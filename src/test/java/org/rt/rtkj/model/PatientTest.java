package org.rt.rtkj.model;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.Option;
import org.rt.rtkj.dicom.CT;
import org.rt.rtkj.dicom.DicomException;
import org.rt.rtkj.dicom.DicomFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
class PatientTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    private static Path getResourceDirectory() {
        return Paths.get("src", "test", "resources");
    }

    @Test
    void add() throws IOException {
        List<Integer> ls = List.of(19, 20, 21);
        var resourceDirectory = getResourceDirectory();
        var ct1 = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "dicom", "carpet", "ct1");
        var ct2 = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "dicom", "carpet", "ct2");
        var ct3 = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "dicom", "carpet", "ct3");
        var lct1 = Files.list(ct1).filter(path -> path.getFileName().toString().startsWith("CT")).collect(Collectors.toList()).subList(0, ls.get(0));
        var lct2 = Files.list(ct2).filter(path -> path.getFileName().toString().startsWith("CT")).collect(Collectors.toList()).subList(0, ls.get(1));
        var lct3 = Files.list(ct3).filter(path -> path.getFileName().toString().startsWith("CT")).collect(Collectors.toList()).subList(0, ls.get(2));
        assertEquals(lct1.size(), ls.get(0));
        assertEquals(lct2.size(), ls.get(1));
        assertEquals(lct3.size(), ls.get(2));

        Patient patient = new Patient();
        var lct = lct1;
        lct.addAll(lct2);
        lct.addAll(lct3);
//        Collections.shuffle(lct);
        lct.forEach(path -> {
            Option<CT> opt_ct = Option.empty();
            try {
                var dcmObj = DicomFactory.read(path);
                assertTrue(dcmObj.hasCT());
                opt_ct = dcmObj.getCt();
            } catch (IOException | DicomException e) {
                e.printStackTrace();
            }
            opt_ct.ifPresent(ct -> {
                var opt_image = ImageFactory.build(ct);
                opt_image.ifPresent(patient::add);
            });
        });

        assertEquals(3, patient.sizeStudies());
        assertEquals(patient.getStudies().get(0).getSeries().size(), 1);
        assertEquals(patient.getStudies().get(1).getSeries().size(), 1);
        assertEquals(patient.getStudies().get(2).getSeries().size(), 1);

        //TODO fix bug on inserting 2D images into a 3D image
        var n1 = patient.getStudies().get(0).getSeries().get(0).getImages().get(0).size();
        var n2 = patient.getStudies().get(1).getSeries().get(0).getImages().get(0).size();
        var n3 = patient.getStudies().get(2).getSeries().get(0).getImages().get(0).size();
        assertEquals(ls.get(0), n1);
        assertEquals(ls.get(1), n2);
        assertEquals(ls.get(2), n3);

        var im3d1 = patient.getStudies().get(0).getSeries().get(0).getImages().get(0);
        var im3d2 = patient.getStudies().get(1).getSeries().get(0).getImages().get(0);
        var im3d3 = patient.getStudies().get(2).getSeries().get(0).getImages().get(0);

        IntStream.range(0, ls.get(0)).forEach(index -> {
            log.info("index: " + index);
            var image = im3d1.get(index);
            var filename = lct1.get(index).getFileName().toString();
            if (filename.startsWith("CT") && filename.endsWith(".dcm")) {
                filename = filename.substring(2, filename.length() - 4);
                assertEquals(filename, image.getSOPInstanceUID().get());
            } else {
                fail("Expected a filename that starts with CT and ends with .dcm");
            }
        });

        IntStream.range(0, ls.get(1)).forEach(index -> {
            var image = im3d2.get(index);
            var filename = lct2.get(index).getFileName().toString();
            if (filename.startsWith("CT") && filename.endsWith(".dcm")) {
                filename = filename.substring(2, filename.length() - 4);
                assertEquals(filename, image.getSOPInstanceUID().get());
            } else {
                fail("Expected a filename that starts with CT and ends with .dcm");
            }
        });

        IntStream.range(0, ls.get(2)).forEach(index -> {
            var image = im3d3.get(index);
            var filename = lct3.get(index).getFileName().toString();
            if (filename.startsWith("CT") && filename.endsWith(".dcm")) {
                filename = filename.substring(2, filename.length() - 4);
                assertEquals(filename, image.getSOPInstanceUID().get());
            } else {
                fail("Expected a filename that starts with CT and ends with .dcm");
            }
        });
    }
}