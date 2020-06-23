package org.rt.rtkj.dicom;

import org.rt.rtkj.Option;

public interface HasPixelRepresentation {
    Option<PixelRepresentation> getPixelRepresentation();
}
