package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.util.Precision;

import java.util.List;

@Data
public class Serie {
    private String seriesInstanceUID;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Image3D> images;

    public void add(Image2D slice) {
        if (slice == null || slice.getSOPInstanceUID().isEmpty() || slice.getSeriesInstanceUID().isEmpty()) return;
        if (seriesInstanceUID.isEmpty() && images.isEmpty()) seriesInstanceUID = slice.getSeriesInstanceUID();
        for (Image3D image3D : images) {
            if (image3D.getFrameOfReferenceUID().equals(slice.getFrameOfReferenceUID()) &&
                    image3D.getModality().equals(slice.getModality()) &&
                    image3D.getStudyInstanceUID().equals(slice.getStudyInstanceUID()) &&
                    image3D.getPatientPosition().equals(slice.getPatientPosition()) &&
                    image3D.getPixelSpacing() != null && image3D.getPixelSpacing().length == 2 &&
                    slice.getPixelSpacing() != null && slice.getPixelSpacing().length == 2 &&
                    Precision.equals(image3D.getPixelSpacing()[0], slice.getPixelSpacing()[0], Precision.EPSILON) &&
                    Precision.equals(image3D.getPixelSpacing()[1], slice.getPixelSpacing()[1], Precision.EPSILON) &&
                    image3D.getImageOrientationPatient() != null && image3D.getImageOrientationPatient().length == 6 &&
                    slice.getImageOrientationPatient() != null && slice.getImageOrientationPatient().length == 6 &&
                    Precision.equals(image3D.getImageOrientationPatient()[0], slice.getImageOrientationPatient()[0], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient()[1], slice.getImageOrientationPatient()[1], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient()[2], slice.getImageOrientationPatient()[2], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient()[3], slice.getImageOrientationPatient()[3], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient()[4], slice.getImageOrientationPatient()[4], Precision.EPSILON) &&
                    Precision.equals(image3D.getImageOrientationPatient()[5], slice.getImageOrientationPatient()[5], Precision.EPSILON) &&
                    image3D.getPixelRepresentation() == slice.getPixelRepresentation() &&
                    image3D.getBitsAllocated() == slice.getBitsAllocated()
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
