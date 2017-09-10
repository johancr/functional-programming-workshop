package com.kambi.func.examples;

@FunctionalInterface
public interface SideEffect<T> {

    void run(T value);
}