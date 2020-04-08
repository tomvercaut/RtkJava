package org.rt.rtkj.dicom;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.util.Precision;
import org.rt.rtkj.utils.CollectionPrecision;

import java.util.ArrayList;
import java.util.List;

@Data
@Log4j2
public class CT3d {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<CT> images = new ArrayList<>();

    public boolean add(CT slice) {
        if (slice == null) return false;
        if (images.isEmpty()) {
            images.add(slice);
            return true;
        }

        CT ref = images.get(0);
        if (!ref.getSpecificCharacterSet().equals(slice.getSpecificCharacterSet())) return false;
        if (!ref.getImageType().equals(slice.getImageType())) return false;
        if (!ref.getSOPClassUID().equals(slice.getSOPClassUID())) return false;
        if (ref.getSOPInstanceUID().equals(slice.getSOPInstanceUID())) {
            log.error("No two DICOM files should have an identical SOPInstanceUID");
            return false;
        }
        if (!ref.getAccessionNumber().equals(slice.getAccessionNumber())) return false;
        if (!ref.getModality().equals(slice.getModality())) return false;
        if (!ref.getModality().equals(Modality.CT)) return false;
        if (!ref.getManufacturer().equals(slice.getManufacturer())) return false;
        if (!ref.getInstitutionName().equals(slice.getInstitutionName())) return false;
        if (!ref.getReferringPhysicianName().equals(slice.getReferringPhysicianName())) return false;
        if (!ref.getStationName().equals(slice.getStationName())) return false;
        if (!ref.getProcedureCodeSequence().equals(slice.getProcedureCodeSequence())) return false;
        if (!ref.getSeriesDescription().equals(slice.getSeriesDescription())) return false;
        if (!ref.getInstitutionalDepartmentName().equals(slice.getInstitutionalDepartmentName())) return false;
        if (!ref.getManufacturerModelName().equals(slice.getManufacturerModelName())) return false;
        if (!ref.getReferencedStudySequence().equals(slice.getReferencedStudySequence())) return false;
        if (!ref.getPatientName().equals(slice.getPatientName())) return false;
        if (!ref.getPatientID().equals(slice.getPatientID())) return false;
        if (!ref.getPatientBirthDate().equals(slice.getPatientBirthDate())) return false;
        if (!ref.getPatientSex().equals(slice.getPatientSex())) return false;
        if (!ref.getPatientAge().equals(slice.getPatientAge())) return false;
        if (!ref.getPatientIdentityRemoved().equals(slice.getPatientIdentityRemoved())) return false;
        if (!ref.getDeidentificationMethod().equals(slice.getDeidentificationMethod())) return false;
        if (!ref.getBodyPartExamined().equals(slice.getBodyPartExamined())) return false;
        if (!ref.getScanOptions().equals(slice.getScanOptions())) return false;
        if (!Precision.equals(ref.getSliceThickness(), slice.getSliceThickness(), Precision.EPSILON)) return false;
        if (!Precision.equals(ref.getKVP(), slice.getKVP(), Precision.EPSILON)) return false;
        if (!Precision.equals(ref.getDataCollectionDiameter(), slice.getDataCollectionDiameter(), Precision.EPSILON))
            return false;
        if (!ref.getDeviceSerialNumber().equals(slice.getDeviceSerialNumber())) return false;
        if (!ref.getSoftwareVersions().equals(slice.getSoftwareVersions())) return false;
        if (!ref.getProtocolName().equals(slice.getProtocolName())) return false;
        if (!Precision.equals(ref.getReconstructionDiameter(), slice.getReconstructionDiameter(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getGantryDetectorTilt(), slice.getGantryDetectorTilt(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getTableHeight(), slice.getTableHeight(), Precision.EPSILON)) return false;
        if (!ref.getRotationDirection().equals(slice.getRotationDirection())) return false;
//        if (ref.getExposureTime() != slice.getExposureTime()) return false;
//        if (ref.getXRayTubeCurrent() != slice.getXRayTubeCurrent()) return false;
//        if (ref.getExposure() != slice.getExposure()) return false;
//        if (ref.getGeneratorPower() != slice.getGeneratorPower()) return false;
        if (ref.getFocalSpots().length != 2) return false;
        if (slice.getFocalSpots().length != 2) return false;
        for (int i = 0; i < 2; i++)
            if (!Precision.equals(ref.getFocalSpots()[i], slice.getFocalSpots()[i], Precision.EPSILON)) return false;
        if (!ref.getConvolutionKernel().equals(slice.getConvolutionKernel())) return false;
        if (!ref.getPatientPosition().equals(slice.getPatientPosition())) return false;
        if (!ref.getExposureModulationType().equals(slice.getExposureModulationType())) return false;
        if (!ref.getStudyInstanceUID().equals(slice.getStudyInstanceUID())) return false;
        if (!ref.getSeriesInstanceUID().equals(slice.getSeriesInstanceUID())) return false;
        if (!ref.getStudyID().equals(slice.getStudyID())) return false;
        if (ref.getSeriesNumber() != slice.getSeriesNumber()) return false;
        if (ref.getAcquisitionNumber() != slice.getAcquisitionNumber()) return false;
//        if (ref.getInstanceNumber() != slice.getInstanceNumber()) return false; // Called Image Number in the previous version of the standard
//        if (!CollectionPrecision.equalsDoubles(ref.getImagePositionPatient(), slice.getImagePositionPatient(), Precision.EPSILON))
//            return false;
        if (!CollectionPrecision.equalsDoubles(ref.getImageOrientationPatient(), slice.getImageOrientationPatient(), Precision.EPSILON))
            return false;
        if (!ref.getFrameOfReferenceUID().equals(slice.getFrameOfReferenceUID())) return false;
        if (ref.getSamplesPerPixel() != slice.getSamplesPerPixel()) return false;
        if (ref.getPhotometricInterpretation() != slice.getPhotometricInterpretation()) return false;
        if (ref.getRows() != slice.getRows()) return false;
        if (ref.getColumns() != slice.getColumns()) return false;
        if (!CollectionPrecision.equalsDoubles(ref.getPixelSpacing(), slice.getPixelSpacing(), Precision.EPSILON))
            return false;
        if (ref.getBitsAllocated() != slice.getBitsAllocated()) return false;
        if (ref.getBitsStored() != slice.getBitsStored()) return false;
        if (ref.getHighBit() != slice.getHighBit()) return false;
        if (ref.getPixelRepresentation() != slice.getPixelRepresentation()) return false;
        if (!Precision.equals(ref.getRescaleIntercept(), slice.getRescaleIntercept(), Precision.EPSILON)) return false;
        if (!Precision.equals(ref.getRescaleSlope(), slice.getRescaleSlope(), Precision.EPSILON)) return false;
        images.add(slice);
        return true;
    }

    public int size() {
        return images.size();
    }
}
