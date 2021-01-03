/*
 * Copyright (c) 2019.
 *  Author: Y24
 *  All rights reserved.
 */

package cn.org.y24.ui.framework;

public class Deliverer {
    public static int broadcastFlag = 0;
    private int senderHashCode;
    private int receiverHashCode;
    private Object message;

    public Deliverer(int senderHashCode,
                     int receiverHashCode,
                     Object message) {
        this.senderHashCode = senderHashCode;
        this.receiverHashCode = receiverHashCode;
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getReceiverHashCode() {
        return receiverHashCode;
    }

    public void setReceiverHashCode(int receiverHashCode) {
        this.receiverHashCode = receiverHashCode;
    }

    public int getSenderHashCode() {
        return senderHashCode;
    }

    public void setSenderHashCode(int senderHashCode) {
        this.senderHashCode = senderHashCode;
    }
}
