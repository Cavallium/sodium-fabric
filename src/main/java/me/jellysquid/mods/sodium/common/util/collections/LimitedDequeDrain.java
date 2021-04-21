package me.jellysquid.mods.sodium.common.util.collections;

import java.util.Deque;
import java.util.Iterator;

public class LimitedDequeDrain<T> implements Iterator<T> {
    private final Deque<T> deque;
    private int maxRemaining;

    public LimitedDequeDrain(Deque<T> deque, int limit) {
        this.deque = deque;
        this.maxRemaining = limit;
    }

    @Override
    public boolean hasNext() {
        return maxRemaining > 0 && !this.deque.isEmpty();
    }

    @Override
    public T next() {
        maxRemaining--;
        return this.deque.remove();
    }
}
