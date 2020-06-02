package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientOrientationCodeItem extends CodeItem {
    private Optional<List<CodeItem>> patientOrientationModifierCodeSequence = Optional.empty();
}
