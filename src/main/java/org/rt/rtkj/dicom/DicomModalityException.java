package org.rt.rtkj.dicom;

public class DicomModalityException extends DicomException {
    private String expected = "";
    private String obtained = "";

    public DicomModalityException(String expected, String obtained) {
        this.expected = expected;
        this.obtained = obtained;
    }

    public DicomModalityException(String message, String expected, String obtained) {
        super(message);
        this.expected = expected;
        this.obtained = obtained;
    }

    public DicomModalityException(String message, Throwable cause, String expected, String obtained) {
        super(message, cause);
        this.expected = expected;
        this.obtained = obtained;
    }

    public DicomModalityException(Throwable cause, String expected, String obtained) {
        super(cause);
        this.expected = expected;
        this.obtained = obtained;
    }

    public DicomModalityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String expected, String obtained) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.expected = expected;
        this.obtained = obtained;
    }
}
