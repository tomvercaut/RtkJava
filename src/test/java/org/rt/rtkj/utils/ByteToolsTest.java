package org.rt.rtkj.utils;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteToolsTest {

    @Test
    void bytesToUInt() {
        long val = 0xFFFE;
        ByteBuffer bb1 = ByteBuffer.allocate(Long.BYTES);
        bb1.order(ByteOrder.LITTLE_ENDIAN);
        bb1.putLong(val);
        byte[] leArray = bb1.array();
        assertEquals(val, ByteTools.bytesToUInt(leArray, 0, false));

//        ByteBuffer bb2 = ByteBuffer.allocate(Long.BYTES);
//        bb2.order(ByteOrder.BIG_ENDIAN);
//        bb2.clear();
//        bb2.putLong(val);
//        byte[] beArray = bb2.array();
//        assertEquals(-2, ByteTools.bytesToUInt(beArray, 0, true));
    }
}