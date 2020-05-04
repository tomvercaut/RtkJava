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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class Reader {
    private static Optional<CodeItem> codeItem(Attributes attr) {
        if (attr == null) return Optional.empty();
        CodeItem item = new CodeItem();
        item.setCodeValue(attr.getString(Tag.CodeValue, ""));
        item.setCodingSchemeDesignator(attr.getString(Tag.CodingSchemeDesignator, ""));
        item.setCodeMeaning(attr.getString(Tag.CodeMeaning, ""));
        item.setMappingResource(attr.getString(Tag.MappingResource, ""));
        item.setContextGroupVersion(DicomUtils.getLocalDateTime(attr.getString(Tag.ContextGroupVersion, "")));
        item.setContextIdentifier(attr.getString(Tag.ContextIdentifier, ""));
        return Optional.of(item);
    }

    private static Optional<ReferencedSOPClassInstanceItem> referencedSOPClassInstance(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedSOPClassInstanceItem item = new ReferencedSOPClassInstanceItem();
        item.setReferencedSOPClassUID(attr.getString(Tag.ReferencedSOPClassUID, ""));
        item.setReferencedSOPInstanceUID(attr.getString(Tag.ReferencedSOPInstanceUID, ""));
        return Optional.of(item);
    }

    private static Optional<DVHReferencedROIItem> dvhReferencedROI(Attributes attributes) {
        if (attributes == null) return Optional.empty();
        DVHReferencedROIItem item = new DVHReferencedROIItem();
        item.setDvhROIContributionType(attributes.getString(Tag.DVHROIContributionType, ""));
        item.setReferencedROINumber(attributes.getInt(Tag.ReferencedROINumber, DicomUtils.UNDEFINED_U32));
        return Optional.of(item);
    }

    private static Optional<EnergyWindowRangeItem> energyWindowRange(Attributes attr) {
        if (attr == null) return Optional.empty();
        EnergyWindowRangeItem item = new EnergyWindowRangeItem();
        item.setEnergyWindowLowerLimit(attr.getDouble(Tag.EnergyWindowLowerLimit, 0.0));
        item.setEnergyWindowUpperLimit(attr.getDouble(Tag.EnergyWindowUpperLimit, 0.0));
        return Optional.of(item);
    }

    private static Optional<PatientOrientationCodeItem> patientOrientationCode(Attributes attr) {
        if (attr == null) return Optional.empty();
        Optional<CodeItem> tmpCodeItem = codeItem(attr);
        if (tmpCodeItem.isEmpty()) return Optional.empty();
        PatientOrientationCodeItem item = (PatientOrientationCodeItem) tmpCodeItem.get();
        if (attr.contains(Tag.PatientOrientationModifierCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PatientOrientationModifierCodeSequence);
            for (Attributes value : seq) {
                var optCode = codeItem(value);
                optCode.ifPresent(code -> item.getPatientOrientationModifierCodeSequence().add(code));
            }
        }
        return Optional.of(item);
    }

    private static Optional<ReducedCodeItem> reducedCode(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReducedCodeItem item = new ReducedCodeItem();
        item.setCodeValue(attr.getString(Tag.CodeValue, ""));
        item.setCodingSchemeDesignator(attr.getString(Tag.CodingSchemeDesignator, ""));
        item.setCodeMeaning(attr.getString(Tag.CodeMeaning, ""));
        return Optional.of(item);
    }

    private static Optional<RequestAttributesItem> requestAttributes(Attributes attr) {
        if (attr == null) return Optional.empty();
        RequestAttributesItem item = new RequestAttributesItem();
        item.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        if (attr.contains(Tag.ReferencedStudySequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedStudySequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> item.getReferencedStudySequence().add(tmp));
            }
            ;
        }
        item.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        item.setRequestedProcedureDescription(attr.getString(Tag.RequestedProcedureDescription, ""));
        if (attr.contains(Tag.RequestedProcedureCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.RequestedProcedureCodeSequence);
            for (Attributes value : seq) {
                var optTmp = reducedCode(value);
                optTmp.ifPresent(tmp -> item.getRequestedProcedureCodeSequence().add(tmp));
            }
        }
        item.setScheduledProcedureStepID(attr.getString(Tag.ScheduledProcedureStepID, ""));
        item.setRequestedProcedureID(attr.getString(Tag.RequestedProcedureID, ""));
        return Optional.of(item);
    }

    private static Optional<ReferencedPatientItem> referencedPatient(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedPatientItem item = new ReferencedPatientItem();
        item.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        item.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        if (attr.contains(Tag.PurposeOfReferenceCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.PurposeOfReferenceCodeSequence);
            for (Attributes value : seq) {
                var optCode = codeItem(value);
                optCode.ifPresent(code -> item.getPurposeOfReferenceCodeSequence().add(code));
            }
        }
        return Optional.of(item);
    }

    private static Optional<RadiopharmaceuticalInformationItem> radiopharmaceuticalInformation(Attributes attr) {
        if (attr == null) return Optional.empty();
        RadiopharmaceuticalInformationItem item = new RadiopharmaceuticalInformationItem();
        item.setRadiopharmaceutical(attr.getString(Tag.Radiopharmaceutical, ""));
        item.setRadiopharmaceuticalStartTime(DicomUtils.tmToLocalTime(attr.getString(Tag.RadiopharmaceuticalStartTime, "")));
        item.setRadionuclideTotalDose(attr.getDouble(Tag.RadionuclideTotalDose, 0.0));
        item.setRadionuclideHalfLife(attr.getDouble(Tag.RadionuclideHalfLife, 0.0));
        item.setRadionuclidePositronFraction(attr.getDouble(Tag.RadionuclidePositronFraction, 0.0));
        item.setRadiopharmaceuticalStartDateTime(DicomUtils.getLocalDateTime(attr.getString(Tag.RadiopharmaceuticalStartDateTime)));
        if (attr.contains(Tag.RadionuclideCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.RadionuclideCodeSequence);
            for (Attributes value : seq) {
                var optTmp = codeItem(value);
                optTmp.ifPresent(tmp -> item.getRadionuclideCodeSequence().add(tmp));
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
                optTmp.ifPresent(tmp -> item.getReferencedSeriesSequence().add(tmp));
            }
        }
        item.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        return Optional.of(item);
    }

    private static Optional<ReferencedSeriesItem> referencedSeries(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedSeriesItem item = new ReferencedSeriesItem();
        if (attr.contains(Tag.ReferencedInstanceSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedInstanceSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> item.getReferencedInstanceSequence().add(tmp));
            }
        }
        item.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        return Optional.of(item);
    }

    private static Optional<RegistrationItem> registration(Attributes attr) {
        if (attr == null) return Optional.empty();
        RegistrationItem item = new RegistrationItem();

        if (attr.contains(Tag.ReferencedImageSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedImageSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> item.getReferencedImageSequence().add(tmp));
            }
        }
        item.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));


        if (attr.contains(Tag.MatrixRegistrationSequence)) {
            Sequence seq = attr.getSequence(Tag.MatrixRegistrationSequence);
            for (Attributes value : seq) {
                var optTmp = matrixRegistration(value);
                optTmp.ifPresent(tmp -> item.getMatrixRegistrationSequence().add(tmp));
            }
        }

        return Optional.of(item);
    }

    private static Optional<MatrixRegistrationItem> matrixRegistration(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new MatrixRegistrationItem();
        if (attr.contains(Tag.MatrixSequence)) {
            Sequence seq = attr.getSequence(Tag.MatrixSequence);
            for (Attributes value : seq) {
                var optTmp = matrix(value);
                optTmp.ifPresent(tmp -> item.getMatrixSequence().add(tmp));
            }
        }
        return Optional.of(item);
    }

    private static TransformationMatrixType frameOfReferenceTransformationMatrixType(Attributes attr) {
        if (attr == null) return TransformationMatrixType.NONE;
        if (attr.contains(Tag.FrameOfReferenceTransformationMatrixType)) {
            var s = attr.getString(Tag.FrameOfReferenceTransformationMatrixType);
            switch (s) {
                case "RIGID":
                    return TransformationMatrixType.RIGID;
                case "RIGID_SCALE":
                    return TransformationMatrixType.RIGID_SCALE;
                case "AFFINE":
                    return TransformationMatrixType.AFFINE;
                default:
                    return TransformationMatrixType.NONE;
            }
        }
        return TransformationMatrixType.NONE;
    }

    private static Optional<MatrixItem> matrix(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new MatrixItem();
        item.setFrameOfReferenceTransformationMatrixType(frameOfReferenceTransformationMatrixType(attr));
        item.setFrameOfReferenceTransformationMatrix(attr.getDoubles(Tag.FrameOfReferenceTransformationMatrix));
        return Optional.of(item);
    }

    private static Optional<RegistrationTypeCodeItem> registrationTypeCode(Attributes attr) {
        return Optional.empty();
    }

    private static Optional<ReferencedFrameOfReferenceItem> referencedFrameOfReference(Attributes attr) {
        if (attr == null) return Optional.empty();
        ReferencedFrameOfReferenceItem item = new ReferencedFrameOfReferenceItem();
        item.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        if (attr.contains(Tag.RTReferencedStudySequence)) {
            Sequence seq = attr.getSequence(Tag.RTReferencedStudySequence);
            for (Attributes value : seq) {
                var optTmp = rtReferencedStudy(value);
                optTmp.ifPresent(tmp -> item.getRtReferencedStudySequence().add(tmp));
            }
        }
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
        if (attr.contains(Tag.RTReferencedSeriesSequence)) {
            Sequence seq = attr.getSequence(Tag.RTReferencedSeriesSequence);
            for (Attributes value : seq) {
                var optTmp = rtReferencedSeriesItem(value);
                optTmp.ifPresent(tmp -> item.getRtReferencedSeriesSequence().add(tmp));
            }
        }
        return Optional.of(item);
    }

    private static Optional<RTReferencedSeriesItem> rtReferencedSeriesItem(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new RTReferencedSeriesItem();
        item.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        if (attr.contains(Tag.ContourImageSequence)) {
            Sequence seq = attr.getSequence(Tag.ContourImageSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> item.getContourImageSequence().add(tmp));
            }
        }
        return Optional.of(item);
    }

    private static Optional<ROIContourItem> roiContour(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ROIContourItem();
        item.setROIDisplayColor(attr.getInts(Tag.ROIDisplayColor));
        if (attr.contains(Tag.ContourSequence)) {
            Sequence seq = attr.getSequence(Tag.ContourSequence);
            for (Attributes value : seq) {
                var optTmp = contour(value);
                optTmp.ifPresent(tmp -> item.getContourSequence().add(tmp));
            }
        }
        item.setReferencedROINumber(attr.getInt(Tag.ReferencedROINumber, DicomUtils.UNDEFINED_U32));
        return Optional.of(item);
    }

    private static Optional<ContourItem> contour(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ContourItem();
        if (attr.contains(Tag.ContourImageSequence)) {
            Sequence seq = attr.getSequence(Tag.ContourImageSequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> item.getContourImageSequence().add(tmp));
            }
        }
        item.setContourGeometricType(attr.getString(Tag.ContourGeometricType, ""));
        item.setNumberOfContourPoints(attr.getInt(Tag.NumberOfContourPoints, DicomUtils.UNDEFINED_U32));
        item.setContourNumber(attr.getInt(Tag.ContourNumber, DicomUtils.UNDEFINED_U32));
        item.setContourData(Arrays.stream(attr.getDoubles(Tag.ContourData)).boxed().collect(Collectors.toList()));
        return Optional.of(item);
    }

    private static Optional<StructureSetROIItem> structureSetROI(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new StructureSetROIItem();
        item.setROINumber(attr.getInt(Tag.ROINumber, DicomUtils.UNDEFINED_U32));
        item.setReferencedFrameOfReferenceUID(attr.getString(Tag.ReferencedFrameOfReferenceUID, ""));
        item.setROIName(attr.getString(Tag.ROIName, ""));
        item.setROIGenerationAlgorithm(attr.getString(Tag.ROIGenerationAlgorithm, ""));
        return Optional.of(item);
    }

    private static Optional<RTROIObservationsItem> rtROIObservations(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new RTROIObservationsItem();

        item.setObservationNumber(attr.getInt(Tag.ObservationNumber, DicomUtils.UNDEFINED_U32));
        item.setReferencedROINumber(attr.getInt(Tag.ReferencedROINumber, DicomUtils.UNDEFINED_U32));
        item.setROIObservationLabel(attr.getString(Tag.ROIObservationLabel, ""));
        item.setRTROIInterpretedType(attr.getString(Tag.RTROIInterpretedType, ""));
        item.setROIInterpreter(attr.getString(Tag.ROIInterpreter, ""));

        if (attr.contains(Tag.ROIPhysicalPropertiesSequence)) {
            Sequence seq = attr.getSequence(Tag.ROIPhysicalPropertiesSequence);
            for (Attributes value : seq) {
                var optTmp = roiPhysicalProperties(value);
                optTmp.ifPresent(tmp -> item.getROIPhysicalPropertiesSequence().add(tmp));
            }
        }
        item.setMaterialID(attr.getString(Tag.MaterialID, ""));
        return Optional.of(item);
    }

    private static Optional<ROIPhysicalPropertiesItem> roiPhysicalProperties(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ROIPhysicalPropertiesItem();
        item.setROIPhysicalProperty(attr.getString(Tag.ROIPhysicalProperty, ""));
        item.setROIPhysicalPropertyValue(attr.getDouble(Tag.ROIPhysicalPropertyValue, DicomUtils.UNDEFINED_DOUBLE));
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
        item.setRoiElementalCompositionAtomicNumber(attr.getInt(Tag.ROIElementalCompositionAtomicNumber, DicomUtils.UNDEFINED_U32));
        item.setRoiElementalCompositionAtomicMassFraction(attr.getDouble(Tag.ROIElementalCompositionAtomicMassFraction, DicomUtils.UNDEFINED_DOUBLE));
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

    private static RotationDirection rotationDirection(String s) {
        if (s == null) return RotationDirection.None;
        if (s.equals("CC")) return RotationDirection.CC;
        if (s.equals("CW")) return RotationDirection.CW;
        return RotationDirection.None;
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

    private static Optional<TreatmentSessionBeamItem> treatmentSessionBeamstructureSetROI(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new TreatmentSessionBeamItem();
        item.setCurrentFractionNumber(attr.getInt(Tag.CurrentFractionNumber, DicomUtils.UNDEFINED_U32));
        item.setTreatmentTerminationStatus(attr.getString(Tag.TreatmentTerminationStatus, ""));
        item.setTreatmentTerminationCode(attr.getString(Tag.TreatmentTerminationCode, ""));
        item.setTreatmentVerificationStatus(attr.getString(Tag.TreatmentVerificationStatus, ""));
        item.setDeliveredTreatmentTime(attr.getDouble(Tag.DeliveredTreatmentTime, DicomUtils.UNDEFINED_DOUBLE));
        if (attr.contains(Tag.ControlPointDeliverySequence)) {
            Sequence seq = attr.getSequence(Tag.ControlPointDeliverySequence);
            for (Attributes value : seq) {
                var optTmp = controlPointDelivery(value);
                optTmp.ifPresent(tmp -> item.getControlPointDeliverySequence().add(tmp));
            }
        }
        if (attr.contains(Tag.ReferencedCalculatedDoseReferenceSequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedCalculatedDoseReferenceSequence);
            for (Attributes value : seq) {
                var optTmp = referencedCalculatedDoseReference(value);
                optTmp.ifPresent(tmp -> item.getReferencedCalculatedDoseReferenceSequence().add(tmp));
            }
        }
        item.setSourceAxisDistance(attr.getDouble(Tag.SourceAxisDistance, DicomUtils.UNDEFINED_DOUBLE));
        item.setBeamName(attr.getString(Tag.BeamName, ""));
        item.setBeamType(attr.getString(Tag.BeamType, ""));
        item.setRadiationType(attr.getString(Tag.RadiationType, ""));
        item.setTreatmentDeliveryType(attr.getString(Tag.TreatmentDeliveryType, ""));
        item.setNumberOfWedges(attr.getInt(Tag.NumberOfWedges, DicomUtils.UNDEFINED_I32));
        item.setNumberOfCompensators(attr.getInt(Tag.NumberOfCompensators, DicomUtils.UNDEFINED_I32));
        item.setNumberOfBoli(attr.getInt(Tag.NumberOfBoli, DicomUtils.UNDEFINED_I32));
        item.setNumberOfBlocks(attr.getInt(Tag.NumberOfBlocks, DicomUtils.UNDEFINED_I32));
        item.setNumberOfControlPoints(attr.getInt(Tag.NumberOfControlPoints, DicomUtils.UNDEFINED_I32));
        item.setReferencedBeamNumber(attr.getInt(Tag.ReferencedBeamNumber, DicomUtils.UNDEFINED_I32));
        return Optional.of(item);
    }

    private static Optional<ReferencedCalculatedDoseReferenceItem> referencedCalculatedDoseReference(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ReferencedCalculatedDoseReferenceItem();
        item.setCalculatedDoseReferenceDoseValue(attr.getDouble(Tag.CalculatedDoseReferenceDoseValue, DicomUtils.UNDEFINED_DOUBLE));
        item.setReferencedDoseReferenceNumber(attr.getInt(Tag.ReferencedDoseReferenceNumber, DicomUtils.UNDEFINED_I32));
        return Optional.of(item);
    }

    private static Optional<ControlPointDeliveryItem> controlPointDelivery(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new ControlPointDeliveryItem();
        item.setTreatmentControlPointDate(attr.getDate(Tag.TreatmentControlPointDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        item.setTreatmentControlPointTime(DicomUtils.tmToLocalTime(attr.getString(Tag.TreatmentControlPointTime, "")));
        item.setSpecifiedMeterset(attr.getString(Tag.SpecifiedMeterset, ""));
        item.setDeliveredMeterset(attr.getString(Tag.DeliveredMeterset, ""));
        item.setDoseRateDelivered(attr.getString(Tag.DoseRateDelivered, ""));
        item.setNominalBeamEnergyUnit(attr.getString(Tag.NominalBeamEnergyUnit, ""));
        item.setNominalBeamEnergy(attr.getString(Tag.NominalBeamEnergy, ""));
        item.setDoseRateSet(attr.getString(Tag.DoseRateSet, ""));
        if (attr.contains(Tag.BeamLimitingDevicePositionSequence)) {
            Sequence seq = attr.getSequence(Tag.BeamLimitingDevicePositionSequence);
            for (Attributes value : seq) {
                var optTmp = beamLimitingDevicePosition(value);
                optTmp.ifPresent(tmp -> item.getBeamLimitingDevicePositionSequence().add(tmp));
            }
        }
        item.setGantryAngle(attr.getDouble(Tag.GantryAngle, DicomUtils.UNDEFINED_DOUBLE));
        item.setGantryRotationDirection(rotationDirection(attr.getString(Tag.GantryRotationDirection, "")));
        item.setBeamLimitingDeviceAngle(attr.getDouble(Tag.BeamLimitingDeviceAngle, DicomUtils.UNDEFINED_DOUBLE));
        item.setBeamLimitingDeviceRotationDirection(rotationDirection(attr.getString(Tag.BeamLimitingDeviceRotationDirection, "")));
        item.setPatientSupportAngle(attr.getDouble(Tag.PatientSupportAngle, DicomUtils.UNDEFINED_DOUBLE));
        item.setPatientSupportRotationDirection(rotationDirection(attr.getString(Tag.PatientSupportRotationDirection, "")));
        item.setTableTopEccentricAxisDistance(attr.getDouble(Tag.TableTopEccentricAxisDistance, DicomUtils.UNDEFINED_DOUBLE));
        item.setTableTopEccentricAngle(attr.getDouble(Tag.TableTopEccentricAngle, DicomUtils.UNDEFINED_DOUBLE));
        item.setTableTopEccentricRotationDirection(rotationDirection(attr.getString(Tag.TableTopEccentricRotationDirection, "")));
        item.setTableTopVerticalPosition(attr.getDouble(Tag.TableTopVerticalPosition, DicomUtils.UNDEFINED_DOUBLE));
        item.setTableTopLongitudinalPosition(attr.getDouble(Tag.TableTopLongitudinalPosition, DicomUtils.UNDEFINED_DOUBLE));
        item.setTableTopLateralPosition(attr.getDouble(Tag.TableTopLateralPosition, DicomUtils.UNDEFINED_DOUBLE));
        item.setReferencedControlPointIndex(attr.getInt(Tag.ReferencedControlPointIndex, DicomUtils.UNDEFINED_I32));
        return Optional.of(item);
    }

    private static Optional<BeamLimitingDevicePositionItem> beamLimitingDevicePosition(Attributes attr) {
        if (attr == null) return Optional.empty();
        var item = new BeamLimitingDevicePositionItem();
        item.setRtBeamLimitingDeviceType(attr.getString(Tag.RTBeamLimitingDeviceType, ""));
        item.setLeafJawPositions(Arrays.stream(attr.getDoubles(Tag.LeafJawPositions)).boxed().collect(Collectors.toList()));
        return Optional.of(item);
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
        item.setDvhType(attr.getString(Tag.DVHType, ""));
        item.setDoseUnits(attr.getString(Tag.DoseUnits, ""));
        item.setDoseType(attr.getString(Tag.DoseType, ""));
        item.setDvhDoseScaling(attr.getDouble(Tag.DVHDoseScaling, 0.0));
        item.setDvhVolumeUnits(attr.getString(Tag.DVHVolumeUnits, ""));
        item.setDvhNumberOfBins(attr.getInt(Tag.DVHNumberOfBins, DicomUtils.UNDEFINED_U32));
        item.setDvhData(attr.getDoubles(Tag.DVHData));
        item.getDvhReferencedROISequence().clear();
        if (attr.contains(Tag.DVHReferencedROISequence)) {
            Sequence seq = attr.getSequence(Tag.DVHReferencedROISequence);
            for (Attributes value : seq) {
                var optTmp = dvhReferencedROI(value);
                optTmp.ifPresent(tmp -> item.getDvhReferencedROISequence().add(tmp));
            }
        }
        item.setDvhMinimumDose(attr.getDouble(Tag.DVHMinimumDose, DicomUtils.UNDEFINED_DOUBLE));
        item.setDvhMaximumDose(attr.getDouble(Tag.DVHMaximumDose, DicomUtils.UNDEFINED_DOUBLE));
        item.setDvhMeanDose(attr.getDouble(Tag.DVHMeanDose, DicomUtils.UNDEFINED_DOUBLE));
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
        ct.setSpecificCharacterSet(attr.getString(Tag.SpecificCharacterSet, ""));
        ct.setImageType(Arrays.stream(attr.getString(Tag.ImageType, "").split("\\\\")).collect(Collectors.toList()));
        ct.setSOPClassUID(attr.getString(Tag.SOPClassUID, ""));
        ct.setSOPInstanceUID(attr.getString(Tag.SOPInstanceUID, ""));
        ct.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setSeriesDate(attr.getDate(Tag.SeriesDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setAcquisitionDate(attr.getDate(Tag.AcquisitionDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setContentDate(attr.getDate(Tag.ContentDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        ct.setSeriesTime(DicomUtils.tmToLocalTime(attr.getString(Tag.SeriesTime, "")));
        ct.setAcquisitionTime(DicomUtils.tmToLocalTime(attr.getString(Tag.AcquisitionTime, "")));
        ct.setContentTime(DicomUtils.tmToLocalTime(attr.getString(Tag.ContentTime, "")));
        ct.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        ct.setModality(modality(attr));
        if (ct.getModality() != Modality.CT) {
            log.error("Trying to read a DICOM file that is not a CT");
            return Optional.empty();
        }
        ct.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        ct.setInstitutionName(attr.getString(Tag.InstitutionName, ""));
        ct.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        ct.setStationName(attr.getString(Tag.StationName, ""));
        ct.getProcedureCodeSequence().clear();
        if (attr.contains(Tag.ProcedureCodeSequence)) {
            Sequence seq = attr.getSequence(Tag.ProcedureCodeSequence);
            for (Attributes value : seq) {
                var optCode = reducedCode(value);
                optCode.ifPresent(code -> ct.getProcedureCodeSequence().add(code));
            }
        }
        ct.setSeriesDescription(attr.getString(Tag.SeriesDescription, ""));
        ct.setInstitutionalDepartmentName(attr.getString(Tag.InstitutionalDepartmentName, ""));
        ct.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));
        ct.getReferencedStudySequence().clear();
        if (attr.contains(Tag.ReferencedStudySequence)) {
            Sequence seq = attr.getSequence(Tag.ReferencedStudySequence);
            for (Attributes value : seq) {
                var optTmp = referencedSOPClassInstance(value);
                optTmp.ifPresent(tmp -> ct.getReferencedStudySequence().add(tmp));
            }
        }
        ct.setPatientName(attr.getString(Tag.PatientName, ""));
        ct.setPatientID(attr.getString(Tag.PatientID, ""));
        ct.setPatientBirthDate(attr.getDate(Tag.PatientBirthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setPatientSex(attr.getString(Tag.PatientSex, ""));
        ct.setPatientAge(attr.getString(Tag.PatientAge, ""));
        ct.setPatientIdentityRemoved(attr.getString(Tag.PatientIdentityRemoved, ""));
        ct.setDeidentificationMethod(attr.getString(Tag.DeidentificationMethod, ""));
        ct.setBodyPartExamined(attr.getString(Tag.BodyPartExamined, ""));
        ct.setScanOptions(attr.getString(Tag.ScanOptions, ""));
        ct.setSliceThickness(attr.getDouble(Tag.SliceThickness, 0.0));
        ct.setKVP(attr.getDouble(Tag.KVP, 0.0));
        ct.setDataCollectionDiameter(attr.getDouble(Tag.DataCollectionDiameter, 0.0));
        ct.setDeviceSerialNumber(attr.getString(Tag.DeviceSerialNumber, ""));
        ct.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        ct.setProtocolName(attr.getString(Tag.ProtocolName, ""));
        ct.setReconstructionDiameter(attr.getDouble(Tag.ReconstructionDiameter, 0.0));
        ct.setGantryDetectorTilt(attr.getDouble(Tag.GantryDetectorTilt, 0.0));
        ct.setTableHeight(attr.getDouble(Tag.TableHeight, 0.0));
        ct.setRotationDirection(attr.getString(Tag.RotationDirection, ""));
        ct.setExposureTime(attr.getInt(Tag.ExposureTime, DicomUtils.UNDEFINED_U32));
        ct.setXRayTubeCurrent(attr.getInt(Tag.XRayTubeCurrent, DicomUtils.UNDEFINED_U32));
        ct.setExposure(attr.getInt(Tag.Exposure, DicomUtils.UNDEFINED_U32));
        ct.setGeneratorPower(attr.getInt(Tag.GeneratorPower, DicomUtils.UNDEFINED_U32));
        ct.setFocalSpots(attr.getDoubles(Tag.FocalSpots));
        ct.setConvolutionKernel(attr.getString(Tag.ConvolutionKernel, ""));
        ct.setPatientPosition(patientPosition(attr));
        ct.setExposureModulationType(attr.getString(Tag.ExposureModulationType, ""));
        ct.setEstimatedDoseSaving(attr.getDouble(Tag.EstimatedDoseSaving, 0.0));
        ct.setCTDIvol(attr.getDouble(Tag.CTDIvol, 0.0));
        ct.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        ct.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        ct.setStudyID(attr.getString(Tag.StudyID, ""));
        ct.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_U32));
        ct.setAcquisitionNumber(attr.getInt(Tag.AcquisitionNumber, DicomUtils.UNDEFINED_U32));
        ct.setInstanceNumber(attr.getInt(Tag.InstanceNumber, DicomUtils.UNDEFINED_U32));
        ct.setPatientOrientation(attr.getString(Tag.PatientOrientation, ""));
        ct.setImagePositionPatient(attr.getDoubles(Tag.ImagePositionPatient));
        ct.setImageOrientationPatient(attr.getDoubles(Tag.ImageOrientationPatient));
        ct.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        ct.setPositionReferenceIndicator(attr.getString(Tag.PositionReferenceIndicator, ""));
        ct.setSliceLocation(attr.getDouble(Tag.SliceLocation, 0.0));
        ct.setImageComments(attr.getString(Tag.ImageComments, ""));
        ct.setSamplesPerPixel(attr.getInt(Tag.SamplesPerPixel, DicomUtils.UNDEFINED_U32));
        ct.setPhotometricInterpretation(photometricInterpretation(attr));
        ct.setRows(attr.getInt(Tag.Rows, DicomUtils.UNDEFINED_U32));
        ct.setColumns(attr.getInt(Tag.Columns, DicomUtils.UNDEFINED_U32));
        ct.setPixelSpacing(attr.getDoubles(Tag.PixelSpacing));
        ct.setBitsAllocated(attr.getInt(Tag.BitsAllocated, DicomUtils.UNDEFINED_U32));
        ct.setBitsStored(attr.getInt(Tag.BitsStored, DicomUtils.UNDEFINED_U32));
        ct.setHighBit(attr.getInt(Tag.HighBit, DicomUtils.UNDEFINED_U32));
        ct.setPixelRepresentation(pixelRepresentation(attr));
        ct.setWindowCenter(attr.getDouble(Tag.WindowCenter, 0.0));
        ct.setWindowWidth(attr.getDouble(Tag.WindowWidth, 0.0));
        ct.setRescaleIntercept(attr.getDouble(Tag.RescaleIntercept, 0.0));
        ct.setRescaleSlope(attr.getDouble(Tag.RescaleSlope, 0.0));
        ct.setScheduledProcedureStepStartDate(attr.getDate(Tag.ScheduledProcedureStepStartDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setScheduledProcedureStepStartTime(DicomUtils.tmToLocalTime(attr.getString(Tag.ScheduledProcedureStepStartTime, "")));
        ct.setScheduledProcedureStepEndDate(attr.getDate(Tag.ScheduledProcedureStepEndDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setScheduledProcedureStepEndTime(DicomUtils.tmToLocalTime(attr.getString(Tag.ScheduledProcedureStepEndTime, "")));
        ct.setPerformedProcedureStepStartDate(attr.getDate(Tag.PerformedProcedureStepStartDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ct.setPerformedProcedureStepStartTime(DicomUtils.tmToLocalTime(attr.getString(Tag.PerformedProcedureStepStartTime, "")));
        ct.setPerformedProcedureStepID(attr.getString(Tag.PerformedProcedureStepID, ""));
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
        ct.setPixelData(getPixelData(buf, 0, nbuf, bps, ct.getPixelRepresentation(), order));
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
        rtdose.setSpecificCharacterSet(attr.getString(Tag.SpecificCharacterSet, ""));
        rtdose.setInstanceCreationDate(attr.getDate(Tag.InstanceCreationDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        rtdose.setInstanceCreationTime(DicomUtils.tmToLocalTime(attr.getString(Tag.InstanceCreationTime, "")));
        rtdose.setSopClassUID(attr.getString(Tag.SOPClassUID, ""));
        if (!rtdose.getSopClassUID().equals(UID.RTDoseStorage)) return Optional.empty();
        rtdose.setSopInstanceUID(attr.getString(Tag.SOPInstanceUID));
        rtdose.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        rtdose.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        rtdose.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        rtdose.setModality(modality(attr));
        if (!rtdose.getModality().equals(Modality.RTDOSE)) {
            log.error("Trying to read a DICOM file that is not a RTDOSE");
            return Optional.empty();
        }
        rtdose.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        rtdose.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        rtdose.setStationName(attr.getString(Tag.StationName, ""));
        rtdose.setSeriesDescription(attr.getString(Tag.SeriesDescription, ""));
        rtdose.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));
        rtdose.setPatientName(attr.getString(Tag.PatientName, ""));
        rtdose.setPatientID(attr.getString(Tag.PatientID, ""));
        if (attr.contains(Tag.PatientBirthDate))
            rtdose.setPatientBirthDate(DicomUtils.getLocalDate(attr.getDate(Tag.PatientBirthDate)));
        else
            rtdose.setPatientBirthDate(null);
        rtdose.setPatientSex(attr.getString(Tag.PatientSex, ""));
        rtdose.setSliceThicknes(attr.getDouble(Tag.SliceThickness, 0.0));
        rtdose.setDeviceSerialNumber(attr.getString(Tag.DeviceSerialNumber, ""));
        rtdose.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        rtdose.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        rtdose.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        rtdose.setStudyID(attr.getString(Tag.StudyID, ""));
        rtdose.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_I32));
        rtdose.setInstanceNumber(attr.getInt(Tag.InstanceNumber, DicomUtils.UNDEFINED_I32));
        rtdose.setImagePositionPatient(attr.getDoubles(Tag.ImagePositionPatient));
        rtdose.setImageOrientationPatient(attr.getDoubles(Tag.ImageOrientationPatient));
        rtdose.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        rtdose.setPositionReferenceIndicator(attr.getString(Tag.PositionReferenceIndicator, ""));
        rtdose.setSamplesPerPixel(attr.getInt(Tag.SamplesPerPixel, DicomUtils.UNDEFINED_U32));
        rtdose.setPhotometricInterpretation(photometricInterpretation(attr));
        rtdose.setNumberOfFrames(attr.getInt(Tag.NumberOfFrames, DicomUtils.UNDEFINED_I32));
        rtdose.setFrameIncrementPointer(attr.getInt(Tag.FrameIncrementPointer, 0));
        rtdose.setRows(attr.getInt(Tag.Rows, DicomUtils.UNDEFINED_U32));
        rtdose.setColumns(attr.getInt(Tag.Columns, DicomUtils.UNDEFINED_U32));
        rtdose.setPixelSpacing(attr.getDoubles(Tag.PixelSpacing));
        rtdose.setBitsAllocated(attr.getInt(Tag.BitsAllocated, DicomUtils.UNDEFINED_U32));
        rtdose.setBitsStored(attr.getInt(Tag.BitsStored, DicomUtils.UNDEFINED_U32));
        rtdose.setHighBit(attr.getInt(Tag.HighBit, DicomUtils.UNDEFINED_U32));
        rtdose.setPixelRepresentation(pixelRepresentation(attr));
        rtdose.setDoseUnits(attr.getString(Tag.DoseUnits, ""));
        rtdose.setDoseType(attr.getString(Tag.DoseType, ""));
        rtdose.setDoseSummationType(attr.getString(Tag.DoseSummationType, ""));
        rtdose.setGridFrameOffsetVector(attr.getDoubles(Tag.GridFrameOffsetVector));
        rtdose.setDoseGridScaling(attr.getDouble(Tag.DoseGridScaling, 0.0));
        rtdose.setTissueHeterogeneityCorrection(attr.getString(Tag.TissueHeterogeneityCorrection, ""));
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
        pt.setSpecificCharacterSet(attr.getString(Tag.SpecificCharacterSet, ""));
        pt.setImageType(attr.getString(Tag.ImageType, ""));
        pt.setSOPClassUID(attr.getString(Tag.SOPClassUID, ""));
        pt.setSOPInstanceUID(attr.getString(Tag.SOPInstanceUID, ""));
        pt.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setSeriesDate(attr.getDate(Tag.SeriesDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setAcquisitionDate(attr.getDate(Tag.AcquisitionDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setContentDate(attr.getDate(Tag.ContentDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        pt.setSeriesTime(DicomUtils.tmToLocalTime(attr.getString(Tag.SeriesTime, "")));
        pt.setAcquisitionTime(DicomUtils.tmToLocalTime(attr.getString(Tag.AcquisitionTime, "")));
        pt.setContentTime(DicomUtils.tmToLocalTime(attr.getString(Tag.ContentTime, "")));
        pt.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        pt.setModality(modality(attr));
        if (pt.getModality() != Modality.PT) {
            log.error("Trying to read a DICOM file that is not a PT");
            return Optional.empty();
        }
        pt.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        pt.setInstitutionName(attr.getString(Tag.InstitutionName, ""));
        pt.setInstitutionAddress(attr.getString(Tag.InstitutionAddress, ""));
        pt.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        pt.setStationName(attr.getString(Tag.StationName, ""));
        pt.setStudyDescription(attr.getString(Tag.StudyDescription, ""));
        pt.setSeriesDescription(attr.getString(Tag.SeriesDescription, ""));
        pt.setInstitutionalDepartmentName(attr.getString(Tag.InstitutionalDepartmentName, ""));
        pt.setPhysiciansOfRecord(attr.getString(Tag.PhysiciansOfRecord, ""));
        pt.setPerformingPhysicianName(attr.getString(Tag.PerformingPhysicianName, ""));
        pt.setOperatorsName(attr.getString(Tag.OperatorsName, ""));
        pt.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));
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
        pt.setPatientName(attr.getString(Tag.PatientName, ""));
        pt.setPatientID(attr.getString(Tag.PatientID, ""));
        pt.setIssuerOfPatientID(attr.getString(Tag.IssuerOfPatientID, ""));
        pt.setPatientBirthDate(attr.getDate(Tag.PatientBirthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setPatientSex(attr.getString(Tag.PatientSex, ""));
        pt.setPatientAge(attr.getString(Tag.PatientAge, ""));
        pt.setPatientSize(attr.getDouble(Tag.PatientSize, 0.0));
        pt.setPatientWeight(attr.getDouble(Tag.PatientWeight, 0.0));
        pt.setPatientAddress(attr.getString(Tag.PatientAddress, ""));
        pt.setBranchOfService(attr.getString(Tag.BranchOfService, ""));
        pt.setPregnancyStatus(attr.getString(Tag.PregnancyStatus, ""));
        pt.setBodyPartExamined(attr.getString(Tag.BodyPartExamined, ""));
        pt.setSliceThickness(attr.getDouble(Tag.SliceThickness, 0.0));
        pt.setDeviceSerialNumber(attr.getString(Tag.DeviceSerialNumber, ""));
        pt.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        pt.setCollimatorType(attr.getString(Tag.CollimatorType, ""));
        pt.setDateOfLastCalibration(attr.getDate(Tag.DateOfLastCalibration).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setTimeOfLastCalibration(DicomUtils.tmToLocalTime(attr.getString(Tag.TimeOfLastCalibration, "")));
        pt.setConvolutionKernel(attr.getString(Tag.ConvolutionKernel, ""));
        pt.setActualFrameDuration(attr.getInt(Tag.ActualFrameDuration, DicomUtils.UNDEFINED_U32));
        pt.setPatientPosition(patientPosition(attr));
        pt.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        pt.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        pt.setStudyID(attr.getString(Tag.StudyID, ""));
        pt.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_U32));
        pt.setAcquisitionNumber(attr.getInt(Tag.AcquisitionNumber, DicomUtils.UNDEFINED_U32));
        pt.setInstanceNumber(attr.getInt(Tag.InstanceNumber, DicomUtils.UNDEFINED_U32));
        pt.setImagePositionPatient(attr.getDoubles(Tag.ImagePositionPatient));
        pt.setImageOrientationPatient(attr.getDoubles(Tag.ImageOrientationPatient));
        pt.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        pt.setPositionReferenceIndicator(attr.getString(Tag.PositionReferenceIndicator, ""));
        pt.setSliceLocation(attr.getDouble(Tag.SliceLocation, 0.0));
        pt.setImageComments(attr.getString(Tag.ImageComments, ""));
        pt.setSamplesPerPixel(attr.getInt(Tag.SamplesPerPixel, DicomUtils.UNDEFINED_U32));
        pt.setPhotometricInterpretation(photometricInterpretation(attr));
        pt.setRows(attr.getInt(Tag.Rows, DicomUtils.UNDEFINED_U32));
        pt.setColumns(attr.getInt(Tag.Columns, DicomUtils.UNDEFINED_U32));
        pt.setPixelSpacing(attr.getDoubles(Tag.PixelSpacing));
        pt.setCorrectedImage(Arrays.stream(attr.getString(Tag.CorrectedImage, "").split("\\\\")).collect(Collectors.toList()));
        pt.setBitsAllocated(attr.getInt(Tag.BitsAllocated, DicomUtils.UNDEFINED_U32));
        pt.setBitsStored(attr.getInt(Tag.BitsStored, DicomUtils.UNDEFINED_U32));
        pt.setHighBit(attr.getInt(Tag.HighBit, DicomUtils.UNDEFINED_U32));
        pt.setPixelRepresentation(pixelRepresentation(attr));
        pt.setSmallestImagePixelValue(attr.getInt(Tag.SmallestImagePixelValue, DicomUtils.UNDEFINED_U32));
        pt.setLargestImagePixelValue(attr.getInt(Tag.LargestImagePixelValue, DicomUtils.UNDEFINED_U32));
        pt.setWindowCenter(attr.getDouble(Tag.WindowCenter, 0.0));
        pt.setWindowWidth(attr.getDouble(Tag.WindowWidth, 0.0));
        pt.setRescaleIntercept(attr.getDouble(Tag.RescaleIntercept, 0.0));
        pt.setRescaleSlope(attr.getDouble(Tag.RescaleSlope, 0.0));
        pt.setRescaleType(attr.getString(Tag.RescaleType, ""));
        pt.setRequestingPhysician(attr.getString(Tag.RequestingPhysician, ""));
        pt.setRequestingService(attr.getString(Tag.RequestingService, ""));
        pt.setRequestedProcedureDescription(attr.getString(Tag.RequestedProcedureDescription, ""));
        pt.setCurrentPatientLocation(attr.getString(Tag.CurrentPatientLocation, ""));
        pt.setPerformedProcedureStepStartDate(attr.getDate(Tag.PerformedProcedureStepStartDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        pt.setPerformedProcedureStepStartTime(DicomUtils.tmToLocalTime(attr.getString(Tag.PerformedProcedureStepStartTime, "")));
        pt.getRequestAttributesSequence().clear();
        if (attr.contains(Tag.RequestAttributesSequence)) {
            Sequence seq = attr.getSequence(Tag.RequestAttributesSequence);
            for (Attributes value : seq) {
                var optTmp = requestAttributes(value);
                optTmp.ifPresent(tmp -> pt.getRequestAttributesSequence().add(tmp));
            }
        }
        pt.setRequestedProcedureID(attr.getString(Tag.RequestedProcedureID, ""));
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
        pt.setNumberOfSlices(attr.getInt(Tag.NumberOfSlices, DicomUtils.UNDEFINED_U32));
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
        pt.setSeriesType(attr.getString(Tag.SeriesType, ""));
        pt.setUnits(attr.getString(Tag.Units, ""));
        pt.setCountsSource(attr.getString(Tag.CountsSource, ""));
        pt.setRandomsCorrectionMethod(attr.getString(Tag.RandomsCorrectionMethod, ""));
        pt.setAttenuationCorrectionMethod(attr.getString(Tag.AttenuationCorrectionMethod, ""));
        pt.setDecayCorrection(attr.getString(Tag.DecayCorrection, ""));
        pt.setReconstructionMethod(attr.getString(Tag.ReconstructionMethod, ""));
        pt.setScatterCorrectionMethod(attr.getString(Tag.ScatterCorrectionMethod, ""));
        pt.setAxialAcceptance(attr.getDouble(Tag.AxialAcceptance, 0.0));
        pt.setAxialMash(attr.getInts(Tag.AxialMash));
        pt.setFrameReferenceTime(attr.getDouble(Tag.FrameReferenceTime, 0.0));
        pt.setDecayFactor(attr.getDouble(Tag.DecayFactor, 0.0));
        pt.setDoseCalibrationFactor(attr.getDouble(Tag.DoseCalibrationFactor, 0.0));
        pt.setScatterFractionFactor(attr.getDouble(Tag.ScatterFractionFactor, 0.0));
        pt.setImageIndex(attr.getInt(Tag.ImageIndex, DicomUtils.UNDEFINED_U32));

        byte[] buf = attr.getBytes(Tag.PixelData);
        int nbuf = buf.length;
        int bps = pt.getBitsAllocated() / 8;
        if ((pt.getBitsAllocated() != 16 || bps != 2) && (pt.getBitsAllocated() != 32 || bps != 4))
            throw new DicomException("Only 16 or 32 bit pixeldata is supported");
        pt.setPixelData(getPixelData(buf, 0, nbuf, bps, pt.getPixelRepresentation(), order));
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
        sr.setSpecificCharacterSet(attr.getString(Tag.SpecificCharacterSet, ""));
        sr.setInstanceCreationDate(attr.getDate(Tag.InstanceCreationDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        sr.setInstanceCreationTime(DicomUtils.tmToLocalTime(attr.getString(Tag.InstanceCreationTime, "")));
        sr.setSOPClassUID(attr.getString(Tag.SOPClassUID, ""));
        sr.setSOPInstanceUID(attr.getString(Tag.SOPInstanceUID, ""));
        sr.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        sr.setSeriesDate(attr.getDate(Tag.SeriesDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        sr.setContentDate(attr.getDate(Tag.ContentDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        sr.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        sr.setSeriesTime(DicomUtils.tmToLocalTime(attr.getString(Tag.SeriesTime, "")));
        sr.setContentTime(DicomUtils.tmToLocalTime(attr.getString(Tag.ContentTime, "")));
        sr.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        sr.setModality(modality(attr));
        if (sr.getModality() != Modality.REG) {
            log.error("Trying to read a DICOM file that is not a SR");
            return Optional.empty();
        }
        sr.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        sr.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        sr.setSeriesDescription(attr.getString(Tag.SeriesDescription, ""));
        sr.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));

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

        sr.setPatientName(attr.getString(Tag.PatientName, ""));
        sr.setPatientID(attr.getString(Tag.PatientID, ""));
        sr.setPatientBirthDate(attr.getString(Tag.PatientBirthDate, ""));
        sr.setPatientSex(attr.getString(Tag.PatientSex, ""));
        sr.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        sr.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        sr.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        sr.setStudyID(attr.getString(Tag.StudyID, ""));
        sr.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_U32));
        sr.setInstanceNumber(attr.getInt(Tag.InstanceNumber, DicomUtils.UNDEFINED_U32));
        sr.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        sr.setPositionReferenceIndicator(attr.getString(Tag.PositionReferenceIndicator, ""));
        sr.setContentLabel(attr.getString(Tag.ContentLabel, ""));
        sr.setContentDescription(attr.getString(Tag.ContentDescription, ""));
        sr.setContentCreatorName(attr.getString(Tag.ContentCreatorName, ""));
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
        ss.setSpecificCharacterSet(attr.getString(Tag.SpecificCharacterSet, ""));
        ss.setInstanceCreationDate(attr.getDate(Tag.InstanceCreationDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ss.setInstanceCreationTime(DicomUtils.tmToLocalTime(attr.getString(Tag.InstanceCreationTime, "")));
        ss.setSOPClassUID(attr.getString(Tag.SOPClassUID, ""));
        ss.setSOPInstanceUID(attr.getString(Tag.SOPInstanceUID, ""));
        ss.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ss.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        ss.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        ss.setModality(modality(attr));
        if (ss.getModality() != Modality.RTSTRUCT) {
            log.error("Trying to read a DICOM file that is not a RTSTRUCT");
            return Optional.empty();
        }
        ss.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        ss.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        ss.setSeriesDescription(attr.getString(Tag.SeriesDescription, ""));
        ss.setOperatorsName(attr.getString(Tag.OperatorsName, ""));
        ss.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));
        ss.setPatientName(attr.getString(Tag.PatientName, ""));
        ss.setPatientID(attr.getString(Tag.PatientID, ""));
        ss.setPatientBirthDate(attr.getDate(Tag.PatientBirthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ss.setPatientSex(attr.getString(Tag.PatientSex, ""));
        ss.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        ss.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        ss.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        ss.setStudyID(attr.getString(Tag.StudyID, ""));
        ss.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_U32));
        ss.setFrameOfReferenceUID(attr.getString(Tag.FrameOfReferenceUID, ""));
        ss.setPositionReferenceIndicator(attr.getString(Tag.PositionReferenceIndicator, ""));
        ss.setStructureSetLabel(attr.getString(Tag.StructureSetLabel, ""));
        ss.setStructureSetDate(attr.getDate(Tag.StructureSetDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        ss.setStructureSetTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StructureSetTime, "")));
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
        ss.setApprovalStatus(attr.getString(Tag.ApprovalStatus, ""));
        return Optional.of(ss);
    }

    public static Optional<RTBeamsTreatmentRecordStorage> rtBeamsTreatmentRecordStorage(Attributes meta, Attributes attr, ByteOrder order) throws IOException {
        if (attr == null) return Optional.empty();
        var optMeta = metaHeader(meta);
        RTBeamsTreatmentRecordStorage record;
        if (optMeta.isEmpty()) {
            record = new RTBeamsTreatmentRecordStorage();
        } else {
            record = new RTBeamsTreatmentRecordStorage(optMeta.get());
        }
        record.setInstanceCreationDate(attr.getDate(Tag.InstanceCreationDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        record.setInstanceCreationTime(DicomUtils.tmToLocalTime(attr.getString(Tag.InstanceCreationTime, "")));
        record.setSOPClassUID(attr.getString(Tag.SOPClassUID, ""));
        if (!record.getSOPClassUID().equals(UID.RTBeamsTreatmentRecordStorage)) {
            log.error("Trying to read a DICOM file that is not a RTRECORD");
            return Optional.empty();
        }
        record.setSOPInstanceUID(attr.getString(Tag.SOPInstanceUID, ""));
        record.setStudyDate(attr.getDate(Tag.StudyDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        record.setStudyTime(DicomUtils.tmToLocalTime(attr.getString(Tag.StudyTime, "")));
        record.setAccessionNumber(attr.getString(Tag.AccessionNumber, ""));
        record.setModality(modality(attr));
        if (record.getModality() != Modality.RTRECORD) {
            log.error("Trying to read a DICOM file that is not a RTRECORD");
            return Optional.empty();
        }
        record.setManufacturer(attr.getString(Tag.Manufacturer, ""));
        record.setReferringPhysicianName(attr.getString(Tag.ReferringPhysicianName, ""));
        record.setOperatorsName(attr.getString(Tag.OperatorsName, ""));
        record.setManufacturerModelName(attr.getString(Tag.ManufacturerModelName, ""));
        record.setPatientName(attr.getString(Tag.PatientName, ""));
        record.setPatientID(attr.getString(Tag.PatientID, ""));
        if (!attr.getString(Tag.PatientBirthDate, "").isBlank())
            record.setPatientBirthDate(attr.getDate(Tag.PatientBirthDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        record.setPatientSex(attr.getString(Tag.PatientSex, ""));
        record.setEthnicGroup(attr.getString(Tag.EthnicGroup, ""));
        record.setSoftwareVersions(attr.getString(Tag.SoftwareVersions, ""));
        record.setStudyInstanceUID(attr.getString(Tag.StudyInstanceUID, ""));
        record.setSeriesInstanceUID(attr.getString(Tag.SeriesInstanceUID, ""));
        record.setStudyID(attr.getString(Tag.StudyID, ""));
        record.setSeriesNumber(attr.getInt(Tag.SeriesNumber, DicomUtils.UNDEFINED_U32));
        record.setInstanceNumber(attr.getInt(Tag.InstanceNumber, DicomUtils.UNDEFINED_U32));
        if (attr.contains(Tag.TreatmentSessionBeamSequence)) {
            Sequence seq = attr.getSequence(Tag.TreatmentSessionBeamSequence);
            for (Attributes value : seq) {
                var optTmp = treatmentSessionBeamstructureSetROI(value);
                optTmp.ifPresent(tmp -> record.getTreatmentSessionBeamSequence().add(tmp));
            }
        }

        return Optional.of(record);
    }
}
