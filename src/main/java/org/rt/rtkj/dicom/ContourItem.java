package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ContourItem {
    private Optional<List<ReferencedSOPClassInstanceItem>> contourImageSequence = Optional.empty();
    private Optional<String> contourGeometricType = Optional.empty();
    private Optional<Integer> numberOfContourPoints = Optional.empty();
    private Optional<Integer> contourNumber = Optional.empty();
    private Optional<List<Double>> contourData = Optional.empty();
}
