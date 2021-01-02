package cn.org.y24.entity;

import cn.org.y24.interfaces.IEntity;
import cn.org.y24.utils.BiTree;
import cn.org.y24.utils.CharCodeTable;
import cn.org.y24.utils.CharCountTable;
import cn.org.y24.utils.HuffmanTreeNode;

import java.util.Map;

public class HuffmanEntity implements IEntity {
    private CharCountTable countTable;
    private CharCodeTable codeTable;
    private BiTree<HuffmanTreeNode> huffmanTree;
    private byte[] input;
    private char[] output;

    public HuffmanEntity(CharCodeTable codeTable) {
        this.codeTable = codeTable;
    }

    public byte[] getInput() {
        return input;
    }

    public void setOutput(char[] output) {
        this.output = output;
    }

    public void setInput(byte[] input) {
        this.input = input;
    }

    public char[] getOutput() {
        return output;
    }

    public BiTree<HuffmanTreeNode> getHuffmanTree() {
        return huffmanTree;
    }

    public void setHuffmanTree(BiTree<HuffmanTreeNode> huffmanTree) {
        this.huffmanTree = huffmanTree;
    }

    public HuffmanEntity(CharCountTable countTable) {
        this.countTable = countTable;
    }

    public CharCountTable getCountTable() {
        return countTable;
    }

    public CharCodeTable getCodeTable() {
        return codeTable;
    }

    public void setCodeTable(CharCodeTable codeTable) {
        this.codeTable = codeTable;
    }

    @Override
    public Map<String, String> toMap() {
        return null;
    }
}
