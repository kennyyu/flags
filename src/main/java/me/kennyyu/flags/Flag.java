package me.kennyyu.flags;

public interface Flag<T> {
    T get();
    void set(T value);
}
