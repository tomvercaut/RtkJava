package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class RTROIObservationsItem {
    private Optional<Integer> observationNumber;
    private Optional<Integer> referencedROINumber;
    private Optional<String> rOIObservationLabel;
    private Optional<String> rTROIInterpretedType;
    private Optional<String> rOIInterpreter;
    private Optional<List<ROIPhysicalPropertiesItem>> rOIPhysicalPropertiesSequence;
    private Optional<String> materialID;
}
