/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.entity.FileBlockInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class ExternalOutFileWorker {
    public static FileBlockInfo getFileBlockInfo(String rootDir, String location, int globalId, CharCodeTable codeTable) {
        try {
            final var root = rootDir + (rootDir.endsWith(File.separator) ? "" : File.separator);
            File file = new File(root + location);
            if (!file.exists() || file.isDirectory())
                throw new IOException();
            final FileBlockInfo info = new FileBlockInfo(location);
            final var blocks = info.getBlocks();
            final var blockIds = info.getBlockIds();
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            char[] current = new char[NewTarFileSpec.maxBlockLength];
            final BitByteFactory factory = BitByteFactory.ofInstance();
            do {
                final int count = reader.read(current);
                if (count == -1)
                    break;

                final List<Byte> data = new ArrayList<>(count * 8);
                for (int i = 0; i < count; i++) {
                    final var code = codeTable.getTable().get(current[i]);
                    if (code == null) {
                        throw new IOException();
                    } else data.addAll(code);
                }
                if (!factory.acceptBits(data))
                    throw new IOException();
                int id = globalId++;
                blockIds.add(id);
                blocks.add(new FileBodyBlock(id) {{
                    this.setPadding(factory.getPadding());
                    this.setVersion(NewTarFileSpec.defVersion);
                    final var content = DataConverter.listToBytes(factory.result());
                    this.setCheckSum(CheckSumHandler.process(content));
                    this.setContent(content);
                }});
            } while (true);
            return info;
        } catch (IOException e) {
            return FileBlockInfo.nullInfo;
        }
    }

    public static List<FileBlockInfo> getAllFileBlockInfo(String rootDir, List<String> fileNames, CharCodeTable codeTable) {
        final List<FileBlockInfo> result = new LinkedList<>();
        int globalId = 0;
        for (var each : fileNames) {
            final var info = getFileBlockInfo(rootDir, each, globalId, codeTable);
            globalId += info.getBlockIds().size();
            if (info.isNull()) {
                result.clear();
                break;
            }
            result.add(info);
        }
        return result;
    }

    public static boolean writeFile(String rootDir, String location, List<char[]> data) {
        try {
            rootDir = rootDir + (rootDir.endsWith(File.separator) ? "" : File.separator);
            new File(rootDir).mkdir();
            var paths = location.split(File.separator);
            var fileName = paths[paths.length - 1];
            final StringBuilder current = new StringBuilder(rootDir.substring(0, rootDir.length() - 1));
            for (int i = 0; i < paths.length - 1; i++) {
                current.append(File.separator).append(paths[i]);
                new File(current.toString()).mkdir();
            }
            File file = new File(current.append(File.separator).append(fileName).toString());
            if (!file.createNewFile())
                throw new IOException();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (var each : data)
                writer.write(each);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
