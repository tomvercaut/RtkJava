package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.io.DicomInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This factory provides a general
 */
@Log4j2
public class DicomFactory {

    public static DicomObject read(String pathname) throws IOException, DicomException {
        File file = new File(pathname);
        return read(file);
    }

    public static DicomObject read(Path path) throws IOException, DicomException {
        if (path == null) return new DicomObject();
        return read(path.toFile());
    }

    public static DicomObject read(File file) throws IOException, DicomException {
        DicomObject dicomObject = new DicomObject();
        if (file == null || !file.isFile() || !file.canRead()) {
            return dicomObject;
        }

        DicomInputStream dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
        Attributes meta = dis.readFileMetaInformation();
        var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        Attributes dataset = dis.readDataset(-1, -1);
        if (!dataset.contains(Tag.SOPClassUID)) {
            log.error("DICOM stream doesn't contain a SOPClassUID");
            return dicomObject;
        }

        String sopClassUID = dataset.getString(Tag.SOPClassUID);
        if (sopClassUID.equals(UID.CTImageStorage)) {
            var ct = Reader.ct(meta, dataset, bo);
            ct.ifPresent(dicomObject::set);
        } else if (sopClassUID.equals(UID.PositronEmissionTomographyImageStorage)) {
            var pt = Reader.pt(meta, dataset, bo);
            pt.ifPresent(dicomObject::set);
        } else if (sopClassUID.equals(UID.RTStructureSetStorage)) {
            var ss = Reader.structureSet(meta, dataset, bo);
            ss.ifPresent(dicomObject::set);
        } else if (sopClassUID.equals(UID.RTDoseStorage)) {
            var rtd = Reader.rtDose(meta, dataset, bo);
            rtd.ifPresent(dicomObject::set);
        } else if (sopClassUID.equals(UID.SpatialRegistrationStorage)) {
            var sr = Reader.spatialRegistration(meta, dataset, bo);
            sr.ifPresent(dicomObject::set);
        } else {
            log.error(String.format("Trying to read an unsupported DICOM file [SOPClassUID: %s]", sopClassUID));
        }
        dicomObject.setPathname(file.getAbsolutePath());
        return dicomObject;
    }

    public static List<DicomObject> read(List<String> pathnames) throws IOException, DicomException {
        if (pathnames == null || pathnames.isEmpty()) return new ArrayList<>();
        List<DicomObject> list = new ArrayList<>(pathnames.size());
        for (String s : pathnames) {
            DicomObject doj = read(s);
            list.add(doj);
        }
        return list;
    }
}