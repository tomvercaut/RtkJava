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

import static org.rt.rtkj.utils.OptUtils.equalsIfPresent;
import static org.rt.rtkj.utils.OptUtils.equalsNotEmpty;

@Data
@Log4j2
public class CT3d implements DicomImage3D {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<CT> images = new ArrayList<>();

    private static void logDiffError(String msg) {
        log.error(msg + " differs between slices");
    }

    public boolean add(CT slice) {
        if (slice == null) return false;
        if (images.isEmpty()) {
            images.add(slice);
            return true;
        }

        CT ref = images.get(0);
        if (!equalsIfPresent(ref.getSpecificCharacterSet(), slice.getSpecificCharacterSet())) {
            logDiffError("SpecificCharacterSet");
            return false;
        }
        if (!equalsIfPresent(ref.getImageType(), slice.getImageType())) {
            logDiffError("ImageType");
            return false;
        }
        if (!equalsNotEmpty(ref.getSOPClassUID(), slice.getSOPClassUID())) {
            logDiffError("SOPClassUID");
            return false;
        }
        if (equalsNotEmpty(ref.getSOPInstanceUID(), slice.getSOPInstanceUID())) {
            log.error("No two DICOM files should have an identical SOPInstanceUID");
            return false;
        }
        if (!equalsIfPresent(ref.getAccessionNumber(), slice.getAccessionNumber())) {
            logDiffError("AccessionNumber");
            return false;
        }
        if (!equalsNotEmpty(ref.getModality(), slice.getModality())) {
            logDiffError("Modality");
            return false;
        }
        if (!equalsNotEmpty(ref.getModality(), Optional.of(Modality.CT))) {
            logDiffError("Modality");
            return false;
        }
        if (!equalsIfPresent(ref.getManufacturer(), slice.getManufacturer())) {
            logDiffError("Manufacturer");
            return false;
        }
        if (!equalsIfPresent(ref.getInstitutionName(), slice.getInstitutionName())) {
            logDiffError("InstitutionName");
            return false;
        }
        if (!equalsIfPresent(ref.getReferringPhysicianName(), slice.getReferringPhysicianName())) {
            logDiffError("ReferringPhysicianName");
            return false;
        }
        if (!equalsIfPresent(ref.getStationName(), slice.getStationName())) {
            logDiffError("StationName");
            return false;
        }
        if (!equalsIfPresent(ref.getProcedureCodeSequence(), slice.getProcedureCodeSequence())) {
            logDiffError("ProcedureCodeSequence");
            return false;
        }
        if (!equalsIfPresent(ref.getSeriesDescription(), slice.getSeriesDescription())) {
            logDiffError("SeriesDescription");
            return false;
        }
        if (!equalsIfPresent(ref.getInstitutionalDepartmentName(), slice.getInstitutionalDepartmentName())) {
            logDiffError("InstitutionalDepartmentName");
            return false;
        }
        if (!equalsIfPresent(ref.getManufacturerModelName(), slice.getManufacturerModelName())) {
            logDiffError("ManufacturerModelName");
            return false;
        }
        if (!equalsIfPresent(ref.getReferencedStudySequence(), slice.getReferencedStudySequence())) {
            logDiffError("ReferencedStudySequence");
            return false;
        }
        if (!equalsNotEmpty(ref.getPatientName(), slice.getPatientName())) {
            logDiffError("PatientName");
            return false;
        }
        if (!equalsNotEmpty(ref.getPatientID(), slice.getPatientID())) {
            logDiffError("PatientID");
            return false;
        }
        if (!equalsIfPresent(ref.getPatientBirthDate(), slice.getPatientBirthDate())) {
            logDiffError("PatientBirthDate");
            return false;
        }
        if (!equalsIfPresent(ref.getPatientSex(), slice.getPatientSex())) {
            logDiffError("PatientSex");
            return false;
        }
        if (!equalsIfPresent(ref.getPatientAge(), slice.getPatientAge())) {
            logDiffError("PatientAge");
            return false;
        }
        if (!equalsIfPresent(ref.getPatientIdentityRemoved(), slice.getPatientIdentityRemoved())) {
            logDiffError("PatientIdentityRemoved");
            return false;
        }
        if (!equalsIfPresent(ref.getDeidentificationMethod(), slice.getDeidentificationMethod())) {
            logDiffError("DeidentificationMethod");
            return false;
        }
        if (!equalsIfPresent(ref.getBodyPartExamined(), slice.getBodyPartExamined())) {
            logDiffError("BodyPartExamined");
            return false;
        }
        if (!equalsIfPresent(ref.getScanOptions(), slice.getScanOptions())) {
            logDiffError("ScanOptions");
            {
                logDiffError("ScanOptions");
                return false;
            }
        }
        if (ref.getSliceThickness().isEmpty() || slice.getSliceThickness().isEmpty() ||
                !Precision.equals(ref.getSliceThickness().get(), slice.getSliceThickness().get(), Precision.EPSILON)) {
            logDiffError("SliceThickness");
            return false;
        }
        if (ref.getKVP().isEmpty() || slice.getKVP().isEmpty() ||
                !Precision.equals(ref.getKVP().get(), slice.getKVP().get(), Precision.EPSILON)) {
            logDiffError("KVP");
            return false;
        }
        if (ref.getDataCollectionDiameter().isEmpty() || slice.getDataCollectionDiameter().isEmpty() ||
                !Precision.equals(ref.getDataCollectionDiameter().get(), slice.getDataCollectionDiameter().get(), Precision.EPSILON)) {
            logDiffError("DataCollectionDiameter");
            return false;
        }
        if (!equalsIfPresent(ref.getDeviceSerialNumber(), slice.getDeviceSerialNumber())) {
            logDiffError("DeviceSerialNumber");
            return false;
        }
        if (!equalsIfPresent(ref.getSoftwareVersions(), slice.getSoftwareVersions())) {
            logDiffError("SoftwareVersions");
            return false;
        }
        if (!equalsIfPresent(ref.getProtocolName(), slice.getProtocolName())) {
            logDiffError("ProtocolName");
            return false;
        }
        if (ref.getReconstructionDiameter().isEmpty() || slice.getReconstructionDiameter().isEmpty() ||
                !Precision.equals(ref.getReconstructionDiameter().get(), slice.getReconstructionDiameter().get(), Precision.EPSILON)) {
            logDiffError("ReconstructionDiameter");
            return false;
        }
        if (ref.getGantryDetectorTilt().isEmpty() || slice.getGantryDetectorTilt().isEmpty() ||
                !Precision.equals(ref.getGantryDetectorTilt().get(), slice.getGantryDetectorTilt().get(), Precision.EPSILON)) {
            logDiffError("GantryDetectorTilt");
            return false;
        }
        if (ref.getTableHeight().isEmpty() || slice.getTableHeight().isEmpty() ||
                !Precision.equals(ref.getTableHeight().get(), slice.getTableHeight().get(), Precision.EPSILON)) {
            logDiffError("TableHeight");
            return false;
        }
        if (!equalsIfPresent(ref.getRotationDirection(), slice.getRotationDirection())) {
            logDiffError("RotationDirection");
            return false;
        }
//        if (ref.getExposureTime() != slice.getExposureTime()) return false;
//        if (ref.getXRayTubeCurrent() != slice.getXRayTubeCurrent()) return false;
//        if (ref.getExposure() != slice.getExposure()) return false;
//        if (ref.getGeneratorPower() != slice.getGeneratorPower()) return false;
        if (ref.getFocalSpots().isEmpty() || ref.getFocalSpots().get().length != 2) {
            log.error("CT slice is missing a FocalSpot");
            return false;
        }
        if (slice.getFocalSpots().isEmpty() || slice.getFocalSpots().get().length != 2) {
            log.error("CT slice is missing a FocalSpot");
            return false;
        }
        for (int i = 0; i < 2; i++)
            if (!Precision.equals(ref.getFocalSpots().get()[i], slice.getFocalSpots().get()[i], Precision.EPSILON)) {
                logDiffError("FocalSpots");
                return false;
            }
        if (!equalsIfPresent(ref.getConvolutionKernel(), slice.getConvolutionKernel())) {
            logDiffError("ConvolutionKernel");
            return false;
        }
        if (!equalsNotEmpty(ref.getPatientPosition(), slice.getPatientPosition())) {
            logDiffError("PatientPosition");
            return false;
        }
        if (!equalsIfPresent(ref.getExposureModulationType(), slice.getExposureModulationType())) {
            logDiffError("ExposureModulationType");
            return false;
        }
        if (!equalsNotEmpty(ref.getStudyInstanceUID(), slice.getStudyInstanceUID())) {
            logDiffError("StudyInstanceUID");
            return false;
        }
        if (!equalsNotEmpty(ref.getSeriesInstanceUID(), slice.getSeriesInstanceUID())) {
            logDiffError("SeriesInstanceUID");
            return false;
        }
        if (!equalsIfPresent(ref.getStudyID(), slice.getStudyID())) {
            logDiffError("StudyID");
            return false;
        }
        if (!equalsIfPresent(ref.getSeriesNumber(), slice.getSeriesNumber())) {
            logDiffError("SeriesNumber");
            return false;
        }
        if (!equalsIfPresent(ref.getAcquisitionNumber(), slice.getAcquisitionNumber())) {
            logDiffError("AcquisitionNumber");
            return false;
        }
//        if (ref.getInstanceNumber() != slice.getInstanceNumber()) return false; // Called Image Number in the previous version of the standard
//        if (!CollectionPrecision.equalsDoubles(ref.getImagePositionPatient(), slice.getImagePositionPatient(), Precision.EPSILON))
//            return false;
        if (ref.getImageOrientationPatient().isEmpty() || slice.getImageOrientationPatient().isEmpty() ||
                !CollectionPrecision.equalsDoubles(ref.getImageOrientationPatient().get(), slice.getImageOrientationPatient().get(), Precision.EPSILON)) {
            logDiffError("ImageOrientationPatient");
            return false;
        }
        if (!equalsIfPresent(ref.getFrameOfReferenceUID(), slice.getFrameOfReferenceUID())) {
            logDiffError("FrameOfReferenceUID");
            return false;
        }
        if (!equalsNotEmpty(ref.getSamplesPerPixel(), slice.getSamplesPerPixel())) {
            logDiffError("SamplesPerPixel");
            return false;
        }
        if (!equalsNotEmpty(ref.getPhotometricInterpretation(), slice.getPhotometricInterpretation())) {
            logDiffError("PhotometricInterpretation");
            return false;
        }
        if (!equalsNotEmpty(ref.getRows(), slice.getRows())) {
            logDiffError("Rows");
            return false;
        }
        if (!equalsNotEmpty(ref.getColumns(), slice.getColumns())) {
            logDiffError("Columns");
            return false;
        }
        if (ref.getPixelSpacing().isEmpty() || slice.getPixelSpacing().isEmpty() ||
                !CollectionPrecision.equalsDoubles(ref.getPixelSpacing().get(), slice.getPixelSpacing().get(), Precision.EPSILON)) {
            logDiffError("PixelSpacing");
            return false;
        }
        if (!equalsNotEmpty(ref.getBitsAllocated(), slice.getBitsAllocated())) {
            logDiffError("BitsAllocated");
            return false;
        }
        if (!equalsNotEmpty(ref.getBitsStored(), slice.getBitsStored())) {
            logDiffError("BitsStored");
            return false;
        }
        if (!equalsNotEmpty(ref.getHighBit(), slice.getHighBit())) {
            logDiffError("HighBit");
            return false;
        }
        if (ref.getPixelRepresentation().isEmpty()) {
            log.error("Existing CT slice is expected to have a pixel representation.");
            return false;
        }
        if (slice.getPixelRepresentation().isEmpty()) {
            log.error("Trying to add a CT slice without a pixel representation.");
            return false;
        }
        if (!equalsNotEmpty(ref.getPixelRepresentation(), slice.getPixelRepresentation())) return false;
        if (ref.getRescaleIntercept().isEmpty() || slice.getRescaleIntercept().isEmpty() || !Precision.equals(ref.getRescaleIntercept().get(), slice.getRescaleIntercept().get(), Precision.EPSILON)) {
            logDiffError("RescaleIntercept");
            return false;
        }
        if (ref.getRescaleSlope().isEmpty() || slice.getRescaleSlope().isEmpty() || !Precision.equals(ref.getRescaleSlope().get(), slice.getRescaleSlope().get(), Precision.EPSILON)) {
            logDiffError("RescaleSlope");
            return false;
        }
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
