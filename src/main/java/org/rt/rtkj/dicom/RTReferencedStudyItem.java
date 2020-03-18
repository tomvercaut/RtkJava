package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RTReferencedStudyItem extends ReferencedSOPClassInstanceItem {
    private List<RTReferencedSeriesItem> rtReferencedSeriesSequence = new ArrayList<>();

    public RTReferencedStudyItem() {
        super();
    }

    public RTReferencedStudyItem(ReferencedSOPClassInstanceItem item) {
        if (item != null) {
            this.setReferencedSOPClassUID(item.getReferencedSOPClassUID());
            this.setReferencedSOPInstanceUID(item.getReferencedSOPInstanceUID());
        }
    }
}
