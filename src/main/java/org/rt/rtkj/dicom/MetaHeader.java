package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetaHeader {
    private int fileMetaInformationGroupLength;
    private byte[] fileMetaInformationVersion;
    private String mediaStorageSOPClassUID;
    private String mediaStorageSOPInstanceUID;
    private String transferSyntaxUID;
    private String implementationClassUID;
    private String implementationVersionName;

    public MetaHeader(MetaHeader hdr) {
        if (hdr != null) {
            fileMetaInformationGroupLength = hdr.fileMetaInformationGroupLength;
            fileMetaInformationVersion = hdr.fileMetaInformationVersion;
            mediaStorageSOPClassUID = hdr.mediaStorageSOPClassUID;
            mediaStorageSOPInstanceUID = hdr.mediaStorageSOPInstanceUID;
            transferSyntaxUID = hdr.transferSyntaxUID;
            implementationClassUID = hdr.implementationClassUID;
            implementationVersionName = hdr.implementationVersionName;
        }
    }
}
