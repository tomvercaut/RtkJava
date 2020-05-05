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
public class RTBeamsTreatmentRecordStorage extends MetaHeader {
    private Optional<LocalDate> instanceCreationDate;
    private Optional<LocalTime> instanceCreationTime;
    private String sOPClassUID;
    private String sOPInstanceUID;
    private Optional<LocalDate> studyDate;
    private Optional<LocalTime> studyTime;
    private String accessionNumber;
    private Modality modality;
    private String manufacturer;
    private String referringPhysicianName;
    private String operatorsName;
    private String manufacturerModelName;
    private String patientName;
    private String patientID;
    private Optional<LocalDate> patientBirthDate;
    private String patientSex;
    private String ethnicGroup;
    private String softwareVersions;
    private String studyInstanceUID;
    private String seriesInstanceUID;
    private String studyID;
    private int seriesNumber;
    private int instanceNumber;
    private List<TreatmentSessionBeamItem> treatmentSessionBeamSequence = new ArrayList<>();
    private Optional<LocalDate> treatmentDate;
    private Optional<LocalTime> treatmentTime;
    private int numberOfFractionsPlanned;
    private String primaryDosimeterUnit;
    private List<TreatmentMachineItem> treatmentMachineSequence = new ArrayList<>();
    private List<ReferencedSOPClassInstanceItem> referencedRTPlanSequence = new ArrayList<>();

    public RTBeamsTreatmentRecordStorage(MetaHeader meta) {
        super(meta);
    }
}
