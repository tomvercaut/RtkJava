package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.util.List;

@Data
public class RTROIObservationsItem {
    private Option<Integer> observationNumber = Option.empty();
    private Option<Integer> referencedROINumber = Option.empty();
    private Option<String> rOIObservationLabel = Option.empty();
    private Option<String> rTROIInterpretedType = Option.empty();
    private Option<String> rOIInterpreter = Option.empty();
    private Option<List<ROIPhysicalPropertiesItem>> rOIPhysicalPropertiesSequence = Option.empty();
    private Option<String> materialID = Option.empty();
}
