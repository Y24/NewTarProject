/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.interfaces;

import cn.org.y24.actions.CryptAction;
import cn.org.y24.entity.CryptEntity;
import cn.org.y24.enums.CryptActionType;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.manager.CryptManager;
import cn.org.y24.utils.CipherAESCryptProcessor;

public abstract class BaseFileWorker {
    private final String location;
    private final String credential;
    private final CryptManager cryptManager;
    private final CryptAlgorithm cryptAlgorithm;

    protected BaseFileWorker(String location, CryptAlgorithm cryptAlgorithm, String credential) throws InvalidPasswordException {
        this.location = location;
        this.credential = credential;
        this.cryptAlgorithm = cryptAlgorithm;
        if (cryptAlgorithm == CryptAlgorithm.noCrypt)
            this.cryptManager = null;
        else this.cryptManager = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, credential));

    }

    private byte[] doCryptAction(CryptActionType cryptActionType, byte[] input) {
        if (cryptAlgorithm == CryptAlgorithm.noCrypt)
            return input;
        final var entity = new CryptEntity(input);
        if (!cryptManager.execute(new CryptAction(cryptActionType, entity)))
            return input;
        return entity.getOutput();
    }

    public byte[] encrypt(byte[] input) {
        return doCryptAction(CryptActionType.defaultEncrypt, input);
    }

    public byte encrypt(byte input) {
        final byte[] in = new byte[]{input};
        return doCryptAction(CryptActionType.defaultEncrypt, in)[0];
    }

    public byte[] decrypt(byte[] input) {
        return doCryptAction(CryptActionType.defaultDecrypt, input);
    }

    public byte decrypt(byte input) {
        final byte[] in = new byte[]{input};
        return doCryptAction(CryptActionType.defaultDecrypt, in)[0];
    }

    public CryptAlgorithm getCryptAlgorithm() {
        return cryptAlgorithm;
    }

    public String getLocation() {
        return location;
    }

    public String getCredential() {
        return credential;
    }

    protected abstract boolean openTargetFile();

    public abstract void close();

}
