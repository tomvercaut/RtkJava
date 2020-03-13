package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.rt.rtkj.dicom.Modality;
import org.rt.rtkj.dicom.PatientPosition;
import org.rt.rtkj.dicom.PixelRepresentation;

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
    private double[] pixelSpacing;
    private double rescaleIntercept;
    private double rescaleSlope;
    private PixelRepresentation pixelRepresentation;
    private int bitsAllocated;
    private List<Double> pixels;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private RowMajorIndex2D indexer = new RowMajorIndex2D();


    public Image2D() {
        resize(0, 0);
    }

    public Image2D(int rows, int columns) {
        resize(rows, columns);
    }

    public boolean setPixel(int row, int column, double val) {
        Optional<Integer> index = indexer.offset(row, column);
        if (index.isPresent()) {
            pixels.set(index.get(), val);
            return true;
        }
        return false;
    }

    public void resize(int rows, int columns) {
        indexer.rows = rows;
        indexer.cols = columns;
        int n = indexer.size();
        pixels = null;
        if (n > 0) {
            pixels = new ArrayList<Double>(n);
            IntStream.range(0, n).forEach(i -> pixels.add(0.0));
        }
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
    }

    public int getRows() {
        return indexer.rows;
    }

    public int getColumns() {
        return indexer.cols;
    }
}
