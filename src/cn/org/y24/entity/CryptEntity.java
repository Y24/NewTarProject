package cn.org.y24.entity;

import cn.org.y24.interfaces.IEntity;

import java.util.Map;

public class CryptEntity implements IEntity {
    private final byte[] input;
    private byte[] output;

    public CryptEntity(byte[] input) {
        this.input = input;
    }

    public byte[] getInput() {
        return input;
    }

    public byte[] getOutput() {
        return output;
    }

    public void setOutput(byte[] output) {
        this.output = output;
    }

    @Override
    public Map<String, String> toMap() {
        return null;
    }
}
