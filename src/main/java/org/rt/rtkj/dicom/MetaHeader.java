package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.rt.rtkj.Option;

@Data
@NoArgsConstructor
public class MetaHeader {
    private Option<Integer> fileMetaInformationGroupLength = Option.empty();
    private Option<Byte[]> fileMetaInformationVersion = Option.empty();
    private Option<String> mediaStorageSOPClassUID = Option.empty();
    private Option<String> mediaStorageSOPInstanceUID = Option.empty();
    private Option<String> transferSyntaxUID = Option.empty();
    private Option<String> implementationClassUID = Option.empty();
    private Option<String> implementationVersionName = Option.empty();

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

    public void clear() {
        fileMetaInformationGroupLength = Option.empty();
        fileMetaInformationVersion = Option.empty();
        mediaStorageSOPClassUID = Option.empty();
        mediaStorageSOPInstanceUID = Option.empty();
        transferSyntaxUID = Option.empty();
        implementationClassUID = Option.empty();
        implementationVersionName = Option.empty();
    }
}
