package org.rt.rtkj.model;

import org.apache.commons.math3.util.Precision;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.dicom.DicomException;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PixelRepresentation;
import org.rt.rtkj.dicom.Reader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageFactoryTest {

    private static Path getResourceDirectory() {
        return Paths.get("src", "test", "resources");
    }

    @Test
    void buildCT() throws IOException, DicomException {
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
        var ct = optCT.get();

        String propertyTmpDir = "java.io.tmpdir";
        var optImage = ImageFactory.build(ct);
        assertTrue(optImage.isPresent());
        var image = optImage.get();
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891557.581701", image.getSOPInstanceUID());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891246.945610", image.getFrameOfReferenceUID());
        assertEquals(Modality.CT, image.getModality());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891206.523047", image.getStudyInstanceUID());
        assertEquals("1.2.392.200036.9116.2.6.1.16.1613471639.1540891527.871910", image.getSeriesInstanceUID());
        assertEquals(6, image.getImageOrientationPatient().get().length);
        assertTrue(Precision.equals(1.00000, image.getImageOrientationPatient().get()[0], Precision.EPSILON));
        assertTrue(Precision.equals(0.00000, image.getImageOrientationPatient().get()[1], Precision.EPSILON));
        assertTrue(Precision.equals(0.00000, image.getImageOrientationPatient().get()[2], Precision.EPSILON));
        assertTrue(Precision.equals(0.00000, image.getImageOrientationPatient().get()[3], Precision.EPSILON));
        assertTrue(Precision.equals(1.00000, image.getImageOrientationPatient().get()[4], Precision.EPSILON));
        assertTrue(Precision.equals(0.00000, image.getImageOrientationPatient().get()[5], Precision.EPSILON));
        assertEquals(3, image.getImagePositionPatient().get().length);
        assertTrue(Precision.equals(-201.953000, image.getImagePositionPatient().get()[0], Precision.EPSILON));
        assertTrue(Precision.equals(-201.953100, image.getImagePositionPatient().get()[1], Precision.EPSILON));
        assertTrue(Precision.equals(46.000000, image.getImagePositionPatient().get()[2], Precision.EPSILON));
        assertEquals(2, image.getPixelSpacing().get().length);
        assertTrue(Precision.equals(0.788, image.getPixelSpacing().get()[0], Precision.EPSILON));
        assertTrue(Precision.equals(0.788, image.getPixelSpacing().get()[1], Precision.EPSILON));
        assertTrue(Precision.equals(0, image.getRescaleIntercept().get()));
        assertTrue(Precision.equals(1, image.getRescaleSlope().get()));
        assertEquals(PixelRepresentation.TWO_COMPLEMENT, image.getPixelRepresentation().get());
        assertEquals(16, image.getBitsAllocated().get());
    }
}