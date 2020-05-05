package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class CodeItem {
    private String codeValue;
    private String codingSchemeDesignator;
    private String codeMeaning;
    private String mappingResource;
    private Optional<LocalDateTime> contextGroupVersion;
    private String contextIdentifier;
}
