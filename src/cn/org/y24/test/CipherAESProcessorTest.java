/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.enums.CryptActionType;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.utils.CipherAESCryptProcessor;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class CipherAESProcessorTest {
    @Test
    void testCrypt() {
        CipherAESCryptProcessor processor = null;
        try {
            final CryptAlgorithm cryptAlgorithm = CryptAlgorithm.defaultCrypt;
            processor = CipherAESCryptProcessor.getInstance(cryptAlgorithm, "12345678123456781234567812345678");
            String plainText = "NewTar CipherAES test!";
            var en = processor.process(CryptActionType.defaultEncrypt, plainText.getBytes(StandardCharsets.UTF_8));
            System.out.println(Base64.getEncoder().encodeToString(en));
            processor = CipherAESCryptProcessor.getInstance(cryptAlgorithm, "12345678123456781234567812345678");
            var de = processor.process(CryptActionType.defaultDecrypt, en);
            System.out.println(new String(de));
//            assertNotEquals(plainText, new String(de));
        } catch (InvalidPasswordException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }
}