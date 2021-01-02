package cn.org.y24.utils;

import java.util.function.Consumer;

public class BiTree<T> {
    private BiTree<T> left, right;
    T value;

    public BiTree(BiTree<T> left, BiTree<T> right, T value) {
        this.left = left;
        this.right = right;
        this.value = value;
    }

    public BiTree(T value) {
        this.left = this.right = null;
        this.value = value;
    }

    public BiTree<T> getLeft() {
        return left;
    }

    public void setLeft(BiTree<T> left) {
        this.left = left;
    }

    public BiTree<T> getRight() {
        return right;
    }

    public void setRight(BiTree<T> right) {
        this.right = right;
    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
