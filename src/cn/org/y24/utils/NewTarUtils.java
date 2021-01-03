/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.actions.FileAction;
import cn.org.y24.actions.HuffmanAction;
import cn.org.y24.entity.FileEntity;
import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.enums.FileActionType;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.manager.FileManager;
import cn.org.y24.manager.HuffmanManager;

public class NewTarUtils {
    public static boolean tar(String location, String destination, CryptAlgorithm cryptAlgorithm, String key) {
        try {
            final FileManager fileManager = new FileManager();
            final FileEntity fileEntity = new FileEntity(location);
            final FileAction fileAction = new FileAction(FileActionType.readLocalUnTarFile, fileEntity);
            if (!fileManager.execute(fileAction))
                return false;
            final FileEntity result = (FileEntity) fileAction.getEntity();
            final HuffmanManager huffmanManager = new HuffmanManager();
            final HuffmanAction huffmanAction = new HuffmanAction(HuffmanActionType.defaultEncode, new HuffmanEntity(result.getCharCountTable()));
            if (!huffmanManager.execute(huffmanAction))
                return false;
            result.setDestination(destination);
            result.setAlgorithm(cryptAlgorithm);
            result.setCredential(key);
            result.setCharCodeTable(((HuffmanEntity) huffmanAction.getEntity()).getCodeTable());
            final FileAction newAction = new FileAction(FileActionType.writeLocalTarFile, result);
            return fileManager.execute(newAction);
        } catch (Exception e) {
            System.err.println("NewTarUtils.tar() Exception: " + e.toString());
            return false;
        }
    }

    public static boolean unTar(String location, String destination, String key) {
        try {
            final FileManager fileManager = new FileManager();
            final FileEntity fileEntity = new FileEntity(location);
            fileEntity.setCredential(key);
            final FileAction fileAction = new FileAction(FileActionType.readLocalTarFile, fileEntity);
            if (!fileManager.execute(fileAction))
                return false;
            final FileEntity result = (FileEntity) fileAction.getEntity();
            result.setDestination(destination);
            final FileAction newAction = new FileAction(FileActionType.writeLocalUnTarFile, result);
            return fileManager.execute(newAction);
        } catch (Exception e) {
            System.err.println("NewTarUtils.unTar() Exception: " + e.toString());
            return false;
        }
    }
}
