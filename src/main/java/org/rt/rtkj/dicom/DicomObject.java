package org.rt.rtkj.dicom;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DicomObject {
    @Getter
    @Setter
    private String pathname;
    @Getter
    private List<String> errors;
    @Getter
    private Optional<CT> ct;
    @Getter
    private Optional<PT> pt;
    @Getter
    private Optional<RTStructureSet> rtstruct;
    @Getter
    private Optional<RTDose> rtdose;
    @Getter
    private Optional<SpatialRegistration> spatialRegistration;
    @Getter
    private Optional<RTBeamsTreatmentRecordStorage> rtBeamsTreatmentRecordStorage;
    @Getter
    private Optional<RTPlan> rtPlan;

    public DicomObject() {
        clear();
    }

    public void clear() {
        ct = Optional.empty();
        pt = Optional.empty();
        rtstruct = Optional.empty();
        rtdose = Optional.empty();
        spatialRegistration = Optional.empty();
        rtBeamsTreatmentRecordStorage = Optional.empty();
        rtPlan = Optional.empty();
        if (errors == null) errors = new ArrayList<>();
        errors.clear();
    }

    public void set(CT ct) {
        clear();
        if (ct != null) this.ct = Optional.of(ct);
    }

    public void set(PT pt) {
        clear();
        if (pt != null) this.pt = Optional.of(pt);
    }

    public void set(RTStructureSet ss) {
        clear();
        if (ss != null) this.rtstruct = Optional.of(ss);
    }

    public void set(RTDose rtdose) {
        clear();
        if (rtdose != null) this.rtdose = Optional.of(rtdose);
    }

    public void set(SpatialRegistration sr) {
        clear();
        if (sr != null) this.spatialRegistration = Optional.of(sr);
    }

    public void set(RTBeamsTreatmentRecordStorage record) {
        clear();
        if (record != null) this.rtBeamsTreatmentRecordStorage = Optional.of(record);
    }

    public void set(RTPlan plan) {
        clear();
        if (plan != null) this.rtPlan = Optional.of(plan);
    }

    public boolean hasCT() {
        return (ct != null) && ct.isPresent();
    }

    public boolean hasPT() {
        return (pt != null) && pt.isPresent();
    }

    public boolean hasStructureSet() {
        return (rtstruct != null) && rtstruct.isPresent();
    }

    public boolean hasRTDose() {
        return (rtdose != null) && rtdose.isPresent();
    }

    public boolean hasSpatialRegistration() {
        return (spatialRegistration != null) && spatialRegistration.isPresent();
    }

    public boolean hasRTBeamsTreatmentRecordStorage() {
        return (rtBeamsTreatmentRecordStorage != null) && rtBeamsTreatmentRecordStorage.isPresent();
    }

    public boolean hasRTPlan() {
        return (rtPlan != null) && rtPlan.isPresent();
    }

    public void addError(String msg) {
        errors.add(msg);
    }
}
