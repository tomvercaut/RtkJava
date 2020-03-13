package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ContourItem {
    private List<ReferencedSOPClassInstanceItem> contourImageSequence = new ArrayList<>();
    private String contourGeometricType;
    private int numberOfContourPoints;
    private int contourNumber;
    private List<Double> contourData = new ArrayList<>();
}
