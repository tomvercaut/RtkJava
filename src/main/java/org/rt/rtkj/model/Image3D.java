package org.rt.rtkj.model;

import lombok.Data;
import org.rt.rtkj.Option;
import org.rt.rtkj.dicom.ImagePositionPatientComparator;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PatientPosition;
import org.rt.rtkj.dicom.PixelRepresentation;

import java.util.ArrayList;
import java.util.List;

@Data
public class Image3D {
    private List<Image2D> images;
    private boolean sorted = false;
    ImagePositionPatientComparator imagePositionPatientComparator = new ImagePositionPatientComparator();

    public void add(Image2D slice) {
        if (images == null) images = new ArrayList<>();
        if (slice.getSOPInstanceUID().isEmpty()) return;
        images.add(slice);
        sorted = false;
    }

    public Image2D get(int index) throws NullPointerException, IndexOutOfBoundsException {
        return images.get(index);
    }

    public int size() {
        return (images == null) ? 0 : images.size();
    }

    public void sort() {
        images.sort(imagePositionPatientComparator);
        sorted = true;
    }

    public Option<String> getFrameOfReferenceUID() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getFrameOfReferenceUID();
    }

    public Option<Modality> getModality() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getModality();
    }

    public Option<String> getStudyInstanceUID() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getStudyInstanceUID();
    }

    public Option<PatientPosition> getPatientPosition() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getPatientPosition();
    }

    public Option<Double[]> getPixelSpacing() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getPixelSpacing();
    }

    public Option<Double[]> getImagePositionPatient() {
        if (images.isEmpty()) return Option.empty();
        if (!sorted) sort();
        return images.get(0).getImagePositionPatient();
    }

    public Option<Double[]> getImageOrientationPatient() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getImageOrientationPatient();
    }

    public Option<PixelRepresentation> getPixelRepresentation() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getPixelRepresentation();
    }

    public Option<Integer> getBitsAllocated() {
        if (images.isEmpty()) return Option.empty();
        return images.get(0).getBitsAllocated();
    }

    public Option<Double> getValue(int column, int row, int depth) {
        if (!sorted) sort();
        int nd = images.size();
        if (depth < 0 || depth >= nd) return Option.empty();
        var image = images.get(depth);
        return image.getValue(column, row);
    }

    public Option<Double> getScaledValue(int column, int row, int depth) {
        if (!sorted) sort();
        int nd = images.size();
        if (depth < 0 || depth >= nd) return Option.empty();
        var image = images.get(depth);
        return image.getScaledValue(column, row);
    }

    //TODO compute slice thickness
}
