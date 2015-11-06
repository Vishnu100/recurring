package com.itranswarp.recurring.common.util;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.itranswarp.recurring.common.util.IdGenerator;

public class IdGeneratorTest {

    @Test
    public void testGetHostId() {
        assertEquals(0x0, IdGenerator.getHostId(""));
        assertEquals(0x0, IdGenerator.getHostId("A"));
        assertEquals(0x0, IdGenerator.getHostId("AA"));
        assertEquals(0x1, IdGenerator.getHostId("A1"));
        assertEquals(0x5, IdGenerator.getHostId("5"));
        assertEquals(0x7, IdGenerator.getHostId("007"));
        assertEquals(0x8, IdGenerator.getHostId("1A8"));
        assertEquals(0xf, IdGenerator.getHostId("C015"));
        assertEquals(0xff, IdGenerator.getHostId("MAC-255"));
        assertEquals(0xfff, IdGenerator.getHostId("COMP_4095"));
        assertEquals(0xffff, IdGenerator.getHostId("COMPx65535"));
        assertEquals(0, IdGenerator.getHostId("C0"));
        assertEquals(0, IdGenerator.getHostId("CX"));
    }

    @Test
    public void testNext() {
        final int COUNT = 1000;
        final String HOST_ID = String.format("%05x", IdGenerator.HOST_ID);
        final double NOW = System.currentTimeMillis();
        Set<String> set = new HashSet<String>();
        for (int i = 0; i < COUNT; i++) {
            String id = IdGenerator.next();
            // check length:
            assertEquals(50, id.length());
            // check 'xxxxxxx-hostId':
            assertTrue(id.endsWith(HOST_ID));
            // check 'timestamp-xxxx':
            assertEquals(NOW, Long.parseLong(id.substring(0, 13), 16), 1000.0);
            set.add(id);
        }
        assertEquals(COUNT, set.size());
    }
}
