/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.enums;

public enum TarAlgorithm {
    defaultTar(0),
    toBeImplemented(1);

    public int getValue() {
        return value;
    }

    private final int value;

    public static TarAlgorithm valueOf(int value) {
        switch (value) {
            case 0 -> {
                return defaultTar;
            }
            default -> {
                return toBeImplemented;
            }
        }
    }

    TarAlgorithm(int value) {
        this.value = value;
    }
}
