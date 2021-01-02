/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.interfaces;


import cn.org.y24.enums.CryptActionType;
import cn.org.y24.exception.InvalidPasswordException;

public interface ICryptProcess {
    byte[] process(CryptActionType type, byte[] target) throws InvalidPasswordException;
}
