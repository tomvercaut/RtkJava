package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Patient {
    private String firstName;
    private String lastName;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<String> patientIDs;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Study> studies;

    public Patient() {
        patientIDs = new ArrayList<>();
        studies = new ArrayList<>();
    }

    public void add(Image2D image) {
        if (image == null ||
                image.getSOPInstanceUID().isEmpty() ||
                image.getStudyInstanceUID().isEmpty() ||
                image.getSeriesInstanceUID().isEmpty())
            return;
        String uid = image.getStudyInstanceUID();
        for (Study study : studies) {
            if (uid.equals(study.getStudyInstanceUID())) {
                study.add(image);
                return;
            }
        }
        Study study = new Study();
        study.add(image);
        studies.add(study);
    }
}
