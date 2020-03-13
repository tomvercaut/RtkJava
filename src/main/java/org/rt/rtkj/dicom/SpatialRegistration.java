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
public class SpatialRegistration extends MetaHeader {
    private String specificCharacterSet;
    private LocalDate instanceCreationDate;
    private LocalTime instanceCreationTime;
    private String sOPClassUID;
    private String sOPInstanceUID;
    private LocalDate studyDate;
    private LocalDate seriesDate;
    private LocalDate contentDate;
    private LocalTime studyTime;
    private LocalTime seriesTime;
    private LocalTime contentTime;
    private String accessionNumber;
    private Modality modality;
    private String manufacturer;
    private String referringPhysicianName;
    private String seriesDescription;
    private String manufacturerModelName;
    private List<ReferencedSeriesItem> referencedSeriesSequence = new ArrayList<>();
    private List<StudiesContainingOtherReferencedInstancesItem> studiesContainingOtherReferencedInstancesSequence = new ArrayList<>();
    private String patientName;
    private String patientID;
    private String patientBirthDate;
    private String patientSex;
    private String softwareVersions;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private String studyID;
    private int seriesNumber;
    private int instanceNumber;
    private String frameOfReferenceUID;
    private String positionReferenceIndicator;
    private String contentLabel;
    private String contentDescription;
    private String contentCreatorName;
    private List<RegistrationItem> registrationSequence = new ArrayList<>();

    public SpatialRegistration(MetaHeader meta) {
        super(meta);
    }
}
