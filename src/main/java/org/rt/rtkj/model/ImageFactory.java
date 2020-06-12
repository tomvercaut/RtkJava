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
        if (ct.getModality().isEmpty() || ct.getModality().get() == Modality.UNKNOWN) {
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
        if (ct.getPatientPosition().isEmpty() || ct.getPatientPosition().get() == PatientPosition.UNKOWN) {
            log.error("Uknown patient position");
            return Optional.empty();
        }
        if (ct.getImagePositionPatient().isEmpty() || ct.getImagePositionPatient().get().length != 3) {
            log.error("Missing or invalid image position patient");
            return Optional.empty();
        }
        if (ct.getImageOrientationPatient().isEmpty() || ct.getImageOrientationPatient().get().length != 6) {
            log.error("Missing or invalid image orientation patient");
            return Optional.empty();
        }
        if (ct.getPixelSpacing().isEmpty() || ct.getPixelSpacing().get().length != 2) {
            log.error("Missing or invalid pixel spacing");
            return Optional.empty();
        }
        if (ct.getPixelRepresentation().isEmpty() || ct.getPixelRepresentation().get() == PixelRepresentation.NONE) {
            log.error("Missing or invalid pixel representation");
            return Optional.empty();
        }
        if (ct.getBitsAllocated().isEmpty() || ct.getBitsAllocated().get() < 1) {
            log.error("Missing or invalid number of allocated bits");
            return Optional.empty();
        }
        if (ct.getSamplesPerPixel().isEmpty() || ct.getSamplesPerPixel().get() != 1) {
            log.error("Only one sample per pixel is supported for a CT image");
            return Optional.empty();
        }
        if (ct.getPhotometricInterpretation().isEmpty() || ct.getPhotometricInterpretation().get() != PhotometricInterpretation.MONOCHROME2) {
            log.error("Only monochrome2 pixel values supported");
            return Optional.empty();
        }

        Image2D image = new Image2D();
        image.setSOPInstanceUID(ct.getSOPInstanceUID());
        image.setFrameOfReferenceUID(ct.getFrameOfReferenceUID());
        image.setModality(ct.getModality());
        image.setStudyInstanceUID(ct.getStudyInstanceUID());
        image.setSeriesInstanceUID(ct.getSeriesInstanceUID());
        image.setPatientPosition(ct.getPatientPosition());
        image.setPixelSpacing(ct.getPixelSpacing());
        image.setImageOrientationPatient(ct.getImageOrientationPatient());
        image.setImagePositionPatient(ct.getImagePositionPatient());
        image.setPixelRepresentation(ct.getPixelRepresentation());
        image.setBitsAllocated(ct.getBitsAllocated());
        image.setRescaleIntercept(ct.getRescaleIntercept());
        image.setRescaleSlope(ct.getRescaleSlope());
        int rows = ct.getRows().orElse(0);
        int cols = ct.getColumns().orElse(0);
        image.resize(cols, rows);
        int idx = 0;
        if (ct.getPixelData().isPresent()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    var pixel = ct.getPixelData().get().get(idx);
                    if (!image.setValue(j, i, pixel)) {
                        log.error("Unable to set pixel value at [row, column] = [" + i + ", " + j + "] = " + (double) pixel);
                        return Optional.empty();
                    }
                    idx++;
                }
            }
        }
        return Optional.of(image);
    }

    public static Optional<Image2D> build(PT pt) {
        // Validation of the input data
        if (pt == null) return Optional.empty();
        if (pt.getSOPInstanceUID().isEmpty()) {
            log.error("Missing SOP instance UID");
            return Optional.empty();
        }
        if (pt.getFrameOfReferenceUID().isEmpty()) {
            log.error("Missing frame of reference UID");
            return Optional.empty();
        }
        if (pt.getModality().isEmpty() || pt.getModality().get() == Modality.UNKNOWN) {
            log.error("Missing modality");
            return Optional.empty();
        }
        if (pt.getStudyInstanceUID().isEmpty()) {
            log.error("Missing study instance UID");
            return Optional.empty();
        }
        if (pt.getSeriesInstanceUID().isEmpty()) {
            log.error("Missing series instance UID");
            return Optional.empty();
        }
        if (pt.getPatientPosition().isEmpty() || pt.getPatientPosition().get() == PatientPosition.UNKOWN) {
            log.error("Uknown patient position");
            return Optional.empty();
        }
        if (pt.getImagePositionPatient().isEmpty() || pt.getImagePositionPatient().get().length != 3) {
            log.error("Missing or invalid image position patient");
            return Optional.empty();
        }
        if (pt.getImageOrientationPatient().isEmpty() || pt.getImageOrientationPatient().get().length != 6) {
            log.error("Missing or invalid image orientation patient");
            return Optional.empty();
        }
        if (pt.getPixelSpacing().isEmpty() || pt.getPixelSpacing().get().length != 2) {
            log.error("Missing or invalid pixel spacing");
            return Optional.empty();
        }
        if (pt.getPixelRepresentation().isEmpty() || pt.getPixelRepresentation().get() == PixelRepresentation.NONE) {
            log.error("Missing or invalid pixel representation");
            return Optional.empty();
        }
        if (pt.getBitsAllocated().isEmpty() || pt.getBitsAllocated().get() < 1) {
            log.error("Missing or invalid number of allocated bits");
            return Optional.empty();
        }
        if (pt.getSamplesPerPixel().isEmpty() || pt.getSamplesPerPixel().get() != 1) {
            log.error("Only one sample per pixel is supported for a CT image");
            return Optional.empty();
        }
        if (pt.getPhotometricInterpretation().isEmpty() || pt.getPhotometricInterpretation().get() != PhotometricInterpretation.MONOCHROME2) {
            log.error("Only monochrome2 pixel values supported");
            return Optional.empty();
        }

        Image2D image = new Image2D();
        image.setSOPInstanceUID(pt.getSOPInstanceUID());
        image.setFrameOfReferenceUID(pt.getFrameOfReferenceUID());
        image.setModality(pt.getModality());
        image.setStudyInstanceUID(pt.getStudyInstanceUID());
        image.setSeriesInstanceUID(pt.getSeriesInstanceUID());
        image.setPatientPosition(pt.getPatientPosition());
        image.setPixelSpacing(pt.getPixelSpacing());
        image.setImageOrientationPatient(pt.getImageOrientationPatient());
        image.setImagePositionPatient(pt.getImagePositionPatient());
        image.setPixelRepresentation(pt.getPixelRepresentation());
        image.setBitsAllocated(pt.getBitsAllocated());
        image.setRescaleIntercept(pt.getRescaleIntercept());
        image.setRescaleSlope(pt.getRescaleSlope());
        int rows = pt.getRows().orElse(0);
        int cols = pt.getColumns().orElse(0);
        image.resize(cols, rows);
        int idx = 0;
        if (pt.getPixelData().isPresent()) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    var pixel = pt.getPixelData().get().get(idx);
                    if (!image.setValue(j, i, pixel)) {
                        log.error("Unable to set pixel value at [row, column] = [" + i + ", " + j + "] = " + (double) pixel);
                        return Optional.empty();
                    }
                    idx++;
                }
            }
        }
        return Optional.of(image);
    }
}
