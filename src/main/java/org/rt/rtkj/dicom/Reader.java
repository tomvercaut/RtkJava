package org.rt.rtkj.dicom;

import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.util.ByteUtils;
import org.rt.rtkj.utils.ByteTools;

import java.io.IOException;
import java.nio.ByteOrder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class Reader {

    /**
     * Read a sequence from attributes by the corresponding DICOM tag.
     *
     * @param attr     attributes containing the sequence.
     * @param seqTag   DICOM tag of the sequence
     * @param function a function interface that has an sequence item attributes as input and returns an optional value T.
     * @param <T>      type representing the sequence item
     * @return If the sequence is found and contains values a list of sequence items is returned. If the sequence is not found or the attribute is null, an empty optional is returned.
     */
    private static <T> Optional<List<T>> readSequence(Attributes attr, int seqTag, Function<Attributes, Optional<T>> function) {
        if (attr == null || !attr.contains(seqTag)) return Optional.empty();
        Sequence seq = attr.getSequence(seqTag);
        Optional<List<T>> optList = Optional.empty();
        for (Attributes value : seq) {
            var optTmp = function.apply(value);
            if (optTmp.isPresent()) {
                var tmp = optTmp.get();
                if (optList.isEmpty()) optList = Optional.of(new ArrayList<>());
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
    private static Optional<String> readString(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        return Optional.ofNullable(attr.getString(tag));
    }

    /**
     * Read an optional integer from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<Integer> readInt(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        var val = attr.getInt(tag, DicomUtils.UNDEFINED_I32);
        if (val == DicomUtils.UNDEFINED_I32) return Optional.empty();
        return Optional.of(val);
    }

    /**
     * Read an optional double from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<Double> readDouble(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        var val = attr.getDouble(tag, DicomUtils.UNDEFINED_DOUBLE);
        if (val == DicomUtils.UNDEFINED_DOUBLE) return Optional.empty();
        return Optional.of(val);
    }

    /**
     * Read an optional float from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<Float> readFloat(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        var val = attr.getFloat(tag, DicomUtils.UNDEFINED_FLOAT);
        if (val == DicomUtils.UNDEFINED_FLOAT) return Optional.empty();
        return Optional.of(val);
    }

    /**
     * Read an optional array of doubles from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<Double[]> readDoubles(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        var val = attr.getDoubles(tag);
        if (val == null) return Optional.empty();
        return Optional.of((Double[]) Arrays.stream(val).boxed().toArray());
    }

    /**
     * Read an optional list of doubles from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<List<Double>> readListDoubles(Attributes attr, int tag) {
        var optArray = readDoubles(attr, tag);
        if (optArray.isEmpty()) return Optional.empty();
        return Optional.of(Arrays.asList(optArray.get()));
    }

    /**
     * Read an optional array of integers from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<Integer[]> readInts(Attributes attr, int tag) {
        if (attr == null || !attr.containsValue(tag)) return Optional.empty();
        var val = attr.getInts(tag);
        if (val == null) return Optional.empty();
        return Optional.of((Integer[]) Arrays.stream(val).boxed().toArray());
    }

    /**
     * Read an optional Date from attributes by it's corresponding DICOM tag.
     *
     * @param attr attributes optionally containing the DICOM tag
     * @param tag  DICOM tag
     * @return If the DICOM tag is found and has a value, the value is returned. If the attributes is null or the tag doesn't contain a value, an empty optional is returned.
     */
    private static Optional<LocalDate> readDate(Attributes attr, int tag) {
        var optString = readString(attr, tag);
        return DicomUtils.getLocalDateFromString(optString);
    }

    private static Optional<CodeItem> codeItem(Attributes attr) {
        if (attr == null) return Optional.empty();
        CodeItem item = new CodeItem();
        item.setCodeValue(readString(attr, Tag.CodeValue));
        item.setCodingSchemeDesignator(readString(attr, Tag.CodingSchemeDesignator));
        item.setCodeMeaning(readString(attr, Tag.CodeMeaning));
        item.setMappingResource(readString(attr, Tag.MappingResource));
        item.setContextGroupVersion(Optional.ofNullable(DicomUtils.getLocalDateTime(attr.getString(Tag.ContextGroupVersion))));
        item.setContextIdentifier(readString(attr, Tag.ContextIdentifier));
        return Optional.of(item);
    }

    private static Optional<ReferencedSOPClassInstanceItem> referencedSOPClassInstance(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        item.setReferencedSOPClassUID(readString(attr, Tag.ReferencedSOPClassUID));
        item.setReferencedSOPInstanceUID(readString(attr, Tag.ReferencedSOPInstanceUID));
        return Optional.of(item);
    }

    private static Optional<DVHReferencedROIItem> dvhReferencedROI(Attributes attributes) {
        if (attributes == null) return Optional.empty();
        DVHReferencedROIItem item = new DVHReferencedROIItem();
        item.setDvhROIContributionType(Optional.ofNullable(attributes.getString(Tag.DVHROIContributionType)));
        if (attributes.containsValue(Tag.ReferencedROINumber))
            item.setReferencedROINumber(readInt(attributes, Tag.ReferencedROINumber));
        return Optional.of(item);
    }

    private static Optional<EnergyWindowRangeItem> energyWindowRange(Attributes attr) {
        if (attr == null) return Optional.empty();
        EnergyWindowRangeItem item = new EnergyWindowRangeItem();
        item.setEnergyWindowLowerLimit(readDouble(attr, Tag.EnergyWindowLowerLimit));
        item.setEnergyWindowUpperLimit(readDouble(attr, Tag.EnergyWindowUpperLimit));
        return Optional.of(item);
    }

    private static Optional<PatientOrientationCodeItem> patientOrientationCode(Attributes attr) {
        if (attr == null) return Optional.empty();
        Optional<CodeItem> tmpCodeItem = codeItem(attr);
        if (tmpCodeItem.isEmpty()) return Optional.empty();
        PatientOrientationCodeItem item = (PatientOrientationCodeItem) tmpCodeItem.get();
        item.setPatientOrientationModifierCodeSequence(readSequence(attr, Tag.PatientOrientationModifierCodeSequence, Reader::codeItem));
        return Optional.of(item);
    }

    private static Optional<ReducedCodeItem> reducedCode(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReducedCodeItem item = new ReducedCodeItem();
        item.setCodeValue(readString(attr, Tag.CodeValue));
        item.setCodingSchemeDesignator(readString(attr, Tag.CodingSchemeDesignator));
        item.setCodeMeaning(readString(attr, Tag.CodeMeaning));
        return Optional.of(item);
    }

    private static Optional<RequestAttributesItem> requestAttributes(Attributes attr) {
        if (attr == null) return Optional.empty();
        RequestAttributesItem item = new RequestAttributesItem();
        item.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        item.setReferencedStudySequence(readSequence(attr, Tag.ReferencedStudySequence, Reader::referencedSOPClassInstance));
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        item.setRequestedProcedureDescription(readString(attr, Tag.RequestedProcedureDescription));
        item.setRequestedProcedureCodeSequence(readSequence(attr, Tag.RequestedProcedureCodeSequence, Reader::reducedCode));
        item.setScheduledProcedureStepID(readString(attr, Tag.ScheduledProcedureStepID));
        item.setRequestedProcedureID(readString(attr, Tag.RequestedProcedureID));
        return Optional.of(item);
    }

    private static Optional<ReferencedPatientItem> referencedPatient(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedPatientItem item = new ReferencedPatientItem();
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        item.setPurposeOfReferenceCodeSequence(readSequence(attr, Tag.PurposeOfReferenceCodeSequence, Reader::codeItem));
        return Optional.of(item);
    }

    private static Optional<RadiopharmaceuticalInformationItem> radiopharmaceuticalInformation(Attributes attr) {
        if (attr == null) return Optional.empty();
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
                        item.setRadionuclideCodeSequence(Optional.of(new ArrayList<>()));
                    item.getRadionuclideCodeSequence().get().add(tmp);
                });
            }
        }
        return Optional.of(item);
    }

    private static Optional<StudiesContainingOtherReferencedInstancesItem> studiesContainingOtherReferencedInstances(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new StudiesContainingOtherReferencedInstancesItem();
        if (attr.contains(Tag.ReferencedSeriesSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedSeriesSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSeries(value);
                optTmp.ifPresent(tmp -> {
                    if (item.getReferencedSeriesSequence().isEmpty())
                        item.setReferencedSeriesSequence(Optional.of(new ArrayList<>()));
                    item.getReferencedSeriesSequence().get().add(tmp);
                });
            }
        }
        item.setStudyInstanceUID(readString(attr, Tag.StudyInstanceUID));
        return Optional.of(item);
    }

    private static Optional<ReferencedSeriesItem> referencedSeries(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedSeriesItem item = new ReferencedSeriesItem();
        item.setReferencedInstanceSequence(readSequence(attr, Tag.ReferencedInstanceSequence, Reader::referencedSOPClassInstance));
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        return Optional.of(item);
    }

    private static Optional<RegistrationItem> registration(Attributes attr) {
        if (attr == null) return Optional.empty();
        RegistrationItem item = new RegistrationItem();
        item.setReferencedImageSequence(readSequence(attr, Tag.ReferencedImageSequence, Reader::referencedSOPClassInstance));
        item.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        item.setMatrixRegistrationSequence(readSequence(attr, Tag.MatrixRegistrationSequence, Reader::matrixRegistration));
        return Optional.of(item);
    }

    private static Optional<MatrixRegistrationItem> matrixRegistration(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new MatrixRegistrationItem();
        item.setMatrixSequence(readSequence(attr, Tag.MatrixSequence, Reader::matrix));
        return Optional.of(item);
    }

    private static Optional<TransformationMatrixType> frameOfReferenceTransformationMatrixType(Attributes attr) {
        if (attr == null) return Optional.empty();
        if (attr.containsValue(Tag.FrameOfReferenceTransformationMatrixType)) {
            var s = attr.getString(Tag.FrameOfReferenceTransformationMatrixType);
            switch (s) {
                case "RIGID":
                    return Optional.of(TransformationMatrixType.RIGID);
                case "RIGID_SCALE":
                    return Optional.of(TransformationMatrixType.RIGID_SCALE);
                case "AFFINE":
                    return Optional.of(TransformationMatrixType.AFFINE);
                default:
                    return Optional.of(TransformationMatrixType.NONE);
            }
        }
        return Optional.empty();
    }

    private static Optional<MatrixItem> matrix(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new MatrixItem();
        item.setFrameOfReferenceTransformationMatrixType(frameOfReferenceTransformationMatrixType(attr));
        item.setFrameOfReferenceTransformationMatrix(readDoubles(attr, Tag.FrameOfReferenceTransformationMatrix));
        return Optional.of(item);
    }

    private static Optional<RegistrationTypeCodeItem> registrationTypeCode(Attributes attr) {
        return Optional.empty();
    }

    private static Optional<ReferencedFrameOfReferenceItem> referencedFrameOfReference(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedFrameOfReferenceItem item = new ReferencedFrameOfReferenceItem();
        item.setFrameOfReferenceUID(readString(attr, Tag.FrameOfReferenceUID));
        item.setRtReferencedStudySequence(readSequence(attr, Tag.RTReferencedStudySequence, Reader::rtReferencedStudy));
        return Optional.of(item);
    }

    private static Optional<RTReferencedStudyItem> rtReferencedStudy(Attributes attr) {
        if (attr == null) return Optional.empty();
        var optReferencedSOPClassInstance = referencedSOPClassInstance(attr);
        RTReferencedStudyItem item;
        if (optReferencedSOPClassInstance.isEmpty()) {
            item = new RTReferencedStudyItem();
        } else {
            item = new RTReferencedStudyItem(optReferencedSOPClassInstance.get());
        }
        item.setRtReferencedSeriesSequence(readSequence(attr, Tag.RTReferencedSeriesSequence, Reader::rtReferencedSeriesItem));
        return Optional.of(item);
    }

    private static Optional<RTReferencedSeriesItem> rtReferencedSeriesItem(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new RTReferencedSeriesItem();
        item.setSeriesInstanceUID(readString(attr, Tag.SeriesInstanceUID));
        item.setContourImageSequence(readSequence(attr, Tag.ContourImageSequence, Reader::referencedSOPClassInstance));
        return Optional.of(item);
    }

    private static Optional<ROIContourItem> roiContour(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ROIContourItem();
        item.setROIDisplayColor(readInts(attr, Tag.ROIDisplayColor));
        item.setContourSequence(readSequence(attr, Tag.ContourSequence, Reader::contour));
        item.setReferencedROINumber(readInt(attr, Tag.ReferencedROINumber));
        return Optional.of(item);
    }

    private static Optional<ContourItem> contour(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ContourItem();
        item.setContourImageSequence(readSequence(attr, Tag.ContourImageSequence, Reader::referencedSOPClassInstance));
        item.setContourGeometricType(readString(attr, Tag.ContourGeometricType));
        item.setNumberOfContourPoints(readInt(attr, Tag.NumberOfContourPoints));
        item.setContourNumber(readInt(attr, Tag.ContourNumber));
        item.setContourData(readListDoubles(attr, Tag.ContourData));
        return Optional.of(item);
    }

    private static Optional<StructureSetROIItem> structureSetROI(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new StructureSetROIItem();
        item.setROINumber(readInt(attr, Tag.ROINumber));
        item.setReferencedFrameOfReferenceUID(readString(attr, Tag.ReferencedFrameOfReferenceUID));
        item.setROIName(readString(attr, Tag.ROIName));
        item.setROIGenerationAlgorithm(readString(attr, Tag.ROIGenerationAlgorithm));
        return Optional.of(item);
    }

    private static Optional<RTROIObservationsItem> rtROIObservations(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new RTROIObservationsItem();

        item.setObservationNumber(readInt(attr, Tag.ObservationNumber));
        item.setReferencedROINumber(readInt(attr, Tag.ReferencedROINumber));
        item.setROIObservationLabel(readString(attr, Tag.ROIObservationLabel));
        item.setRTROIInterpretedType(readString(attr, Tag.RTROIInterpretedType));
        item.setROIInterpreter(readString(attr, Tag.ROIInterpreter));

        if (attr.contains(Tag.ROIPhysicalPropertiesSequence)) {
            Sequence seq = attr.getSequence(Tag.ROIPhysicalPropertiesSequence);
            for (Attributes value : seq) {
                var optTmp = roiPhysicalProperties(value);
                optTmp.ifPresent(tmp -> item.getROIPhysicalPropertiesSequence().add(tmp));
            }
        }
        item.setMaterialID(readString(attr, Tag.MaterialID));
        return Optional.of(item);
    }

    private static Optional<ROIPhysicalPropertiesItem> roiPhysicalProperties(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ROIPhysicalPropertiesItem();
        item.setROIPhysicalProperty(readString(attr, Tag.ROIPhysicalProperty));
        item.setROIPhysicalPropertyValue(readDouble(attr, Tag.ROIPhysicalPropertyValue));
        if (attr.contains(Tag.ROIElementalCompositionSequence)) {
            Sequence seq = attr.getSequence(Tag.ROIElementalCompositionSequence);
            for (Attributes value : seq) {
                var optTmp = roiElementalComposition(value);
                optTmp.ifPresent(tmp -> item.getROIElementalCompositionSequence().add(tmp));
            }
        }
        return Optional.of(item);
    }

    private static Optional<ROIElementalCompositionItem> roiElementalComposition(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ROIElementalCompositionItem();
        item.setRoiElementalCompositionAtomicNumber(readInt(attr, Tag.ROIElementalCompositionAtomicNumber));
        item.setRoiElementalCompositionAtomicMassFraction(readDouble(attr, Tag.ROIElementalCompositionAtomicMassFraction));
        return Optional.of(item);
    }

    private static PixelRepresentation pixelRepresentation(Attributes attr) {
        if (attr == null) return PixelRepresentation.NONE;
        int val = attr.getInt(Tag.PixelRepresentation, DicomUtils.UNDEFINED_U32);
        if (val == 0) return PixelRepresentation.UNSIGNED;
        else if (val == 1) return PixelRepresentation.TWO_COMPLEMENT;
        return PixelRepresentation.NONE;
    }

    private static PatientPosition patientPosition(Attributes attr) {
        if (attr == null) return PatientPosition.UNKOWN;
        var pp = attr.getString(Tag.PatientPosition, "");
        if (pp.equals("HFS")) return PatientPosition.HFS;
        if (pp.equals("FFS")) return PatientPosition.HFS;
        if (pp.equals("HFP")) return PatientPosition.HFS;
        if (pp.equals("FFP")) return PatientPosition.HFS;
        log.error("Unsupported patient position: " + pp);
        return PatientPosition.UNKOWN;
    }

    private static PhotometricInterpretation photometricInterpretation(Attributes attr) {
        if (attr == null) return PhotometricInterpretation.UNKOWN;
        var val = attr.getString(Tag.PhotometricInterpretation, "");
        switch (val) {
            case "MONOCHROME1":
                return PhotometricInterpretation.MONOCHROME1;
            case "MONOCHROME2":
                return PhotometricInterpretation.MONOCHROME2;
            case "PALETTE COLOR":
                return PhotometricInterpretation.PALETTE_COLOR;
            case "RGB":
                return PhotometricInterpretation.RGB;
            case "HSV":
                return PhotometricInterpretation.RETIRED_HSV;
            case "ARGB":
                return PhotometricInterpretation.RETIRED_ARGB;
            case "CMYK":
                return PhotometricInterpretation.RETIRED_CMYK;
            case "FULL":
                return PhotometricInterpretation.RETIRED_YBR_FULL;
            case "YBR_FULL_422":
                return PhotometricInterpretation.YBR_FULL_422;
            case "YBR_PARTIAL_422":
                return PhotometricInterpretation.YBR_PARTIAL_422;
            case "YBR_PARTIAL_420":
                return PhotometricInterpretation.YBR_PARTIAL_420;
            default:
                return PhotometricInterpretation.UNKOWN;
        }
    }

    private static Modality modality(Attributes attr) {
        if (attr == null) return Modality.UNKNOWN;
        var val = attr.getString(Tag.Modality, "");
        switch (val) {
            case "AR":
                return Modality.AR;
            case "AS":
                return Modality.RETIRED_AS;
            case "ASMT":
                return Modality.ASMT;
            case "AU":
                return Modality.AU;
            case "BDUS":
                return Modality.BDUS;
            case "BI":
                return Modality.BI;
            case "BMD":
                return Modality.BMD;
            case "CD":
                return Modality.RETIRED_CD;
            case "CF":
                return Modality.RETIRED_CF;
            case "CP":
                return Modality.RETIRED_CP;
            case "CR":
                return Modality.CR;
            case "CS":
                return Modality.RETIRED_CS;
            case "CT":
                return Modality.CT;
            case "DD":
                return Modality.RETIRED_DD;
            case "DF":
                return Modality.RETIRED_DF;
            case "DG":
                return Modality.DG;
            case "DM":
                return Modality.RETIRED_DM;
            case "DOC":
                return Modality.DOC;
            case "DS":
                return Modality.RETIRED_DS;
            case "DX":
                return Modality.DX;
            case "EC":
                return Modality.RETIRED_EC;
            case "ECG":
                return Modality.ECG;
            case "EPS":
                return Modality.EPS;
            case "ES":
                return Modality.ES;
            case "FA":
                return Modality.RETIRED_FA;
            case "FID":
                return Modality.FID;
            case "FS":
                return Modality.RETIRED_FS;
            case "GM":
                return Modality.GM;
            case "HC":
                return Modality.HC;
            case "HD":
                return Modality.HD;
            case "IO":
                return Modality.IO;
            case "IOL":
                return Modality.IOL;
            case "IVOCT":
                return Modality.IVOCT;
            case "IVUS":
                return Modality.IVUS;
            case "KER":
                return Modality.KER;
            case "KO":
                return Modality.KO;
            case "LEN":
                return Modality.LEN;
            case "LP":
                return Modality.RETIRED_LP;
            case "LS":
                return Modality.LS;
            case "MA":
                return Modality.RETIRED_MA;
            case "MG":
                return Modality.MG;
            case "MR":
                return Modality.MR;
            case "MS":
                return Modality.RETIRED_MS;
            case "NM":
                return Modality.NM;
            case "OAM":
                return Modality.OAM;
            case "OCT":
                return Modality.OCT;
            case "OP":
                return Modality.OP;
            case "OPM":
                return Modality.OPM;
            case "OPR":
                return Modality.OPR;
            case "OPT":
                return Modality.RETIRED_OPT;
            case "OPV":
                return Modality.OPV;
            case "OSS":
                return Modality.OSS;
            case "OT":
                return Modality.OT;
            case "PLAN":
                return Modality.PLAN;
            case "PR":
                return Modality.PR;
            case "PT":
                return Modality.PT;
            case "PX":
                return Modality.PX;
            case "REG":
                return Modality.REG;
            case "RESP":
                return Modality.RESP;
            case "RF":
                return Modality.RF;
            case "RG":
                return Modality.RG;
            case "RTDOSE":
                return Modality.RTDOSE;
            case "RTIMAGE":
                return Modality.RTIMAGE;
            case "RTPLAN":
                return Modality.RTPLAN;
            case "RTRECORD":
                return Modality.RTRECORD;
            case "RTSTRUCT":
                return Modality.RTSTRUCT;
            case "RWV":
                return Modality.RWV;
            case "SEG":
                return Modality.SEG;
            case "SM":
                return Modality.SM;
            case "SMR":
                return Modality.SMR;
            case "SR":
                return Modality.SR;
            case "SRF":
                return Modality.SRF;
            case "ST":
                return Modality.RETIRED_ST;
            case "STAIN":
                return Modality.STAIN;
            case "TG":
                return Modality.TG;
            case "US":
                return Modality.US;
            case "VA":
                return Modality.VA;
            case "VF":
                return Modality.RETIRED_VF;
            case "XA":
                return Modality.XA;
            case "XC":
                return Modality.XC;
        }
        return Modality.UNKNOWN;
    }

    /**
     * @param buffer byte array
     * @param offset offset to start reading data in the byte array
     * @param length number of bytes to read
     * @param bps    bytes per sample
     * @param order  byteorder
     * @return List with pixel values
     */
    private static List<Long> getPixelData(byte[] buffer, int offset, int length,
                                           int bps, PixelRepresentation pixelRepresentation, ByteOrder order) throws UnsupportedOperationException {
        List<Long> pixels;
        int end = offset + length;
        if (buffer == null || end > buffer.length || order == null) return new ArrayList<>();
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
                    ;
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
        return pixels;
    }

    private static Optional<MetaHeader> metaHeader(Attributes meta) throws IOException {
        if (meta == null) return Optional.empty();
        var hdr = new MetaHeader();
        hdr.setFileMetaInformationGroupLength(meta.getInt(Tag.FileMetaInformationGroupLength, DicomUtils.UNDEFINED_U32));
        hdr.setFileMetaInformationVersion(meta.getBytes(Tag.FileMetaInformationVersion));
        hdr.setMediaStorageSOPClassUID(meta.getString(Tag.MediaStorageSOPClassUID, ""));
        hdr.setMediaStorageSOPInstanceUID(meta.getString(Tag.MediaStorageSOPInstanceUID, ""));
        hdr.setTransferSyntaxUID(meta.getString(Tag.TransferSyntaxUID, ""));
        hdr.setImplementationClassUID(meta.getString(Tag.ImplementationClassUID, ""));
        hdr.setImplementationVersionName(meta.getString(Tag.ImplementationVersionName, ""));
        return Optional.of(hdr);
    }

    private static Optional<DvhItem> dvh(Attributes attr) throws IOException {
        if (attr == null) return Optional.empty();
        DvhItem item = new DvhItem();
        item.setDvhType(readString(attr, Tag.DVHType));
        item.setDoseUnits(readString(attr, Tag.DoseUnits));
        item.setDoseType(readString(attr, Tag.DoseType));
        item.setDvhDoseScaling(readDouble(attr, Tag.DVHDoseScaling));
        item.setDvhVolumeUnits(readString(attr, Tag.DVHVolumeUnits));
        item.setDvhNumberOfBins(readInt(attr, Tag.DVHNumberOfBins));
        item.setDvhData(readDoubles(attr, Tag.DVHData));
        item.getDvhReferencedROISequence().clear();
        if (attr.contains(Tag.DVHReferencedROISequence)) {
            Sequence seq = attr.getSequence(Tag.DVHReferencedROISequence);
            for (Attributes value : seq) {
                var optTmp = dvhReferencedROI(value);
                optTmp.ifPresent(tmp -> item.getDvhReferencedROISequence().add(tmp));
            }
        }
        item.setDvhMinimumDose(readDouble(attr, Tag.DVHMinimumDose));
        item.setDvhMaximumDose(readDouble(attr, Tag.DVHMaximumDose));
        item.setDvhMeanDose(readDouble(attr, Tag.DVHMeanDose));
        return Optional.of(item);
    }

    public static Optional<CT> ct(Attributes meta, Attributes attr, ByteOrder order) throws IOException, DicomException {
        if (attr == null) return Optional.empty();
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
        ct.setImageType(Arrays.stream(readString(attr, Tag.ImageType).split("\\\\")).collect(Collectors.toList()));
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
        if (ct.getModality() != Modality.CT) {
            log.error("Trying to read a DICOM file that is not a CT");
            return Optional.empty();
        }
        ct.setManufacturer(readString(attr, Tag.Manufacturer));
        ct.setInstitutionName(readString(attr, Tag.InstitutionName));
        ct.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        ct.setStationName(readString(attr, Tag.StationName));
        ct.getProcedureCodeSequence().clear();
        if (attr.contains(Tag.ProcedureCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.ProcedureCodeSequence);
            for (Attributes value : seq) {
                var optCode = reducedCode(value);
                optCode.ifPresent(code -> ct.getProcedureCodeSequence().add(code));
            }
        }
        ct.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        ct.setInstitutionalDepartmentName(readString(attr, Tag.InstitutionalDepartmentName));
        ct.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        ct.getReferencedStudySequence().clear();
        if (attr.contains(Tag.ReferencedStudySequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedStudySequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> ct.getReferencedStudySequence().add(tmp));
            }
        }
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
        ct.getPerformedProtocolCodeSequence().clear();
        if (attr.contains(Tag.PerformedProtocolCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PerformedProtocolCodeSequence);
            for (Attributes value : seq) {
                var optTmp = reducedCode(value);
                optTmp.ifPresent(tmp -> ct.getPerformedProtocolCodeSequence().add(tmp));
            }
        }

        byte[] buf = attr.getBytes(Tag.PixelData);
        int nbuf = buf.length;
        int bps = ct.getBitsAllocated() / 8;
        if (ct.getBitsAllocated() != 16 || bps != 2) throw new DicomException("Only 16bit CT pixeldata is supported");
        var optPixelRepresentation = ct.getPixelRepresentation();
        if (optPixelRepresentation.isEmpty()) throw new DicomException("CT requires a valid pixel representation");
        ct.setPixelData(getPixelData(buf, 0, nbuf, bps, optPixelRepresentation.get(), order));
//        for (int i = 0; i < nbuf; i += bps) {
//            int tv = ByteUtils.bytesToShort(buf, i, order == ByteOrder.BIG_ENDIAN);
//            ct.getPixelData().add(tv);
//        }
        return Optional.of(ct);
    }

    public static Optional<RTDose> rtDose(Attributes meta, Attributes attr, ByteOrder order) throws DicomException, IOException {
        if (attr == null) return Optional.empty();
        RTDose rtdose = new RTDose();
        if (meta != null) {
            rtdose.setFileMetaInformationGroupLength(meta.getInt(Tag.FileMetaInformationGroupLength, DicomUtils.UNDEFINED_U32));
            rtdose.setFileMetaInformationVersion(meta.getBytes(Tag.FileMetaInformationVersion));
            rtdose.setMediaStorageSOPClassUID(meta.getString(Tag.MediaStorageSOPClassUID, ""));
            rtdose.setMediaStorageSOPInstanceUID(meta.getString(Tag.MediaStorageSOPInstanceUID, ""));
            rtdose.setTransferSyntaxUID(meta.getString(Tag.TransferSyntaxUID, ""));
            rtdose.setImplementationClassUID(meta.getString(Tag.ImplementationClassUID, ""));
            rtdose.setImplementationVersionName(meta.getString(Tag.ImplementationVersionName, ""));
        }
        rtdose.setSpecificCharacterSet(readString(attr, Tag.SpecificCharacterSet));
        rtdose.setInstanceCreationDate(readDate(attr, Tag.InstanceCreationDate));
        rtdose.setInstanceCreationTime(DicomUtils.tmToLocalTime(readString(attr, Tag.InstanceCreationTime)));
        rtdose.setSopClassUID(readString(attr, Tag.SOPClassUID));
        if (!rtdose.getSopClassUID().equals(UID.RTDoseStorage)) return Optional.empty();
        rtdose.setSopInstanceUID(attr.getString(Tag.SOPInstanceUID));
        rtdose.setStudyDate(readDate(attr, Tag.StudyDate));
        rtdose.setStudyTime(DicomUtils.tmToLocalTime(readString(attr, Tag.StudyTime)));
        rtdose.setAccessionNumber(readString(attr, Tag.AccessionNumber));
        rtdose.setModality(modality(attr));
        if (!rtdose.getModality().equals(Modality.RTDOSE)) {
            log.error("Trying to read a DICOM file that is not a RTDOSE");
            return Optional.empty();
        }
        rtdose.setManufacturer(readString(attr, Tag.Manufacturer));
        rtdose.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        rtdose.setStationName(readString(attr, Tag.StationName));
        rtdose.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        rtdose.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));
        rtdose.setPatientName(readString(attr, Tag.PatientName));
        rtdose.setPatientID(readString(attr, Tag.PatientID));
        if (attr.contains(Tag.PatientBirthDate))
            rtdose.setPatientBirthDate(DicomUtils.getLocalDate(attr.getDate(Tag.PatientBirthDate)));
        else
            rtdose.setPatientBirthDate(null);
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
        rtdose.getDvhSequence().clear();
        if (attr.contains(Tag.DVHSequence)) {
            Sequence seq = attr.getSequence(Tag.DVHSequence);
            for (Attributes value : seq) {
                var optTmp = dvh(value);
                optTmp.ifPresent(tmp -> rtdose.getDvhSequence().add(tmp));
            }
        }
        rtdose.getReferencedRTPlanSequence().clear();
        if (attr.contains(Tag.ReferencedRTPlanSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedRTPlanSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> rtdose.getReferencedRTPlanSequence().add(tmp));
            }
        }
        rtdose.getReferencedStructureSetSequence().clear();
        if (attr.contains(Tag.ReferencedStructureSetSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedStructureSetSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> rtdose.getReferencedStructureSetSequence().add(tmp));
            }
        }

        if (rtdose.getPhotometricInterpretation() != PhotometricInterpretation.MONOCHROME2) {
            throw new DicomException("In RTDose files the supported photometric interpretation is MONOCHROME2, not " +
                    rtdose.getPhotometricInterpretation());
        }

        byte[] buf = attr.getBytes(Tag.PixelData);
        int nbuf = buf.length;
        int bps = rtdose.getBitsAllocated() / 8;
        if ((rtdose.getBitsAllocated() != 16 || bps != 2) && (rtdose.getBitsAllocated() != 32 || bps != 4))
            throw new DicomException("Only 16 or 32 bit pixeldata is supported");
        rtdose.setPixelData(getPixelData(buf, 0, nbuf, bps, rtdose.getPixelRepresentation(), order));
//        for (int i = 0; i < nbuf; i += bps) {
//            int tv = ByteUtils.bytesToShort(buf, i, order == ByteOrder.BIG_ENDIAN);
//            rtdose.getPixelData().add(tv);
//        }

//        byte[] buf = attr.getBytes(Tag.PixelData);
//        int nbuf = buf.length;
//        int bps = rtdose.getBitsAllocated() / 8;
//        if(rtdose.getBitsAllocated() != 16 || bps != 2) throw new DicomException("Only 16bit CT pixeldata is supported");
//        for (int i = 0; i < nbuf; i += bps) {
//            int tv = ByteUtils.bytesToShort(buf, i, order == ByteOrder.BIG_ENDIAN);
//            rtdose.getPixelData().add(tv);
//        }
        return Optional.of(rtdose);
    }

    public static Optional<PT> pt(Attributes meta, Attributes attr, ByteOrder order) throws IOException, DicomException {
        if (attr == null) return Optional.empty();
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
        if (pt.getModality() != Modality.PT) {
            log.error("Trying to read a DICOM file that is not a PT");
            return Optional.empty();
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
        pt.getReferencedPatientSequence().clear();
        if (attr.contains(Tag.ReferencedPatientSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedPatientSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> pt.getReferencedPatientSequence().add(tmp));
            }
        }
        pt.getPurposeOfReferenceCodeSequence().clear();
        if (attr.contains(Tag.PurposeOfReferenceCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PurposeOfReferenceCodeSequence);
            for (Attributes value : seq) {
                var optTmp = codeItem(value);
                optTmp.ifPresent(tmp -> pt.getPurposeOfReferenceCodeSequence().add(tmp));
            }
        }
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
        pt.setCorrectedImage(Arrays.stream(readString(attr, Tag.CorrectedImage).split("\\\\")).collect(Collectors.toList()));
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
        pt.getRequestAttributesSequence().clear();
        if (attr.contains(Tag.RequestAttributesSequence)) {
            Sequence seq = attr.getSequence(Tag.RequestAttributesSequence);
            for (Attributes value : seq) {
                var optTmp = requestAttributes(value);
                optTmp.ifPresent(tmp -> pt.getRequestAttributesSequence().add(tmp));
            }
        }
        pt.setRequestedProcedureID(readString(attr, Tag.RequestedProcedureID));
        pt.getEnergyWindowRangeSequence().clear();
        if (attr.contains(Tag.EnergyWindowRangeSequence)) {
            Sequence seq = attr.getSequence(Tag.EnergyWindowRangeSequence);
            for (Attributes value : seq) {
                var optTmp = energyWindowRange(value);
                optTmp.ifPresent(tmp -> pt.getEnergyWindowRangeSequence().add(tmp));
            }
        }
        pt.getRadiopharmaceuticalInformationSequence().clear();
        if (attr.contains(Tag.RadiopharmaceuticalInformationSequence)) {
            Sequence seq = attr.getSequence(Tag.RadiopharmaceuticalInformationSequence);
            for (Attributes value : seq) {
                var optTmp = radiopharmaceuticalInformation(value);
                optTmp.ifPresent(tmp -> pt.getRadiopharmaceuticalInformationSequence().add(tmp));
            }
        }
        pt.setNumberOfSlices(readInt(attr, Tag.NumberOfSlices));
        pt.getPatientOrientationCodeSequence().clear();
        if (attr.contains(Tag.PatientOrientationCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PatientOrientationCodeSequence);
            for (Attributes value : seq) {
                var optTmp = patientOrientationCode(value);
                optTmp.ifPresent(tmp -> pt.getPatientOrientationCodeSequence().add(tmp));
            }
        }
        pt.getPatientGantryRelationshipCodeSequence().clear();
        if (attr.contains(Tag.PatientGantryRelationshipCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PatientGantryRelationshipCodeSequence);
            for (Attributes value : seq) {
                var optTmp = codeItem(value);
                optTmp.ifPresent(tmp -> pt.getPatientGantryRelationshipCodeSequence().add(tmp));
            }
        }
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

        byte[] buf = attr.getBytes(Tag.PixelData);
        int nbuf = buf.length;
        int bps = pt.getBitsAllocated() / 8;
        if ((pt.getBitsAllocated() != 16 || bps != 2) && (pt.getBitsAllocated() != 32 || bps != 4))
            throw new DicomException("Only 16 or 32 bit pixeldata is supported");
        var optPixelRepresentation = pt.getPixelRepresentation();
        if (optPixelRepresentation.isEmpty()) throw new DicomException("CT requires a valid pixel representation");
        pt.setPixelData(getPixelData(buf, 0, nbuf, bps, optPixelRepresentation.get(), order));
//        for (int i = 0; i < nbuf; i += bps) {
//            var bb = ByteBuffer.wrap(buf, i, bps);
//            pt.getPixelData().add(bb.getInt());
//        }

        return Optional.of(pt);
    }

    public static Optional<SpatialRegistration> spatialRegistration(Attributes meta, Attributes attr, ByteOrder order) throws IOException {
        if (attr == null) return Optional.empty();
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
        if (sr.getModality() != Modality.REG) {
            log.error("Trying to read a DICOM file that is not a SR");
            return Optional.empty();
        }
        sr.setManufacturer(readString(attr, Tag.Manufacturer));
        sr.setReferringPhysicianName(readString(attr, Tag.ReferringPhysicianName));
        sr.setSeriesDescription(readString(attr, Tag.SeriesDescription));
        sr.setManufacturerModelName(readString(attr, Tag.ManufacturerModelName));

        sr.getReferencedSeriesSequence().clear();
        if (attr.contains(Tag.ReferencedSeriesSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedSeriesSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSeries(value);
                optTmp.ifPresent(tmp -> sr.getReferencedSeriesSequence().add(tmp));
            }
        }

        sr.getStudiesContainingOtherReferencedInstancesSequence().clear();
        if (attr.contains(Tag.StudiesContainingOtherReferencedInstancesSequence)) {
            Sequence seq = attr.getSequence(Tag.StudiesContainingOtherReferencedInstancesSequence);
            for (Attributes value : seq) {
                var optTmp = studiesContainingOtherReferencedInstances(value);
                optTmp.ifPresent(tmp -> sr.getStudiesContainingOtherReferencedInstancesSequence().add(tmp));
            }
        }

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
        sr.getRegistrationSequence().clear();
        if (attr.contains(Tag.RegistrationSequence)) {
            Sequence seq = attr.getSequence(Tag.RegistrationSequence);
            for (Attributes value : seq) {
                var optTmp = registration(value);
                optTmp.ifPresent(tmp -> sr.getRegistrationSequence().add(tmp));
            }
        }
        return Optional.of(sr);
    }

    public static Optional<RTStructureSet> structureSet(Attributes meta, Attributes attr, ByteOrder order) throws IOException {
        if (attr == null) return Optional.empty();
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
        if (ss.getModality() != Modality.RTSTRUCT) {
            log.error("Trying to read a DICOM file that is not a RTSTRUCT");
            return Optional.empty();
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
        if (attr.contains(Tag.ReferencedFrameOfReferenceSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedFrameOfReferenceSequence);
            for (Attributes value : seq) {
                var optTmp = referencedFrameOfReference(value);
                optTmp.ifPresent(tmp -> ss.getReferencedFrameOfReferenceSequence().add(tmp));
            }
        }
        if (attr.contains(Tag.StructureSetROISequence)) {
            Sequence seq = attr.getSequence(Tag.StructureSetROISequence);
            for (Attributes value : seq) {
                var optTmp = structureSetROI(value);
                optTmp.ifPresent(tmp -> ss.getStructureSetROISequence().add(tmp));
            }
        }

        if (attr.contains(Tag.ROIContourSequence)) {
            Sequence seq = attr.getSequence(Tag.ROIContourSequence);
            for (Attributes value : seq) {
                var optTmp = roiContour(value);
                optTmp.ifPresent(tmp -> ss.getRoiContourSequence().add(tmp));
            }
        }

        if (attr.contains(Tag.RTROIObservationsSequence)) {
            Sequence seq = attr.getSequence(Tag.RTROIObservationsSequence);
            for (Attributes value : seq) {
                var optTmp = rtROIObservations(value);
                optTmp.ifPresent(tmp -> ss.getRtROIObservationsSequence().add(tmp));
            }
        }
        ss.setApprovalStatus(readString(attr, Tag.ApprovalStatus));
        return Optional.of(ss);
    }
}
