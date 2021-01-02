/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.enums;

import cn.org.y24.exception.NotImplementException;

public enum CryptAlgorithm {
    noCrypt(0),
    defaultCrypt(1),
    toBeImplemented(2);

    public int getValue() {
        return value;
    }

    private final int value;

    CryptAlgorithm(int value) {
        this.value = value;
    }

    public static CryptAlgorithm valueOf(int value) {
        switch (value) {
            case 0 -> {
                return noCrypt;
            }
            case 1 -> {
                return defaultCrypt;
            }

            default -> {
                return toBeImplemented;
            }
        }
    }

    public static String[] getCryptDescriptionString(CryptAlgorithm algorithm) {
        try {

            switch (algorithm) {
                case defaultCrypt -> {
                    return new String[]{"AES", "AES/CBC/PKCS5Padding"};
                }
                default -> {
                    throw new NotImplementException(algorithm.name());
                }
            }
        } catch (NotImplementException e) {
            System.err.println(e.getMessage());
        }
        return new String[]{"AES", "AES/CBC/PKCS5Padding"};
    }
}
