/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.entity;

import cn.org.y24.enums.CryptAlgorithm;

public class TarMessageEntity {
    public String location;
    public String destination;
    public CryptAlgorithm cryptAlgorithm;
    public String key;
}
