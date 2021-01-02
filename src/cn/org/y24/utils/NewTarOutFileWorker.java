/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.interfaces.BaseFileWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class NewTarOutFileWorker extends BaseFileWorker {
    static private NewTarOutFileWorker nullWorker;

    static {
        try {
            nullWorker = new NewTarOutFileWorker("", CryptAlgorithm.noCrypt, "");
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        }
    }

    private OutputStream output;
    private NewTarHeader header;

    public boolean isNull() {
        return getLocation().equals("");
    }

    private NewTarOutFileWorker(String location, CryptAlgorithm cryptAlgorithm, String credential) throws InvalidPasswordException {
        super(location, cryptAlgorithm, credential);
    }

    public static NewTarOutFileWorker ofInstance(String location, CryptAlgorithm algorithm, String credential) {
        final File file = new File(location);
        try {
            if (file.exists())
                throw new IOException();
            return new NewTarOutFileWorker(location, algorithm, credential);
        } catch (IOException | InvalidPasswordException e) {
            return nullWorker;
        }
    }

    public NewTarHeader getHeader() {
        return header;
    }

    public void setHeader(NewTarHeader header) {
        this.header = header;
    }

    @Override
    protected boolean openTargetFile() {
        if (output != null) return true;
        final File file = new File(super.getLocation());
        if (file.exists())
            return false;
        try {
            output = new FileOutputStream(file);
            writeBytesDirectly(encrypt(NewTarFileSpec.magicNumberBytes));
            writeBytesDirectly(new byte[]{(byte) getCryptAlgorithm().getValue()});
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void close() {
        if (output != null) {
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
                System.err.println("IOException occurs when calling close in OutWorker");
            }
        }
    }

    private boolean writeStringDirectly(String s) {
        return writeBytes(s.getBytes(StandardCharsets.UTF_8));
    }

    private boolean writeIntDirectly(int target) {
        return writeBytesDirectly(encrypt(DataConverter.intToBytes(target)));
    }

    private boolean writeBytes(byte[] bytes) {
        return writeIntDirectly(bytes.length)
                && writeBytesDirectly(bytes);
    }

    private boolean writeBytesDirectly(byte[] bytes) {
        try {
            output.write(encrypt(bytes));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeHeader(NewTarHeader header) {
        if (!openTargetFile()) return false;
        try {
            if (!writeIntDirectly(header.getMajorVersion())
                    || !writeIntDirectly(header.getTarAlgorithm().getValue())
                    || !writeIntDirectly(header.getZipAlgorithm().getValue())
                    || !writeIntDirectly(header.getCryptAlgorithm().getValue())
                    || !writeStringDirectly(header.getOwner())
                    || !writeStringDirectly(NewTarFileSpec.defaultDateFormat.format(header.getCreateTime())))
                throw new IOException();
            return true;
        } catch (IOException e) {
            System.err.println("IOException occurs when calling writeHeader in OutWorker");
            return false;
        }
    }

    public boolean writeCharCode(CharCodeTable table) {
        if (!writeIntDirectly(table.getTable().size()))
            return false;
        for (var item : table.getTable().entrySet()) {
            if (!writeBytesDirectly(DataConverter.charToByte(item.getKey()))
                    || !writeBytes(DataConverter.listToBytes(item.getValue())))
                return false;
        }
        return true;
    }

    public boolean writeFileNameList(FileNameList list) {
        try {
            if (!writeIntDirectly(list.size()))
                throw new IOException();
            for (var s : list.allEntries()) {
                if (!writeStringDirectly(s.getKey()) || !writeIntDirectly(s.getValue().size()))
                    throw new IOException();
                for (var id : s.getValue()) {
                    if (!writeIntDirectly(id))
                        throw new IOException();
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeOneFileBlock(FileBodyBlock block) {
        try {
            if (!writeIntDirectly(block.getId())
                    || !writeIntDirectly(block.getCheckSum())
                    || !writeIntDirectly(block.getVersion())
                    || !writeIntDirectly(block.getPadding())
                    || !writeBytes(block.getContent())) {
                throw new IOException();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean writeFileBlocks(Collection<FileBodyBlock> blocks) {
        try {
            if (!writeIntDirectly(blocks.size()))
                throw new IOException();
            for (var block : blocks)
                if (!writeOneFileBlock(block))
                    throw new IOException();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NewTarOutFileWorker
                && ((NewTarOutFileWorker) obj).getLocation().equals(this.getLocation())
                && ((NewTarOutFileWorker) obj).getCredential().equals(this.getCredential());
    }
}
