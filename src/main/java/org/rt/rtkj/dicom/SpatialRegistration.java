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
    private Optional<String> specificCharacterSet = Optional.empty();
    private Optional<LocalDate> instanceCreationDate = Optional.empty();
    private Optional<LocalTime> instanceCreationTime = Optional.empty();
    private Optional<String> sOPClassUID = Optional.empty();
    private Optional<String> sOPInstanceUID = Optional.empty();
    private Optional<LocalDate> studyDate = Optional.empty();
    private Optional<LocalDate> seriesDate = Optional.empty();
    private Optional<LocalDate> contentDate = Optional.empty();
    private Optional<LocalTime> studyTime = Optional.empty();
    private Optional<LocalTime> seriesTime = Optional.empty();
    private Optional<LocalTime> contentTime = Optional.empty();
    private Optional<String> accessionNumber = Optional.empty();
    private Optional<Modality> modality = Optional.empty();
    private Optional<String> manufacturer = Optional.empty();
    private Optional<String> referringPhysicianName = Optional.empty();
    private Optional<String> seriesDescription = Optional.empty();
    private Optional<String> manufacturerModelName = Optional.empty();
    private Optional<List<ReferencedSeriesItem>> referencedSeriesSequence = Optional.empty();
    private Optional<List<StudiesContainingOtherReferencedInstancesItem>> studiesContainingOtherReferencedInstancesSequence = Optional.empty();
    private Optional<String> patientName = Optional.empty();
    private Optional<String> patientID = Optional.empty();
    private Optional<String> patientBirthDate = Optional.empty();
    private Optional<String> patientSex = Optional.empty();
    private Optional<String> softwareVersions = Optional.empty();
    private Optional<String> studyInstanceUID = Optional.empty();
    private Optional<String> seriesInstanceUID = Optional.empty();
    private Optional<String> studyID = Optional.empty();
    private Optional<Integer> seriesNumber = Optional.empty();
    private Optional<Integer> instanceNumber = Optional.empty();
    private Optional<String> frameOfReferenceUID = Optional.empty();
    private Optional<String> positionReferenceIndicator = Optional.empty();
    private Optional<String> contentLabel = Optional.empty();
    private Optional<String> contentDescription = Optional.empty();
    private Optional<String> contentCreatorName = Optional.empty();
    private Optional<List<RegistrationItem>> registrationSequence = Optional.empty();

    public SpatialRegistration(MetaHeader meta) {
        super(meta);
    }
}
