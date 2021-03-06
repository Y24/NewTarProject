/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.actions.FileAction;
import cn.org.y24.actions.HuffmanAction;
import cn.org.y24.entity.FileEntity;
import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.enums.FileActionType;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.manager.FileManager;
import cn.org.y24.manager.HuffmanManager;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileManagerTest {
    @Test
    void testReadUnTarFile() {
        final String dir = "/home/y24/NewTarWorkPath";
        String destination = dir + "/en.newtar";
        String dest = dir + "/demo";
        String key = "12345678123456781234567812345678";
        HuffmanManager huffmanManager = new HuffmanManager();

        FileManager fileManager = new FileManager();
        FileEntity fileEntity = new FileEntity(dir);
        FileAction fileAction = new FileAction(FileActionType.readLocalUnTarFile, fileEntity);
        assertTrue(fileManager.execute(fileAction));

        FileEntity newFileEntity = (FileEntity) fileAction.getEntity();
        HuffmanEntity huffmanEntity = new HuffmanEntity(newFileEntity.getCharCountTable());
        HuffmanAction huffmanAction = new HuffmanAction(HuffmanActionType.defaultEncode, huffmanEntity);
        assertTrue(huffmanManager.execute(huffmanAction));
        HuffmanEntity newHuffmanEntity = (HuffmanEntity) huffmanAction.getEntity();
        newFileEntity.setDestination(destination);
        newFileEntity.setCharCodeTable(newHuffmanEntity.getCodeTable());
//        newFileEntity.setAlgorithm(CryptAlgorithm.noCrypt);
        newFileEntity.setAlgorithm(CryptAlgorithm.defaultCrypt);
        newFileEntity.setCredential(key);
        assertTrue(fileManager.execute(new FileAction(FileActionType.writeLocalTarFile, newFileEntity)));
        FileEntity entity1 = new FileEntity(destination);
        entity1.setAlgorithm(CryptAlgorithm.defaultCrypt);
        entity1.setCredential(key);
//        entity1.setAlgorithm(CryptAlgorithm.noCrypt);
        FileAction unTarAction = new FileAction(FileActionType.readLocalTarFile, entity1);
        assertTrue(fileManager.execute(unTarAction));
        FileEntity unTarEntity = (FileEntity) unTarAction.getEntity();
        FileEntity entity2 = new FileEntity(destination, dest + "/a");
        entity2.setFileNameList(unTarEntity.getFileNameList());
        entity2.setCharCodeTable(unTarEntity.getCharCodeTable());
        entity2.setFileBodyBlocks(unTarEntity.getFileBodyBlocks());
        FileAction action = new FileAction(FileActionType.writeLocalUnTarFile, entity2);
        assertTrue(fileManager.execute(action));
        FileEntity entity = (FileEntity) action.getEntity();
    }

    @Test
    void testWriteRemoteTarFile() {
        final FileManager fileManager = new FileManager();
        final var location = "/home/y24/NewTarWorkPath/a.newtar";
        final var destination = "ntfp://localhost:2424/y24/demo.newtar";
        FileAction fileAction = new FileAction(FileActionType.writeRemoteTarFile,
                new FileEntity(location, destination));
        assertTrue(fileManager.execute(fileAction));
    }

    @Test
    void testReadRemoteTarFile() {
        final FileManager fileManager = new FileManager();
        final var destination = "/home/y24/NewTarWorkPath/";
        final var location = "ntfp://localhost:2424/y24/demo.newtar";
        FileAction fileAction = new FileAction(FileActionType.readRemoteTarFile,
                new FileEntity(location, destination));
        assertTrue(fileManager.execute(fileAction));
    }
}
