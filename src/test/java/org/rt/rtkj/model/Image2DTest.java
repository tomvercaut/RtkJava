package org.rt.rtkj.model;

import org.apache.commons.math3.util.Precision;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.dicom.CT;
import org.rt.rtkj.dicom.DicomException;
import org.rt.rtkj.dicom.Reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Image2DTest {

    private CT ct0;
    private Image2D image0;

    private static Path getResourceDirectory() {
        return Paths.get("src", "test", "resources");
    }

    @BeforeEach
    private void beforeEach() throws IOException, DicomException {
        var resourceDirectory = getResourceDirectory();
        var file = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "dicom",
                "carpet", "ct3",
                "CT1.2.392.200036.9116.2.6.1.16.1613471639.1540891557.581701.dcm").toFile();
        assertTrue(file.exists());
        assertTrue(file.isFile());
        DicomInputStream dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
        Attributes meta = dis.readFileMetaInformation();
        var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        Attributes dataset = dis.readDataset(-1, -1);
        var optCT = Reader.ct(meta, dataset, bo);
        assertTrue(optCT.isPresent());
        ct0 = optCT.get();
        var optImage = ImageFactory.build(ct0);
        assertTrue(optImage.isPresent());
        image0 = optImage.get();
    }

    // TODO Validate the transform with a skewed DICOM dataset
    @Test
    void i2c() {
        var coord = image0.i2c(40, 148);
        assertEquals(1, coord.numCols());
        assertEquals(4, coord.numRows());
        assertTrue(Precision.equals(-170.433, coord.get(0, 0), Precision.EPSILON), "Value: " + coord.get(0, 0));
        assertTrue(Precision.equals(-85.3291, coord.get(1, 0), Precision.EPSILON), "Value: " + coord.get(1, 0));
        assertTrue(Precision.equals(46, coord.get(2, 0), Precision.EPSILON), "Value: " + coord.get(2, 0));

        coord = image0.i2c(455, 184);
        assertEquals(1, coord.numCols());
        assertEquals(4, coord.numRows());
        assertTrue(Precision.equals(156.587, coord.get(0, 0), 0.00001), "Value: " + coord.get(0, 0));
        assertTrue(Precision.equals(-56.9611, coord.get(1, 0), 0.00001), "Value: " + coord.get(1, 0));
        assertTrue(Precision.equals(46, coord.get(2, 0), 0.00001), "Value: " + coord.get(2, 0));

        coord = image0.i2c(91, 361);
        assertEquals(1, coord.numCols());
        assertEquals(4, coord.numRows());
        assertTrue(Precision.equals(-130.245, coord.get(0, 0), 0.00001), "Value: " + coord.get(0, 0));
        assertTrue(Precision.equals(82.5149, coord.get(1, 0), 0.00001), "Value: " + coord.get(1, 0));
        assertTrue(Precision.equals(46, coord.get(2, 0), 0.00001), "Value: " + coord.get(2, 0));

        coord = image0.i2c(412, 405);
        assertEquals(1, coord.numCols());
        assertEquals(4, coord.numRows());
        assertTrue(Precision.equals(122.703, coord.get(0, 0), 0.00001), "Value: " + coord.get(0, 0));
        assertTrue(Precision.equals(117.1869, coord.get(1, 0), 0.00001), "Value: " + coord.get(1, 0));
        assertTrue(Precision.equals(46, coord.get(2, 0), 0.00001), "Value: " + coord.get(2, 0));
    }

//    @Test
//    void c2i() {
//        var idx = image0.c2i(-170.433, -85.3291, 46);
//        assertEquals(1, idx.numCols());
//        assertEquals(4, idx.numRows());
//        assertEquals(Precision.equals((double)40, idx.get(0)), "Index: " + idx.get(0));
//        assertEquals(Precision.equals((double)148, idx.get(1)), "Index: " + idx.get(1));
//    }

    @Test
    void getValue() {
        var coord = image0.getValue(40, 148);
        assertTrue(coord.isPresent());
        assertEquals(-1002, coord.get());
        coord = image0.getValue(455, 184);
        assertTrue(coord.isPresent());
        assertEquals(7, coord.get());
        coord = image0.getValue(91, 361);
        assertTrue(coord.isPresent());
        assertEquals(-55, coord.get());
        coord = image0.getValue(412, 405);
        assertTrue(coord.isPresent());
        assertEquals(332, coord.get());
    }
}