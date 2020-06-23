package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.rt.rtkj.Option;

@Data
public class ReferencedSOPClassInstanceItem {
    private Option<String> referencedSOPClassUID = Option.empty();
    private Option<String> referencedSOPInstanceUID = Option.empty();

    public static Option<ReferencedSOPClassInstanceItem> build(Attributes attr) {
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        if (attr == null) return Option.empty();
        item.referencedSOPClassUID = Option.of(attr.getString(Tag.ReferencedSOPClassUID));
        item.referencedSOPInstanceUID = Option.of(attr.getString(Tag.ReferencedSOPInstanceUID));
        return Option.of(item);
    }
}
