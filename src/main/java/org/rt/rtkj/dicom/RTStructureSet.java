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
public class RTStructureSet extends MetaHeader {
    private Option<String> SpecificCharacterSet = Option.empty();
    private Option<LocalDate> InstanceCreationDate = Option.empty();
    private Option<LocalTime> InstanceCreationTime = Option.empty();
    private Option<String> SOPClassUID = Option.empty();
    private Option<String> SOPInstanceUID = Option.empty();
    private Option<LocalDate> StudyDate = Option.empty();
    private Option<LocalTime> StudyTime = Option.empty();
    private Option<String> AccessionNumber = Option.empty();
    private Option<Modality> Modality = Option.empty();
    private Option<String> Manufacturer = Option.empty();
    private Option<String> ReferringPhysicianName = Option.empty();
    private Option<String> SeriesDescription = Option.empty();
    private Option<String> OperatorsName = Option.empty();
    private Option<String> ManufacturerModelName = Option.empty();
    private Option<String> PatientName = Option.empty();
    private Option<String> PatientID = Option.empty();
    private Option<LocalDate> PatientBirthDate = Option.empty();
    private Option<String> PatientSex = Option.empty();
    private Option<String> SoftwareVersions = Option.empty();
    private Option<String> StudyInstanceUID = Option.empty();
    private Option<String> SeriesInstanceUID = Option.empty();
    private Option<String> StudyID = Option.empty();
    private Option<Integer> SeriesNumber = Option.empty();
    private Option<String> FrameOfReferenceUID = Option.empty();
    private Option<String> PositionReferenceIndicator = Option.empty();
    private Option<String> StructureSetLabel = Option.empty();
    private Option<LocalDate> StructureSetDate = Option.empty();
    private Option<LocalTime> StructureSetTime = Option.empty();
    private Option<List<ReferencedFrameOfReferenceItem>> referencedFrameOfReferenceSequence = Option.empty();
    private Option<List<StructureSetROIItem>> structureSetROISequence = Option.empty();
    private Option<List<ROIContourItem>> roiContourSequence = Option.empty();
    private Option<List<RTROIObservationsItem>> rtROIObservationsSequence = Option.empty();
    private Option<String> approvalStatus = Option.empty();

    public RTStructureSet(MetaHeader meta) {
        super(meta);
    }

    /**
     * Clear the meta header
     */
    public void clearMetaHeader() {
        super.clear();
    }
}
