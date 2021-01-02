/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.enums;

public enum ZipAlgorithm {
    defaultZip(0),
    toBeImplemented(1);

    public int getValue() {
        return value;
    }

    private final int value;

    public static ZipAlgorithm valueOf(int value) {
        switch (value) {
            case 0 -> {
                return defaultZip;
            }
            default -> {
                return toBeImplemented;
            }
        }
    }

    ZipAlgorithm(int value) {
        this.value = value;
    }
}
