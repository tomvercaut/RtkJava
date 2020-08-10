package org.rt.rtkj.utils;

import org.rt.rtkj.dicom.SpatialRegistration;
import org.rt.rtkj.dicom.TransformationMatrixType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransformBuilder {

    /**
     * Get a sequential list of RIGID transform matrices from a spatial registration.
     *
     * @param sr DICOM spatial registration object
     * @return List of RIDID transforms.
     */
    public static List<Transform4x4> getRigidFrameOfReferenceTransformMatrices(SpatialRegistration sr) {
        Objects.requireNonNull(sr);
        List<Transform4x4> l = new ArrayList<>();
        var optSeq = sr.getRegistrationSequence();
        if (optSeq.isEmpty()) return l;
        var regSeq = optSeq.get();
        for (var regItem : regSeq) {
            var optMatRegSeq = regItem.getMatrixRegistrationSequence();
            if (optMatRegSeq.isEmpty()) continue;
            var matRegSeq = optMatRegSeq.get();
            for (var matReg : matRegSeq) {
                var optMatSeq = matReg.getMatrixSequence();
                if (optMatSeq.isEmpty()) continue;
                var matSeq = optMatSeq.get();
                for (var mat : matSeq) {
                    var matType = mat.getFrameOfReferenceTransformationMatrixType().orElseGet(() -> TransformationMatrixType.NONE);
                    if (matType != TransformationMatrixType.RIGID) continue;
                    var optMat = mat.getFrameOfReferenceTransformationMatrix();
                    if (optMat.isPresent()) {
                        var matrix = optMat.get();
                        l.add(Transform4x4.fromDicom(matrix));
                    }
                }
            }
        }
        return l;
    }
}
