package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class ReferencedCalculatedDoseReferenceItem {
    private double calculatedDoseReferenceDoseValue;
    private int referencedDoseReferenceNumber;
}
