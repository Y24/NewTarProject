package cn.org.y24.entity;

import cn.org.y24.enums.CryptAlgorithm;
import cn.org.y24.interfaces.IEntity;
import cn.org.y24.utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FileEntity implements IEntity {
    public String getLocation() {
        return location;
    }

    public void setHeader(NewTarHeader header) {
        this.header = header;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public void setCharCodeTable(CharCodeTable charCodeTable) {
        this.charCodeTable = charCodeTable;
    }

    public NewTarHeader getHeader() {
        return header;
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setCharCountTable(CharCountTable charCountTable) {
        this.charCountTable = charCountTable;
    }

    public FileEntity(String location, String destination) {
        this.location = location;
        this.destination = destination;
    }

    public FileEntity(String location) {
        this.location = location;
        destination = "";
    }

    final private String location;
    private String destination;

    private NewTarHeader header;
    private List<String> fileNames;
    private FileNameList fileNameList;
    private Collection<FileBodyBlock> fileBodyBlocks;
    private CharCountTable charCountTable;
    private CharCodeTable charCodeTable;
    private String credential;
    private CryptAlgorithm algorithm;

    public CryptAlgorithm getAlgorithm() {
        return algorithm;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setAlgorithm(CryptAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Collection<FileBodyBlock> getFileBodyBlocks() {
        return fileBodyBlocks;
    }

    public void setFileBodyBlocks(Collection<FileBodyBlock> fileBodyBlocks) {
        this.fileBodyBlocks = fileBodyBlocks;
    }

    public FileNameList getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(FileNameList fileNameList) {
        this.fileNameList = fileNameList;
    }

    public CharCountTable getCharCountTable() {
        return charCountTable;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public CharCodeTable getCharCodeTable() {
        return charCodeTable;
    }


    @Override
    public Map<String, String> toMap() {
        return null;
    }
}
