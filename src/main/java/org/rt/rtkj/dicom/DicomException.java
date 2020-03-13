package org.rt.rtkj.dicom;

public class DicomException extends Exception {
    public DicomException() {
    }

    public DicomException(String message) {
        super(message);
    }

    public DicomException(String message, Throwable cause) {
        super(message, cause);
    }

    public DicomException(Throwable cause) {
        super(cause);
    }

    public DicomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
