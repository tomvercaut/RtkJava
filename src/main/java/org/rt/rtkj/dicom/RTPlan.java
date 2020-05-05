package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//TODO complete RTPLAN API, the currently it is a minimal implementation

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RTPlan extends MetaHeader {
    private String SOPClassUID;
    private String SOPInstanceUID;
    private String PatientName;
    private String PatientID;
    private String RTPlanLabel;
    private String RTPlanName;
    private String RTPlanDescription;

    public RTPlan(MetaHeader meta) {
        super(meta);
    }
}
