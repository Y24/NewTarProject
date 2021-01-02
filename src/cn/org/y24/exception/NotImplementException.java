/*
 * Copyright (c) 2021.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.exception;

public class NotImplementException extends Exception {
    @Override
    public String getMessage() {
        return message;
    }

    private final String message;

    public NotImplementException(String target) {
        super();
        this.message = String.format("%s is not implemented!", target);
    }
}
