package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;

@Data
public class ReducedCodeItem {
    private String codeValue;
    private String codingSchemeDesignator;
    private String codeMeaning;

    static ReducedCodeItem build(Attributes attr) {
        ReducedCodeItem item = new ReducedCodeItem();
        if (attr == null) return item;
        item.codeValue = attr.getString(Tag.CodeValue, "");
        item.codingSchemeDesignator = attr.getString(Tag.CodingSchemeDesignator, "");
        item.codeMeaning = attr.getString(Tag.CodeMeaning, "");
        return item;
    }
}
