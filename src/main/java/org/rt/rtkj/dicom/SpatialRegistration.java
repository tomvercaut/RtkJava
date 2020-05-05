package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpatialRegistration extends MetaHeader {
    private String specificCharacterSet;
    private Optional<LocalDate> instanceCreationDate;
    private Optional<LocalTime> instanceCreationTime;
    private String sOPClassUID;
    private String sOPInstanceUID;
    private Optional<LocalDate> studyDate;
    private Optional<LocalDate> seriesDate;
    private Optional<LocalDate> contentDate;
    private Optional<LocalTime> studyTime;
    private Optional<LocalTime> seriesTime;
    private Optional<LocalTime> contentTime;
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
