package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ReferencedSeriesItem {
    private List<ReferencedSOPClassInstanceItem> referencedInstanceSequence = new ArrayList<>();
    private String seriesInstanceUID;
}
