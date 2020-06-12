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
import java.util.Optional;

@Data
@Log4j2
public class CT3d implements DicomImage3D {
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
        if (!Precision.equals(ref.getSliceThickness().get(), slice.getSliceThickness().get(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getKVP().get(), slice.getKVP().get(), Precision.EPSILON)) return false;
        if (!Precision.equals(ref.getDataCollectionDiameter().get(), slice.getDataCollectionDiameter().get(), Precision.EPSILON))
            return false;
        if (!ref.getDeviceSerialNumber().equals(slice.getDeviceSerialNumber())) return false;
        if (!ref.getSoftwareVersions().equals(slice.getSoftwareVersions())) return false;
        if (!ref.getProtocolName().equals(slice.getProtocolName())) return false;
        if (!Precision.equals(ref.getReconstructionDiameter().get(), slice.getReconstructionDiameter().get(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getGantryDetectorTilt().get(), slice.getGantryDetectorTilt().get(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getTableHeight().get(), slice.getTableHeight().get(), Precision.EPSILON))
            return false;
        if (!ref.getRotationDirection().equals(slice.getRotationDirection())) return false;
//        if (ref.getExposureTime() != slice.getExposureTime()) return false;
//        if (ref.getXRayTubeCurrent() != slice.getXRayTubeCurrent()) return false;
//        if (ref.getExposure() != slice.getExposure()) return false;
//        if (ref.getGeneratorPower() != slice.getGeneratorPower()) return false;
        if (ref.getFocalSpots().get().length != 2) return false;
        if (slice.getFocalSpots().get().length != 2) return false;
        for (int i = 0; i < 2; i++)
            if (!Precision.equals(ref.getFocalSpots().get()[i], slice.getFocalSpots().get()[i], Precision.EPSILON))
                return false;
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
        if (!CollectionPrecision.equalsDoubles(ref.getImageOrientationPatient().get(), slice.getImageOrientationPatient().get(), Precision.EPSILON))
            return false;
        if (!ref.getFrameOfReferenceUID().equals(slice.getFrameOfReferenceUID())) return false;
        if (ref.getSamplesPerPixel() != slice.getSamplesPerPixel()) return false;
        if (ref.getPhotometricInterpretation() != slice.getPhotometricInterpretation()) return false;
        if (ref.getRows() != slice.getRows()) return false;
        if (ref.getColumns() != slice.getColumns()) return false;
        if (!CollectionPrecision.equalsDoubles(ref.getPixelSpacing().get(), slice.getPixelSpacing().get(), Precision.EPSILON))
            return false;
        if (ref.getBitsAllocated() != slice.getBitsAllocated()) return false;
        if (ref.getBitsStored() != slice.getBitsStored()) return false;
        if (ref.getHighBit() != slice.getHighBit()) return false;
        if (ref.getPixelRepresentation().isEmpty()) {
            log.error("Existing CT slice is expected to have a pixel representation.");
            return false;
        }
        if (slice.getPixelRepresentation().isEmpty()) {
            log.error("Trying to add a CT slice without a pixel representation.");
            return false;
        }
        if (ref.getPixelRepresentation().get() != slice.getPixelRepresentation().get()) return false;
        if (!Precision.equals(ref.getRescaleIntercept().get(), slice.getRescaleIntercept().get(), Precision.EPSILON))
            return false;
        if (!Precision.equals(ref.getRescaleSlope().get(), slice.getRescaleSlope().get(), Precision.EPSILON))
            return false;
        images.add(slice);
        return true;
    }

    /**
     * Number of CT slices in the 3D volume.
     *
     * @return Number of CT slices
     */
    public int size() {
        return images.size();
    }

    /**
     * True if no slices are present or if the image list is null.
     *
     * @return True if no slices are stored.
     */
    public boolean isEmpty() {
        return (images == null) || images.isEmpty();
    }

    /**
     * Get a CT slice at a given array index.
     *
     * @param index position in the array
     * @return A CT slice at the requested array position, Optional.empty is returned otherwise
     */
    public Optional<CT> get(int index) {
        if (images == null || index < 0 || index >= size()) return Optional.empty();
        return Optional.of(images.get(index));
    }

    /**
     * Get the image orientation patient array if all slices have the same image orientation patient.
     *
     * @return Image orientation patient if all are equal, Optional.empty is returned otherwise.
     */
    public Optional<Double[]> getImageOrientation() {
        int n = size();
        Optional<Double[]> opt_io = Optional.empty();
        for (int i = 0; i < n; i++) {
            var io = images.get(i).getImageOrientationPatient();
            if (io.isEmpty()) {
                log.error("Every image slice requires an image orientation patient array.");
                return Optional.empty();
            }
            if (opt_io.isEmpty()) {
                opt_io = io;
            } else {
                var ref_io = opt_io.get();
                if (ref_io.length != io.get().length) {
                    log.error("Image orientation patient array is not equal between slices.");
                    return Optional.empty();
                }
                for (int j = 0; j < ref_io.length; j++) {
                    if (!Precision.equals(ref_io[j], io.get()[j])) {
                        log.error(String.format("Image orientation is not equal across all slices. [%.10f <-> %.10f]", ref_io[j], io.get()[j]));
                        return Optional.empty();
                    }
                }
            }
        }
        return opt_io;
    }

    /**
     * Get the slice thickness of the CT slices if all slices have the same thickness.
     *
     * @return Uniform slice thickness if all are equal, Optional.empty is returned otherwise.
     */
    public Optional<Double> getUniformSliceThickness() {
        double first = 0.0;
        int n = size();
        final String errMsg = "Unable to obtain uniform slice thickness.";
        if (n == 0) {
            log.error(errMsg);
            return Optional.empty();
        }
        if (images.get(0).getSliceThickness().isEmpty()) {
            log.error("First 2D slice of the 3D scan doesn't have a slice thickness defined");
            return Optional.empty();
        }
        first = images.get(0).getSliceThickness().get();
        for (int i = 1; i < n; i++) {
            if (images.get(i).getSliceThickness().isEmpty()) {
                log.error(String.format("2D slice [index=%d] of the 3D scan doesn't have a slice thickness defined", i));
                return Optional.empty();
            }
            if (!Precision.equals(first, images.get(i).getSliceThickness().get())) {
                log.warn("Not all slices have the same slice thickness.");
                log.error(errMsg);
                return Optional.empty();
            }
        }
        return Optional.of(first);
    }

    @Override
    public Optional<String> getFrameOfReferenceUID() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getFrameOfReferenceUID();
    }

    @Override
    public Optional<Modality> getModality() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getModality();
    }

    @Override
    public Optional<PatientPosition> getPatientPosition() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getPatientPosition();
    }

    @Override
    public Optional<Double[]> getImagePositionPatient() {
        return Optional.empty();
    }

    @Override
    public Optional<Double[]> getImageOrientationPatient() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getImageOrientationPatient();
    }

    @Override
    public Optional<Integer> getRows() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getRows();
    }

    @Override
    public Optional<Integer> getColumns() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getColumns();
    }

    @Override
    public Optional<Double[]> getPixelSpacing() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getPixelSpacing();
    }

    @Override
    public Optional<Integer> getBitsAllocated() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getBitsAllocated();
    }

    @Override
    public Optional<Integer> getBitsStored() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getBitsStored();
    }

    @Override
    public Optional<Integer> getHighBit() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getHighBit();
    }

    @Override
    public Optional<String> getStudyInstanceUID() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getStudyInstanceUID();
    }

    @Override
    public Optional<PixelRepresentation> getPixelRepresentation() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getPixelRepresentation();
    }

    @Override
    public Optional<String> getSeriesInstanceUID() {
        return (images == null || images.isEmpty()) ? Optional.empty() : images.get(0).getSeriesInstanceUID();
    }
}
