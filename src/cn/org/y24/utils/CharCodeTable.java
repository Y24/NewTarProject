/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharCodeTable {
    private final Map<Character, List<Byte>> table;
    public static final CharCodeTable nullTable = new CharCodeTable(null);

    private CharCodeTable(Map<Character, List<Byte>> table) {
        this.table = table;
    }

    static public CharCodeTable ofInstance() {
        return new CharCodeTable(new HashMap<>());
    }

    public Map<Character, List<Byte>> getTable() {
        return table;
    }

    public boolean add(char target, List<Byte> charCode) {
        return table.putIfAbsent(target, charCode) == null;
    }

    public boolean addAll(Map<Character, List<Byte>> target) {
        AtomicBoolean result = new AtomicBoolean(true);
        target.forEach((character, s) -> result.set(result.get() || table.putIfAbsent(character, s) == null));
        return result.get();
    }

    public boolean delete(char target) {
        return table.remove(target) != null;
    }

    public void clear() {
        table.clear();
    }

    public boolean isNull() {
        return table == null;
    }
}
