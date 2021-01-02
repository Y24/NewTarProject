/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;


import java.util.*;

public class BitByteFactory {
    List<Byte> pool;
    private int padding = 0;

    public int getPadding() {
        return padding;
    }

    private BitByteFactory() {
        pool = new LinkedList<>();
    }

    static public BitByteFactory ofInstance() {
        return new BitByteFactory();
    }

    // It's better to use ArrayList.
    public boolean acceptBits(List<Byte> bits) {
        pool = new ArrayList<>();
        if (!bits.stream().allMatch(aByte -> aByte == HuffmanTreeNode.leftByte || aByte == HuffmanTreeNode.rightByte))
            return false;
        padding = 8 - (bits.size() % 8);
        final int bytesCount = bits.size() / 8;
        for (int i = 0; i < bytesCount; i++) {
            pool.add(bitsToByte(bits.subList(i * 8, (i + 1) * 8)));
        }
        final List<Byte> last = new ArrayList<>();
        for (int i = 8 * bytesCount; i < bits.size(); i++)
            last.add(bits.get(i));
        for (int i = 0; i < padding; i++)
            last.add(HuffmanTreeNode.leftByte);
        pool.add(bitsToByte(last));
        return true;
    }

    // It's better to use ArrayList.
    public boolean acceptBytes(List<Byte> bytes, int padding) {
        pool = new ArrayList<>();
        bytes.forEach(aByte -> pool.addAll(byteToBits(aByte)));
        for (int i = 0; i < padding; i++) {
            pool.remove(pool.size() - 1);
        }
        return pool.stream().allMatch(aByte -> aByte == HuffmanTreeNode.leftByte || aByte == HuffmanTreeNode.rightByte);
    }

    public List<Byte> result() {
        return pool;
    }

    public static byte bitsToByte(List<Byte> bits) {
        int result = 0x0000;
        for (int i = 0; i < 8; i++) {
            result |= (bits.get(i) << (8 - 1 - i));
        }
        return (byte) result;
    }

    public static List<Byte> byteToBits(byte target) {
        final List<Byte> result = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            final int shift = 8 - 1 - i;
            result.add((byte) ((target & 1 << shift) >> shift));
        }
        return result;
    }

}
