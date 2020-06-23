package org.rt.rtkj.dicom;

import lombok.Data;
import org.rt.rtkj.Option;

import java.time.LocalDateTime;

@Data
public class CodeItem {
    private Option<String> codeValue = Option.empty();
    private Option<String> codingSchemeDesignator = Option.empty();
    private Option<String> codeMeaning = Option.empty();
    private Option<String> mappingResource = Option.empty();
    private Option<LocalDateTime> contextGroupVersion = Option.empty();
    private Option<String> contextIdentifier = Option.empty();
}
