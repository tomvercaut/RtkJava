package org.rt.rtkj.dicom;

import lombok.Data;

import static org.rt.rtkj.utils.CollectionPrecision.equalsDoubles;

/**
 * Compare RTDose instances while taking into account additonal flags.
 */
@Data
public class RTDoseComparison {
    /**
     * Ignore the date and time flags in the RTDose instance
     */
    private boolean ignoreDateTime;
    /**
     * Ignore the meta header in the RTDose instance
     */
    private boolean ignoreMetaHeader;

    public RTDoseComparison() {
        ignoreDateTime = true;
        ignoreMetaHeader = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RTDoseComparison)) return false;

        RTDoseComparison that = (RTDoseComparison) o;

        if (ignoreDateTime != that.ignoreDateTime) return false;
        return ignoreMetaHeader == that.ignoreMetaHeader;
    }

    @Override
    public int hashCode() {
        int result = (ignoreDateTime ? 1 : 0);
        result = 31 * result + (ignoreMetaHeader ? 1 : 0);
        return result;
    }

    public boolean equals(RTDose d1, RTDose d2) {
        if (!ignoreMetaHeader) {
            var m1 = (MetaHeader) d1;
            var m2 = (MetaHeader) d2;
            if (!m1.equals(m2)) return false;
        }
        if (!d1.getSpecificCharacterSet().equals(d2.getSpecificCharacterSet())) return false;
        if (!ignoreDateTime) {
            if (!d1.getInstanceCreationDate().equals(d2.getInstanceCreationDate())) return false;
            if (!d1.getInstanceCreationTime().equals(d2.getInstanceCreationTime())) return false;
        }
        if (!d1.getSopClassUID().equals(d2.getSopClassUID())) return false;
        if (!d1.getSopInstanceUID().equals(d2.getSopInstanceUID())) return false;
        if (!ignoreDateTime) {
            if (!d1.getStudyDate().equals(d2.getStudyDate())) return false;
            if (!d1.getStudyTime().equals(d2.getStudyTime())) return false;
        }
        if (!d1.getAccessionNumber().equals(d2.getAccessionNumber())) return false;
        if (!d1.getModality().equals(d2.getModality())) return false;
        if (!d1.getManufacturer().equals(d2.getManufacturer())) return false;
        if (!d1.getReferringPhysicianName().equals(d2.getReferringPhysicianName())) return false;
        if (!d1.getStationName().equals(d2.getStationName())) return false;
        if (!d1.getSeriesDescription().equals(d2.getSeriesDescription())) return false;
        if (!d1.getManufacturerModelName().equals(d2.getManufacturerModelName())) return false;
        if (!d1.getPatientName().equals(d2.getPatientName())) return false;
        if (!d1.getPatientID().equals(d2.getPatientID())) return false;
        if (!ignoreDateTime) if (!d1.getPatientBirthDate().equals(d2.getPatientBirthDate())) return false;
        if (!d1.getPatientSex().equals(d2.getPatientSex())) return false;
        if (!d1.getSliceThicknes().equals(d2.getSliceThicknes())) return false;
        if (!d1.getDeviceSerialNumber().equals(d2.getDeviceSerialNumber())) return false;
        if (!d1.getSoftwareVersions().equals(d2.getSoftwareVersions())) return false;
        if (!d1.getStudyInstanceUID().equals(d2.getStudyInstanceUID())) return false;
        if (!d1.getSeriesInstanceUID().equals(d2.getSeriesInstanceUID())) return false;
        if (!d1.getStudyID().equals(d2.getStudyID())) return false;
        if (!d1.getSeriesNumber().equals(d2.getSeriesNumber())) return false;
        if (!d1.getInstanceNumber().equals(d2.getInstanceNumber())) return false;
        if (!equalsDoubles(d1.getImagePositionPatient(), d2.getImagePositionPatient())) return false;
        if (!equalsDoubles(d1.getImageOrientationPatient(), d2.getImageOrientationPatient()))
            return false;
        if (!d1.getFrameOfReferenceUID().equals(d2.getFrameOfReferenceUID())) return false;
        if (!d1.getPositionReferenceIndicator().equals(d2.getPositionReferenceIndicator())) return false;
        if (!d1.getSamplesPerPixel().equals(d2.getSamplesPerPixel())) return false;
        if (!d1.getPhotometricInterpretation().equals(d2.getPhotometricInterpretation())) return false;
        if (!d1.getNumberOfFrames().equals(d2.getNumberOfFrames())) return false;
        if (!d1.getFrameIncrementPointer().equals(d2.getFrameIncrementPointer())) return false;
        if (!d1.getRows().equals(d2.getRows())) return false;
        if (!d1.getColumns().equals(d2.getColumns())) return false;
        if (!equalsDoubles(d1.getPixelSpacing(), d2.getPixelSpacing())) return false;
        if (!d1.getBitsAllocated().equals(d2.getBitsAllocated())) return false;
        if (!d1.getBitsStored().equals(d2.getBitsStored())) return false;
        if (!d1.getHighBit().equals(d2.getHighBit())) return false;
        if (!d1.getPixelRepresentation().equals(d2.getPixelRepresentation())) return false;
        if (!d1.getDoseUnits().equals(d2.getDoseUnits())) return false;
        if (!d1.getDoseType().equals(d2.getDoseType())) return false;
        if (!equalsDoubles(d1.getGridFrameOffsetVector(), d2.getGridFrameOffsetVector())) return false;
        if (!d1.getDoseGridScaling().equals(d2.getDoseGridScaling())) return false;
        if (!d1.getTissueHeterogeneityCorrection().equals(d2.getTissueHeterogeneityCorrection()))
            return false;
        if (d1.getDvhSequence().isPresent() != d2.getDvhSequence().isPresent()) return false;
        if (d1.getDvhSequence().isPresent()) {

            if (d1.getDvhSequence().get().size() != d2.getDvhSequence().get().size()) return false;
            var ndvh = d1.getDvhSequence().get().size();
            for (int i = 0; i < ndvh; i++) {
                var inputDvh = d1.getDvhSequence().get().get(i);
                var checkDvh = d2.getDvhSequence().get().get(i);
                if (!inputDvh.getDvhType().equals(checkDvh.getDvhType())) return false;
                if (!inputDvh.getDoseUnits().equals(checkDvh.getDoseUnits())) return false;
                if (!inputDvh.getDvhDoseScaling().equals(checkDvh.getDvhDoseScaling())) return false;
                if (!inputDvh.getDvhVolumeUnits().equals(checkDvh.getDvhVolumeUnits())) return false;
                if (!inputDvh.getDvhNumberOfBins().equals(checkDvh.getDvhNumberOfBins())) return false;
                if (!equalsDoubles(inputDvh.getDvhData(), checkDvh.getDvhData())) return false;
                if (!inputDvh.getDvhReferencedROISequence().equals(checkDvh.getDvhReferencedROISequence()))
                    return false;
                if (!inputDvh.getDvhMinimumDose().equals(checkDvh.getDvhMinimumDose())) return false;
                if (!inputDvh.getDvhMaximumDose().equals(checkDvh.getDvhMaximumDose())) return false;
                if (!inputDvh.getDvhMeanDose().equals(checkDvh.getDvhMeanDose())) return false;
            }
        }
        var inputReferencedRTPlanSequence = d1.getReferencedRTPlanSequence();
        var checkReferencedRTPlanSequence = d2.getReferencedRTPlanSequence();
        if (!inputReferencedRTPlanSequence.equals(checkReferencedRTPlanSequence)) return false;
        if (!d1.getReferencedStructureSetSequence().equals(d2.getReferencedStructureSetSequence()))
            return false;
        if (!d1.getPixelData().equals(d2.getPixelData())) return false;
        return true;
    }
}
