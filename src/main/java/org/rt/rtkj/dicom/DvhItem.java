package org.rt.rtkj.dicom;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DvhItem {
    private String dvhType;
    private String doseUnits;
    private String doseType;
    private double dvhDoseScaling;
    private String dvhVolumeUnits;
    private double[] dvhData;
    private List<DVHReferencedROIItem> dvhReferencedROISequence = new ArrayList<>();
    private int dvhNumberOfBins;
    private double dvhMinimumDose;
    private double dvhMaximumDose;
    private double dvhMeanDose;
}
