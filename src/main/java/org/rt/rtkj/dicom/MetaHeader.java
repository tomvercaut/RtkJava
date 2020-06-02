package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class MetaHeader {
    private Optional<Integer> fileMetaInformationGroupLength;
    private Optional<byte[]> fileMetaInformationVersion;
    private Optional<String> mediaStorageSOPClassUID;
    private Optional<String> mediaStorageSOPInstanceUID;
    private Optional<String> transferSyntaxUID;
    private Optional<String> implementationClassUID;
    private Optional<String> implementationVersionName;

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
