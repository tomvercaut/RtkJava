package org.rt.rtkj.dicom;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.rt.rtkj.Option;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpatialRegistration extends MetaHeader {
    private Option<String> specificCharacterSet = Option.empty();
    private Option<LocalDate> instanceCreationDate = Option.empty();
    private Option<LocalTime> instanceCreationTime = Option.empty();
    private Option<String> sOPClassUID = Option.empty();
    private Option<String> sOPInstanceUID = Option.empty();
    private Option<LocalDate> studyDate = Option.empty();
    private Option<LocalDate> seriesDate = Option.empty();
    private Option<LocalDate> contentDate = Option.empty();
    private Option<LocalTime> studyTime = Option.empty();
    private Option<LocalTime> seriesTime = Option.empty();
    private Option<LocalTime> contentTime = Option.empty();
    private Option<String> accessionNumber = Option.empty();
    private Option<Modality> modality = Option.empty();
    private Option<String> manufacturer = Option.empty();
    private Option<String> referringPhysicianName = Option.empty();
    private Option<String> seriesDescription = Option.empty();
    private Option<String> manufacturerModelName = Option.empty();
    private Option<List<ReferencedSeriesItem>> referencedSeriesSequence = Option.empty();
    private Option<List<StudiesContainingOtherReferencedInstancesItem>> studiesContainingOtherReferencedInstancesSequence = Option.empty();
    private Option<String> patientName = Option.empty();
    private Option<String> patientID = Option.empty();
    private Option<String> patientBirthDate = Option.empty();
    private Option<String> patientSex = Option.empty();
    private Option<String> softwareVersions = Option.empty();
    private Option<String> studyInstanceUID = Option.empty();
    private Option<String> seriesInstanceUID = Option.empty();
    private Option<String> studyID = Option.empty();
    private Option<Integer> seriesNumber = Option.empty();
    private Option<Integer> instanceNumber = Option.empty();
    private Option<String> frameOfReferenceUID = Option.empty();
    private Option<String> positionReferenceIndicator = Option.empty();
    private Option<String> contentLabel = Option.empty();
    private Option<String> contentDescription = Option.empty();
    private Option<String> contentCreatorName = Option.empty();
    private Option<List<RegistrationItem>> registrationSequence = Option.empty();

    public SpatialRegistration(MetaHeader meta) {
        super(meta);
    }

    /**
     * Clear the meta header
     */
    public void clearMetaHeader() {
        super.clear();
    }
}
