package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;

import java.util.Optional;

@Data
public class ReferencedSOPClassInstanceItem {
    private Optional<String> referencedSOPClassUID = Optional.empty();
    private Optional<String> referencedSOPInstanceUID = Optional.empty();

    public static Optional<ReferencedSOPClassInstanceItem> build(Attributes attr) {
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        if (attr == null) return Optional.empty();
        item.referencedSOPClassUID = Optional.of(attr.getString(Tag.ReferencedSOPClassUID));
        item.referencedSOPInstanceUID = Optional.of(attr.getString(Tag.ReferencedSOPInstanceUID));
        return Optional.of(item);
    }
}
