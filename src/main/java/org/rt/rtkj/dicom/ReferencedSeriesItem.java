package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class ReferencedSeriesItem {
    private Option<List<ReferencedSOPClassInstanceItem>> referencedInstanceSequence = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
}
