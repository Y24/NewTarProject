/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.exception;

public class InvalidPasswordException extends Exception {
    @Override
    public String getMessage() {
        return message;
    }

    private final String message;

    public InvalidPasswordException(String password) {
        super();
        this.message = String.format("Password %s is invalid!", password);
    }
}
