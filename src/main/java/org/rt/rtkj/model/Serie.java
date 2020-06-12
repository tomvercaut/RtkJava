package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.Precision;
import org.rt.rtkj.dicom.Modality;

import java.util.ArrayList;
import java.util.List;

@Data
@Log4j2
public class Serie {
    private String seriesInstanceUID;
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private List<Image3D> images;

    public void add(Image2D slice) {
        if (images == null) images = new ArrayList<>();
        if (slice == null || slice.getSOPInstanceUID().isEmpty() ||
                slice.getSeriesInstanceUID().isEmpty()) return;
        if (seriesInstanceUID.isEmpty() && images.isEmpty())
            seriesInstanceUID = slice.getSeriesInstanceUID().get();
        for (Image3D image3D : images) {
            if (image3D.getPixelSpacing().isEmpty()) {
                log.error("Iterating over a 3D image that doesn't have pixel spacing. ");
                return;
            }
            if (image3D.getImageOrientationPatient().isEmpty()) {
                log.error("Iterating over a 3D image that doesn't have an image orientation. ");
                return;
            }
            if (image3D.getModality().isEmpty()) {
                log.error("Iterating over a 3D image that doesn't have an image modality. ");
                return;
            }

            if (image3D.getFrameOfReferenceUID().equals(slice.getFrameOfReferenceUID()) &&
                    image3D.getModality().equals(slice.getModality()) &&
                    image3D.getStudyInstanceUID().equals(slice.getStudyInstanceUID()) &&
                    image3D.getPatientPosition().equals(slice.getPatientPosition()) &&
                    image3D.getPixelSpacing().get().length == 2 &&
                    slice.getPixelSpacing().get().length == 2 &&
                    Precision.equals(image3D.getPixelSpacing().get()[0], slice.getPixelSpacing().get()[0], Precision.EPSILON) &&
                    Precision.equals(image3D.getPixelSpacing().get()[1], slice.getPixelSpacing().get()[1], Precision.EPSILON) &&
                    image3D.getImageOrientationPatient().get().length == 6 &&
                    slice.getImageOrientationPatient().get().length == 6 &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[0], slice.getImageOrientationPatient().get()[0], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[1], slice.getImageOrientationPatient().get()[1], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[2], slice.getImageOrientationPatient().get()[2], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[3], slice.getImageOrientationPatient().get()[3], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[4], slice.getImageOrientationPatient().get()[4], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient().get()[5], slice.getImageOrientationPatient().get()[5], Precision.EPSILON) &&
                    image3D.getPixelRepresentation() == slice.getPixelRepresentation() &&
                    image3D.getBitsAllocated() == slice.getBitsAllocated() &&
                    (image3D.getModality().get() == Modality.CT || image3D.getModality().get() == Modality.PT || image3D.getModality().get() == Modality.MR)
            ) {
                image3D.add(slice);
                return;
            }
        }
        Image3D image3D = new Image3D();
        image3D.add(slice);
        images.add(image3D);
    }
}
