package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ReferencedSeriesItem {
    private Optional<List<ReferencedSOPClassInstanceItem>> referencedInstanceSequence = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
}
