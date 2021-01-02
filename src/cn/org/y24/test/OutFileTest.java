/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.test;

import cn.org.y24.utils.ExternalOutFileWorker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutFileTest {
    @Test
    void testOut() {
        final String str = "12eqwfrgefwer";
        final List<char[]> data = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            data.add(str.toCharArray());
        }
        assertTrue(ExternalOutFileWorker.writeFile("/home/y24/NewTarWorkPath/newTar", "tes.out", data));
    }
}
