package org.rt.rtkj.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Study {
    private String studyInstanceUID;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.NONE)
    private List<Serie> series;

    public Study() {
        studyInstanceUID = "";
        series = new ArrayList<>();
    }

    public Study(String studyInstanceUID) {
        if (studyInstanceUID == null || studyInstanceUID.isBlank())
            this.studyInstanceUID = "";
        else
            this.studyInstanceUID = studyInstanceUID;
        series = new ArrayList<>();
    }

    public void add(Image2D image) {
        if (series == null) series = new ArrayList<>();
        if (image == null || image.getSOPInstanceUID().isEmpty() ||
                image.getStudyInstanceUID().isEmpty() || image.getSeriesInstanceUID().isEmpty())
            return;
        if (series.isEmpty() && studyInstanceUID.isBlank())
            this.studyInstanceUID = image.getStudyInstanceUID().get();
        if (!this.studyInstanceUID.equals(image.getStudyInstanceUID().get())) return;
        String uid = image.getSeriesInstanceUID().get();
        for (Serie serie : series) {
            if (serie.getSeriesInstanceUID().equals(uid)) {
                serie.add(image);
                return;
            }
        }
        Serie serie = new Serie();
        serie.setSeriesInstanceUID(uid);
        serie.add(image);
        series.add(serie);
    }
}
