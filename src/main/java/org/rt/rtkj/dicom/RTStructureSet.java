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
public class RTStructureSet extends MetaHeader {
    private Optional<String> SpecificCharacterSet = Optional.empty();
    private Optional<LocalDate> InstanceCreationDate = Optional.empty();
    private Optional<LocalTime> InstanceCreationTime = Optional.empty();
    private Optional<String> SOPClassUID = Optional.empty();
    private Optional<String> SOPInstanceUID = Optional.empty();
    private Optional<LocalDate> StudyDate = Optional.empty();
    private Optional<LocalTime> StudyTime = Optional.empty();
    private Optional<String> AccessionNumber = Optional.empty();
    private Optional<Modality> Modality = Optional.empty();
    private Optional<String> Manufacturer = Optional.empty();
    private Optional<String> ReferringPhysicianName = Optional.empty();
    private Optional<String> SeriesDescription = Optional.empty();
    private Optional<String> OperatorsName = Optional.empty();
    private Optional<String> ManufacturerModelName = Optional.empty();
    private Optional<String> PatientName = Optional.empty();
    private Optional<String> PatientID = Optional.empty();
    private Optional<LocalDate> PatientBirthDate = Optional.empty();
    private Optional<String> PatientSex = Optional.empty();
    private Optional<String> SoftwareVersions = Optional.empty();
    private Optional<String> StudyInstanceUID = Optional.empty();
    private Optional<String> SeriesInstanceUID = Optional.empty();
    private Optional<String> StudyID = Optional.empty();
    private Optional<Integer> SeriesNumber = Optional.empty();
    private Optional<String> FrameOfReferenceUID = Optional.empty();
    private Optional<String> PositionReferenceIndicator = Optional.empty();
    private Optional<String> StructureSetLabel = Optional.empty();
    private Optional<LocalDate> StructureSetDate = Optional.empty();
    private Optional<LocalTime> StructureSetTime = Optional.empty();
    private Optional<List<ReferencedFrameOfReferenceItem>> referencedFrameOfReferenceSequence = Optional.empty();
    private Optional<List<StructureSetROIItem>> structureSetROISequence = Optional.empty();
    private Optional<List<ROIContourItem>> roiContourSequence = Optional.empty();
    private Optional<List<RTROIObservationsItem>> rtROIObservationsSequence = Optional.empty();
    private Optional<String> approvalStatus = Optional.empty();

    public RTStructureSet(MetaHeader meta) {
        super(meta);
    }
}
