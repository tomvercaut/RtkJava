package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

@Data
public class StructureSetROIItem {
    private Option<Integer> rOINumber = Option.empty();
    private Option<String> referencedFrameOfReferenceUID = Option.empty();
    private Option<String> rOIName = Option.empty();
    private Option<String> rOIGenerationAlgorithm = Option.empty();
}
