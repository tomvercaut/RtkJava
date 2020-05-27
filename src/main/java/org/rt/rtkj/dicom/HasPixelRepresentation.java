package org.rt.rtkj.dicom;

import java.util.Optional;

public interface HasPixelRepresentation {
    Optional<PixelRepresentation> getPixelRepresentation();
}
