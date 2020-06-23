package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.rt.rtkj.Option;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RTReferencedStudyItem extends ReferencedSOPClassInstanceItem {
    private Option<List<RTReferencedSeriesItem>> rtReferencedSeriesSequence = Option.empty();

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
