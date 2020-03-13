package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RTROIObservationsItem {
    private int observationNumber;
    private int referencedROINumber;
    private String rOIObservationLabel;
    private String rTROIInterpretedType;
    private String rOIInterpreter;
    private List<ROIPhysicalPropertiesItem> rOIPhysicalPropertiesSequence = new ArrayList<>();
    private String materialID;
}
