package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class CodeItem {
    private Optional<String> codeValue = Optional.empty();
    private Optional<String> codingSchemeDesignator = Optional.empty();
    private Optional<String> codeMeaning = Optional.empty();
    private Optional<String> mappingResource = Optional.empty();
    private Optional<LocalDateTime> contextGroupVersion = Optional.empty();
    private Optional<String> contextIdentifier = Optional.empty();
}
