/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.enums.TarAlgorithm;
import cn.org.y24.enums.ZipAlgorithm;

import java.text.ParseException;
import java.util.Date;

public class NewTarHeader {
    private final int majorVersion;
    private final TarAlgorithm tarAlgorithm;
    private final ZipAlgorithm zipAlgorithm;
    private final CryptAlgorithm cryptAlgorithm;
    private final Date createTime;
    private final String owner;

    public int getMajorVersion() {
        return majorVersion;
    }

    public TarAlgorithm getTarAlgorithm() {
        return tarAlgorithm;
    }

    public ZipAlgorithm getZipAlgorithm() {
        return zipAlgorithm;
    }

    public CryptAlgorithm getCryptAlgorithm() {
        return cryptAlgorithm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getOwner() {
        return owner;
    }

    public NewTarHeader(int majorVersion, TarAlgorithm tarAlgorithm, ZipAlgorithm zipAlgorithm, CryptAlgorithm cryptAlgorithm, Date createTime, String owner) {
        this.majorVersion = majorVersion;
        this.tarAlgorithm = tarAlgorithm;
        this.zipAlgorithm = zipAlgorithm;
        this.cryptAlgorithm = cryptAlgorithm;
        this.createTime = createTime;
        this.owner = owner;
    }

    public boolean isNull() {
        return majorVersion == -1;
    }

    static public NewTarHeader defaultHeader(String owner) {
        return new NewTarHeader(0, TarAlgorithm.defaultTar, ZipAlgorithm.defaultZip, CryptAlgorithm.defaultCrypt, new Date(), owner);
    }

    static public NewTarHeader nullHeader() {
        return new NewTarHeader(-1, TarAlgorithm.toBeImplemented, ZipAlgorithm.toBeImplemented, CryptAlgorithm.toBeImplemented, new Date(), "");
    }

}
