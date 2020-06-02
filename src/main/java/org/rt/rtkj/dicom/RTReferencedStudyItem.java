package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class RTReferencedStudyItem extends ReferencedSOPClassInstanceItem {
    private Optional<List<RTReferencedSeriesItem>> rtReferencedSeriesSequence = Optional.empty();

    public RTReferencedStudyItem() {
        super();
    }

    public RTReferencedStudyItem(ReferencedSOPClassInstanceItem item) {
        super();
        if (item != null) {
            this.setReferencedSOPClassUID(item.getReferencedSOPClassUID());
            this.setReferencedSOPInstanceUID(item.getReferencedSOPInstanceUID());
        }
    }
}
