/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import java.util.*;

public class FileNameList {
    static final FileNameList nullList = new FileNameList(false);
    final private boolean validFlag;
    private final Map<String, List<Integer>> pool = new HashMap<>();

    private FileNameList(boolean validFlag) {
        this.validFlag = validFlag;
    }

    public boolean isNull() {
        return !validFlag;
    }

    public static FileNameList ofInstance() {
        return new FileNameList(true);
    }

    public boolean exists(String target) {
        return pool.containsKey(target);
    }

    public boolean delete(String target) {
        if (pool.containsKey(target)) {
            pool.remove(target);
            return true;
        }
        return false;
    }

    public void clear() {
        pool.clear();
    }

    public boolean addAll(String target, List<Integer> blockIds) {
        for (var id : blockIds) {
            if (!add(target, id)) return false;
        }
        return true;
    }

    public boolean add(String target, int blockId) {
        if (pool.containsKey(target)) {
            final var list = pool.get(target);
            if (list.contains(blockId))
                return false;
            list.add(blockId);
        } else {
            pool.put(target, new ArrayList<>() {{
                this.add(blockId);
            }});
        }
        return true;
    }

    public Set<String> allFileNames() {
        return pool.keySet();
    }

    public Set<Map.Entry<String, List<Integer>>> allEntries() {
        return pool.entrySet();
    }

    public List<Integer> get(String target) {
        return pool.get(target);
    }

    public int size() {
        return pool.size();
    }
}
