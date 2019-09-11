package org.iota.jota.utils;

/**
 * Created by paul on 4/15/17.
 */
public class Pair<S, T> {
    private S low;
    private T hi;

    public Pair(S k, T v) {
        this.low = k;
        this.hi = v;
    }

    public S getLow() {
        return low;
    }

    public T getHi() {
        return hi;
    }
}