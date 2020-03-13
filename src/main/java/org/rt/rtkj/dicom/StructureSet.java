package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StructureSet extends MetaHeader {
    private String SpecificCharacterSet;
    private LocalDate InstanceCreationDate;
    private LocalTime InstanceCreationTime;
    private String SOPClassUID;
    private String SOPInstanceUID;
    private LocalDate StudyDate;
    private LocalTime StudyTime;
    private String AccessionNumber;
    private Modality Modality;
    private String Manufacturer;
    private String ReferringPhysicianName;
    private String SeriesDescription;
    private String OperatorsName;
    private String ManufacturerModelName;
    private String PatientName;
    private String PatientID;
    private LocalDate PatientBirthDate;
    private String PatientSex;
    private String SoftwareVersions;
    private String StudyInstanceUID;
    private String SeriesInstanceUID;
    private String StudyID;
    private int SeriesNumber;
    private String FrameOfReferenceUID;
    private String PositionReferenceIndicator;
    private String StructureSetLabel;
    private LocalDate StructureSetDate;
    private LocalTime StructureSetTime;
    private List<ReferencedFrameOfReferenceItem> referencedFrameOfReferenceSequence = new ArrayList<>();
    private List<StructureSetROIItem> structureSetROISequence = new ArrayList<>();
    private List<ROIContourItem> roiContourSequence = new ArrayList<>();
    private List<RTROIObservationsItem> rtROIObservationsSequence = new ArrayList<>();
    private String approvalStatus;

    public StructureSet(MetaHeader meta) {
        super(meta);
    }
}
