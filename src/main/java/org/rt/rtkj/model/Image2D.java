package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;
import org.rt.rtkj.Option;
import org.rt.rtkj.dicom.HasImagePositionPatient;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PatientPosition;
import org.rt.rtkj.dicom.PixelRepresentation;
import org.rt.rtkj.utils.Transform4x4;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class Image2D implements HasImagePositionPatient {
    private Option<String> sOPInstanceUID = Option.empty();
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<Modality> modality = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<PatientPosition> patientPosition = Option.empty();
    private Option<Double[]> imagePositionPatient = Option.empty();
    private Option<Double[]> imageOrientationPatient = Option.empty();
    private Option<Double[]> pixelSpacing = Option.empty(); // 0: row spacing / 1: column> spacing
    private Option<Double> rescaleIntercept = Option.empty();
    private Option<Double> rescaleSlope = Option.empty();
    private Option<PixelRepresentation> pixelRepresentation = Option.empty();
    private Option<Integer> bitsAllocated = Option.empty();
    private Option<List<Double>> pixels = Option.empty();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private RowMajorIndex2D indexer = new RowMajorIndex2D();


    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Transform4x4 tm = new Transform4x4();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Transform4x4 itm = new Transform4x4();
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private boolean updateInverseTm;

    public Image2D() {
        updateInverseTm = true;
        resize(0, 0);
    }

    public Image2D(int columns, int rows) {
        updateInverseTm = true;
        resize(columns, rows);
    }

    public boolean setValue(int column, int row, double val) {
        Option<Integer> index = indexer.offset(column, row);
        if (pixels.isEmpty() || index.isEmpty()) return false;
        pixels.get().set(index.get(), val);
        return true;
    }

    public Option<Double> getValue(int column, int row) {
        Option<Integer> index = indexer.offset(column, row);
        if (pixels.isEmpty() || index.isEmpty()) return Option.empty();
        try {
            var val = pixels.get().get(index.get());
            return Option.of(val);
        } catch (IndexOutOfBoundsException e) {
            return Option.empty();
        }
    }

    public Option<Double> getScaledValue(int colum, int row) {
        var optVal = getValue(colum, row);
        if (optVal.isPresent() && rescaleSlope.isPresent() && rescaleIntercept.isPresent())
            return Option.of(optVal.get() * rescaleSlope.get() + rescaleIntercept.get());
        return Option.empty();
    }

    public void resize(int columns, int rows) {
        indexer.rows = rows;
        indexer.cols = columns;
        int n = indexer.size();
        pixels = Option.empty();
        if (n > 0) {
            pixels = Option.of(new ArrayList<Double>(n));
            IntStream.range(0, n).forEach(i -> pixels.get().add(0.0));
        }
        updateInverseTm = true;
    }

    public void clear() {
        sOPInstanceUID = Option.empty();
        frameOfReferenceUID = Option.empty();
        modality = Option.empty();
        patientPosition = Option.empty();
        imagePositionPatient = Option.empty();
        imageOrientationPatient = Option.empty();
        indexer.rows = 0;
        indexer.cols = 0;
        pixelSpacing = Option.empty();
        rescaleIntercept = Option.empty();
        rescaleSlope = Option.empty();
        pixelRepresentation = Option.empty();
        bitsAllocated = Option.empty();
        pixels = Option.empty();
        tm.zero();
        itm.zero();
    }

    public int getRows() {
        return indexer.rows;
    }

    public int getColumns() {
        return indexer.cols;
    }

    /**
     * Convert matrix indices to physical coordinates.
     *
     * @param col column index
     * @param row row index
     * @return Column vector with physical coordinates
     */
    public SimpleMatrix i2c(int col, int row) {
        SimpleMatrix p = new SimpleMatrix(4, 1);
        p.set(0, 0, col);
        p.set(1, 0, row);
        p.set(2, 0, 0);
        p.set(3, 0, 1);
        return tm.apply(p);
    }

// Inverse doesn't always exist.
//    /**
//     * TODO check validity of the depth coordinate and the inverse transform.
//     * Convert physical coordinates to matrix coordinates
//     * @param col column coordinate
//     * @param row row coordinate
//     * @param depth depth coordinate
//     * @return Matrix with column and row indices.
//     */
//    public SimpleMatrix c2i(double col, double row, double depth) {
//        if(updateInverseTm) updateInverse();
//        SimpleMatrix p = new SimpleMatrix(4, 1);
//        p.set(0, 0, col);
//        p.set(1, 0, row);
//        p.set(2, 0, depth);
//        p.set(3, 0, 1);
//        return itm.apply(p);
//    }

    public boolean setImagePositionPatient(Option<Double[]> imagePositionPatient) {
        updateInverseTm = true;
        this.imagePositionPatient = imagePositionPatient;
        if (this.imagePositionPatient.isPresent()) {
            this.tm.mat.set(0, 3, imagePositionPatient.get()[0]);
            this.tm.mat.set(1, 3, imagePositionPatient.get()[1]);
            this.tm.mat.set(2, 3, imagePositionPatient.get()[2]);
            this.tm.mat.set(3, 3, 1.0);
            return true;
        } else {
            return false;
        }
    }

    public boolean setImageOrientationPatient(Option<Double[]> imageOrientationPatient) {
        updateInverseTm = true;
        this.imageOrientationPatient = imageOrientationPatient;
        if (this.imageOrientationPatient.isPresent()) {
            this.tm.mat.set(0, 0, imageOrientationPatient.get()[0] * pixelSpacing.get()[1]);
            this.tm.mat.set(1, 0, imageOrientationPatient.get()[1] * pixelSpacing.get()[1]);
            this.tm.mat.set(2, 0, imageOrientationPatient.get()[2] * pixelSpacing.get()[1]);
            this.tm.mat.set(0, 1, imageOrientationPatient.get()[3] * pixelSpacing.get()[0]);
            this.tm.mat.set(1, 1, imageOrientationPatient.get()[4] * pixelSpacing.get()[0]);
            this.tm.mat.set(2, 1, imageOrientationPatient.get()[5] * pixelSpacing.get()[0]);
            return true;
        }
        return false;
    }

    private void updateInverse() {
        if (updateInverseTm) {
            itm = tm.invert();
        }
        updateInverseTm = false;
    }
}
