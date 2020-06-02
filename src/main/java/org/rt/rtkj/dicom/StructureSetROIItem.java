package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.Optional;

@Data
public class StructureSetROIItem {
    private Optional<Integer> rOINumber = Optional.empty();
    private Optional<String> referencedFrameOfReferenceUID = Optional.empty();
    private Optional<String> rOIName = Optional.empty();
    private Optional<String> rOIGenerationAlgorithm = Optional.empty();
}
