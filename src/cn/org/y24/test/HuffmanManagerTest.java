/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.actions.HuffmanAction;
import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.manager.HuffmanManager;
import cn.org.y24.utils.CharCodeTable;
import cn.org.y24.utils.CharCountTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanManagerTest {
    @Test
    void testExecute() {
        final HuffmanManager manager = new HuffmanManager();
        final CharCountTable countTable = CharCountTable.ofInstance();
        final int[] counts = new int[]{2, 3, 5, 1, 7, 2, 7, 9, 6, 3};
        for (int i = 0; i < counts.length; i++) {
            countTable.add((char) (i + 'a'), counts[i]);
        }
    }
}