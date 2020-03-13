package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RTReferencedSeriesItem {
    private String seriesInstanceUID;
    private List<ReferencedSOPClassInstanceItem> contourImageSequence = new ArrayList<>();
}
