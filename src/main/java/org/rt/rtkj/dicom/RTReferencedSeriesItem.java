package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class RTReferencedSeriesItem {
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<List<ReferencedSOPClassInstanceItem>> contourImageSequence = Option.empty();
}
