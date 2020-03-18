package org.rt.rtkj.dicom;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.junit.jupiter.api.Test;
import org.rt.rtkj.ResourceFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CTTest {

    @Test
    public void read() throws IOException, DicomException {
        var resourceDirectory = ResourceFactory.getInstance().getDicomPath();
        var file = Paths.get(resourceDirectory.toFile().getAbsolutePath(), "carpet", "ct3", "CT1.2.392.200036.9116.2.6.1.16.1613471639.1540891557.581701.dcm").toFile();
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
        String dirTmp = System.getProperty(propertyTmpDir);
        Path filePpm = Paths.get(dirTmp, "ct.tiff");
        Writer.writeGrayTiff(ct, filePpm.toString());
    }
}
