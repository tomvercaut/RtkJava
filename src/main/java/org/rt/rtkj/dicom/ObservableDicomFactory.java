package org.rt.rtkj.dicom;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.log4j.Log4j2;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.io.DicomInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ObservableDicomFactory {

    public static class DataAttributes {
        public String pathname;
        public List<String> errors;
        public Attributes meta;
        public Attributes dataset;
        public ByteOrder bo;

        public DataAttributes() {
            pathname = "";
            errors = new ArrayList<>();
            meta = null;
            dataset = null;
            bo = null;
        }

        public boolean hasError() {
            return (errors != null) && errors.size() > 0;
        }
    }

    private static Flowable<DataAttributes> readFile(String observablePathname) {
        return Flowable.just(observablePathname).map(pathname ->
        {
            File file = new File(pathname);
            DataAttributes da = new DataAttributes();
            if (!file.isFile() || !file.canRead()) {
                da.pathname = pathname;
                if (!file.isFile()) {
                    da.errors.add("Pathname is not a file");
                }
                if (!file.canRead()) {
                    da.errors.add("Unable to read file.");
                }
                return da;
            }

            DicomInputStream dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
            Attributes meta = dis.readFileMetaInformation();
            var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
            Attributes dataset = dis.readDataset(-1, -1);
            if (!dataset.contains(Tag.SOPClassUID)) {
                da.errors.add("DICOM stream doesn't contain a SOPClassUID");
                return da;
            }
            da.meta = meta;
            da.dataset = dataset;
            da.bo = bo;
            return da;
        });
    }

    private static Flowable<DicomObject> processData(DataAttributes observableDataAttrubtes) {
        return Flowable.just(observableDataAttrubtes).map(da -> {
            DicomObject dicomObject = new DicomObject();
            dicomObject.setPathname(da.pathname);
            da.errors.forEach(dicomObject::addError);

            if (da.dataset == null) {
                log.error("");
            }

            String sopClassUID = da.dataset.getString(Tag.SOPClassUID);
            if (sopClassUID.equals(UID.CTImageStorage)) {
                var ct = Reader.ct(da.meta, da.dataset, da.bo);
                ct.ifPresent(dicomObject::set);
            } else if (sopClassUID.equals(UID.PositronEmissionTomographyImageStorage)) {
                var pt = Reader.pt(da.meta, da.dataset, da.bo);
                pt.ifPresent(dicomObject::set);
            } else if (sopClassUID.equals(UID.RTStructureSetStorage)) {
                var ss = Reader.structureSet(da.meta, da.dataset, da.bo);
                ss.ifPresent(dicomObject::set);
            } else if (sopClassUID.equals(UID.RTDoseStorage)) {
                var rtd = Reader.rtDose(da.meta, da.dataset, da.bo);
                rtd.ifPresent(dicomObject::set);
            } else if (sopClassUID.equals(UID.SpatialRegistrationStorage)) {
                var sr = Reader.spatialRegistration(da.meta, da.dataset, da.bo);
                sr.ifPresent(dicomObject::set);
            } else {
                String msg = String.format("Trying to read an unsupported DICOM file [SOPClassUID: %s]", sopClassUID);
                dicomObject.addError(msg);
                log.error(msg);
            }
            dicomObject.setPathname(da.pathname);
            return dicomObject;
        });
    }

    public static List<DicomObject> read(List<String> pathnames) {
        List<DicomObject> list = new ArrayList<>();
        boolean exp1 = true;
        if (exp1) {
            Flowable.fromIterable(pathnames)
                    .observeOn(Schedulers.io())
                    .flatMap(ObservableDicomFactory::readFile)
                    .observeOn(Schedulers.computation())
                    .flatMap(ObservableDicomFactory::processData)
                    .blockingSubscribe(list::add);
        } else {
            Flowable.fromIterable(pathnames)
                    .observeOn(Schedulers.io())
                    .map(pathname -> {
                        File file = new File(pathname);
                        DataAttributes da = new DataAttributes();
                        if (!file.isFile() || !file.canRead()) {
                            da.pathname = pathname;
                            if (!file.isFile()) {
                                da.errors.add("Pathname is not a file");
                            }
                            if (!file.canRead()) {
                                da.errors.add("Unable to read file.");
                            }
                            return da;
                        }

                        DicomInputStream dis = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
                        Attributes meta = dis.readFileMetaInformation();
                        var bo = (dis.bigEndian()) ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
                        Attributes dataset = dis.readDataset(-1, -1);
                        if (!dataset.contains(Tag.SOPClassUID)) {
                            da.errors.add("DICOM stream doesn't contain a SOPClassUID");
                            return da;
                        }
                        da.meta = meta;
                        da.dataset = dataset;
                        da.bo = bo;
                        return da;
                    })
                    .observeOn(Schedulers.computation())
                    .map(da -> {
                        DicomObject dicomObject = new DicomObject();
                        dicomObject.setPathname(da.pathname);
                        da.errors.forEach(dicomObject::addError);

                        if (da.dataset == null) {
                            log.error("");
                        }

                        String sopClassUID = da.dataset.getString(Tag.SOPClassUID);
                        if (sopClassUID.equals(UID.CTImageStorage)) {
                            var ct = Reader.ct(da.meta, da.dataset, da.bo);
                            ct.ifPresent(dicomObject::set);
                        } else if (sopClassUID.equals(UID.PositronEmissionTomographyImageStorage)) {
                            var pt = Reader.pt(da.meta, da.dataset, da.bo);
                            pt.ifPresent(dicomObject::set);
                        } else if (sopClassUID.equals(UID.RTStructureSetStorage)) {
                            var ss = Reader.structureSet(da.meta, da.dataset, da.bo);
                            ss.ifPresent(dicomObject::set);
                        } else if (sopClassUID.equals(UID.RTDoseStorage)) {
                            var rtd = Reader.rtDose(da.meta, da.dataset, da.bo);
                            rtd.ifPresent(dicomObject::set);
                        } else if (sopClassUID.equals(UID.SpatialRegistrationStorage)) {
                            var sr = Reader.spatialRegistration(da.meta, da.dataset, da.bo);
                            sr.ifPresent(dicomObject::set);
                        } else {
                            String msg = String.format("Trying to read an unsupported DICOM file [SOPClassUID: %s]", sopClassUID);
                            dicomObject.addError(msg);
                            log.error(msg);
                        }
                        dicomObject.setPathname(da.pathname);
                        return dicomObject;
                    })
                    .blockingSubscribe(list::add);
        }
        return list;
    }
}
