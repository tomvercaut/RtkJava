package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

@Data
public class DVHReferencedROIItem {
    private Option<String> dvhROIContributionType = Option.empty();
    private Option<Integer> referencedROINumber = Option.empty();
}
