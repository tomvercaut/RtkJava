package org.rt.rtkj.utils;

import org.dcm4che3.util.ByteUtils;

public class ByteTools {

    public static long bytesToUInt(byte[] bytes, int off, boolean bigEndian) {
        return ByteUtils.bytesToInt(bytes, off, bigEndian) & 0xFFFFFFFFL;
    }

//    private static long bytesToUIntBE(byte[] bytes, int off) {
//        return (bytes[off] << 24) + ((bytes[off + 1] & 255) << 16) + ((bytes[off + 2] & 255) << 8) + (bytes[off + 3] & 255);
//    }
//
//    private static long bytesToUIntLE(byte[] bytes, int off) {
//        return (bytes[off + 3] << 24) + ((bytes[off + 2] & 255) << 16) + ((bytes[off + 1] & 255) << 8) + (bytes[off] & 255);
//    }
}
