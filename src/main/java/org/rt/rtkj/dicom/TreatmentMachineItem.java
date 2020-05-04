package org.rt.rtkj.dicom;

import lombok.Data;

@Data
public class TreatmentMachineItem {
    private String manufacturer;
    private String institutionName;
    private String manufacturerModelName;
    private String deviceSerialNumber;
    private String treatmentMachineName;
}
