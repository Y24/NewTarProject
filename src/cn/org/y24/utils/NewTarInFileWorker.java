/*
 * Copyright (c) 2020.
 * Author: Y24.
 * All Rights Reserved.
 */

package cn.org.y24.utils;

import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.enums.TarAlgorithm;
import cn.org.y24.enums.ZipAlgorithm;
import cn.org.y24.exception.InvalidPasswordException;
import cn.org.y24.interfaces.BaseFileWorker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;

public class NewTarInFileWorker extends BaseFileWorker {
    private InputStream input;
    static private NewTarInFileWorker emptyWorker;

    static {
        try {
            emptyWorker = new NewTarInFileWorker("", CryptAlgorithm.noCrypt, "");
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        }
    }

    private NewTarInFileWorker(String location, CryptAlgorithm cryptAlgorithm, String credential) throws InvalidPasswordException {
        super(location, cryptAlgorithm, credential);
    }

    public boolean isNull() {
        return getLocation().equals("");
    }

    public static NewTarInFileWorker ofInstance(String location, String credential) {
        final File file = new File(location);
        if (!file.exists() || !file.isFile())
            return emptyWorker;
        try {
            final InputStream input = new FileInputStream(file);
            byte[] magicField = new byte[NewTarFileSpec.magicNumberBytes.length];
            if (input.read(magicField) < NewTarFileSpec.magicNumberBytes.length
                    || !NewTarFileSpec.magicNumber.equals(new String(magicField)))
                return emptyWorker;

            final CryptAlgorithm cryptAlgorithm = CryptAlgorithm.valueOf(input.read());
            input.close();
            return NewTarFileSpec.isValidCredential(cryptAlgorithm, new String(magicField), credential)
                    ? new NewTarInFileWorker(location, cryptAlgorithm, credential) : emptyWorker;
        } catch (IOException | InvalidPasswordException e) {
            return emptyWorker;
        }
    }

    public FileNameList getFileNameList() {
        final FileNameList list = FileNameList.ofInstance();
        try {
            final int totalSize = getIntDirectly();
            if (totalSize < 0)
                throw new IOException();
            for (int i = 0; i < totalSize; i++) {
                final String name = getStringDirectly();
                if (name == null)
                    throw new IOException();
                final int blockCount = getIntDirectly();
                List<Integer> blockIdSet = new LinkedList<>();
                for (int j = 0; j < blockCount; j++) {
                    final int current = getIntDirectly();
                    if (current < 0)
                        throw new IOException();
                    blockIdSet.add(current);
                }
                list.addAll(name, blockIdSet);
            }
            return list;
        } catch (IOException e) {
            return FileNameList.nullList;
        }
    }

    public CharCodeTable getCharCodeTable() {
        CharCodeTable codeTable = CharCodeTable.ofInstance();
        try {
            final int count = getIntDirectly();
            if (count < 0)
                throw new IOException();
            for (int i = 0; i < count; i++) {
                // each char has 2 bytes.
                final byte[] c = getBytesDirectly(2);
                final byte[] charCode = getBytes();
                if (c == null || charCode == null)
                    throw new IOException();
                codeTable.add(DataConverter.byteToChar(c), DataConverter.bytesToList(charCode));
            }
            return codeTable;
        } catch (IOException e) {
            return CharCodeTable.nullTable;
        }

    }

    private byte[] getBytesDirectly(int count) {
        if (count < 1)
            return null;
        try {
            final byte[] buffer = new byte[count];
            if (input.read(buffer, 0, count) < count)
                return null;
            return decrypt(buffer);
        } catch (IOException e) {
            return null;
        }
    }

    private byte[] getBytes() {

        try {
            final int count = getIntDirectly();
            if (count < 1)
                return null;
            final byte[] buffer = new byte[count];
            if (input.read(buffer, 0, count) < count)
                return null;
            return decrypt(buffer);
        } catch (IOException e) {
            return null;
        }
    }

    private int getIntDirectly() {
        final var result = getBytesDirectly(4);
        if (result == null)
            return -1;
        return DataConverter.bytesToInt(result);
    }

    private String getStringDirectly() {
        final byte[] bytes = getBytes();
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    public FileBodyBlock getOneFileBlock() {
        try {
            final int id = getIntDirectly();
            final int checkSum = getIntDirectly();
            final int version = getIntDirectly();
            final var padding = getIntDirectly();
            if (id < 0 || version < 0 || padding < 0) {
                throw new IOException();
            }
            final byte[] buffer = getBytes();
            if (buffer == null)
                throw new IOException();
            return new FileBodyBlock(id) {{
                this.setContent(buffer);
                this.setCheckSum(checkSum);
                this.setVersion(version);
                this.setPadding(padding);
                this.setContent(buffer);
            }};
        } catch (IOException e) {
            return FileBodyBlock.nullBlock;
        }
    }

    public Collection<FileBodyBlock> getFileBlocks(int count) {
        if (count < 0) return null;
        final Collection<FileBodyBlock> result = new LinkedList<>();
        try {
            for (int i = 0; i < count; i++) {
                final var current = getOneFileBlock();
                if (current.isNull())
                    throw new IOException();
                result.add(current);
            }
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    public int getFileBlockCount() {
        return getIntDirectly();
    }

    public NewTarHeader getHeader() {
        /* if (!writeIntDirectly(header.getMajorVersion())
                    || !writeIntDirectly(header.getTarAlgorithm().getValue())
                    || !writeIntDirectly(header.getZipAlgorithm().getValue())
                    || !writeStringDirectly(header.getOwner())
                    || !writeStringDirectly(NewTarFileSpec.defaultDateFormat.format(header.getCreateTime())))
  */
        if (!openTargetFile())
            return NewTarHeader.nullHeader();
        try {
            final var majorVersion = getIntDirectly();
            final var tarAlgorithm = TarAlgorithm.valueOf(getIntDirectly());
            final var zipAlgorithm = ZipAlgorithm.valueOf(getIntDirectly());
            final var cryptAlgorithm = CryptAlgorithm.valueOf(getIntDirectly());
            final var owner = getStringDirectly();
            final var time = NewTarFileSpec.defaultDateFormat.parse(getStringDirectly());
            if (majorVersion < 0
                    || tarAlgorithm == TarAlgorithm.toBeImplemented
                    || zipAlgorithm == ZipAlgorithm.toBeImplemented
                    || cryptAlgorithm == CryptAlgorithm.toBeImplemented)
                throw new IOException();
            return new NewTarHeader(majorVersion, tarAlgorithm, zipAlgorithm, cryptAlgorithm, time, owner);
        } catch (IOException | ParseException e) {
            return NewTarHeader.nullHeader();
        }
    }

    @Override
    protected boolean openTargetFile() {
        if (input != null) return true;
        final File file = new File(super.getLocation());
        if (!file.exists() || !file.isFile())
            return false;
        try {
            input = new FileInputStream(file);
            // with an additional cryptAlgorithm byte.
            input.skipNBytes(NewTarFileSpec.magicNumberBytes.length + 1);
            return true;
        } catch (IOException e) {
            System.err.println("Cannot happen in openTargetFile of inWorker!");
            return false;
        }
    }

    @Override
    public void close() {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                System.err.println("IOException occurs when calling close in inWorker");
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NewTarInFileWorker
                && ((NewTarInFileWorker) obj).getLocation().equals(this.getLocation())
                && ((NewTarInFileWorker) obj).getCredential().equals(this.getCredential());
    }
}
