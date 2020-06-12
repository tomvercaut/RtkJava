package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class RTROIObservationsItem {
    private Optional<Integer> observationNumber = Optional.empty();
    private Optional<Integer> referencedROINumber = Optional.empty();
    private Optional<String> rOIObservationLabel = Optional.empty();
    private Optional<String> rTROIInterpretedType = Optional.empty();
    private Optional<String> rOIInterpreter = Optional.empty();
    private Optional<List<ROIPhysicalPropertiesItem>> rOIPhysicalPropertiesSequence = Optional.empty();
    private Optional<String> materialID = Optional.empty();
}
