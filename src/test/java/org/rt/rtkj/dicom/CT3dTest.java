package org.rt.rtkj.dicom;

import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class CT3dTest {

    private static boolean filterCTs(Path path) {
        var filename = path.getFileName().toString();
        return filename.startsWith("CT");
    }

    @Test
    void add() throws IOException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var ct1Dir = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "carpet", "ct1");
        var ct3Dir = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "carpet", "ct3");
        assertTrue(Files.isDirectory(ct1Dir));
        assertTrue(Files.isDirectory(ct3Dir));
        var ct1List = Files.list(ct1Dir).filter(CT3dTest::filterCTs).collect(Collectors.toList());
        var ct3List = Files.list(ct3Dir).filter(CT3dTest::filterCTs).collect(Collectors.toList());
        assertNotNull(ct1List);
        assertNotNull(ct3List);
        var n1 = ct1List.size();
        var n3 = ct3List.size();
        assertTrue(n1 > 5);
        assertTrue(n3 > 5);
        var ct3d = new CT3d();

        List<CT> lct1 = new ArrayList<>();
        List<CT> lct3 = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> {
            try {
                var obj1 = DicomFactory.read(ct1List.get(i));
                var obj3 = DicomFactory.read(ct3List.get(i));
                assertNotNull(obj1);
                assertNotNull(obj3);
                assertTrue(obj1.hasCT());
                assertTrue(obj3.hasCT());
                assertTrue(obj1.getCt().isPresent());
                assertTrue(obj3.getCt().isPresent());
                lct1.add(obj1.getCt().get());
                lct3.add(obj3.getCt().get());
            } catch (IOException | DicomException e) {
                fail(e.getMessage());
            }
        });

        IntStream.range(0, lct1.size()).forEach(i -> assertTrue(ct3d.add(lct1.get(i))));

        IntStream.range(0, lct3.size()).forEach(i -> assertFalse(ct3d.add(lct3.get(i))));
        assertEquals(5, ct3d.size());
    }
}