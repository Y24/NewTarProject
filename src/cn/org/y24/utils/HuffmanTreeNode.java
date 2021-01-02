/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

public class HuffmanTreeNode {
    public static final char noLeafChar = '^';
    public static final byte leftByte = 0;
    public static final byte rightByte = 1;
    final private int weigh;
    final private char code;

    public int getWeigh() {
        return weigh;
    }

    public char getCode() {
        return code;
    }

    public HuffmanTreeNode(int weigh, char code) {
        this.weigh = weigh;
        this.code = code;
    }
}
