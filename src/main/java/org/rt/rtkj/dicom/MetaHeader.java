package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rt.rtkj.Option;

@Data
@NoArgsConstructor
public class MetaHeader {
    private Option<Integer> fileMetaInformationGroupLength;
    private Option<Byte[]> fileMetaInformationVersion;
    private Option<String> mediaStorageSOPClassUID;
    private Option<String> mediaStorageSOPInstanceUID;
    private Option<String> transferSyntaxUID;
    private Option<String> implementationClassUID;
    private Option<String> implementationVersionName;

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
