package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.Optional;

@Data
public class DVHReferencedROIItem {
    private Optional<String> dvhROIContributionType = Optional.empty();
    private Optional<Integer> referencedROINumber = Optional.empty();
}
