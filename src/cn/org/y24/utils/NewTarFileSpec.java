/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.actions.CryptAction;
import cn.org.y24.entity.CryptEntity;
import cn.org.y24.enums.CryptActionType;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.manager.CryptManager;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class NewTarFileSpec {
    public static final String magicNumber = ">_<$_$^_^?_>";
    public static final byte[] magicNumberBytes = magicNumber.getBytes(StandardCharsets.UTF_8);
    public static final NewTarHeader defaultHeader = NewTarHeader.defaultHeader("~_~");
    public static final int headItemsCount = 6;
    public static final int maxBlockLength = 1024;
    public static final int maxReadCount = 1024;
    public static final int defVersion = 0;
    public static final DateFormat defaultDateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);

    public static boolean isValidCredential(CryptAlgorithm cryptAlgorithm, String magicField, String testKey) {
        if (cryptAlgorithm == CryptAlgorithm.noCrypt)
            return true;
        try {
            final CryptManager manager = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, testKey));
            final CryptEntity entity = new CryptEntity(magicField.getBytes(StandardCharsets.UTF_8));
            if (!manager.execute(new CryptAction(CryptActionType.defaultDecrypt, entity)))
                return false;
            return new String(entity.getOutput()).equals(magicNumber);
        } catch (InvalidPasswordException e) {
            return false;
        }
    }

    public static String encryptMagicField(CryptAlgorithm cryptAlgorithm, String key) {
        if (cryptAlgorithm == CryptAlgorithm.noCrypt)
            return magicNumber;
        try {
            final CryptManager manager = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, key));
            final CryptEntity entity = new CryptEntity(magicNumberBytes);
            if (!manager.execute(new CryptAction(CryptActionType.defaultEncrypt, entity)))
                return "";
            return new String(entity.getOutput());
        } catch (InvalidPasswordException e) {
            return "";
        }
    }
}
