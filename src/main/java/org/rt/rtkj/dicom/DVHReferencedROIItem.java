package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class DVHReferencedROIItem {
    private String dvhROIContributionType;
    private int referencedROINumber;
}
