package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.ejml.simple.SimpleMatrix;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PatientPosition;
import org.rt.rtkj.dicom.PixelRepresentation;
import org.rt.rtkj.utils.Transform4x4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Data
public class Image2D {
    private String sOPInstanceUID;
    private String frameOfReferenceUID;
    private Modality modality;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private PatientPosition patientPosition;
    private double[] imagePositionPatient;
    private double[] imageOrientationPatient;
    private double[] pixelSpacing; // 0: row spacing / 1: column spacing
    private double rescaleIntercept;
    private double rescaleSlope;
    private PixelRepresentation pixelRepresentation;
    private int bitsAllocated;
    private List<Double> pixels;
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
        if (index.isPresent()) {
            pixels.set(index.get(), val);
            return true;
        }
        return false;
    }

    public Optional<Double> getValue(int column, int row) {
        Optional<Integer> index = indexer.offset(column, row);
        if (index.isPresent()) {
            try {
                var val = pixels.get(index.get());
                return Optional.of(val);
            } catch (IndexOutOfBoundsException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    public Optional<Double> getScaledValue(int colum, int row) {
        var optVal = getValue(colum, row);
        if (optVal.isPresent())
            return Optional.of(optVal.get() * getRescaleSlope() + getRescaleIntercept());
        return Optional.empty();
    }

    public void resize(int columns, int rows) {
        indexer.rows = rows;
        indexer.cols = columns;
        int n = indexer.size();
        pixels = null;
        if (n > 0) {
            pixels = new ArrayList<Double>(n);
            IntStream.range(0, n).forEach(i -> pixels.add(0.0));
        }
        updateInverseTm = true;
    }

    public void clear() {
        sOPInstanceUID = "";
        frameOfReferenceUID = "";
        modality = Modality.UNKNOWN;
        patientPosition = PatientPosition.UNKOWN;
        imagePositionPatient = null;
        imageOrientationPatient = null;
        indexer.rows = 0;
        indexer.cols = 0;
        pixelSpacing = null;
        rescaleIntercept = 0.0;
        rescaleSlope = 0.0;
        pixelRepresentation = PixelRepresentation.NONE;
        bitsAllocated = 0;
        pixels = null;
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

    public void setImagePositionPatient(double[] imagePositionPatient) {
        updateInverseTm = true;
        this.imagePositionPatient = imagePositionPatient;
        this.tm.mat.set(0, 3, imagePositionPatient[0]);
        this.tm.mat.set(1, 3, imagePositionPatient[1]);
        this.tm.mat.set(2, 3, imagePositionPatient[2]);
        this.tm.mat.set(3, 3, 1.0);
    }

    public void setImageOrientationPatient(double[] imageOrientationPatient) {
        updateInverseTm = true;
        this.imageOrientationPatient = imageOrientationPatient;
        this.tm.mat.set(0, 0, imageOrientationPatient[0] * pixelSpacing[1]);
        this.tm.mat.set(1, 0, imageOrientationPatient[1] * pixelSpacing[1]);
        this.tm.mat.set(2, 0, imageOrientationPatient[2] * pixelSpacing[1]);
        this.tm.mat.set(0, 1, imageOrientationPatient[3] * pixelSpacing[0]);
        this.tm.mat.set(1, 1, imageOrientationPatient[4] * pixelSpacing[0]);
        this.tm.mat.set(2, 1, imageOrientationPatient[5] * pixelSpacing[0]);
    }

    private void updateInverse() {
        if (updateInverseTm) {
            itm = tm.invert();
        }
        updateInverseTm = false;
    }
}
