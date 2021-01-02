/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.actions.CryptAction;
import cn.org.y24.entity.CryptEntity;
import cn.org.y24.enums.CryptActionType;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.manager.CryptManager;
import cn.org.y24.utils.CipherAESCryptProcessor;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptManagerTest {
    @Test
    void testCrypt() {
        try {
            final String key = "12345678123456781234567812345678";
            final String WrongKey0 = "12345678123456781234567812345679";
            final String WrongKey1 = "02345678123456781234567812345678";
            final String str = "New Tar AES Crypt Demo String!";
            final CryptAlgorithm cryptAlgorithm = CryptAlgorithm.defaultCrypt;
            final CryptManager manager = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, key));
            final CryptManager manager0 = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, WrongKey0));
            final CryptManager manager1 = new CryptManager(CipherAESCryptProcessor.getInstance(cryptAlgorithm, WrongKey1));
            byte[] input = str.getBytes(StandardCharsets.UTF_8);
            CryptEntity entity = new CryptEntity(input);
            CryptAction action = new CryptAction(CryptActionType.defaultEncrypt, entity);
            manager.execute(action);
            final byte[] bytes = ((CryptEntity) action.getEntity()).getOutput();
            final String out = Base64.getEncoder().encodeToString(bytes);
            System.out.println(out);
            action = new CryptAction(CryptActionType.defaultDecrypt, new CryptEntity(bytes));
            manager.execute(action);
            System.out.println(new String(((CryptEntity) action.getEntity()).getOutput()));

            action = new CryptAction(CryptActionType.defaultDecrypt, new CryptEntity(bytes));
            if (manager0.execute(action))
                System.out.println(new String(((CryptEntity) action.getEntity()).getOutput()));
            action = new CryptAction(CryptActionType.defaultDecrypt, new CryptEntity(bytes));
            if (manager1.execute(action))
                System.out.println(new String(((CryptEntity) action.getEntity()).getOutput()));
        } catch (InvalidPasswordException e) {
            System.err.println(e.getMessage());
        }
    }
}
