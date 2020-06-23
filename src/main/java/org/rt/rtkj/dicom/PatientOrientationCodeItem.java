package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.rt.rtkj.Option;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PatientOrientationCodeItem extends CodeItem {
    private Option<List<CodeItem>> patientOrientationModifierCodeSequence = Option.empty();
}
