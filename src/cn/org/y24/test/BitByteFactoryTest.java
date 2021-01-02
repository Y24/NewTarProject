/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.utils.BitByteFactory;
import cn.org.y24.utils.DataConverter;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BitByteFactoryTest {
    @Test
    void testBitsToByte() {
        final var random = new SecureRandom();
        final int size = 33;
        final int padding = 8 - size % 8;
        final BitByteFactory factory = BitByteFactory.ofInstance();
        final byte[] bits = new byte[size];
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (byte) (random.nextBoolean() ? 1 : 0);
        }
        final List<Byte> list = DataConverter.bytesToList(bits);
        assertTrue(factory.acceptBits(list));
        assertEquals(factory.getPadding(), padding);
        final var result = factory.result();
        assertTrue(factory.acceptBytes(result, padding));
        assertArrayEquals(DataConverter.listToBytes(factory.result()), bits);
    }

    @Test
    void testIntToBytes() {
        final int target = new SecureRandom().nextInt();
        assertEquals(DataConverter.bytesToInt(DataConverter.intToBytes(target)), target);
    }
}
