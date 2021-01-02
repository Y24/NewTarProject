package cn.org.y24.manager;

import cn.org.y24.actions.HuffmanAction;
import cn.org.y24.entity.HuffmanEntity;
import cn.org.y24.enums.HuffmanActionType;
import cn.org.y24.interfaces.IManager;
import cn.org.y24.interfaces.IType;
import cn.org.y24.utils.*;

import java.util.*;
import java.util.stream.Collectors;

// do Huffman coding, but only encode, leaving decoding to FileManager.
public class HuffmanManager implements IManager<HuffmanAction> {

    @Override
    public boolean execute(HuffmanAction huffmanAction) {
        final var entity = (HuffmanEntity) huffmanAction.getEntity();
        IType actionType = huffmanAction.getType();
        if (actionType == HuffmanActionType.defaultEncode) {
            final BiTree<HuffmanTreeNode> root = buildHuffmanTree(entity.getCountTable());
            entity.setHuffmanTree(root);
            entity.setCodeTable(getHuffmanCode(root));
        } else if (actionType == HuffmanActionType.defaultDecode) {
            final var tree = restoreHuffmanTree(entity.getCodeTable());
            char[] output = decodeToChars(tree, entity.getInput());
            if (output == null)
                return false;
            entity.setOutput(output);
            entity.setHuffmanTree(tree);
        }
        return true;
    }

    public static boolean isEqualTree(BiTree<HuffmanTreeNode> treeA, BiTree<HuffmanTreeNode> treeB) {
        if (treeA == null && treeB == null)
            return true;
        if (treeA == null || treeB == null)
            return false;
        if (treeA.isLeaf() && treeB.isLeaf() && treeA.getValue().getCode() == treeB.getValue().getCode()) {
            return true;
        }
        if (treeA.isLeaf() && !treeB.isLeaf() || !treeA.isLeaf() && treeB.isLeaf())
            return false;
        return isEqualTree(treeA.getLeft(), treeB.getLeft()) && isEqualTree(treeA.getLeft(), treeB.getRight());
    }

    public static void preTravelLeaves(BiTree<HuffmanTreeNode> tree) {
        if (tree == null)
            return;
        if (tree.isLeaf())
            System.out.println(tree.getValue().getCode());
        preTravelLeaves(tree.getLeft());
        preTravelLeaves(tree.getRight());
    }

    private char[] decodeToChars(BiTree<HuffmanTreeNode> tree, byte[] input) {

        if (tree == null)
            return null;
        final List<Character> result = new LinkedList<>();
        var current = tree;
        for (var code : input) {
            if (current.isLeaf()) {
                result.add(current.getValue().getCode());
                current = tree;
            }
            current = code == HuffmanTreeNode.leftByte ? current.getLeft() : current.getRight();
            if (current == null) {
                return null;
            }
        }
        if (!current.isLeaf())
            return null;
        result.add(current.getValue().getCode());
        return DataConverter.listToChars(result);
    }

    private BiTree<HuffmanTreeNode> restoreHuffmanTree(CharCodeTable codeTable) {
        final var root = new BiTree<>(new HuffmanTreeNode(0, HuffmanTreeNode.noLeafChar));
        var current = root;
        for (var each : codeTable.getTable().entrySet()) {
            final char c = each.getKey();
            for (var code : each.getValue()) {
                if (code == HuffmanTreeNode.leftByte) {
                    if (current.getLeft() == null)
                        current.setLeft(new BiTree<>(new HuffmanTreeNode(0, HuffmanTreeNode.noLeafChar)));
                    current = current.getLeft();
                } else if (code == HuffmanTreeNode.rightByte) {
                    if (current.getRight() == null)
                        current.setRight(new BiTree<>(new HuffmanTreeNode(0, HuffmanTreeNode.noLeafChar)));
                    current = current.getRight();
                } else return null;
            }
            current.setValue(new HuffmanTreeNode(0, c));
            current = root;
        }
        return root;
    }

    private CharCodeTable getHuffmanCode(BiTree<HuffmanTreeNode> root) {
        final CharCodeTable codeTable = CharCodeTable.ofInstance();
        final Queue<BiTree<HuffmanTreeNode>> workQueue = new LinkedList<>();
        final Queue<List<Byte>> encodeQueue = new LinkedList<>();
        workQueue.add(root);
        encodeQueue.add(new LinkedList<>());
        while (!workQueue.isEmpty()) {
            var current = workQueue.poll();
            var code = encodeQueue.poll();
            if (current.getLeft() == null && current.getRight() == null) {
                codeTable.add(current.getValue().getCode(), code);
                continue;
            }
            if (current.getLeft() != null) {
                workQueue.add(current.getLeft());
                assert code != null;
                var leftCode = new LinkedList<>(code);
                leftCode.add(HuffmanTreeNode.leftByte);
                encodeQueue.add(new LinkedList<>(leftCode));
            }
            if (current.getRight() != null) {
                workQueue.add(current.getRight());
                assert code != null;
                var rightCode = new LinkedList<>(code);
                rightCode.add(HuffmanTreeNode.rightByte);
                encodeQueue.add(new LinkedList<>(rightCode));
            }
        }
        return codeTable;
    }

    private BiTree<HuffmanTreeNode> buildHuffmanTree(CharCountTable charCountTable) {
        final PriorityQueue<BiTree<HuffmanTreeNode>> huffmanQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.getValue().getWeigh()));
        huffmanQueue.addAll(charCountTable.getTable().entrySet().stream().map(characterIntegerEntry -> new BiTree<>(new HuffmanTreeNode(characterIntegerEntry.getValue(), characterIntegerEntry.getKey()))).collect(Collectors.toList()));
        while (huffmanQueue.size() > 1) {
            final var first = huffmanQueue.poll();
            final var second = huffmanQueue.poll();
            assert second != null;
            huffmanQueue.add(new BiTree<>(first, second, new HuffmanTreeNode(first.getValue().getWeigh() + second.getValue().getWeigh(), HuffmanTreeNode.noLeafChar)));
        }
        return huffmanQueue.poll();
    }
}
