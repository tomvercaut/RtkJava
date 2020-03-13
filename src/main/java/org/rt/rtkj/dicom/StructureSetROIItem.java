package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class StructureSetROIItem {
    private int rOINumber;
    private String referencedFrameOfReferenceUID;
    private String rOIName;
    private String rOIGenerationAlgorithm;
}
