/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import java.util.HashMap;
import java.util.Map;

public class CharCountTable {
    final private Map<Character, Integer> table;

    private CharCountTable(Map<Character, Integer> table) {
        this.table = table;
    }

    public static CharCountTable ofInstance() {
        return new CharCountTable(new HashMap<>());
    }

    public Map<Character, Integer> getTable() {
        return table;
    }

    public void addOne(char target) {
        add(target, 1);
    }

    public void add(char target, int count) {
        table.putIfAbsent(target, 0);
        table.put(target, table.get(target) + count);
    }

    public void addAll(Map<Character, Integer> target) {
        target.forEach(this::add);
    }

    public void clear() {
        table.clear();
    }
}
