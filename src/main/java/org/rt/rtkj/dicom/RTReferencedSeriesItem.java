package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RTReferencedSeriesItem {
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<List<ReferencedSOPClassInstanceItem>> contourImageSequence = Optional.empty();
}
