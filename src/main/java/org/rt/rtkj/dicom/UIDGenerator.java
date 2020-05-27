package org.rt.rtkj.dicom;

import java.util.concurrent.ThreadLocalRandom;

public class UIDGenerator {

    private static String orgRoot() {
        return "9.87.654.1.";
    }

    public static String studyInstance() {
        return orgRoot() + "5.97.2.6972." + DicomUtils.getLocalDateTimeNow() + ThreadLocalRandom.current().nextInt(0, 100);
    }
}
