/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.entity;

import cn.org.y24.utils.FileBodyBlock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FileBlockInfo {
    public final static FileBlockInfo nullInfo = new FileBlockInfo("");
    private final String fileName;
    private final List<Integer> blockIds;
    private final List<FileBodyBlock> blocks;

    public FileBlockInfo(String fileName) {
        this.fileName = fileName;
        this.blockIds = new LinkedList<>();
        this.blocks = new LinkedList<>();
    }

    public boolean isNull() {
        return fileName.equals("");
    }

    public String getFileName() {
        return fileName;
    }

    public List<Integer> getBlockIds() {
        return blockIds;
    }

    public List<FileBodyBlock> getBlocks() {
        return blocks;
    }

}
