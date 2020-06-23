package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ContourItem {
    private Option<List<ReferencedSOPClassInstanceItem>> contourImageSequence = Option.empty();
    private Option<String> contourGeometricType = Option.empty();
    private Option<Integer> numberOfContourPoints = Option.empty();
    private Option<Integer> contourNumber = Option.empty();
    private Option<List<Double>> contourData = Option.empty();
}
