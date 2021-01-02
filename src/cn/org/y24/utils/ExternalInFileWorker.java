/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExternalInFileWorker {
    // rootFile is a regular file.
    public static boolean processSingleFile(File rootFile, List<String> fileNames, CharCountTable countTable) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(rootFile));
            fileNames.add(rootFile.getPath());
            char[] current = new char[NewTarFileSpec.maxBlockLength];
            do {
                final int size = reader.read(current);
                if (size == -1)
                    break;
                final Map<Character, Integer> count = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    final char c = current[i];
                    if (count.containsKey(c)) {
                        count.replace(c, count.get(c) + 1);
                    } else count.put(c, 1);
                }
                countTable.addAll(count);
            } while (true);
            reader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // rootFile is directory.
    public static boolean processAllFiles(File rootFile, List<String> fileNames, CharCountTable countTable) {
        final String[] files = rootFile.list();
        if (files == null)
            return false;
        boolean result = true;
        for (String s : files) {
            File file = new File(rootFile.getPath() + File.separator + s);
            if (file.isDirectory())
                result &= processAllFiles(file, fileNames, countTable);
            else
                result &= processSingleFile(file, fileNames, countTable);
        }
        return result;
    }

}
