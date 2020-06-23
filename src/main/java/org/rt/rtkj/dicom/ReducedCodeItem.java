package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.rt.rtkj.Option;

@Data
public class ReducedCodeItem {
    private Option<String> codeValue = Option.empty();
    private Option<String> codingSchemeDesignator = Option.empty();
    private Option<String> codeMeaning = Option.empty();

    static Option<ReducedCodeItem> build(Attributes attr) {
        ReducedCodeItem item = new ReducedCodeItem();
        if (attr == null) return Option.empty();
        item.codeValue = Option.ofNullable(attr.getString(Tag.CodeValue));
        item.codingSchemeDesignator = Option.ofNullable(attr.getString(Tag.CodingSchemeDesignator));
        item.codeMeaning = Option.ofNullable(attr.getString(Tag.CodeMeaning));
        return Option.of(item);
    }
}
