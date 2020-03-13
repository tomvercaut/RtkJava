package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;

@Data
public class ReferencedSOPClassInstanceItem {
    private String referencedSOPClassUID;
    private String referencedSOPInstanceUID;

    public static ReferencedSOPClassInstanceItem build(Attributes attr) {
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        if (attr == null) return item;
        item.referencedSOPClassUID = attr.getString(Tag.ReferencedSOPClassUID, "");
        item.referencedSOPInstanceUID = attr.getString(Tag.ReferencedSOPInstanceUID, "");
        return item;
    }
}
