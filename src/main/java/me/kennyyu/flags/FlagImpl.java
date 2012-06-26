package me.kennyyu.flags;

public class FlagImpl<T> implements Flag<T> {
    private T value;

    public FlagImpl(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
