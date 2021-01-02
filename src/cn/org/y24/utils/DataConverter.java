/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    public static char byteToChar(byte[] b) {
        return (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
    }

    public static List<Byte> bytesToList(byte[] bytes) {
        List<Byte> result = new ArrayList<>();
        for (byte each : bytes) {
            result.add(each);
        }
        return result;
    }

    public static char[] listToChars(List<Character> list) {
        final char[] result = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static List<Character> charsToList(char[] bytes) {
        List<Character> result = new ArrayList<>();
        for (char each : bytes) {
            result.add(each);
        }
        return result;
    }

    public static byte[] listToBytes(List<Byte> list) {
        final byte[] result = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

    public static byte[] intToBytes(int target) {
        // a int has 4 byte.
        final byte[] result = new byte[4];
        for (int i = 0; i < 4; i++)
            result[i] = (byte) ((target >> (8 * i)) & 0xff);
        return result;
    }

    public static int bytesToInt(byte[] target) {
        assert (target.length == 4);
        int result = 0;
        for (int i = 0; i < 4; i++)
            result += (target[i] & 0xff) << (i * 8);
        return result;
    }

}
