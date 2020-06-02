package org.rt.rtkj.dicom;

import lombok.Data;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;

import java.util.Optional;

@Data
public class ReducedCodeItem {
    private Optional<String> codeValue = Optional.empty();
    private Optional<String> codingSchemeDesignator = Optional.empty();
    private Optional<String> codeMeaning = Optional.empty();

    static Optional<ReducedCodeItem> build(Attributes attr) {
        ReducedCodeItem item = new ReducedCodeItem();
        if (attr == null) return Optional.empty();
        item.codeValue = Optional.ofNullable(attr.getString(Tag.CodeValue));
        item.codingSchemeDesignator = Optional.ofNullable(attr.getString(Tag.CodingSchemeDesignator));
        item.codeMeaning = Optional.ofNullable(attr.getString(Tag.CodeMeaning));
        return Optional.of(item);
    }
}
