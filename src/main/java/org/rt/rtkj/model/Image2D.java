package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;
import org.rt.rtkj.dicom.HasImagePositionPatient;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PatientPosition;
import org.rt.rtkj.dicom.PixelRepresentation;
import org.rt.rtkj.utils.Transform4x4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Data
public class Image2D implements HasImagePositionPatient {
    private Optional<String> sOPInstanceUID = Optional.empty();
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<Modality> modality = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<PatientPosition> patientPosition = Optional.empty();
    private Optional<Double[]> imagePositionPatient = Optional.empty();
    private Optional<Double[]> imageOrientationPatient = Optional.empty();
    private Optional<Double[]> pixelSpacing = Optional.empty(); // 0: row spacing / 1: column spacing
    private Optional<Double> rescaleIntercept = Optional.empty();
    private Optional<Double> rescaleSlope = Optional.empty();
    private Optional<PixelRepresentation> pixelRepresentation = Optional.empty();
    private Optional<Integer> bitsAllocated = Optional.empty();
    private Optional<List<Double>> pixels = Optional.empty();
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
        Optional<Integer> index = indexer.offset(column, row);
        if (pixels.isEmpty() || index.isEmpty()) return false;
        pixels.get().set(index.get(), val);
        return true;
    }

    public Optional<Double> getValue(int column, int row) {
        Optional<Integer> index = indexer.offset(column, row);
        if (pixels.isEmpty() || index.isEmpty()) return Optional.empty();
        try {
            var val = pixels.get().get(index.get());
            return Optional.of(val);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public Optional<Double> getScaledValue(int colum, int row) {
        var optVal = getValue(colum, row);
        if (optVal.isPresent() && rescaleSlope.isPresent() && rescaleIntercept.isPresent())
            return Optional.of(optVal.get() * rescaleSlope.get() + rescaleIntercept.get());
        return Optional.empty();
    }

    public void resize(int columns, int rows) {
        indexer.rows = rows;
        indexer.cols = columns;
        int n = indexer.size();
        pixels = Optional.empty();
        if (n > 0) {
            pixels = Optional.of(new ArrayList<Double>(n));
            IntStream.range(0, n).forEach(i -> pixels.get().add(0.0));
        }
        updateInverseTm = true;
    }

    public void clear() {
        sOPInstanceUID = Optional.empty();
        frameOfReferenceUID = Optional.empty();
        modality = Optional.empty();
        patientPosition = Optional.empty();
        imagePositionPatient = Optional.empty();
        imageOrientationPatient = Optional.empty();
        indexer.rows = 0;
        indexer.cols = 0;
        pixelSpacing = Optional.empty();
        rescaleIntercept = Optional.empty();
        rescaleSlope = Optional.empty();
        pixelRepresentation = Optional.empty();
        bitsAllocated = Optional.empty();
        pixels = Optional.empty();
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

    public boolean setImagePositionPatient(Optional<Double[]> imagePositionPatient) {
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

    public boolean setImageOrientationPatient(Optional<Double[]> imageOrientationPatient) {
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
