/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.enums.CryptActionType;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.interfaces.ICryptProcess;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CipherAESCryptProcessor implements ICryptProcess {
    private String transformation;
    private String algorithm;
    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;
    private SecretKey secretKey;

    private CipherAESCryptProcessor(CryptAlgorithm cryptAlgorithm, byte[] key, byte[] iv) {
        try {
            this.algorithm = CryptAlgorithm.getCryptDescriptionString(cryptAlgorithm)[0];
            this.transformation = CryptAlgorithm.getCryptDescriptionString(cryptAlgorithm)[1];
            this.cipher = Cipher.getInstance(transformation);
            this.ivParameterSpec = new IvParameterSpec(iv);
            this.secretKey = new SecretKeySpec(key, algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println("Cannot happen in new CipherAES()!\n");
        }
    }

    /**
     * @param key key must be 32 bytes long!
     * @throws InvalidPasswordException
     */
    static public CipherAESCryptProcessor getInstance(CryptAlgorithm cryptAlgorithm, String key) throws InvalidPasswordException {
        if (key.length() != 32) {
            throw new InvalidPasswordException(key);
        }
        return new CipherAESCryptProcessor(cryptAlgorithm, key.substring(0, 16).getBytes(StandardCharsets.UTF_8), key.substring(16).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public byte[] process(CryptActionType type, byte[] target) throws InvalidPasswordException {
        try {
            cipher.init(type == CryptActionType.defaultEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(target);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            throw new InvalidPasswordException(new String(secretKey.getEncoded()) + new String(ivParameterSpec.getIV()));
        }
    }
}
