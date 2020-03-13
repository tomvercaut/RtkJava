package org.rt.rtkj.dicom;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CodeItem {
    private String codeValue;
    private String codingSchemeDesignator;
    private String codeMeaning;
    private String mappingResource;
    private LocalDateTime contextGroupVersion;
    private String contextIdentifier;
}
