package org.iota.jota.utils;

/**
 * Created by paul on 4/15/17.
 */
public class Pair<S, T> {
    private S left;
    private T right;

    public Pair(S k, T v) {
        this.left = k;
        this.right = v;
    }

    public S getLeft() {
        return left;
    }

    public T getRight() {
        return right;
    }
}