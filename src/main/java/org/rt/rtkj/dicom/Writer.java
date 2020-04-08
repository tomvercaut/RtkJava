package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.rt.rtkj.model.Image3D;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

import static java.awt.image.DataBuffer.TYPE_USHORT;

@Log4j2
public class Writer {
    public static void writeGrayTiff(CT ct, String filename) throws IOException {
        log.info("filename: " + filename);
        int nr = ct.getRows();
        int nc = ct.getColumns();

        File outputFile = new File(filename);
        ImageTypeSpecifier specifier = ImageTypeSpecifier.createGrayscale(16, TYPE_USHORT, false);
        Iterator<ImageWriter> writers = ImageIO.getImageWriters(specifier, "tiff");
        ;
        ImageWriter writer = null;
        while (writers.hasNext()) {
            writer = writers.next();
            break;
        }
        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        int n = nr * nc;
        for (int i = 0; i < n; ++i) {
            long pixel = ct.getPixelData().get(i);
            if (pixel < min) min = pixel;
            if (pixel > max) max = pixel;
        }
        log.info("min: " + min);
        log.info("max: " + max);
        double imin = Math.abs(min);
        double scale = max + imin;
        if (scale > 65535.0) {
            scale = ((double) 65535.0) / scale;
//            scale /= ((double)65535.0);
        }

        if (writer == null) throw new IOException("Unable to create tiff writer");
        BufferedImage bi = new BufferedImage(nc, nr, BufferedImage.TYPE_USHORT_GRAY);
        WritableRaster raster = bi.getRaster();
        int k = 0;
        for (int j = 0; j < nr; j++) {
            for (int i = 0; i < nc; i++) {
                double p = ct.getPixelData().get(k);
                p += imin;
//                p *= scale;
                raster.setPixel(i, j, new int[]{(int) p});
                ++k;
            }
        }
        ImageOutputStream ios = new FileImageOutputStream(outputFile);
        writer.setOutput(ios);
        writer.write(bi);
    }

    public Optional<Attributes> rtdose(CT3d volume, Image3D dose) {
        return Optional.empty();
    }
}
