package org.rt.rtkj.model;

import lombok.extern.log4j.Log4j2;
import org.rt.rtkj.dicom.*;

import java.util.Optional;

@Log4j2
public class ImageFactory {

    public static Optional<Image2D> build(CT ct) {
        // Validation of the input data
        if (ct == null) return Optional.empty();
        if (ct.getSOPInstanceUID().isEmpty()) {
            log.error("Missing SOP instance UID");
            return Optional.empty();
        }
        if (ct.getFrameOfReferenceUID().isEmpty()) {
            log.error("Missing frame of reference UID");
            return Optional.empty();
        }
        if (ct.getModality() == Modality.UNKNOWN) {
            log.error("Missing modality");
            return Optional.empty();
        }
        if (ct.getStudyInstanceUID().isEmpty()) {
            log.error("Missing study instance UID");
            return Optional.empty();
        }
        if (ct.getSeriesInstanceUID().isEmpty()) {
            log.error("Missing series instance UID");
            return Optional.empty();
        }
        if (ct.getPatientPosition() == PatientPosition.UNKOWN) {
            log.error("Uknown patient position");
            return Optional.empty();
        }
        if (ct.getImagePositionPatient() == null || ct.getImagePositionPatient().length != 3) {
            log.error("Missing or invalid image position patient");
            return Optional.empty();
        }
        if (ct.getImageOrientationPatient() == null || ct.getImageOrientationPatient().length != 6) {
            log.error("Missing or invalid image orientation patient");
            return Optional.empty();
        }
        if (ct.getPixelSpacing() == null || ct.getPixelSpacing().length != 2) {
            log.error("Missing or invalid pixel spacing");
            return Optional.empty();
        }
        if (ct.getPixelRepresentation() == null || ct.getPixelRepresentation() == PixelRepresentation.NONE) {
            log.error("Missing or invalid pixel representation");
            return Optional.empty();
        }
        if (ct.getBitsAllocated() < 1) {
            log.error("Missing or invalid number of allocated bits");
            return Optional.empty();
        }
        if (ct.getSamplesPerPixel() != 1) {
            log.error("Only one sample per pixel is supported for a CT image");
            return Optional.empty();
        }
        if (ct.getPhotometricInterpretation() != PhotometricInterpretation.MONOCHROME2) {
            log.error("Only monochrome2 pixel values supported");
            return Optional.empty();
        }

        Image2D image = new Image2D();
        image.setSOPInstanceUID(ct.getSOPInstanceUID());
        image.setFrameOfReferenceUID(ct.getFrameOfReferenceUID());
        image.setModality(ct.getModality());
        image.setStudyInstanceUID(ct.getStudyInstanceUID());
        image.setPatientPosition(ct.getPatientPosition());
        image.setPixelSpacing(ct.getPixelSpacing());
        image.setImageOrientationPatient(ct.getImageOrientationPatient());
        image.setImagePositionPatient(ct.getImagePositionPatient());
        image.setPixelRepresentation(ct.getPixelRepresentation());
        image.setBitsAllocated(ct.getBitsAllocated());
        int rows = ct.getRows();
        int cols = ct.getColumns();
        int idx = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                var pixel = ct.getPixelData().get(idx);
                if (!image.setPixel(i, j, pixel)) {
                    log.error("Unable to set pixel value at [row, column] = [" + i + ", " + j + "] = " + (double) pixel);
                    return Optional.empty();
                }
            }
        }
        return Optional.of(image);
    }
}
