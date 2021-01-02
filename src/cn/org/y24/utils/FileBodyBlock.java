/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

public class FileBodyBlock {
    public final static FileBodyBlock nullBlock = new FileBodyBlock(-1);
    private final int id;
    private int checkSum;
    private int version;
    private int padding;
    private byte[] content;

    public FileBodyBlock(int id) {
        this.id = id;
    }

    public boolean isNull() {
        return id == -1;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getId() {
        return id;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public byte[] getContent() {
        return content;
    }
}
