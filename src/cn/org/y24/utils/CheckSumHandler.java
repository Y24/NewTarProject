/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

/// crc16 modbus
public abstract class CheckSumHandler {
    static final private int polynomial = 0x0000a001;

    private CheckSumHandler() {
    }

    public static int process(int crc, byte data) {
        crc ^= data;
        for (int i = 0; i < 8; i++) {
            if ((crc & 0x00000001) == 1) {
                crc >>= 1;
                crc ^= polynomial;
            } else crc >>= 1;
        }
        return crc;
    }

    public static int process(byte[] data) {
        int crc = 0x0000ffff;
        for (var each : data) {
            crc ^= each;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x00000001) == 1) {
                    crc >>= 1;
                    crc ^= polynomial;
                } else crc >>= 1;
            }
        }
        return crc;
    }

}
