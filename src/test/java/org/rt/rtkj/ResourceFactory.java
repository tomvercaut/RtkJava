package org.rt.rtkj;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceFactory {

    private static volatile ResourceFactory instance;
    private static final Object mutex = new Object();

    public static ResourceFactory getInstance() {
        ResourceFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (mutex) {
                localInstance = instance;
                if (localInstance == null) {
                    localInstance = instance = new ResourceFactory();
                }
            }
        }
        return localInstance;
    }

    public Path getPath() {
        return Paths.get("src", "test", "resources");
    }

    public Path getDicomPath() {
        return Paths.get(getPath().toAbsolutePath().toString(), "dicom");
    }

    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
