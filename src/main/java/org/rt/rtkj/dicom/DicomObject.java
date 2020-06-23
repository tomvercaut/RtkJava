package org.rt.rtkj.dicom;

import lombok.Getter;
import lombok.Setter;
import org.rt.rtkj.Option;

import java.util.ArrayList;
import java.util.List;

public class DicomObject {
    @Getter
    @Setter
    private String pathname;
    @Getter
    private List<String> errors;
    @Getter
    private Option<CT> ct;
    @Getter
    private Option<PT> pt;
    @Getter
    private Option<RTStructureSet> rtstruct;
    @Getter
    private Option<RTDose> rtdose;
    @Getter
    private Option<SpatialRegistration> spatialRegistration;

    public DicomObject() {
        clear();
    }

    public void clear() {
        ct = Option.empty();
        pt = Option.empty();
        rtstruct = Option.empty();
        rtdose = Option.empty();
        spatialRegistration = Option.empty();
        if (errors == null) errors = new ArrayList<>();
        errors.clear();
    }

    public void set(CT ct) {
        clear();
        if (ct != null) this.ct = Option.of(ct);
    }

    public void set(PT pt) {
        clear();
        if (pt != null) this.pt = Option.of(pt);
    }

    public void set(RTStructureSet ss) {
        clear();
        if (ss != null) this.rtstruct = Option.of(ss);
    }

    public void set(RTDose rtdose) {
        clear();
        if (rtdose != null) this.rtdose = Option.of(rtdose);
    }

    public void set(SpatialRegistration sr) {
        clear();
        if (sr != null) this.spatialRegistration = Option.of(sr);
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

    public void addError(String msg) {
        errors.add(msg);
    }
}
