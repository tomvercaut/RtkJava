package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.util.ByteUtils;
import org.rt.rtkj.Option;
import org.rt.rtkj.utils.ByteTools;

import java.io.IOException;
import java.nio.ByteOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Log4j2
public class Reader {

    /**
     * Read a sequence from attributes by the corresponding DICOM tag.
     *
     * @param attr     attributes containing the sequence.
     * @param seqTag   DICOM tag of the sequence
     * @param function a function interface that has an sequence item attributes as input and returns an Option value T.
     * @param <T>      type representing the sequence item
     * @return If the sequence is found and contains values a list of sequence items is returned. If the sequence is not found or the attribute is null, an empty optional is returned.
     */
    private static <T> Option<List<T>> readSequence(Attributes attr, int seqTag, Function<Attributes, Option<T>> function) {
        if (attr == null || !attr.contains(seqTag)) return Option.empty();
        Sequence seq = attr.getSequence(seqTag);
        Option<List<T>> optList = Option.empty();
        for (Attributes value : seq) {
            var optTmp = function.apply(value);
            if (optTmp.isPresent()) {
                var tmp = optTmp.get();
                if (optList.isEmpty()) optList = Option.of(new ArrayList<>());
                optList.get().add(tmp);
            }
        }
        return optList;
    }

    /**
     * Read an optional String from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<String> readString(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        return Option.ofNullable(attr.getString(tag));
    }

    /**
     * Read an optional List of Strings from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty Option is returned.
     */
    private static Option<List<String>> readListString(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var optS = readString(attr, tag);
        if (optS.isEmpty() || optS.get().isBlank()) return Option.empty();
        var s = optS.get();
        var arrayS = s.split("\\\\");
        if (arrayS.length == 0) return Option.empty();
        return Option.of(List.of(arrayS));
    }

    /**
     * Read an Option integer from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Integer> readInt(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var val = attr.getInt(tag, DicomUtils.UNDEFINED_I32);
        if (val == DicomUtils.UNDEFINED_I32) return Option.empty();
        return Option.of(val);
    }

    /**
     * Read an optional double from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Double> readDouble(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var val = attr.getDouble(tag, DicomUtils.UNDEFINED_DOUBLE);
        if (val == DicomUtils.UNDEFINED_DOUBLE) return Option.empty();
        return Option.of(val);
    }

    /**
     * Read an optional float from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Float> readFloat(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var val = attr.getFloat(tag, DicomUtils.UNDEFINED_FLOAT);
        if (val == DicomUtils.UNDEFINED_FLOAT) return Option.empty();
        return Option.of(val);
    }

    /**
     * Read an optional array of doubles from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Double[]> readDoubles(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var val = attr.getDoubles(tag);
        if (val == null) return Option.empty();
        return Option.of(Arrays.stream(val).boxed().toArray(Double[]::new));
    }

    /**
     * Read an optional list of doubles from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<List<Double>> readListDoubles(Attributes attr, int tag) {
        var optArray = readDoubles(attr, tag);
        if (optArray.isEmpty()) return Option.empty();
        return Option.of(Arrays.asList(optArray.get()));
    }

    /**
     * Read an optional array of integers from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Integer[]> readInts(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        var val = attr.getInts(tag);
        if (val == null) return Option.empty();
        return Option.of(Arrays.stream(val).boxed().toArray(Integer[]::new));
    }

    /**
     * Read an optional Date from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<LocalDate> readDate(Attributes attr, int tag) {
        var optString = readString(attr, tag);
        return DicomUtils.getLocalDateFromString(optString);
    }

    /**
     * Read an optional array of bytes from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Option<Byte[]> readBytes(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Option.empty();
        try {
            var bytes = attr.getBytes(tag);
            if (bytes == null) return Option.empty();
            var n = bytes.length;
            Byte[] bb = new Byte[n];
            for (int i = 0; i < n; i++) {
                bb[i] = bytes[i];
            }
            return Option.of(bb);
        } catch (IOException e) {
            log.error(e);
        }
        return Option.empty();
    }

    private static Option<CodeItem> codeItem(Attributes attr) {
        if (attr == null) return Option.empty();
        CodeItem item = new CodeItem();
        item.setCodeValue(readString(attr, Tag.CodeValue));
        item.setCodingSchemeDesignator(readString(attr, Tag.CodingSchemeDesignator));
        item.setCodeMeaning(readString(attr, Tag.CodeMeaning));
        item.setMappingResource(readString(attr, Tag.MappingResource));
        item.setContextGroupVersion(Option.ofNullable(DicomUtils.getLocalDateTime(attr.getString(Tag.ContextGroupVersion))));
        item.setContextIdentifier(readString(attr, Tag.ContextIdentifier));
        return Option.of(item);
    }

    private static Option<ReferencedSOPClassInstanceItem> referencedSOPClassInstance(Attributes attr) {
        if (attr == null) return Option.empty();
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        item.setReferencedSOPClassUID(readString(attr, Tag.ReferencedSOPClassUID));
        item.setReferencedSOPInstanceUID(readString(attr, Tag.ReferencedSOPInstanceUID));
        return Option.of(item);
    }

    private static Option<DVHReferencedROIItem> dvhReferencedROI(Attributes attributes) {
        if (attributes == null) return Option.empty();
        DVHReferencedROIItem item = new DVHReferencedROIItem();
        item.setDvhROIContributionType(Option.ofNullable(attributes.getString(Tag.DVHROIContributionType)));
        if (attributes.containsValue(Tag.ReferencedROINumber))
            item.setReferencedROINumber(readInt(attributes, Tag.ReferencedROINumber));
        return Option.of(item);
    }

    private static Option<EnergyWindowRangeItem> energyWindowRange(Attributes attr) {
        if (attr == null) return Option.empty();
        EnergyWindowRangeItem item = new EnergyWindowRangeItem();
        item.setEnergyWindowLowerLimit(readDouble(attr, Tag.EnergyWindowLowerLimit));
        item.setEnergyWindowUpperLimit(readDouble(attr, Tag.EnergyWindowUpperLimit));
        return Option.of(item);
    }

    private static Option<PatientOrientationCodeItem> patientOrientationCode(Attributes attr) {
        if (attr == null) return Option.empty();
        Option<CodeItem> tmpCodeItem = codeItem(attr);
        if (tmpCodeItem.isEmpty()) return Option.empty();
        PatientOrientationCodeItem item = (PatientOrientationCodeItem) tmpCodeItem.get();
        item.setPatientOrientationModifierCodeSequence(readSequence(attr, Tag.PatientOrientationModifierCodeSequence, Reader::codeItem));
        return Option.of(item);
    }

    private static Option<ReducedCodeItem> reducedCode(Attributes attr) {
        if (attr == null) return Option.empty();
        ReducedCodeItem item = new ReducedCodeItem();
        item.setCodeValue(readString(attr, Tag.CodeValue));
        item.setCodingSchemeDesignator(readString(attr, Tag.CodingSchemeDesignator));
        item.setCodeMeaning(readString(attr, Tag.CodeMeaning));
        return Option.of(item);
    }

    private static Option<RequestAttributesItem> requestAttributes(Attributes attr) {
        if (attr == null) return Option.empty();
        RequestAttributesItem item = new RequestAttributesItem();
        item.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        item.setReferencedStudySequence(readSequence(attr, Tag.ReferencedStudySequence, Reader::referencedSOPClassInstance));
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        item.setRequestedProcedureDescription(readString(attr, Tag.RequestedProcedureDescription));
        item.setRequestedProcedureCodeSequence(readSequence(attr, Tag.RequestedProcedureCodeSequence, Reader::reducedCode));
        item.setScheduledProcedureStepID(readString(attr, Tag.ScheduledProcedureStepID));
        item.setRequestedProcedureID(readString(attr, Tag.RequestedProcedureID));
        return Option.of(item);
    }

    private static Option<ReferencedPatientItem> referencedPatient(Attributes attr) {
        if (attr == null) return Option.empty();
        ReferencedPatientItem item = new ReferencedPatientItem();
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        item.setPurposeOfReferenceCodeSequence(readSequence(attr, Tag.PurposeOfReferenceCodeSequence, Reader::codeItem));
        return Option.of(item);
    }

    private static Option<RadiopharmaceuticalInformationItem> radiopharmaceuticalInformation(Attributes attr) {
        if (attr == null) return Option.empty();
        RadiopharmaceuticalInformationItem item = new RadiopharmaceuticalInformationItem();
        item.setRadiopharmaceutical(readString(attr, Tag.Radiopharmaceutical));
        item.setRadiopharmaceuticalStartTime(DicomUtils.tmToLocalTime(readString(attr, Tag.RadiopharmaceuticalStartTime)));
        if (attr.containsValue(Tag.RadionuclideTotalDose))
            item.setRadionuclideTotalDose(readDouble(attr, Tag.RadionuclideTotalDose));
        if (attr.containsValue(Tag.RadionuclideHalfLife))
            item.setRadionuclideHalfLife(readDouble(attr, Tag.RadionuclideHalfLife));
        if (attr.containsValue(Tag.RadionuclidePositronFraction))
            item.setRadionuclidePositronFraction(readDouble(attr, Tag.RadionuclidePositronFraction));
        if (attr.containsValue(Tag.RadiopharmaceuticalStartDateTime))
            item.setRadiopharmaceuticalStartDateTime(DicomUtils.getLocalDateTime(readString(attr, Tag.RadiopharmaceuticalStartDateTime)));
        if (attr.contains(Tag.RadionuclideCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.RadionuclideCodeSequence);
            for (Attributes value : seq) {
                var optTmp = codeItem(value);
                optTmp.ifPresent(tmp -> {
                    if (item.getRadionuclideCodeSequence().isEmpty())
                        item.setRadionuclideCodeSequence(Option.of(new ArrayList<>()));
                    item.getRadionuclideCodeSequence().get().add(tmp);
                });
            }
        }
        return Option.of(item);
    }

    private static Option<StudiesContainingOtherReferencedInstancesItem> studiesContainingOtherReferencedInstances(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new StudiesContainingOtherReferencedInstancesItem();
        if (attr.contains(Tag.ReferencedSeriesSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedSeriesSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSeries(value);
                optTmp.ifPresent(tmp -> {
                    if (item.getReferencedSeriesSequence().isEmpty())
                        item.setReferencedSeriesSequence(Option.of(new ArrayList<>()));
                    item.getReferencedSeriesSequence().get().add(tmp);
                });
            }
        }
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        return Option.of(item);
    }

    private static Option<ReferencedSeriesItem> referencedSeries(Attributes attr) {
        if (attr == null) return Option.empty();
        ReferencedSeriesItem item = new ReferencedSeriesItem();
        item.setReferencedInstanceSequence(readSequence(attr, Tag.ReferencedInstanceSequence, Reader::referencedSOPClassInstance));
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        return Option.of(item);
    }

    private static Option<RegistrationItem> registration(Attributes attr) {
        if (attr == null) return Option.empty();
        RegistrationItem item = new RegistrationItem();
        item.setReferencedImageSequence(readSequence(attr, Tag.ReferencedImageSequence, Reader::referencedSOPClassInstance));
        item.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        item.setMatrixRegistrationSequence(readSequence(attr, Tag.MatrixRegistrationSequence, Reader::matrixRegistration));
        return Option.of(item);
    }

    private static Option<MatrixRegistrationItem> matrixRegistration(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new MatrixRegistrationItem();
        item.setMatrixSequence(readSequence(attr, Tag.MatrixSequence, Reader::matrix));
        return Option.of(item);
    }

    private static Option<TransformationMatrixType> frameOfReferenceTransformationMatrixType(Attributes attr) {
        if (attr == null) return Option.empty();
        if (attr.containsValue(Tag.FrameOfReferenceTransformationMatrixType)) {
            var s = attr.getString(Tag.FrameOfReferenceTransformationMatrixType);
            switch (s) {
                case "RIGID":
                    return Option.of(TransformationMatrixType.RIGID);
                case "RIGID_SCALE":
                    return Option.of(TransformationMatrixType.RIGID_SCALE);
                case "AFFINE":
                    return Option.of(TransformationMatrixType.AFFINE);
                default:
                    return Option.of(TransformationMatrixType.NONE);
            }
        }
        return Option.empty();
    }

    private static Option<MatrixItem> matrix(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new MatrixItem();
        item.setFrameOfReferenceTransformationMatrixType(frameOfReferenceTransformationMatrixType(attr));
        item.setFrameOfReferenceTransformationMatrix(readDoubles(attr, Tag.FrameOfReferenceTransformationMatrix));
        return Option.of(item);
    }

    private static Option<RegistrationTypeCodeItem> registrationTypeCode(Attributes attr) {
        return Option.empty();
    }

    private static Option<ReferencedFrameOfReferenceItem> referencedFrameOfReference(Attributes attr) {
        if (attr == null) return Option.empty();
        ReferencedFrameOfReferenceItem item = new ReferencedFrameOfReferenceItem();
        item.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        item.setRtReferencedStudySequence(readSequence(attr, Tag.RTReferencedStudySequence, Reader::rtReferencedStudy));
        return Option.of(item);
    }

    private static Option<RTReferencedStudyItem> rtReferencedStudy(Attributes attr) {
        if (attr == null) return Option.empty();
        var optReferencedSOPClassInstance = referencedSOPClassInstance(attr);
        RTReferencedStudyItem item;
        if (optReferencedSOPClassInstance.isEmpty()) {
            item = new RTReferencedStudyItem();
        } else {
            item = new RTReferencedStudyItem(optReferencedSOPClassInstance.get());
        }
        item.setRtReferencedSeriesSequence(readSequence(attr, Tag.RTReferencedSeriesSequence, Reader::rtReferencedSeriesItem));
        return Option.of(item);
    }

    private static Option<RTReferencedSeriesItem> rtReferencedSeriesItem(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new RTReferencedSeriesItem();
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        item.setContourImageSequence(readSequence(attr, Tag.ContourImageSequence, Reader::referencedSOPClassInstance));
        return Option.of(item);
    }

    private static Option<ROIContourItem> roiContour(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new ROIContourItem();
        item.setROIDisplayColor(readInts(attr, Tag.ROIDisplayColor));
        item.setContourSequence(readSequence(attr, Tag.ContourSequence, Reader::contour));
        item.setReferencedROINumber(readInt(attr, Tag.ReferencedROINumber));
        return Option.of(item);
    }

    private static Option<ContourItem> contour(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new ContourItem();
        item.setContourImageSequence(readSequence(attr, Tag.ContourImageSequence, Reader::referencedSOPClassInstance));
        item.setContourGeometricType(readString(attr, Tag.ContourGeometricType));
        item.setNumberOfContourPoints(readInt(attr, Tag.NumberOfContourPoints));
        item.setContourNumber(readInt(attr, Tag.ContourNumber));
        item.setContourData(readListDoubles(attr, Tag.ContourData));
        return Option.of(item);
    }

    private static Option<StructureSetROIItem> structureSetROI(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new StructureSetROIItem();
        item.setROINumber(readInt(attr, Tag.ROINumber));
        item.setReferencedFrameOfReferenceUID(readString(attr, Tag.ReferencedFrameOfReferenceUID));
        item.setROIName(readString(attr, Tag.ROIName));
        item.setROIGenerationAlgorithm(readString(attr, Tag.ROIGenerationAlgorithm));
        return Option.of(item);
    }

    private static Option<RTROIObservationsItem> rtROIObservations(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new RTROIObservationsItem();

        item.setObservationNumber(readInt(attr, Tag.ObservationNumber));
        item.setReferencedROINumber(readInt(attr, Tag.ReferencedROINumber));
        item.setROIObservationLabel(readString(attr, Tag.ROIObservationLabel));
        item.setRTROIInterpretedType(readString(attr, Tag.RTROIInterpretedType));
        item.setROIInterpreter(readString(attr, Tag.ROIInterpreter));
        item.setROIPhysicalPropertiesSequence(readSequence(attr, Tag.ROIPhysicalPropertiesSequence, Reader::roiPhysicalProperties));
        item.setMaterialID(readString(attr, Tag.MaterialID));
        return Option.of(item);
    }

    private static Option<ROIPhysicalPropertiesItem> roiPhysicalProperties(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new ROIPhysicalPropertiesItem();
        item.setROIPhysicalProperty(readString(attr, Tag.ROIPhysicalProperty));
        item.setROIPhysicalPropertyValue(readDouble(attr, Tag.ROIPhysicalPropertyValue));
        item.setROIElementalCompositionSequence(readSequence(attr, Tag.ROIElementalCompositionSequence, Reader::roiElementalComposition));
        return Option.of(item);
    }

    private static Option<ROIElementalCompositionItem> roiElementalComposition(Attributes attr) {
        if (attr == null) return Option.empty();
        var item = new ROIElementalCompositionItem();
        item.setRoiElementalCompositionAtomicNumber(readInt(attr, Tag.ROIElementalCompositionAtomicNumber));
        item.setRoiElementalCompositionAtomicMassFraction(readDouble(attr, Tag.ROIElementalCompositionAtomicMassFraction));
        return Option.of(item);
    }

    private static Option<PixelRepresentation> pixelRepresentation(Attributes attr) {
        if (attr == null) return Option.empty();
        int val = attr.getInt(Tag.PixelRepresentation, DicomUtils.UNDEFINED_U32);
        if (val == 0) return Option.of(PixelRepresentation.UNSIGNED);
        else if (val == 1) return Option.of(PixelRepresentation.TWO_COMPLEMENT);
        return Option.of(PixelRepresentation.NONE);
    }

    private static Option<PatientPosition> patientPosition(Attributes attr) {
        if (attr == null) return Option.empty();
        var pp = attr.getString(Tag.PatientPosition, "");
        if (pp.equals("HFS")) return Option.of(PatientPosition.HFS);
        if (pp.equals("FFS")) return Option.of(PatientPosition.FFS);
        if (pp.equals("HFP")) return Option.of(PatientPosition.HFP);
        if (pp.equals("FFP")) return Option.of(PatientPosition.FFP);
        log.error("Unsupported patient position: " + pp);
        return Option.of(PatientPosition.UNKOWN);
    }

    private static Option<PhotometricInterpretation> photometricInterpretation(Attributes attr) {
        if (attr == null) return Option.empty();
        var val = attr.getString(Tag.PhotometricInterpretation, "");
        switch (val) {
            case "MONOCHROME1":
                return Option.of(PhotometricInterpretation.MONOCHROME1);
            case "MONOCHROME2":
                return Option.of(PhotometricInterpretation.MONOCHROME2);
            case "PALETTE COLOR":
                return Option.of(PhotometricInterpretation.PALETTE_COLOR);
            case "RGB":
                return Option.of(PhotometricInterpretation.RGB);
            case "HSV":
                return Option.of(PhotometricInterpretation.RETIRED_HSV);
            case "ARGB":
                return Option.of(PhotometricInterpretation.RETIRED_ARGB);
            case "CMYK":
                return Option.of(PhotometricInterpretation.RETIRED_CMYK);
            case "FULL":
                return Option.of(PhotometricInterpretation.RETIRED_YBR_FULL);
            case "YBR_FULL_422":
                return Option.of(PhotometricInterpretation.YBR_FULL_422);
            case "YBR_PARTIAL_422":
                return Option.of(PhotometricInterpretation.YBR_PARTIAL_422);
            case "YBR_PARTIAL_420":
                return Option.of(PhotometricInterpretation.YBR_PARTIAL_420);
            default:
                return Option.of(PhotometricInterpretation.UNKOWN);
        }
    }

    private static Option<Modality> modality(Attributes attr) {
        if (attr == null) return Option.empty();
        var optVal = readString(attr, Tag.Modality);
        if (optVal.isEmpty()) return Option.empty();
        var val = optVal.get();
        switch (val) {
            case "AR":
                return Option.of(Modality.AR);
            case "AS":
                return Option.of(Modality.RETIRED_AS);
            case "ASMT":
                return Option.of(Modality.ASMT);
            case "AU":
                return Option.of(Modality.AU);
            case "BDUS":
                return Option.of(Modality.BDUS);
            case "BI":
                return Option.of(Modality.BI);
            case "BMD":
                return Option.of(Modality.BMD);
            case "CD":
                return Option.of(Modality.RETIRED_CD);
            case "CF":
                return Option.of(Modality.RETIRED_CF);
            case "CP":
                return Option.of(Modality.RETIRED_CP);
            case "CR":
                return Option.of(Modality.CR);
            case "CS":
                return Option.of(Modality.RETIRED_CS);
            case "CT":
                return Option.of(Modality.CT);
            case "DD":
                return Option.of(Modality.RETIRED_DD);
            case "DF":
                return Option.of(Modality.RETIRED_DF);
            case "DG":
                return Option.of(Modality.DG);
            case "DM":
                return Option.of(Modality.RETIRED_DM);
            case "DOC":
                return Option.of(Modality.DOC);
            case "DS":
                return Option.of(Modality.RETIRED_DS);
            case "DX":
                return Option.of(Modality.DX);
            case "EC":
                return Option.of(Modality.RETIRED_EC);
            case "ECG":
                return Option.of(Modality.ECG);
            case "EPS":
                return Option.of(Modality.EPS);
            case "ES":
                return Option.of(Modality.ES);
            case "FA":
                return Option.of(Modality.RETIRED_FA);
            case "FID":
                return Option.of(Modality.FID);
            case "FS":
                return Option.of(Modality.RETIRED_FS);
            case "GM":
                return Option.of(Modality.GM);
            case "HC":
                return Option.of(Modality.HC);
            case "HD":
                return Option.of(Modality.HD);
            case "IO":
                return Option.of(Modality.IO);
            case "IOL":
                return Option.of(Modality.IOL);
            case "IVOCT":
                return Option.of(Modality.IVOCT);
            case "IVUS":
                return Option.of(Modality.IVUS);
            case "KER":
                return Option.of(Modality.KER);
            case "KO":
                return Option.of(Modality.KO);
            case "LEN":
                return Option.of(Modality.LEN);
            case "LP":
                return Option.of(Modality.RETIRED_LP);
            case "LS":
                return Option.of(Modality.LS);
            case "MA":
                return Option.of(Modality.RETIRED_MA);
            case "MG":
                return Option.of(Modality.MG);
            case "MR":
                return Option.of(Modality.MR);
            case "MS":
                return Option.of(Modality.RETIRED_MS);
            case "NM":
                return Option.of(Modality.NM);
            case "OAM":
                return Option.of(Modality.OAM);
            case "OCT":
                return Option.of(Modality.OCT);
            case "OP":
                return Option.of(Modality.OP);
            case "OPM":
                return Option.of(Modality.OPM);
            case "OPR":
                return Option.of(Modality.OPR);
            case "OPT":
                return Option.of(Modality.RETIRED_OPT);
            case "OPV":
                return Option.of(Modality.OPV);
            case "OSS":
                return Option.of(Modality.OSS);
            case "OT":
                return Option.of(Modality.OT);
            case "PLAN":
                return Option.of(Modality.PLAN);
            case "PR":
                return Option.of(Modality.PR);
            case "PT":
                return Option.of(Modality.PT);
            case "PX":
                return Option.of(Modality.PX);
            case "REG":
                return Option.of(Modality.REG);
            case "RESP":
                return Option.of(Modality.RESP);
            case "RF":
                return Option.of(Modality.RF);
            case "RG":
                return Option.of(Modality.RG);
            case "RTDOSE":
                return Option.of(Modality.RTDOSE);
            case "RTIMAGE":
                return Option.of(Modality.RTIMAGE);
            case "RTPLAN":
                return Option.of(Modality.RTPLAN);
            case "RTRECORD":
                return Option.of(Modality.RTRECORD);
            case "RTSTRUCT":
                return Option.of(Modality.RTSTRUCT);
            case "RWV":
                return Option.of(Modality.RWV);
            case "SEG":
                return Option.of(Modality.SEG);
            case "SM":
                return Option.of(Modality.SM);
            case "SMR":
                return Option.of(Modality.SMR);
            case "SR":
                return Option.of(Modality.SR);
            case "SRF":
                return Option.of(Modality.SRF);
            case "ST":
                return Option.of(Modality.RETIRED_ST);
            case "STAIN":
                return Option.of(Modality.STAIN);
            case "TG":
                return Option.of(Modality.TG);
            case "US":
                return Option.of(Modality.US);
            case "VA":
                return Option.of(Modality.VA);
            case "VF":
                return Option.of(Modality.RETIRED_VF);
            case "XA":
                return Option.of(Modality.XA);
            case "XC":
                return Option.of(Modality.XC);
        }
        return Option.of(Modality.UNKNOWN);
    }

    /**
     * @param buffer byte array
     * @param offset offset to start reading data in the byte array
     * @param length number of bytes to read
     * @param bps    bytes per sample
     * @param order  byteorder
     * @return List with pixel values
     */
    private static Option<List<Long>> getPixelData(byte[] buffer, int offset, int length,
                                                   int bps, PixelRepresentation pixelRepresentation, ByteOrder order) throws UnsupportedOperationException {
        List<Long> pixels;
        int end = offset + length;
        if (buffer == null || end > buffer.length || order == null) return Option.empty();
        pixels = new ArrayList<>(length / bps + 1);
        if (bps == 2) {
            int i = offset;
            switch (pixelRepresentation) {
                case UNSIGNED:
                    while (i < end) {
                        int tv = ByteUtils.bytesToUShort(buffer, i, order == ByteOrder.BIG_ENDIAN);
                        pixels.add((long) tv);
                        i += bps;
                    }
                    break;
                case TWO_COMPLEMENT:
                    while (i < end) {
                        int tv = ByteUtils.bytesToShort(buffer, i, order == ByteOrder.BIG_ENDIAN);
                        pixels.add((long) tv);
                        i += bps;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Currently only 2 types of 16 bit pixels can be interpreted: unsigned and two complement");
            }
        } else if (bps == 4) {
            int i = offset;
            switch (pixelRepresentation) {
                case TWO_COMPLEMENT:
                    while (i < end) {
                        int tv = ByteUtils.bytesToInt(buffer, i, order == ByteOrder.BIG_ENDIAN);
                        pixels.add((long) tv);
                        i += bps;
                    }
                    break;
                case UNSIGNED:
                    while (i < end) {
                        long tv = ByteTools.bytesToUInt(buffer, i, order == ByteOrder.BIG_ENDIAN);
                        pixels.add(tv);
                        i += bps;
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("Currently only 1 type of 32 bit pixels can be interpreted: two complement.");
            }
        } else {
            throw new UnsupportedOperationException("Currently only 2 or 4 bytes per sample are supported.");
        }
        return Option.of(pixels);
    }

    private static Option<MetaHeader> metaHeader(Attributes attr) {
        if (attr == null) return Option.empty();
        var hdr = new MetaHeader();
        hdr.setFileMetaInformationGroupLength(readInt(attr, Tag.FileMetaInformationGroupLength));
        hdr.setFileMetaInformationVersion(readBytes(attr, Tag.FileMetaInformationVersion));
        hdr.setMediaStorageSOPClassUID(readString(attr, Tag.MediaStorageSOPClassUID));
        hdr.setMediaStorageSOPInstanceUID(readString(attr, Tag.MediaStorageSOPInstanceUID));
        hdr.setTransferSyntaxUID(readString(attr, Tag.TransferSyntaxUID));
        hdr.setImplementationClassUID(readString(attr, Tag.ImplementationClassUID));
        hdr.setImplementationVersionName(readString(attr, Tag.ImplementationVersionName));
        return Option.of(hdr);
    }

    private static Option<DvhItem> dvh(Attributes attr) {
        if (attr == null) return Option.empty();
        DvhItem item = new DvhItem();
        item.setDvhType(readString(attr, Tag.DVHType));
        item.setDoseUnits(readString(attr, Tag.DoseUnits));
        item.setDoseType(readString(attr, Tag.DoseType));
        item.setDvhDoseScaling(readDouble(attr, Tag.DVHDoseScaling));
        item.setDvhVolumeUnits(readString(attr, Tag.DVHVolumeUnits));
        item.setDvhNumberOfBins(readInt(attr, Tag.DVHNumberOfBins));
        item.setDvhData(readDoubles(attr, Tag.DVHData));
        item.setDvhReferencedROISequence(readSequence(attr, Tag.DVHReferencedROISequence, Reader::dvhReferencedROI));
        item.setDvhMinimumDose(readDouble(attr, Tag.DVHMinimumDose));
        item.setDvhMaximumDose(readDouble(attr, Tag.DVHMaximumDose));
        item.setDvhMeanDose(readDouble(attr, Tag.DVHMeanDose));
        return Option.of(item);
    }

    public static Option<CT> ct(Attributes meta, Attributes attr, ByteOrder order) throws IOException, DicomException {
        if (attr == null) return Option.empty();
        if (order != ByteOrder.LITTLE_ENDIAN)
            throw new DicomException("Only little endian DICOM files are currently supported.");
        var optMeta = metaHeader(meta);
        CT ct;
        if (optMeta.isEmpty()) {
            ct = new CT();
        } else {
            ct = new CT(optMeta.get());
        }
        ct.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        ct.setImageType(readListString(attr, Tag.ImageType));
        ct.setSOPClassUID(readString(attr, Tag.SOPClassUID));
        ct.setSOPInstanceUID(readString(attr, Tag.SOPInstanceUID));
        ct.setStudyDate(readDate(attr, Tag.StudyDate));
        ct.setSeriesDate(readDate(attr, Tag.SeriesDate));
        ct.setAcquisitionDate(readDate(attr, Tag.AcquisitionDate));
        ct.setContentDate(readDate(attr, Tag.ContentDate));
        ct.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        ct.setSeriesTime(DicomUtils.tmToLocalTime(readString(attr, Tag.SeriesTime)));
        ct.setAcquisitionTime(DicomUtils.tmToLocalTime(readString(attr, Tag.AcquisitionTime)));
        ct.setContentTime(DicomUtils.tmToLocalTime(readString(attr, Tag.ContentTime)));
        ct.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        ct.setModality(modality(attr));
        if (ct.getModality().isEmpty() || ct.getModality().get() != Modality.CT) {
            log.error("Trying to read a DICOM file that is not a CT");
            return Option.empty();
        }
        ct.setManufacturer(readString(attr, Tag.Manufacturer));
        ct.setInstitutionName(readString(attr, Tag.InstitutionName));
        ct.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        ct.setStationName(readString(attr, Tag.StationName));
        ct.setProcedureCodeSequence(readSequence(attr, Tag.ProcedureCodeSequence, Reader::reducedCode));
        ct.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        ct.setInstitutionalDepartmentName(readString(attr, Tag.InstitutionalDepartmentName));
        ct.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        ct.setReferencedStudySequence(readSequence(attr, Tag.ReferencedStudySequence, Reader::referencedSOPClassInstance));
        ct.setPatientName(readString(attr, Tag.PatientName));
        ct.setPatientID(readString(attr, Tag.PatientID));
        ct.setPatientBirthDate(readDate(attr, Tag.PatientBirthDate));
        ct.setPatientSex(readString(attr, Tag.PatientSex));
        ct.setPatientAge(readString(attr, Tag.PatientAge));
        ct.setPatientIdentityRemoved(readString(attr, Tag.PatientIdentityRemoved));
        ct.setDeidentificationMethod(readString(attr, Tag.DeidentificationMethod));
        ct.setBodyPartExamined(readString(attr, Tag.BodyPartExamined));
        ct.setScanOptions(readString(attr, Tag.ScanOptions));
        ct.setSliceThickness(readDouble(attr, Tag.SliceThickness));
        ct.setKVP(readDouble(attr, Tag.KVP));
        ct.setDataCollectionDiameter(readDouble(attr, Tag.DataCollectionDiameter));
        ct.setDeviceSerialNumber(readString(attr, Tag.DeviceSerialNumber));
        ct.setSoftwareVersions(readString(attr, Tag.SoftwareVersions));
        ct.setProtocolName(readString(attr, Tag.ProtocolName));
        ct.setReconstructionDiameter(readDouble(attr, Tag.ReconstructionDiameter));
        ct.setGantryDetectorTilt(readDouble(attr, Tag.GantryDetectorTilt));
        ct.setTableHeight(readDouble(attr, Tag.TableHeight));
        ct.setRotationDirection(readString(attr, Tag.RotationDirection));
        ct.setExposureTime(readInt(attr, Tag.ExposureTime));
        ct.setXRayTubeCurrent(readInt(attr, Tag.XRayTubeCurrent));
        ct.setExposure(readInt(attr, Tag.Exposure));
        ct.setGeneratorPower(readInt(attr, Tag.GeneratorPower));
        ct.setFocalSpots(readDoubles(attr, Tag.FocalSpots));
        ct.setConvolutionKernel(readString(attr, Tag.ConvolutionKernel));
        ct.setPatientPosition(patientPosition(attr));
        ct.setExposureModulationType(readString(attr, Tag.ExposureModulationType));
        ct.setEstimatedDoseSaving(readDouble(attr, Tag.EstimatedDoseSaving));
        ct.setCTDIvol(readDouble(attr, Tag.CTDIvol));
        ct.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        ct.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        ct.setStudyID(readString(attr, Tag.StudyID));
        ct.setSeriesNumber(readInt(attr, Tag.SeriesNumber));
        ct.setAcquisitionNumber(readInt(attr, Tag.AcquisitionNumber));
        ct.setInstanceNumber(readInt(attr, Tag.InstanceNumber));
        ct.setPatientOrientation(readString(attr, Tag.PatientOrientation));
        ct.setImagePositionPatient(readDoubles(attr, Tag.ImagePositionPatient));
        ct.setImageOrientationPatient(readDoubles(attr, Tag.ImageOrientationPatient));
        ct.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        ct.setPositionReferenceIndicator(readString(attr, Tag.PositionReferenceIndicator));
        ct.setSliceLocation(readDouble(attr, Tag.SliceLocation));
        ct.setImageComments(readString(attr, Tag.ImageComments));
        ct.setSamplesPerPixel(readInt(attr, Tag.SamplesPerPixel));
        ct.setPhotometricInterpretation(photometricInterpretation(attr));
        ct.setRows(readInt(attr, Tag.Rows));
        ct.setColumns(readInt(attr, Tag.Columns));
        ct.setPixelSpacing(readDoubles(attr, Tag.PixelSpacing));
        ct.setBitsAllocated(readInt(attr, Tag.BitsAllocated));
        ct.setBitsStored(readInt(attr, Tag.BitsStored));
        ct.setHighBit(readInt(attr, Tag.HighBit));
        ct.setPixelRepresentation(pixelRepresentation(attr));
        ct.setWindowCenter(readDouble(attr, Tag.WindowCenter));
        ct.setWindowWidth(readDouble(attr, Tag.WindowWidth));
        ct.setRescaleIntercept(readDouble(attr, Tag.RescaleIntercept));
        ct.setRescaleSlope(readDouble(attr, Tag.RescaleSlope));
        ct.setScheduledProcedureStepStartDate(readDate(attr, Tag.ScheduledProcedureStepStartDate));
        ct.setScheduledProcedureStepStartTime(DicomUtils.tmToLocalTime(readString(attr, Tag.ScheduledProcedureStepStartTime)));
        ct.setScheduledProcedureStepEndDate(readDate(attr, Tag.ScheduledProcedureStepEndDate));
        ct.setScheduledProcedureStepEndTime(DicomUtils.tmToLocalTime(readString(attr, Tag.ScheduledProcedureStepEndTime)));
        ct.setPerformedProcedureStepStartDate(readDate(attr, Tag.PerformedProcedureStepStartDate));
        ct.setPerformedProcedureStepStartTime(DicomUtils.tmToLocalTime(readString(attr, Tag.PerformedProcedureStepStartTime)));
        ct.setPerformedProcedureStepID(readString(attr, Tag.PerformedProcedureStepID));
        ct.setPerformedProtocolCodeSequence(readSequence(attr, Tag.PerformedProtocolCodeSequence, Reader::reducedCode));
        var optBuf = readBytes(attr, Tag.PixelData);
        if (ct.getBitsAllocated().isPresent() && optBuf.isPresent()) {
            var tbuf = optBuf.get();
            int nbuf = tbuf.length;
            int bps = ct.getBitsAllocated().get() / 8;
            if (ct.getBitsAllocated().get() != 16 || bps != 2)
                throw new DicomException("Only 16bit CT pixeldata is supported");
            var optPixelRepresentation = ct.getPixelRepresentation();
            if (optPixelRepresentation.isEmpty()) throw new DicomException("CT requires a valid pixel representation");
            byte[] bb = new byte[nbuf];
            for (int i = 0; i < nbuf; i++) {
                bb[i] = tbuf[i];
            }
            tbuf = null;
            optBuf = Option.empty();
            ct.setPixelData(getPixelData(bb, 0, nbuf, bps, optPixelRepresentation.get(), order));
        }
        return Option.of(ct);
    }

    public static Option<RTDose> rtDose(Attributes meta, Attributes attr, ByteOrder order) throws DicomException, IOException {
        if (attr == null) return Option.empty();
        var optMeta = metaHeader(meta);
        RTDose rtdose;
        if (optMeta.isEmpty()) {
            rtdose = new RTDose();
        } else {
            rtdose = new RTDose(optMeta.get());
        }
        rtdose.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        rtdose.setInstanceCreationDate(readDate(attr, Tag.InstanceCreationDate));
        rtdose.setInstanceCreationTime(DicomUtils.tmToLocalTime(readString(attr, Tag.InstanceCreationTime)));
        rtdose.setSopClassUID(readString(attr, Tag.SOPClassUID));
        if (rtdose.getSopClassUID().isEmpty() || !rtdose.getSopClassUID().get().equals(UID.RTDoseStorage)) {
            log.error("Trying to read a DICOM file that is not a RTDOSE");
            return Option.empty();
        }
        rtdose.setStudyDate(readDate(attr, Tag.StudyDate));
        rtdose.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        rtdose.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        rtdose.setModality(modality(attr));
        if (rtdose.getModality().isEmpty() || !rtdose.getModality().get().equals(Modality.RTDOSE)) {
            log.error("Trying to read a DICOM file that is not a RTDOSE");
            return Option.empty();
        }
        rtdose.setManufacturer(readString(attr, Tag.Manufacturer));
        rtdose.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        rtdose.setStationName(readString(attr, Tag.StationName));
        rtdose.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        rtdose.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        rtdose.setPatientName(readString(attr, Tag.PatientName));
        rtdose.setPatientID(readString(attr, Tag.PatientID));
        rtdose.setPatientBirthDate(readDate(attr, Tag.PatientBirthDate));
        rtdose.setPatientSex(readString(attr, Tag.PatientSex));
        rtdose.setSliceThicknes(readDouble(attr, Tag.SliceThickness));
        rtdose.setDeviceSerialNumber(readString(attr, Tag.DeviceSerialNumber));
        rtdose.setSoftwareVersions(readString(attr, Tag.SoftwareVersions));
        rtdose.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        rtdose.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        rtdose.setStudyID(readString(attr, Tag.StudyID));
        rtdose.setSeriesNumber(readInt(attr, Tag.SeriesNumber));
        rtdose.setInstanceNumber(readInt(attr, Tag.InstanceNumber));
        rtdose.setImagePositionPatient(readDoubles(attr, Tag.ImagePositionPatient));
        rtdose.setImageOrientationPatient(readDoubles(attr, Tag.ImageOrientationPatient));
        rtdose.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        rtdose.setPositionReferenceIndicator(readString(attr, Tag.PositionReferenceIndicator));
        rtdose.setSamplesPerPixel(readInt(attr, Tag.SamplesPerPixel));
        rtdose.setPhotometricInterpretation(photometricInterpretation(attr));
        rtdose.setNumberOfFrames(readInt(attr, Tag.NumberOfFrames));
        rtdose.setFrameIncrementPointer(readInt(attr, Tag.FrameIncrementPointer));
        rtdose.setRows(readInt(attr, Tag.Rows));
        rtdose.setColumns(readInt(attr, Tag.Columns));
        rtdose.setPixelSpacing(readDoubles(attr, Tag.PixelSpacing));
        rtdose.setBitsAllocated(readInt(attr, Tag.BitsAllocated));
        rtdose.setBitsStored(readInt(attr, Tag.BitsStored));
        rtdose.setHighBit(readInt(attr, Tag.HighBit));
        rtdose.setPixelRepresentation(pixelRepresentation(attr));
        rtdose.setDoseUnits(readString(attr, Tag.DoseUnits));
        rtdose.setDoseType(readString(attr, Tag.DoseType));
        rtdose.setDoseSummationType(readString(attr, Tag.DoseSummationType));
        rtdose.setGridFrameOffsetVector(readDoubles(attr, Tag.GridFrameOffsetVector));
        rtdose.setDoseGridScaling(readDouble(attr, Tag.DoseGridScaling));
        rtdose.setTissueHeterogeneityCorrection(readString(attr, Tag.TissueHeterogeneityCorrection));
        rtdose.setDvhSequence(readSequence(attr, Tag.DVHSequence, Reader::dvh));
        rtdose.setReferencedRTPlanSequence(readSequence(attr, Tag.ReferencedRTPlanSequence, Reader::referencedSOPClassInstance));
        rtdose.setReferencedStructureSetSequence(readSequence(attr, Tag.ReferencedStructureSetSequence, Reader::referencedSOPClassInstance));
        if (rtdose.getPhotometricInterpretation().isEmpty() || rtdose.getPhotometricInterpretation().get() != PhotometricInterpretation.MONOCHROME2) {
            var pi = "";
            if (rtdose.getPhotometricInterpretation().isPresent())
                pi = rtdose.getPhotometricInterpretation().get().toString();
            throw new DicomException("In RTDose files the supported photometric interpretation is MONOCHROME2, not " + pi);
        }

        var optBuf = readBytes(attr, Tag.PixelData);
        if (rtdose.getBitsAllocated().isPresent() && optBuf.isPresent()) {
            var tbuf = optBuf.get();
            int nbuf = tbuf.length;
            var optBitsAllocated = rtdose.getBitsAllocated();
            var bitsAllocated = optBitsAllocated.get();
            int bps = bitsAllocated / 8;
            if ((bitsAllocated != 16 || bps != 2) && (bitsAllocated != 32 || bps != 4))
                throw new DicomException("Only 16 or 32 bit pixeldata is supported");
            if (rtdose.getPixelRepresentation().isEmpty())
                throw new DicomException("RTDOSE requires a valid pixel representation");
            byte[] bb = new byte[nbuf];
            for (int i = 0; i < nbuf; i++) {
                bb[i] = tbuf[i];
            }
            tbuf = null;
            optBuf = Option.empty();
            rtdose.setPixelData(getPixelData(bb, 0, nbuf, bps, rtdose.getPixelRepresentation().get(), order));
        }
        return Option.of(rtdose);
    }

    public static Option<PT> pt(Attributes meta, Attributes attr, ByteOrder order) throws IOException, DicomException {
        if (attr == null) return Option.empty();
        var optMeta = metaHeader(meta);
        PT pt;
        if (optMeta.isEmpty()) {
            pt = new PT();
        } else {
            pt = new PT(optMeta.get());
        }
        pt.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        pt.setImageType(readString(attr, Tag.ImageType));
        pt.setSOPClassUID(readString(attr, Tag.SOPClassUID));
        pt.setSOPInstanceUID(readString(attr, Tag.SOPInstanceUID));
        pt.setStudyDate(readDate(attr, Tag.StudyDate));
        pt.setSeriesDate(readDate(attr, Tag.SeriesDate));
        pt.setAcquisitionDate(readDate(attr, Tag.AcquisitionDate));
        pt.setContentDate(readDate(attr, Tag.ContentDate));
        pt.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        pt.setSeriesTime(DicomUtils.tmToLocalTime(readString(attr, Tag.SeriesTime)));
        pt.setAcquisitionTime(DicomUtils.tmToLocalTime(readString(attr, Tag.AcquisitionTime)));
        pt.setContentTime(DicomUtils.tmToLocalTime(readString(attr, Tag.ContentTime)));
        pt.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        pt.setModality(modality(attr));
        if (pt.getModality().isEmpty() || pt.getModality().get() != Modality.PT) {
            log.error("Trying to read a DICOM file that is not a PT");
            return Option.empty();
        }
        pt.setManufacturer(readString(attr, Tag.Manufacturer));
        pt.setInstitutionName(readString(attr, Tag.InstitutionName));
        pt.setInstitutionAddress(readString(attr, Tag.InstitutionAddress));
        pt.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        pt.setStationName(readString(attr, Tag.StationName));
        pt.setStudyDescription(readString(attr, Tag.StudyDescription));
        pt.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        pt.setInstitutionalDepartmentName(readString(attr, Tag.InstitutionalDepartmentName));
        pt.setPhysiciansOfRecord(readString(attr, Tag.PhysiciansOfRecord));
        pt.setPerformingPhysicianName(readString(attr, Tag.PerformingPhysicianName));
        pt.setOperatorsName(readString(attr, Tag.OperatorsName));
        pt.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        pt.setReferencedPatientSequence(readSequence(attr, Tag.ReferencedPatientSequence, Reader::referencedSOPClassInstance));
        pt.setPurposeOfReferenceCodeSequence(readSequence(attr, Tag.PurposeOfReferenceCodeSequence, Reader::codeItem));
        pt.setPatientName(readString(attr, Tag.PatientName));
        pt.setPatientID(readString(attr, Tag.PatientID));
        pt.setIssuerOfPatientID(readString(attr, Tag.IssuerOfPatientID));
        pt.setPatientBirthDate(readDate(attr, Tag.PatientBirthDate));
        pt.setPatientSex(readString(attr, Tag.PatientSex));
        pt.setPatientAge(readString(attr, Tag.PatientAge));
        pt.setPatientSize(readDouble(attr, Tag.PatientSize));
        pt.setPatientWeight(readDouble(attr, Tag.PatientWeight));
        pt.setPatientAddress(readString(attr, Tag.PatientAddress));
        pt.setBranchOfService(readString(attr, Tag.BranchOfService));
        pt.setPregnancyStatus(readString(attr, Tag.PregnancyStatus));
        pt.setBodyPartExamined(readString(attr, Tag.BodyPartExamined));
        pt.setSliceThickness(readDouble(attr, Tag.SliceThickness));
        pt.setDeviceSerialNumber(readString(attr, Tag.DeviceSerialNumber));
        pt.setSoftwareVersions(readString(attr, Tag.SoftwareVersions));
        pt.setCollimatorType(readString(attr, Tag.CollimatorType));
        pt.setDateOfLastCalibration(readDate(attr, Tag.DateOfLastCalibration));
        pt.setTimeOfLastCalibration(DicomUtils.tmToLocalTime(readString(attr, Tag.TimeOfLastCalibration)));
        pt.setConvolutionKernel(readString(attr, Tag.ConvolutionKernel));
        pt.setActualFrameDuration(readInt(attr, Tag.ActualFrameDuration));
        pt.setPatientPosition(patientPosition(attr));
        pt.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        pt.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        pt.setStudyID(readString(attr, Tag.StudyID));
        pt.setSeriesNumber(readInt(attr, Tag.SeriesNumber));
        pt.setAcquisitionNumber(readInt(attr, Tag.AcquisitionNumber));
        pt.setInstanceNumber(readInt(attr, Tag.InstanceNumber));
        pt.setImagePositionPatient(readDoubles(attr, Tag.ImagePositionPatient));
        pt.setImageOrientationPatient(readDoubles(attr, Tag.ImageOrientationPatient));
        pt.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        pt.setPositionReferenceIndicator(readString(attr, Tag.PositionReferenceIndicator));
        pt.setSliceLocation(readDouble(attr, Tag.SliceLocation));
        pt.setImageComments(readString(attr, Tag.ImageComments));
        pt.setSamplesPerPixel(readInt(attr, Tag.SamplesPerPixel));
        pt.setPhotometricInterpretation(photometricInterpretation(attr));
        pt.setRows(readInt(attr, Tag.Rows));
        pt.setColumns(readInt(attr, Tag.Columns));
        pt.setPixelSpacing(readDoubles(attr, Tag.PixelSpacing));
        pt.setCorrectedImage(readListString(attr, Tag.CorrectedImage));
        pt.setBitsAllocated(readInt(attr, Tag.BitsAllocated));
        pt.setBitsStored(readInt(attr, Tag.BitsStored));
        pt.setHighBit(readInt(attr, Tag.HighBit));
        pt.setPixelRepresentation(pixelRepresentation(attr));
        pt.setSmallestImagePixelValue(readInt(attr, Tag.SmallestImagePixelValue));
        pt.setLargestImagePixelValue(readInt(attr, Tag.LargestImagePixelValue));
        pt.setWindowCenter(readDouble(attr, Tag.WindowCenter));
        pt.setWindowWidth(readDouble(attr, Tag.WindowWidth));
        pt.setRescaleIntercept(readDouble(attr, Tag.RescaleIntercept));
        pt.setRescaleSlope(readDouble(attr, Tag.RescaleSlope));
        pt.setRescaleType(readString(attr, Tag.RescaleType));
        pt.setRequestingPhysician(readString(attr, Tag.RequestingPhysician));
        pt.setRequestingService(readString(attr, Tag.RequestingService));
        pt.setRequestedProcedureDescription(readString(attr, Tag.RequestedProcedureDescription));
        pt.setCurrentPatientLocation(readString(attr, Tag.CurrentPatientLocation));
        pt.setPerformedProcedureStepStartDate(readDate(attr, Tag.PerformedProcedureStepStartDate));
        pt.setPerformedProcedureStepStartTime(DicomUtils.tmToLocalTime(readString(attr, Tag.PerformedProcedureStepStartTime)));
        pt.setRequestAttributesSequence(readSequence(attr, Tag.RequestAttributesSequence, Reader::requestAttributes));
        pt.setRequestedProcedureID(readString(attr, Tag.RequestedProcedureID));
        pt.setEnergyWindowRangeSequence(readSequence(attr, Tag.EnergyWindowRangeSequence, Reader::energyWindowRange));
        pt.setRadiopharmaceuticalInformationSequence(readSequence(attr, Tag.RadiopharmaceuticalInformationSequence, Reader::radiopharmaceuticalInformation));
        pt.setNumberOfSlices(readInt(attr, Tag.NumberOfSlices));
        pt.setPatientOrientationCodeSequence(readSequence(attr, Tag.PatientOrientationCodeSequence, Reader::patientOrientationCode));
        pt.setPatientGantryRelationshipCodeSequence(readSequence(attr, Tag.PatientGantryRelationshipCodeSequence, Reader::codeItem));
        pt.setSeriesType(readString(attr, Tag.SeriesType));
        pt.setUnits(readString(attr, Tag.Units));
        pt.setCountsSource(readString(attr, Tag.CountsSource));
        pt.setRandomsCorrectionMethod(readString(attr, Tag.RandomsCorrectionMethod));
        pt.setAttenuationCorrectionMethod(readString(attr, Tag.AttenuationCorrectionMethod));
        pt.setDecayCorrection(readString(attr, Tag.DecayCorrection));
        pt.setReconstructionMethod(readString(attr, Tag.ReconstructionMethod));
        pt.setScatterCorrectionMethod(readString(attr, Tag.ScatterCorrectionMethod));
        pt.setAxialAcceptance(readDouble(attr, Tag.AxialAcceptance));
        pt.setAxialMash(readInts(attr, Tag.AxialMash));
        pt.setFrameReferenceTime(readDouble(attr, Tag.FrameReferenceTime));
        pt.setDecayFactor(readDouble(attr, Tag.DecayFactor));
        pt.setDoseCalibrationFactor(readDouble(attr, Tag.DoseCalibrationFactor));
        pt.setScatterFractionFactor(readDouble(attr, Tag.ScatterFractionFactor));
        pt.setImageIndex(readInt(attr, Tag.ImageIndex));

        var optBuf = readBytes(attr, Tag.PixelData);
        if (pt.getBitsAllocated().isPresent() && optBuf.isPresent()) {
            var tbuf = optBuf.get();
            int nbuf = tbuf.length;
            int bps = pt.getBitsAllocated().get() / 8;
            if ((pt.getBitsAllocated().get() != 16 || bps != 2) && (pt.getBitsAllocated().get() != 32 || bps != 4))
                throw new DicomException("Only 16 or 32 bit PT pixeldata is supported");
            var optPixelRepresentation = pt.getPixelRepresentation();
            if (optPixelRepresentation.isEmpty()) throw new DicomException("PT requires a valid pixel representation");
            byte[] bb = new byte[nbuf];
            for (int i = 0; i < nbuf; i++) {
                bb[i] = tbuf[i];
            }
            tbuf = null;
            optBuf = Option.empty();
            pt.setPixelData(getPixelData(bb, 0, nbuf, bps, optPixelRepresentation.get(), order));
        }
        return Option.of(pt);
    }

    public static Option<SpatialRegistration> spatialRegistration(Attributes meta, Attributes attr, ByteOrder order) throws IOException {
        if (attr == null) return Option.empty();
        var optMeta = metaHeader(meta);
        SpatialRegistration sr;
        if (optMeta.isEmpty()) {
            sr = new SpatialRegistration();
        } else {
            sr = new SpatialRegistration(optMeta.get());
        }
        sr.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        sr.setInstanceCreationDate(readDate(attr, Tag.InstanceCreationDate));
        sr.setInstanceCreationTime(DicomUtils.tmToLocalTime(readString(attr, Tag.InstanceCreationTime)));
        sr.setSOPClassUID(readString(attr, Tag.SOPClassUID));
        sr.setSOPInstanceUID(readString(attr, Tag.SOPInstanceUID));
        sr.setStudyDate(readDate(attr, Tag.StudyDate));
        sr.setSeriesDate(readDate(attr, Tag.SeriesDate));
        sr.setContentDate(readDate(attr, Tag.ContentDate));
        sr.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        sr.setSeriesTime(DicomUtils.tmToLocalTime(readString(attr, Tag.SeriesTime)));
        sr.setContentTime(DicomUtils.tmToLocalTime(readString(attr, Tag.ContentTime)));
        sr.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        sr.setModality(modality(attr));
        if (sr.getModality().isEmpty() || sr.getModality().get() != Modality.REG) {
            log.error("Trying to read a DICOM file that is not a SR");
            return Option.empty();
        }
        sr.setManufacturer(readString(attr, Tag.Manufacturer));
        sr.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        sr.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        sr.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        sr.setReferencedSeriesSequence(readSequence(attr, Tag.ReferencedSeriesSequence, Reader::referencedSeries));
        sr.setStudiesContainingOtherReferencedInstancesSequence(readSequence(attr, Tag.StudiesContainingOtherReferencedInstancesSequence, Reader::studiesContainingOtherReferencedInstances));

        sr.setPatientName(readString(attr, Tag.PatientName));
        sr.setPatientID(readString(attr, Tag.PatientID));
        sr.setPatientBirthDate(readString(attr, Tag.PatientBirthDate));
        sr.setPatientSex(readString(attr, Tag.PatientSex));
        sr.setSoftwareVersions(readString(attr, Tag.SoftwareVersions));
        sr.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        sr.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        sr.setStudyID(readString(attr, Tag.StudyID));
        sr.setSeriesNumber(readInt(attr, Tag.SeriesNumber));
        sr.setInstanceNumber(readInt(attr, Tag.InstanceNumber));
        sr.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        sr.setPositionReferenceIndicator(readString(attr, Tag.PositionReferenceIndicator));
        sr.setContentLabel(readString(attr, Tag.ContentLabel));
        sr.setContentDescription(readString(attr, Tag.ContentDescription));
        sr.setContentCreatorName(readString(attr, Tag.ContentCreatorName));
        sr.setRegistrationSequence(readSequence(attr, Tag.RegistrationSequence, Reader::registration));
        return Option.of(sr);
    }

    public static Option<RTStructureSet> structureSet(Attributes meta, Attributes attr, ByteOrder order) throws IOException {
        if (attr == null) return Option.empty();
        var optMeta = metaHeader(meta);
        RTStructureSet ss;
        if (optMeta.isEmpty()) {
            ss = new RTStructureSet();
        } else {
            ss = new RTStructureSet(optMeta.get());
        }
        ss.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        ss.setInstanceCreationDate(readDate(attr, Tag.InstanceCreationDate));
        ss.setInstanceCreationTime(DicomUtils.tmToLocalTime(readString(attr, Tag.InstanceCreationTime)));
        ss.setSOPClassUID(readString(attr, Tag.SOPClassUID));
        ss.setSOPInstanceUID(readString(attr, Tag.SOPInstanceUID));
        ss.setStudyDate(readDate(attr, Tag.StudyDate));
        ss.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        ss.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        ss.setModality(modality(attr));
        if (ss.getModality().isEmpty() || ss.getModality().get() != Modality.RTSTRUCT) {
            log.error("Trying to read a DICOM file that is not a RTSTRUCT");
            return Option.empty();
        }
        ss.setManufacturer(readString(attr, Tag.Manufacturer));
        ss.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        ss.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        ss.setOperatorsName(readString(attr, Tag.OperatorsName));
        ss.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        ss.setPatientName(readString(attr, Tag.PatientName));
        ss.setPatientID(readString(attr, Tag.PatientID));
        ss.setPatientBirthDate(readDate(attr, Tag.PatientBirthDate));
        ss.setPatientSex(readString(attr, Tag.PatientSex));
        ss.setSoftwareVersions(readString(attr, Tag.SoftwareVersions));
        ss.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        ss.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        ss.setStudyID(readString(attr, Tag.StudyID));
        ss.setSeriesNumber(readInt(attr, Tag.SeriesNumber));
        ss.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        ss.setPositionReferenceIndicator(readString(attr, Tag.PositionReferenceIndicator));
        ss.setStructureSetLabel(readString(attr, Tag.StructureSetLabel));
        ss.setStructureSetDate(readDate(attr, Tag.StructureSetDate));
        ss.setStructureSetTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StructureSetTime)));
        ss.setReferencedFrameOfReferenceSequence(readSequence(attr, Tag.ReferencedFrameOfReferenceSequence, Reader::referencedFrameOfReference));
        ss.setStructureSetROISequence(readSequence(attr, Tag.StructureSetROISequence, Reader::structureSetROI));
        ss.setRoiContourSequence(readSequence(attr, Tag.ROIContourSequence, Reader::roiContour));
        ss.setRtROIObservationsSequence(readSequence(attr, Tag.RTROIObservationsSequence, Reader::rtROIObservations));
        ss.setApprovalStatus(readString(attr, Tag.ApprovalStatus));
        return Option.of(ss);
    }
}
